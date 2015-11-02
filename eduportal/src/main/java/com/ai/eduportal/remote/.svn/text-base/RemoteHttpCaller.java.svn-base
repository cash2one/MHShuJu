package com.ai.eduportal.remote;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;

import common.ai.httpclient.tools.HttpClientHelper;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;
import common.ai.tools.ClassUtil;

public class RemoteHttpCaller implements RemoteHttpCallerI {
	private Logger log = LoggerFactory.getOuterCallerLog(RemoteHttpCaller.class);
	private String httpurl;
	private HttpClientHelper httpClient;
	public RemoteHttpCaller(String httpurl){
		this.httpurl = httpurl;
//		this.httpClient = httpClient;
	}
	public RemoteHttpCaller(HttpClientHelper httpClient,String httpurl){
		this.httpurl = httpurl;
		this.httpClient = httpClient;
	}
	/* (non-Javadoc)
	 * @see com.ai.eduportal.remote.RemoteHttpCallerI#getResponse(java.lang.String)
	 */
	public String getResponse1(String reqstr){
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			// 设置请求的方式和相关参数
			URL httpurl = new URL(this.httpurl);
			HttpURLConnection conn = (HttpURLConnection) httpurl.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("ContentType","text/xml;charset=utf-8");
			conn.setReadTimeout(300*1000);// 读取超时时间
			conn.setDoOutput(true);
			conn.setConnectTimeout(300*1000);// 链接超时时间
			conn.setDoInput(true);
			out = new PrintWriter(conn.getOutputStream());
			out.print(reqstr);
			out.flush();
			// 读取数据
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
			String line = null;// 读取的每行
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			log.error("call [{}] error:{} the inputStr is: {}", e, httpurl,reqstr);
			//e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				log.error("close call [{}]'s the input output stream error:{}", ex,httpurl);
				//ex.printStackTrace();
			}
		}
		return result;
	}
	public String getResponse(String reqstr){
		
		long startTime = System.currentTimeMillis();
		int reqNum = 0;
        Map<Integer, String> rtnMap = null;
        Integer reRequestTimes = (Integer)ClassUtil.getFieldVal(httpClient, "reRequestTimes");
        if(reRequestTimes==null)reRequestTimes = 0;
        while (reqNum < reRequestTimes.intValue()) {
        	rtnMap = invokService(reqstr);
        	if (rtnMap != null && rtnMap.containsKey(Integer.valueOf(200))) {
                break;
            } else {
            	reqNum++;
                log.info("invokService Request {} times failure!", null, String.valueOf(reqNum));
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("invokService ,used time:{} ms.method[{}]", null,String.valueOf(endTime - startTime),"getHttpResonseJson");
        if (rtnMap != null) {
        	String rtnJson = rtnMap.get(Integer.valueOf(200));
        	if (rtnJson != null && rtnJson.length() > 0) {
        		return rtnJson;
        	}
        }
        return null;
	}
	private Map<Integer,String> invokService(String reqstr){
		PostMethod post = new PostMethod();
		Header contentTypeheader = new Header();
		contentTypeheader.setName("Content-Type");
		contentTypeheader.setValue("application/x-www-form-urlencoded;charset=UTF-8");
		// hearder设置
		post.setRequestHeader(contentTypeheader);
		int requestTimeout = (Integer)ClassUtil.getFieldVal(httpClient, "requestTimeout");
		post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, requestTimeout * 1000);
		
		int rtnStatus  = 0;
        String rtnJson = null;
        try {
        	post.setURI(new URI(httpurl,true));    //设置请求URL
        	HttpClient httpclient = httpClient.getHttpClient();
        	final NameValuePair[] data = new NameValuePair[] {new NameValuePair("paraData", reqstr)};
        	post.setRequestBody(data);
            httpclient.executeMethod(post);
            rtnStatus = post.getStatusCode();
            if(rtnStatus==200){
                rtnJson = new String(post.getResponseBody(), "UTF-8");
            }else{
                rtnJson = "";
            }
        }catch (Exception e) {
        	rtnStatus = 0;
            rtnJson = "";
            log.error("call [{}] error:{} the inputStr is: {}", e, httpurl,reqstr);
        }finally{httpClient.relaseConnection(post);}
        
        Map<Integer,String> rtnMap = new HashMap<Integer, String>();
        rtnMap.put(Integer.valueOf(rtnStatus), rtnJson);
        return rtnMap;
	}
	
	public String getResponse(String reqstr,String fileName, File file){
		long startTime = System.currentTimeMillis();
		int reqNum = 0;
		Map<Integer, String> rtnMap = null;
		Integer reRequestTimes = (Integer) ClassUtil.getFieldVal(httpClient,"reRequestTimes");
		if (reRequestTimes == null)
			reRequestTimes = 0;
		while (reqNum < reRequestTimes.intValue()) {
			rtnMap = invokService(reqstr,fileName,file);
			if (rtnMap != null && rtnMap.containsKey(Integer.valueOf(200))) {
				break;
			} else {
				reqNum++;
				log.info("invokService Request {} times failure!", null,String.valueOf(reqNum));
			}
		}
		long endTime = System.currentTimeMillis();
		log.info("invokService ,used time:{} ms.method[{}]", null,String.valueOf(endTime - startTime), "getHttpResonseJson");
		if (rtnMap != null) {
			String rtnJson = rtnMap.get(Integer.valueOf(200));
			if (rtnJson != null && rtnJson.length() > 0) {
				return rtnJson;
			}
		}
		return null;
	}
	
	private Map<Integer, String> invokService(String reqstr,String fileName,File file) {
		PostMethod post = new PostMethod();
		int requestTimeout = (Integer) ClassUtil.getFieldVal(httpClient,"requestTimeout");
		post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,requestTimeout * 1000);
		int rtnStatus = 0;
		String rtnJson = null;
		try {
			post.setURI(new URI(httpurl, true)); // 设置请求URL
			HttpClient httpclient = httpClient.getHttpClient();
			Part[] parts = new Part[2];
			parts[0] = new StringPart("paraData", reqstr);
			FilePart filePart = new  FilePart("mediaFile", file);
			filePart.setContentType("binary");
			parts[1]=filePart;
			
			MultipartRequestEntity mentity = new MultipartRequestEntity(parts,post.getParams());
			post.setRequestEntity(mentity);
			httpclient.getParams().setContentCharset("UTF-8"); 
			 
			httpclient.executeMethod(post);
			rtnStatus = post.getStatusCode();
			if (rtnStatus == 200) {
				rtnJson = new String(post.getResponseBody(), "UTF-8");
			} else {
				rtnJson = "";
			}
		} catch (Exception e) {
			rtnStatus = 0;
			rtnJson = "";
			log.error("call [{}] error:{} the inputStr is: {}", e, httpurl,
					reqstr);
		} finally {
			httpClient.relaseConnection(post);
		}
		Map<Integer, String> rtnMap = new HashMap<Integer, String>();
		rtnMap.put(Integer.valueOf(rtnStatus), rtnJson);
		return rtnMap;
	}
}
