package com.ai.eduportal.remote.outer;

import java.util.Map;

import com.ai.frame.bean.OutputObject;

public class EduSyncRunner {
	private EduPlatformHttclient httpclient;
	private Map<String,String> params;
	private String uid;
	
	public EduSyncRunner(EduPlatformHttclient httpclient,Map<String,String> params,String uid){
		this.httpclient = httpclient;
		this.params = params;
		this.uid    = uid;
	}
	
	public OutputObject syncHeEduCallback(){
		OutputObject rtn = new OutputObject();
		try{
			Map<String,String> rtnBeans = httpclient.syncHeEduCallback(params,uid);
			
			rtn.setBean(rtnBeans);
			rtn.setReturnCode(rtnBeans.get(EduPlatformSvcI.EDU_RTNCODE));
			rtn.setReturnMessage(rtnBeans.get(EduPlatformSvcI.EDU_RTNMSG));
		}catch(Exception e){
			rtn.setReturnCode(EduPlatformSvcI.EDU_RTNERR);
			rtn.setReturnMessage("调用失败.");
		}
		return rtn;

	}

}
