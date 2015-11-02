<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>500 - 系统内部错误</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/theme/common.css"/>

<style type="text/css">
	#go-direct{height:200px;}
	.cd-box{width:594px;height:200px;position:absolute;top:45%;left:50%;margin:-220px 0 0 -297px;}
	.popover{width:600px;background-color:white;border-color:#71B8EC;border-style:solid;border-width:0 3px 3px;position:absolute;z-index:2002;text-align:left;box-shadow:3px 3px #CCC;-moz-box-shadow:3px 3px #CCC;-webkit-box-shadow:3px 3px #CCC;-o-box-shadow:3px 3px #CCC;}
	.popover{text-align:left;}
	.popover .pop-header{height:35px;background-color:#71B8EC;}
	.popover .pop-title{height:35px;font:normal 18px/34px 'Microsoft YaHei';color:white;text-indent:12px;}
	.cd-box .pop-content{padding-left:60px;}
	.pop-logo{float:left;margin-top:30px;width:60px;height:55px;background:url("icon_result.png") no-repeat;}
	.pop-logo.pop-success{background-position:left top;}
	.pop-logo.pop-error{background-position:left -55px;}
	.pop-txt{margin-top:30px;float:left;padding-left:20px;line-height:55px;width:400px;height:55px;letter-spacing:2px;font-family:'microsoft yahei';font-size:16px;font-weight:bold;}
	.pop-tip{clear:both;padding-top:20px;width:500px;height:50px;font-size:12px;text-align:center;}
	#go-direct a.green:link,#go-direct a.green:visited,#go-direct .green{color:#0287CA;font-size: 16px;}
	#timeNowOut{color:#FF0000;font-size:24px;font-weight:700;display:inline-block;padding:0 5px;}
</style>
</head>
<body>
    <div id="go-direct" class="popover cd-box">
        <div class="pop-header">
        	<h3 class="pop-title">温馨提示</h3>
        </div>
        <div class="pop-content">
            <div class="pop-logo pop-error"></div>
            <% 
    		String errmsg = exception.getMessage();
    		if(errmsg!=null&&errmsg.indexOf("error=10001")>-1){
	    	%>
	    		<div class="pop-txt">你还没有在门户注册, 不能进行登录。</div>
	    		<div class="pop-tip"><a href="https://ha.ac.10086.cn/logout?goto=http://www.ha.10086.cn/edu/hn/action/commonCMS-Logout.action?uid=Logout" class="green">返回首页</a></div>
	    	<%		
	    		}else if(errmsg!=null&&errmsg.indexOf("error=10001")>-1){
	    	%>
	    		<div class="pop-txt">你的手机号不是河南用户,不能进行登录。</div>
	    		<div class="pop-tip"><a href="https://ha.ac.10086.cn/logout?goto=http://www.ha.10086.cn/edu/hn/action/commonCMS-Logout.action?uid=Logout" class="green">返回首页</a></div>
	    	<%		
	    		}else{
	    	%>
	    		<div class="pop-txt">500 - 系统内部错误。</div>
	    		<div class="pop-tip"><a href="https://ha.ac.10086.cn/logout?goto=http://www.ha.10086.cn/edu/hn/action/commonCMS-Logout.action?uid=Logout" class="green">返回首页</a></div>
	    	<%	
	    		}
	    	%>
            
        </div>
    </div>

</body>
</html>