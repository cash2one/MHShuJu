package com.ai.eduportal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.ai.eduportal.bean.HEAUser;
import com.ai.frame.bean.InputObject;
import com.ai.frame.bean.OutputObject;
import com.ai.frame.util.SpringContextHelper;
import com.ai.frame.web.control.CallCoreService;
import com.ai.frame.web.convertor.CommonConvert;
import common.ai.tools.DateUtil;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

public class Cms4App extends CmsServiceClient {
	private CallCoreService callCoreService;
	public static final String APP_HOME_CACHE     = "appAllHomeKey";
	public static final String APP_MENUS_CACHE    = "appMenusCacheKey";
	public static final String HOTMENUID          = "hot-menu";
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String queryAppList(InputObject inobj) {
		String menuapps = getStdAppList(inobj);
		Map menuappMap  = JsonUtil.convertJson2Object(menuapps, Map.class);
		String menuID   = inobj.getParams().get("menuID");
		String keywords = inobj.getParams().get("keywords");
		if(StringUtil.isEmpty(menuID)){
			menuID = HOTMENUID;
		}
		
		List<Map<String,String>> orders = null;
		try{
			InputObject inputobj = new InputObject();
			inputobj.setService("crmCallerService");
			inputobj.setMethod("getUserOrders");
			inputobj.getParams().put("userId", inobj.getParams().get("mobile"));
			OutputObject userOrders = callCoreService.execute(inputobj);
			orders = userOrders.getBeans();
		}catch(Exception e){
		}
		
		List<Map<String,Object>> stdapps = (List<Map<String,Object>>)menuappMap.get("datas");
		
		//搜索
		if(!StringUtil.isEmpty(keywords)){
			return searchApps(stdapps,keywords,orders);
		}
		
		List<Map<String,Object>> meuapps = new ArrayList<Map<String,Object>>();
		if(stdapps != null && stdapps.size() > 0){
			for(Map<String,Object> stdapp:stdapps){

//				int ishotapp    =  StringUtil.str2Int(StringUtil.obj2TrimStr(stdapp.get("ishot")));
//				if((HOTMENUID.equals(menuID) && ishotapp == 1)|| (StringUtil.trim(menuID).equals(StringUtil.trim((String)stdapp.get("menuID"))))){
				if(StringUtil.trim(menuID).equals(StringUtil.trim((String)stdapp.get("menuID")))){
					
					List<Map<String,String>> apps = (List<Map<String,String>>)stdapp.get("appLs");
					Map<String,Object> appmap = new HashMap<String,Object>();
					appmap.putAll(stdapp);
					appmap.remove("appLs");
					List<Map<String,String>> appLs = new ArrayList<Map<String,String>>();
					for(Map<String,String> app:apps){
//						System.out.println(app.get("appID"));
						if(canAdd(inobj,app,orders)){
							if(isOrdered(app.get("appID"),orders)){
								app.put("byOrdered", "1");
							}
							appLs.add(app);
						}
					}
					appmap.put("appLs", appLs);

					meuapps.add(appmap);
				}
			}
		}

		Map<String,Object> rtnMp = new HashMap<String,Object>();
		rtnMp.put("datas", meuapps);
		rtnMp.put("rtnCode", 1);
		String rtnJson = JsonUtil.convertObject2Json(rtnMp); 
		return rtnJson;
	}
	@SuppressWarnings("unchecked")
	private String searchApps(List<Map<String,Object>> stdapps,String keywords,List<Map<String,String>> orders){
		List<Map<String,Object>> meuapps = new ArrayList<Map<String,Object>>();
		if(stdapps != null && stdapps.size() > 0){
			InputObject inobj = new InputObject();
			inobj.getParams().put("keywords", keywords);
			for(Map<String,Object> stdapp:stdapps){
				List<Map<String,String>> apps = (List<Map<String,String>>)stdapp.get("appLs");
				
				Map<String,Object> appmap = new HashMap<String,Object>();
				List<Map<String,String>> appLs = new ArrayList<Map<String,String>>();
				for(Map<String,String> app:apps){
					if(canAdd(inobj,app,orders)){
						if(isOrdered(app.get("appID"),orders)){
							app.put("byOrdered", "1");
						}
						appLs.add(app);
					}
				}
				appmap.put("appLs", appLs);

				meuapps.add(appmap);
			}
		}
		
		Map<String,Object> rtnMp = new HashMap<String,Object>();
		rtnMp.put("datas", meuapps);
		rtnMp.put("rtnCode", 1);
		String rtnJson = JsonUtil.convertObject2Json(rtnMp); 
		return rtnJson;
	}
	//条件筛选
	private boolean canAdd(InputObject inobj,Map<String,String> app,List<Map<String,String>> orders){
		boolean teacher = StringUtil.str2Boolean(inobj.getParams().get("teacher"));
		boolean student = StringUtil.str2Boolean(inobj.getParams().get("student"));
		boolean ismuti  = StringUtil.str2Boolean(inobj.getParams().get("ismuti"));
		boolean isfree  = StringUtil.str2Boolean(inobj.getParams().get("isfree"));
		boolean horder  = StringUtil.str2Boolean(inobj.getParams().get("horder"));
		boolean norder  = StringUtil.str2Boolean(inobj.getParams().get("norder"));
		String keywords  = StringUtil.trim(inobj.getParams().get("keywords"));
		
//		boolean tcanadd = true;
//		boolean scanadd = true;
//		boolean mcanadd = true;
//		boolean fcanadd = true;
//		boolean ocanadd = true;
//		boolean ncanadd = true;
		boolean kcanadd = true;
		boolean canadd = true;
		
//		if(teacher && !isTeacher(StringUtil.trim(app.get("domain")))){//是老师
//			tcanadd = false;
//		}
		if(teacher && !isTeacher(StringUtil.trim(app.get("domain")))){//是老师
			canadd = false;
		}
		
//		if(student && !isStudent(StringUtil.trim(app.get("domain")))){//是家长\学生
//			scanadd = false;
//		}
		if(student && !isStudent(StringUtil.trim(app.get("domain")))){//是家长\学生
			canadd = false;
		}
		
//		if(ismuti && !isMutiApp(StringUtil.trim(app.get("isMutiApp")))){//是营销
//			mcanadd = false;
//		}
		if(ismuti && !isMutiApp(StringUtil.trim(app.get("isMutiApp")))){//是营销
			canadd = false;
		}
		
//		if(isfree && !isfree(app)){//是免费
//			fcanadd = false;
//		}
		if(isfree && !isfree(app)){//是免费
			canadd = false;
		}
		
//		if((horder && !norder) && !isOrdered(app.get("appID"),orders)){//是已订购
//			ocanadd = false;
//		}
		if((horder && !norder) && !isOrdered(app.get("appID"),orders)){//是已订购
			canadd = false;
		}
		
//		if((!horder && norder) && isOrdered(app.get("appID"),orders)){//是未订购
//			ncanadd = false;
//		}
		if((!horder && norder) && isOrdered(app.get("appID"),orders)){//是未订购
			canadd = false;
		}
		
		
		if(!StringUtil.isEmpty(keywords)){//关键词
//			String appName = app.get("appName");
//			String appDesc = app.get("appDesc");
			String appVal  = app.get("label");
			
			if(
//			   (!StringUtil.isEmpty(appName) && appName.indexOf(keywords)>-1)
//			 ||(!StringUtil.isEmpty(appDesc) && appDesc.indexOf(keywords)>-1)||
			 (!StringUtil.isEmpty(appVal)  && appVal.toUpperCase().indexOf(keywords.toUpperCase())>-1)
			   ){
				kcanadd = true;
			}else{
				kcanadd = false;
			}
			return kcanadd;
		}
		
//		if(tcanadd && scanadd && mcanadd && fcanadd && ocanadd && ncanadd){
//			return false;
//		}else{
//			return true;
//		}
		
//		System.out.println("----canadd---->"+canadd);
		return canadd;
	}
	@SuppressWarnings("unchecked")
	public String getStdMenuList(InputObject inobj) {
		//取得首页二级菜单
		Map<String,Object> secmenuMp  = SpringContextHelper.instance.getOscache().get(APP_MENUS_CACHE);
		if(secmenuMp==null||secmenuMp.size()==0){
			String secmenustr = getHomeSecMenu();
			secmenuMp = JsonUtil.convertJson2Object(secmenustr, Map.class);
			if (secmenuMp != null && secmenuMp.size() > 0) {
				List<Map<String,String>> rows = (List<Map<String,String>>)secmenuMp.get(CommonConvert.OMGGRID_ROWSKEY);
				Map<String,String> hotmenu = new HashMap<String,String>();
				hotmenu.put("menuID",   HOTMENUID);
				hotmenu.put("menuName", "热门应用");
				rows.add(0, hotmenu);
				secmenuMp.put(CommonConvert.OMGGRID_ROWSKEY, rows);
				
				secmenuMp.put("rtnCode", 1);
				SpringContextHelper.instance.getOscache().put(APP_MENUS_CACHE, secmenuMp);
			}
		}
		
		String rtnJson = JsonUtil.convertObject2Json(secmenuMp); 
		return rtnJson; 
	}
	@SuppressWarnings("unchecked")
	public String getStdAppList(InputObject inobj) {
		List<Map<String,Object>> stdapps = SpringContextHelper.instance.getOscache().get(APP_HOME_CACHE);
		if(stdapps != null && stdapps.size() > 0){
			Map<String,Object> rtnMp = new HashMap<String,Object>();
			rtnMp.put("datas", stdapps);
			rtnMp.put("rtnCode", 1);
			String rtnJson = JsonUtil.convertObject2Json(rtnMp); 
			return rtnJson;
		}
		
		//取得首页二级菜单
		String secmenustr = getStdMenuList(inobj);
		//取得已经上线的单独应用
		String singlestr  = getSingleApp();
		//取得已经上线的组合应用
		String mutistr    = getMutiApp();
		
		Map<String,Object> secmenuMp = JsonUtil.convertJson2Object(secmenustr, Map.class);
		List<Map<String,String>> secmenuLs = (List<Map<String,String>>)secmenuMp.get("rows");
		Map<String,Object> singleMp = JsonUtil.convertJson2Object(singlestr, Map.class);
		List<Map<String,String>> singleLs  = (List<Map<String,String>>)singleMp.get("rows");
		Map<String,Object> mutiMp = JsonUtil.convertJson2Object(mutistr, Map.class);
		List<Map<String,String>> mutiLs    = (List<Map<String,String>>)mutiMp.get("rows");
		
		//保存所有应用信息
		List<Map<String,String>> appList = new ArrayList<Map<String,String>>();
		if(singleLs != null)appList.addAll(singleLs);
		if(mutiLs != null)appList.addAll(mutiLs);
		
		stdapps = calculatMenus(secmenuLs,appList);
		if (stdapps != null && stdapps.size() > 0) {
			SpringContextHelper.instance.getOscache().put(APP_HOME_CACHE, stdapps);
		}
		
		Map<String,Object> rtnMp = new HashMap<String,Object>();
		rtnMp.put("datas", stdapps);
		rtnMp.put("rtnCode", 1);
		String rtnJson = JsonUtil.convertObject2Json(rtnMp); 
		return rtnJson;
	}
//	private boolean isOrder(Map<String,String> app){
//		String isOrder = app.get("byOrdered");
//		if(StringUtil.str2Int(isOrder) == 1){
//			return true;
//		}
//		return false;
//	}
	private boolean isfree(Map<String,String> app){
		String isMutiApp = app.get("isMutiApp");
		if(!StringUtil.isEmpty(isMutiApp) && !isMutiApp.equals("1")){//组合应用
			if(StringUtil.str2Double(app.get("packagePrice"))==0){
				return true;
			}
		}
		return false;
	}
	private boolean isMutiApp(String isMutiApp){
		if(!StringUtil.isEmpty(isMutiApp) && isMutiApp.equals("1")){//组合应用
			return true;
		}
		return false;
	}
	private boolean isTeacher(String domain){
		if(!StringUtil.isEmpty(domain) && domain.indexOf("2")>-1){//老师
			return true;
		}
		return false;
	}
	private boolean isStudent(String domain){
		if(!StringUtil.isEmpty(domain) && (domain.indexOf("1")>-1 && domain.indexOf("3")>-1)){//家长和学生
			return true;
		}
		return false;
	}
	private boolean isOrdered(String appId,List<Map<String,String>> orders){
		if(orders!=null&&orders.size()>0){
			for(Map<String,String> orderapp:orders){
				String isExpire = orderapp.get("expireDate");
				long diff = DateUtil.compareDate(DateUtil.string2Date(isExpire));
				if(diff > 0) continue;
				if(diff <= 0 && StringUtil.trim(appId).equals(StringUtil.trim(orderapp.get("appId")))){
					return true;
				}
			}
		}
		return false;
	}
	private List<Map<String,Object>> calculatMenus(List<Map<String,String>> secMenus,List<Map<String,String>> appList){
		List<Map<String,Object>> menuLs = new ArrayList<Map<String,Object>>();
		for(Map<String,String> secMenu:secMenus){
			String menuID = secMenu.get("menuID");
			Map<String,Object> menuMap = new HashMap<String,Object>();
			
			List<Map<String,String>> thirdMenus = getMenuApps(menuID,appList);
			if(thirdMenus==null || thirdMenus.size()<1){
				continue;
			}
			menuMap.putAll(secMenu);
			menuMap.put("appLs", thirdMenus);
			menuLs.add(menuMap);
		}
		return menuLs;
	}
	private List<Map<String,String>> getMenuApps(String menuID,List<Map<String,String>> appList){
		List<Map<String,String>> apps = new ArrayList<Map<String,String>>();
		if(appList!=null && appList.size() > 0){
			for(Map<String,String> app:appList){
				int ishotapp    =  StringUtil.str2Int(StringUtil.obj2TrimStr(app.get("ishot")));
				
				if((HOTMENUID.equals(menuID) && ishotapp == 1) || (StringUtil.trim(menuID).equals(StringUtil.trim(app.get("secMenu"))))){
					apps.add(app);
				}
			}
		}
		return apps;
	}
	public void setCallCoreService(CallCoreService callCoreService) {
		this.callCoreService = callCoreService;
	}
	
