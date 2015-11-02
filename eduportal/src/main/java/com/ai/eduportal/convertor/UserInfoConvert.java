package com.ai.eduportal.convertor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.eduportal.Util.Constants;
import com.ai.frame.bean.InputObject;
import com.ai.frame.bean.OutputObject;
import com.ai.frame.util.ControlConstants;
import com.ai.frame.web.convertor.CommonConvert;
import com.ai.frame.web.xml.bean.Parameter;

import common.ai.tools.JsonUtil;

public class UserInfoConvert extends CommonConvert{
	public String convertUser2List(InputObject input, OutputObject output,
			List<Parameter> parameters) {
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		List datas = output.getBeans();
		List<Map<String, String>> rows = this.convertListMap(datas, parameters);
		List<Map<String, String>> studentList=new ArrayList<Map<String,String>>();
		List<Map<String, String>> teacherList=new ArrayList<Map<String,String>>();
		for (Map<String, String> map : rows) {
			if("学生".equals(map.get("typeName"))){
				studentList.add(map);
			}else if("老师".equals(map.get("typeName"))){
				teacherList.add(map);
			}
		}
		gridMp.put("parents",studentList);
		gridMp.put("teacher",teacherList);
		if ((parameters != null) && (parameters.size() > 0)) {
			gridMp.put(this.getToKey("returnMessage", parameters),output.getReturnMessage());
			gridMp.put(this.getToKey("returnCode", parameters),output.getReturnCode());
		} else {
			gridMp.put("returnCode", output.getReturnCode());
		}
		return JsonUtil.convertObject2Json(gridMp);
	}
	
	public String convertListMap2Beans(InputObject input, OutputObject output,
			List<Parameter> parameters) {
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		List datas = output.getBeans();
		List<Map<String, String>> rows = this.convertListMap(datas, parameters);
		gridMp.put("beans",rows);
		if ((parameters != null) && (parameters.size() > 0)) {
			gridMp.put(this.getToKey("returnMessage", parameters),output.getReturnMessage());
			gridMp.put(this.getToKey("returnCode", parameters),output.getReturnCode());
		} else {
			gridMp.put("returnCode", output.getReturnCode());
		}
		return JsonUtil.convertObject2Json(gridMp);
	}
	
	public String convertList2UserInfo(InputObject inputObject, OutputObject outputObject,
			List<Parameter> parameters){
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		if (!"0000".equals(outputObject.getReturnCode())) {
			outputObject = new OutputObject();
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "调用crm接口出错");
			return JsonUtil.convertObject2Json(gridMp);
		}
//		if (outputObject.getBeans().size()==0) {
//			outputObject = new OutputObject();
//			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
//			gridMp.put("rtnMsg", "无角色信息");
//			return JsonUtil.convertObject2Json(gridMp);
//		}
		// 学生信息
		List<Map<String, String>> studenList = new ArrayList<Map<String, String>>();
		// 老师信息
		List<Map<String, String>> teaList = new ArrayList<Map<String, String>>();
		for (Map<String, String> map : outputObject.getBeans()) {
			//2015-5-11 选角色时候不显示家长信息
			if(String.valueOf(Constants.USER_TYPE.PARENT).equals(map.get("userTp"))){
				continue;
			}
			Map<String, String> rtnMap = new HashMap<String, String>();
			rtnMap.put("userId", map.get("userId"));
			rtnMap.put("userName",map.get("userNm"));
			rtnMap.put("city",map.get("area"));
			rtnMap.put("region",map.get("subArea"));
			rtnMap.put("schoolName",map.get("schoolNm"));
			rtnMap.put("schoolId",map.get("schoolId"));
			rtnMap.put("gradeName",map.get("gradeNm"));
			rtnMap.put("gradeId",map.get("gradeId"));
			rtnMap.put("sex",map.get("sex"));//1男 2女
			rtnMap.put("xxtUid",map.get("xxtuid"));
			//接口1代表老师 1老师 2学生 3家长
			//本地 1:学生 2:老师 3:家长
			if(Constants.CRM_USER_TYPE.TEACHER.equals(map.get("userTp"))){
				rtnMap.put("typeId", "2");
				rtnMap.put("imgUrl", "../theme/images/headIcon.png");
			}else{
				if(Constants.CRM_USER_TYPE.PARENT.equals(map.get("userTp"))){
					//保留家长角色,用于发信息
					rtnMap.put("typeId", "3");
				}else{
					rtnMap.put("typeId", "1");
				}
				//TODO 换掉路径
				rtnMap.put("imgUrl", "../theme/images/headIcon.png");
			}
			if(Constants.CRM_USER_TYPE.TEACHER.equals(map.get("userTp"))){
				teaList.add(rtnMap);
			}else{
				studenList.add(rtnMap);
			}
		}
		gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
		gridMp.put("rtnMsg", "call success");
		gridMp.put("parents", studenList);
		gridMp.put("teacher", teaList);
		return JsonUtil.convertObject2Json(gridMp);
	}
	
	public String convertRtnMsg(InputObject input, OutputObject output,
			List<Parameter> parameters) {
		StringBuffer rtn;
		try {
			Map<String,String>outMap = new HashMap<String,String>();
			if ((parameters != null) && (parameters.size() > 0)) {
				outMap.putAll(convertMap(output.getBean(), parameters));
				outMap.put(getToKey("returnCode", parameters),output.getReturnCode());
				outMap.put(getToKey("returnMessage", parameters),output.getReturnMessage());
			} else {
				outMap.put("returnCode", output.getReturnCode());
				outMap.put("returnMessage", output.getReturnMessage());
			}
			return JsonUtil.convertObject2Json(outMap);
		} catch (Exception e) {
			rtn = new StringBuffer();
			rtn.append("{\"returnCode\":").append("0").append(",\"returnMessage\":").append(e.getMessage()).append("}");
		}
		return rtn.toString();
	}
}
