package com.ai.eduportal.action;

import java.util.HashMap;
import java.util.Map;

import com.ai.eduportal.service.ApkpackageService;
import com.ai.frame.bean.InputObject;
import common.ai.tools.JsonUtil;


public class UploadAppAction extends Strut2BaseAction {
	
	public void setApkservice(ApkpackageService apkservice) {
		this.apkservice = apkservice;
	}
	private ApkpackageService apkservice;
	
	public String excute(){
		InputObject orinputObj = this.getInputObject();
		try {
			boolean upzip = apkservice.processApkZip(orinputObj);
			Map<String,String> appDetails = new HashMap<String,String>();
			if(upzip){
				appDetails.put("rtnMsg", "上传成功.");
				appDetails.put("rtnCode", "1");
			}else{
				appDetails.put("rtnMsg", "上传失败.");
				appDetails.put("rtnCode", "0");
			}
			String sendJson = JsonUtil.convertObject2Json(appDetails);
			
			sendJson(sendJson,true);
		} catch (Exception e) {
			//e.printStackTrace();
			
			Map<String,String> appDetails = new HashMap<String,String>();
			appDetails.put("rtnCode", "0");
			String msg = e.getMessage();
			if(msg!=null && msg.indexOf("上传包的")>-1){
				appDetails.put("rtnMsg", msg);
			}else{
				appDetails.put("rtnMsg", "上传失败");
			}
			String sendJson = JsonUtil.convertObject2Json(appDetails);
			
			sendJson(sendJson,true);
		}
		
		return null;
	}

	
}
