package com.ai.eduportal.remote;

import com.ai.frame.bean.InputObject;
import com.ai.frame.web.control.CallCoreService;
import common.ai.httpclient.tools.HttpClientHelper;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;

public class RemoteCallerFactory implements RemoteCallerFactoryI {
	private String xxtHttpurl;
	private String xxbHttpurl;
	private String tbkHttpurl;
	private HttpClientHelper httpClient;
	private CallCoreService callCoreService;
	private String logservice;
	private String logmethod;
	/**校讯通**/
	private String xencryptedKey;
	/**同步课堂**/
	private String tencryptedKey;
	/**中小学学习报**/
	private String zencryptedKey;
	private Logger logger = LoggerFactory.getOuterCallerLog(RemoteCallerFactory.class);
	
	/**
	 * remote http调用
	 * @param phone      手机号码
	 * @param serialNo   请求调用时的序列
	 * @param reqStr     请求参数
	 * @param respStr    返回参数
	 * @param useTimes   用时(ms)
	 */
	private void remoteCallerLog(final String phone,final String serialNo,final String reqStr,final String respStr,final long useTimes){
		new Thread(new Runnable() {
			public void run() {
				InputObject in = new InputObject();
				in.setService(logservice);
				in.setMethod(logmethod);
				in.getParams().put("appOperlog", serialNo);
				in.getParams().put("logOperator", phone);
				in.getParams().put("reqStr", reqStr);
				in.getParams().put("respStr", respStr);
				in.getParams().put("useTimes", String.valueOf(useTimes));
				callCoreService.execute(in);
			}
		}).start();
	}
	/* 
	 * 校讯通接口调用
	 */
	public String getXxtResponse(String phone,String serialNo){

		return getCallerResp(xxtHttpurl,phone,serialNo,TYPE_XXT);
	}
	/* 
	 * 学习报接口调用
	 */
	public String getXxbResponse(String phone,String serialNo){
		
		return getCallerResp(xxbHttpurl,phone,serialNo,TYPE_ZXXXXB);
	}
	/* 
	 * 同步课堂接口调用
	 */
	public String getTbkResponse(String phone,String serialNo){

		return getCallerResp(tbkHttpurl,phone,serialNo,TYPE_TBKT);
	}
	private String getCallerResp(String httpUrl,String phone,String serialNo,int type){
		CodeUtil codeUtil = new CodeUtil();
		if(TYPE_XXT == type){
			codeUtil.setEncryptedKey(xencryptedKey);
		}else if(TYPE_TBKT == type){
			codeUtil.setEncryptedKey(tencryptedKey);
		}else if(TYPE_ZXXXXB == type){
			codeUtil.setEncryptedKey(zencryptedKey);
		}
		
		
		String reqStr = codeUtil.getParamData(serialNo,phone);
		if(TYPE_TBKT == type){
			reqStr = codeUtil.getParamData1(serialNo,phone);
		}
		long start = System.currentTimeMillis();
		RemoteHttpCallerI caller = new RemoteHttpCaller(httpClient,httpUrl);
		String respStr = caller.getResponse(reqStr);
//		String respStr = caller.getResponse1("paraData=" + reqStr);
//		String respStr = "";
//		if(TYPE_ZXXXXB == type){
//			respStr = caller.getResponse1(reqStr);
//		}else{
//			respStr = caller.getResponse(reqStr);
//		}
		
		logger.debug("remote http called,url={},reqStr={},respStr={}", null, httpUrl,reqStr,respStr);
		remoteCallerLog(phone,serialNo,reqStr,respStr,System.currentTimeMillis() - start);
		
		return respStr;
	}
	public void setXxtHttpurl(String xxtHttpurl) {
		this.xxtHttpurl = xxtHttpurl;
	}
	public void setXxbHttpurl(String xxbHttpurl) {
		this.xxbHttpurl = xxbHttpurl;
	}
	public void setTbkHttpurl(String tbkHttpurl) {
		this.tbkHttpurl = tbkHttpurl;
	}
	public void setHttpClient(HttpClientHelper httpClient) {
		this.httpClient = httpClient;
	}
	public void setCallCoreService(CallCoreService callCoreService) {
		this.callCoreService = callCoreService;
	}
	public void setLogservice(String logservice) {
		this.logservice = logservice;
	}
	public void setLogmethod(String logmethod) {
		this.logmethod = logmethod;
	}
	public void setXencryptedKey(String xencryptedKey) {
		this.xencryptedKey = xencryptedKey;
	}
	public void setTencryptedKey(String tencryptedKey) {
		this.tencryptedKey = tencryptedKey;
	}
	public void setZencryptedKey(String zencryptedKey) {
		this.zencryptedKey = zencryptedKey;
	}
	
}
