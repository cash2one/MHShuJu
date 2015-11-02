package com.ai.eduportal.remote.freeMarker;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerUtil {
	private Logger log = LoggerFactory.getUtilLog(FreeMarkerUtil.class);
	
	private final String PREFIX = "freeMarker/ftl/";
	private Configuration configuration;
	private static final FreeMarkerUtil instance = new FreeMarkerUtil();
	public static FreeMarkerUtil getInstance(){
		return instance;
	}
	private FreeMarkerUtil(){
		init();
	}
	private void init(){
		try {
			configuration = new Configuration();
			configuration.setClassicCompatible(true);

			configuration.setTemplateLoader(new JarTemplateLoader());

			configuration.setDateFormat("yyyy-MM-dd HH:mm:ss");
			configuration.setNumberFormat("");
			configuration.setDefaultEncoding("UTF-8");
		} catch (Exception e) {
			log.error("{} init error{}.",e,"FreeMarkerUtil");
		}
	}
	
	private Template getTemplate(String name) throws IOException{
		return configuration.getTemplate(PREFIX + name,Locale.SIMPLIFIED_CHINESE);
	}
	
	public String template2Str(Object rootMap,String name){
		StringWriter writer = new StringWriter();
		try{
			Template template = getTemplate(name);
			
			template.process(rootMap, writer);
		}catch(Exception e){
			log.error("{}.{} called error{}.",e,"template2Str",name);
		}
		
		return writer.toString();
	}
}
