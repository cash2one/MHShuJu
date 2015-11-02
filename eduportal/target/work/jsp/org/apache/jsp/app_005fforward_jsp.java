package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.ai.eduportal.Util.Constants;
import common.ai.tools.JsonUtil;
import java.util.Map;
import com.ai.frame.util.SpringContextHelper;
import common.ai.logger.LoggerFactory;
import common.ai.logger.Logger;
import java.util.Calendar;
import com.ai.frame.web.util.SystemPropUtil;
import common.ai.encryption.tools.EncryptionUtil;
import common.ai.tools.StringUtil;
import java.net.URLEncoder;

public final class app_005fforward_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.print(encodeStr);
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