	// 产品详情
	public String getHeaAppDetail(InputObject inputObject) {
		StringBuffer rtn;
		try {
			Map<String, String> map = inputObject.getParams();
			String appId = map.get("appId");
			if(StringUtil.isEmpty(appId)){
				rtn = new StringBuffer();
				rtn.append("{\"rtnCode\":").append("\"0\"").append(",\"rtnMsg\":").append("\"参数错误\"").append("}");
				return rtn.toString();
			}
			Map<String, Object> appDetails =getAppDetailMp(appId);
			String offerId = StringUtil.obj2Str(appDetails.get("offerId"));
			String threeOfferCode = StringUtil.obj2Str(appDetails.get("threeOfferCode"));
			String sixOfferCode = StringUtil.obj2Str(appDetails.get("sixOfferCode"));
			String twelveOfferCode = StringUtil.obj2Str(appDetails.get("twelveOfferCode"));
			String threeSmsCode = StringUtil.obj2Str(appDetails.get("threeSmsCode"));
			String sixSmsCode = StringUtil.obj2Str(appDetails.get("sixSmsCode"));
			String twelveSmsCode = StringUtil.obj2Str(appDetails.get("twelveSmsCode"));
			String isMutiApp = StringUtil.obj2Str(appDetails.get("isMutiApp"));
			map.put("offerId", offerId);
			map.put("threeOfferCode", threeOfferCode);
			map.put("sixOfferCode", sixOfferCode);
			map.put("twelveOfferCode", twelveOfferCode);
			map.put("threeSmsCode", threeSmsCode);
			map.put("sixSmsCode", sixSmsCode);
			map.put("twelveSmsCode", twelveSmsCode);
			map.put("isMutiApp", isMutiApp);
			OutputObject object =callCoreService.execute(inputObject);
			Map<String, String> rtnMap = object.getBean();
			String orderState=rtnMap.get("orderState");
			String month=rtnMap.get("month");
			appDetails.put("roleState", rtnMap.get("roleState"));
			appDetails.put("orderState",orderState);
			
			rtnMap.clear();
			List<Map<String, String>> ptList=new ArrayList<Map<String,String>>();
			//组合产品
			if ("1".equals(isMutiApp)) {
				//组合产品价格列表
				rtnMap.put("price", String.valueOf(appDetails.get("threepromprice")));
				rtnMap.put("smsCode",String.valueOf(appDetails.get("threeSmsCode")));
				rtnMap.put("offerCode", String.valueOf(appDetails.get("threeOfferCode")));
				rtnMap.put("month", "3");
				rtnMap.put("ptSubName", "3个月");
				ptList.add(rtnMap);
				
				rtnMap=new HashMap<String, String>();
				rtnMap.put("price",String.valueOf(appDetails.get("sixpromprice")));
				rtnMap.put("smsCode",String.valueOf(appDetails.get("sixSmsCode")));
				rtnMap.put("offerCode",String.valueOf(appDetails.get("sixOfferCode")));
				rtnMap.put("month", "6");
				rtnMap.put("ptSubName", "6个月");
				ptList.add(rtnMap);
				
				rtnMap=new HashMap<String, String>();
				rtnMap.put("price", String.valueOf(appDetails.get("twelvepromprice")));
				rtnMap.put("smsCode",String.valueOf(appDetails.get("twelveSmsCode")));
				rtnMap.put("offerCode", String.valueOf(appDetails.get("twelveOfferCode")));
				rtnMap.put("month", "12");
				rtnMap.put("ptSubName", "12个月");
				ptList.add(rtnMap);
				
				appDetails.put("mPtInfo", ptList);
				//默认选中
				if(month==null||month.equals("3")){
					appDetails.put("mIndex", 0);
				}else if(month.equals("6")){
					appDetails.put("mIndex", 1);
				}else {
					appDetails.put("mIndex", 2);
				}
			}else{
				 appDetails.put("offerCode",threeOfferCode);
			}
			appDetails.put("rtnCode", "1");
			int roleid = 0;
			try {
				HEAUser heaUser = null;
				HttpSession session=getSession(false);
				// session有数据
				if (session != null) {
					heaUser = (HEAUser) session.getAttribute(HEAUser.HEA_CURRENT_USER);
					roleid=heaUser.getTypeId();
				}
			} catch (Exception e) {
			}
			appDetails.put("rtnMsg", getDetailMsg(object.getBean(),StringUtil.obj2Str(appDetails.get("domain")),roleid));
			String sendJson = JsonUtil.convertObject2Json(appDetails);
			return sendJson;
		} catch (Exception e) {
			rtn = new StringBuffer();
			rtn.append("{\"rtnCode\":").append("\"0\"").append(",\"rtnMsg\":\"").append(e.getMessage()).append("\"}");
		}
		return rtn.toString();
	}
	
	 public String getDetailMsg(Map<String,String> details,String appdomains,int roleid){
		String msg = "";
		if(StringUtil.str2Int(details.get("orderState")) == 1){
			msg = "已订购产品无法再次订购！";
		}else if(roleid > 0 &&(StringUtil.isEmpty(appdomains) || appdomains.indexOf(roleid)==-1)){
			msg = "您当前的角色无法订购该产品！";
		}else if(StringUtil.str2Int(details.get("roleState")) != 1){
			msg = "您没有权限订购该产品！";
		}
		return msg;
	}
	
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
		
	public HttpSession getSession(boolean isNew) {
		return getRequest().getSession(isNew);
	}
	
	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}
}
