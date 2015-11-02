package com.ai.eduportal.action;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.ai.eduportal.Util.Constants;
import com.ai.eduportal.bean.User;
import com.ai.eduportal.remote.CodeUtil;
import com.ai.eduportal.remote.RemoteCallerFactoryI;
import com.ai.eduportal.remote.RemoteRespHelper;
import com.ai.frame.bean.InputObject;
import com.ai.frame.bean.OutputObject;
import com.ai.frame.util.ControlConstants;
import com.ai.frame.util.SpringContextHelper;
import com.ai.frame.web.action.BaseAction;
import com.ai.frame.web.util.SystemPropUtil;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;
import common.ai.tools.DateUtil;
import common.ai.tools.IOUtil;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

public class LoginAction extends BaseAction {
	private Logger logger = LoggerFactory.getActionLog(this.getClass());
	private RemoteCallerFactoryI remoteCaller;
	private static final String RTN_SUC = "0";//校讯通调用成功0 失败为1
	private static final String XXB_RTN_SUC = "1";//学习报 同步课堂调用成功标识
	public void setRemoteCaller(RemoteCallerFactoryI remoteCaller) {
		this.remoteCaller = remoteCaller;
	}
	private void sendJson(String respJson,boolean isGzip){
		if(isGzip){
			OutputStream writer = null;
			try{
				byte[] rtndata  = IOUtil.compressByteWithGzip(respJson.getBytes("UTF-8"));
				
				getResponse().setHeader("Content-Encoding", "gzip");
				getResponse().setContentLength(rtndata.length);
				
				writer = getResponse().getOutputStream();
				writer.write(rtndata);
				
				writer.flush();
			}catch(Exception e){
				logger.error("excute", respJson, "{} called response is:{} error:{}", e);
			}finally{
				if(writer!=null)IOUtil.closeOutputStream(writer);
				logger.debug(respJson,"send Json finally:{}");
			}
		}else{
			sendJson(respJson);
		}
	}
//	public String ssoauth(){
//		logger.info("ssoauth", "弹转成功,{}.");
//		String samlArt = getRequest().getParameter("SAMLart");
//		String relayState = getRequest().getParameter("RelayState");
//		System.out.println("samlArt:"+samlArt);
//		System.out.println("relayState:"+relayState);
//		
//		
//		
//		return "success";
//	}
//	public String ssoback(){
//		CookiesUtil cookutil = new CookiesUtil(getRequest());
//		String cmtokenid = cookutil.getCookieVal(CookiesUtil.COOKIES_CMTOKENID);
//		if(StringUtil.isEmpty(cmtokenid)){
//			logger.error("sso login", "{} 弹出层登录验证失败.");
//			//TODO
//			return "error";
//		}else{
//			logger.info(cmtokenid, "弹出层登陆验证成功,cmtokenid={}.");
//			AuthnRequest authnRequest = new AuthnRequest();
//			authnRequest.setID(SAMLUtil.newSAMLMsgID());
//			authnRequest.setIssueInstant(SAMLUtil.getUTCTimeStr());
//			authnRequest.setIssuer(SSOClientConfig.getIssuer());
//			authnRequest.setAllowCreate("true");
//			String loginPost=SAMLUtil.getAuthnRequestHtml("https://ha.ac.10086.cn/SSOPOST", authnRequest.toXML(), "module/choiceRole.html");
////			System.out.println("ssoback----"+loginPost);
////			
////			try {
////				HttpServletResponse resp = getResponse();
////				resp.getWriter().write(loginPost);
////				resp.getWriter().flush();
////			} catch (IOException e) {
////			}
//			
//			return "success";
//		}
//		
//	}
	
	//是否登录
	public String isLogin() {
		OutputObject outObj = getOutputObject();
		HttpSession session=getSession(false);
		User user=null;
		int favoritesize = outObj.getBeans().size();
		outObj.getBeans().clear();
		if(session==null){
			outObj.setReturnCode(ControlConstants.RTNCODE_ERR);
			outObj.setReturnMessage("has't login");
		}else{
			user=(User) session.getAttribute(User.CURRENT_USER);
		}		
		if(user == null || StringUtil.isEmpty(user.getUserId())){
			outObj.setReturnCode(ControlConstants.RTNCODE_ERR);
			outObj.setReturnMessage("has't login");
		}else{
			outObj.setBean(JsonUtil.convertBean2Map(user));
			if(user.getTypeid()!=2){
				//2015年2月 添加用户头像
				if(StringUtil.isEmpty(user.getUserIcon())){
					outObj.getBean().put("IMGURL","../theme/images/0.png");
				}else{
					outObj.getBean().put("IMGURL",user.getUserIcon());
				}
			}else{
				if(StringUtil.isEmpty(user.getUserIcon())){
					outObj.getBean().put("IMGURL","../theme/images/0.png");
				}else{
					outObj.getBean().put("IMGURL",user.getUserIcon());
				}
			}
			outObj.setReturnCode(ControlConstants.RTNCODE_SUC);
			outObj.setReturnMessage("call success");
		}
		outObj.getBean().put("favoritesize", String.valueOf(favoritesize));
		String json = JsonUtil.convertObject2Json(outObj);
		json = json.replaceAll("returnCode", "rtnCode");
		json = json.replaceAll("returnMessage", "rtnMsg");
		//getSession(true).setAttribute(User.LOGIN_USER, user);
		sendJson(json,true);
		return null;
	}
		
	//登录
	public String login() {
		InputObject inputObject = getInputObject();
		//OutputObject outObj = getOutputObject(inputObject);
//		String mobile = inputObject.getParams().get("phoneNum");
//		boolean result=loginService.login(mobile);
//		OutputObject outObj = new OutputObject();
//		if(result){
		OutputObject outObj = new OutputObject();
		outObj.setReturnCode(ControlConstants.RTNCODE_SUC);
		if(outObj.getReturnCode().equals(ControlConstants.RTNCODE_SUC)){//登录成功
			String phonNum = inputObject.getParams().get("mobile");
			User user = new User();
			user.setMobile(StringUtil.str2Long(phonNum));
			user.setUserId("0");
			user.setUserName(phonNum);
			user.setSex(1);
//			user.setUserType(1);
			//ClassUtil.setPropValFromMap(outObj.getBean(), user);
			
			getSession(true).setAttribute(User.LOGIN_USER, user);
			outObj.setBean(JsonUtil.convertBean2Map(user));
			outObj.setReturnCode(ControlConstants.RTNCODE_SUC);
			outObj.setReturnMessage("call success");
		}else{
			outObj.setReturnCode(ControlConstants.RTNCODE_ERR);
			if(ControlConstants.RTNCODE_ERR.equals(outObj.getReturnCode())){
				outObj.setReturnMessage("此号码还没有注册，请重新注册后再登录.");
			}else{
				outObj.setReturnMessage("手机号或是密码错误.");
			}
		}
		outObj.getBean().put("cacheKey", inputObject.getParams().get("cacheKey"));
		
		String json = JsonUtil.convertObject2Json(outObj);
		json=json.replaceAll("returnCode", "rtnCode");
		json=json.replaceAll("returnMessage", "rtnMsg");
		sendJson(json,true);
		return null;
	}

