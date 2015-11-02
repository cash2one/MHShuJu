package com.ai.eduportal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.frame.util.SpringContextHelper;
import com.ai.frame.web.util.ControlFactory;
import com.ai.frame.web.xml.bean.Action;
import com.ai.frame.web.xml.bean.Output;
import com.ai.frame.web.xml.bean.Parameter;
import common.ai.httpclient.tools.HttpClientHelper;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;
import common.ai.tools.ClassUtil;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

public class CmsServiceClient {
	public static String EDU_APP_BASEDIR = "";
	public static final String HOME_TOP_CACHE     = "homeTopKey";
	public static final String HOME_TOP_CACHETWO     = "homeTopKeytwo";
	public static final String HOME_APPS_CACHE    = "homeAppsKey";
	public static final String CMS_APPS_CACHE     = "cmsAllAppsKey";
	public static final String HOME_HOTNEWAPPS_CACHE = "homeHotNewAppsKey";
	
    private static final Logger log = LoggerFactory.getOuterCallerLog(CmsServiceClient.class);
    private static final String CMSACTION = "/hn/cmsouterAction";
    private CmsServiceClientParams httpParams;
    private HttpClientHelper httpClient;
    protected DownLoadPic downLoadPic;
    private static final String PICTYPESUB = "ContentTypes";
	public void setHttpParams(CmsServiceClientParams httpParams) {
		this.httpParams = httpParams;
	}
	public void setHttpClient(HttpClientHelper httpClient) {
		this.httpClient = httpClient;
	}
	private void calculationPicHref(List<Map<String,String>> datas,String picName){
		if(datas!=null){
			for(Map<String,String> data:datas){
				calculationPicHref(data,picName);
			}
		}
	}
	private void calculationPicHref(Map<String,String> data,String picName){
		String preHref = data.get(picName);
		if(!StringUtil.isEmpty(preHref)){
			String picType = data.get(picName + PICTYPESUB);
			if(!StringUtil.isEmpty(picType)){
				if(preHref.startsWith(".//")){
					preHref = preHref.substring(3);
				}else if(preHref.startsWith("./")){
					preHref = preHref.substring(2);
				}else if(preHref.startsWith("/")){
					preHref = preHref.substring(1);
				}
				
				data.put(picName, preHref + "/" + picName + picType);
			}
		}
	}
	/**清空所有缓存*/
	public void clearAllCache(){
		SpringContextHelper.instance.getOscache().clear();
		try{
			System.out.println("---clearAllCache-1--");
			String baseDir = EDU_APP_BASEDIR;//ServletActionContext.getServletContext().getRealPath("/");
			System.out.println("---clearAllCache-2--");
			getHomeTops(baseDir,new ArrayList<Map<String,String>>());
			System.out.println("---clearAllCache-3--");
		}catch(Exception e){
			log.error("clearAllCache", "{} called.加载应用信息失败:{}",e);
		}
		log.info("clearAllCache", "{} called.清空所有缓存完成。");
	}
	
