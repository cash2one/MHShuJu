package com.ai.eduportal.remote.mock;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.ai.eduportal.remote.app.AppRemoteCallerI;

import common.ai.resource.tools.ClassPathResource;
import common.ai.resource.tools.Resource;
import common.ai.tools.IOUtil;
import common.ai.tools.StringUtil;

public class AppRemoteCallerMock implements AppRemoteCallerI {

	public String getAnnouncement(String reqStr,String schoolID) {
		return null;
	}

	public String getAnnouncementNum(String reqStr ,String schoolID) {
		return "";
	}

	public String getFriendAnnouncement(String reqStr,String schoolId) {
		return null;
	}

	public String getGradeAnnouncement(String gradeID, int startPg, int limit) {
		return null;
	}

	public String getGradeScore(String reqStr,String gradeId) {
		return null;
	}

	public String getStCourseHisScoreLs(String reqStr,String userId) {
		return null;
	}

	public String getStCourseScoreLs(String reqStr,String userId) {
		return null;
	}

	public String getStScoreLs(String reqStr,String schoolId) {
		return null;
	}

	public String getTeScoreLs(String reqStr,String userId) {
		return null;
	}

	public String getWorkContent(String reqStr,String userId) {
		
//		Map<String,Object> respMp = getWorkMp(userId,type);
		
		//return FreeMarkerUtil.getInstance().template2Str(respMp, "app/WorkContent.ftl");
//		String jsonFile = "STWorkContent.json";
//		if(StringUtil.str2Int(type) == 2){
//			jsonFile = "WorkContent.json";
//		}
//		return getFileContent2Str(jsonFile);
		return null;
	}

	public String sendAnnouncement(String reqStr,String userId) {
		return null;
	}
//	private Map<String,Object> getWorkMp(String userId, String type){
//		Map<String,Object> resp = new HashMap<String, Object>();
//		resp.putAll(getErroInfo());
//		
//		List<Map<String,Object>> workList = new ArrayList<Map<String,Object>>();
//		for(int i=1 ;i<6;i++){
//			workList.add(getStWorkMp(i,userId,type));
//		}
//		resp.put("workList", workList);
//		
//		return resp;
//	}
	protected Map<String,Object> getErroInfo(){
		Map<String,Object> resp = new HashMap<String, Object>();
		resp.put("retCode", "1");
		resp.put("retMsg",  "调用接口成功");
		return resp;
	}
//	private Map<String,Object> getStWorkMp(int row,String userId, String type){
//		Map<String,Object> resp = new HashMap<String, Object>();
//		resp.put("title", userId+"10月1号语文作业"+row);
//		resp.put("estime", "20");
//		resp.put("id", "1000"+(row%2));
//		resp.put("date", "2015-01-21 "+row);
//		resp.put("course", type+"_语文"+row);
//		resp.put("grade", "郑州小学三年级五班"+row);
//		resp.put("gradeId", "20002");
//		resp.put("teacher", "李老师"+row);
//		resp.put("require", "记录时长，完整性检查");
//		resp.put("teacher", "李老师"+row);
//		resp.put("school",  "郑州小学"+row);
//		resp.put("content", "1:背诵<<唐诗300首>>P115页 2:背诵<<唐诗300首>>P118页 3:背诵<<唐诗300首>>P125页");
//		resp.put("attach",  "['http://www.sina.com.cn','http://www.baidu.com']");
//		
//		return resp;
//	}
	private String getFileContent2Str(String fileName){
		Resource    rs = new ClassPathResource("freeMarker/ftl/app/"+fileName);
		InputStream in = rs.getInputStream();
		String str;
		try {
			str = new String(IOUtil.inputStream2Bytes(in, IOUtil.BUFFER),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			str="";
		}
		
		return str;
	}

	public String sendPicAnnouncement(String reqStr, String fileName,
			File msgFile, String userId) {
		return null;
	}

	
}