	//发送验证码
	public String sendRandNum() {
		InputObject inputObject = getInputObject();
		String mobile=inputObject.getParams().get("mobile");
		OutputObject outObj=new OutputObject();
		if(StringUtil.isEmpty(mobile)||mobile.trim().length()!=11){
			outObj.setReturnCode(ControlConstants.RTNCODE_ERR);
			outObj.setReturnMessage("请输入正确的手机号码");
			String json = JsonUtil.convertObject2Json(outObj);
			json=json.replaceAll("returnCode", "rtnCode");
			json=json.replaceAll("returnMessage", "rtnMsg");
			sendJson(json,true);
			return null;
		}
		int sendTimeDiff=StringUtil.str2Int(SystemPropUtil.getString("sms_send_time_diff"));
		String isCheckTime=SystemPropUtil.getString("is_check_sms_send_time");
		if (isCheckTime != null) {
			if ("y".equalsIgnoreCase(isCheckTime)|| "true".equalsIgnoreCase(isCheckTime)) {
				String lastSendTime = SpringContextHelper.instance.getOscache().get("Rand_" + mobile);
//				String timeStamp = DateUtil.date2String(new Date(),"yyyyMMddHHmmss");
				if (lastSendTime == null) {
					//SpringContextHelper.instance.getOscache().put("Rand_" + mobile, timeStamp);
				} else {
					int timeDiff = (int) getDayDiff(DateUtil.string2Date(lastSendTime, "yyyyMMddHHmmss"), new Date(), 1000);
					if (timeDiff < sendTimeDiff) {
						outObj.setReturnCode(ControlConstants.RTNCODE_ERR);
						outObj.setReturnMessage(String.format("验证码发送间隔时间为%s秒",sendTimeDiff));
						String json = JsonUtil.convertObject2Json(outObj);
						json = json.replaceAll("returnCode", "rtnCode");
						json = json.replaceAll("returnMessage", "rtnMsg");
						sendJson(json, true);
						return null;
					} else {
						//SpringContextHelper.instance.getOscache().put("Rand_" + mobile, timeStamp);
					}
				}
			}
		}
		outObj = getOutputObject(inputObject);
		if(outObj==null){
			outObj=new OutputObject();
			outObj.setReturnCode(ControlConstants.RTNCODE_ERR);
			outObj.setReturnMessage("系统忙,发送验证码失败");
		}else if(outObj.getReturnMessage()!=null && outObj.getReturnMessage().indexOf("call ")!=-1&&outObj.getReturnMessage().indexOf("error")!=-1){
			outObj.setReturnMessage("系统忙,发送验证码失败");
		}
		System.out.println("--------------------------="+outObj.getReturnCode());
		if(ControlConstants.RTNCODE_SUC.equals(outObj.getReturnCode())){
			
			HttpSession session = getRequest().getSession(true);
			session.setAttribute("R_Rand", outObj.getBusiCode());
			outObj.setBusiCode("");
			
			String timeStamp = DateUtil.date2String(new Date(),DateUtil.YYYYMMDDHHMMSS);
			SpringContextHelper.instance.getOscache().put("Rand_" + mobile, timeStamp);
		}
		//清除验证码
		outObj.setBusiCode("");
		String json = JsonUtil.convertObject2Json(outObj);
		json=json.replaceAll("returnCode", "rtnCode");
		json=json.replaceAll("returnMessage", "rtnMsg");
		sendJson(json,true);
		return null;
	}

	//检查验证码 校验时间间隔
	public String checkRandNum() {
		InputObject inputObject = getInputObject();
		String mobile = inputObject.getParams().get("phoneNum");
		String randNum= inputObject.getParams().get("smsValidate");
		OutputObject outObj =getOutputObject(inputObject);
		//System.out.println("-------------mobile=" + mobile + "---------rand="+ randNum);
		if (ControlConstants.RTNCODE_SUC.equals(outObj.getReturnCode())) {
			HttpSession session = getRequest().getSession(false);
			if(session==null){
				outObj.setReturnCode(ControlConstants.RTNCODE_ERR);
				outObj.setReturnMessage("请先获取验证码");
			}else{
				String randInfo=(String) session.getAttribute("R_Rand");
				if(!StringUtil.isEmpty(randInfo)&&randInfo.indexOf("|")>-1){
					String randStr=randInfo.split("\\|")[0];
					String dateStr=randInfo.split("\\|")[1];
					long dateDiff=getDayDiff(DateUtil.string2Date(dateStr, DateUtil.YYYYMMDDHHMMSS), new Date(),60 * 1000);
					//验证码30分钟过期
					if(dateDiff>30){
						session.removeAttribute("R_Rand");
						outObj.setReturnCode(ControlConstants.RTNCODE_ERR);
						outObj.setReturnMessage("验证码已过期");
					}else{
						if(randStr.equalsIgnoreCase(randNum)){
							//session.removeAttribute("R_Rand");//第2步参数校验
							outObj.setReturnCode(ControlConstants.RTNCODE_SUC);
							outObj.setReturnMessage("call success");
						}else{
							outObj.setReturnCode(ControlConstants.RTNCODE_ERR);
							outObj.setReturnMessage("验证码错误");
						}
					}
				}else{
					outObj.setReturnCode(ControlConstants.RTNCODE_ERR);
					outObj.setReturnMessage("验证码错误");
				}
			}
		}
		String json = JsonUtil.convertObject2Json(outObj);
		json=json.replaceAll("returnCode", "rtnCode");
		json=json.replaceAll("returnMessage", "rtnMsg");
		sendJson(json,true);
		return null;
	}
	public String excuteClear(){
//		MyService.truncateSsoCookie2(getRequest(), getResponse());
		
		sendJson("{\"rtnCode\":\"1\"}", true);
		//getRequest().getSession().invalidate();
		return null;
	}
	//角色切换
	@SuppressWarnings("unchecked")
	public String switchRole(){
		InputObject inputObject = getInputObject();
		OutputObject object = getOutputObject(inputObject);
		OutputObject outObj = new OutputObject();
		String userId = inputObject.getParams().get("userId");
		String userType = inputObject.getParams().get("userType");
		String userName=inputObject.getParams().get("userName");
		String userImgUrl=inputObject.getParams().get("imgUrl");
        String orgId=inputObject.getParams().get("orgId");
		String sex=inputObject.getParams().get("sex");
		//logger.info("switchRole", "-------切换角色后,传入的username="+userName);
		if (!StringUtil.isEmpty(userId)) {
			HttpSession session = getRequest().getSession(false);
			List<User> userList=new ArrayList<User>();
			if(session!=null&&object!=null){
				User loginUser=(User) session.getAttribute(User.LOGIN_USER);//取登录用户手机号码
				long mobile=loginUser.getMobile();
				List<Map<String,String>> rtnBeans=object.getBeans();
				User currentUser=new User();
				currentUser.setMobile(mobile);
				currentUser.setUserId(StringUtil.trim(userId));
				currentUser.setSysType(1);
				currentUser.setOtherUserId(StringUtil.str2Long(userId));
				currentUser.setTypeid(StringUtil.str2Int(userType));
				currentUser.setUserName(userName);
				//2015年2月 添加用户头像
				currentUser.setUserIcon(userImgUrl);
                currentUser.setIsMulRole(1);
                currentUser.setOrgId(StringUtil.str2Long(orgId));
				currentUser.setSex(StringUtil.str2Int(sex));
				//获取其他系统用户绑定的id
				for (Map<String, String> map : rtnBeans) {
					User user=new User();
					user.setUserId(StringUtil.trim(userId));
					user.setUserName(map.get("USERNAME"));
					user.setMobile(mobile);
					user.setSysType(StringUtil.str2Int(map.get("SYSTYPE")));
					user.setOtherUserId(StringUtil.str2Long(map.get("OTHERUSERID")));
					user.setTypeid(StringUtil.str2Int(map.get("USERTYPE")));
					user.setUserName(map.get("OTHERUSERNAME"));
					userList.add(user);
				}
				//2014-11-12 添加当前用户[校讯通]
				userList.add(currentUser);
				session.setAttribute(User.CURRENT_USER, currentUser);//用户类型
				session.setAttribute(User.USER_LIST, userList);
				outObj.setReturnCode(ControlConstants.RTNCODE_SUC);
				outObj.setReturnMessage("角色切换成功");
			}else{
				outObj.setReturnCode(ControlConstants.RTNCODE_ERR);
				outObj.setReturnMessage("角色切换失败");
			}
		}else{
			outObj.setReturnCode(ControlConstants.RTNCODE_ERR);
			outObj.setReturnMessage("call error.");
		}
		String cacheKey = inputObject.getParams().get("cacheKey");
//		Map cacheMap = SpringContextHelper.instance.getOscache().get(cacheKey);
		Map cacheMap = null;
		if(!StringUtil.isEmpty(cacheKey)){
			String cache = SpringContextHelper.instance.getOscache().get(cacheKey);
			if(!StringUtil.isEmpty(cache)){
				cacheMap = JsonUtil.convertJson2Object(cache, Map.class);
			}
			
		}
		
//		Map cacheMap = DirectCache.getInstance().getVal(cacheKey);
		
//		Iterator keys = cacheMap.keySet().iterator();
//		while(keys.hasNext()){
//			String key = (String)keys.next();
//			String redirect  = getCacheVal(key,cacheMap);
//			System.out.println(key+"============value:========="+redirect);
//		}
		if(cacheMap!=null){
			String redirect  = getCacheVal("redirect",cacheMap);
			String sourceFlg = getCacheVal("sourceFlg",cacheMap);
			if(StringUtil.str2Int(sourceFlg) == 1){//立即订购
				String appType = getCacheVal("isMutiApp",cacheMap);
				String sysType = getCacheVal("sysType",cacheMap);
				
				if(StringUtil.str2Int(appType) !=1){//单独应用
					int stype = StringUtil.str2Int(sysType);
					Object userRoles = getSession().getAttribute(User.USER_LIST);
					
					String sysRoleId = User.getSysTypeRole(userRoles,stype);
					if(StringUtil.isEmpty(sysRoleId)){
						sysRoleId = userId;
					}
					
					redirect = addFiled2Url(redirect,"roleId",sysRoleId);
				}else{
					redirect = addFiled2Url(redirect,"cacheKey",inputObject.getParams().get("cacheKey"));
				}
			}else{
				redirect = addFiled2Url(redirect,"cacheKey",inputObject.getParams().get("cacheKey"));
			}
			
			outObj.getBean().put("cacheKey", inputObject.getParams().get("cacheKey"));
			outObj.getBean().put("redirect", redirect);
		}
		
		String json = JsonUtil.convertObject2Json(outObj);
		json=json.replaceAll("returnCode", "rtnCode");
		json=new String(json.replaceAll("returnMessage", "rtnMsg"));
		sendJson(json,true);
		return null;
	}
	private String addFiled2Url(String redirect,String fieldNm,String fieldVal){
		//判空
		if(StringUtil.isEmpty(redirect)){
			return "";
		}
		if(redirect.indexOf("?")>-1){
			redirect = redirect + "&" + fieldNm + "=" + fieldVal;
		}else{
			redirect = redirect + "?" + fieldNm + "=" + fieldVal;
		}
		return redirect;
	}
	@SuppressWarnings("unchecked")
	private String getCacheVal(String fieldVal,Map cacheMap){
		Object obj = cacheMap.get(fieldVal);
		if(obj instanceof String[]){
			String[] urls = (String[])obj;
			return urls[0]; 
		}else if (obj instanceof String){
			return String.valueOf(obj); 
		}
		
		return null;
	}
	
