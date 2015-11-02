package org.apache.jsp.error;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class _404_jsp extends org.apache.jasper.runtime.HttpJspBase
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
    Throwable exception = org.apache.jasper.runtime.JspRuntimeLibrary.getThrowable(request);
    response.setStatus(((Integer)request.getAttribute("javax.servlet.error.status_code")).intValue());
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html;charset=UTF-8");
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
      out.write("<!DOCTYPE HTML>\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n");
      out.write("<title>404 - 系统找不到页面</title>\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
      out.print(request.getContextPath() );
      out.write("/theme/common.css\"/>\r\n");
      out.write("\r\n");
      out.write("<style type=\"text/css\">\r\n");
      out.write("\t#go-direct{height:200px;}\r\n");
      out.write("\t.cd-box{width:594px;height:200px;position:absolute;top:45%;left:50%;margin:-220px 0 0 -297px;}\r\n");
      out.write("\t.popover{width:600px;background-color:white;border-color:#71B8EC;border-style:solid;border-width:0 3px 3px;position:absolute;z-index:2002;text-align:left;box-shadow:3px 3px #CCC;-moz-box-shadow:3px 3px #CCC;-webkit-box-shadow:3px 3px #CCC;-o-box-shadow:3px 3px #CCC;}\r\n");
      out.write("\t.popover{text-align:left;}\r\n");
      out.write("\t.popover .pop-header{height:35px;background-color:#71B8EC;}\r\n");
      out.write("\t.popover .pop-title{height:35px;font:normal 18px/34px 'Microsoft YaHei';color:white;text-indent:12px;}\r\n");
      out.write("\t.cd-box .pop-content{padding-left:60px;}\r\n");
      out.write("\t.pop-logo{float:left;margin-top:30px;width:60px;height:55px;background:url(\"icon_result.png\") no-repeat;}\r\n");
      out.write("\t.pop-logo.pop-success{background-position:left top;}\r\n");
      out.write("\t.pop-logo.pop-error{background-position:left -55px;}\r\n");
      out.write("\t.pop-txt{margin-top:30px;float:left;padding-left:20px;line-height:55px;width:400px;height:55px;letter-spacing:2px;font-family:'microsoft yahei';font-size:16px;font-weight:bold;}\r\n");
      out.write("\t.pop-tip{clear:both;padding-top:20px;width:500px;height:50px;font-size:12px;text-align:center;}\r\n");
      out.write("\t#go-direct a.green:link,#go-direct a.green:visited,#go-direct .green{color:#0287CA;font-size: 16px;}\r\n");
      out.write("\t#timeNowOut{color:#FF0000;font-size:24px;font-weight:700;display:inline-block;padding:0 5px;}\r\n");
      out.write("</style>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("    <div id=\"go-direct\" class=\"popover cd-box\">\r\n");
      out.write("        <div class=\"pop-header\">\r\n");
      out.write("        \t<h3 class=\"pop-title\">温馨提示</h3>\r\n");
      out.write("        </div>\r\n");
      out.write("        <div class=\"pop-content\">\r\n");
      out.write("            <div class=\"pop-logo pop-error\"></div>\r\n");
      out.write("            <div class=\"pop-txt\">404 - 系统找不到页面。</div>\r\n");
      out.write("\t    \t<div class=\"pop-tip\"><a href=\"https://ha.ac.10086.cn/logout?goto=http://www.ha.10086.cn/edu/hn/action/commonCMS-Logout.action?uid=Logout\" class=\"green\">返回首页</a></div>\r\n");
      out.write("        </div>\r\n");
      out.write("    </div>\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
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
