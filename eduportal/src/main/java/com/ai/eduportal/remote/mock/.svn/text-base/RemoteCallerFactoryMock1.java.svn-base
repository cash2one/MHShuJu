package com.ai.eduportal.remote.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.eduportal.remote.CodeUtil;
import com.ai.eduportal.remote.RemoteCallerFactory;
import com.ai.eduportal.remote.RemoteCallerFactoryI;
import com.ai.eduportal.remote.RemoteHttpCaller;
import com.ai.eduportal.remote.RemoteHttpCallerI;
import com.ai.eduportal.remote.freeMarker.FreeMarkerUtil;
import com.ai.frame.bean.InputObject;
import com.ai.frame.web.control.CallCoreService;
import common.ai.httpclient.tools.HttpClientHelper;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;

public class RemoteCallerFactoryMock1 implements RemoteCallerFactoryI{
	private static final String respThFtl = "respTeacher.ftl";
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
	private void remoteCallerLog(String phone,String serialNo,String reqStr,String respStr,long useTimes){
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
	/* 
	 * 校讯通接口调用
	 */
	public String getXxtResponse(String phone,String serialNo){

//		return getCallerResp(xxtHttpurl,phone,serialNo,TYPE_XXT);
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("phone", phone);
//		map.put("userType", userType);
//		String reqStr = FreeMarkerUtil.getInstance().template2Str(map, reqFtl);
		CodeUtil codeUtil = new CodeUtil();
		codeUtil.setEncryptedKey("wpdk3k56wpdk3k56wpdk3k56");
		String reqStr = codeUtil.getParamData(serialNo,phone);
//		log.info(reqStr, "getXxtResponse.req is {}");
		Map<String,Object> respMp = getRespMp2(reqStr,phone);
//		if(userType !=1){
//			return FreeMarkerUtil.getInstance().template2Str(respMp, respThFtl);
//		}else{
//			return FreeMarkerUtil.getInstance().template2Str(respMp, respStFtl);
//		}
		if(phone.equals("15093265532")){
			return"{ \"retTeaInfo\": [ { \"TYPE\": 1, \"USERID\": 6087160, \"USERNAME\": \"刘海丽\", \"SCHOOL\": \"测试大学\", \"AREA\": \"郑州\", \"SUB_AREA\": \"金水区\" } ], \"retConInfo\": [ ], \"_rc\": \"success\", \"errorInfo\": { \"retMsg\": \"调用成功！\", \"retCode\": 0 } } ";
		}else if(phone.equals("15093265533")){
			return "{ \"retTeaInfo\": [ ], \"retConInfo\": [ { \"TYPE\": 2, \"USERID\": 58441357, \"GRADE\": \"技术\", \"USERNAME\": \"刘海丽1\", \"SCHOOL\": \"郑州家长俱乐部学校\", \"AREA\": \"郑州\", \"SUB_AREA\": \"二七区\" } ], \"_rc\": \"success\", \"errorInfo\": { \"retMsg\": \"调用成功！\", \"retCode\": 0 } } ";
		}else if(phone.equals("15093265534")){
			return "{ \"retTeaInfo\": [ ], \"retConInfo\": [ ], \"_rc\": \"success\", \"errorInfo\": { \"retMsg\": \"调用成功！\", \"retCode\": 0 } } ";
		}
		return FreeMarkerUtil.getInstance().template2Str(respMp, respThFtl);
	}
	private Map<String,Object> getRespMp2(String reqStr,String phone){
		Map<String,Object> resp = new HashMap<String, Object>();
		resp.putAll(getErroInfo2());
//		if(userType !=1){
//			resp.putAll(getRetInfo(userType));
//		}else{
//			resp.putAll(getStRetInfo(userType));
//		}
		resp.putAll(getStRetInfo(phone));
		return resp;
	}
	private Map<String,Object> getErroInfo2(){
		Map<String,Object> resp = new HashMap<String, Object>();
		resp.put("retCode", "0");
		resp.put("retMsg",  "调用接口成功");
		return resp;
	}
	
	private Map<String,Object> getRetInfo(int row,int userType,String phone){
		Map<String,Object> resp = new HashMap<String, Object>();
		resp.put("phone", phone);
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
	private Map<String,Object> getStRetInfo(String phone){
		Map<String,Object> resp = new HashMap<String, Object>();
		
		List<Map<String,Object>> users = new ArrayList<Map<String,Object>>();
		for(int i=1 ;i<5;i++){
			users.add(getStRetInfo(i,1,phone));
		}
		resp.put("userList", users);
		
		List<Map<String,Object>> teaList = new ArrayList<Map<String,Object>>();
		for(int i=1 ;i<3;i++){
			teaList.add(getRetInfo(i,i%2==0?2:3,phone));
		}
		resp.put("teaList", teaList);
		
		return resp;
	}
	private Map<String,Object> getStRetInfo(int row,int userType,String phone){
		Map<String,Object> resp = new HashMap<String, Object>();
		resp.put("phone", phone);
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
//	private Map<String,Object> getErroInfo(){
//		Map<String,Object> resp = new HashMap<String, Object>();
//		resp.put("retCode", "1");
//		resp.put("retMsg",  "调用接口成功");
//		return resp;
//	}
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
