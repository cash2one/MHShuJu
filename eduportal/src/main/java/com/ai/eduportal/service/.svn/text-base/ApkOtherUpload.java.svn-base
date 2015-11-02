package com.ai.eduportal.service;

import java.io.File;

import com.ai.frame.util.SpringContextHelper;
import com.ai.frame.web.util.SystemPropUtil;
import com.caucho.hessian.server.HessianServlet;
import com.caucho.services.server.ServiceContext;

import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;
import common.ai.tools.IOUtil;
import common.ai.tools.StringUtil;

@SuppressWarnings("serial")
public class ApkOtherUpload extends HessianServlet implements ApkOtherUploadI {
	private Logger logger = LoggerFactory.getActionLog(this.getClass());
	private ApkpackageService apkService;
	public ApkOtherUpload() {
		this.apkService = SpringContextHelper.getBean("apkservice", ApkpackageService.class);
	}
	private static final String TMPFILENAME = "tmpFile";
	/* (non-Javadoc)
	 * @see com.ai.eduportal.service.ApkOtherUploadI#uploadApk(java.io.InputStream, java.lang.String)
	 */
	public boolean uploadApk(byte[] fin,String fileType){
		String clientIP = ServiceContext.getContextRequest().getRemoteAddr();
		logger.info(StringUtil.trim(clientIP,"unknown"), "-----------from the client ip {} called the uploadApk-----------.");
		
		String fileName = null;
		try {
			if(!StringUtil.isEmpty(clientIP) && apkService.isInLocalIps(StringUtil.trim(clientIP))){
			}else{
				return false;
			}
			
			String uploadFilePath = SystemPropUtil.getString("upload.file.path");
			fileName = CmsServiceClient.EDU_APP_BASEDIR + File.separator + uploadFilePath + File.separator;
			File fileBase   = new File(fileName);
			if(!fileBase.exists()){
				fileBase.mkdirs();
			}
			
			fileName += TMPFILENAME + StringUtil.getUUID() + fileType;
			
			IOUtil.bytes2File(fin, fileName, IOUtil.BUFFER);
		
			logger.info(clientIP, "{} called uploadApk success.");
			return apkService.processApkZip(fileName);
		} catch (Exception e) {
			logger.error("{} called the uploadApk method is error:{}", e, StringUtil.trim(clientIP,"unknown"));
			return false;
		}finally{
			if(!StringUtil.isEmpty(fileName) && fileName.endsWith(fileType)){
				apkService.remoteFile(fileName);
			}
		}
	}
}
