package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.net.URLEncoder;
import java.net.URL;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.HttpClient;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;
import java.util.TreeSet;
import java.util.Set;
import java.util.Random;
import java.util.Calendar;
import common.ai.encryption.tools.EncryptionUtil;
import java.util.Map;
import java.util.HashMap;
import common.ai.httpclient.tools.HttpClientHelper;

public final class testHeEdu_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

 String host = "localhost";String port = "80"; String hetoken="3bb38680-209b-4829-8d3f-84ebd14bfd15";String aeskey = "COHeJfoWQgaYBuna";String aesSalt="5075428636499153";
String randomNum(int size){
    	      System.out.println("-----------randomNum-------->"+size);
    	      Random random = new Random();
              Set<Integer> nums = new TreeSet<Integer>();
              while(nums.size()<size){
                  int randomNum = random.nextInt(10);
                  nums.add(randomNum);
              }
           
              return org.apache.commons.lang.StringUtils.join(nums, "");
          }
       
String base64Aes(String str){
    	    try{  
              System.out.println("------------------->"+str);
              byte[] aesbyte = EncryptionUtil.aesEncode(str.getBytes("UTF-8"), aeskey.getBytes(), aesSalt);
              return EncryptionUtil.encodeStrBase64(aesbyte);
    	    }catch(Exception e){
    	    	e.printStackTrace();
    	    	System.out.println("-------error------------>"+str);
    	    }
    	    return "";
          }
       
String aesTokenEncode(){
              System.out.println("-----------aesTokenEncode-------->");
              String extend = hetoken + "," + Calendar.getInstance().getTime().getTime() + "," + randomNum(6);
              
              return base64Aes(extend);
          }
       
