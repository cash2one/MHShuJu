package com.ai.eduportal.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.types.PatternSet;
import org.springframework.beans.factory.InitializingBean;

import com.ai.eduportal.interceptor.InputObjectAdpter;
import com.ai.frame.bean.InputObject;
import com.ai.frame.web.util.SystemPropUtil;
import com.caucho.hessian.client.HessianProxyFactory;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;
import common.ai.tools.IOUtil;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

public class ApkpackageService implements InitializingBean{
	private Logger logger = LoggerFactory.getActionLog(this.getClass());
	private static final String UPDATEFILE = "update.json";
	private String apkUploadUrls;
	public void setApkUploadUrls(String apkUploadUrls) {
		this.apkUploadUrls = apkUploadUrls;
	}
	private List<String> apkUploads = new ArrayList<String>();
	private List<String> localIps   = new ArrayList<String>();
	
	public boolean isInLocalIps(String ip){
		logger.info(String.valueOf(localIps.size()), "--->>the local host ip size is {}----->>");
		return localIps.contains(ip);
	}
	public void addIp2LocalIps(String ipport){
		if(!StringUtil.isEmpty(ipport)){
			String [] tmp = ipport.split(":");
			localIps.add(StringUtil.trim(tmp[0]));
		}
	}
	public void afterPropertiesSet() throws Exception {
		if(StringUtil.isEmpty(apkUploadUrls))return;
		String tmp = apkUploadUrls.substring(7);
		int first = tmp.indexOf("/");
		
		String ipports  = tmp.substring(0,first);
		String services = tmp.substring(first);
		
		String localIp  = StringUtil.getLocalHostIp();
		logger.info(localIp, "---the local host ip is {}-----");
		String [] iparr = ipports.split(";");
		for(String ipport:iparr){
			if(!ipport.startsWith(localIp)){
				addIp2LocalIps(ipport);
				apkUploads.add("http://" + ipport + services);
			}
		}
		
		logger.info(localIp,"---------{} afterPropertiesSet END---------");
	}
	public int uploadApk2OtherServers(String serverUrl,File zipFile){
		try {
			HessianProxyFactory factory = new HessianProxyFactory();
			factory.setConnectTimeout(StringUtil.str2Long(SystemPropUtil.getString("webservice.connections.timeout"), 5000));
			factory.setReadTimeout(StringUtil.str2Long(SystemPropUtil.getString("webservice.http.timeout"), 5000));
			
			ApkOtherUploadI apkservice  = (ApkOtherUploadI)factory.create(ApkOtherUploadI.class, serverUrl);
			boolean rtn = apkservice.uploadApk(IOUtil.file2bytes(zipFile, IOUtil.BUFFER), ".zip");
			if(rtn)return 200;
		} catch (Throwable e) {
			return 500;
		}
		return 500;
		
	}
	/**
	 * 通过Hessian方式上传包到其他服务器上
	 * @param zipFileName
	 */
	public void uploadApk2OtherServers(String zipFileName){
		if(apkUploads.size()>0){
			for(String apkUploadUrl:apkUploads){
				int rtn = uploadApk2OtherServers(apkUploadUrl,new File(zipFileName));
				logger.info("--------uploadApk to {} 's return code is {}-----------", null, apkUploadUrl, String.valueOf(rtn));
			}
		}
	}
	
	//由其他服务器上传过来的包处理,直接解压不用验证
	public boolean processApkZip(String fileName) throws Exception{
		String uploadFilePath = SystemPropUtil.getString("upload.file.path");
		File apkzipFile = new File(fileName);
		try{
			if(apkzipFile!=null && apkzipFile.exists() && analyseZip(fileName)){
				// 解压
				String zipdir = CmsServiceClient.EDU_APP_BASEDIR + File.separator + uploadFilePath + File.separator + "zip" + File.separator;
				File zipd = new File(zipdir);
				if(!zipd.exists()){
					zipd.mkdirs();
				}
				unZipFile(fileName,zipdir);
				
				return true;
			}
		}finally{
			remoteFile(fileName);
		}
		return false;
	}