	/**取得所有应用*/
	public String getAllApp(){
		List<Map<String,String>> appList = getAllApps();
		List<Map<String,String>> hotkeywords = getHotKeywords(appList);
		if(hotkeywords.size()>5){
			hotkeywords = hotkeywords.subList(0, 5);
		}
		
		Map<String,Object> rtnmap = new HashMap<String,Object>();
		rtnmap.put("rtnCode", "1");
		rtnmap.put("hotkeywords",  hotkeywords);
		rtnmap.put("rows", appList);
		
		String rtnJson = JsonUtil.convertObject2Json(rtnmap);
		
		return rtnJson;
	}
	private List<Map<String,String>> getUserOrders(List<Map<String,String>> appList,List<Map<String,String>> userOrders){
		List<Map<String,String>> userOrderLs = new ArrayList<Map<String,String>>();
		for(Map<String,String> userOrder:userOrders){
			String appId = userOrder.get("appId");
			Map<String,String> appData = getAppData(appId,appList);
			if(appData!=null && appData.size()>0){
				appData.put("isExpire", userOrder.get("isExpire"));
				userOrderLs.add(appData);
			}
		}
		return userOrderLs;
	}
	private Map<String,String> getAppData(String appId,List<Map<String,String>> appList){
		if(appList!=null){
			for(Map<String,String> app:appList){
				int month = getProdMonth(appId,app);
				if(month>0)return app;
			}
		}
		return null;
	}
	private int getProdMonth(String offerCode,Map<String, String> map){
		String threeOfferCode  = StringUtil.trim(map.get("threeOfferCode"));
		String sixOfferCode    = StringUtil.trim(map.get("sixOfferCode"));
		String twelveOfferCode = StringUtil.trim(map.get("twelveOfferCode"));
		offerCode = StringUtil.trim(offerCode);
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
//	private Map<String,String> getAppData(String appId,List<Map<String,String>> appList){
//		if(appList!=null){
//			for(Map<String,String> app:appList){
//				String orinalAppId = app.get("appID");
//				if(StringUtil.trim(appId).equals(StringUtil.trim(orinalAppId))){
//					return app;
//				}
//			}
//		}
//		return null;
//	}
	private List<Map<String,String>> getMyTrack(String[] myTracks,List<Map<String,String>> appList){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(String myTrack:myTracks){
			Map<String,String> app = getMyTrack(myTrack,appList);
			if(app!=null)list.add(app);
		}
		return list;
	}
	private Map<String,String> getMyTrack(String myTrack,List<Map<String,String>> appList){
		if(appList!=null){
			for(Map<String,String> app:appList){
				if(app.get("appID").equals(myTrack)){
					return app;
				}
			}
		}
		return null;
	}
	/**取得首页Body信息*/
	public String getAllHotNewApp(String myTrack,List<Map<String,String>> appfavorite,List<Map<String,String>> userOrders,long userId){
		Map<String,Object> rtn= new HashMap<String,Object>();
//		rtn.putAll(getAllHotNewApp());
		Map<String, Object> allHotNewApp = getAllHotNewApp();
			rtn.putAll(allHotNewApp);
		if(userId>0){
			List<Map<String,String>> appList = getAllApps();
			rtn.put("myTrack", getMyTrack(myTrack.split(","),appList));
		}
		if(userId>0){
			calculationPicHref(appfavorite,"appPic");
			
			rtn.put("appfavorite", appfavorite);
		}
		if(userId>0){
			List<Map<String,String>> appList = getAllApps();
			
			List<Map<String,String>> orderApps = getUserOrders(appList,userOrders);
			if(orderApps!=null&&orderApps.size()>7){
				orderApps = orderApps.subList(0, 7);
			}
			rtn.put("userOrderApps", orderApps);
		}
		
		rtn.put("rtnCode", "1");	
		
		String rtnJson = JsonUtil.convertObject2Json(rtn);
		
		return rtnJson;
	}
	/**取得所有热销新品应用*/
	public Map<String,Object> getAllHotNewApp(){
		Map<String,Object> rtnmap = SpringContextHelper.instance.getOscache().get(HOME_HOTNEWAPPS_CACHE);
		if(rtnmap!=null&&rtnmap.size()>0){
			return rtnmap;
		}
		
		List<Map<String,String>> hotList = calculatHotApps();
		List<Map<String,String>> newList = calculatNewApps();
		
		rtnmap = new HashMap<String,Object>();
		
		if(hotList!=null&&hotList.size()>7){
			hotList = hotList.subList(0, 7);
		}
		if(newList!=null&&newList.size()>7){
			newList = newList.subList(0, 7);
		}
		rtnmap.put("newApps", newList);
		rtnmap.put("hotApps", hotList);
		
		if(rtnmap!=null && rtnmap.size()>0)SpringContextHelper.instance.getOscache().put(HOME_HOTNEWAPPS_CACHE, rtnmap);
		
		return rtnmap;
	}
	private List<Map<String,String>> calculatHotApps(){
		return calculatHotNewApps("ishot");
	}
	private List<Map<String,String>> calculatNewApps(){
		return calculatHotNewApps("isnew");
	}
	private List<Map<String,String>> calculatHotNewApps(String key){
		List<Map<String,String>> appList = getAllApps();
		
		List<Map<String,String>> hotnewApps = new ArrayList<Map<String,String>>();
		if(appList!=null){
			for(Map<String,String> app:appList){
				String hotNew = app.get(key);
				if(!StringUtil.isEmpty(hotNew) && StringUtil.str2Int(hotNew) == 1){
					hotnewApps.add(app);
				}
			}
		}
		return hotnewApps;
	}
	@SuppressWarnings("unchecked")
	public Map<String,String> getAppInfo(String getKey,String keyVal){
		Object obj = SpringContextHelper.instance.getOscache().get(HOME_APPS_CACHE);
		if(obj != null){
			List<Map<String,String>> apps = (List<Map<String,String>>)obj;
			for(Map<String,String> app:apps){
				String oldKey = app.get(getKey);
				if(oldKey.equals(keyVal)){
					return app;
				}
			}
		}
		return null;
	}
	public String getAppName(String getKey,String keyVal){
		Map<String,String> app = getAppInfo(getKey,keyVal);
		if(app!=null){
			return app.get("appName");
		}
		return null;
	}
	public String getPicUrl(String getKey,String keyVal){
		Map<String,String> app = getAppInfo(getKey,keyVal);
		if(app!=null){
			return app.get("appPic");
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	private List<Map<String,String>> getAllApps(){
		Object obj = SpringContextHelper.instance.getOscache().get(HOME_APPS_CACHE);
		if(obj != null){
			return (List<Map<String,String>>)obj;
		}
		//取得单独应用
		String singlestr  = getSingleApp();
		//取得组合应用
		String mutistr    = getMutiApp();
		
		Map<String,Object> singleMp = JsonUtil.convertJson2Object(singlestr, Map.class);
		List<Map<String,String>> singleLs  = (List<Map<String,String>>)singleMp.get("rows");
		Map<String,Object> mutiMp = JsonUtil.convertJson2Object(mutistr, Map.class);
		List<Map<String,String>> mutiLs    = (List<Map<String,String>>)mutiMp.get("rows");
		
		List<Map<String,String>> appList = new ArrayList<Map<String,String>>();
		if(singleLs != null)appList.addAll(singleLs);
		if(mutiLs != null)appList.addAll(mutiLs);
		
		if(appList.size()>0)SpringContextHelper.instance.getOscache().put(HOME_APPS_CACHE, appList);
		
		return appList;
	}
	/***
	 *根据AppID取得帮助信息 
	 * @param appId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getContactByAppId(String appId){
		Map<String,String> app = getAppInfo("appID",appId);
		String contactNodeId   = app.get("contact");
		
		String contactJson     = getNodeByID(contactNodeId,"contact");
		Map<String,Object> contactMp = JsonUtil.convertJson2Object(contactJson, Map.class);
		List<Map<String,String>> contactLs = (List<Map<String,String>>)contactMp.get("rows");
		if(contactLs!=null && contactLs.size()>0){
			return contactLs.get(0);
		}
		
		return null;
	}
	/**取得应用详细信息*/
	@SuppressWarnings("unchecked")
	public Map<String,Object> getAppDetailMp(String nodeUid){
		String appDetail = getAppDetail(nodeUid);
		
		Map<String,Object> appDetailMp = JsonUtil.convertJson2Object(appDetail, Map.class);
		List<Map<String,String>> appDetailLs = (List<Map<String,String>>)appDetailMp.get("rows");
		
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		if(appDetailLs!=null && appDetailLs.size()>0){
			Map<String,String> appinfos = appDetailLs.get(0);
			
			//应用本身相关信息
			rtnMap.putAll(appinfos);
			
			//联系方式
			String contactNodeId   = appinfos.get("contact");
			String contactJson     = getNodeByID(contactNodeId,"contact");
			Map<String,Object> contactMp = JsonUtil.convertJson2Object(contactJson, Map.class);
			List<Map<String,String>> contactLs = (List<Map<String,String>>)contactMp.get("rows");
			if(contactLs!=null && contactLs.size()>0){
				rtnMap.put("contact", contactLs.get(0));
			}
			
			//推荐组合 
			List<Map<String,String>> cpackages = new ArrayList<Map<String,String>>();
			String recmdpckgNodeId = appinfos.get("recmdpckg");
			if(!StringUtil.isEmpty(recmdpckgNodeId)){
				String[] appIds = recmdpckgNodeId.split(",");
				for(int i=0;i<appIds.length;i++){
					String recmdpckgJson  = getNodeByID(appIds[i],"recmdpckg");
					Map<String,Object> recmdpckgMp = JsonUtil.convertJson2Object(recmdpckgJson, Map.class);
					List<Map<String,String>> recmdpckgLs = (List<Map<String,String>>)recmdpckgMp.get("rows");
					calculationPicHref(recmdpckgLs,"appPic");
					if(recmdpckgLs!=null && recmdpckgLs.size()>0){
						cpackages.add(recmdpckgLs.get(0));
					}
				}
			}
			rtnMap.put("cpackage", cpackages);
			
		}
		
		return rtnMap;
	}
	
	@SuppressWarnings("unchecked")
	private String getNodeByID(String nodeId,String converter){
		String contactJson     = getNodeById(nodeId,converter);
		List<Map<String,String>> datas = new ArrayList<Map<String,String>>();
		Map<String,String> data = JsonUtil.convertJson2Object(contactJson, Map.class);
		datas.add(data);
		
		return convert(datas, converter);
	}
	/**缓存所有应用包括已经上线和已经下线的产品**/
	@SuppressWarnings("unchecked")
	private void cacheAllApps(String baseDir,List<Map<String,String>> onLinesingleApp,List<Map<String,String>> onLinemutiApp,List<Map<String,String>> adverLs){
		List<Map<String,String>> appList = new ArrayList<Map<String,String>>();
		if(onLinesingleApp != null)appList.addAll(onLinesingleApp);
		if(onLinemutiApp != null)appList.addAll(onLinemutiApp);
		
		//取得已经下线的单独应用
		String singlestr  = getOffSingleApp();
		//取得已经下线的组合应用
		String mutistr    = getOffMutiApp();
		
		Map<String,Object> singleMp = JsonUtil.convertJson2Object(singlestr, Map.class);
		List<Map<String,String>> singleLs  = (List<Map<String,String>>)singleMp.get("rows");
		Map<String,Object> mutiMp = JsonUtil.convertJson2Object(mutistr, Map.class);
		List<Map<String,String>> mutiLs    = (List<Map<String,String>>)mutiMp.get("rows");
		
		if(singleLs != null)appList.addAll(singleLs);
		if(mutiLs != null)appList.addAll(mutiLs);
		
		if(appList.size()>0)SpringContextHelper.instance.getOscache().put(CMS_APPS_CACHE, appList);
		
		List<String> picUrls = calculatePics(adverLs,appList);
		downLoadPic.downloadPicFromCms(baseDir, picUrls);
	}
	/**取得首页头部*/
	@SuppressWarnings("unchecked")
	public synchronized String getHomeTops(String baseDir,List<Map<String,String>> userOrders){
		String rtnJson = "";
		rtnJson = SpringContextHelper.instance.getOscache().get(HOME_TOP_CACHE);
		
		if(!StringUtil.isEmpty(rtnJson)){
			return rtnJson;
		}
		//取得首页广告数据
		String adverstr   = getHomeAdvertising();
		//取得首页二级菜单
		String secmenustr = getHomeSecMenu();
		//取得已经上线的单独应用
		String singlestr  = getSingleApp();
		//取得已经上线的组合应用
		String mutistr    = getMutiApp();
		//取得快报
		String expressstr =  getHomeExpress();
		
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
		
		Map<String,Object> adverMp = JsonUtil.convertJson2Object(adverstr, Map.class);
		List<Map<String,String>> adverLs   = (List<Map<String,String>>)adverMp.get("rows");
		
		calculateAdverLs(adverLs,appList);
		
		cacheAllApps(baseDir,singleLs,mutiLs,adverLs);
		if(appList.size()>0)SpringContextHelper.instance.getOscache().put(HOME_APPS_CACHE, appList);
		//保存所有应用信息.结束
		
		Map<String,Object> expressMp = JsonUtil.convertJson2Object(expressstr, Map.class);
		List<Map<String,String>> expressLs = (List<Map<String,String>>)expressMp.get("rows");
		
//		calculateAdverLs(adverLs,appList);
//		//从cms下载所有图片在本地
//		List<String> picUrls = calculatePics(adverLs,appList);
//		downLoadPic.downloadPicFromCms(baseDir, picUrls);
		//List<Map<String,Map<String,String>>> picUrls = calculatePics1(adverLs,appList);
		//downLoadPic.downloadPicFromCms(baseDir, picUrls,this);
		
		Map<String,List<Map<String,String>>> secmenus = splitSecmenus(secmenuLs);
		Map<String,List<Map<String,Object>>> menus    = calculatMenus(secmenus,singleLs,mutiLs,userOrders);
		
		if(expressLs!=null&&expressLs.size()>5){
			expressLs = expressLs.subList(0, 5);
		}
		List<Map<String,String>> teacherSubMenu = getsubMenu(appList,1);
		List<Map<String,String>> studentSubMenu = getsubMenu(appList,2);
		List<Map<String,String>> activitSubMenu = getsubMenu(appList,3);
		
		Map<String,List<Map<String,String>>> subMenuMp = new HashMap<String,List<Map<String,String>>>();
		subMenuMp.put("teacher", teacherSubMenu);
		subMenuMp.put("father",  studentSubMenu);
		subMenuMp.put("actived", activitSubMenu);
		
		Map<String,Object> homeTops = new HashMap<String,Object>();
		
		homeTops.put("secmenus",  menus);
		homeTops.put("subMenu",   subMenuMp);
		homeTops.put("adverpics", adverLs);
		homeTops.put("expresses", expressLs);
		homeTops.put("rtnCode",   "1");
		
		rtnJson = JsonUtil.convertObject2Json(homeTops);
		if(null == userOrders || userOrders.size() == 0){
			if(!StringUtil.isEmpty(rtnJson))SpringContextHelper.instance.getOscache().put(HOME_TOP_CACHE, rtnJson);
		}
		
    	return rtnJson;
	}
	
	private List<Map<String,String>> getsubMenu(List<Map<String,String>> appList,int type){
		List<Map<String,String>> rtnls = new ArrayList<Map<String,String>>();
		for(Map<String,String> app :appList){
			String commonUse = StringUtil.trim(app.get("commonUse"));
			if(commonUse.indexOf(String.valueOf(type))>-1)rtnls.add(app);
		}
		return rtnls;
	}
	private void calculateAdverLs(List<Map<String,String>> adverLs,List<Map<String,String>> appList){
		if(adverLs!=null){
			for(Map<String,String> adverMp:adverLs){
				calculateAdverMp(adverMp,appList);
			}
		}
	}
	private void calculateAdverMp(Map<String,String> adverMp,List<Map<String,String>> appList){
		String appId = adverMp.get("appID");
		String isMutiApp = getPropVal(appId,appList,"isMutiApp");
		String nodeUid   = getPropVal(appId,appList,"nodeUid");
		String apphref   = getPropVal(appId,appList,"apphref");
		adverMp.put("isMutiApp", StringUtil.trim(isMutiApp));
		adverMp.put("nodeUid", StringUtil.trim(nodeUid));
		adverMp.put("apphref", StringUtil.trim(apphref));
	}
	private String getPropVal(String advAppId,List<Map<String,String>> appList,String propName){
		if(appList!=null){
			for(Map<String,String> appMp:appList){
				String appId = appMp.get("appID");
				if(StringUtil.trim(appId).equals(StringUtil.trim(advAppId))){
					return appMp.get(propName);
				}
			}
		}
		return null;
	}
	private List<Map<String,String>> getHotKeywords(List<Map<String,String>> appList){
		List<Map<String,String>> keywords = new ArrayList<Map<String,String>>();
		if(appList!=null){
			for(Map<String,String> appMap:appList){
				int isKeywords = StringUtil.str2Int(appMap.get("isKeywords"), 2);
				if(isKeywords == 1){
					keywords.add(appMap);
				}
			}
		}
		return keywords;
	}
//	private List<Map<String,Map<String,String>>> calculatePics1(List<Map<String,String>> adverLs,List<Map<String,String>> appList){
//		List<Map<String,Map<String,String>>> picurls = new ArrayList<Map<String,Map<String,String>>>();
//		picurls.addAll(calculatePics1(adverLs));
//		picurls.addAll(calculatePics1(appList));
//		
//		return picurls;
//	}
//	private List<Map<String,Map<String,String>>> calculatePics1(List<Map<String,String>> picList){
//		List<Map<String,Map<String,String>>> picurls = new ArrayList<Map<String,Map<String,String>>>();
//		if(picList!=null&&picList.size()>0){
//			for(Map<String,String> picMp:picList){
//				Map<String,String> urls = new HashMap<String,String>();
//				String appPic     = picMp.get(DownLoadPic.APP_APPPIC);
//				String appMxPic   = picMp.get(DownLoadPic.APP_APPMXPIC);
//				String appDescPic = picMp.get(DownLoadPic.APP_APPDESCPIC);
//				String adverPic   = picMp.get(DownLoadPic.APP_ADVERPIC);
//				
//				int i=0;
//				if(!StringUtil.isEmpty(adverPic)){
//					urls.put(DownLoadPic.APP_ADVERPIC,adverPic);
//					i++;
//				}
//				if(!StringUtil.isEmpty(appPic)){
//					urls.put(DownLoadPic.APP_APPPIC,appPic);
//					i++;
//				}
//				if(!StringUtil.isEmpty(appMxPic)){
//					urls.put(DownLoadPic.APP_APPMXPIC,appMxPic);
//					i++;
//				}
//				if(!StringUtil.isEmpty(appDescPic)){
//					urls.put(DownLoadPic.APP_APPDESCPIC,appDescPic);i++;
//				}
//				if(i > 0){
//					String nodeId = picMp.get(DownLoadPic.APP_NODEUID);
//					Map<String,Map<String,String>> urlmap = new HashMap<String, Map<String,String>>();
//					urlmap.put(nodeId, urls);
//					picurls.add(urlmap);
//				}
//			}
//		}
//		
//		return picurls;
//	}
	
	protected List<String> calculatePics(List<Map<String,String>> adverLs,List<Map<String,String>> appList){
		List<String> picurls = new ArrayList<String>();
		picurls.addAll(calculatePics(adverLs));
		picurls.addAll(calculatePics(appList));
		
		return picurls;
	}
	protected List<String> calculatePics(List<Map<String,String>> picList){
		List<String> picurls = new ArrayList<String>();
		if(picList!=null&&picList.size()>0){
			for(Map<String,String> picMp:picList){
				String appPic     = picMp.get("appPic");
				String appMxPic   = picMp.get("appMxPic");
				String appDescPic = picMp.get("appDescPic");
				String adverPic   = picMp.get("adverPic");
				String priceTable   = picMp.get("priceTable");
				String appdown    = picMp.get("appdown");
//				System.out.println("calculatePics:"+appdown);
				if(!StringUtil.isEmpty(adverPic)){
					picurls.add(adverPic);
				}
				if(!StringUtil.isEmpty(appPic)){
					picurls.add(appPic);
				}
				if(!StringUtil.isEmpty(appMxPic)){
					picurls.add(appMxPic);
				}
				if(!StringUtil.isEmpty(appDescPic)){
					picurls.add(appDescPic);
				}
				if(!StringUtil.isEmpty(priceTable)){
					picurls.add(priceTable);
				}
				if(!StringUtil.isEmpty(appdown)){
					picurls.add(appdown);
				}
			}
		}
		
		return picurls;
	}
	
	private Map<String,List<Map<String,Object>>> calculatMenus(Map<String,List<Map<String,String>>> secmenus,List<Map<String,String>> singleLs,List<Map<String,String>> mutiLs,List<Map<String, String>> userOrders){
		List<Map<String,String>> teacher = secmenus.get("teacher");
		List<Map<String,String>> father  = secmenus.get("father");
		List<Map<String,String>> actived = secmenus.get("actived");
		
		Map<String,List<Map<String,Object>>> menusMaps = new HashMap<String,List<Map<String,Object>>>();
		
		List<Map<String,Object>> tmenus = calculatMenus(teacher,singleLs,mutiLs,1);
		List<Map<String,Object>> fmenus = calculatMenus(father,singleLs,mutiLs,2);
		List<Map<String,Object>> amenus = calculatMenus(actived,singleLs,mutiLs,3);
		
		
		menusMaps.put("teacher", tmenus);
		menusMaps.put("father",  fmenus);
		menusMaps.put("actived", amenus);
		
		return menusMaps;
	}
	
	private List<Map<String,Object>> calculatMenus(List<Map<String,String>> secMenus,List<Map<String,String>> singleLs,List<Map<String,String>> mutiLs,int type){
		List<Map<String,Object>> menuLs = new ArrayList<Map<String,Object>>();
		
		for(Map<String,String> secMenu:secMenus){
			String menuID = secMenu.get("menuID");
			Map<String,Object> menuMap = new HashMap<String,Object>();
			List<Map<String,String>> thirdMenus = getMenuApps(menuID,singleLs,mutiLs,type);
			if(thirdMenus==null || thirdMenus.size()<1){
				continue;
			}
			
			menuMap.putAll(secMenu);
			menuMap.put("thirdMenus", getMenuApps(menuID,singleLs,mutiLs,type));
			menuLs.add(menuMap);
		}
		
		return menuLs;
	}
	private List<Map<String,String>> getMenuApps(String menuId,List<Map<String,String>> singleLs,List<Map<String,String>> mutiLs,int type){
		List<Map<String,String>> apps = new ArrayList<Map<String,String>>();
		if(singleLs!=null){
			apps.addAll(getMenuApps(menuId,singleLs,type));
		}
		
		if(mutiLs!=null){
			apps.addAll(getMenuApps(menuId,mutiLs,type));
		}
		
		return apps;
	}
	private List<Map<String,String>> getMenuApps(String menuId,List<Map<String,String>> apps,int type){
		List<Map<String,String>> menuapps = new ArrayList<Map<String,String>>();
		for(Map<String,String> app:apps){
			if(StringUtil.trim(menuId).equals(StringUtil.trim(app.get("secMenu")))){//判断是否是指定二级菜单下
				if(type == 1 && isTeacher(StringUtil.trim(app.get("domain")))){
					menuapps.add(app);
				}else if(type == 2 && isStudent(StringUtil.trim(app.get("domain")))){
					menuapps.add(app);
				}else if(type == 3 && isActived(StringUtil.trim(app.get("isActive")))){
					menuapps.add(app);
				}
			}
		}
		return menuapps;
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
	private boolean isActived(String isActive){
		if(!StringUtil.isEmpty(isActive) && StringUtil.str2Int(isActive) == 1){//参加活动
			return true;
		}
		return false;
	}
	private Map<String,List<Map<String,String>>> splitSecmenus(List<Map<String,String>> secmenuLs){
		List<Map<String,String>> teacher = new ArrayList<Map<String,String>>();
		List<Map<String,String>> father  = new ArrayList<Map<String,String>>();
		List<Map<String,String>> actived = new ArrayList<Map<String,String>>();
		
		for(Map<String,String> secmenu:secmenuLs){
			String menuDomain = secmenu.get("menuDomain");
			String isActive   = secmenu.get("isActive");
			if(isTeacher(menuDomain)){//老师
				teacher.add(secmenu);
			}
			if(isStudent(menuDomain)){//家长和学生
				father.add(secmenu);
			}
			if(isActived(isActive)){//参加活动
				actived.add(secmenu);
			}
		}
		
		Map<String,List<Map<String,String>>> rtn = new HashMap<String,List<Map<String,String>>>();
		rtn.put("teacher", teacher);
		rtn.put("father",  father);
		rtn.put("actived", actived);
		
		return rtn; 
	}
	
    @SuppressWarnings("unchecked")
	public String getHomeAdvertising(){
    	String rtnJson = getHomeAdvertisingOriginal();
    	List<Map<String,String>> datas = JsonUtil.convertJson2Object(rtnJson, List.class);
    	
    	calculationPicHref(datas,DownLoadPic.APP_ADVERPIC);
    	
    	return convert(datas,"getHomeAdvertising");
    }
	public String getHomeAdvertisingOriginal(){
		return httpClient.getHttpResonseJson(httpParams.getHomeAdvertising(CmsServiceClientParams.ONLINE), "getHomeAdvertising");
	}
	
	@SuppressWarnings("unchecked")
	public String getHomeSecMenu(){
		String rtnJson = getHomeSecMenuOriginal();
    	List<Map<String,String>> datas = JsonUtil.convertJson2Object(rtnJson, List.class);
    	
    	return convert(datas,"getHomeSecMenu");
	}
	
	public String getHomeSecMenuOriginal(){
		return httpClient.getHttpResonseJson(httpParams.getHomeSecMenu(CmsServiceClientParams.ONLINE), "getHomeSecMenu");
	}
	
	@SuppressWarnings("unchecked")
	public String getHomeExpress(){
		String rtnJson = getHomeExpressOriginal();
    	List<Map<String,String>> datas = JsonUtil.convertJson2Object(rtnJson, List.class);
    	
    	return convert(datas,"getHomeExpress");
	}
	public String getHomeExpressOriginal(){
		return httpClient.getHttpResonseJson(httpParams.getHomeExpress(CmsServiceClientParams.ONLINE), "getHomeExpress");
	}
	/**取得应用详细信息**/
	@SuppressWarnings("unchecked")
	public String getAppDetail(String nodeUid){
		String rtnJson = getAppDetailOriginal(nodeUid);
//    	List<Map<String,String>> datas = JsonUtil.convertJson2Object(rtnJson, List.class);
    	Map<String,String> data = JsonUtil.convertJson2Object(rtnJson, Map.class);
    	List<Map<String,String>> datas = new ArrayList<Map<String,String>>();
    	datas.add(data);
    	
    	calculationPicHref(datas,DownLoadPic.APP_APPPIC);
    	calculationPicHref(datas,DownLoadPic.APP_APPMXPIC);
    	calculationPicHref(datas,DownLoadPic.APP_APPDESCPIC);
    	calculationPicHref(datas,DownLoadPic.APP_PRICETABLE);
    	calculationPicHref(datas,DownLoadPic.APP_APPDOWN);
    	
    	return convert(datas,"getAppDetail");
	}
	public String getAppDetailOriginal(String nodeUid){
		return getNodeById(nodeUid,"getAppDetail");
	}
	public String getNodeById(String nodeUid,String logType){
		return httpClient.getHttpResonseJson(httpParams.getNodeByUid(nodeUid), logType);
	}
	@SuppressWarnings("unchecked")
	public String getNodeBinaryByUid(String nodeUid){
		String rtnJson = getNodeBinaryOriginalByUid(nodeUid);
		
		Map<String,String> data = JsonUtil.convertJson2Object(rtnJson, Map.class);
    	List<Map<String,String>> datas = new ArrayList<Map<String,String>>();
    	datas.add(data);
    	
    	return convert(datas,"getAppDetail");
	}
	public String getNodeBinaryOriginalByUid(String nodeUid){
		return httpClient.getHttpResonseJson(httpParams.getNodeBinaryByUid(nodeUid), "getNodeBinaryProps");
	}
	@SuppressWarnings("unchecked")
	public String getSingleApp(){
		String rtnJson = getSingleAppOriginal();
    	List<Map<String,String>> datas = JsonUtil.convertJson2Object(rtnJson, List.class);
    	calculationPicHref(datas,DownLoadPic.APP_APPPIC);
    	calculationPicHref(datas,DownLoadPic.APP_APPMXPIC);
    	calculationPicHref(datas,DownLoadPic.APP_APPDESCPIC);
    	calculationPicHref(datas,DownLoadPic.APP_PRICETABLE);
    	calculationPicHref(datas,DownLoadPic.APP_APPDOWN);
    	
    	return convert(datas,"getSingleApp");
	}
	@SuppressWarnings("unchecked")
	public String getOffSingleApp(){
		String rtnJson = getSingleAppOffOriginal();
    	List<Map<String,String>> datas = JsonUtil.convertJson2Object(rtnJson, List.class);
    	calculationPicHref(datas,DownLoadPic.APP_APPPIC);
    	calculationPicHref(datas,DownLoadPic.APP_APPMXPIC);
    	calculationPicHref(datas,DownLoadPic.APP_APPDESCPIC);
    	calculationPicHref(datas,DownLoadPic.APP_PRICETABLE);
    	return convert(datas,"getSingleApp");
	}
	public String getSingleAppOriginal(){
		return httpClient.getHttpResonseJson(httpParams.getSingleApp(CmsServiceClientParams.ONLINE), "getSingleApp");
	}
	public String getSingleAppOffOriginal(){
		return httpClient.getHttpResonseJson(httpParams.getSingleApp(CmsServiceClientParams.OFFLINE), "getSingleApp");
	}
	@SuppressWarnings("unchecked")
	public String getMutiApp(){
		String rtnJson = getMutiAppOriginal();
    	List<Map<String,String>> datas = JsonUtil.convertJson2Object(rtnJson, List.class);
    	calculationPicHref(datas,DownLoadPic.APP_APPPIC);
    	calculationPicHref(datas,DownLoadPic.APP_APPMXPIC);
    	calculationPicHref(datas,DownLoadPic.APP_APPDESCPIC);
    	calculationPicHref(datas,DownLoadPic.APP_PRICETABLE);
    	return convert(datas,"getMutiApp");
	}
	@SuppressWarnings("unchecked")
	public String getOffMutiApp(){
		String rtnJson = getMutiAppOffOriginal();
    	List<Map<String,String>> datas = JsonUtil.convertJson2Object(rtnJson, List.class);
    	calculationPicHref(datas,DownLoadPic.APP_APPPIC);
    	calculationPicHref(datas,DownLoadPic.APP_APPMXPIC);
    	calculationPicHref(datas,DownLoadPic.APP_APPDESCPIC);
    	calculationPicHref(datas,DownLoadPic.APP_PRICETABLE);
    	
    	return convert(datas,"getMutiApp");
	}
	public String getMutiAppOriginal(){
		return httpClient.getHttpResonseJson(httpParams.getMutiApp(CmsServiceClientParams.ONLINE), "getMutiApp");
	}
	public String getMutiAppOffOriginal(){
		return httpClient.getHttpResonseJson(httpParams.getMutiApp(CmsServiceClientParams.OFFLINE), "getMutiApp");
	}
	public String convert(List<Map<String,String>> datas,String uid){
		Action cmsaction = ControlFactory.getControl().getAction(CMSACTION);
		if(cmsaction!=null){
			Output cmsoutput = cmsaction.getOutput(uid);
			List<Parameter> parameters = cmsoutput.getParameters();
			String convertor = cmsoutput.getConvertor();
			String method    = cmsoutput.getMethod();
			if(StringUtil.isEmpty(convertor)){
				convertor = "com.ai.eduportal.convertor.CmsResultConvert";
			}
			if(StringUtil.isEmpty(method)){
				log.error("{} convert to json has't config the convert method.", null, uid);
				
				return null;
			}
			
			Class<?>[] paramcls = new Class<?>[]{List.class,List.class};
			Object[]  paramvals = new Object[]{datas,parameters};
			
			return ClassUtil.invokMethod(String.class, convertor, method, paramcls, paramvals);
		}else{
			log.error("{} convert to json has't get the action:{}", null, uid,CMSACTION);
		}
		return null;
	}
	public void setDownLoadPic(DownLoadPic downLoadPic) {
		this.downLoadPic = downLoadPic;
	}
}
