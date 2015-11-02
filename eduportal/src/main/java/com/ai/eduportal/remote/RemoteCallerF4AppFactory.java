package com.ai.eduportal.remote;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.ai.eduportal.Util.Constants;
import com.ai.eduportal.bean.HEAUser;
import com.ai.eduportal.interceptor.InputObjectAdpter;
import com.ai.eduportal.remote.app.AppRemoteCallerI;
import com.ai.eduportal.service.CmsServiceClient;
import com.ai.frame.bean.InputObject;
import com.ai.frame.bean.OutputObject;
import com.ai.frame.util.ControlConstants;
import com.ai.frame.util.SpringContextHelper;
import com.ai.frame.web.control.CallCoreService;
import com.ai.frame.web.util.ControlFactory;
import com.ai.frame.web.util.SystemPropUtil;
import com.ai.frame.web.xml.bean.Action;
import com.ai.frame.web.xml.bean.Output;
import com.ai.frame.web.xml.bean.Parameter;
import common.ai.tools.ClassUtil;
import common.ai.tools.DateUtil;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

public class RemoteCallerF4AppFactory implements RemoteCallerF4AppFactoryI {
	private static final String APPACTION = "/app/action/commonOuter";
	private static final String APPACTION2 = "/app/action/commonOuterNormal";
	private static final boolean RemoteCallerF4AppFactory = false;
	private AppRemoteCallerI appRemoteCaller;
	private CallCoreService callCoreService;
//	private Logger logger = LoggerFactory.getOuterCallerLog(RemoteCallerF4AppFactory.class);
	private CmsServiceClient cmsService;
	private CodeUtil codeUtil;

	private Map<String,Map<String,String>> list2Map(List<Map<String,String>> userFinishedWorks){
		Map<String,Map<String,String>> rtnmp = new HashMap<String,Map<String,String>>();
		if(userFinishedWorks!=null){
			String workId = "workId";
			String userId = "userId";
			for(Map<String,String> map : userFinishedWorks){
				String mapKey = map.get(userId) + "_" + map.get(workId);
				rtnmp.put(mapKey, map);
			}
		}
		return rtnmp;
	}
	private void setWorkstate(List<Map<String,Object>> works,Map<String,Map<String,String>> finishWorks){
		if(works!=null){
			for(Map<String,Object> work:works){
				String mapKey = StringUtil.obj2TrimStr(work.get("studentId"))+"_"+StringUtil.obj2TrimStr(work.get("id"));
				if(finishWorks.containsKey(mapKey)){
					work.put("worktime",  finishWorks.get(mapKey).get("workTime"));
					work.put("workstate", finishWorks.get(mapKey).get("workState"));
				}
			}
		}
	}
	/***输出到页面json转换方法调用**/
	protected String convert(List<Map<String,Object>> datas,String uid){
		Action appaction = ControlFactory.getControl().getAction(APPACTION);
		if(appaction!=null){
			Output appoutput = appaction.getOutput(uid);
			List<Parameter> parameters = appoutput.getParameters();
			String convertor = appoutput.getConvertor();
			String method    = appoutput.getMethod();
			if(StringUtil.isEmpty(convertor)){
				convertor = "com.ai.eduportal.convertor.AppOuterConvert";
			}
			if(StringUtil.isEmpty(method)){
				return null;
			}
			
			Class<?>[] paramcls = new Class<?>[]{List.class,List.class};
			Object[]  paramvals = new Object[]{datas,parameters};
			
			return ClassUtil.invokMethod(String.class, convertor, method, paramcls, paramvals);
		}
		return null;
	}
	private Map<String,Map<String,String>> gradelist2Map(List<Map<String,String>> gradeFinishedWorks){
		Map<String,Map<String,String>> rtnmp = new HashMap<String,Map<String,String>>();
		if(gradeFinishedWorks!=null){
			String workId = "workId";
			String gradeId= "gradeId";
			for(Map<String,String> map : gradeFinishedWorks){
				String mapkey = map.get(gradeId) + "-" + map.get(workId);
				rtnmp.put(mapkey, map);
			}
		}
		return rtnmp;
	}
	private void setGradeWorkstate(List<Map<String,Object>> works,Map<String,Map<String,String>> finishWorks){
		if(works!=null){
			for(Map<String,Object> work:works){
				String mapkey = work.get("gradeId") + "-" + work.get("id");
				if(finishWorks.containsKey(mapkey)){
					work.put("worktime",  finishWorks.get(mapkey).get("workTime"));
					work.put("workstate", finishWorks.get(mapkey).get("workState"));
					
					work.put("gradeNum",   finishWorks.get(mapkey).get("gradeNum"));
					work.put("finishedNum",finishWorks.get(mapkey).get("finishedNum"));
				}
			}
		}
	}

//	/**取得班级作业**/
//	public String getGradeWorkContent(InputObject inobj){
//		
//		String userId = inobj.getParams().get("userId");
//		String type   = inobj.getParams().get("type");
//		int startPg   = StringUtil.str2Int(inobj.getParams().get("start"));
//		int limit     = StringUtil.str2Int(inobj.getParams().get("limit"));
//		RemoteRespHelper helper = new RemoteRespHelper(appRemoteCaller.getWorkContent(userId,type,startPg,limit));
//		
//		List<Map<String,Object>> works = helper.getWorkContent();
//		String workIds = getWorkIds(works)[0];
//		inobj.getParams().put("workIds", workIds);
//		
//		OutputObject outobj = callCoreService.execute(inobj);
//		List<Map<String,String>> gradeFinishedWorks = outobj.getBeans();
//		
//		Map<String,Map<String,String>> finishWorks = gradelist2Map(gradeFinishedWorks);
//		setGradeWorkstate(works,finishWorks);
//		
//		return convert(works,inobj.getParams().get(ControlConstants.UID));
//	}
	private String[] getWorkIds(List<Map<String,Object>> works){
		List<String> workIds = new ArrayList<String>();
		List<String> userIds = new ArrayList<String>();
		if(works!=null){
			for(Map<String,Object> work:works){
				String workId = StringUtil.obj2TrimStr(work.get("id"));
				String userId = StringUtil.obj2TrimStr(work.get("studentId"));
				if(!workIds.contains(workId))workIds.add(workId);
				if(!userIds.contains(userId))userIds.add(userId);
			}
		}
		String workId = StringUtil.arrayJoin(workIds.toArray(new String[workIds.size()]), ",");
		String userId = StringUtil.arrayJoin(userIds.toArray(new String[userIds.size()]), ",");
		return new String[]{workId,userId};
	}
      /***
	 * 作业列表接口联调
	 * @param userId   xxt侧对应的用户ID
	 * @param type     用户类型
	 *                 1:代表老师，2:代表家长/学生
	 * 
	 * */
//	public String getWorkContent(InputObject inobj){
//		String userId = inobj.getParams().get("userId");
//		String type   = inobj.getParams().get("type");
//		int startPg   = StringUtil.str2Int(inobj.getParams().get("start"));
//		int limit     = StringUtil.str2Int(inobj.getParams().get("limit"));
//		RemoteRespHelper helper = new RemoteRespHelper(appRemoteCaller.getWorkContent(userId,type,startPg,limit));
//		
//		List<Map<String,Object>> works = helper.getWorkContent();
//		String[] tmp   = getWorkIds(works);
//		String workIds = tmp[0];
//		String userIds = tmp[1];
//		inobj.getParams().put("workIds", workIds);
//		inobj.getParams().put("userId", userIds);
//		
//		OutputObject outobj = callCoreService.execute(inobj);
//		List<Map<String,String>> userFinishedWorks = outobj.getBeans();
//		
//		Map<String,Map<String,String>> finishWorks = list2Map(userFinishedWorks);
//		setWorkstate(works,finishWorks);
//		
//		return convert(works,inobj.getParams().get(ControlConstants.UID));
//	}

	public void setCallCoreService(CallCoreService callCoreService) {
		this.callCoreService = callCoreService;
	}

	public void setCmsService(CmsServiceClient cmsService) {
		this.cmsService = cmsService;
	}

