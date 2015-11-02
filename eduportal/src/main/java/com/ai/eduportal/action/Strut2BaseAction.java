package com.ai.eduportal.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.ai.eduportal.Util.DateUtilBack;
import com.ai.eduportal.bean.User;
import com.ai.eduportal.service.CmsServiceClient;
import com.ai.frame.bean.InputObject;
import com.ai.frame.bean.OutputObject;
import com.ai.frame.util.ControlConstants;
import com.ai.frame.util.SpringContextHelper;
import com.ai.frame.web.action.BaseAction;
import com.ai.frame.web.util.SystemPropUtil;
import common.ai.encryption.tools.EncryptionUtil;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;
import common.ai.tools.DateUtil;
import common.ai.tools.IOUtil;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

/**
 * 防strut2的ActionSupport
 * @author yiyun
 *
 */
public class Strut2BaseAction extends BaseAction {
	public static final String ERROR = "error";
	private CmsServiceClient cmsService;
	private Logger logger = LoggerFactory.getActionLog(this.getClass());
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public ServletContext getServletContext() {
		return ServletActionContext.getServletContext();
	}
	
	/**前台base64编码字符串解码*/
	private String decodeBase64(String base64Str){
		String decodeStr = EncryptionUtil.decryptBase64(base64Str);
		try {
			decodeStr = URLDecoder.decode(decodeStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("{}.URLDecoder error:{}", e,"decodeBase64");
		}
		return decodeStr;
	}

	public String excuteLogout(){
//		User user = (User)getRequest().getSession().getAttribute(User.LOGIN_USER);
//		try{
//			
//			MyService.truncateSsoCookie2(getRequest(), getResponse());
//
//		}catch(Throwable e){}
		getRequest().getSession().removeAttribute(User.LOGIN_USER);
		getRequest().getSession().removeAttribute(User.CURRENT_USER);
		getRequest().getSession().removeAttribute(User.USER_LIST);
		getRequest().getSession().removeAttribute(User.LOGINTOKEN);
		getRequest().getSession().removeAttribute(User.TMPLOGINU);
		
		getRequest().getSession().invalidate();
		
//		OutputObject outObj = new OutputObject();
//		if(user!=null){
//			outObj.getBean().put("phoneNum", String.valueOf(user.getMobile()));
//		}
//		outObj.setReturnCode(ControlConstants.RTNCODE_SUC);
		
		//sendJson(convertOutputObject2Json(outObj),true);
		
		//return null;
		
		//删除session之后，调转统一退出地址
		String logOut = "logOut";
		return logOut;
	}
	/**购物车结算页面调用*/
	@SuppressWarnings("unchecked")
	public String excuteGetCarCache(){
		InputObject inputObject = getInputObject();
		String orderInfos = inputObject.getParams().get("prodInfo");
		orderInfos = decodeBase64(orderInfos);
		List<Map<String,String>> orderInfoLs = JsonUtil.convertJson2Object(orderInfos, List.class);
		if(orderInfoLs!=null){
			for(Map<String,String> orderInfoMap:orderInfoLs){
				String appPic = cmsService.getPicUrl("appID",orderInfoMap.get("appID"));
				orderInfoMap.put("appPic", appPic);
				String appName = cmsService.getAppName("appID",orderInfoMap.get("appID"));
				orderInfoMap.put("appName", appName);
				//取得帮助信息
				orderInfoMap.putAll(cmsService.getContactByAppId(orderInfoMap.get("appID")));
			}
		}
		
		OutputObject outObj = new OutputObject();
		outObj.setReturnCode(ControlConstants.RTNCODE_SUC);
		outObj.getBeans().addAll(orderInfoLs);
		
		sendJson(convertOutputObject2Json(outObj),true);
		
		return null;
	}
	@SuppressWarnings("unchecked")
	public String excuteGetCache(){
		InputObject inputObject = getInputObject();
		String cacheKey = inputObject.getParams().get("cacheKey");
		String rtnstr = SpringContextHelper.instance.getOscache().get(cacheKey);
		Map<String,String> rtnMap = JsonUtil.convertJson2Object(rtnstr, Map.class);
//		Map params = DirectCache.getInstance().getVal(cacheKey);
		
//		Map<String,String> rtnMap = convertMap(params);
		String appPic = cmsService.getPicUrl("appID",rtnMap.get("appID"));
		rtnMap.put("appPic", appPic);
		String appName = cmsService.getAppName("appID",rtnMap.get("appID"));
		rtnMap.put("appName", appName);
		//取得帮助信息
		rtnMap.putAll(cmsService.getContactByAppId(rtnMap.get("appID")));
		
		OutputObject outObj = new OutputObject();
		outObj.setReturnCode(ControlConstants.RTNCODE_SUC);
		outObj.getBeans().add(rtnMap);
		
		sendJson(convertOutputObject2Json(outObj),true);
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String excuteGetCommonCache(){
		Map<String,String> rtnMap = null;
		String prev = null;
		InputObject inputObject = getInputObject();
		String cacheKey = inputObject.getParams().get("cacheKey");
		if(StringUtil.isEmpty(cacheKey)){
			
			cacheKey = (String)getRequest().getAttribute("cacheKey");
			String rtnstr = SpringContextHelper.instance.getOscache().get(cacheKey);
			rtnMap = JsonUtil.convertJson2Object(rtnstr, Map.class);
//			Map params = DirectCache.getInstance().getVal(cacheKey);
			
//			rtnMap = convertMap(params);
			prev = rtnMap.get("prev");
		}else{
			String rtnstr = SpringContextHelper.instance.getOscache().get(cacheKey);
			rtnMap = JsonUtil.convertJson2Object(rtnstr, Map.class);
//			Map params = DirectCache.getInstance().getVal(cacheKey);
			
//			rtnMap = convertMap(params);
			prev = rtnMap.get("prev");
		}
		
		OutputObject outObj = new OutputObject();
		outObj.getBean().put("prev", prev);
		outObj.setReturnCode(ControlConstants.RTNCODE_SUC);
		if(StringUtil.str2Int(prev) == 1){//购物车过来
			String orderInfos = rtnMap.get("prodInfo");
			orderInfos = decodeBase64(orderInfos);
			List<Map<String,String>> orderInfoLs = JsonUtil.convertJson2Object(orderInfos, List.class);
			if(orderInfoLs!=null){
				for(Map<String,String> orderInfoMap:orderInfoLs){
					String appPic = cmsService.getPicUrl("appID",orderInfoMap.get("appID"));
					orderInfoMap.put("appPic", appPic);
					String appName = cmsService.getAppName("appID",orderInfoMap.get("appID"));
					orderInfoMap.put("appName", appName);
					//取得帮助信息
					orderInfoMap.putAll(cmsService.getContactByAppId(orderInfoMap.get("appID")));
				}
			}
			
			outObj.getBeans().addAll(orderInfoLs);
		}else{//立即订购过来
			String appPic = cmsService.getPicUrl("appID",rtnMap.get("appID"));
			rtnMap.put("appPic", appPic);
			String appName = cmsService.getAppName("appID",rtnMap.get("appID"));
			rtnMap.put("appName", appName);
			//取得帮助信息
			rtnMap.putAll(cmsService.getContactByAppId(rtnMap.get("appID")));
			
			outObj.getBeans().add(rtnMap);
		}
		
//		String rtnJson = JsonUtil.convertObject2Json(outObj);
//		rtnJson = rtnJson.replace("returnCode", "rtnCode");
//		rtnJson = rtnJson.replace("returnMessage", "rtnMsg");
		
		sendJson(convertOutputObject2Json(outObj),true);
		
		return null;
	}
//	@SuppressWarnings("unchecked")
//	private Map<String,String> convertMap(Map params){
//		Map<String,String> rtnMap = new HashMap<String,String>();
//		Iterator keyIt = params.keySet().iterator();
//		while(keyIt.hasNext()){
//			String key = (String)keyIt.next();
//			rtnMap.put(key, getCacheVal(key,params));
//		}
//		return rtnMap;
//	}
//	@SuppressWarnings("unchecked")
//	private String getCacheVal(String field,Map cacheMap){
//		Object obj = cacheMap.get(field);
//		if(obj instanceof String[]){
//			String[] urls = (String[])obj;
//			return urls[0]; 
//		}else if (obj instanceof String){
//			return String.valueOf(obj); 
//		}
//		
//		return null;
//	}
	@SuppressWarnings("unchecked")
	public String imOrderGo(){
		InputObject inputObject = getInputObject();
		String userId = inputObject.getParams().get("userId");
		String goUrl  = inputObject.getParams().get("orderUrl");
		String appType= inputObject.getParams().get("appType");
		String sysType= inputObject.getParams().get("sysType");
		String nodeUid= inputObject.getParams().get("nodeUid");
		String cachePrefix = "imOrder";
		String fullPath = SystemPropUtil.getString("edu.full.path");
		if(!StringUtil.isEmpty(userId)){//已经登录
			if(StringUtil.str2Int(appType) == 1){//组合应用
				String cacheKey = cachePrefix + StringUtil.getUUID();
				SpringContextHelper.instance.getOscache().put(cacheKey, JsonUtil.convertObject2Json(inputObject.getParams()));
				
//				Map params = DirectCache.getInstance().getVal(cacheKey);
//				DirectCache.getInstance().put(cacheKey, inputObject.getParams());
				goUrl = "confirmBill.html";
				if(goUrl.indexOf("?")>-1){
					goUrl = goUrl + "&cacheKey="+cacheKey;
				}else{
					goUrl = goUrl + "?cacheKey="+cacheKey;
				}
				
				try {
					logger.info(fullPath+"module/"+goUrl, "go to system's url is :{}");
					getResponse().sendRedirect(fullPath+"module/"+goUrl);
//					getResponse().sendRedirect("../../module/"+goUrl);
				} catch (IOException e) {
					logger.error("sendRedirect to {} error:{}",e,goUrl);
				}
			}else{//单独应用
//				int stype = StringUtil.str2Int(sysType);
//				Object userRoles = getSession().getAttribute(User.USER_LIST);
//				
//				String sysRoleId = User.getSysTypeRole(userRoles,stype);
//				if(StringUtil.isEmpty(sysRoleId)){
//					sysRoleId = userId;
//				}
//				String appDetail = cmsService.getAppDetailOriginal(nodeUid);
//				Map<String,String> appDetailMp = JsonUtil.convertJson2Object(appDetail, Map.class);
//				if(appDetailMp!=null && appDetailMp.size()>0){
//					goUrl = appDetailMp.get("apphref");
//				}else{
//					goUrl = fullPath+"module/index.html";
//				}
//				
//				if(goUrl.indexOf("?")>-1){
//					goUrl = goUrl + "&roleId="+sysRoleId;
//				}else{
//					goUrl = goUrl + "?roleId="+sysRoleId;
//				}
				goUrl = fullPath + "edu_forward.jsp?nodeUid="+nodeUid+"&sysType="+sysType;
				logger.info(goUrl, "go to other system's url is :{}");
				try {
					getResponse().sendRedirect(goUrl);
				} catch (Exception e) {
					logger.error("sendRedirect to {} error:{}",e,goUrl);
				}
				
			}
			
		}else{//没有登录时点击后重新登录跳转过来
		}
		return null;
	}
	/**应用详细信息*/
	public String excuteAppDetail(){
		try{
			
			InputObject inputObject = getInputObject();
			String appId = inputObject.getParams().get("appId");
			
			Map<String,Object> appDetails = cmsService.getAppDetailMp(appId);
			
			String offerId         = StringUtil.obj2Str(appDetails.get("offerId"));
			String threeOfferCode  = StringUtil.obj2Str(appDetails.get("threeOfferCode"));
			String sixOfferCode    = StringUtil.obj2Str(appDetails.get("sixOfferCode"));
			String twelveOfferCode = StringUtil.obj2Str(appDetails.get("twelveOfferCode"));
			String threeSmsCode    = StringUtil.obj2Str(appDetails.get("threeSmsCode"));
			String sixSmsCode      = StringUtil.obj2Str(appDetails.get("sixSmsCode"));
			String twelveSmsCode   = StringUtil.obj2Str(appDetails.get("twelveSmsCode"));
			String isMutiApp       = StringUtil.obj2Str(appDetails.get("isMutiApp"));
			inputObject.getParams().put("offerId",        offerId);
			inputObject.getParams().put("threeOfferCode", threeOfferCode);
			inputObject.getParams().put("sixOfferCode",   sixOfferCode);
			inputObject.getParams().put("twelveOfferCode",twelveOfferCode);
			inputObject.getParams().put("threeSmsCode",   threeSmsCode);
			inputObject.getParams().put("sixSmsCode",     sixSmsCode);
			inputObject.getParams().put("twelveSmsCode",  twelveSmsCode);
			inputObject.getParams().put("isMutiApp",      isMutiApp);
			
			OutputObject object = getOutputObject(inputObject);
			
			appDetails.putAll(object.getBean());
//			appDetails.put("orderState", "0");
//			appDetails.put("roleState", "1");
			appDetails.put("rtnCode", "1");
			
			int roleid = 0;
			try {
				User user = (User) getSession().getAttribute(User.CURRENT_USER);
				roleid = user.getTypeid();
			} catch (Exception e) {
			}
			appDetails.put("rtnMsg", getDetailMsg(object.getBean(),StringUtil.obj2Str(appDetails.get("domain")),roleid));
			
			String sendJson = JsonUtil.convertObject2Json(appDetails);
			
			sendJson(sendJson,true);
		}catch(Exception e){
			sendErrorMsg(e.getMessage());
		}
		return null;
	}
	private String getDetailMsg(Map<String,String> details,String appdomains,int roleid){
		String msg = "";
		if(StringUtil.str2Int(details.get("orderState")) == 1){
			msg = "已订购产品无法再次订购！";
		}else if(roleid > 0 &&(StringUtil.isEmpty(appdomains) || appdomains.indexOf(String.valueOf(roleid))==-1)){
			msg = "您当前的角色无法订购该产品！";
		}else if(StringUtil.str2Int(details.get("roleState")) != 1){
			msg = "您没有权限订购该产品！";
		}
		return msg;
	}
	public void sendErrorMsg(String errMsg){
		Map<String,String> appDetails = new HashMap<String,String>();
		appDetails.put("rtnMsg", errMsg);
		appDetails.put("rtnCode", "0");
		String sendJson = JsonUtil.convertObject2Json(appDetails);
		
		sendJson(sendJson,true);
	}
	/**首页头部*/
	public String excuteHomeTop(){
		try{
			long start = System.currentTimeMillis();
			
			InputObject inputobj = new InputObject();
			inputobj.setService("crmCallerService");
			inputobj.setMethod("getUserOrders");
			User loginUser=(User) getSession(true).getAttribute(User.LOGIN_USER);//取登录用户手机号码
			List<Map<String,String>> beans = new ArrayList<Map<String,String>>();
			long mobile = 0L;
			List<Map<String, String>> userOrders = new ArrayList<Map<String,String>>();
			if(null != loginUser){
				mobile=loginUser.getMobile();
				inputobj.getParams().put("userId", String.valueOf(mobile));
				OutputObject userOrdersObject = getOutputObject(inputobj);
				userOrders = userOrdersObject.getBeans();
			}
			
			long end = System.currentTimeMillis();
			System.out.println("excuteHomeTop.1.used--------"+(end-start));
			start = System.currentTimeMillis();
			
			String baseDir = getServletContext().getRealPath("/");
			String homeTops = cmsService.getHomeTops(baseDir,beans);
			
			end = System.currentTimeMillis();
			System.out.println("excuteHomeTop.2.used--------"+(end-start));
			start = System.currentTimeMillis();
			
			//开始转换用户登录后是 app是否是已订购
			Map<String,Object> homeTopsMap = JsonUtil.convertJson2Object(homeTops, Map.class);
			List<Map<String,String>> adverpics = (List<Map<String, String>>) homeTopsMap.get("adverpics");//首页广告
			List<Map<String,String>> expresses = (List<Map<String, String>>) homeTopsMap.get("expresses");
			Map<String,List<Map<String,Object>>> secmenus = (Map<String, List<Map<String, Object>>>) homeTopsMap.get("secmenus");
			List<Map<String,Object>> teacher = (List<Map<String,Object>>) secmenus.get("teacher");
			List<Map<String,Object>> father = (List<Map<String,Object>>) secmenus.get("father");
			List<Map<String,Object>> actived = (List<Map<String,Object>>) secmenus.get("actived");
			
			Map<String,List<Map<String,String>>> subMenu = (Map<String, List<Map<String, String>>>) homeTopsMap.get("subMenu");
			
			addMenuIsOrderStateByUserOrders(userOrders,adverpics,expresses, teacher, father,actived, subMenu);
			
			end = System.currentTimeMillis();
			System.out.println("excuteHomeTop.3.used--------"+(end-start));
			
			secmenus.put("teacher", teacher);
			secmenus.put("father",  father);
			secmenus.put("actived", actived);
			homeTopsMap.put("secmenus",  secmenus);
			homeTopsMap.put("adverpics", adverpics);
			homeTopsMap.put("expresses", expresses);
//			homeTopsMap.put("rtnCode",   "1");
			homeTops = JsonUtil.convertObject2Json(homeTopsMap);
			
			sendJson(homeTops,true);
		}catch(Exception e){
			sendErrorMsg(e.getMessage());
			System.out.println(e);
		}
		return null;
	}
	
	public void addMenuIsOrderStateByUserOrders(List<Map<String, String>> userOrders,List<Map<String,String>> adverpics,
			List<Map<String,String>> expresses,List<Map<String,Object>> teacher,List<Map<String,Object>> father,
			List<Map<String,Object>> actived,Map<String,List<Map<String,String>>> subMenu){
		if(null != userOrders && userOrders.size() != 0){
			List<String> sList = new ArrayList<String>();
			for(Map<String,String> userOrder : userOrders){
				Date currentDate = new Date();
				String currentDateString = DateUtil.date2String(currentDate, "yyyy-MM-dd HH:mm:ss");
				String isExpire = userOrder.get("expireDate");
				if(DateUtilBack.dateCompare(currentDateString, isExpire)){
					sList.add(userOrder.get("appId"));
				}
			}
			addMenuIsOrderState(sList, adverpics);
			
			//开始反向解析secmenus	List<Map<String,Object>> teacher
			List<Map<String, String>> teacherList = subMenu.get("teacher");
			List<Map<String, String>> fatherList = subMenu.get("father");
			List<Map<String, String>> activedList = subMenu.get("actived");
			addMenuIsOrderState(sList, teacherList);
			addMenuIsOrderState(sList, fatherList);
			addMenuIsOrderState(sList, activedList);
			subMenu.put("teacher", teacherList);
			subMenu.put("father", fatherList);
			subMenu.put("actived", activedList);
			
			
			for(Map<String,Object> map : teacher){
				List<Map<String,String>> thirdMenus = (List<Map<String, String>>) map.get("thirdMenus");
				addMenuIsOrderState(sList, thirdMenus);
			}
			for(Map<String,Object> map : father){
				List<Map<String,String>> thirdMenus = (List<Map<String, String>>) map.get("thirdMenus");
				addMenuIsOrderState(sList, thirdMenus);
			}
			for(Map<String,Object> map : actived){
				List<Map<String,String>> thirdMenus = (List<Map<String, String>>) map.get("thirdMenus");
				addMenuIsOrderState(sList, thirdMenus);
			}
		}
	}
	public void addMenuIsOrderState(List<String> sList,List<Map<String,String>> mapOfList){
		for(String s : sList){
			for(Map<String,String> adver : mapOfList){
				if(!"".equals(adver.get("isMutiApp"))&&!adver.get("isMutiApp").equals("1")&&
						!"".equals(s)&&s.equals(adver.get("appID"))){
					adver.put("byOrdered","1");
					
				}
			}
		}
	}
	
	public String excuteClearAllCache(){
		cmsService.clearAllCache();
		
		String ip = StringUtil.getLocalHostIp();
        
		Map<String,String> appDetails = new HashMap<String,String>();
		appDetails.put("rtnMsg", "清空["+ip+"]缓存成功");
		appDetails.put("rtnCode", "1");
		String sendJson = JsonUtil.convertObject2Json(appDetails);
		
		sendJson(sendJson,true);
		
		return null;
	}
	/**首页热销和新应用*/
	public String excuteHomeBodyApps(){
		try{
			long start = System.currentTimeMillis();
			
			//xinheng
			InputObject inputObject = getInputObject();
			inputObject.getParams().put("startNum", "0");
			inputObject.getParams().put("endNum", "3");
			OutputObject object = getOutputObject(inputObject);
//			String userId = inputObject.getParams().get("userId");
			String mobile = inputObject.getParams().get("mobile");
			InputObject inputobj = new InputObject();
			inputobj.setService("crmCallerService");
			inputobj.setMethod("getUserOrders");
			inputobj.getParams().put("userId", mobile);
			
			OutputObject userOrders = getOutputObject(inputobj);
			
			long end = System.currentTimeMillis();
			System.out.println("excuteHomeBodyApps.1.userOrders--------"+(end-start));
			start = System.currentTimeMillis();
			
			if(userOrders.getBeans()!=null && userOrders.getBeans().size() != 0){
				List<Map<String, String>> rtnBeans = userOrders.getBeans();
				for(Map<String,String> map : rtnBeans){
					//判断是否已失效Start
					String expireDateString = map.get("expireDate").toString();
					Date expireDate = DateUtil.string2Date(expireDateString, "yyyy-MM-dd HH:mm:ss");
					Date currentTime = new Date();
					Calendar c1 = Calendar.getInstance();
					c1.setTime(expireDate);
					Calendar c2 = Calendar.getInstance();
					c2.setTime(currentTime);
					if (c1.before(c2)){
						map.put("isExpire", "1");//已失效
					}else{
						map.put("isExpire", "2");//没有失效
					}
					//判断是否已失效End
				}
			}
			end = System.currentTimeMillis();
			System.out.println("excuteHomeBodyApps.2.userOrders.isExpire--------"+(end-start));
			start = System.currentTimeMillis();
			
			String myTrack = inputObject.getParams().get("myTrack");
			
			String homeTops = cmsService.getAllHotNewApp(myTrack,object.getBeans(),userOrders.getBeans(),StringUtil.str2Long(mobile));
			
			end = System.currentTimeMillis();
			System.out.println("excuteHomeBodyApps.3.getAllHotNewApp--------"+(end-start));
			start = System.currentTimeMillis();
			
			
			Map<String,Object> rtn= new HashMap<String,Object>();
			Map<String,Object> homeTopsMap = JsonUtil.convertJson2Object(homeTops, Map.class);
			List<Map<String,String>> newApps = (List<Map<String, String>>) homeTopsMap.get("newApps");
			List<Map<String,String>> hotApps = (List<Map<String, String>>) homeTopsMap.get("hotApps");
			List<Map<String,String>> myTrackList = (List<Map<String, String>>) homeTopsMap.get("myTrack");
			List<Map<String,String>> userOrderAppsList = (List<Map<String, String>>) homeTopsMap.get("userOrderApps");
			List<Map<String,String>> appfavoriteList = (List<Map<String, String>>) homeTopsMap.get("appfavorite");
			if(null != userOrders && userOrders.getBeans().size() != 0){
				Map<String,Object> rtnmap = new HashMap<String,Object>();
				//把已订购的产品id放在一个List中
				List<String> sList = new ArrayList<String>();
				try{
				for(Map<String,String> userOrder : userOrders.getBeans()){
					Date currentDate = new Date();
					String currentDateString = DateUtil.date2String(currentDate, "yyyy-MM-dd HH:mm:ss");
					String isExpire = userOrder.get("expireDate");
					if(DateUtilBack.dateCompare(currentDateString, isExpire)){
						sList.add(userOrder.get("appId"));
					}
				}
				System.out.println("excuteHomeBodyApps.3.1.sList--------"+(end-start));
				start = System.currentTimeMillis();
				
				for(String s : sList){
					for(Map<String,String> hotAppMap : hotApps){
						//获取产品如果是单产品，比对其 产品id
						if(hotAppMap.containsKey("appID")&&hotAppMap.containsKey("isMutiApp")&&!"".equals(hotAppMap.get("isMutiApp"))&&!hotAppMap.get("isMutiApp").equals("1")&&
								!"".equals(s)&&s.equals(hotAppMap.get("appID"))){
							hotAppMap.put("byOrdered","1");
							}
						}
					for(Map<String,String> newAppMap : newApps){
						if(newAppMap.containsKey("appID")&&newAppMap.containsKey("isMutiApp")&&!"".equals(s)&&s.equals(newAppMap.get("appID"))
								&&!"".equals(newAppMap.get("isMutiApp"))&&!newAppMap.get("isMutiApp").equals("1")){
							newAppMap.put("byOrdered","1");
						}
					}
					for(Map<String,String> myTrackMap : myTrackList){
						if(myTrackMap.containsKey("appID")&&myTrackMap.containsKey("isMutiApp")&&!"".equals(s)&&s.equals(myTrackMap.get("appID"))
								&&!"".equals(myTrackMap.get("isMutiApp"))&&!myTrackMap.get("isMutiApp").equals("1")){
							myTrackMap.put("byOrdered","1");
						}
					}
					for(Map<String,String> userOrderAppsMap : userOrderAppsList){
						if(userOrderAppsMap.containsKey("appID")&&userOrderAppsMap.containsKey("isMutiApp")&&!"".equals(s)&&s.equals(userOrderAppsMap.get("appID"))
								&&!"".equals(userOrderAppsMap.get("isMutiApp"))&&!userOrderAppsMap.get("isMutiApp").equals("1")){
							userOrderAppsMap.put("byOrdered","1");
						}
					}
					for(Map<String,String> appfavoriteMap : appfavoriteList){
						if(appfavoriteMap.containsKey("appID")&&appfavoriteMap.containsKey("isMutiApp")&&!"".equals(s)&&s.equals(appfavoriteMap.get("appID"))
								&&!"".equals(appfavoriteMap.get("isMutiApp"))&&!appfavoriteMap.get("isMutiApp").equals("1")){
							appfavoriteMap.put("byOrdered","1");
						}
					}
				}
				}catch(Exception e){
					logger.error("----->{}add byordered error:{}", e, "");
				}
				end = System.currentTimeMillis();
				System.out.println("excuteHomeBodyApps.4.convert--------"+(end-start));
				
				homeTopsMap.put("newApps", hotApps);
				homeTopsMap.put("hotApps", newApps);
				homeTopsMap.put("myTrack", myTrackList);
				homeTopsMap.put("userOrderApps", userOrderAppsList);
				rtn.putAll(homeTopsMap);
				homeTops = JsonUtil.convertObject2Json(rtn);
			}
			
			sendJson(homeTops,true);
		}catch(Exception e){
			logger.error("----->{}excuteHomeBodyApps error:{}", e, "");
			sendErrorMsg(e.getMessage());
		}
		return null;
	}
	public String excuteHomeNewHotApp(){
		try{
			String homeTops = cmsService.getAllHotNewApp(null,null,null,0);
			
			Map<String,Object> rtn= new HashMap<String,Object>();
			Map<String,Object> homeTopsMap = JsonUtil.convertJson2Object(homeTops, Map.class);
			User loginUser=(User) getSession(true).getAttribute(User.LOGIN_USER);//取登录用户手机号码
			if(null != loginUser){
				long mobile=loginUser.getMobile();
				InputObject inputobj = new InputObject();
				inputobj.setService("crmCallerService");
				inputobj.setMethod("getUserOrders");
				inputobj.getParams().put("userId", String.valueOf(mobile));
				OutputObject userOrders = getOutputObject(inputobj);
				List<Map<String,String>> newApps = (List<Map<String, String>>) homeTopsMap.get("newApps");
				List<Map<String,String>> hotApps = (List<Map<String, String>>) homeTopsMap.get("hotApps");
				if(null != userOrders && userOrders.getBeans().size() != 0){
					Map<String,Object> rtnmap = new HashMap<String,Object>();
					//把已订购的产品id放在一个List中
					List<String> sList = new ArrayList<String>();
					for(Map<String,String> userOrder : userOrders.getBeans()){
						Date currentDate = new Date();
						String currentDateString = DateUtil.date2String(currentDate, "yyyy-MM-dd HH:mm:ss");
						String isExpire = userOrder.get("expireDate");
						if(DateUtilBack.dateCompare(currentDateString, isExpire)){
							sList.add(userOrder.get("appId"));
						}
					}
					for(String s : sList){
						for(Map<String,String> hotAppMap : hotApps){
							//获取产品如果是单产品，比对其 产品id
							if(!"".equals(hotAppMap.get("isMutiApp"))&&!hotAppMap.get("isMutiApp").equals("1")&&
									!"".equals(s)&&s.equals(hotAppMap.get("appID"))){
								hotAppMap.put("byOrdered","1");
								}
							}
						for(Map<String,String> newAppMap : newApps){
							if(!"".equals(s)&&s.equals(newAppMap.get("appID"))
									&&!"".equals(newAppMap.get("isMutiApp"))&&!newAppMap.get("isMutiApp").equals("1")){
								newAppMap.put("byOrdered","1");
							}
						}
					}
					homeTopsMap.put("newApps", hotApps);
					homeTopsMap.put("hotApps", newApps);
					rtn.putAll(homeTopsMap);
					homeTops = JsonUtil.convertObject2Json(rtn);
				}
			}
			sendJson(homeTops,true);
		}catch(Exception e){
			sendErrorMsg(e.getMessage());
		}
		return null;
	}
	/**所有应用*/
	public String excuteAllApps(){
		try{
			//xh
			InputObject inputobj = new InputObject();
			inputobj.setService("crmCallerService");
			inputobj.setMethod("getUserOrders");
			User loginUser=(User) getSession(true).getAttribute(User.LOGIN_USER);//取登录用户手机号码
			List<Map<String,String>> beans = new ArrayList<Map<String,String>>();
			
			String homeTops = cmsService.getAllApp();
			
			Map<String,Object> homeTopsMap = JsonUtil.convertJson2Object(homeTops, Map.class);
			List<Map<String,String>> rows = (List<Map<String, String>>) homeTopsMap.get("rows");
			List<Map<String,String>> hotkeywords = (List<Map<String, String>>) homeTopsMap.get("hotkeywords");
			//根据用户订单ordeList信息，与所有应用进行比对，如果已经订购状态加入byOrdered状态为1.
			if(null != loginUser){
				long mobile=loginUser.getMobile();
				inputobj.getParams().put("userId", String.valueOf(mobile));
				OutputObject userOrders = getOutputObject(inputobj);
				beans = userOrders.getBeans();
				if(null != beans && beans.size() != 0){
					//把已订购的产品id放在一个List中
					List<String> sList = new ArrayList<String>();
					for(Map<String,String> userOrder : beans){
						Date currentDate = new Date();
						String currentDateString = DateUtil.date2String(currentDate, "yyyy-MM-dd HH:mm:ss");
						String isExpire = userOrder.get("expireDate");
						if(DateUtilBack.dateCompare(currentDateString, isExpire)){
							sList.add(userOrder.get("appId"));
						}
					}
					for(String s : sList){
						for(Map<String,String> singMap : rows){
							if(!"".equals(singMap.get("isMutiApp"))&&!singMap.get("isMutiApp").equals("1")&&
									!"".equals(s)&&s.equals(singMap.get("appID"))){
								singMap.put("byOrdered","1");
							}
						}
					}
					for(String s : sList){
						for(Map<String,String> singMap : hotkeywords){
							if(!"".equals(singMap.get("isMutiApp"))&&!singMap.get("isMutiApp").equals("1")&&
									!"".equals(s)&&s.equals(singMap.get("appID"))){
								singMap.put("byOrdered","1");
							}
						}
					}
				}
				homeTopsMap.put("hotkeywords",  hotkeywords);
				homeTopsMap.put("rows", rows);
				homeTops = JsonUtil.convertObject2Json(homeTopsMap);
			}
			
			sendJson(homeTops,true);
		}catch(Exception e){
			sendErrorMsg(e.getMessage());
		}
		return null;
	}
	private String cacheKey;
	
	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public String forwordTo(){
//		InputObject inobj = getInputObject();
//		OutputObject out = getOutputObject();
		String forword = getRequest().getParameter("forword");
		String prev = getRequest().getParameter("prev");
		String ssoprev = getRequest().getParameter("ssoprev");
		if(!StringUtil.isEmpty(ssoprev)){
			String cacheKey = (String)getRequest().getAttribute("cacheKey");
			setCacheKey(cacheKey);
		}else
		if(!StringUtil.isEmpty(prev)){
			
			String cacheKey = (String)getRequest().getAttribute("cacheKey");
			setCacheKey(cacheKey);
		}
		return forword;
	}
	public void sendJson(String respJson,boolean isGzip){
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
	public String excute(){
		InputObject inputObject = getInputObject();
		//String inputStr = JsonUtil.convertObject2Json(inputObject.getParams());
		logger.info(JsonUtil.convertObject2Json(inputObject.getParams()), "excute 's param is :{}");
		OutputObject object = getOutputObject(inputObject);
		
		String callback = getRequest().getParameter("callback");
		String respJson = convertOutputObject2Json(object);
		
		if(StringUtil.isEmpty(callback)){
			sendJson(respJson,true);
		}else{
			sendJson(callback+"("+respJson+")");
		}
		
		return null;
	}

	public void setCmsService(CmsServiceClient cmsService) {
		this.cmsService = cmsService;
	}
	
	//我的购物车
	@SuppressWarnings("unchecked")
	public String excuteGetAllCartOrder() {
			try {
				//取cookie中保存的id
				Cookie[] cookies = getRequest().getCookies();
				Cookie productsCookie = null;
				if(cookies==null||cookies.length==0){
					return null;
				}
				for (Cookie cookie : cookies) {
					if ("prodIds".equals(cookie.getName())) {
						productsCookie = cookie;
						break;
					}
				}
				String prodIdsStr = URLDecoder.decode(productsCookie.getValue(),"UTF-8");
//				System.out.println("-------------=" + prodIdsStr);
				List<Map<String, String>> resultMapList = JsonUtil.convertJson2Object(prodIdsStr, List.class);
				if(resultMapList==null||resultMapList.size()==0){
					 return null;
				}
				StringBuffer productIdsStr=new StringBuffer();
				//拼接 组合包id
				for (Map<String, String> map : resultMapList) {
					if(!StringUtil.isEmpty((String) map.get("prodId"))){
						productIdsStr.append(",").append(map.get("prodId"));
					}
				}
				InputObject inputObject = getInputObject();
				if(productIdsStr.length()>0){
					inputObject.getParams().put("appCode",productIdsStr.substring(1));
				}
				//取价格
				OutputObject object = getOutputObject(inputObject);
				List<Map<String,String>> rtnBeans=object.getBeans();
				OutputObject returnObject=new OutputObject();
				//缓存取组合包信息
				Object obj = SpringContextHelper.instance.getOscache().get(CmsServiceClient.HOME_APPS_CACHE);
				
				List<Map<String,String>> allObj=null;
				if(obj != null){
					 allObj=(List<Map<String,String>>)obj;
				}
				if (rtnBeans==null||allObj == null) {
					returnObject.setReturnCode(ControlConstants.RTNCODE_ERR);
					returnObject.setReturnMessage("系统异常!");
					String json = JsonUtil.convertObject2Json(returnObject);
					sendJson(json,true);
					return null;
				}
				for (Map<String, String> map : resultMapList) {
					for (Map<String, String> map2 : allObj) {
	                     if(map.get("prodId").equals(map2.get("appID"))){
	                    	 map.put("appID", String.valueOf(map2.get("appID")));
	                    	 map.put("appName", map2.get("appName"));
	                    	 map.put("nodeUid", map2.get("nodeUid"));
	                    	 map.put("appPic", map2.get("appPic"));
	                    	 map.put("month", String.valueOf(map.get("month")));
	                    	 //cms 取价格信息
	                    	 map.put("threenormprice", String.valueOf(StringUtil.str2Double(map2.get("threenormprice"), 0)));
	                    	 map.put("threepromprice", String.valueOf(StringUtil.str2Double(map2.get("threepromprice"), 0)));
	                    	 map.put("sixnormprice", String.valueOf(StringUtil.str2Double(map2.get("sixnormprice"), 0)));
	                    	 map.put("sixpromprice", String.valueOf(StringUtil.str2Double(map2.get("sixpromprice"), 0)));
	                    	 map.put("twelvenormprice", String.valueOf(StringUtil.str2Double(map2.get("twelvenormprice"), 0)));
	                    	 map.put("twelvepromprice", String.valueOf(StringUtil.str2Double(map2.get("twelvepromprice"), 0)));
	                     }
					}
				}
				//取是否收藏信息
				for (Map<String, String> map : resultMapList) {
					for (Map<String, String> map3 : rtnBeans) {
	                     if(map.get("prodId").equals(map3.get("prodId"))){
	                    	 map.put("isCollect", String.valueOf(map3.get("isCollect")));
	                     }
					}
				}
				returnObject.setReturnCode(ControlConstants.RTNCODE_SUC);
				returnObject.setReturnMessage("call success");
				returnObject.setBeans(resultMapList);
				String json =JsonUtil.convertObject2Json(returnObject);
				json=json.replaceAll("returnCode", "rtnCode");
				json=new String(json.replaceAll("returnMessage", "rtnMsg"));
				sendJson(json,true);
			} catch (Exception e) {
				 sendErrorMsg(e.getMessage());
			}
			return null;
		}
		
		//提交订单(未登录跳至登录页面)
		public String verifyAllOrder(){
			InputObject inputObject = getInputObject();
			String userId = inputObject.getParams().get("userId");
			String userMobile= inputObject.getParams().get("mobile");
			String products = inputObject.getParams().get("prodInfo");
			OutputObject returnObject=new OutputObject();
			//判断是否登录或者是否有权限
			if(!StringUtil.isEmpty(userId)){
				returnObject= getOutputObject(inputObject);
			}else{
				returnObject.setReturnCode(ControlConstants.RTNCODE_ERR);
				returnObject.setReturnMessage("请先登录!");
			}
			if(StringUtil.isEmpty(products)){
				returnObject.setReturnCode(ControlConstants.RTNCODE_ERR);
				returnObject.setReturnMessage("确认订单信息出错");
			}
			//订单信息放入缓存
			if(ControlConstants.RTNCODE_SUC.equals(returnObject.getReturnCode())){
				String cacheKey=userMobile+"|"+userId;
				try {
					products = EncryptionUtil.decryptBase64(products);
					products = URLDecoder.decode(products, "utf-8");
					SpringContextHelper.instance.getOscache().put(cacheKey,products);
				} catch (Exception e) {
					sendErrorMsg(e.getMessage());
				}
			}else{
				if(!StringUtil.isEmpty(returnObject.getBusiCode())){
					List<Map<String,String>>  successMapList=null;
					List<Map<String, Object>>productsMaps=null;
					List<Map<String,Object>>  successObjectList=new ArrayList<Map<String,Object>>();
					List<Map<String,Object>>  failObjectMapList=new ArrayList<Map<String,Object>>();
					try {
						 successMapList=JsonUtil.convertJson2Object(returnObject.getBusiCode(), List.class);
						 products = EncryptionUtil.decryptBase64(products);
						 products = URLDecoder.decode(products, "utf-8");
						 productsMaps = JsonUtil.convertJson2Object(products, List.class);
						 if (productsMaps != null && productsMaps.size() > 0) {
							 //订购成功的数据
							 for(Map<String,String> retMap:successMapList){
							    for (Map<String, Object> map : productsMaps) {
									 if(retMap.get("appID").equals(map.get("appID"))&&retMap.get("month").equals(map.get("month"))){
										 successObjectList.add(map);
										 continue;
									 }
								 }
							  }
							 
							 //订购失败的数据
						 for (Map<String, Object> map : productsMaps) {
							 for (Map<String, String> retMap : successMapList) {
								if (retMap.get("appID").equals(map.get("appID"))&& retMap.get("month").equals(map.get("month"))&& retMap.get("appID").equals(map.get("appID"))) {
									continue;
								}else{
									failObjectMapList.add(map);
								}
							}
						 }
					 }
					String cacheKey=userMobile+"|"+userId;
					SpringContextHelper.instance.getOscache().put(cacheKey,JsonUtil.convertObject2Json(successObjectList));
					} catch (Exception e) {
						sendErrorMsg(e.getMessage());
					}
				}
			}
			String json =JsonUtil.convertObject2Json(returnObject);
			json=json.replaceAll("returnCode", "rtnCode");
			json=new String(json.replaceAll("returnMessage", "rtnMsg"));
			sendJson(json,true);
			return null;
		}
		
	//我的订单
//	@SuppressWarnings("unchecked")
//	public String excuteGetAllAllOrder() {
//		try {
//			InputObject inputObject = getInputObject();
//			// 取订购appId
//			OutputObject object = getOutputObject(inputObject);
//			OutputObject returnObject = new OutputObject();
//			if (ControlConstants.RTNCODE_ERR.equals(object.getReturnCode())) {
//				returnObject.setReturnCode(ControlConstants.RTNCODE_ERR);
//				returnObject.setReturnMessage("系统异常!"+ object.getReturnMessage());
//				String json = JsonUtil.convertObject2Json(returnObject);
//				sendJson(json, true);
//				return null;
//			}
//			List<Map<String, String>> rtnBeans = object.getBeans();
//			// 缓存取组合包信息
//			Object obj = SpringContextHelper.instance.getOscache().get(CmsServiceClient.HOME_APPS_CACHE);
//			List<Map<String, String>> allObj = null;
//			if (obj != null) {
//				allObj = (List<Map<String, String>>) obj;
//			}
//			if (rtnBeans == null || allObj == null) {
//				returnObject.setReturnCode(ControlConstants.RTNCODE_ERR);
//				returnObject.setReturnMessage("系统异常,获取缓存信息失败!");
//				String json = JsonUtil.convertObject2Json(returnObject);
//				sendJson(json, true);
//				return null;
//			}
//			List<Map<String, String>> cancelOrderList = new ArrayList<Map<String, String>>();
//			List<Map<String, String>> orderList = new ArrayList<Map<String, String>>();
//			for (Map<String, String> map : rtnBeans) {
//				for (Map<String, String> map2 : allObj) {
//					if (map.get("appID").equals(map2.get("appID"))) {
//						map.put("nodeUid", map2.get("nodeUid"));
//						map.put("appPic", map2.get("appPic"));
//						map.put("nodeUid", map2.get("nodeUid"));
//						// map.put("price", map2.get(map.get("priceText")));
//						// 已取消
//						if ("1".equals(map.get("isCancel"))) {
//							cancelOrderList.add(map);
//						} else {
//							orderList.add(map);
//						}
//					}
//				}
//			}
//			Map<String, Object> resultMap = new HashMap<String, Object>();
//			resultMap.put("unordered", cancelOrderList);
//			resultMap.put("nowOrder", orderList);
//			resultMap.put("rtnCode", ControlConstants.RTNCODE_SUC);
//			resultMap.put("rtnMsg", "call success");
//			String json = JsonUtil.convertObject2Json(resultMap);
//			sendJson(json, true);
//		} catch (Exception e) {
//			sendErrorMsg(e.getMessage());
//		}
//		return null;
//	}
		 //我的订单
		@SuppressWarnings("unchecked")
		public String excuteGetAllAllOrder() {
			try {
				InputObject inputObject = getInputObject();
				// 取订购appId
				OutputObject object = getOutputObject(inputObject);
				List<Map<String, String>> rtnBeans = object.getBeans();
				OutputObject returnObject = new OutputObject();
				// 缓存取组合包信息
				Object obj = SpringContextHelper.instance.getOscache().get(CmsServiceClient.CMS_APPS_CACHE);
				
				List<Map<String, String>> allObj = null;
				if (obj != null) {
					allObj = (List<Map<String, String>>) obj;
				}
				if (rtnBeans == null || allObj == null || ControlConstants.RTNCODE_ERR.equals(object.getReturnCode())) {
					returnObject.setReturnCode(ControlConstants.RTNCODE_ERR);
					returnObject.setReturnMessage("系统异常!");
					String json = JsonUtil.convertObject2Json(returnObject);
					sendJson(json, true);
					return null;
				}
				
				List<Map<String, String>> singleOrderList = new ArrayList<Map<String, String>>();
				List<Map<String, String>> multiOrderList = new ArrayList<Map<String, String>>();
				for (Map<String, String> map : rtnBeans) {
					for (Map<String, String> map2 : allObj) {
						//判断是否已失效Start
						String expireDateString = map.get("expireDate").toString();
						Date expireDate = DateUtil.string2Date(expireDateString, "yyyy-MM-dd HH:mm:ss");
						Date currentTime = new Date();
						Calendar c1 = Calendar.getInstance();
						c1.setTime(expireDate);
						Calendar c2 = Calendar.getInstance();
						c2.setTime(currentTime);
						if (c1.before(c2)){
							System.out.println("expireDate"+"         "+"currentTime"+"已失效      ================");
							map.put("isExpire", "1");//已失效
						}else{
							map.put("isExpire", "2");//没有失效
						}
						//判断是否已失效End
						//单产品
						if(!"1".equals(map2.get("isMutiApp"))){
							if(map.get("appId").equals(map2.get("appID"))){
								if(!StringUtil.isEmpty(map.get("priceName"))&&!StringUtil.isEmpty(map.get("priceFee"))){
									String appName = String.valueOf(map2.get("appName")) + "(" + map.get("priceName") + ")";
									map.put("appName", appName);
									map.put("appPrice", map.get("priceFee"));
								}else{
									map.put("appName", String.valueOf(map2.get("appName")));
									map.put("appPrice", String.valueOf(map2.get("packagePrice")));
								}
								
								
								map.put("nodeUid",  String.valueOf(map2.get("nodeUid")));
								map.put("appPic",   String.valueOf(map2.get("appPic")));
								
								map.put("apphref", String.valueOf(map2.get("apphref")));
								map.put("isExpire", map.get("isExpire"));
								map.put("isMutiApp", "2");
								map.put("sysType", String.valueOf(map2.get("sysType")));//用于跳转
								singleOrderList.add(map);
								continue;
							}
						}else{
							//组合产品
							int prodMonth = getProdMonth(map.get("appId"), map2);
							if (prodMonth > 0) {
								map.put("appName", map2.get("appName"));
								map.put("nodeUid", map2.get("nodeUid"));
								map.put("appPic",  map2.get("appPic"));
								map.put("month",   String.valueOf(prodMonth));
								map.put("appPrice",getProdPrice(prodMonth,map2));
								map.put("isExpire", map.get("isExpire"));
								map.put("isMutiApp", "1");
								multiOrderList.add(map);
							}
						}
					}
				}
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("singleOrder", singleOrderList);
				resultMap.put("multiOrder", multiOrderList);
				resultMap.put("rtnCode", ControlConstants.RTNCODE_SUC);
				resultMap.put("rtnMsg", "call success");
				String json = JsonUtil.convertObject2Json(resultMap);
				sendJson(json, true);
			} catch (Exception e) {
				sendErrorMsg(e.getMessage());
			}
			return null;
		}
		private String getProdPrice(int month,Map<String, String> map){
			if(month == 3){
				String price = map.get("threepromprice");
				if(StringUtil.isEmpty(price)){
					return StringUtil.trim(map.get("packagePrice"));
				}
				return StringUtil.trim(price);
			}else if(month == 6){
				String price = map.get("sixpromprice");
				return StringUtil.trim(price);
			}else if(month == 12){
				String price = map.get("twelvepromprice");
				return StringUtil.trim(price);
			}
			return "";
		}
		private int getProdMonth(String offerCode,Map<String, String> map){
			String threeOfferCode  = StringUtil.trim(map.get("threeOfferCode"));
			String sixOfferCode    = StringUtil.trim(map.get("sixOfferCode"));
			String twelveOfferCode = StringUtil.trim(map.get("twelveOfferCode"));
			offerCode = StringUtil.trim(offerCode);
			if(StringUtil.isEmpty(offerCode))return 0;
			if(threeOfferCode.equals(offerCode)){
				return 3;
			}else if(sixOfferCode.equals(offerCode)){
				return 6;
			}else if(twelveOfferCode.equals(offerCode)){
				return 12;
			}else{
				return 0;
			}
		}	
		
	//取订单缓存	
	public String excuteGetOrderCacheInfo() {
		InputObject inputObj = getInputObject();
		OutputObject outputObject = new OutputObject();
		String userId = inputObj.getParams().get("userId");
		String mobileStr = inputObj.getParams().get("mobile");
		if (StringUtil.isEmpty(userId) || StringUtil.isEmpty(mobileStr)) {
			outputObject.setReturnCode(ControlConstants.RTNCODE_ERR);
			outputObject.setReturnMessage("参数错误");
		} else {
			String cacheKey = mobileStr + "|" + userId;
			String cacheValueStr = SpringContextHelper.instance.getOscache().get(cacheKey);
			if (cacheValueStr != null) {
				//删除缓存
				SpringContextHelper.instance.getOscache().remove(cacheKey);
				outputObject.setReturnCode(ControlConstants.RTNCODE_SUC);
				outputObject.setReturnMessage(cacheValueStr);
			}else{
				//无缓存信息
				outputObject.setReturnCode(ControlConstants.RTNCODE_SUC);
				List<Map<String,Object>>  nullObjectList=new ArrayList<Map<String,Object>>();
				outputObject.setReturnMessage(JsonUtil.convertObject2Json(nullObjectList));
			}
		}
		String json = JsonUtil.convertObject2Json(outputObject);
		json = json.replaceAll("returnCode", "rtnCode");
		json = new String(json.replaceAll("returnMessage", "rtnMsg"));
		sendJson(json, true);
		return null;
	}
}
