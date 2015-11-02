<%@page import="com.ai.eduportal.Util.Constants"%>
<%@page import="common.ai.tools.JsonUtil"%>
<%@page import="java.util.Map"%>
<%@page import="com.ai.frame.util.SpringContextHelper"%>
<%@page import="common.ai.logger.LoggerFactory"%>
<%@page import="common.ai.logger.Logger"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.ai.frame.web.util.SystemPropUtil"%>
<%@page import="common.ai.encryption.tools.EncryptionUtil"%>
<%@page import="common.ai.tools.StringUtil"%>
<%@ page contentType="text/html;charset=utf-8" %>
<%@page import="java.net.URLEncoder"%>
<%
    Logger logger = LoggerFactory.getActionLog(this.getClass());
    logger.info("", "{}forward start ...");
	String userId = request.getParameter("userId");
	String goUrl  = request.getParameter("goUrl");
	
	String sysType= "1";
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
		logger.info("", "-----------forward is not mock.");
		sysRoleId = request.getParameter("sysRoleId");
		orgId = request.getParameter("orgId");
		
		
		/****
		HttpServletRequest mrequest = ServletActionContext.getRequest();
		Object loginuser = mrequest.getSession().getAttribute(HEAUser.HEA_CURRENT_USER);
		logger.info(loginuser.getClass().getName(),"===========the forward channelType param is {}.");
        if(loginuser instanceof HEAUser){
        	HEAUser luser = (HEAUser)loginuser;
        	
            sysRoleId  = String.valueOf(luser.getTypeId());
            orgId      = String.valueOf(luser.getOrgId());
            userId     = String.valueOf(luser.getUserId());
            
            sysRoleId  = User.convertHeRole(sysRoleId);

            //如果是学生角色，获取家长用户id的信息,用户类型变为家长,但是组织信息还是选取学生的组织信息
            if(Constants.CRM_USER_TYPE.STUDENT.equals(sysRoleId)){
            	sysRoleId   = Constants.CRM_USER_TYPE.PARENT;//用户类型变为家长
            	Object puser = mrequest.getSession().getAttribute(HEAUser.HEA_PT_USER);
            	if(puser instanceof HEAUser){
            		HEAUser pluser = (HEAUser)puser;
            		userId      = String.valueOf(pluser.getUserId());//家长用户id
            	}
            }
        }
		*****/
	}
	
	
    String roleId = sysRoleId;
	String channelType = sysType;
	String aessalt = SystemPropUtil.getString("edu.aes.salt."+channelType);
	String aeskey  = SystemPropUtil.getString("edu.aes.key."+channelType);
	String formUrl= SystemPropUtil.getString("edu.aes.url."+channelType);
	
	long time = Calendar.getInstance().getTime().getTime();
	
	String orign = userId + "," + roleId + "," + aeskey + "," + time;
	byte[] encode    = EncryptionUtil.aesEncode(orign.getBytes("UTF-8"), aeskey.getBytes(), aessalt);
	String encodeStr = EncryptionUtil.encodeStrBase64(encode);
	
	logger.info(channelType,"the forward channelType param is {}.");
	logger.info(aessalt,    "the forward aessalt param is {}.");
	logger.info(aeskey,     "the forward aeskey param is {}.");
	logger.info(userId,     "the forward userid param is {}.");
	logger.info(roleId,     "the forward roleId param is {}.");
	logger.info(goUrl,      "the forward goUrl param is {}.");
	logger.info(orgId,      "the forward orgId param is {}.");
	logger.info(encodeStr,  "the forward encodeStr param is {}.");
	logger.info(formUrl,    "the forward formUrl param is {}.");
	logger.info(sysType,    "the forward sysType param is {}.");
%>
<html>
<body onload="document.forms[0].submit()">
<form action="<%=formUrl %>" method="post">
	<input type="hidden" name="id"    value="<%=userId %>">
	<input type="hidden" name="role"  value="<%=roleId %>">
	<input type="hidden" name="url"   value="<%=goUrl%>">
	<input type="hidden" name="orgId" value="<%=orgId %>">
	<input type="hidden" name="accesstoken" value="<%=encodeStr%>">
</form>
</body>
</html>