	// 得到用户信息及帐号绑定信息(用于帐号绑定)
	public String excuteGetAllUserInfo() {
		InputObject inputObject = getInputObject();
		String mobile = inputObject.getParams().get("mobile");
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		List<Map<String, String>> rtnBeansList = null;
		if (!StringUtil.isEmpty(mobile) && mobile.trim().length() == 11) {
			OutputObject object = getOutputObject(inputObject);
			rtnBeansList = object.getBeans();
			String jsonStr = object.getBusiCode();
			if (StringUtil.isEmpty(jsonStr)) {
				gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
				gridMp.put("rtnMsg", "调用crm接口出错，没有返回任何信息");
				sendJson(JsonUtil.convertObject2Json(gridMp), true);
				return null;
			}
			List<Map<String, String>> userList = JsonUtil.convertJson2Object(jsonStr, List.class);
			if (userList==null||userList.size()==0) {
				gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
				gridMp.put("rtnMsg", "调用crm接口出错，没有返回任何信息");
				sendJson(JsonUtil.convertObject2Json(gridMp), true);
				return null;
			}

			// 2015-5-11日 删除家长信息
			if (userList != null) {
				Iterator<Map<String, String>> it = userList.iterator();
				while (it.hasNext()) {
					Map<String, String> map = it.next();
					if (map.get("userTp").equals(String.valueOf(Constants.USER_TYPE.PARENT))) {
						it.remove();
					}
				}
			}
			// 学生信息
			List<Map<String, String>> studenList = new ArrayList<Map<String, String>>();
			// 老师信息
			List<Map<String, String>> teaList = new ArrayList<Map<String, String>>();
			for (Map<String, String> map : userList) {
				Map<String, String> rtnMap = getCrmUserMap(map);
				if (Constants.CRM_USER_TYPE.TEACHER.equals(map.get("userTp"))) {
					if (rtnBeansList != null && rtnBeansList.size() > 0) {
						String userId = rtnMap.get("userId");
						for (Map<String, String> rtnBeanMap : rtnBeansList) {
							if (String.valueOf(Constants.USER_TYPE.TEACHER).equals(rtnBeanMap.get("USERTYPE"))&& rtnBeanMap.get("USERID").equals(userId)) {
								if (Constants.SYSTEM_TYPE.TNKT.equals(rtnBeanMap.get("SYSTYPE"))) {
									rtnMap.put("tbktId", rtnBeanMap.get("OTHERUSERID"));
									rtnMap.put("tbktName",rtnBeanMap.get("OTHERUSERNAME"));
								} else if (Constants.SYSTEM_TYPE.XXB.equals(map.get("SYSTYPE"))) {
									rtnMap.put("xxbId", rtnBeanMap.get("OTHERUSERID"));
									rtnMap.put("xxbName",rtnBeanMap.get("OTHERUSERNAME"));
								}
							}
						}
					}
					teaList.add(rtnMap);
				} else {
					if (rtnBeansList != null && rtnBeansList.size() > 0) {
						String userId = rtnMap.get("userId");
						for (Map<String, String> rtnBeanMap : rtnBeansList) {
							if (String.valueOf(Constants.USER_TYPE.STUDENT).equals(rtnBeanMap.get("USERTYPE"))&& rtnBeanMap.get("USERID").equals(userId)) {
								if (Constants.SYSTEM_TYPE.TNKT.equals(rtnBeanMap.get("SYSTYPE"))) {
									rtnMap.put("tbktId", rtnBeanMap.get("OTHERUSERID"));
									rtnMap.put("tbktName",rtnBeanMap.get("OTHERUSERNAME"));
								} else if (Constants.SYSTEM_TYPE.XXB.equals(rtnBeanMap.get("SYSTYPE"))) {
									rtnMap.put("xxbId", rtnBeanMap.get("OTHERUSERID"));
									rtnMap.put("xxbName",rtnBeanMap.get("OTHERUSERNAME"));
								}
							}
						}
					}
					studenList.add(rtnMap);
				}
			}
			gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
			gridMp.put("rtnMsg", "call success");
			gridMp.put("parents", studenList);
			gridMp.put("teacher", teaList);
		}else {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "请先登录");
		}
		sendJson(JsonUtil.convertObject2Json(gridMp), true);
		return null;
	}
	
	public Map<String, String> getCrmUserMap(Map<String, String> map) {
		Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("userId", map.get("xxtuid"));
		rtnMap.put("userName", map.get("userNm"));
		rtnMap.put("city", map.get("area"));
		rtnMap.put("region", map.get("subArea"));
		rtnMap.put("schoolId", map.get("schoolId"));
		rtnMap.put("schoolName", map.get("schoolNm"));
		rtnMap.put("grade", map.get("gradeNm"));
		rtnMap.put("sex", map.get("sex"));// 1男 2女
		// 接口1代表老师 1老师 2学生 3家长
		if ("1".equals(map.get("userTp"))) {
			rtnMap.put("typeId", "2");
		} else {
			rtnMap.put("typeId", "1");
		}
		return rtnMap;
	}	

	// 得到用户校讯通的帐号信息(注册时展示信息)
	public String excuteGetDefaultInfo() {
		InputObject inputObject = getInputObject();
		String mobile = inputObject.getParams().get("phoneNum");
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(mobile)&&mobile.trim().length()==11) {
			String seqStr=null;
			RemoteRespHelper remoteHelper=null;
			String cacheKey="XXT_"+String.valueOf(mobile.trim());
			//先从缓存取
			String jsonStr=SpringContextHelper.instance.getOscache().get(cacheKey);
			boolean flag=StringUtil.isEmpty(jsonStr);
			if(flag){
				OutputObject object = getOutputObject(inputObject);
				if(ControlConstants.RTNCODE_SUC.equals(object.getReturnCode())){
					seqStr=object.getReturnMessage();
				}else{
					gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
					gridMp.put("rtnMsg", "获取参数[seq]错误");
					sendJson(JsonUtil.convertObject2Json(gridMp),true);
					return null;
				}
				jsonStr = remoteCaller.getXxtResponse(mobile,seqStr);
				if(StringUtil.isEmpty(jsonStr)){
					gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
					gridMp.put("rtnMsg", "调用校讯通接口出错，没有返回任何信息");
					sendJson(JsonUtil.convertObject2Json(gridMp),true);
					return null;
				}
				remoteHelper = RemoteRespHelper.getNewInstance(jsonStr);
				if (RTN_SUC.equals(remoteHelper.getRtnCode())) {
					SpringContextHelper.instance.getOscache().put(cacheKey,jsonStr);
				}else{
					gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
					gridMp.put("rtnMsg", String.format("调用校讯通接口出错，错误信息%s", remoteHelper.getRtnMsg()));
					sendJson(JsonUtil.convertObject2Json(gridMp),true);
					return null;
				}
			}else{
				remoteHelper = RemoteRespHelper.getNewInstance(jsonStr);
			}
			if (!RTN_SUC.equals(remoteHelper.getRtnCode())) {
				gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
				gridMp.put("rtnMsg", remoteHelper.getRtnMsg());
			} else {
				List<Map<String, String>> studenList = new ArrayList<Map<String, String>>();
				if (remoteHelper.getStdSize(null) > 0) {
					for (int i = 0, len = remoteHelper.getStdSize(null); i < len; i++) {
						Map<String, String> stuMap = new HashMap<String, String>();
						stuMap.put("userId",String.valueOf(remoteHelper.getStdInfo("USERID", i)));
						stuMap.put("userName",remoteHelper.getStdInfo("USERNAME", i));
						stuMap.put("city",remoteHelper.getStdInfo("AREA", i));
						stuMap.put("region",remoteHelper.getStdInfo("SUB_AREA", i));
						stuMap.put("schoolName",remoteHelper.getStdInfo("SCHOOL", i));
						String[]result=splitGradeInfo( remoteHelper.getStdInfo("GRADE", i));
						stuMap.put("grade", String.valueOf(result[0]));
						stuMap.put("subName",String.valueOf(result[1]));
						studenList.add(stuMap);
					}
				}
				// 老师信息
				List<Map<String, String>> teaList = new ArrayList<Map<String, String>>();
				if (remoteHelper.getTeaSize(null) > 0) {
					for (int i = 0, len = remoteHelper.getTeaSize(null); i < len; i++) {
						Map<String, String> teaMap = new HashMap<String, String>();
						teaMap.put("userId",String.valueOf(remoteHelper.getTeaInfo("USERID", i)));
						teaMap.put("userName",remoteHelper.getTeaInfo("USERNAME", i));
						teaMap.put("city", remoteHelper.getTeaInfo("AREA", i));
						teaMap.put("region",remoteHelper.getTeaInfo("SUB_AREA", i));
						teaMap.put("schoolName",remoteHelper.getTeaInfo("SCHOOL", i));
						teaList.add(teaMap);
					}
				}
				gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
				gridMp.put("rtnMsg", "call success");
				gridMp.put("parents", studenList);
				gridMp.put("teacher", teaList);
			}
		} else {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "参数[手机号码]错误");
		}
		String callback = getRequest().getParameter("callback");
		String respJson = JsonUtil.convertObject2Json(gridMp);
		if(StringUtil.isEmpty(callback)){
			sendJson(respJson,true);
		}else{
			sendJson(callback+"("+respJson+")");
		}