String heEduCallEduApiTest(String service,Map<String,String> params,String busiCode){
    	      System.out.println("-----------heEduCallEduApiTest-------->");
              HttpClientHelper clienthp = new HttpClientHelper();
              clienthp.setHttpReqUrl(host);
              clienthp.setHttpReqPort(port);
              clienthp.setHttpReqServiceName(service);
              clienthp.setConnectTimeout(180000);
              clienthp.setRequestTimeout(18000);
              
              return clienthp.getHttpResonseJson(params, busiCode);
          }
       
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
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html>\r\n");
      out.write("\t<head>\r\n");
      out.write("\t   <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\r\n");
      out.write("\t   <meta name=\"renderer\" content=\"webkit\" />\r\n");
      out.write("       <meta http-equiv=\"X-UA-Compatible\" content=\"IE=Edge,chrome=1\" />\r\n");
      out.write("       <title>和教育门户相关接口测试</title>\r\n");
      out.write("       \r\n");
      out.write("       <link href=\"theme/jsonFormater.css\" type=\"text/css\" rel=\"stylesheet\"/>\r\n");
      out.write("       \r\n");
      out.write("\t</head>\r\n");
      out.write("\t<body>\r\n");
      out.write("\t   <!-- 模拟和教育调用教育门户api相关接口（使用httpClient提交） -->\r\n");
      out.write("\r\n");
      out.write("\t   \t   ");
      out.write("    \r\n");
      out.write("\t   \r\n");
      out.write("       ");
      out.write("\r\n");
      out.write("       ");
      out.write("\r\n");
      out.write("      ");
      out.write("\r\n");
      out.write("       ");
      out.write("\r\n");
      out.write("       <h1 id=\"interTitle\"></h1>\r\n");
      out.write("       <div id='container'></div>\r\n");
      out.write("       \r\n");
      out.write("       <script type=\"text/javascript\" src=\"lib/jquery/1.7.2/jquery.js\"></script>\r\n");
      out.write("       <script type=\"text/javascript\" src=\"lib/jsonFormater/jsonFormater.js\"></script>\r\n");
      out.write("    \r\n");
      out.write("       <script type=\"text/javascript\">\r\n");
      out.write("           var opt = {};\r\n");
      out.write("           var jf  = {};\r\n");
      out.write("           opt = {\r\n");
      out.write("                   dom:'#container',\r\n");
      out.write("                   singleTab:' ',\r\n");
      out.write("                   tabSize:4,\r\n");
      out.write("                   imgCollapsed:'theme/images/jsonFormater/Collapsed.gif',\r\n");
      out.write("                   imgExpanded:'theme/images/jsonFormater/Expanded.gif',\r\n");
      out.write("                   isCollapsible:false,\r\n");
      out.write("                   quoteKeys:true\r\n");
      out.write("               };\r\n");
      out.write("           jf = new JsonFormater(opt); //创建JSON格式化对象 \r\n");
      out.write("\r\n");
      out.write("           \r\n");
      out.write("           function logout(userId,extend){\r\n");
      out.write("        \t   $.ajax({\r\n");
      out.write("                   url:'http://www.ha.10086.cn/edu/logout', \r\n");
      out.write("                   type:'get',\r\n");
      out.write("                   dataType:'json',\r\n");
      out.write("                   data:'id='+userId+'&xxtCode=410000'+'&extend='+extend,\r\n");
      out.write("                   success:function(json){\r\n");
      out.write("                       $('#interTitle').html('测试登出接口7.'); \r\n");
      out.write("                       jf.doFormat(json);\r\n");
      out.write("                   },error:function(){\r\n");
      out.write("                       $('#interTitle').html('测试登出接口7失败.'); \r\n");
      out.write("                   }\r\n");
      out.write("               }); \r\n");
      out.write("           }\r\n");
      out.write("       </script>\r\n");
      out.write("       ");
 
       int internum = StringUtil.str2Int(request.getParameter("internum"),1);
       String extend = aesTokenEncode();
       System.out.print("--------extend-------->"+extend);
       String rtnjson = "调用失败";
       String interTitle = "未知接口";
       if(internum == 1){
           String service = "edu/login_check";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           String password = "123456";
           
           password = base64Aes(password);
           System.out.print("--------password-------->"+password);
           loginCheckParams.put("account", "13837112779");
           loginCheckParams.put("password",password);
           loginCheckParams.put("role",    "3");
           
           loginCheckParams.put("extend",  extend);
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"login_check");
           interTitle = "测试1 用户登录验证接口";
       }else if(internum == 2){
           String service = "edu/user_detail";
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("account", "13525509733");//13403710123
           loginCheckParams.put("extend",  extend);
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"user_detail");
           interTitle = "测试2 用户详细信息获取接口";
       }else if(internum == 3){//太慢待测试
           String service = "edu/class/student_parent";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("classId", "3477161");
           loginCheckParams.put("extend",  extend);
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"student_parent");
           interTitle = "测试3 班级中学生与家长信息获取接口";
       }else if(internum == 4){//和教育跳转到门户
           String service = "edu/jump";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("id",  "48546363");
           loginCheckParams.put("role","3");
           loginCheckParams.put("extend",  extend);
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"jump");
           interTitle = "测试3 班级中学生与家长信息获取接口";
           
           String jextend = aesTokenEncode();
           Map callback = JsonUtil.convertJson2Object(rtnjson, Map.class);
           String jumrul = (String)callback.get("callbackurl");
           String accesstoken = (String)callback.get("accesstoken");
           
           try{
              jumrul = jumrul + "?accesstoken="+accesstoken + "&extend="+URLEncoder.encode(jextend, "UTF-8");
           }catch(Exception e){}
           System.out.print("------------------->"+jumrul);
           
           out.println("<a href=\""+jumrul+"\">跳转到门户</a>");
       }else if(internum == 7){
    	    String userId = "48546363";
    	    extend = URLEncoder.encode(extend, "UTF-8");
    	    out.println("<script type=\"text/javascript\">logout('"+userId+"','"+extend+"');</script>");
    	    return;   
       }else if(internum == 8){
           String service = "edu/user/order";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("phone",   "13837112779");//13849033850
           loginCheckParams.put("extend",  extend);
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"user_order");
           interTitle = "测试8 用户校讯通业务信息获取接口";
       }else if(internum == 9){//没有查询到记录
           String service = "edu/class/teacher";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("id",   "3345432");//班级Id
           loginCheckParams.put("extend",  extend);
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"class_teacher");
           interTitle = "测试9 班级教师信息获取接口";
       }else if(internum == 10){
           String service = "edu/transfers/user";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("timeStamp", Calendar.getInstance().getTime().getTime()+"");//时间
           loginCheckParams.put("name",      "王妈妈");//
           loginCheckParams.put("nickName",  "王妈妈");//
           loginCheckParams.put("account",   "15237110949");//
           loginCheckParams.put("operation", "SAVE");//
           loginCheckParams.put("id",        "73581238");//
           loginCheckParams.put("type",      "3");
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"transfers_user");
           interTitle = "测试10 用户信息同步接口";
       }else if(internum == 11){
           String service = "edu/transfers/user_class";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("timeStamp", Calendar.getInstance().getTime().getTime()+"");//时间
           loginCheckParams.put("userId",    "4");//
           loginCheckParams.put("classId",   "3");//
           loginCheckParams.put("role",      "2");//
           loginCheckParams.put("operation", "1");//
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"transfers_userClass");
           interTitle = "测试11 用户班级信息同步接口";
       }else if(internum == 12){
           String service = "edu/transfers/user_relationship";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("timeStamp", Calendar.getInstance().getTime().getTime()+"");//时间
           loginCheckParams.put("id",        "4");//
           loginCheckParams.put("parentId",  "3");//
           loginCheckParams.put("operation", "1");//
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"transfers_userRelationship");
           interTitle = "测试12 用户关系同步接口";
       }else if(internum == 13){
           String service = "edu/transfers/class";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("timeStamp", Calendar.getInstance().getTime().getTime()+"");//时间
           loginCheckParams.put("id",        "4");//
           loginCheckParams.put("type",      "3");//
           loginCheckParams.put("operation", "1");//
           loginCheckParams.put("schoolId",  "3");//
           loginCheckParams.put("schoolNme", "3");//
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"transfers_syncClass");
           interTitle = "测试13 班级信息同步接口";
       }else if(internum == 14){
           String service = "edu/transfers/school";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("timeStamp", Calendar.getInstance().getTime().getTime()+"");//时间
           loginCheckParams.put("id",        "4");//
           loginCheckParams.put("type",      "3");//
           loginCheckParams.put("name",      "3");//
           loginCheckParams.put("operation", "1");//
           loginCheckParams.put("province",  "3");//
           loginCheckParams.put("city",      "3");//
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"transfers_syncSchool");
           interTitle = "测试14 学校信息同步接口";
       }else if(internum == 15){
           String service = "edu/transfers/user_order";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("timeStamp", Calendar.getInstance().getTime().getTime()+"");//时间
           loginCheckParams.put("phone",     "4");//
           loginCheckParams.put("status",    "3");//
           loginCheckParams.put("name",      "3");//
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"transfers_syncUserOrder");
           interTitle = "测试15 用户校讯通业务信息同步接口";
       }else if(internum == 16){
           rtnjson = "";
           interTitle = "测试16 用户资料全量同步接口,由crm进程调用。";
       }else if(internum == 17){
           String service = "edu/transfers/password";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("timeStamp", Calendar.getInstance().getTime().getTime()+"");//时间
           loginCheckParams.put("id",      "4");//
           loginCheckParams.put("account", "3");//
           loginCheckParams.put("password","3");//
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"transfers_syncHeEduPwd");
           interTitle = "测试17 用户账号密码同步接口";
       }else if(internum == 18){//
           String service = "edu/sync/password";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("id",        "JnNUrSA4XSeqh0NMQhT6NQ==");//用户13525509733
           loginCheckParams.put("timeStamp", Calendar.getInstance().getTime().getTime()+"");//时间
           String password = "abc123";
           
           password = base64Aes(password);
           loginCheckParams.put("password",  password);//密码
           loginCheckParams.put("extend",    extend);
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"class_teacher");
           interTitle = "测试18 用户密码同步接口";
       }else if(internum == 19){//
           String service = "edu/transfers/products";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("timeStamp", Calendar.getInstance().getTime().getTime()+"");//时间
           loginCheckParams.put("province",  "4");//
           loginCheckParams.put("groupCode", "3");//
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"transfers_queryproducts");
           interTitle = "测试19 产品查询接口";
       }else if(internum == 20){//
           String service = "edu/transfers/groups";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("timeStamp", Calendar.getInstance().getTime().getTime()+"");//时间
           loginCheckParams.put("province",  "4");//
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"transfers_querygroups");
           interTitle = "测试20 产品组查询接口";
       }else if(internum == 21){//
           String service = "edu/products/group";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("timeStamp", Calendar.getInstance().getTime().getTime()+"");//时间
           loginCheckParams.put("province",    "4");//
           loginCheckParams.put("bossCode",    "4");//
           loginCheckParams.put("productCodes","4");//
           loginCheckParams.put("groupName",   "4");//
           loginCheckParams.put("groupDesc",   "4");//
           loginCheckParams.put("groupIcon",   "4");//
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"products_queryproductsgroup");
           interTitle = "测试21 产品组合接口";
       }else if(internum == 22){//
           String service = "edu/group/boss_code";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("timeStamp", Calendar.getInstance().getTime().getTime()+"");//时间
           loginCheckParams.put("province",  "4");//
           loginCheckParams.put("bossCode",  "4");//
           loginCheckParams.put("groupCode", "4");//
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"group_groupBossCode");
           interTitle = "测试22 产品组绑定业务接口";
       }else if(internum == 23){//这个接口crm还没有给出接口.现在为19接口
           String service = "edu/order/group";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("groupBillingId", "qqq");//时间
           loginCheckParams.put("bossCode",  "YDZQ100014");//
           loginCheckParams.put("dn",        "15093265531");//
           loginCheckParams.put("type",      "1");//
           loginCheckParams.put("unionId",   "4");//
           loginCheckParams.put("extend",  extend);
           
           rtnjson = heEduCallEduApiTest(service,loginCheckParams,"order_group");
           interTitle = "测试19(23) 产品包月订购、包月退订、点播接口";
       }else if(internum == 24){//测试由第三方系统调用门户接口,然后由门户调用和教育接口,现在为20
           String service = "edu/transfers/orderGroup";
           
           Map<String,String> loginCheckParams = new HashMap<String,String>();
           loginCheckParams.put("timeStamp",Calendar.getInstance().getTime().getTime()+"");//时间戳
           loginCheckParams.put("province",  "1");//省
           loginCheckParams.put("groupBillingId", "2");//产品组编码
           loginCheckParams.put("bossCode",  "3");//业务编码
           loginCheckParams.put("dn",        "4");//手机号码
           loginCheckParams.put("type",      "5");//业务类型
           loginCheckParams.put("source",    "6");//订购关系来源
           loginCheckParams.put("orderTime", "7");//订购时间
           loginCheckParams.put("quitTime",  "8");//退订时间
           loginCheckParams.put("startTime", "9");//生效时间
           loginCheckParams.put("endTime",   "10");//失效时间
           loginCheckParams.put("unionId",   "11");//集团ID
           
           try{
	           HttpClient client = new HttpClient();
	           client.getHttpConnectionManager().getParams().setConnectionTimeout(180000);
	           
	           PostMethod post = new PostMethod();
	           post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 180000);
	           
	           StringBuffer httpUrl = new StringBuffer();
	           if(StringUtil.str2Int(port) == 80){
	               httpUrl.append("http://").append(host).append("/").append(service);
	           }else{
	               httpUrl.append("http://").append(host).append(":").append(port).append("/").append(service);
	           }
	           
	           String reqUrl = httpUrl.toString();
	           post.setURI(new URI(reqUrl,true));
	           String content = JsonUtil.convertObject2Json(loginCheckParams);
	           
	           System.out.print("-----------测试20(24)--------------------->"+content);
	           StringRequestEntity entity = new StringRequestEntity(content,"text/json","UTF-8");
	           post.setRequestEntity(entity);
	           
	           client.executeMethod(post);
	           int rtnStatus = post.getStatusCode();
	           if(rtnStatus==200){
	        	   rtnjson = new String(post.getResponseBody(), "UTF-8");
	           }else{
	        	   rtnjson = "";
	           }
	           
	           post.releaseConnection();
	           
	           interTitle = "测试20(24) 订购关系同步接口";
           }catch(Exception e){}
       }
       out.println("<script type=\"text/javascript\">$('#interTitle').html('"+interTitle+"');jf.doFormat("+rtnjson+");</script>");
       
       
      out.write("\r\n");
      out.write("\t</body>\r\n");
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