	public boolean processApkZip(InputObject orinputObj) throws Exception{
		InputObjectAdpter inputObj = new InputObjectAdpter(orinputObj);
		
		String uploadFilePath = SystemPropUtil.getString("upload.file.path");
		
		String fileName = CmsServiceClient.EDU_APP_BASEDIR + File.separator + uploadFilePath + File.separator;
		File fileBase   = new File(fileName);
		if(!fileBase.exists()){
			fileBase.mkdirs();
		}
		
		fileName += orinputObj.getFileName(0) + StringUtil.getUUID() + orinputObj.getFileType(0);
		
		IOUtil.bytes2File(IOUtil.file2bytes(inputObj.getFile(0), IOUtil.BUFFER), fileName, IOUtil.BUFFER);
		try{
			//分析zip包内容
			if(analyseZip(fileName)){
				//解压
				String zipdir = CmsServiceClient.EDU_APP_BASEDIR + File.separator + uploadFilePath + File.separator + "zip" + File.separator;
				File zipd = new File(zipdir);
				if(!zipd.exists()){
					zipd.mkdirs();
				}
				unZipFile(fileName,zipdir);
				
				//上传到其他服务器
				uploadApk2OtherServers(fileName);
				
				return true;
			}
		}catch(Exception e){
			throw new Exception(e);
		}finally{
			remoteFile(fileName);
		}
		
		return false;
	}
	protected void remoteFile(String fileName){
		logger.info(fileName, "=====> delete file {} start...");
		File tmpF = new File(fileName);
		if(tmpF.exists()){
			if(!tmpF.delete()){
				System.gc();
				tmpF.delete();
			}
			
		}
	}
	public void unZipFile(String zipFile,String destdir){
		unZipFile(zipFile,destdir,"**");
	}
	public void unZipFile(String zipFile,String destdir,String includes){
		
		Project prj = new Project();
		prj.setName("ant's api");
		prj.setBaseDir(new File(System.getProperty("user.dir")));
		
		Expand expand = new Expand();
        expand.setProject(prj);
        expand.setSrc(new File(zipFile));
        expand.setOverwrite(true);
        expand.setDest(new File(destdir));
        
        PatternSet pset = new PatternSet();
        pset.setIncludes(includes);
        expand.addPatternset(pset);
        expand.execute();
	}
	
	/**
	 * 判断上传包的信息
	 * @param appid    apk ID
	 * @param version  apk 版本
	 * @return 0: 服务器上还没有apk包,
	 *         -1:上传的包的id与服务器要求不一致,
	 *          1:上传更新包合格,
	 *          2:上传更新包版本比服务器上的版本低。
	 */
	@SuppressWarnings("unchecked")
	public int checkApkInfo(String appid,String version){
		String uploadFilePath = SystemPropUtil.getString("upload.file.path");
		String jsonappid = SystemPropUtil.getString("edu.apk.appid");
		
		String fileName = CmsServiceClient.EDU_APP_BASEDIR + File.separator + uploadFilePath + File.separator + "zip" + File.separator + UPDATEFILE;
		
		File upjsonFile = new File(fileName);
		if(upjsonFile.exists()){
			String upjson = IOUtil.inputStream2String(IOUtil.file2InputStream(upjsonFile));
			Map<String,Object> upmp = JsonUtil.convertJson2Object(upjson, Map.class);
			
			Map<String,String> android = (Map<String,String>)upmp.get("Android");
			String androidVer= StringUtil.trim(android.get("version"));
			
			
			if(appid.equals(jsonappid)){
				
				if(version.compareTo(androidVer) > 0){
					return 1;
				}else{
					return 2;
				}
			}else{
				return -1;
			}
		}else{
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	public boolean analyseZip(String zipName) throws Exception{
		ZipFile     zf      = null;
		InputStream in      = null;
		FileInputStream fin = null;
		ZipInputStream  zin = null;

		try {
			zf = new ZipFile(zipName);
			fin= new FileInputStream(zipName);
			in = new BufferedInputStream(fin);
			zin= new ZipInputStream(in);
			
			ZipEntry ze;
			while ((ze = zin.getNextEntry()) != null) {
				if (ze.isDirectory()) {
					continue;
				}else{
					String fname = ze.getName();
					long size    = ze.getSize();
//					System.out.println("----zip entry name:--------"+fname);
					if (UPDATEFILE.equals(fname) && size > 0) {
						//String upjson = IOUtil.inputStream2String(zf.getInputStream(ze));
						String upjson = new String(IOUtil.inputStream2Bytes(zf.getInputStream(ze), 1024),"UTF-8");
						Map<String,Object> upmp = JsonUtil.convertJson2Object(upjson, Map.class);
						if(upmp == null){
							throw new Exception("上传包的update.json文件读取失败.");
						}
						Map<String,String> android = (Map<String,String>)upmp.get("Android");
						String appid = StringUtil.obj2TrimStr(upmp.get("appid"));
						String androidVer= StringUtil.trim(android.get("version"));
						
						ZipEntry apkzip = zf.getEntry(appid+".apk");
						if(apkzip !=null && apkzip.getSize() > 0){
						}else{
							throw new Exception("上传包的内容不符合标准.");
						}
						
						String jsonappid = SystemPropUtil.getString("edu.apk.appid");
						int checknum = checkApkInfo(appid,androidVer);
						if(checknum == 0 && jsonappid.equals(appid)){
							return true;
						}else if(checknum == 0 || checknum == -1){
							throw new Exception("上传包的ID与服务器要求不一致.");
						}else if(checknum == 2){
							throw new Exception("上传包的版本比服务器现在的版本低.");
						}else if(checknum == 1){
							return true;
						}
						break;
					}else{
						continue;
					}
				}
			}
			return false;
		} catch (Exception e) {
			throw new Exception(e);
		}finally{
			IOUtil.closeInputStream(zin);
			IOUtil.closeInputStream(fin);
			IOUtil.closeInputStream(in);
			
			try{
				if(zf !=null){
					zf.close();
				}
			}catch(Exception e){}
		}
	}
}
