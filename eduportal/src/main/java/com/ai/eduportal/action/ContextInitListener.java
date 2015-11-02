package com.ai.eduportal.action;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletContextEvent;

import com.ai.eduportal.service.CmsServiceClient;
import com.ai.frame.util.SpringContextHelper;
import com.ai.frame.web.ContextLoaderListener;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;

public class ContextInitListener extends ContextLoaderListener{
	private final Logger logger = LoggerFactory.getActionLog(this.getClass());
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		
		final CmsServiceClient cmsService = SpringContextHelper.getBean("cmsClient", CmsServiceClient.class);
		final String baseDir = event.getServletContext().getRealPath("/");
		
		CmsServiceClient.EDU_APP_BASEDIR = baseDir;
		logger.info("contextInitialized", "※---------{} init---> get cms homeTops start.");
		new Thread(new Runnable(){
			public void run() {
				try{
					cmsService.getHomeTops(baseDir,new ArrayList<Map<String,String>>());
					logger.info("contextInitialized", baseDir,"※---------{} init---> get cms homeTops End the base dir is {}.");
				}catch(Exception e){
					logger.error("※---------{} init error:{}", e, "contextInitialized");
				}
			}
		}).start();
		
	}
}
