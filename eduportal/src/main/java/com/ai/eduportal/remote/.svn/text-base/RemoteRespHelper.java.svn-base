package com.ai.eduportal.remote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

public class RemoteRespHelper {
	public static final String SUC = "1";
	private String respstr;
	private Map<String,Object> respMp;
	
	@SuppressWarnings("unchecked")
	public RemoteRespHelper(String respstr){
		this.respstr = respstr;
		respMp = JsonUtil.convertJson2Object(this.respstr, Map.class);
	}
	public static RemoteRespHelper getNewInstance(String respstr){
		return new RemoteRespHelper(respstr);
	}
	
	/**取得返回值，适用于老师的返回值*/
	@SuppressWarnings("unchecked")
	public String getTeaInfo(String fildName,int row){
		Map<String,Object> map = getTeaInfo(row);
		
		return StringUtil.obj2Str(map.get(fildName));
	}
	/**取得返回值，适用于学生的返回值*/
	public String getStdInfo(String fildName,int row){
		Map<String,Object> map = getStdInfo(row);
		return StringUtil.obj2Str(map.get(fildName));
	}
	/**取得返回值，适用于老师的返回值*/
	public Map<String,Object> getTeaInfo(int row){
		return getRtnInfo(Respkeys.TEAINFO,row);
	}
	/**取得返回值，适用于学生的返回值*/
	public Map<String,Object> getStdInfo(int row){
		return getRtnInfo(Respkeys.RETINFO,row);
	}
	/**取得返回值，适用于学生的返回值*/
	public int getTeaSize(String key){
		return getRtnInfoSize(Respkeys.TEAINFO);
	}
	/**取得返回值，适用于学生的返回值*/
	public int getStdSize(String key){
		return getRtnInfoSize(Respkeys.RETINFO);
	}
	/**取得返回值的数组长度，适用于学生返回值*/
	@SuppressWarnings("unchecked")
	public int getRtnInfoSize(String key){
		Object rtnInfo = respMp.get(key);
		if(rtnInfo instanceof List){
			List infoList = (List)rtnInfo;
			return infoList.size();
		}
		return -1;
	}
	@SuppressWarnings("unchecked")
	public Map<String,Object> getRtnInfo(String fildName,int row){
		Object rtnInfo = respMp.get(fildName);
		if(rtnInfo instanceof List){
			List infoList = (List)rtnInfo;
			if(row > infoList.size()-1) return null;
			Map<String,Object> info = (Map<String,Object>)infoList.get(row);
			return info;
		}
		return null;
	}
	
	/**取得调用结果的返回码*/
	@SuppressWarnings("unchecked")
	public String getRtnCode(){
		Map<String,Object> errInfo = (Map<String,Object>)respMp.get(Respkeys.ERRORINFO);
		return StringUtil.obj2Str(errInfo.get(Respkeys.RETCODE));
	}
	
	/**取得调用结果的返回信息*/
	@SuppressWarnings("unchecked")
	public String getRtnMsg(){
		Map<String,Object> errInfo = (Map<String,Object>)respMp.get(Respkeys.ERRORINFO);
		return StringUtil.obj2Str(errInfo.get(Respkeys.RETMSG));
	}
	
	/**公告数目*/
	public int getAnnouncementNum(){
		String totalCount = StringUtil.obj2Str(respMp.get(Respkeys.TOTALS));
		return StringUtil.str2Int(totalCount);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getListDatas(String fildName){
		Object rtnInfo = respMp.get(fildName);
		if(rtnInfo instanceof List){
			return (List<Map<String,Object>>)rtnInfo;
		}
		return null;
	}
	/**公告*/
	public List<Map<String,Object>> getAnnouncement(){
		return getListDatas(Respkeys.APPRETINFO);
	}
	/**作业列表*/
	public List<Map<String,Object>> getWorkContent(){
		return getListDatas(Respkeys.APPRETINFO);
	}
	/**科目成绩*/
	public List<Map<String,Object>> getCourseScore(){
		return getListDatas(Respkeys.APPRETINFO);
	}
	
	/**班级成绩*/
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getGradeScore(){
		if(!SUC.equals(getRtnCode())){
			return null;
		}
		Map<String,Object> respMp1   = (Map<String,Object>)respMp.get(Respkeys.APPRETINFO);
		respMp1.remove(Respkeys.SCOREDATAS);
		
		Map<String,Object> respMp2  = JsonUtil.convertJson2Object(this.respstr, Map.class);
		Map<String,Object> rtnInfo  = (Map<String,Object>)respMp2.get(Respkeys.APPRETINFO);
		List<Map<String,Object>> datas = (List<Map<String,Object>>)rtnInfo.get(Respkeys.SCOREDATAS);
		for(Map<String,Object> data:datas){
			data.putAll(respMp1);
		}
		
		return datas;
	}
	
	/**老师搜索考试列表*/
	public List<Map<String,Object>> getTeaExamList(){
		if(!"0".equals(getRtnCode())){
			return null;
		}
		List<Map<String,Object>> teaExamListNew = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> teaExamListByDate = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> teaExamList = getListDatas(Respkeys.APPRETINFO);
		for(Map<String,Object> map : teaExamList){
			String[] courseIds = map.get("courseId").toString().split(",");
			String[] courseNms = map.get("courseNm").toString().split(",");
			for(int i = 0; i < courseIds.length; i++){
				Map<String, Object> newMap = new HashMap<String, Object>();
				newMap.putAll(map);
				newMap.put("courseId", courseIds[i]);
				newMap.put("courseNm", courseNms[i]);
				teaExamListNew.add(newMap);
			}
		}
		//TODO	teaExamListByDate
//		for(Map<String,Object> map : teaExamListNew){
//			String examdate = map.get("EXAMDATE").toString();
//			String date = examdate.substring(0, 9);
//			String[] split = examdate.substring(0, 9).split("-");
//			String year = split[0];
//			String month = split[0];
//			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//			
//			
//			Map<String,Object> m = new HashMap<String,Object>();
//			m.put(date, list);
//			list.add(m);
//			
//		}
		return teaExamListNew;
	}
	
	public List getRtnList(String key){
		Object rtnInfo = respMp.get(key);
		if(rtnInfo instanceof List){
			List infoList = (List)rtnInfo;
			return infoList;
		}
		return null;
	}
	
	class Respkeys{
		static final String ERRORINFO = "errorInfo";
		static final String RETCODE   = "retCode";
		static final String RETMSG    = "retMsg";
		static final String RETINFO   = "retConInfo";
		static final String TEAINFO   = "retTeaInfo";
		static final String PHONE     = "phone";
		static final String TYPE      = "type";
		static final String USERNAME  = "userName";
		static final String AREA      = "area";
		static final String SUBAREA   = "sub_area";
		static final String SCHOOL    = "school";
		static final String GRADE     = "grade";
		static final String USERID    = "userId";
		static final String USERSEX   = "userSex";
		static final String COURSE    = "course";
		static final String TOTALS    = "totalCount";
		static final String APPRETINFO= "retInfo";
		static final String SCOREDATAS= "datas";
	}
}