//		sendJson(JsonUtil.convertObject2Json(gridMp),true);
		return null;
	}

	//帐号绑定选择帐号
	/**
	 * @deprecated
	 */
	public String excuteGetSysAccoutInfo2(){
		InputObject inputObject = getInputObject();
		String mobile = inputObject.getParams().get("mobile");
		String sysType = inputObject.getParams().get("sysType");
		String userType = inputObject.getParams().get("userType");
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		List<Map<String, String>> rtnBeansList = null;
		List<Map<String, String>> acList=new ArrayList<Map<String,String>>();
		List<Map<String, String>> bindAcList=new ArrayList<Map<String,String>>();
		if(StringUtil.isEmpty(sysType)||StringUtil.isEmpty(userType)){
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "参数为空");
			sendJson(JsonUtil.convertObject2Json(gridMp),true);
			return null;
		}
		String tbktCacheKey="TBKT_"+String.valueOf(mobile.trim());//同步课堂
		String xxbCacheKey="XXB_"+String.valueOf(mobile.trim());//学习报
		String jsonStr=null;
		if (!StringUtil.isEmpty(mobile)&&mobile.trim().length()==11) {
			String userIdKey="USERID";
			String userNameKey="USERNAME";
			//同步课堂
			if("2".equals(sysType)){
				jsonStr=SpringContextHelper.instance.getOscache().get(tbktCacheKey);
			}else{
				jsonStr=SpringContextHelper.instance.getOscache().get(xxbCacheKey);
				userIdKey = "userId";
				userNameKey = "userName";
			}
			if (StringUtil.isEmpty(jsonStr)) {
				inputObject.getParams().put("needSeq", "1");
			}
			OutputObject object = getOutputObject(inputObject);
			rtnBeansList = object.getBeans();
			RemoteRespHelper remoteHelper=null;
			if (StringUtil.isEmpty(jsonStr)) {
				if (ControlConstants.RTNCODE_ERR.equals(object.getReturnCode())) {
					gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
					gridMp.put("rtnMsg", object.getReturnMessage());
					sendJson(JsonUtil.convertObject2Json(gridMp),true);
					return null;
				}
				String seqStr = null;
				if (rtnBeansList != null && rtnBeansList.size() > 0) {
					for (Map<String, String> map : rtnBeansList) {
						if (map.get("edu_serial") != null) {
							seqStr = map.get("edu_serial");
							break;
						}
					}
				}
				// 同步课堂
				if ("2".equals(sysType)) {
					jsonStr = remoteCaller.getTbkResponse(mobile, seqStr);
				} else {
					jsonStr = remoteCaller.getXxbResponse(mobile, seqStr);
				}
				if(StringUtil.isEmpty(jsonStr)){
					gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
					gridMp.put("rtnMsg", String.format("调用接口出错,没有返回任何信息"));
					sendJson(JsonUtil.convertObject2Json(gridMp),true);
					return null;
				}
				remoteHelper = RemoteRespHelper.getNewInstance(jsonStr);
				if (XXB_RTN_SUC.equals(remoteHelper.getRtnCode())) {
					if("2".equals(sysType)){
						SpringContextHelper.instance.getOscache().put(tbktCacheKey,jsonStr);
					}else{
						SpringContextHelper.instance.getOscache().put(xxbCacheKey,jsonStr);
					}
				}else{
					gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
					gridMp.put("rtnMsg", String.format("调用接口出错，错误信息%s", remoteHelper.getRtnMsg()));
					sendJson(JsonUtil.convertObject2Json(gridMp),true);
					return null;
				}
			}
			remoteHelper = RemoteRespHelper.getNewInstance(jsonStr);
			//同步课堂 学习报1 代表成功  0代表失败
			if (XXB_RTN_SUC.equals(remoteHelper.getRtnCode())) {
				//学生
				if ("1".equals(userType)) {
					if (remoteHelper.getStdSize(null) > 0) {
						for (int i = 0, len = remoteHelper.getStdSize(null); i < len; i++) {
							Map<String, String> acMap = new HashMap<String, String>();
							String userId = String.valueOf(remoteHelper.getStdInfo(userIdKey, i));
							acMap.put("isBind", "0");
							acMap.put("otherId", userId);
							acMap.put("otherName",remoteHelper.getStdInfo(userNameKey, i));
							if (rtnBeansList != null && rtnBeansList.size() > 0) {
								for (Map<String, String> map : rtnBeansList) {
									if (map.get("OTHERUSERID").equals(userId)) {
										//手动排序 已绑定帐号放在最后
										Map<String, String> bindMap = new HashMap<String, String>();
										bindMap.put("isBind", "1");
										bindMap.put("otherId", userId);
										bindMap.put("otherName",remoteHelper.getStdInfo(userNameKey, i));
										acMap.clear();
										acMap=null;
										bindAcList.add(bindMap);
										break;
									}
								}
							}
							if(acMap!=null){
								acList.add(acMap);
							}
						}
						if(bindAcList.size()>0){
							acList.addAll(bindAcList);
						}
					}
				} else {
					//老师
					if (remoteHelper.getTeaSize(null) > 0) {
						for (int i = 0, len = remoteHelper.getTeaSize(null); i < len; i++) {
							Map<String, String> acMap = new HashMap<String, String>();
							String userId = String.valueOf(remoteHelper.getTeaInfo(userIdKey, i));
							acMap.put("isBind", "0");
							acMap.put("otherId", userId);
							acMap.put("otherName",remoteHelper.getTeaInfo(userNameKey, i));
							if (rtnBeansList != null && rtnBeansList.size() > 0) {
								for (Map<String, String> map : rtnBeansList) {
									if (map.get("OTHERUSERID").equals(userId)) {
										//手动排序 已绑定帐号放在最后
										Map<String, String> bindMap = new HashMap<String, String>();
										bindMap.put("isBind", "1");
										bindMap.put("otherId", userId);
										bindMap.put("otherName",remoteHelper.getTeaInfo(userNameKey, i));
										acMap.clear();
										acMap=null;
										bindAcList.add(bindMap);
										break;
									}
								}
							}
							if(acMap!=null){
								acList.add(acMap);
							}
						}
						if(bindAcList.size()>0){
							acList.addAll(bindAcList);
						}
					}
				}
			}else{
				gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
				gridMp.put("rtnMsg", String.format("调用接口出错，错误信息%s", remoteHelper.getRtnMsg()));
				sendJson(JsonUtil.convertObject2Json(gridMp),true);
				return null;
			}
			gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
			gridMp.put("rtnMsg", "call success");
			gridMp.put("beans", acList);
		}else{
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "参数[手机号码]错误");
		}
		sendJson(JsonUtil.convertObject2Json(gridMp),true);
		return null;
	}
	
	//拆分年级信息
	public String[] splitGradeInfo(String grade){
		String[] result=new String[2];
		if(StringUtil.isEmpty(grade)){
			result[0]="";
			result[1]="";
			return result;
		}
		if(grade.indexOf("年级")>-1){
			result[0]=new String(grade.substring(0,grade.indexOf("年级")+2));
			grade=new String(grade.substring(grade.indexOf("年级")+2));
			result[1]=grade;
		}else{
			result[0]=grade;
			result[1]="";
		}
		return result;
	}
	
	//计算2个时间的间隔
	public  long getDayDiff(Date startDate, Date endDate,int diffValue){
		long t1 = startDate.getTime();
		long t2 = endDate.getTime();
		long count = (t2 - t1) /diffValue;
		return Math.abs(count);
	}
	
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public ServletContext getServletContext() {
		return ServletActionContext.getServletContext();
	}
	
	//页面调用返回url和seqStr 用于选择帐号
	public String excuteGetSysAccoutInfo(){
		InputObject inputObject = getInputObject();
		String mobile = inputObject.getParams().get("mobile");
		String sysType = inputObject.getParams().get("sysType");
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		List<Map<String, String>> rtnBeansList = null;
		if(StringUtil.isEmpty(sysType)){
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "参数为空");
			sendJson(JsonUtil.convertObject2Json(gridMp),true);
			return null;
		}
		if (!StringUtil.isEmpty(mobile)&&mobile.trim().length()==11) {
			inputObject.getParams().put("needSeq", "1");
			OutputObject object = getOutputObject(inputObject);
			if(!ControlConstants.RTNCODE_SUC.equals(object.getReturnCode())){
				gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
				gridMp.put("rtnMsg", object.getReturnMessage());
				sendJson(JsonUtil.convertObject2Json(gridMp),true);
			}
			rtnBeansList = object.getBeans();
			String seqStr = null;
			if (rtnBeansList != null&&rtnBeansList.size() != 0) {
				Iterator<Map<String, String>> itMap=rtnBeansList.iterator();
				while(itMap.hasNext()){
					Map<String, String> tmpMap=itMap.next();
					if (tmpMap.get("edu_serial") != null) {
						seqStr = tmpMap.get("edu_serial");
						itMap.remove();
						break;
					}
				}
			}
			if(StringUtil.isEmpty(seqStr)){
				gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
				gridMp.put("rtnMsg", "获取[seq]失败");
				sendJson(JsonUtil.convertObject2Json(gridMp),true);
			}
			String tencryptedKey = SystemPropUtil.getString("caller.tencryptedKey");//同步课堂
			String zencryptedKey = SystemPropUtil.getString("caller.zencryptedKey");//学习报
			String xxbHttpurl=SystemPropUtil.getString("caller.xxbHttpurl");//学习报url
			String tbkHttpurl=SystemPropUtil.getString("caller.tbkHttpurl");//同步课堂url
			CodeUtil codeUtil = new CodeUtil();
			String xxbseqMobile =null;
			String  tbkseqMobile =null;

			codeUtil.setEncryptedKey(zencryptedKey);
			xxbseqMobile = codeUtil.getParamData(seqStr, mobile);

			codeUtil.setEncryptedKey(tencryptedKey);
			tbkseqMobile = codeUtil.getParamData1(seqStr, mobile);
			gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
			gridMp.put("rtnMsg", "call success");
			gridMp.put("bindInfo", rtnBeansList);
			gridMp.put("xxb_url", xxbHttpurl);
			gridMp.put("xxb_seq", xxbseqMobile);
			gridMp.put("tbk_url", tbkHttpurl);
			gridMp.put("tbk_seq", tbkseqMobile);
		}else{
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "参数[手机号码]错误");
		}
		sendJson(JsonUtil.convertObject2Json(gridMp),true);
		return null;
	}
	
    //角色切换:调crm接口获取角色信息
	public String excuteGetXXTAccountInfo() {
		InputObject inputObject = getInputObject();
		Map<String, String> inputMap = inputObject.getParams();
		logger.info(JsonUtil.convertObject2Json(inputMap), "excuteGetXXTAccountInfo's params:{}");
		String userId = inputMap.get("userId");
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		// 学生信息
		List<Map<String, String>> studenList = new ArrayList<Map<String, String>>();
		// 老师信息
		List<Map<String, String>> teaList = new ArrayList<Map<String, String>>();
		if (!StringUtil.isEmpty(userId)) {
			OutputObject object = getOutputObject(inputObject);
			if (!ControlConstants.RTNCODE_SUC.equals(object.getReturnCode())) {
				if(object.getReturnMessage()!=null&&(object.getReturnMessage().indexOf("无记录")>-1||object.getReturnMessage().indexOf("不存在")>-1)){
					HttpSession session = getRequest().getSession(false);
					if(session!=null){
						User loginUser = (User) session.getAttribute(User.LOGIN_USER);
						session.setAttribute(User.CURRENT_USER, loginUser);
					}
					gridMp.put("parents", studenList);
					gridMp.put("teacher", teaList);
					gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
					gridMp.put("rtnMsg", "call success");
					sendJson(JsonUtil.convertObject2Json(gridMp), true);
					return null;
				}
				gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
				gridMp.put("rtnMsg", object.getReturnMessage());
				sendJson(JsonUtil.convertObject2Json(gridMp), true);
				return null;
			}
			List<Map<String, String>> userList = object.getBeans();
			//无角色信息
			if(userList.size()==0){
				HttpSession session = getRequest().getSession(false);
				if(session!=null){
					User loginUser = (User) session.getAttribute(User.LOGIN_USER);
					session.setAttribute(User.CURRENT_USER, loginUser);
				}
				gridMp.put("parents", studenList);
				gridMp.put("teacher", teaList);
				gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
				gridMp.put("rtnMsg", "call success");
				sendJson(JsonUtil.convertObject2Json(gridMp), true);
				return null;
			}
			//2015-5-11日 删除家长信息
			Map<String,User> parentUser  = new HashMap<String,User>();
			if(userList!=null){
				Iterator<Map<String, String>> it = userList.iterator();
				while(it.hasNext()){
					Map<String, String> map=it.next();
					if(map.get("userTp").equals(String.valueOf(Constants.USER_TYPE.PARENT))){
						User parentU = User.convertCrmLoginUser(map);
						if(parentU!=null){
							parentUser.put(Constants.CRM_USER_TYPE.PARENT, parentU);
						}
						
						it.remove();
					}
				}
			}
			//保家长信息存到session
			HttpSession loginsession = getRequest().getSession(false);
			if(parentUser.size()>0){
				loginsession.setAttribute(User.PARENTLOGINU, parentUser);
			}
			
			List<Map<String, String>> userUrlList=JsonUtil.convertJson2Object(object.getBusiCode(), List.class);
			// 2015年2月份添加:若只有一个角色一个用户不选角色直接跳转
			if (userList.size() == 1) {
				Map<String, String> map = userList.get(0);
				HttpSession session = getRequest().getSession(false);
				User user = null,loginUser=null;
				if (session != null) {
					loginUser = (User) session.getAttribute(User.LOGIN_USER);
					if(loginUser==null){
						gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
						gridMp.put("rtnMsg", "请先登录");
						sendJson(JsonUtil.convertObject2Json(gridMp), true);
						return null;
					}
					user = (User) session.getAttribute(User.CURRENT_USER);
					// 保存用户信息
					if (user == null) {
						user = new User();
						//为保持用户头像的连续使用,userId取xxtId而不是crm侧userId
						user.setUserId(StringUtil.trim(map.get("xxtuid")));
						user.setSex(StringUtil.str2Int(map.get("sex")));
						user.setOrgId(StringUtil.str2Long(map.get("schoolId")));
						user.setMobile(loginUser.getMobile());
						//crm 1:老师 2:学生 3:家长
						//本地 1:学生 2:老师 3:家长
						if (Constants.CRM_USER_TYPE.TEACHER.equals(map.get("userTp"))) {
							user.setTypeid(2);
							user.setUserIcon(getStdImgUrl(map.get("xxtuid"),userUrlList));
						} else {
							//学生 家长数据在一个里面，没有区分
							user.setTypeid(1);
							user.setUserIcon(getTeaImgUrl(map.get("userId"),userUrlList));
						}
						user.setOtherUserId(StringUtil.str2Long(map.get("xxtuid")));
						user.setSysType(1);
						user.setUserName(map.get("userNm"));
						user.setIsMulRole(-1);
						session.setAttribute(User.CURRENT_USER, user);
					}
					List<User> userAllList = (List<User>) session.getAttribute(User.USER_LIST);
					if (userAllList == null || userAllList.size() == 0) {
						userAllList = new ArrayList<User>();
						for (Map<String, String> urlMap : userUrlList) {
							if (!"100".equals(urlMap.get("SYSTYPE"))) {
								User otherUser = new User();
								otherUser.setUserId(StringUtil.trim(urlMap.get("USERID")));
								otherUser.setSysType(StringUtil.str2Int(urlMap.get("SYSTYPE")));
								otherUser.setOtherUserId(StringUtil.str2Long(urlMap.get("OTHERUSERID")));
								otherUser.setTypeid(StringUtil.str2Int(urlMap.get("USERTYPE")));
								otherUser.setUserName(urlMap.get("OTHERUSERNAME"));
								userAllList.add(otherUser);
							}
						}
						userAllList.add(user);// 校讯通用户
						session.setAttribute(User.USER_LIST, userAllList);
					}
				}
				// 页面直接跳转
				gridMp.put("isRedirect", "1");
			}else{
				for (Map<String, String> map : userList) {
					Map<String, String> rtnMap =getCrmUserMap(map, userUrlList);
					if(Constants.CRM_USER_TYPE.TEACHER.equals(map.get("userTp"))){
						teaList.add(rtnMap);
					}else{
						studenList.add(rtnMap);
					}
				}
			}
			gridMp.put("parents", studenList);
			gridMp.put("teacher", teaList);
			gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
			gridMp.put("rtnMsg", "call success");
		} else {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "参数[用户ID]错误");
		}
		sendJson(JsonUtil.convertObject2Json(gridMp), true);
		return null;
	}
	
	public Map<String, String> getCrmUserMap(Map<String, String> map,List<Map<String, String>> userUrlList) {
		Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("userId", map.get("xxtuid"));
		rtnMap.put("userName", map.get("userNm"));
		rtnMap.put("city", map.get("area"));
		rtnMap.put("region", map.get("subArea"));
		rtnMap.put("schoolId", map.get("schoolId"));
		rtnMap.put("schoolName", map.get("schoolNm"));
		rtnMap.put("grade", map.get("gradeNm"));
		rtnMap.put("sex", map.get("sex"));// 1男 2女
		// 接口1代表老师 1老师 2学生 3家长
		if ("1".equals(map.get("userTp"))) {
			rtnMap.put("typeId", "2");
			rtnMap.put("imgUrl", getTeaImgUrl(map.get("xxtuid"), userUrlList));
		} else {
			rtnMap.put("typeId", "1");
			rtnMap.put("imgUrl", getStdImgUrl(map.get("xxtuid"), userUrlList));
		}
		return rtnMap;
	}
	
	public Map<String, String> getStdMapInfo(RemoteRespHelper remoteHelper,List<Map<String, String>> userUrlList, int i) {
		Map<String, String> stuMap = new HashMap<String, String>();
		String userId = String.valueOf(remoteHelper.getStdInfo("USERID", i));
		stuMap.put("userId", userId);
		stuMap.put("userName", remoteHelper.getStdInfo("USERNAME", i));
		stuMap.put("city", remoteHelper.getStdInfo("AREA", i));
		stuMap.put("region", remoteHelper.getStdInfo("SUB_AREA", i));
		stuMap.put("schoolName", remoteHelper.getStdInfo("SCHOOL", i));
		stuMap.put("grade", remoteHelper.getStdInfo("GRADE", i));
		if (userUrlList.size() == 0) {
			stuMap.put("imgUrl", "../theme/images/0.png");
		} else {
			stuMap.put("imgUrl", getStdImgUrl(userId, userUrlList));
		}
		return stuMap;
	}
	
	public String getStdImgUrl(String userId,List<Map<String, String>> userUrlList) {
		// 得到用户选择的头像
		String userIcon = "../theme/images/0.png";
		if(userUrlList!=null){
			for (Map<String, String> iconMap : userUrlList) {
				// 学生或者家长
				if ("100".equals(iconMap.get("SYSTYPE"))&&!"2".equals(iconMap.get("USERTYPE"))) {
					if (userId.equals(iconMap.get("USERID"))) {
						userIcon =convertUserImg(iconMap.get("OTHERUSERID"));
						break;
					}
				}
			}
		}
		return userIcon;
	}
	
	public Map<String, String> getTeaMapInfo(RemoteRespHelper remoteHelper,List<Map<String, String>> userUrlList, int i) {
		Map<String, String> teaMap = new HashMap<String, String>();
		String userId = String.valueOf(remoteHelper.getTeaInfo("USERID", i));
		teaMap.put("userId", userId);
		teaMap.put("userName", remoteHelper.getTeaInfo("USERNAME", i));
		teaMap.put("city", remoteHelper.getTeaInfo("AREA", i));
		teaMap.put("region", remoteHelper.getTeaInfo("SUB_AREA", i));
		teaMap.put("schoolName", remoteHelper.getTeaInfo("SCHOOL", i));
		if (userUrlList.size() == 0) {
			teaMap.put("imgUrl", "../theme/images/0.png");
		} else {
			teaMap.put("imgUrl", getTeaImgUrl(userId, userUrlList));
		}
		return teaMap;
	}
	
	//图像路径
	public String convertUserImg(String userIcon){
		Map<String,String> userIconMap=new HashMap<String, String>();
		userIconMap.put("1", "../theme/images/1.png");
		userIconMap.put("2", "../theme/images/2.png");
		userIconMap.put("3", "../theme/images/3.png");
		userIconMap.put("4", "../theme/images/4.png");
		userIconMap.put("5", "../theme/images/5.png");
		userIconMap.put("6", "../theme/images/6.png");
		userIconMap.put("7", "../theme/images/7.png");
		userIconMap.put("8", "../theme/images/8.png");
		userIconMap.put("9", "../theme/images/9.png");
		String iconImg= userIconMap.get(userIcon);
		if(StringUtil.isNull(iconImg)){
	           //默认头像
				iconImg="../theme/images/0.png";
		}
		return iconImg;
	}
	
	public String getTeaImgUrl(String userId,List<Map<String, String>> userUrlList) {
		// 得到用户选择的头像
		String userIcon = "../theme/images/0.png";
		if(userUrlList!=null){
			for (Map<String, String> iconMap : userUrlList) {
				// 老师
				if ("100".equals(iconMap.get("SYSTYPE"))&&"2".equals(iconMap.get("USERTYPE"))) {
					if (userId.equals(iconMap.get("USERID"))) {
						userIcon =convertUserImg( iconMap.get("OTHERUSERID"));
						break;
					}
				}
			}
		}
		return userIcon;
	}
	
	//保存用户选择的图片
	public String excuteSaveUserIcon(){
		InputObject inputObject = getInputObject();
		String mobile = inputObject.getParams().get("mobile");
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		if (StringUtil.isEmpty(mobile)||mobile.trim().length()!= 11) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "请先登录");
		}else{
			OutputObject outputObject=getOutputObject();
			if(ControlConstants.RTNCODE_SUC.equals(outputObject.getReturnCode())){
				//修改当前用户的头像
				HttpSession session=getSession(false);
				User user=null;
				if(session!=null){
					user=(User) session.getAttribute(User.CURRENT_USER);
					user.setUserIcon(convertUserImg(inputObject.getParams().get("userIcon")));
					session.setAttribute(User.CURRENT_USER, user);
				}	
				gridMp.put("rtnCode", ControlConstants.RTNCODE_SUC);
				gridMp.put("rtnMsg", "call success");
			}else{
				gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
				gridMp.put("rtnMsg", outputObject.getReturnMessage());
			}
		}
		sendJson(JsonUtil.convertObject2Json(gridMp), true);
		return null;
	}
	
	private static int width = 68; 
    private static int height = 32; 
	public String excuteRandomCode() {
		HttpServletResponse response = getResponse();
		HttpServletRequest request = getRequest();
//		try {
//		logger.info("", "create VertifyCode start......");
////		String filePath = CmsServiceClient.EDU_APP_BASEDIR + "/test.png" ;
////		BufferedImage img = Imaging.getBufferedImage(new File(filePath));
//		
////		ImageBuilder builder  = new ImageBuilder(width, height, true);
////		BufferedImage img = builder.getBufferedImage();
////		ImageFormat format = ImageFormats.PNG;
////		Map<String, Object> optionalParams = new HashMap<String, Object>();
//		BufferedImage img = new BufferedImage(width, height,
//				BufferedImage.TYPE_INT_ARGB);
//		logger.info("BufferedImage", "create {} success......");
//		
//		// 得到该图片的绘图对象
//		Graphics g = img.createGraphics();
//		
//		logger.info("Graphics", "create {} success......");
//		Random r = new Random();
//		// 设定背景色
////		g.setColor(getRandColor(200, 250)); 
//		g.setColor(new Color(255,255,255,255)); 
//		// 设定字体 
//        //g.setFont(new Font("楷体", Font.BOLD, 22));  
//        g.setFont(new Font(g.getFont().getFontName(), Font.BOLD | Font.ITALIC, 22));
//        logger.info("Font", "create {} success......");
//		
//        // 填充整个图片的颜色
//		g.fillRect(0, 0, width, height);
//		Random random = new Random(); 
//        // 随机产生50条干扰线，使图象中的认证码不易被其它程序探测到  
//        g.setColor(getRandColor(160, 200)); 
//        for (int i = 0; i < 50; i++){ 
//            int x = random.nextInt(width); 
//            int y = random.nextInt(height); 
//            int xl = random.nextInt(12); 
//            int yl = random.nextInt(12); 
//            g.drawLine(x, y, x + xl, y + yl); 
//        } 
//	        
//		// 向图片中输出数字和字母
//		StringBuffer sb = new StringBuffer();
////		ABCDEFGHIJKLMNOPQRSTUVWXYZ
//		char[] ch = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
//		int index, len = ch.length;
//		for (int i = 0; i < 4; i++) {
//			index = r.nextInt(len);
////			g.setColor(new Color(255,171,82));
//			g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
//			g.drawString("" + ch[index], (i * 15) + 3, 26);// 写什么数字，在图片 的什么位置画
//			sb.append(ch[index]);
//		}
//		g.dispose();
//		logger.info("VertifyCode", "create {} success......");
//		
//		request.getSession().setAttribute(Constants.IMG_CODE.AuthCode, sb.toString());
//		request.getSession().setAttribute(Constants.IMG_CODE.AuthCodeTime, Calendar.getInstance().getTime());
//		response.setContentType("image/jpeg");
//		
////		Imaging.writeImage(img, response.getOutputStream(), format, optionalParams);
//			ImageIO.write(img, "JPG", response.getOutputStream());
////			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
////			encoder.encode(img);
//			
//			logger.info("VertifyCode", "output {} success......");
//		} catch (Exception e) {
//			logger.error("output {} error", e, "VertifyCode");
//		} 
		
		
		
		int width = 68;
		int height= 32;
		
		char[] ch = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		int index, len = ch.length;
		Random random = new Random();
		String[] lettles = new String[4];
		
		String prefix    = "randomCode/";
		StringBuffer randomcode = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			index = random.nextInt(len);
			char lettle = ch[index];
			lettles[i]  = prefix + lettle + ".png";
			randomcode.append(lettle);
		}
		
		BufferedImage imageNew = new BufferedImage(width, height,  BufferedImage.TYPE_INT_RGB);
		int[]  whitebgArray    = readImageArray(prefix + "bgpng.png",18,6);
		int[]  rightbgArray    = readImageArray(prefix + "rtbgpng.png",2,32);
		int i = 0;
		for(String lettle:lettles){
			int[]  lettleArray    = readImageArray(lettle,18,26);
			imageNew.setRGB(i*18 - i*2, 0, 18, 26, lettleArray, 0, 18);
			
			imageNew.setRGB(i*18 - i*2, 26, 18, 6, whitebgArray, 0, 18);
			i++;
		}
		imageNew.setRGB(66, 0, 2, 32, rightbgArray, 0, 2);
		
		try {
			ImageIO.write(imageNew,  "png",  response.getOutputStream());//写图片
			
//			request.getSession().setAttribute("captchaToken", randomcode.toString());
			request.getSession().setAttribute(Constants.IMG_CODE.AuthCode, randomcode.toString());
			request.getSession().setAttribute(Constants.IMG_CODE.AuthCodeTime, Calendar.getInstance().getTime());
			
			logger.info("create random code[{}] success.", null, randomcode.toString());
		} catch (IOException e) {
			logger.error("create random code[{}] error:", e, randomcode.toString());
		}
		
		
