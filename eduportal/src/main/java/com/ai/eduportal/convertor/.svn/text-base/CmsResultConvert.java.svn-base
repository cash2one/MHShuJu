package com.ai.eduportal.convertor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.eduportal.service.CmsServiceClient;
import com.ai.frame.bean.InputObject;
import com.ai.frame.bean.OutputObject;
import com.ai.frame.util.ControlConstants;
import com.ai.frame.util.SpringContextHelper;
import com.ai.frame.web.convertor.CommonConvert;
import com.ai.frame.web.xml.bean.Parameter;
import common.ai.tools.JsonUtil;

public class CmsResultConvert extends CommonConvert {
	public String convert2list(InputObject input,OutputObject output,List<Parameter> parameters){
		Map<String,Object> gridMp = new HashMap<String,Object>();
    	if (parameters != null && parameters.size() > 0) {
    		gridMp.put(getToKey(RTNCODEKEY,parameters), output.getReturnCode());
    		gridMp.putAll(convertMap(output.getBean(),parameters));
    	}else{
    		gridMp.put(RTNCODEKEY, output.getReturnCode());
    		gridMp.putAll(output.getBean());
    	}
    	
    	List<Map<String,String>> datas = output.getBeans();
    	List<Map<String,String>> rows  = convertListMap(datas,parameters);
    	gridMp.put(OMGGRID_ROWSKEY, rows);
    	return JsonUtil.convertObject2Json(gridMp);
    }
	public String convertFavorite(InputObject input,OutputObject output,List<Parameter> parameters){
    	Map<String,Object> gridMp = new HashMap<String,Object>();
    	if (parameters != null && parameters.size() > 0) {
    		gridMp.put(getToKey(RTNCODEKEY,parameters), output.getReturnCode());
    	}else{
    		gridMp.put(RTNCODEKEY, output.getReturnCode());
    	}
    	
    	List<Map<String,String>> datas = output.getBeans();
    	List<Map<String,String>> rows  = convertListMap(datas,parameters);
    	
    	//加入APP价格
    	coculateAppPrice(rows);
    	
    	gridMp.put(OMGGRID_ROWSKEY, rows);
    	
    	return JsonUtil.convertObject2Json(gridMp);
    }
	public String convertAppcar(InputObject input,OutputObject output,List<Parameter> parameters){
    	Map<String,Object> gridMp = new HashMap<String,Object>();
    	if (parameters != null && parameters.size() > 0) {
    		gridMp.put(getToKey(RTNCODEKEY,parameters), output.getReturnCode());
    	}else{
    		gridMp.put(RTNCODEKEY, output.getReturnCode());
    	}
    	
    	List<Map<String,String>> datas = output.getBeans();
    	List<Map<String,String>> rows  = convertListMap(datas,parameters);
    	
    	//加入APP信息
    	coculateAppcar(rows);
    	
    	gridMp.put(OMGGRID_ROWSKEY, rows);
    	
    	return JsonUtil.convertObject2Json(gridMp);
    }
	@SuppressWarnings("unchecked")
	private void coculateAppcar(List<Map<String,String>> rows){
		Object obj = SpringContextHelper.instance.getOscache().get(CmsServiceClient.HOME_APPS_CACHE);
		if(obj!=null){
			List<Map<String,String>> appInfos = (List<Map<String,String>>)obj;
			for(Map<String,String> row:rows){
				Map<String,String> appInfo = getAppcarInfo(appInfos,row);
				if(appInfo!=null){
					row.putAll(appInfo);
				}
			}
		}
	}
	private Map<String,String> getAppcarInfo(List<Map<String,String>> appInfos,Map<String,String> row){
		for(Map<String,String> appInfo:appInfos){
			if(appInfo.get("appID").equals(row.get("appID"))){
				return appInfo;
			}
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	private void coculateAppPrice(List<Map<String,String>> rows){
		Object obj = SpringContextHelper.instance.getOscache().get(CmsServiceClient.HOME_APPS_CACHE);
		if(obj!=null){
			List<Map<String,String>> appInfos = (List<Map<String,String>>)obj;
			for(Map<String,String> row:rows){
				Map<String,String> appInfo = getAppInfo(appInfos,row);
				if(appInfo!=null){
					String isMutiApp = appInfo.get("isMutiApp");
					String appPrice  = appInfo.get("packagePrice");
					if("1".equals(isMutiApp)){
						appPrice  = appInfo.get("threenormprice");
					}else{
						row.put("apphref", appInfo.get("apphref"));
					}
					row.put("appPrice", appPrice);
				}
			}
		}
	}
	private Map<String,String> getAppInfo(List<Map<String,String>> appInfos,Map<String,String> row){
		for(Map<String,String> appInfo:appInfos){
			if(appInfo.get("nodeUid").equals(row.get("nodeUid"))){
				return appInfo;
			}
		}
		return null;
	}
	public String mapConvert(List<Map<String,String>> datas,List<Parameter> parameters){
		try{
			Map<String,String> outMap = new HashMap<String,String>();
			if(datas!=null&&datas.size()>0){
				if (parameters != null && parameters.size() > 0) {
					outMap.putAll(convertMap(datas.get(0),parameters));
				}else{
					outMap.putAll(datas.get(0));
				}
			}else{
				outMap.put(RTNCODEKEY, ControlConstants.RTNCODE_SUC);
			}
			
			return JsonUtil.convertObject2Json(outMap);
		}catch(Exception e){
    		StringBuffer rtn = new StringBuffer();
    		rtn.append("{\""+RTNCODEKEY+"\":").append(ControlConstants.RTNCODE_ERR).append("}");
    		return rtn.toString();
    	}
	}
	
	public String listmapConvert(List<Map<String,String>> datas,List<Parameter> parameters){
		Map<String,Object> gridMp = new HashMap<String,Object>();
		
		List<Map<String,String>> rows  = convertListMap(datas,parameters);
		
		gridMp.put(OMGGRID_ROWSKEY, rows);
		return JsonUtil.convertObject2Json(gridMp);
	}
	
}
