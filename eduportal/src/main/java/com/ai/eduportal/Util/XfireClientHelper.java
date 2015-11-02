package com.ai.eduportal.Util;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.xfire.client.Client;
import org.codehaus.xfire.transport.http.CommonsHttpMessageSender;

import com.ai.eduportal.remote.RemoteHttpCaller;
import com.ai.eduportal.remote.RemoteHttpCallerI;

import common.ai.httpclient.tools.HttpClientHelper;
import common.ai.tools.StringUtil;

public class XfireClientHelper {
	private Log log = LogFactory.getLog(XfireClientHelper.class);
	private int connectTimeout = 20000;
	private int sendTimeout = 50000;
	private ThreadLocal<Client> xfireClient = new ThreadLocal<Client>();
	private HttpClientHelper httpClient;
	public boolean checkServiceUrl(String url) {
		RemoteHttpCallerI caller = new RemoteHttpCaller(httpClient,url);
		String respStr = caller.getResponse1("");
		if(!StringUtil.isEmpty(respStr)){
			return true;
		}
		return false;
	}
	public void initWebserviceClient(String serviceWSdl){
		Client client = xfireClient.get();
		if(client != null)return;
		try{
			URL url = new URL(serviceWSdl);
			HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
			httpConnection.setReadTimeout(connectTimeout);//设置http连接的读超时,单位是毫秒
			httpConnection.connect();
			
			client = new Client(httpConnection.getInputStream(), null);
			client.setProperty(CommonsHttpMessageSender.HTTP_TIMEOUT, String.valueOf(sendTimeout));//设置发送的超时限制,单位是毫秒;
			client.setProperty(CommonsHttpMessageSender.DISABLE_KEEP_ALIVE, "true");
            client.setProperty(CommonsHttpMessageSender.DISABLE_EXPECT_CONTINUE, "true");
            
            xfireClient.set(client);
		}catch(Exception e){
			log.error("initWebserviceClient error:", e);
		}
	}
	public String excuteRemoteMethod2(String serviceWSdl,String method,String reqXml){
		initWebserviceClient(serviceWSdl);
		Client client = xfireClient.get();
		Object[] params = new Object[]{reqXml};
		try {
			Object[] results = client.invoke(method, params);
			if (results != null && results.length > 0) {
				Object rtn = results[0];
                return (String)rtn;
            }
		}catch (Exception e) {
			log.error("excuteRemoteMethod["+serviceWSdl+"] error:", e);
		}finally{
			if(client!=null){
				client.close();
				client = null;
				xfireClient.remove();
			}
		}
		return null;
	}
	
	public String excuteRemoteMethod(String serviceWSdl,String method,String reqXml){
		int i =0;
		String rtn = excuteRemote(serviceWSdl,method,reqXml);
		
		while(i < 3){
			if(rtn !=null )break;
			
			if(rtn == null){
				log.error("excuteRemoteMethod["+serviceWSdl+"] error 's num:"+i);
			}
			
			rtn = excuteRemote(serviceWSdl,method,reqXml);
			i++;
		}
		return rtn;
	}
	private String excuteRemote(String serviceWSdl,String method,String reqXml){
		initWebserviceClient(serviceWSdl);
		Client client = xfireClient.get();
		Object[] params = new Object[]{reqXml};
		try {
			Object[] results = client.invoke(method, params);
			if (results != null && results.length > 0) {
				Object rtn = results[0];
                return (String)rtn;
            }
		}catch (Exception e) {
			log.error("excuteRemoteMethod["+serviceWSdl+"] error:", e);
		}
		finally{
			if(client!=null){
				client.close();
				client = null;
				xfireClient.remove();
			}
		}
		return null;
	}
	
	public int getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	public int getSendTimeout() {
		return sendTimeout;
	}
	public void setSendTimeout(int sendTimeout) {
		this.sendTimeout = sendTimeout;
	}
	public void setHttpClient(HttpClientHelper httpClient) {
		this.httpClient = httpClient;
	}
}