//		String uid = StringUtil.getUUID();
//		uid = uid + Calendar.getInstance().getTime().getTime();
//		
//		request.getSession().setAttribute(Constants.IMG_CODE.AuthCode, uid);
//		request.getSession().setAttribute(Constants.IMG_CODE.AuthCodeTime, Calendar.getInstance().getTime());
//		
//		Map<String,String> out = new HashMap<String, String>();
//		out.put("rtnCode",ControlConstants.RTNCODE_SUC);
//		out.put("rtnMsg",uid);
//		sendJson(JsonUtil.convertObject2Json(out), true);
		
		return null;
	}
	// 从图片中读取RGB
	public int[] readImageArray(String name, int width, int height) {
		InputStream in = null;
		try {
			in = this.getClass().getClassLoader().getResourceAsStream(name);
			BufferedImage imageOne = ImageIO.read(in);

			// 从图片中读取RGB
			int[] imageArrayOne = new int[width * height];
			imageArrayOne = imageOne.getRGB(0, 0, width, height, imageArrayOne,
					0, width);

			return imageArrayOne;
		} catch (Exception e) {
			System.err.println("从图片中读取RGB失败：" + e.getMessage());
			return null;
		}finally{
			IOUtil.closeInputStream(in);
		}
	}
	private Color getRandColor(int fc, int bc) { 
        // 给定范围获得随机颜色 
        Random random = new Random(); 
        if (fc > 255) 
            fc = 255; 
        if (bc > 255) 
            bc = 255; 
        int r = fc + random.nextInt(bc - fc); 
        int g = fc + random.nextInt(bc - fc); 
        int b = fc + random.nextInt(bc - fc); 
        return new Color(r, g, b); 
    } 
	
	// 重置密码
	public String excuteEduResetPwd() {
		InputObject inputObject = getInputObject();
		String mobile = inputObject.getParams().get("mobile");
//		String rand = inputObject.getParams().get("rand");
//		String pwd = inputObject.getParams().get("pwd");
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		if (StringUtil.isEmpty(mobile) || mobile.trim().length() != 11) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "请输入正确的手机号码");
			sendJson(JsonUtil.convertObject2Json(gridMp), true);
			return null;
		}
