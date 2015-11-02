<%@page import="com.ai.eduportal.Util.Constants"%>
<%@page import="common.ai.tools.JsonUtil"%>
<%@page import="java.util.Map"%>
<%@page import="com.ai.eduportal.service.CmsServiceClient"%>
<%@page import="com.ai.frame.util.SpringContextHelper"%>
<%@page import="common.ai.logger.LoggerFactory"%>
<%@page import="common.ai.logger.Logger"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.ai.frame.web.util.SystemPropUtil"%>
<%@page import="common.ai.encryption.tools.EncryptionUtil"%>
<%@page import="com.ai.eduportal.bean.User"%>
<%@page import="common.ai.tools.StringUtil"%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@page import="java.net.URLEncoder"%>
<%
    Logger logger = LoggerFactory.getCustomerLog(this.getClass(),"heEdulog");
    logger.info("", "{}forward start ...");
	String userId = request.getParameter("userId");
	String goUrl  = request.getParameter("orderUrl");
	
	String sysType= request.getParameter("sysType");
	String isMock = request.getParameter("isMock");
	
	logger.info(isMock, "forward is mock {}.");
	//获取当前用户对应子平台用户信息
	int stype = StringUtil.str2Int(sysType);
	String sysRoleId = null;
	String orgId  = null;
	if("1".equals(isMock)){
		sysRoleId = request.getParameter("roleId");
		orgId  = request.getParameter("orgId");
	}else{
		Object loginuser = request.getSession().getAttribute(User.CURRENT_USER);
		String mobile = "";
        if(loginuser instanceof User){
            User luser = (User)loginuser;
            sysRoleId  = String.valueOf(luser.getTypeid());
            orgId      = String.valueOf(luser.getOrgId());
            userId     = String.valueOf(luser.getUserId());
            mobile     = String.valueOf(luser.getMobile());
            sysRoleId  = User.convertHeRole(sysRoleId);
            logger.info(sysRoleId, "user type is {}.");
            
            //如果是学生角色，获取家长用户id的信息,用户类型变为家长,但是组织信息还是选取学生的组织信息
            if(Constants.CRM_USER_TYPE.STUDENT.equals(sysRoleId)){
            	sysRoleId   = Constants.CRM_USER_TYPE.PARENT;//用户类型变为家长
            	Object puser = request.getSession().getAttribute(User.PARENTLOGINU);
            	if(puser instanceof Map){
            		Map pmuser  = (Map)puser;
            		User pluser = (User)pmuser.get(sysRoleId);
            		if(pluser!=null){
            			userId      = String.valueOf(pluser.getUserId());//家长用户id
                        
                        logger.info(userId, "user type is student and his parent user id is {}.");
            		}else{
            			logger.info(userId, "cant't get user [{}] 's parent user info.");
            		}
            	}else{
            		logger.info(userId, "the user [{}] 's parent map is null.");
            	}
            }
        }
        //获取订购产品的url
        String fullPath = SystemPropUtil.getString("edu.full.path");
        String nodeUid = request.getParameter("nodeUid");
        CmsServiceClient cmsService = (CmsServiceClient)SpringContextHelper.getBean("cmsClient", CmsServiceClient.class);
        String appDetail = cmsService.getAppDetailOriginal(nodeUid);
        Map appDetailMp = (Map)JsonUtil.convertJson2Object(appDetail, Map.class);
        if(appDetailMp!=null && appDetailMp.size()>0){
        	goUrl = (String)appDetailMp.get("apphref");
        }else{
        	goUrl = "";
        }
        
        if(!"3".equals(sysType)){
        	if(goUrl.indexOf("?")>-1){
                goUrl = goUrl + "&roleId="+sysRoleId;
            }else if(!StringUtil.isEmpty(goUrl)){
                goUrl = goUrl + "?roleId="+sysRoleId;
            }
        }
		if(!"1".equals(sysType)){//非校讯通的用户
			Object userRoles = request.getSession().getAttribute(User.USER_LIST);
			logger.info(sysType, "not xxt user jumped {}.");
			if(loginuser instanceof User){
				User cuser = (User)loginuser;
				int ustype  = cuser.getTypeid();
				String cuid  = cuser.getUserId();
				User roleUser = User.getSysTypeRoleUser(userRoles,stype,ustype,cuid);
				if(roleUser!=null){
	                userId = String.valueOf(roleUser.getOtherUserId());
	                orgId  = String.valueOf(roleUser.getOrgId());
	                logger.info(userId, "not xxt user id is {}.");
	                logger.info(orgId,  "not xxt user orgId is {}.");
	            }else{
	                userId = "0";
	                orgId  = "0";
	            }
			}
			
		}
	}
	
	
    String roleId = sysRoleId;
	String channelType = sysType;
	String aessalt = SystemPropUtil.getString("edu.aes.salt."+channelType);
	String aeskey  = SystemPropUtil.getString("edu.aes.key."+channelType);
	String formUrl = SystemPropUtil.getString("edu.aes.url."+channelType);
	
	long time = Calendar.getInstance().getTime().getTime();
	
	String orign = userId + "," + roleId + "," + aeskey + "," + time;
	byte[] encode    = EncryptionUtil.aesEncode(orign.getBytes("UTF-8"), aeskey.getBytes(), aessalt);
	String encodeStr = EncryptionUtil.encodeStrBase64(encode);
	
	logger.info(channelType,"-->the forward channelType param is {}.");
	logger.info(aessalt,    "-->the forward aessalt param is {}.");
	logger.info(aeskey,     "-->the forward aeskey param is {}.");
	logger.info(userId,     "-->the forward id param is {}.");
	logger.info(roleId,     "-->the forward role param is {}.");
	logger.info(goUrl,      "-->the forward url param is {}.");
	logger.info(orgId,      "-->the forward orgId param is {}.");
	logger.info(orign,      "-->the forward orign string is {}.");
	logger.info(encodeStr,  "-->the forward accesstoken param is {}.");
	logger.info(formUrl,    "-->the forward formUrl param is {}.");
	logger.info(sysType,    "-->the forward sysType param is {}.");
%>
<html>
<body onload="document.forms[0].submit()">
<form action="<%=formUrl %>" method="post">
	<input type="hidden" name="id"    value="<%=userId %>">
	<input type="hidden" name="role"  value="<%=roleId %>">
	<input type="hidden" name="url"   value="<%=goUrl%>">
	<input type="hidden" name="orgId" value="<%=orgId %>">
	<input type="hidden" name="accesstoken" value="<%=encodeStr %>">
</form>
</body>
</html>
