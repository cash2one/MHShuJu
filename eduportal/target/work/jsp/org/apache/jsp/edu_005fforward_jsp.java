package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.ai.eduportal.Util.Constants;
import common.ai.tools.JsonUtil;
import java.util.Map;
import com.ai.eduportal.service.CmsServiceClient;
import com.ai.frame.util.SpringContextHelper;
import common.ai.logger.LoggerFactory;
import common.ai.logger.Logger;
import java.util.Calendar;
import com.ai.frame.web.util.SystemPropUtil;
import common.ai.encryption.tools.EncryptionUtil;
import com.ai.eduportal.bean.User;
import common.ai.tools.StringUtil;
import java.net.URLEncoder;

public final class edu_005fforward_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static java.util.Vector _jspx_dependants;

  private org.apache.jasper.runtime.ResourceInjector _jspx_resourceInjector;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html;charset=utf-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      String resourceInjectorClassName = config.getInitParameter("com.sun.appserv.jsp.resource.injector");
      if (resourceInjectorClassName != null) {
        _jspx_resourceInjector = (org.apache.jasper.runtime.ResourceInjector) Class.forName(resourceInjectorClassName).newInstance();
        _jspx_resourceInjector.setContext(application);
      }

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

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
			User roleUser = User.getSysTypeRoleUser(userRoles,stype);
			logger.info(sysType, "not xxt user jumped {}.");
			if(roleUser!=null){
				userId = String.valueOf(roleUser.getUserId());
	            orgId  = String.valueOf(roleUser.getOrgId());
	            logger.info(userId, "not xxt user id is {}.");
	            logger.info(orgId,  "not xxt user orgId is {}.");
			}else{
				userId = "0";
				orgId  = "0";
			}
			if("3".equals(sysType)){
				userId = mobile;
				orgId="test";
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
	logger.info(userId,     "-->the forward userid param is {}.");
	logger.info(roleId,     "-->the forward roleId param is {}.");
	logger.info(goUrl,      "-->the forward goUrl param is {}.");
	logger.info(orgId,      "-->the forward orgId param is {}.");
	logger.info(orign,      "-->the forward orign string is {}.");
	logger.info(encodeStr,  "-->the forward encodeStr param is {}.");
	logger.info(formUrl,    "-->the forward formUrl param is {}.");
	logger.info(sysType,    "-->the forward sysType param is {}.");

      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<body onload=\"document.forms[0].submit()\">\r\n");
      out.write("<form action=\"");
      out.print(formUrl );
      out.write("\" method=\"post\">\r\n");
      out.write("\t<input type=\"hidden\" name=\"id\"    value=\"");
      out.print(userId );
      out.write("\">\r\n");
      out.write("\t<input type=\"hidden\" name=\"role\"  value=\"");
      out.print(roleId );
      out.write("\">\r\n");
      out.write("\t<input type=\"hidden\" name=\"url\"   value=\"");
      out.print(goUrl);
      out.write("\">\r\n");
      out.write("\t<input type=\"hidden\" name=\"orgId\" value=\"");
      out.print(orgId );
      out.write("\">\r\n");
      out.write("\t<input type=\"hidden\" name=\"accesstoken\" value=\"");
      out.print(encodeStr );
      out.write("\">\r\n");
      out.write("</form>\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
