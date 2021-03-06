package com.ai.eduportal.httpclient.xxt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.ai.eduportal.remote.CodeUtil;
import com.ai.eduportal.remote.RemoteHttpCaller;
import common.ai.httpclient.tools.HttpClientHelper;

public class CmccEduTest {

	public static void main(String[] args) {
		String key = "wpdk3k56wpdk3k56wpdk3k56";// 密钥字符串长度要是8的倍数，双方密钥要一致，否则出差，无法解析参数
//		String serialNo="cs201410141234567890";
		String serialNo="cs201410221000000023";
		
//		String phone="15837184063";
		String phone="13676917030";
		String serialNoEncrypted = DES.encrypt(serialNo, key);// 加密后的数数据
//		CodeUtil.setEncryptedKey(key);
		
//		String serialNoEncrypted1 = CodeUtil.lettlerEncrypted(serialNo);
//		System.out.println(serialNoEncrypted.equalsIgnoreCase(serialNoEncrypted1));
		String phoneEncrypted  = DES.encrypt(phone, key);// 加密后的数据
//		String phoneEncrypted1 = CodeUtil.lettlerEncrypted(phone);// 加密后的数据
//		System.out.println(phoneEncrypted.equalsIgnoreCase(phoneEncrypted1));
		
		String vnumber  = MD5.encrypt(serialNo+ phone.substring(5, 11));// md5加密,生成鉴权码：
//		String vnumber1 = EncryptionUtil.md5Str(serialNo+ phone.substring(5, 11));
//		System.out.println(vnumber+",,,"+vnumber1);
		
		
		String vnumberEncrypted= DES.encrypt(vnumber, key);//，防止md5生成的乱码进行传输时
		if(vnumberEncrypted.length()>=30){
			vnumberEncrypted=vnumberEncrypted.substring(0, 30);//截取前30位进行传递
		}
		String paraData="serialNo="+serialNoEncrypted+"&phone="+phoneEncrypted+"&vnumber="+vnumberEncrypted;
		System.out.println(paraData);
//		try {
//			paraData = java.net.URLEncoder.encode(paraData, "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		
//		String paraData1 = CodeUtil.getParamData(serialNo, phone);
//		System.out.println(paraData+"\n"+paraData1);
		//System.out.println(paraData);	
		//paraData:serialNo=495a4417e05b4ddd96ffce0def8c219f8d529c1e0ead3b3a&phone=92710f55bec4f103500b5b995652468a&vnumber=8ed73b710995bde0a6eeca9308aaffcc67935c4e9d66e7b70ec40f4e96c0497a
		//paraData:serialNo%3D495a4417e05b4ddd96ffce0def8c219f8d529c1e0ead3b3a%26phone%3D92710f55bec4f103500b5b995652468a%26vnumber%3D8ed73b710995bde0a6eeca9308aaffcc67935c4e9d66e7b70ec40f4e96c0497a
	
		// 发送 POST 请求：调用校信通接口，并获取返回的数据
		// post方式传递参数
//		String rspStr = sendPost("http://api.xxt.cn/jxlx/cmccedugetuser.do","paraData=" + paraData);
		String rspStr = sendPost("http://localhost/edu/hn/login/user-testCall.action","paraData=" + paraData);
		
		String respStr = callByHttp("http://api.xxt.cn/jxlx/cmccedugetuser.do",paraData);
//		String respStr = callByHttp("http://localhost/edu/hn/login/user-testCall.action",paraData);
		System.out.println(rspStr);
		System.out.println(respStr);
		
		System.out.println("=======================================================");
		
		//中小学学习报
		testXXB();
		System.out.println("=======================================================");
		//同步课堂
		testTTGT();
	}
	public static void testTTGT(){
		
		String key = "wpdk3k56wpdk3k56wpdk3k56";// 密钥字符串长度要是8的倍数，双方密钥要一致，否则出差，无法解析参数
		String serialNo="cs201410221000000023";
		
//		String phone="15837184063";
		String phone="13803896412";
		String serialNoEncrypted = DES.encrypt(serialNo, key);// 加密后的数数据
		//CodeUtil.setEncryptedKey(key);
		
		String phoneEncrypted  = DES.encrypt(phone, key);// 加密后的数据
		
		String vnumber  = MD5.encrypt(serialNo+ phone.substring(5, 11));// md5加密,生成鉴权码：
		
		
		String vnumberEncrypted= DES.encrypt(vnumber, key);//，防止md5生成的乱码进行传输时
		if(vnumberEncrypted.length()>=30){
			vnumberEncrypted=vnumberEncrypted.substring(0, 30);//截取前30位进行传递
		}
		String paraData="serialNo="+serialNoEncrypted+"&phone="+phoneEncrypted+"&vnumber="+vnumberEncrypted;
//		try {
//			paraData = java.net.URLEncoder.encode(paraData, "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		CodeUtil codeUtil = new CodeUtil();
		codeUtil.setEncryptedKey(key);
		String paraData1 = codeUtil.getParamData1(serialNo, phone);
		System.out.println(paraData+"\n"+paraData1);
	
		// 发送 POST 请求：调用校信通接口，并获取返回的数据
		// post方式传递参数
//		String rspStr = sendPost("http://211.142.200.7:8280/queryport_hnxxtcm/iface/ecusert_jsonOrderList.action","paraData=" + paraData);
		
		String respStr = callByHttp("http://student.xueceping.cn/api/userinfo/",paraData1);
//		System.out.println(rspStr);
		System.out.println(respStr);
	}
	public static void testXXB(){
		
		String key = "deskey12";// 密钥字符串长度要是8的倍数，双方密钥要一致，否则出差，无法解析参数
		String serialNo="cs201410241000000063";
		
//		String phone="15837184063";
		String phone="13837188559";
//		String serialNoEncrypted = DES.encrypt(serialNo, key);// 加密后的数数据
//		//CodeUtil.setEncryptedKey(key);
//		
//		String phoneEncrypted  = DES.encrypt(phone, key);// 加密后的数据
//		
//		String vnumber  = MD5.encrypt(serialNo+ phone.substring(5, 11));// md5加密,生成鉴权码：
//		
//		
//		String vnumberEncrypted= DES.encrypt(vnumber, key);//，防止md5生成的乱码进行传输时
//		if(vnumberEncrypted.length()>=30){
//			vnumberEncrypted=vnumberEncrypted.substring(0, 30);//截取前30位进行传递
//		}
//		String paraData="serialNo="+serialNoEncrypted+"&phone="+phoneEncrypted+"&vnumber="+vnumberEncrypted;
//		try {
//			paraData = java.net.URLEncoder.encode(paraData, "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		CodeUtil codeUtil = new CodeUtil();
		codeUtil.setEncryptedKey(key);
		String paraData1 = codeUtil.getParamData(serialNo, phone);
//		System.out.println(paraData+"\n"+paraData1);
		System.out.println(paraData1);
	
		// 发送 POST 请求：调用校信通接口，并获取返回的数据
		// post方式传递参数
//		String rspStr = sendPost("http://211.142.200.7:8280/queryport_hnxxtcm/iface/ecusert_jsonOrderList.action","paraData=" + paraData);
		
		String respStr = callByHttp("http://211.142.200.7:8280/queryport_hnxxtcm/iface/ecusert_jsonOrderList.action",paraData1);
//		System.out.println(rspStr);
		System.out.println(respStr);
	}
	public static String callByHttp(String strURL, String param) {
		HttpClientHelper http = new HttpClientHelper();
//		http.setHttpReqUrl("http://localhost");
//		http.setHttpReqPort("80");
//		http.setHttpReqServiceName("edu/hn/login/user-testCall.action");
		RemoteHttpCaller call= new RemoteHttpCaller(http,strURL);
		return call.getResponse(param);
	}
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果：网页内容
	 */
	public static String sendPost(String strURL, String param) {

		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			// 设置请求的方式和相关参数
			URL httpurl = new URL(strURL);
			HttpURLConnection conn = (HttpURLConnection) httpurl
					.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("ContentType","text/xml;charset=utf-8");
			conn.setReadTimeout(60*1000);// 读取超时时间
			conn.setDoOutput(true);
			conn.setConnectTimeout(60*1000);// 链接超时时间
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(param);
			out.flush();
			// 读取数据
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream(),"utf-8"));
			String line = null;// 读取的每行
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
}
