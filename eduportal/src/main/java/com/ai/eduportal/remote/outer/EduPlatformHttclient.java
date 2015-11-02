package com.ai.eduportal.remote.outer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ai.frame.bean.OutputObject;
import com.ai.frame.web.util.SystemPropUtil;
import common.ai.httpclient.tools.HttpClientHelper;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

public class EduPlatformHttclient {
	private Logger logger = LoggerFactory.getCustomerLog(this.getClass(),"heEdulog");
	private Map<String,HttpClientHelper> clients = new HashMap<String,HttpClientHelper>();
	private int connectTimeout;
	private int requestTimeout;
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	public void setRequestTimeout(int requestTimeout) {
		this.requestTimeout = requestTimeout;
	}
	public void setReRequestTimes(int reRequestTimes) {
		this.reRequestTimes = reRequestTimes;
	}
	private int reRequestTimes;
	
	private HttpClientHelper getClientHelper(String url){
		HttpClientHelper client = clients.get(url);
		if(client == null){
			client = new HttpClientHelper();
			client.setConnectTimeout(connectTimeout);
			client.setRequestTimeout(requestTimeout);
			client.setReRequestTimes(reRequestTimes);
			
			setClientHelperUrls(client,url);
			
			clients.put(url, client);
		}
		
		return clients.get(url);
	}
	private void setClientHelperUrls(HttpClientHelper clientHelper,String url){
		String tmp = url.substring(7);
		if(url.indexOf("https://")>-1){
			tmp = url.substring(7);
		}
		int first = tmp.indexOf("/");
		String ipports  = tmp.substring(0,first);
		String services = tmp.substring(first+1);
		String [] iparr = ipports.split(":");
		String remoteIp = iparr[0];
		String remoteport = "80";
		if(iparr.length ==2){
			remoteport = iparr[1];
		}
		
		logger.info(remoteIp, "setClientHelperUrls.remoteIp is {}");
		logger.info(remoteport, "setClientHelperUrls.remoteport is {}");
		logger.info(services, "setClientHelperUrls.services is {}");
		clientHelper.setHttpReqUrl(remoteIp);
		clientHelper.setHttpReqPort(remoteport);
		clientHelper.setHttpReqServiceName(services);
	}

	public Map<String,String> getHeEduJumpCallback(Map<String,String> params){
		Map<String,String> rtnMp = new HashMap<String,String>();
		String jumpUrl = SystemPropUtil.getString("edu.he.jump.url");
		if(StringUtil.isEmpty(jumpUrl)){
			rtnMp.put(EduPlatformSvcI.EDU_RTNCODE, EduPlatformSvcI.EDU_CONFERR);
			rtnMp.put(EduPlatformSvcI.EDU_RTNMSG, "跳转到和教育的地址没有设置.");
		}else{
			HttpClientHelper clientHelper = getClientHelper(jumpUrl);
			
			String rtnJson = clientHelper.getHttpResonseJson(params, "edu.he.jump");
			
			rtnMp = convert2Mp(rtnJson);
		}
		return rtnMp;
		
	}
	public OutputObject queryHeEduCallback(Map<String,String> params,String uid,String listField){
		OutputObject rtnMp = new OutputObject();
		String propuid = "edu.he."+uid+".url";
		String syncurl = SystemPropUtil.getString(propuid);
		if(StringUtil.isEmpty(syncurl)){
			rtnMp.setReturnCode(EduPlatformSvcI.EDU_CONFERR);
			rtnMp.setReturnMessage("查询和教育的接口地址没有设置.["+uid+"]");
		}else{
			HttpClientHelper clientHelper = getClientHelper(syncurl);
			
			String rtnJson = clientHelper.getHttpResonseJson(params, propuid);
			
			rtnMp = convert2Output(rtnJson,listField);
		}
		return rtnMp;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private OutputObject convert2Output(String rtnJson,String listField){
		Map rtnmp = JsonUtil.convertJson2Object(rtnJson, Map.class);
		
		OutputObject rtn = new OutputObject();
		if(rtnmp!=null){
			Iterator mpit = rtnmp.keySet().iterator();
			while(mpit.hasNext()){
				String key = StringUtil.obj2TrimStr(mpit.next());
				if(!key.equals(listField)){
					String val = StringUtil.obj2TrimStr(rtnmp.get(key));
					
					rtn.getBean().put(key, val);
				}else{//list数组区域
					List datas = (List)rtnmp.get(key);
					if(datas!=null){
						rtn.getBeans().addAll(datas);
					}
					
				}
				
			}
		}
		return rtn;
	}
	public Map<String,String> syncHeEduCallback(Map<String,String> params,String uid){
		Map<String,String> rtnMp = new HashMap<String,String>();
		String propuid = "edu.he."+uid+".url";
		String syncurl = SystemPropUtil.getString(propuid);
		if(StringUtil.isEmpty(syncurl)){
			rtnMp.put(EduPlatformSvcI.EDU_RTNCODE, EduPlatformSvcI.EDU_CONFERR);
			rtnMp.put(EduPlatformSvcI.EDU_RTNMSG, "同步到和教育的接口地址没有设置.["+uid+"]");
		}else{
			HttpClientHelper clientHelper = getClientHelper(syncurl);
			
			String rtnJson = clientHelper.getHttpResonseJson(params, propuid);
			
			rtnMp = convert2Mp(rtnJson);
		}
		return rtnMp;
	}
	@SuppressWarnings("rawtypes")
	private Map<String,String> convert2Mp(String rtnJson){
		Map rtnmp = JsonUtil.convertJson2Object(rtnJson, Map.class);
		
		Map<String,String> rtn = new HashMap<String,String>();
		if(rtnmp!=null){
			Iterator mpit = rtnmp.keySet().iterator();
			while(mpit.hasNext()){
				String key = StringUtil.obj2TrimStr(mpit.next());
				String val = StringUtil.obj2TrimStr(rtnmp.get(key));
				
				rtn.put(key, val);
			}
		}
		return rtn;
	}
	
}
