package com.ai.eduportal.remote.freeMarker;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;

import freemarker.cache.TemplateLoader;

public class JarTemplateLoader implements TemplateLoader{
	private Logger log = LoggerFactory.getUtilLog(JarTemplateLoader.class);
	private ThreadLocal<InputStream> tplResourceStream = new ThreadLocal<InputStream>();
	public JarTemplateLoader(){}
	private InputStream getTplResourceStream(){
		return tplResourceStream.get();
	}
	public void closeTemplateSource(Object templateSource) throws IOException {
		//log.info(templateSource, "----closeTemplateSource-------{}");
		Resource templateResource = (Resource)templateSource;
		templateResource.closeCloseable(getTplResourceStream());
		
		tplResourceStream.remove();
	}

	public Object findTemplateSource(String name) throws IOException {
		//System.out.println("----findTemplateSource-------"+name);
		log.info(name, "----findTemplateSource-------{}");
		if(name!=null && name.length()>0){
			if(name.indexOf("_zh_CN")>-1){
				name = name.replace("_zh_CN", "");
			}
			//System.out.println("----findTemplateSource-------"+name);
			log.info(name, "----findTemplateSource1-------{}");
			Resource templateResource = new ClassPathResource(name);
			return templateResource;
		}
		return null;
	}

	public long getLastModified(Object templateSource) {
		return 0;
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		InputStream inputStream = tplResourceStream.get();
		//System.out.println("----getReader-------"+inputStream);
		if(inputStream == null){
			Resource templateResource = (Resource)templateSource;
			inputStream = templateResource.getInputStream();
			
			tplResourceStream.set(inputStream);
		}
		
		return new InputStreamReader(inputStream,encoding);
	}
}