//		if (StringUtil.isEmpty(rand)||StringUtil.isEmpty(pwd)) {
//			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
//			gridMp.put("rtnMsg", "参数错误");
//			sendJson(JsonUtil.convertObject2Json(gridMp), true);
//			return null;
//		}
		//校验验证码
//		HttpSession session = getRequest().getSession(false);
//		if (session == null) {
//			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
//			gridMp.put("rtnMsg", "请先获取验证码");
//			sendJson(JsonUtil.convertObject2Json(gridMp), true);
//			return null;
//		}
//		String randInfo=(String) session.getAttribute("R_Rand");
//		if(!StringUtil.isEmpty(randInfo)&&randInfo.indexOf("|")>-1){
//			String randStr=randInfo.split("\\|")[0];	
//			//不区分大小写
//			if(!rand.equalsIgnoreCase(randStr)){
//				gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
//				gridMp.put("rtnMsg", "验证码错误");
//				sendJson(JsonUtil.convertObject2Json(gridMp), true);
//				return null;
//			}
//		}else{
//			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
//			gridMp.put("rtnMsg", "请先获取验证码");
//			sendJson(JsonUtil.convertObject2Json(gridMp), true);
//			return null;
//		}
		//删除验证码缓存
//		session.removeAttribute("R_Rand");
//		SpringContextHelper.instance.getOscache().remove("Rand_" + mobile);
		OutputObject outObj = getOutputObject(inputObject);
		if (outObj == null|| (outObj.getReturnMessage()!=null&&outObj.getReturnMessage().indexOf("call ") != -1 && outObj.getReturnMessage().indexOf("error") != -1)) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "系统忙,重置密码失败");
			sendJson(JsonUtil.convertObject2Json(gridMp), true);
			return null;
		}
		gridMp.put("rtnCode", outObj.getReturnCode());
		gridMp.put("rtnMsg", outObj.getReturnMessage());
		sendJson(JsonUtil.convertObject2Json(gridMp), true);
		return null;
	}
	
	// 修改密码
	public String excuteEduChangPwd() {
		InputObject inputObject = getInputObject();
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		HttpSession session = getRequest().getSession(false);
		if(session==null){
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg","请先登录");
			sendJson(JsonUtil.convertObject2Json(gridMp), true);
			return null;
		}
		User user = (User) session.getAttribute(User.CURRENT_USER);
		if (user == null) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "请先登录");
			sendJson(JsonUtil.convertObject2Json(gridMp), true);
			return null;
		}
		String oldPwd = inputObject.getParams().get("oldPwd");
		String newPwd = inputObject.getParams().get("newPwd");
		if(StringUtil.isEmpty(oldPwd)||StringUtil.isEmpty(newPwd)){
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "参数错误");
			sendJson(JsonUtil.convertObject2Json(gridMp), true);
			return null;
		}
