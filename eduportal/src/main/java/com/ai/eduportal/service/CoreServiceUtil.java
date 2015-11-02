package com.ai.eduportal.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.InitializingBean;

import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;

public class CoreServiceUtil implements InitializingBean{
	private String websvurl;
	private List<String> websvurls = new ArrayList<String>();
	private List<String> sucwebsvurls = new ArrayList<String>();
	private List<String> errwebsvurls = new ArrayList<String>();
	private Logger logger = LoggerFactory.getApplicationLog(CoreServiceUtil.class);
	private HttpClient client = new HttpClient();
	
	public void afterPropertiesSet() throws Exception {
		String tmp = websvurl.substring(7);
		int first = tmp.indexOf("/");
		
		String ipports  = tmp.substring(0,first);
		String services = tmp.substring(first);
		
		String [] iparr = ipports.split(";");
		for(String ipport:iparr){
			websvurls.add("http://" + ipport + services);
		}
		
		logArr(websvurls);
	}
	
	public boolean checkurls(String url){
		GetMethod method  = null;
		try {
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);// 设置连接超时时间为3秒
			method = new GetMethod(url); 
			int statusCode = client.executeMethod(method);// 状态200为OK状态
			return statusCode == HttpStatus.SC_OK;
		} catch (Exception e) {
			return false;
		}finally{
			if(method!=null){//释放连接资源
				method.releaseConnection();
	        }
		}
	}
	
	public synchronized String getRandomUrls(){
		int listSize = sucwebsvurls.size();
		if(listSize < 1) trimUrls();
		listSize = sucwebsvurls.size();
		
		int randomIndex = (int) (Math.random() * listSize);// 取得随机数
		if(randomIndex == listSize) randomIndex = randomIndex - 1;
		
		String appUrl = sucwebsvurls.get(randomIndex);
		return appUrl;
	}
	
	public synchronized void trimUrls(){
		System.out.println("-----------trimUrls---------------");
		sucwebsvurls.clear();
		errwebsvurls.clear();
		
		for(String url:websvurls){
			if(checkurls(url)){
				sucwebsvurls.add(url);
			}else{
				errwebsvurls.add(url);
			}
		}
		//logArr(sucwebsvurls);
		System.out.println("-----------trimUrls---------------"+errwebsvurls.size());
		logErrArr();
	}
	private void logArr(final List<String> websvurls){
		new Thread(new Runnable(){
			public void run() {
				int i = 0;
				for(String url:websvurls){
					logger.info("--->> 连接地址{}:{} .------------", null, String.valueOf(i),url);
					i++;
				}
			}
		}).start();
	}
	private void logErrArr(){
		final List<String> websvurls = errwebsvurls;
		new Thread(new Runnable(){
			public void run() {
				for(String url:websvurls){
					logger.error("--->> {} 连接不能访问{}.------------", null, url,"");
				}
			}
		}).start();
	}
	public void setWebsvurl(String websvurl) {
		this.websvurl = websvurl;
	}
	public static void main(String[] args) throws Exception {
		CoreServiceUtil u = new CoreServiceUtil();
		u.setWebsvurl("http://10.96.168.121:22100;10.96.168.121:22200;10.96.168.120:22100;10.96.168.120:22200/educore/services/IControlService?wsdl");
		u.afterPropertiesSet();
	}
}
