package com.ai.eduportal.convertor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import com.ai.frame.bean.InputObject;
import com.ai.frame.bean.OutputObject;
import com.ai.frame.web.convertor.CommonConvert;
import com.ai.frame.web.xml.bean.Parameter;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;
import common.ai.tools.JsonUtil;

public class EduPlatformConvert extends CommonConvert{
	private static String BEANSKEY = "beans";
	private static String BEANKEY  = "bean";
	private static String RTNCODE  = "0";
	private Logger logger = LoggerFactory.getUtilLog(CommonConvert.class);
	
	private static ObjectMapper mapper = new ObjectMapper();
	static{
		mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	public String convert(OutputObject rtnBean,List<Parameter> parameters){
		String rtnjson = rtnBean.getReturnMessage();
		
		if(RTNCODE.equals(rtnBean.getReturnCode())){
			try {
				ObjectNode  root    = mapper.createObjectNode();
				
				JsonNode bodyNode   = mapper.readTree(rtnjson);
				if (parameters != null && parameters.size() > 0) {
					root.put(getToKey(RTNCODEKEY,parameters), rtnBean.getReturnCode());
					root.put(getToKey(BEANKEY,parameters), bodyNode);
				}else{
					root.put(RTNCODEKEY, rtnBean.getReturnCode());
					root.put(BEANKEY, bodyNode);
				}
				
				return mapper.writeValueAsString(root);
			} catch (Exception e) {
				logger.error("read and write json str[{}] error:{}", e, rtnjson);
			} 
			
		}
		
		Map<String,Object> rtnmap = new HashMap<String,Object>();
		if (parameters != null && parameters.size() > 0) {
			rtnmap.put(getToKey(RTNCODEKEY,parameters), rtnBean.getReturnCode());
			rtnmap.put(getToKey(RTNMSGKEY, parameters), rtnBean.getReturnMessage());
			rtnmap.put(getToKey(BEANKEY, parameters), null);
		}else{
			rtnmap.put(RTNCODEKEY, rtnBean.getReturnCode());
			rtnmap.put(RTNMSGKEY,  rtnBean.getReturnMessage());
			rtnmap.put(BEANKEY,  null);
		}
		
		String rtnJson = JsonUtil.convertObject2Json(rtnmap);
		return rtnJson;
		
	}
	public String convertMp(OutputObject output,List<Parameter> parameters){
		
		return convertSingleData(null,output,parameters);
	}
	/**转换beans属性里的字段*/
	public String convertBeans(OutputObject output,List<Parameter> parameters){
		Map<String,Object> gridMp = new HashMap<String,Object>();
		
		addRtnInfo(gridMp,output,parameters);
		addBeansInfo(gridMp,output,parameters);
		
		return JsonUtil.convertObject2Json(gridMp);
	}
	/**转换beans属性里的字段*/
	public String convertBeans(InputObject in,OutputObject output,List<Parameter> parameters){
		Map<String,Object> gridMp = new HashMap<String,Object>();
		
		addRtnInfo(gridMp,output,parameters);
		addBeansInfo(gridMp,output,parameters);
		
		return JsonUtil.convertObject2Json(gridMp);
	}
	/**转换bean属性里的字段*/
	public String convertBean(OutputObject output,List<Parameter> parameters){
		Map<String,Object> gridMp = new HashMap<String,Object>();
		
		addRtnInfo(gridMp,output,parameters);
		addBeanInfo(gridMp,output,parameters);
		
		return JsonUtil.convertObject2Json(gridMp);
	}
	/**转换bean和beans属性里的字段*/
	public String convertAllBean(OutputObject output,List<Parameter> parameters){
		Map<String,Object> gridMp = new HashMap<String,Object>();
		
		addRtnInfo(gridMp,output,parameters);
		addBeanInfo(gridMp,output,parameters);
		addBeansInfo(gridMp,output,parameters);
		
		return JsonUtil.convertObject2Json(gridMp);
	}
	//convert returnCode和returnMessage字段
	protected void addRtnInfo(Map<String,Object> gridMp,OutputObject output,List<Parameter> parameters){
		if (parameters != null && parameters.size() > 0) {
    		gridMp.put(getToKey(RTNCODEKEY,parameters), output.getReturnCode());
    		gridMp.put(getToKey(RTNMSGKEY,parameters),  output.getReturnMessage());
    	}else{
    		gridMp.put(RTNCODEKEY, output.getReturnCode());
    		gridMp.put(RTNMSGKEY,  output.getReturnMessage());
    	}
	}
	protected void addBeansInfo(Map<String,Object> gridMp,OutputObject output,List<Parameter> parameters){
		//beans convert
		List<Map<String,String>> datas = output.getBeans();
    	List<Map<String,String>> rows  = convertListMap(datas,parameters);
    	
    	gridMp.put(getToKey(BEANSKEY,parameters), rows);
	}
	protected void addBeanInfo(Map<String,Object> gridMp,OutputObject output,List<Parameter> parameters){
		//bean convert
		Map<String,String> rtnMap = convertMap(output.getBean(),parameters);
		gridMp.put(getToKey(BEANKEY,parameters), rtnMap);
	}
	protected void addMpInfo(Map<String,Object> gridMp,OutputObject output,List<Parameter> parameters){
		//bean convert
		Map<String,String> rtnMap = convertMap(output.getBean(),parameters);
		gridMp.putAll(rtnMap);
	}
}
