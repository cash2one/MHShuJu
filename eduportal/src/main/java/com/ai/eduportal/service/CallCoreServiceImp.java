package com.ai.eduportal.service;

import com.ai.eduportal.Util.XfireClientHelper;
import com.ai.frame.bean.InputObject;
import com.ai.frame.bean.OutputObject;
import com.ai.frame.web.control.CallCoreService;
import com.ai.frame.web.control.impl.CallCoreServiceImpl;
import com.ai.frame.web.util.SystemPropUtil;
import common.ai.httpclient.tools.HttpClientHelper;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

public class CallCoreServiceImp implements CallCoreService {
	private HttpClientHelper httpClient;
	private CoreServiceUtil coreServiceUtil;
	private Logger logger = LoggerFactory.getApplicationLog(CallCoreServiceImpl.class);
	public OutputObject execute(InputObject inputObject) {
		StringBuilder builder = new StringBuilder();
		if (inputObject != null) {
			builder.append(inputObject.getServerIp()).append(":");
			builder.append(inputObject.getUserId()).append(":");
			builder.append(inputObject.getService()).append(":");
			builder.append(inputObject.getMethod());
		}
		
		try{
			logger.info(builder.toString(),"Call remote method Start:{}");
			
			long start = System.currentTimeMillis();
			if(StringUtil.isEmpty(inputObject.getService()) || StringUtil.isEmpty(inputObject.getMethod())){
			    logger.error(builder.toString(),"call remote's service id is null or method is null.{}");
			    return new OutputObject();
			}
			
			String json   = JsonUtil.convertObject2Json(inputObject);
//			String weburl = SystemPropUtil.getString("remoteServiceWsdl");
			String weburl = coreServiceUtil.getRandomUrls();
			logger.info(weburl,"Call remote app url is:{}");
			
			XfireClientHelper client = new XfireClientHelper();
			client.setHttpClient(httpClient);
			client.setConnectTimeout(StringUtil.str2Int(SystemPropUtil.getString("webservice.connections.timeout"),20000));
			client.setSendTimeout(StringUtil.str2Int(SystemPropUtil.getString("webservice.connections.timeout"),300000));
			
//			String result = client.excuteRemoteMethod2(weburl, "execute", json);
			
			String result = client.excuteRemoteMethod(weburl, "execute", json);
			OutputObject value = JsonUtil.convertJson2Object(result,OutputObject.class);
			
			long end = System.currentTimeMillis();
			
			logger.info(builder.toString(), String.valueOf(end - start),"Call remote method End,param:{},used:{} ms");
			return value;
		}catch(Exception e){
			logger.error(builder.toString(),"Call remote method Error.params:{} , errors:{}.", e);
		}
		return new OutputObject();
	}

	public void setHttpClient(HttpClientHelper httpClient) {
		this.httpClient = httpClient;
	}

	public void setCoreServiceUtil(CoreServiceUtil coreServiceUtil) {
		this.coreServiceUtil = coreServiceUtil;
	}
}
