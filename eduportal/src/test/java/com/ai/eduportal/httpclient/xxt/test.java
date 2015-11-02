package com.ai.eduportal.httpclient.xxt;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpRequest;

public class test{
	public static String sendGet(String url,String param) throws Exception{
		url = "http://www.baidu.com";
		URL realUrl = new URL(url);
		//请求打开连接
		URLConnection connection  = realUrl.openConnection();
		//设置通用的请求属性
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 建立实际的连接
        connection.connect();
        Map<String, List<String>> map = connection.getHeaderFields();
		
		
		
		return map.toString();
	}
	public  static void main(String[] args) throws Exception {
		String sendGet = sendGet("", "");
		System.out.println(sendGet);
	}
}