	// 角色切换-关爱界面
	public String getUserRoleInfo(InputObject inputObject) {
		String mobile = inputObject.getParams().get("mobile");
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		if (StringUtil.isEmpty(mobile)) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "参数错误");
			return JsonUtil.convertObject2Json(gridMp);
		}
		//获取用户信息
		HttpSession session =getSession(true);
		HEAUser heaUser = null;
		// session有数据
		if (session != null) {
			heaUser = (HEAUser) session.getAttribute(HEAUser.HEA_CURRENT_USER);
			if(heaUser!=null){
				gridMp.put("userInfo", JsonUtil.convertBean2Map(heaUser));
			}
		} 
		if (heaUser==null){
			OutputObject outputObject =callCoreService.execute(inputObject);
			//调用crm接口失败
			if(ControlConstants.RTNCODE_ERR.equals(outputObject.getReturnCode())){
				heaUser=new HEAUser();
				heaUser.setMobile(Long.parseLong(mobile));
			}else{
				heaUser=new HEAUser();
				ClassUtil.setPropValFromMap(outputObject.getBean(), heaUser);
				heaUser.setMobile(Long.parseLong(mobile));
				//调用接口失败下次继续调接口
				session.setAttribute(HEAUser.HEA_CURRENT_USER,heaUser);//用户信息放入session
			}
		}
		//获取新品推荐 8个
		Map<String, Object> allHotNewApp =cmsService.getAllHotNewApp();
		List<Map<String,String>> newApps = (List<Map<String, String>>) allHotNewApp.get("newApps");
		List<Map<String,String>> reList=new ArrayList<Map<String,String>>();
		int i=0;
		if(newApps!=null){
			Iterator<Map<String, String>> it = newApps.iterator();
			while(it.hasNext()){
				Map<String, String> map=it.next();
				map.remove("appDesc");//删除不需要的字段
				reList.add(map);
				i++;
				if(i==8){
					break;
				}
			}
		}
		gridMp.put("newApps", reList);
		Map<String, Integer> notifyMap=new HashMap<String, Integer>();
		if(heaUser.getSchoolId()==0){
			notifyMap.put("noticeNum", 0);//公告
		}else{
			String schoolId =String.valueOf(heaUser.getSchoolId());
			String cacheKey=mobile+"#"+schoolId;
			//优先从缓存取,公告数量缓存，详情不缓存
			String noticeNumStr=SpringContextHelper.instance.getOscache().get(cacheKey);
			//缓存无数据
			if(StringUtil.isEmpty(noticeNumStr)){
				InputObject inobj=new InputObject();
				inobj.setService("orderService");
				inobj.setMethod("getEduCallSerialNo");
				OutputObject outobj = callCoreService.execute(inobj);
				String serialNo = outobj.getReturnMessage();
				String key = Constants.XXT_SECRET.AnnounNum;
				String schoolIdEncrypted = codeUtil.lettlerEncrypted2(schoolId, key);
				String param = "&schoolId="+schoolIdEncrypted;
				String reqStr = codeUtil.getParamData2(serialNo,schoolId,key,param);
				RemoteRespHelper helper = new RemoteRespHelper(String.valueOf(appRemoteCaller.getAnnouncementNum(reqStr,schoolId)));
				if(!helper.getRtnCode().equals("0")){
					notifyMap.put("noticeNum", 0);//公告
				}else{
					notifyMap.put("noticeNum", helper. getAnnouncementNum());//公告
					//放入缓存
					SpringContextHelper.instance.getOscache().put(cacheKey,String.valueOf(notifyMap.get("noticeNum")));
				}
			}else{
				notifyMap.put("noticeNum",StringUtil.str2Int(noticeNumStr));//公告
			}
		}
		notifyMap.put("homeWorkNum", 0);//作业
		notifyMap.put("schoolRptNum", 0);//成绩单
		notifyMap.put("scheduleNum", 0);//课程表
		notifyMap.put("seatChartNum", 0);//座位表
		gridMp.put("notifyInfo", notifyMap);
		gridMp.put("userInfo", heaUser);
		gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
		gridMp.put("rtnMsg", "success");
		return JsonUtil.convertObject2Json(gridMp);
	}
	
	// 角色切换 保存切换信息
	public String saveHeaChoiceRole(InputObject inputObject) {
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		HttpSession session = getSession(false);
		// if (session == null) {
		// gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
		// gridMp.put("rtnMsg", "请先登录");
		// return JsonUtil.convertObject2Json(gridMp);
		// }
		OutputObject outputObject = callCoreService.execute(inputObject);
		if (ControlConstants.RTNCODE_ERR.equals(outputObject.getReturnCode())) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", outputObject.getReturnMessage());
		} else {
			HEAUser heaUser = new HEAUser();
			ClassUtil.setPropValFromMap(inputObject.getParams(), heaUser);
			heaUser.setOrgId(heaUser.getSchoolId());
			if (heaUser.getTypeId() != 2) {
				heaUser.setImgUrl("../theme/images/headIcon.png");
			} else {
				heaUser.setImgUrl("../theme/images/headIcon.png");
			}
			session.setAttribute(HEAUser.HEA_CURRENT_USER, heaUser);
			String ptInfo = outputObject.getBusiCode();
			if (!StringUtil.isEmpty(ptInfo)) {
				// 保存当前学生的家长id
				Map<String, String> ptUserMap = JsonUtil.convertJson2Object(ptInfo, Map.class);
				HEAUser ptUser = new HEAUser();
				ClassUtil.setPropValFromMap(ptUserMap, ptUser);
				session.setAttribute(HEAUser.HEA_PT_USER, ptUser);
			}
			gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
			gridMp.put("rtnMsg", "切换成功");
		}
		return JsonUtil.convertObject2Json(gridMp);
	}
	
	//发送随机码
	public String sendHeaRd(InputObject inputObject) {
		String mobile = inputObject.getParams().get("mobile");
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		if (StringUtil.isEmpty(mobile) || mobile.trim().length() != 11) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "请输入正确的手机号码");
			return JsonUtil.convertObject2Json(gridMp);
		}
		int sendTimeDiff = StringUtil.str2Int(SystemPropUtil.getString("sms_send_time_diff"));
		String isCheckTime = SystemPropUtil.getString("is_check_sms_send_time");
		if (isCheckTime != null) {
			if ("y".equalsIgnoreCase(isCheckTime)|| "true".equalsIgnoreCase(isCheckTime)) {
				String lastSendTime = SpringContextHelper.instance.getOscache().get("AppRand_" + mobile);
				String timeStamp = DateUtil.date2String(new Date(),"yyyyMMddHHmmss");
				if (lastSendTime == null) {
					SpringContextHelper.instance.getOscache().put("AppRand_" + mobile, timeStamp);
				} else {
					int timeDiff = (int) getDayDiff(DateUtil.string2Date(lastSendTime, "yyyyMMddHHmmss"), new Date(), 1000);
					if (timeDiff < sendTimeDiff) {
						gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
						gridMp.put("rtnMsg", String.format("随机码发送间隔时间为%s秒",sendTimeDiff));
						return JsonUtil.convertObject2Json(gridMp);
					} else {
						SpringContextHelper.instance.getOscache().put("AppRand_" + mobile, timeStamp);
					}
				}
			}
		}
		OutputObject outObj =callCoreService.execute(inputObject);
		if(outObj==null){
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "系统忙,随机码发送失败");
			return JsonUtil.convertObject2Json(gridMp);
		}else  if(outObj.getReturnMessage()!=null && outObj.getReturnMessage().indexOf("call ")!=-1&&outObj.getReturnMessage().indexOf("error")!=-1){
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "系统忙,随机码发送失败");
			return JsonUtil.convertObject2Json(gridMp);
		}
		if (ControlConstants.RTNCODE_SUC.equals(outObj.getReturnCode())) {
			HttpSession session = getRequest().getSession(true);
			session.setAttribute("APP_LoginRd", outObj.getBusiCode());
			outObj.setBusiCode("");
		}
		// 清除随机码
		outObj.setBusiCode("");
		String json = JsonUtil.convertObject2Json(outObj);
		json = json.replaceAll("returnCode", "rtnCode");
		json = json.replaceAll("returnMessage", "rtnMsg");
		return json;
	}

	/**
	 * @Description: 用户登录接口
	 */
	public String heaLogin(InputObject inputObject) {
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		Map<String, String> inMap = inputObject.getParams();
		String mobile =inMap.get("mobile");
		String randNum =inMap.get("pwd");
		if (StringUtil.isEmpty(mobile)||StringUtil.isEmpty(randNum)) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "登录信息错误");
			return JsonUtil.convertObject2Json(gridMp);
		}
		try {
			HttpSession session = getRequest().getSession(false);//默认随机码登录
			//获取用户角色列表(检查服务密码是否正确)
			OutputObject remoteObject = callCoreService.execute(inputObject);
	        if(remoteObject==null){
				gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
				gridMp.put("rtnMsg", "系统忙,登录失败");
				return JsonUtil.convertObject2Json(gridMp);
			}else if(remoteObject.getReturnMessage()!=null && remoteObject.getReturnMessage().indexOf("call ")!=-1&&remoteObject.getReturnMessage().indexOf("error")!=-1){
				gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
				gridMp.put("rtnMsg", "系统忙,登录失败");
				return JsonUtil.convertObject2Json(gridMp);
			}
			if (ControlConstants.RTNCODE_ERR.equals(remoteObject.getReturnCode())) {
				gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
				gridMp.put("rtnMsg", remoteObject.getReturnMessage());
				return JsonUtil.convertObject2Json(gridMp);
			}
			if(session==null){
				session = getRequest().getSession(true);//自动登陆无session
			}
			
			// 放入Session信息
			HEAUser user = new HEAUser();
            Map<String,String> userInfoMap=remoteObject.getBean();
			if(!StringUtil.isEmpty(userInfoMap.get("userId"))){
				user.setUserId(StringUtil.trim(userInfoMap.get("userId")));
			}
			if(!StringUtil.isEmpty(userInfoMap.get("userName"))){
				user.setUserName(userInfoMap.get("userName"));
			}
			if(!StringUtil.isEmpty(userInfoMap.get("mobile"))){
				user.setMobile(StringUtil.str2Long(userInfoMap.get("mobile")));
			}else{
				user.setMobile(StringUtil.str2Long(mobile));
			}
			if(!StringUtil.isEmpty(userInfoMap.get("sex"))){
				user.setSex(userInfoMap.get("sex"));
			}
			session.setAttribute(HEAUser.HEA_LOGIN_USER,user);
			//有角色信息
			if("1".equals(remoteObject.getBusiCode())){
				gridMp.put("typeId", "1");
			}
			gridMp.put("rtnCode",ControlConstants.RTNCODE_SUC);
			gridMp.put("rtnMsg", "call success");
			return JsonUtil.convertObject2Json(gridMp);
		} catch (Exception e) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", e.getMessage());
		}
		return JsonUtil.convertObject2Json(gridMp);
	}

	//退出
	public String heaLogOut(InputObject inputObject) {
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		String userInfo = inputObject.getParams().get("mobile");
		if (StringUtil.isEmpty(userInfo)) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "请先登录");
			return JsonUtil.convertObject2Json(gridMp);
		}
		HttpSession session=getRequest().getSession(false);
		//销毁session
		if(session!=null){
			session.removeAttribute(HEAUser.HEA_LOGIN_USER);
			session.removeAttribute(HEAUser.HEA_CURRENT_USER);
			session.removeAttribute(HEAUser.HEA_PT_USER);
			session.invalidate();
		}
		gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
		gridMp.put("rtnMsg", "call success");
		return JsonUtil.convertObject2Json(gridMp);
	}
	
	// 计算2个时间的间隔
	public long getDayDiff(Date startDate, Date endDate, int diffValue) {
		long t1 = startDate.getTime();
		long t2 = endDate.getTime();
		long count = (t2 - t1) / diffValue;
		return Math.abs(count);
	}
	
	//取当前用户信息
	public String getHeaUserInfo(InputObject inputObject) {
		HttpSession session = getRequest().getSession(false);
		OutputObject outObj=new OutputObject();
		if(session==null){
			outObj.setReturnCode(ControlConstants.RTNCODE_ERR);
			outObj.setReturnMessage("has't login");
		}else{
			HEAUser heaUser = (HEAUser) session.getAttribute(HEAUser.HEA_CURRENT_USER);
			if(heaUser!=null){
				outObj.setBean(JsonUtil.convertBean2Map(heaUser));
				outObj.setReturnCode(ControlConstants.RTNCODE_SUC);
				outObj.setReturnMessage("success");
			}else{
				outObj.setReturnCode(ControlConstants.RTNCODE_ERR);
				outObj.setReturnMessage("has't login");
			}
		}
		String json = JsonUtil.convertObject2Json(outObj);
		json = json.replaceAll("returnCode", "rtnCode");
		json = json.replaceAll("returnMessage", "rtnMsg");
		return json;
	}
	
	//订购产品:成功加上推荐产品(暂时取大多数人正在使用)
	public String verifyHeaOrder(InputObject inputObject) {
		String checkRt=checkUserOrderInfo(inputObject);
		if(!StringUtil.isNull(checkRt)){
			return checkRt;
		}
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		OutputObject outputObject = callCoreService.execute(inputObject);
		if(ControlConstants.RTNCODE_ERR.equals(outputObject.getReturnCode())){
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", outputObject.getReturnMessage());
			return JsonUtil.convertObject2Json(gridMp);
		}
		gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
		gridMp.put("rtnMsg", outputObject.getReturnMessage());
		gridMp.put("recdApps", getAppRecmd(4));
		return JsonUtil.convertObject2Json(gridMp);
	}
	
	//退订产品 成功加上推荐产品(暂时取大多数人正在使用)
	public String unSubHeaOrder(InputObject inputObject) {
		String checkRt=checkUserOrderInfo(inputObject);
		if(!StringUtil.isNull(checkRt)){
			return checkRt;
		}
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		OutputObject outputObject = callCoreService.execute(inputObject);
		if(ControlConstants.RTNCODE_ERR.equals(outputObject.getReturnCode())){
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", outputObject.getReturnMessage());
			return JsonUtil.convertObject2Json(gridMp);
		}
		gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
		gridMp.put("rtnMsg", outputObject.getReturnMessage());
		gridMp.put("recdApps", getAppRecmd(4));
		return JsonUtil.convertObject2Json(gridMp);
	}

	//预检查订单信息
	public String checkUserOrderInfo(InputObject inputObject){
		Map<String, String> paraMap = inputObject.getParams();
		String mobileStr = paraMap.get("mobile");
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		if (StringUtil.isEmpty(mobileStr) || mobileStr.trim().length() != 11) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "参数错误,请检查是否登录");
			return JsonUtil.convertObject2Json(gridMp);
		}
		String products = paraMap.get("prodInfo");
		if (StringUtil.isEmpty(products)) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "确认订单信息出错");
			return JsonUtil.convertObject2Json(gridMp);
		}
		return null;
	}
	
	//得到app推荐
	public List<Map<String, String>> getAppRecmd(int num) {
		Map<String, Object> allHotNewApp = cmsService.getAllHotNewApp();
		List<Map<String, String>> newApps = (List<Map<String, String>>) allHotNewApp.get("hotApps");
		List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
		int i = 0;
		if (newApps != null) {
			Iterator<Map<String, String>> it = newApps.iterator();
			while (it.hasNext()) {
				Map<String, String> map = it.next();
				map.remove("appDesc");// 删除不需要的字段
				reList.add(map);
				i++;
				if (i == num) {
					break;
				}
			}
		}
		return reList;
	}
	
	//单产品接口信息
	public String querySinglePtPriceSign(InputObject inputObject){
		OutputObject outputObject =callCoreService.execute(inputObject);
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		gridMp.put("ptInfo",outputObject.getBeans());
		gridMp.put("rtnCode", outputObject.getReturnCode());
		gridMp.put("rtnMsg", outputObject.getReturnMessage());
		return JsonUtil.convertObject2Json(gridMp);
	}
	
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
	
	public HttpSession getSession(boolean isNew) {
		return getRequest().getSession(isNew);
	}
	
	public void setAppRemoteCaller(AppRemoteCallerI appRemoteCaller) {
		this.appRemoteCaller = appRemoteCaller;
	}
	
	public void setCodeUtil(CodeUtil codeUtil) {
		this.codeUtil = codeUtil;
	}
	
	/***
	 * 获取校园公告的条数
	 * @param schoolID   学校ID
	 * 
	 * */
	public String getNoticeCounts(InputObject inobj){
		String key = Constants.XXT_SECRET.AnnounNum;
		OutputObject outputObject = new OutputObject();
//		String schoolId = "416043";
		String schoolId = inobj.getParams().get("schoolID");
		String schoolIdEncrypted = codeUtil.lettlerEncrypted2(schoolId, key);
		String param = "&schoolId="+schoolIdEncrypted;
		String serialNo = inobj.getParams().get("serialNo");
		String reqStr = codeUtil.getParamData2(serialNo,schoolId,key,param);
		RemoteRespHelper helper = new RemoteRespHelper(String.valueOf(appRemoteCaller.getAnnouncementNum(reqStr,schoolId)));
		if(!helper.getRtnCode().equals("0")){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage(helper.getRtnMsg());
			return convertOutputObject2RtsEr(outputObject);
		}
		String announcementNum = String.valueOf(helper. getAnnouncementNum());
		HashMap<String, Object> announcementMap = new HashMap<String,Object>();
		List<Map<String,Object>> announcement = new ArrayList<Map<String,Object>>();
		announcementMap.put("announcementNum", announcementNum);
		announcement.add(announcementMap);
		return convert(announcement,inobj.getParams().get(ControlConstants.UID));
		
	}
	
	/***
	 * 获取校园公告列表
	 * @param schoolID   学校ID
	 * 
	 * */
	public String getNoticeCountsList(InputObject inobj){
		String key = Constants.XXT_SECRET.AnnounList;
		OutputObject outputObject = new OutputObject();
//		String schoolId = "416043";
//		String start = "1";
//		String limit = "7";
		String schoolId = inobj.getParams().get("schoolID");
		String start = inobj.getParams().get("start");
		String limit = inobj.getParams().get("limit");
		//拼接加密之后的参数串
		String schoolIdEncrypted = codeUtil.lettlerEncrypted2(schoolId, key);
		String startEncrypted = codeUtil.lettlerEncrypted2(start, key);
		String limitEncrypted = codeUtil.lettlerEncrypted2(limit, key);
		String param = "&schoolId="+schoolIdEncrypted+"&start="+startEncrypted+"&limit="+limitEncrypted;

		String serialNo = inobj.getParams().get("serialNo");
		CodeUtil codeUtil = new CodeUtil();
		String reqStr = codeUtil.getParamData2(serialNo,schoolId,key,param);
		RemoteRespHelper helper = new RemoteRespHelper
				(String.valueOf(appRemoteCaller.getAnnouncement(reqStr,schoolId)));
		if(!helper.getRtnCode().equals(ControlConstants.RTNCODE_ERR)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage(helper.getRtnMsg());
			return convertOutputObject2RtsEr(outputObject);
		}
		List<Map<String,Object>> announcement = helper. getAnnouncement();
		return convert(announcement,inobj.getParams().get(ControlConstants.UID));
	}
	/***
	 * 获取 班级朋友列表
	 * @param schoolID   学校ID  userId userType
	 * 
	 * */
	public String getMsglist(InputObject inobj){
		String key = Constants.XXT_SECRET.Msglist;
		OutputObject outputObject = new OutputObject();
//		String schoolId = "16540000113619";//65789079014073
//		String userId = "3914586";//5864263
//		String userType = "2";//0
//		String executeDate = "2015-04-19 10:12:08";
		String userId =  inobj.getParams().get("userId");
		String userType =  inobj.getParams().get("userType");
		String schoolId = inobj.getParams().get("schoolId");
		String executeDate = inobj.getParams().get("executeDate");
		if(StringUtil.isEmpty(userId)||StringUtil.isEmpty(userType)||StringUtil.isEmpty(schoolId)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage("请确认登陆有效账户！");
			return convertOutputObject2RtsEr(outputObject);
		}
        //userType转换
		if(userType.equals(String.valueOf(Constants.USER_TYPE.TEACHER))){
        	userType=Constants.XXT_USER_TYPE.TEACHER;
        }else if(userType.equals(String.valueOf(Constants.USER_TYPE.STUDENT))){
        	HttpSession session = getRequest().getSession(false);
			if (session == null) {
				outputObject.setReturnCode("");
			}
			HEAUser heaUser = (HEAUser) session.getAttribute(HEAUser.HEA_PT_USER);
			if (heaUser == null) {
				outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
				outputObject.setReturnMessage("家长信息不存在！");
				return convertOutputObject2RtsEr(outputObject);
			}
			userId=heaUser.getUserId();
        	userType=Constants.XXT_USER_TYPE.PARENT;
        }
		//拼接加密之后的参数串
		String schoolIdEncrypted = codeUtil.lettlerEncrypted2(schoolId, key);
		String userIdEncrypted = codeUtil.lettlerEncrypted2(userId, key);
		String userTypeEncrypted = codeUtil.lettlerEncrypted2(userType, key);
		String executeDateEncrypted = codeUtil.lettlerEncrypted2(executeDate, key);
		String param = "&userId="+userIdEncrypted+"&userType="+userTypeEncrypted+"&schoolId="+schoolIdEncrypted+"&executeDate="+executeDateEncrypted;
		String serialNo = inobj.getParams().get("serialNo");
		CodeUtil codeUtil = new CodeUtil();
		String reqStr = codeUtil.getParamData2(serialNo,userId,key,param);
		RemoteRespHelper helper = new RemoteRespHelper(String.valueOf(appRemoteCaller.getFriendAnnouncement(reqStr,userId)));
		if(!helper.getRtnCode().equals("0")){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage(helper.getRtnMsg());
			return convertOutputObject2RtsEr(outputObject);
		}
		List<Map<String,Object>> msgList = helper. getAnnouncement();
		return convert(msgList,inobj.getParams().get(ControlConstants.UID));
     }
	
	/***
	 * 学生成绩表查询
	 * @param userId   角色Id
	 * 
	 * */
	public String getStudentScoreList(InputObject inobj){
		String key = Constants.XXT_SECRET.StuScoreList;
		OutputObject outputObject = new OutputObject();
//		String userId = "76551685";
//		String start = "1";
//		String limit = "7";
		String userId = inobj.getParams().get("userId");
		String start = inobj.getParams().get("start");
		String limit = inobj.getParams().get("limit");
		if(StringUtil.isEmpty(userId)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage("请确认登陆信息有效！");
			return convertOutputObject2RtsEr(outputObject);
		}
		//拼接加密之后的参数串
		String userIdEncrypted = codeUtil.lettlerEncrypted2(userId, key);
		String startEncrypted = codeUtil.lettlerEncrypted2(start, key);
		String limitEncrypted = codeUtil.lettlerEncrypted2(limit, key);
		String param = "&userId="+userIdEncrypted+"&start="+startEncrypted+"&limit="+limitEncrypted;
		String serialNo = inobj.getParams().get("serialNo");
		String reqStr = codeUtil.getParamData2(serialNo,userId,key,param);
		RemoteRespHelper helper = new RemoteRespHelper(String.valueOf(appRemoteCaller.getStScoreLs(reqStr, userId)));
		if(!helper.getRtnCode().equals(ControlConstants.RTNCODE_ERR)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage(helper.getRtnMsg());
			return convertOutputObject2RtsEr(outputObject);
		}
		List<Map<String,Object>> courseScore = helper.getCourseScore();
		return convert(courseScore,inobj.getParams().get(ControlConstants.UID));
	}
	
	/***
	 * 学生某次考试所有科目成绩
	 * @param userId   角色Id   
	 * 		  examId   考试Id
	 * 
	 * */
	public String getStuScoreOfOneExam(InputObject inobj){
		String key = Constants.XXT_SECRET.StuAllSubjectScoreOneExam;
		OutputObject outputObject = new OutputObject();
//		String userId = "76551685";
//		String examId = "2122446";
		String userId = inobj.getParams().get("userId");
		String examId = inobj.getParams().get("examId");
		if(StringUtil.isEmpty(userId)||StringUtil.isEmpty(examId)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage("请确认登陆有效账号！");
			return convertOutputObject2RtsEr(outputObject);
		}
		//拼接加密之后的参数串
		String userIdEncrypted = codeUtil.lettlerEncrypted2(userId, key);
		String examIdEncrypted = codeUtil.lettlerEncrypted2(examId, key);
		String param = "&userId="+userIdEncrypted+"&examId="+examIdEncrypted;
		String serialNo = inobj.getParams().get("serialNo");
		String reqStr = codeUtil.getParamData2(serialNo,userId,key,param);
		RemoteRespHelper helper = new RemoteRespHelper(String.valueOf(appRemoteCaller.getStCourseScoreLs(reqStr, userId)));
		if(!helper.getRtnCode().equals(ControlConstants.RTNCODE_ERR)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage(helper.getRtnMsg());
			return convertOutputObject2RtsEr(outputObject);
		}
		List<Map<String,Object>> oneCourseScore = helper.getCourseScore();
		return convert(oneCourseScore,inobj.getParams().get(ControlConstants.UID));
	}
	
	/***
	 * 学生成绩列表,学生家长，某一科成绩的历史信息
	 * @param userId   xxt侧对应的用户ID
	 * @param type     用户类型
	 *                 1:代表老师，2:代表家长/学生
	 * 
	 * */
	public String getStuOneCourseHistory(InputObject inobj){
		String key = Constants.XXT_SECRET.StuOneHisScore;
		OutputObject outputObject = new OutputObject();
//		String userId = "76551685";
//		String courseId = "3";
//		String start = "1";
//		String limit = "7";
		String userId = inobj.getParams().get("userId");
		String courseId = inobj.getParams().get("courseId");
		String start = inobj.getParams().get("start");
		String limit = inobj.getParams().get("limit");
		if(StringUtil.isEmpty(userId)||StringUtil.isEmpty(courseId)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage("请确认登陆有效账号！");
			return convertOutputObject2RtsEr(outputObject);
		}
		//拼接加密之后的参数串
		String userIdEncrypted = codeUtil.lettlerEncrypted2(userId, key);
		String courseIdEncrypted = codeUtil.lettlerEncrypted2(courseId, key);
		String startEncrypted = codeUtil.lettlerEncrypted2(start, key);
		String limitEncrypted = codeUtil.lettlerEncrypted2(limit, key);
		String param = "&userId="+userIdEncrypted+"&courseId="+courseIdEncrypted+
				"&start="+startEncrypted+"&limit="+limitEncrypted;
		String serialNo = inobj.getParams().get("serialNo");
		String reqStr = codeUtil.getParamData2(serialNo,userId,key,param);
		RemoteRespHelper helper = new RemoteRespHelper(String.valueOf(appRemoteCaller.getStCourseHisScoreLs(reqStr, userId)));
		if(!helper.getRtnCode().equals(ControlConstants.RTNCODE_ERR)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage(helper.getRtnMsg());
			return convertOutputObject2RtsEr(outputObject);
		}
		List<Map<String,Object>> oneCourseScore = helper.getCourseScore();
		return convert(oneCourseScore,inobj.getParams().get(ControlConstants.UID));
	}
	
	
	/***
	 * 老师进入成绩页面，搜索出来 不同学期 下 不同班级不同考试信息
	 * @param userId   xxt侧对应的用户ID
	 * @param type     用户类型
	 *                 1:代表老师，2:代表家长/学生
	 * 
	 * */
	public String getTeaExamList(InputObject inobj){
		String key = Constants.XXT_SECRET.TeaExamteaquery;
		OutputObject outputObject = new OutputObject();
//		String userId = "3914586";
//		String schoolId = "16540000113619";
//		String startPg = "1";
//		String limit = "20";
		String userId = inobj.getParams().get("userId");
		String schoolId = inobj.getParams().get("schoolId");
		String startPg = inobj.getParams().get("startPg");
		String limit = inobj.getParams().get("limit");
		//拼接加密之后的参数串
		String userIdEncrypted = codeUtil.lettlerEncrypted2(userId, key);
		String schoolIdEncrypted = codeUtil.lettlerEncrypted2(schoolId, key);
		String startPgEncrypted = codeUtil.lettlerEncrypted2(startPg, key);
		String limitEncrypted = codeUtil.lettlerEncrypted2(limit, key);
		String param = "&userId="+userIdEncrypted+"&schoolId="+schoolIdEncrypted+
				"&startPg="+startPgEncrypted+"&limit="+limitEncrypted;
		String serialNo = inobj.getParams().get("serialNo");
		String reqStr = codeUtil.getParamData2(serialNo,userId,key,param);
		RemoteRespHelper helper = new RemoteRespHelper(String.valueOf(appRemoteCaller.getTeScoreLs(reqStr, userId)));
		if(!helper.getRtnCode().equals(ControlConstants.RTNCODE_ERR)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage(helper.getRtnMsg());
			return convertOutputObject2RtsEr(outputObject);
		}
		List<Map<String,Object>> teaExamList = helper.getTeaExamList();
		return convert(teaExamList,inobj.getParams().get(ControlConstants.UID));
	}
	

	/***
	 * 老师班级某次考试单科目成绩列表
	 * @param userId   xxt侧对应的用户ID
	 * @param type     用户类型
	 *                 1:代表老师，2:代表家长/学生
	 * 
	 * */
	public String getTeaOneExamInfo(InputObject inobj){
		String key = Constants.XXT_SECRET.OneExamList;
		OutputObject outputObject = new OutputObject();
//		String courseId = "945294";//945294
//		String examId = "2170122";
//		String gradeId = "3062231";
		String courseId = inobj.getParams().get("courseId");
		String examId = inobj.getParams().get("examId");
		String gradeId = inobj.getParams().get("gradeId");
		//拼接加密之后的参数串
		String courseIdEncrypted = codeUtil.lettlerEncrypted2(courseId, key);
		String examIdEncrypted = codeUtil.lettlerEncrypted2(examId, key);
		String gradeIdEncrypted = codeUtil.lettlerEncrypted2(gradeId, key);
		String param = "&courseId="+courseIdEncrypted+"&examId="+examIdEncrypted+
				"&gradeId="+gradeIdEncrypted;
		String serialNo = inobj.getParams().get("serialNo");
		String reqStr = codeUtil.getParamData2(serialNo,examId,key,param);
		RemoteRespHelper helper = new RemoteRespHelper(String.valueOf(appRemoteCaller.getGradeScore(reqStr, courseId)));
		if(!helper.getRtnCode().equals(ControlConstants.RTNCODE_ERR)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage(helper.getRtnMsg());
			return convertOutputObject2RtsEr(outputObject);
		}
		List<Map<String,Object>> oneCourseScore = helper.getCourseScore();
		return convert(oneCourseScore,inobj.getParams().get(ControlConstants.UID));
	}
	
   /***
	 * 作业接口
	 * 
	 * */
	public String getHomeworkList(InputObject inobj){
		String key = Constants.XXT_SECRET.Homework;
		OutputObject outputObject = new OutputObject();
//		String userId = "517327";//"517327";
//		String userType = "1";
//		String schoolId = "9381";//"9381";
//		String start = "0";
//		String limit = "5";
//		String status = "2";
		String userId = inobj.getParams().get("userId");
		String getWorkUserId = userId;
		String userType = inobj.getParams().get("userType");
		String schoolId = inobj.getParams().get("schoolId");
		String start = inobj.getParams().get("start");
		String limit = inobj.getParams().get("limit");
		String status = inobj.getParams().get("status");
		 //userType转换
		if(userType.equals(String.valueOf(Constants.USER_TYPE.TEACHER))){
        	userType=Constants.XXT_USER_TYPE.TEACHER;
        }else if(userType.equals(String.valueOf(Constants.USER_TYPE.STUDENT))){
        	HttpSession session = getRequest().getSession(false);
			if (session == null) {
				outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
				outputObject.setReturnMessage("请先登录!");
				return convertOutputObject2RtsEr(outputObject);
			}
			HEAUser heaUser = (HEAUser) session.getAttribute(HEAUser.HEA_PT_USER);
			if (heaUser == null) {
				//可能是自动登录进入
				InputObject inobj2=new InputObject();
				inobj2.getParams().put("loginUserId",inobj.getParams().get("loginUserId"));
				inobj.setService("userLastRoleService");
				inobj.setMethod("getUserParentInfo");
				inobj.setServerIp(StringUtil.getLocalHostIp());
				OutputObject outobj = callCoreService.execute(inobj);
				if(outobj!=null&&!StringUtil.isEmpty(outobj.getBusiCode())){
					// 保存当前学生的家长id
					Map<String, String> ptUserMap = JsonUtil.convertJson2Object(outobj.getBusiCode(), Map.class);
					HEAUser ptUser = new HEAUser();
					ClassUtil.setPropValFromMap(ptUserMap, ptUser);
					session.setAttribute(HEAUser.HEA_PT_USER, ptUser);
					userId=ptUser.getUserId();
				}else{
					outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
					outputObject.setReturnMessage("获取家长信息失败!");
					return convertOutputObject2RtsEr(outputObject);
				}
			}else{
				userId=heaUser.getUserId();
			}
        	userType=Constants.XXT_USER_TYPE.PARENT;
        }
		//拼接加密之后的参数串
		String userIdEncrypted = codeUtil.lettlerEncrypted2(userId, key);
		String userTypeEncrypted = codeUtil.lettlerEncrypted2(userType, key);
		String schoolIdEncrypted = codeUtil.lettlerEncrypted2(schoolId, key);
		String startEncrypted = codeUtil.lettlerEncrypted2(start, key);
		String limitEncrypted = codeUtil.lettlerEncrypted2(limit, key);
		String param = "&userId="+userIdEncrypted+"&userType="+userTypeEncrypted+
				"&schoolId="+schoolIdEncrypted+"&start="+startEncrypted+"&limit="+limitEncrypted;
		String serialNo = inobj.getParams().get("serialNo");
		String reqStr = codeUtil.getParamData2(serialNo,userId,key,param);
		RemoteRespHelper helper = new RemoteRespHelper(String.valueOf(appRemoteCaller.getWorkContent(reqStr,userId)));
		if(!helper.getRtnCode().equals(ControlConstants.RTNCODE_ERR)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage(helper.getRtnMsg());
			return convertOutputObject2RtsEr(outputObject);
		}
		List<Map<String, Object>> workContent = helper.getWorkContent();
		if (Constants.XXT_USER_TYPE.PARENT.equals(userType)) {// 家长
			String[] tmp   = getWorkIds(workContent);
			String workIds = tmp[0];
			String userIds=StringUtil.arrayJoin(new String[]{userId}, ",");
			inobj.getParams().put("workIds", workIds);
			inobj.getParams().put("userId", getWorkUserId);
			inobj.setService("workmarkService");
			inobj.setMethod("getUserWorkmarks");
			inobj.setServerIp(StringUtil.getLocalHostIp());
					
			OutputObject outobj = callCoreService.execute(inobj);
			List<Map<String,String>> userFinishedWorks = outobj.getBeans();
			Map<String,Map<String,String>> finishWorks = list2Map(userFinishedWorks);
			setWorkstate(workContent,getWorkUserId,finishWorks);
            if (!StringUtil.isEmpty(status)) {
				List<Map<String, Object>> workRtnContent = new ArrayList<Map<String, Object>>();
				if (workContent.size() > 0) {
					for (Map<String, Object> work : workContent) {
						String workstate=StringUtil.obj2Str(work.get("workstate"));
						if (status.equals("1")&&workstate.equals("1")) {
							 workRtnContent.add(work);
						} else if (status.equals("0")&&workstate.equals("0")) {
							 workRtnContent.add(work);
						}
					}
					if (status.equals("0")||status.equals("1")){
						workContent.clear();
						workContent.addAll(workRtnContent);
					}
				}
			}
		}else if(Constants.XXT_USER_TYPE.TEACHER.equals(userType)){//老师
			String[] allIds = getTeaWorkIds(workContent);
			String workIds = allIds[0];
			String userIds=StringUtil.arrayJoin(new String[]{userId}, ",");
			inobj.getParams().put("userId", userIds);
			inobj.getParams().put("workIds", workIds);
			inobj.getParams().put("gradeId", allIds[1]);
			inobj.setService("workmarkService");
			inobj.setMethod("getTeaWorkContent");
			inobj.setServerIp(StringUtil.getLocalHostIp());
			
			OutputObject outobj = callCoreService.execute(inobj);
			if(outobj.getReturnCode().equals(ControlConstants.RTNCODE_SUC)){
				List<Map<String,String>> finishRateList = outobj.getBeans();
				Map<String,Map<String,String>> finishWorks = gradelist2Map(finishRateList);
				setTeaGradeWorkstate(workContent,finishWorks);
			}else{
				for (Map<String,Object> work : workContent) {
					work.put("finishedNum",  "0%");
					work.put("unFinishedNum", "100%");
				}
			}
			if (!StringUtil.isEmpty(status)) {
				List<Map<String, Object>> workRtnContent = new ArrayList<Map<String, Object>>();
				if (workContent.size() > 0) {
					for (Map<String, Object> work : workContent) {
						String finishStr = StringUtil.obj2Str(work.get("finishedNum"));
						finishStr = finishStr.replaceAll("%", "");
						double finish = StringUtil.str2Double(finishStr);
						if (status.equals("1")) {
							if (finish >= 100) {
								workRtnContent.add(work);
							}
						} else if (status.equals("0")) {
							if (finish < 100) {
								workRtnContent.add(work);
							}
						}
					}
					if (status.equals("0")||status.equals("1")){
						workContent.clear();
						workContent.addAll(workRtnContent);
					}
				}
			}
		}
		//设置courseId
        setCourseId(workContent);
		return convert(workContent,inobj.getParams().get(ControlConstants.UID));
	}

	
    /***
	 * 发送信息
	 * @throws UnsupportedEncodingException 
	 * 
	 * */
	public String sendClassMsg(InputObject inobj) throws UnsupportedEncodingException{
		String key = Constants.XXT_SECRET.MsgSend;
		//家长可以 只能发给学生的班主任 或者回复给他发信息的教师
		//xxt侧usertype的定义 0教师 2家长 crm侧usertype1：老师 2：学生 3：家长
		OutputObject outputObject = new OutputObject();
		String userId = inobj.getParams().get("userId");
		String userType = inobj.getParams().get("userType");
		String schoolId = inobj.getParams().get("schoolId");
//		String userId = "5864263";
//		String userType = "0";
//		String schoolId = "65789079014073";
		
		String msgContent =inobj.getParams().get("msgContent");
		String msgType = inobj.getParams().get("msgType");
		String destUserType = inobj.getParams().get("destUserType");
		String destId = inobj.getParams().get("destId");
		String destUnitId = inobj.getParams().get("destUnitId");
		String ggName = inobj.getParams().get("ggName");
		String name = Constants.SEND_MSG.STU_NAME;
		String tempId=inobj.getParams().get("tempId");
		
		if(StringUtil.isEmpty(userId)||StringUtil.isEmpty(userType)||StringUtil.isEmpty(schoolId)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage("获取用户信息参数失败！");
			return convertOutputObject2RtsEr(outputObject);
		}
		//destUserType转换	 0教师，2家长
		if(null != destUserType && destUserType.equals(String.valueOf(Constants.CRM_USER_TYPE.DEST_USER_TEA))){
			destUserType=Constants.XXT_USER_TYPE.TEACHER;
        }else if(null != destUserType && destUserType.equals(String.valueOf(Constants.CRM_USER_TYPE.DEST_USER_STU))){
        	destUserType=Constants.XXT_USER_TYPE.PARENT;
        }
		//userType转换
		if(userType.equals(String.valueOf(Constants.USER_TYPE.TEACHER))){
        	userType=Constants.XXT_USER_TYPE.TEACHER;
        	name = Constants.SEND_MSG.TEA_NAME;
        }else if(userType.equals(String.valueOf(Constants.USER_TYPE.STUDENT))){
        	HttpSession session = getRequest().getSession(false);
			HEAUser heaUser = (HEAUser) session.getAttribute(HEAUser.HEA_PT_USER);
			if (heaUser == null) {
				outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
				outputObject.setReturnMessage("请行进行登陆或获取用户信息参数失败！");
				return convertOutputObject2RtsEr(outputObject);
			}
			userId=heaUser.getUserId();
        	userType=Constants.XXT_USER_TYPE.PARENT;
        }
		if(!StringUtil.isEmpty(msgContent)){
			msgContent= URLEncoder.encode(msgContent,"UTF-8");
		}else{
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage("内容不能为空！");
			return convertOutputObject2RtsEr(outputObject);
		}
		// 公告gg  留言cc
		ggName= URLEncoder.encode(ggName,"UTF-8");
		name= URLEncoder.encode(name,"UTF-8");
		//拼接加密之后的参数串
		String userIdEncrypted = codeUtil.lettlerEncrypted2(userId, key);
		String userTypeEncrypted = codeUtil.lettlerEncrypted2(userType, key);
		String schoolIdEncrypted = codeUtil.lettlerEncrypted2(schoolId, key);
		String msgContentEncrypted = codeUtil.lettlerEncrypted2(msgContent, key);
		String msgTypeEncrypted = codeUtil.lettlerEncrypted2(msgType, key);
		String destUserTypeEncrypted = codeUtil.lettlerEncrypted2(destUserType, key);
		String destIdEncrypted = codeUtil.lettlerEncrypted2(destId, key);
		String destUnitIdEncrypted = codeUtil.lettlerEncrypted2(destUnitId, key);
		String ggNameEncrypted = codeUtil.lettlerEncrypted2(ggName, key);
		String nameEncrypted= codeUtil.lettlerEncrypted2(name, key);
		String param = "&userId="+userIdEncrypted+"&userType="+userTypeEncrypted+
				"&schoolId="+schoolIdEncrypted+"&msgContent="+msgContentEncrypted+"&msgType="+msgTypeEncrypted+"&destUserType="+destUserTypeEncrypted+
				"&destId="+destIdEncrypted+"&destUnitId="+destUnitIdEncrypted+"&ggName="+ggNameEncrypted+
				"&name="+nameEncrypted;
		String serialNo = inobj.getParams().get("serialNo");
		String reqStr = codeUtil.getParamData2(serialNo,userId,key,param);
		RemoteRespHelper helper = new RemoteRespHelper(String.valueOf(appRemoteCaller.sendAnnouncement(reqStr,userId)));
		if(!helper.getRtnCode().equals(ControlConstants.RTNCODE_ERR)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage(helper.getRtnMsg());
			return convertOutputObject2RtsEr(outputObject);
		}
		List<Map<String,Object>> workContent = helper.getWorkContent();
		String rtnJson=convert(workContent,inobj.getParams().get(ControlConstants.UID));
		Map<String,Object> rtnMap=JsonUtil.convertJson2Object(rtnJson, Map.class);
		rtnMap.put("tempId", tempId);
		return JsonUtil.convertObject2Json(rtnMap);
	}

	
	//个人信息:获取当前用户信息以及用户所有的角色信息
	public String getCurrentUserAllInfo(InputObject inputObject) {
		String mobile = inputObject.getParams().get("mobile");
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		if (StringUtil.isEmpty(mobile)) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "参数错误");
			return JsonUtil.convertObject2Json(gridMp);
		}
		// 获取用户信息
		HttpSession session = getSession(true);
		HEAUser heaUser = new HEAUser();
		// session有数据
		if (session != null) {
			heaUser = (HEAUser) session.getAttribute(HEAUser.HEA_CURRENT_USER);
		} else {
			heaUser.setMobile(Long.parseLong(mobile));
		}
		OutputObject outputObject = callCoreService.execute(inputObject);
		// 学生信息
		List<Map<String, String>> studenList = new ArrayList<Map<String, String>>();
		// 老师信息
		List<Map<String, String>> teaList = new ArrayList<Map<String, String>>();
		if (ControlConstants.RTNCODE_SUC.equals(outputObject.getReturnCode())) {
			for (Map<String, String> map : outputObject.getBeans()) {
				Map<String, String> rtnMap = new HashMap<String, String>();
				rtnMap.put("userId", map.get("userId"));
				rtnMap.put("userName", map.get("userNm"));
				rtnMap.put("city", map.get("area"));
				rtnMap.put("region", map.get("subArea"));
				rtnMap.put("schoolName", map.get("schoolNm"));
				rtnMap.put("schoolId", map.get("schoolId"));
				rtnMap.put("gradeName", map.get("gradeNm"));
				rtnMap.put("gradeId", map.get("gradeId"));
				rtnMap.put("sex", map.get("sex"));// 1男 2女
				rtnMap.put("xxtUid", map.get("xxtuid"));
				// 接口1代表老师 1老师 2学生 3家长
				if ("1".equals(map.get("userTp"))) {
					rtnMap.put("typeId", "2");
					rtnMap.put("imgUrl", "../theme/images/headIcon.png");
				} else {
					rtnMap.put("typeId", "1");
					// TODO 换掉路径
					rtnMap.put("imgUrl", "../theme/images/headIcon.png");
				}
				if ("1".equals(map.get("userTp"))) {
					teaList.add(rtnMap);
				} else {
					studenList.add(rtnMap);
				}
			}
		}
		gridMp.put("userInfo", JsonUtil.convertBean2Map(heaUser));
		gridMp.put("parents", studenList);
		gridMp.put("teacher", teaList);
		gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
		gridMp.put("rtnMsg", "success");
		return JsonUtil.convertObject2Json(gridMp);
	}
		
	// 获取用户所有订单
	public String getUserAllOrder(InputObject inputObject) {
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		String mobile=inputObject.getParams().get("mobile");
		if(StringUtil.isEmpty(mobile)){
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "当前用户无订单信息");
			return JsonUtil.convertObject2Json(gridMp);
		}
		OutputObject outputObject = callCoreService.execute(inputObject);
		List<Map<String, String>> rtnBeans = outputObject.getBeans();
		// 缓存取组合包信息
		Object obj = SpringContextHelper.instance.getOscache().get(CmsServiceClient.CMS_APPS_CACHE);
		List<Map<String, String>> allObj = null;
		if (obj != null) {
			allObj = (List<Map<String, String>>) obj;
		}
		if (rtnBeans == null|| allObj == null|| ControlConstants.RTNCODE_ERR.equals(outputObject.getReturnCode())) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "系统异常");
			return JsonUtil.convertObject2Json(gridMp);
		}
		List<Map<String, String>> singleOrderList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> multiOrderList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> appOrderList = new ArrayList<Map<String, String>>();
		for (Map<String, String> map : rtnBeans) {
			if (map.get("appSingleOrder") != null) {
				continue;
			}
			for (Map<String, String> map2 : allObj) {
				// 判断是否已失效Start
				String expireDateString = map.get("expireDate").toString();
				Date expireDate = DateUtil.string2Date(expireDateString,"yyyy-MM-dd HH:mm:ss");
				Date currentTime = new Date();
				Calendar c1 = Calendar.getInstance();
				c1.setTime(expireDate);
				Calendar c2 = Calendar.getInstance();
				c2.setTime(currentTime);
				if (c1.before(c2)) {
					map.put("isExpire", "1");// 已失效
				} else {
					map.put("isExpire", "2");// 没有失效
				}
				// 判断是否已失效End
				// 单产品
				if ("2".equals(map2.get("isMutiApp"))) {
					if (map.get("appId").equals(map2.get("appID"))) {
						map.put("appName", String.valueOf(map2.get("appName")));
						map.put("nodeUid", String.valueOf(map2.get("nodeUid")));
						map.put("appPic", String.valueOf(map2.get("appPic")));
						map.put("appPrice",String.valueOf(map2.get("packagePrice")));
						map.put("apphref", String.valueOf(map2.get("apphref")));
						map.put("isExpire", map.get("isExpire"));
						map.put("isMutiApp", "2");
						singleOrderList.add(map);
						continue;
					}
				} else {
					// 组合产品
					int prodMonth = getProdMonth(map.get("appId"), map2);
					if (prodMonth > 0) {
						map.put("appName", map2.get("appName"));
						map.put("nodeUid", map2.get("nodeUid"));
						map.put("appPic", map2.get("appPic"));
						map.put("month", String.valueOf(prodMonth));
						map.put("appPrice", getProdPrice(prodMonth, map2));
						map.put("isExpire", map.get("isExpire"));
						map.put("isMutiApp", "1");
						map.put("appId", map2.get("appID"));
						multiOrderList.add(map);
					}
				}
			}
		}
		// app单产品
		for (Map<String, String> map : rtnBeans) {
			if (map.get("appSingleOrder") == null) {
				continue;
			}
			for (Map<String, String> map2 : allObj) {
				if (map.get("offerCode").equals(map2.get("threeOfferCode"))) {
					map.put("nodeUid", String.valueOf(map2.get("nodeUid")));
					map.put("appPic", String.valueOf(map2.get("appPic")));
					map.put("appPrice",String.valueOf(map2.get("packagePrice")));
					map.put("apphref", String.valueOf(map2.get("apphref")));
					map.put("appId", map2.get("appID"));
					appOrderList.add(map);
				}
			}
		}
		gridMp.put("singleOrder", singleOrderList);
		gridMp.put("multiOrder", multiOrderList);
		gridMp.put("appSigOrder", appOrderList);
		gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
		gridMp.put("rtnMsg", "call success");
		return JsonUtil.convertObject2Json(gridMp);
	}
		
	public String getProdPrice(int month, Map<String, String> map) {
		if (month == 3) {
			String price = map.get("threepromprice");
			if (StringUtil.isEmpty(price)) {
				return StringUtil.trim(map.get("packagePrice"));
			}
			return StringUtil.trim(price);
		} else if (month == 6) {
			String price = map.get("sixpromprice");
			return StringUtil.trim(price);
		} else if (month == 12) {
			String price = map.get("twelvepromprice");
			return StringUtil.trim(price);
		}
		return "";
	}

	public int getProdMonth(String offerCode, Map<String, String> map) {
		String threeOfferCode = StringUtil.trim(map.get("threeOfferCode"));
		String sixOfferCode = StringUtil.trim(map.get("sixOfferCode"));
		String twelveOfferCode = StringUtil.trim(map.get("twelveOfferCode"));
		offerCode = StringUtil.trim(offerCode);
		if (StringUtil.isEmpty(offerCode))
			return 0;
		if (threeOfferCode.equals(offerCode)) {
			return 3;
		} else if (sixOfferCode.equals(offerCode)) {
			return 6;
		} else if (twelveOfferCode.equals(offerCode)) {
			return 12;
		} else {
			return 0;
		}
	}
	
	/***
	 * 学生成绩列表,学生家长，某一科成绩的历史信息
	 * @param userId   xxt侧对应的用户ID
	 * @param type     用户类型
	 *                 1:代表老师，2:代表家长/学生
	 * 
	 * */
	public String getStuOneCourseHistoryPolt(InputObject inobj) {
		String key = Constants.XXT_SECRET.StuOneHisScore;
//		 String userId = "76551685";
//		 String courseId = "3";
//		 String start = "1";
//		 String limit = "7";
		String userId = inobj.getParams().get("userId");
		String courseId = inobj.getParams().get("courseId");
		String start = inobj.getParams().get("start");
		String limit = inobj.getParams().get("limit");
		// 拼接加密之后的参数串
		String userIdEncrypted = codeUtil.lettlerEncrypted2(userId, key);
		String courseIdEncrypted = codeUtil.lettlerEncrypted2(courseId, key);
		String startEncrypted = codeUtil.lettlerEncrypted2(start, key);
		String limitEncrypted = codeUtil.lettlerEncrypted2(limit, key);
		String param = "&userId=" + userIdEncrypted + "&courseId="
				+ courseIdEncrypted + "&start=" + startEncrypted + "&limit="
				+ limitEncrypted;
		String serialNo = inobj.getParams().get("serialNo");
		String reqStr = codeUtil.getParamData2(serialNo, userId, key, param);
		RemoteRespHelper helper = new RemoteRespHelper(String.valueOf(appRemoteCaller.getStCourseHisScoreLs(reqStr,userId)));
		if (!helper.getRtnCode().equals(ControlConstants.RTNCODE_ERR)) {
			OutputObject outputObject = new OutputObject();
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage(helper.getRtnMsg());
			return convertOutputObject2RtsEr(outputObject);
		}
		List<Map<String, Object>> historyScorePolt = helper.getCourseScore();
		String avgScore = "";
		if (null != historyScorePolt.get(0).get("AVGSCORE")) {
			avgScore = historyScorePolt.get(0).get("AVGSCORE").toString();
		}
		// SCORE AVGSCORE
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("100-91", 0);
		map.put("90-81", 0);
		map.put("80-71", 0);
		map.put("70-61", 0);
		map.put("60-0", 0);
		int upAvg = 0;
		int downAvg = 0;
		Iterator<String> keyit = map.keySet().iterator();
		while (keyit.hasNext()) {
			String mapKey = keyit.next();
			for (Map<String, Object> m : historyScorePolt) {
				if(null != m.get("SCORE")){
					String score = m.get("SCORE").toString();
					String[] tmpscore = mapKey.split("-");
					if (mathMaxMin(score, tmpscore[0], tmpscore[1])) {
						Integer val = (Integer) map.get(mapKey);
						map.put(mapKey, Integer.valueOf(val + 1));
					}
					if (avgScore.length() != 0) {
						if (Integer.parseInt(score) >= Integer.parseInt(avgScore)) {
							upAvg++;
						} else {
							downAvg++;
						}
					}
				}
			}
		}

		Map<String, String> colorMap = new HashMap<String, String>();
		colorMap.put("100-91", "#b1c262");
		colorMap.put("90-81", "#8dddfa");
		colorMap.put("80-71", "#7cbddd");
		colorMap.put("70-61", "#ffaa76");
		colorMap.put("60-0", "#e46959");
		colorMap.put("upAvg", "#4572a7");
		colorMap.put("downAvg", "#de6464");
		for (Entry<String, Object> entry : map.entrySet()) {
			Map<String, Object> rtnMap = new HashMap<String, Object>();
			rtnMap.put("name", entry.getKey());
			rtnMap.put("value", entry.getValue());
			rtnMap.put("color", colorMap.get(entry.getKey()));
			list.add(rtnMap);
		}

		Map<String, Object> rtnMap = new HashMap<String, Object>();

		List<Map<String, Object>> avgList = new ArrayList<Map<String, Object>>();
		Map<String, Object> avgMap = new HashMap<String, Object>();
		avgMap.put("name", "平均分以上");
		avgMap.put("value", upAvg);
		avgMap.put("color", colorMap.get("upAvg"));
		avgList.add(avgMap);

		avgMap = new HashMap<String, Object>();
		avgMap.put("name", "平均分以下");
		avgMap.put("value", downAvg);
		avgMap.put("color", colorMap.get("downAvg"));
		avgList.add(avgMap);

		rtnMap.put("beans", list);
		rtnMap.put("rows", avgList);

		List<Map<String, Object>> convertList = new ArrayList<Map<String, Object>>();
		convertList.add(rtnMap);
		return convert(convertList, inobj.getParams().get(ControlConstants.UID));
	}

	
	/***
	 * 老师班级某次考试单科目成绩列表 分析
	 * 
	 * @param userId
	 *            xxt侧对应的用户ID
	 * @param type
	 *            用户类型 1:代表老师，2:代表家长/学生
	 * 
	 * */
	public String getTeaOneExamInfoPoit(InputObject inobj) {
		String key = Constants.XXT_SECRET.OneExamList;
		OutputObject outputObject = new OutputObject();
		// String courseId = "945294";
		// String examId = "2170122";
		// String gradeId = "3062231";
		String courseId = inobj.getParams().get("courseId");
		String examId = inobj.getParams().get("examId");
		String gradeId = inobj.getParams().get("gradeId");
		// 拼接加密之后的参数串
		String courseIdEncrypted = codeUtil.lettlerEncrypted2(courseId, key);
		String examIdEncrypted = codeUtil.lettlerEncrypted2(examId, key);
		String gradeIdEncrypted = codeUtil.lettlerEncrypted2(gradeId, key);
		String param = "&courseId=" + courseIdEncrypted + "&examId="
				+ examIdEncrypted + "&gradeId=" + gradeIdEncrypted;
		String serialNo = inobj.getParams().get("serialNo");
		String reqStr = codeUtil.getParamData2(serialNo, examId, key, param);
		RemoteRespHelper helper = new RemoteRespHelper(String.valueOf(appRemoteCaller.getGradeScore(reqStr, courseId)));
		if(!helper.getRtnCode().equals(ControlConstants.RTNCODE_ERR)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage(helper.getRtnMsg());
			return convertOutputObject2RtsEr(outputObject);
		}
		List<Map<String, Object>> oneCourseScore = helper.getCourseScore();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("100-91", 0);
		map.put("90-81", 0);
		map.put("80-71", 0);
		map.put("70-61", 0);
		map.put("60-0", 0);
		Iterator<String> keyit = map.keySet().iterator();
		while (keyit.hasNext()) {
			String mapKey = keyit.next();
			for (Map<String, Object> m : oneCourseScore) {
				if (null != m.get("SCORE")) {
					String score = m.get("SCORE").toString();
					String[] tmpscore = mapKey.split("-");
					if (mathMaxMin(score, tmpscore[0], tmpscore[1])) {
						Integer val = (Integer) map.get(mapKey);
						map.put(mapKey, Integer.valueOf(val + 1));
					}
				}
			}
		}
		List<Map<String, Object>> poitList = new ArrayList<Map<String, Object>>();
		Map<String, String> colorMap = new HashMap<String, String>();
		colorMap.put("100-91", "#b1c262");
		colorMap.put("90-81", "#8dddfa");
		colorMap.put("80-71", "#7cbddd");
		colorMap.put("70-61", "#ffaa76");
		colorMap.put("60-0", "#e46959");
		for (Entry<String, Object> entry : map.entrySet()) {
			Map<String, Object> rtnMap = new HashMap<String, Object>();
			rtnMap.put("name", entry.getKey());
			rtnMap.put("value", entry.getValue());
			rtnMap.put("color", colorMap.get(entry.getKey()));
			poitList.add(rtnMap);
		}
		// poitList.add(map);
		return convert(poitList, inobj.getParams().get(ControlConstants.UID));
	}
	
	public boolean mathMaxMin(String num,String max,String min){
		if(Double.parseDouble(num) <= Double.parseDouble(max) && Double.parseDouble(num) >= Double.parseDouble(min)){
			return true;
		}else{
			return false;
		}
	}
	
	public String sendClassPicMsg(InputObject inobj) throws Exception {
		String key = Constants.XXT_SECRET.MsgPicSend;
		OutputObject outputObject = new OutputObject();
		InputObjectAdpter inputObj = new InputObjectAdpter(inobj);
		File msgFile = inputObj.getFile(0);
		if (msgFile == null) {
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage("请选择要发送的语言/图片!");
			return convertOutputObject2RtsEr(outputObject);
		}
		// 2M
		if (msgFile.length() > 2 * 1024 * 1024) {
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage("文件内容过大，文件大小为2M!");
			return convertOutputObject2RtsEr(outputObject);
		}
		//1 语音；2图片
		String fileType = inobj.getParams().get("fileType");
		String fileName = inobj.getParams().get("fileName");
		String fileTimeLong = inobj.getParams().get("fileTimeLong");
		String userId = inobj.getParams().get("userId");
		String userType = inobj.getParams().get("userType");
		String schoolId = inobj.getParams().get("schoolId");
		String name = Constants.SEND_MSG.STU_NAME;
//		String userId = "5864263";
//		String userType = "0";
//		String schoolId = "65789079014073";
		String msgContent = inobj.getParams().get("msgContent");
		if(StringUtil.isEmpty(userId)||StringUtil.isEmpty(userType)||StringUtil.isEmpty(schoolId)){
			 outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			 outputObject.setReturnMessage("获取用户信息参数失败!");
			 return convertOutputObject2RtsEr(outputObject);
		}
		// userType转换
		if (userType.equals(String.valueOf(Constants.USER_TYPE.TEACHER))) {
			userType = Constants.XXT_USER_TYPE.TEACHER;
			name = Constants.SEND_MSG.TEA_NAME;
		} else if(userType.equals(String.valueOf(Constants.USER_TYPE.STUDENT))){
        	HttpSession session = getRequest().getSession(false);
			HEAUser heaUser = (HEAUser) session.getAttribute(HEAUser.HEA_PT_USER);
			if (heaUser == null) {
				outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
				outputObject.setReturnMessage("请行进行登陆或获取用户信息参数失败!");
				return convertOutputObject2RtsEr(outputObject);
			}
			userId=heaUser.getUserId();
        	userType=Constants.XXT_USER_TYPE.PARENT;
		}
		fileName = URLEncoder.encode(fileName, "UTF-8");
		String msgType = StringUtil.trim(inobj.getParams().get("msgType"), "gg");
		String destUserType = inobj.getParams().get("destUserType");
		//destUserType转换
		if(null != destUserType && destUserType.equals(String.valueOf(Constants.CRM_USER_TYPE.DEST_USER_TEA))){
			destUserType=Constants.XXT_USER_TYPE.TEACHER;
        }else if(null != destUserType && destUserType.equals(String.valueOf(Constants.CRM_USER_TYPE.DEST_USER_STU))){
        	destUserType=Constants.XXT_USER_TYPE.PARENT;
        }
		String destId = StringUtil.trim(inobj.getParams().get("destId"),"");
		String destUnitId = StringUtil.trim(inobj.getParams().get("destUnitId"), "5386035");
		String ggName = StringUtil.trim(inobj.getParams().get("ggName"), "老师");
		String tempId = inobj.getParams().get("tempId");
		// 公告
		ggName = URLEncoder.encode(ggName, "UTF-8");
		name = URLEncoder.encode(name, "UTF-8");
		// 拼接加密之后的参数串
		String fileTypeEncrypted = codeUtil.lettlerEncrypted2(fileType, key);
		String fileNameEncrypted = codeUtil.lettlerEncrypted2(fileName, key);
		String fileTimeLongEncrypted = codeUtil.lettlerEncrypted2(fileTimeLong,key);
		String userIdEncrypted = codeUtil.lettlerEncrypted2(userId, key);
		String userTypeEncrypted = codeUtil.lettlerEncrypted2(userType, key);
		String schoolIdEncrypted = codeUtil.lettlerEncrypted2(schoolId, key);
		String msgContentEncrypted = codeUtil.lettlerEncrypted2(msgContent, key);
		String msgTypeEncrypted = codeUtil.lettlerEncrypted2(msgType, key);
		String destUserTypeEncrypted = codeUtil.lettlerEncrypted2(destUserType,key);
		String destIdEncrypted = codeUtil.lettlerEncrypted2(destId, key);
		String destUnitIdEncrypted = codeUtil.lettlerEncrypted2(destUnitId, key);
		String ggNameEncrypted = codeUtil.lettlerEncrypted2(ggName, key);
		String nameEncrypted = codeUtil.lettlerEncrypted2(name, key);
		String param = "&userId=" + userIdEncrypted + "&userType="
				+ userTypeEncrypted + "&schoolId=" + schoolIdEncrypted
				+ "&msgContent=" + msgContentEncrypted + "&msgType="
				+ msgTypeEncrypted + "&destUserType=" + destUserTypeEncrypted
				+ "&destId=" + destIdEncrypted + "&destUnitId="
				+ destUnitIdEncrypted + "&ggName=" + ggNameEncrypted + "&name="
				+ nameEncrypted + "&fileType=" + fileTypeEncrypted
				+ "&fileName=" + fileNameEncrypted + "&fileTimeLong="
				+ fileTimeLongEncrypted;
		String serialNo = inobj.getParams().get("serialNo");
		String reqStr = codeUtil.getParamData2(serialNo, userId, key, param);
		RemoteRespHelper helper = new RemoteRespHelper(String.valueOf(appRemoteCaller.sendPicAnnouncement(reqStr,fileName, msgFile, userId)));
		if(!helper.getRtnCode().equals(ControlConstants.RTNCODE_ERR)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage(helper.getRtnMsg());
			return convertOutputObject2RtsEr(outputObject);
		}
		List<Map<String, Object>> workContent = helper.getWorkContent();
		String rtnJson = convert(workContent,inobj.getParams().get(ControlConstants.UID));
		Map<String, Object> rtnMap = JsonUtil.convertJson2Object(rtnJson,Map.class);
		rtnMap.put("tempId", tempId);
		return JsonUtil.convertObject2Json(rtnMap);
	}
  
    //作业列表
	public void setWorkstate(List<Map<String, Object>> works, String userId,Map<String, Map<String, String>> finishWorks) {
		if (works != null) {
			for (Map<String, Object> work : works) {
				String mapKey = StringUtil.obj2TrimStr(userId) + "_"+ StringUtil.obj2TrimStr(work.get("id"));
				if (finishWorks.containsKey(mapKey)) {
					work.put("worktime", finishWorks.get(mapKey)	.get("workTime"));
					work.put("workstate",finishWorks.get(mapKey).get("workState"));
				} else {
					work.put("worktime", "0");
					work.put("workstate", "0");
				}
			}
		}
	}
   
	public void setCourseId(List<Map<String, Object>> works) {
		if(works==null||works.size()==0){
			return;
		}
		Map<String, String> courseMap = new HashMap<String, String>();
		courseMap.put("语言", "1");
		courseMap.put("数学", "2");
		courseMap.put("英语", "3");
		courseMap.put("体育", "4");
		courseMap.put("社会", "5");
		courseMap.put("音乐", "6");
		courseMap.put("美术", "7");
		courseMap.put("科学", "8");
		courseMap.put("艺术", "9");
		courseMap.put("手工", "10");
		courseMap.put("生活与健康", "11");
		courseMap.put("语文", "12");
		courseMap.put("自然", "13");
		courseMap.put("微机", "14");
		courseMap.put("计算机", "15");
		courseMap.put("信息技术", "16");
		courseMap.put("体育与健康", "17");
		courseMap.put("品德与社会", "18");
		courseMap.put("品德与生活", "19");
		courseMap.put("物理", "20");
		courseMap.put("化学", "21");
		courseMap.put("生物", "22");
		courseMap.put("地理", "23");
		courseMap.put("历史", "24");
		courseMap.put("政治", "25");
		courseMap.put("思想品德", "26");
		courseMap.put("理科综合", "27");
		courseMap.put("文科综合", "28");
		if (works != null) {
			for (Map<String, Object> work : works) {
				work.put("courseId", StringUtil.trim(courseMap.get(work.get("course")), "000"));
			}
		}
	}
	
	private String[] getTeaWorkIds(List<Map<String, Object>> works) {
		List<String> workIds = new ArrayList<String>();
		List<String> gradeIds = new ArrayList<String>();
		if (works != null) {
			for (Map<String, Object> work : works) {
				String workId = StringUtil.obj2TrimStr(work.get("id"));
				String gradeId = StringUtil.obj2TrimStr(work.get("gradeId"));
				if (!workIds.contains(workId))
					workIds.add(workId);
				if (!gradeIds.contains(gradeId))
					gradeIds.add(gradeId);
			}
		}
		String workId = StringUtil.arrayJoin(
				workIds.toArray(new String[workIds.size()]), ",");
		String gradeIdList = StringUtil.arrayJoin(
				gradeIds.toArray(new String[gradeIds.size()]), ",");
		return new String[] { workId, gradeIdList };
	}
	
	private void setTeaGradeWorkstate(List<Map<String, Object>> works,
			Map<String, Map<String, String>> finishWorks) {
		if (works != null) {
			for (Map<String, Object> work : works) {
				String mapkey = work.get("gradeId") + "-" + work.get("id");
				if (finishWorks.containsKey(mapkey)) {
					work.put("finishedNum",finishWorks.get(mapkey).get("finishRate"));
					work.put("unFinishedNum",finishWorks.get(mapkey).get("unFinishRate"));
				} else {
					work.put("finishedNum", "0%");
					work.put("unFinishedNum", "100%");
				}
			}
		}
	}
	
	public String getHomeWorkDtlList(InputObject inobj){
	 OutputObject outputObject = callCoreService.execute(inobj);
		Map<String,Object> rtn = new HashMap<String,Object>();
		if(outputObject.getReturnCode().equals(ControlConstants.RTNCODE_ERR)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage("查询失败！");
			return convertOutputObject2RtsEr(outputObject);
		}
		rtn.put("rtnCode", "1");
		rtn.put("rtnMsg", "调用成功");
		rtn.put("retInfo", outputObject.getBeans());
		return JsonUtil.convertObject2Json(rtn);
	}
	
	public String getHomeWorkRateDtlList(InputObject inobj){
		OutputObject outputObject = callCoreService.execute(inobj);
		if(outputObject.getReturnCode().equals(ControlConstants.RTNCODE_ERR)){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage("查询失败！");
			return convertOutputObject2RtsEr(outputObject);
		}
		String str=outputObject.getBusiCode();
		Map<String,Object> rtnMap=JsonUtil.convertJson2Object(str, Map.class);
		List<Map<String, String>>  rateList=(List<Map<String, String>>) rtnMap.get("rateList");
		List<Map<String, String>>  stdList=(List<Map<String, String>>) rtnMap.get("stdList");
		if(rateList==null||rateList.size()==0){
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage("查询失败！");
			return convertOutputObject2RtsEr(outputObject);
		}
		List<Map<String, String>>  rtnRateList=new ArrayList<Map<String,String>>();
		Map<String, String> rateMap=rateList.get(0);
		Map<String,Object> rtn = new HashMap<String,Object>();
		Map<String, String> fsMap=new HashMap<String, String>();
		fsMap.put("name", "已完成");
		fsMap.put("value", rateMap.get("finishRate"));
		fsMap.put("color","#5DBABE");
		rtnRateList.add(fsMap);
		
		fsMap=new HashMap<String, String>();
		fsMap.put("name", "未完成");
		fsMap.put("value", rateMap.get("unFinishRate"));
		fsMap.put("color","#E4E3E3");
		rtnRateList.add(fsMap);
		
		rtn.put("rtnCode", "1");
		rtn.put("rtnMsg", "调用成功");
		rtn.put("beans", rtnRateList);
		rtn.put("rows", stdList);
		return JsonUtil.convertObject2Json(rtn);
	}
	
	public String convertOutputObject2RtsEr(OutputObject outputObject){
		Map<String,Object> rtn = new HashMap<String,Object>();
		rtn.put("retCode", outputObject.getReturnCode());
		rtn.put("retMsg", outputObject.getReturnMessage());
		rtn.put("retInfo", outputObject.getBeans());
		return JsonUtil.convertObject2Json(rtn);
	}
}