//		String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,}$";
//		if(!newPwd.matches(regex)){
//			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
//			gridMp.put("rtnMsg", "新密码必须包含数字和字母");
//			sendJson(JsonUtil.convertObject2Json(gridMp), true);
//			return null;
//		}
		OutputObject outObj = getOutputObject(inputObject);
		if (outObj == null|| (outObj.getReturnMessage()!=null&&outObj.getReturnMessage().indexOf("call ") != -1 && outObj.getReturnMessage().indexOf("error") != -1)) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "系统忙,修改密码失败");
			sendJson(JsonUtil.convertObject2Json(gridMp), true);
			return null;
		}
		gridMp.put("rtnCode", outObj.getReturnCode());
		gridMp.put("rtnMsg", outObj.getReturnMessage());
		sendJson(JsonUtil.convertObject2Json(gridMp), true);
		return null;
	}
	
	// 发送初始密码
	public String excuteSendSms() {
		InputObject inputObject = getInputObject();
		String mobile = inputObject.getParams().get("mobile");
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		if (StringUtil.isEmpty(mobile) || mobile.trim().length() != 11) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "请输入正确的手机号码");
			sendJson(JsonUtil.convertObject2Json(gridMp), true);
			return null;
		}
		int sendTimeDiff = StringUtil.str2Int(SystemPropUtil.getString("sms_send_time_diff"));
		String isCheckTime = SystemPropUtil.getString("is_check_sms_send_time");
		if (isCheckTime != null) {
			if ("y".equalsIgnoreCase(isCheckTime)|| "true".equalsIgnoreCase(isCheckTime)) {
				String lastSendTime = SpringContextHelper.instance.getOscache().get("FRand_" + mobile);
				//String timeStamp = DateUtil.date2String(new Date(),"yyyyMMddHHmmss");
				if (lastSendTime == null) {
					//SpringContextHelper.instance.getOscache().put("FRand_" + mobile, timeStamp);
				} else {
					int timeDiff = (int) getDayDiff(DateUtil.string2Date(	lastSendTime, "yyyyMMddHHmmss"), new Date(), 1000);
					if (timeDiff < sendTimeDiff) {
						gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
						gridMp.put("rtnMsg",String.format("短信发送间隔时间为%s秒", sendTimeDiff));
						sendJson(JsonUtil.convertObject2Json(gridMp), true);
						return null;
					} else {
						//SpringContextHelper.instance.getOscache().put("FRand_" + mobile, timeStamp);
					}
				}
			}
		}
		OutputObject outObj = getOutputObject(inputObject);
		
	    if(outObj==null||(outObj.getReturnMessage()!=null && outObj.getReturnMessage().indexOf("call ")!=-1&&outObj.getReturnMessage().indexOf("error")!=-1)){
	    	gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "系统忙暂时无法发送短信");
			sendJson(JsonUtil.convertObject2Json(gridMp), true);
			return null;
		}
		//发送出错
		if(ControlConstants.RTNCODE_ERR.equals(outObj.getReturnCode())){
			//SpringContextHelper.instance.getOscache().remove("FRand_" + mobile);
		}else{//发送成功
			String timeStamp = DateUtil.date2String(new Date(),DateUtil.YYYYMMDDHHMMSS);
			SpringContextHelper.instance.getOscache().put("FRand_" + mobile, timeStamp);
		}
		
		gridMp.put("rtnCode", outObj.getReturnCode());
		gridMp.put("rtnMsg", outObj.getReturnMessage());
		sendJson(JsonUtil.convertObject2Json(gridMp), true);
		return null;
	}
	
	// 检查是否是首次登录
	public String excuteCheckFirstLogin() {
		InputObject inputObject = getInputObject();
		String mobile = inputObject.getParams().get("mobile");
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		if (StringUtil.isEmpty(mobile) || mobile.trim().length() != 11) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "请输入正确的手机号码");
			sendJson(JsonUtil.convertObject2Json(gridMp), true);
			return null;
		}
		OutputObject outObj = getOutputObject(inputObject);
		gridMp.put("rtnCode", outObj.getReturnCode());
		gridMp.put("rtnMsg", outObj.getReturnMessage());
		sendJson(JsonUtil.convertObject2Json(gridMp), true);
		return null;
	}
	
	public String excuteEduBackLoginCleSession() {
		OutputObject outObj = new OutputObject();
		try {
			getRequest().getSession().removeAttribute(User.CURRENT_USER);
			getRequest().getSession().removeAttribute(User.LOGIN_USER);
			getRequest().getSession().removeAttribute(User.LOGINTOKEN);
			getRequest().getSession().removeAttribute(User.TMPLOGINU);
			getRequest().getSession().removeAttribute(User.USER_LIST);
			outObj.setReturnCode(ControlConstants.RTNCODE_SUC);
			outObj.setReturnMessage("修改密码清楚session成功！");
		} catch (Exception e) {
			outObj.setReturnCode(ControlConstants.RTNCODE_ERR);
			outObj.setReturnMessage("删除session失败！");
		}
		String json = JsonUtil.convertObject2Json(outObj);
		json=json.replaceAll("returnCode", "rtnCode");
		json=json.replaceAll("returnMessage", "rtnMsg");
		sendJson(json, true);
		return null;
	}
	
	// 检查用户密码是否正确 
	public String excuteCheckPwdRt() {
		InputObject inputObject = getInputObject();
		String mobile = inputObject.getParams().get("mobile");
		HashMap<String, Object> gridMp = new HashMap<String, Object>();
		if (StringUtil.isEmpty(mobile) || mobile.trim().length() != 11) {
			gridMp.put("rtnCode", ControlConstants.RTNCODE_ERR);
			gridMp.put("rtnMsg", "请输入正确的手机号码");
			sendJson(JsonUtil.convertObject2Json(gridMp), true);
			return null;
		}
		OutputObject outObj = getOutputObject(inputObject);
		gridMp.put("rtnCode", outObj.getReturnCode());
		gridMp.put("rtnMsg", outObj.getReturnMessage());
		sendJson(JsonUtil.convertObject2Json(gridMp), true);
		return null;
	}
	
}
