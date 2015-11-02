package com.ai.eduportal.remote.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.eduportal.remote.CodeUtil;
import com.ai.eduportal.remote.RemoteCallerFactoryI;
import com.ai.eduportal.remote.RemoteHttpCaller;
import com.ai.eduportal.remote.freeMarker.FreeMarkerUtil;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;

public class RemoteCallerFactoryMock implements RemoteCallerFactoryI {
	private Logger log = LoggerFactory.getOuterCallerLog(RemoteHttpCaller.class);
//	private static final String reqFtl = "req.ftl";
//	private static final String respStFtl = "respStudent.ftl";
	private static final String respThFtl = "respTeacher.ftl";
	/* 
	 * 校讯通接口调用
	 */
	public String getXxtResponse(String phone,String serialNo){
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("phone", phone);
//		map.put("userType", userType);
//		String reqStr = FreeMarkerUtil.getInstance().template2Str(map, reqFtl);
		CodeUtil codeUtil = new CodeUtil();
		codeUtil.setEncryptedKey("wpdk3k56wpdk3k56wpdk3k56");
		String reqStr = codeUtil.getParamData(serialNo,phone);
		log.info(reqStr, "getXxtResponse.req is {}");
		Map<String,Object> respMp = getRespMp2(reqStr);
//		if(userType !=1){
//			return FreeMarkerUtil.getInstance().template2Str(respMp, respThFtl);
//		}else{
//			return FreeMarkerUtil.getInstance().template2Str(respMp, respStFtl);
//		}
		return FreeMarkerUtil.getInstance().template2Str(respMp, respThFtl);
	}
	private Map<String,Object> getRespMp(String reqStr){
		Map<String,Object> resp = new HashMap<String, Object>();
		resp.putAll(getErroInfo());
//		if(userType !=1){
//			resp.putAll(getRetInfo(userType));
//		}else{
//			resp.putAll(getStRetInfo(userType));
//		}
		resp.putAll(getStRetInfo());
		return resp;
	}
	
	private Map<String,Object> getRespMp2(String reqStr){
		Map<String,Object> resp = new HashMap<String, Object>();
		resp.putAll(getErroInfo2());
//		if(userType !=1){
//			resp.putAll(getRetInfo(userType));
//		}else{
//			resp.putAll(getStRetInfo(userType));
//		}
		resp.putAll(getStRetInfo());
		return resp;
	}
	
	private Map<String,Object> getErroInfo2(){
		Map<String,Object> resp = new HashMap<String, Object>();
		resp.put("retCode", "0");
		resp.put("retMsg",  "调用接口成功");
		return resp;
	}
	
	private Map<String,Object> getRetInfo(int row,int userType){
		Map<String,Object> resp = new HashMap<String, Object>();
		resp.put("phone", "13111111111");
		resp.put("type",  userType);
		resp.put("userName",  userType==2?"老师"+row:"家长"+row);
		resp.put("area",  "XXX市");
		resp.put("sub_area",  "XXX市");
		resp.put("school",  "XXX中学");
		//resp.put("course",  new String[]{"语文","地理"});
		resp.put("userId",  "10000001"+row);
		//resp.put("userSex", "1");
		return resp;
	}
	private Map<String,Object> getStRetInfo(){
		Map<String,Object> resp = new HashMap<String, Object>();
		
		List<Map<String,Object>> users = new ArrayList<Map<String,Object>>();
		for(int i=1 ;i<5;i++){
			users.add(getStRetInfo(i,1));
		}
		resp.put("userList", users);
		
		List<Map<String,Object>> teaList = new ArrayList<Map<String,Object>>();
		for(int i=1 ;i<3;i++){
			teaList.add(getRetInfo(i,i%2==0?2:3));
		}
		resp.put("teaList", teaList);
		
		return resp;
	}
	private Map<String,Object> getStRetInfo(int row,int userType){
		Map<String,Object> resp = new HashMap<String, Object>();
		resp.put("phone", "13111111111");
		resp.put("type",  userType);
		resp.put("userName",  "学生"+row);
		resp.put("area",  "XXX市");
		resp.put("sub_area",  "XXX市");
		resp.put("school",  "XXX中学");
		resp.put("grade",   "XXX年级XXX班级");
		resp.put("userId",  "8000001"+row);
//		if(row % 2 ==0){
//			resp.put("userSex", "1");
//		}else{
//			resp.put("userSex", "0");
//		}
		
		return resp;
	}
	private Map<String,Object> getErroInfo(){
		Map<String,Object> resp = new HashMap<String, Object>();
		resp.put("retCode", "1");
		resp.put("retMsg",  "调用接口成功");
		return resp;
	}
	/* 
	 * 学习报接口调用
	 */
	public String getXxbResponse(String phone,String serialNo){
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("phone", phone);
//		map.put("userType", userType);
//		String reqStr = FreeMarkerUtil.getInstance().template2Str(map, reqFtl);
		CodeUtil codeutil = new CodeUtil();
		codeutil.setEncryptedKey("wpdk3k56wpdk3k56wpdk3k56");
		String reqStr = codeutil.getParamData(serialNo,phone);
		Map<String,Object> respMp = getRespMp(reqStr);
//		if(userType !=1){
//			return FreeMarkerUtil.getInstance().template2Str(respMp, respThFtl);
//		}else{
//			return FreeMarkerUtil.getInstance().template2Str(respMp, respStFtl);
//		}
		String resultStr=FreeMarkerUtil.getInstance().template2Str(respMp, respThFtl);
		resultStr=resultStr.replaceAll("USERID", "userId");
		resultStr=resultStr.replaceAll("USERNAME", "userName");
		return resultStr;
	}
	/* 
	 * 同步课堂接口调用
	 */
	public String getTbkResponse(String phone,String serialNo){
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("phone", phone);
//		map.put("userType", userType);
//		String reqStr = FreeMarkerUtil.getInstance().template2Str(map, reqFtl);
		CodeUtil codeutil = new CodeUtil();
		codeutil.setEncryptedKey("wpdk3k56wpdk3k56wpdk3k56");
		String reqStr = codeutil.getParamData(serialNo,phone);
		Map<String,Object> respMp = getRespMp(reqStr);
//		if(userType !=1){
//			return FreeMarkerUtil.getInstance().template2Str(respMp, respThFtl);
//		}else{
//			return FreeMarkerUtil.getInstance().template2Str(respMp, respStFtl);
//		}
		return FreeMarkerUtil.getInstance().template2Str(respMp, respThFtl);
	}
}
