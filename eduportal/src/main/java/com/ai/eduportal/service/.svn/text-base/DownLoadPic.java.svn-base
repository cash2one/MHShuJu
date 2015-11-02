package com.ai.eduportal.service;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import common.ai.httpclient.tools.HttpClientHelper;
import common.ai.tools.IOUtil;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

public class DownLoadPic {
	public static final String APP_APPPIC     = "appPic";
	public static final String APP_NODEUID    = "nodeUid";
	public static final String APP_APPMXPIC   = "appMxPic";
	public static final String APP_APPDESCPIC = "appDescPic";
	public static final String APP_ADVERPIC   = "adverPic";
	public static final String APP_PRICETABLE   = "priceTable";
	public static final String APP_APPDOWN      = "appdown";

	private HttpClientHelper httpClient;
	private String cmsPath;
	/**从cms下载图片到本地*/
	@SuppressWarnings("unchecked")
	public void downloadPicFromCms(String baseDir,List<Map<String,Map<String,String>>> picUrls,CmsServiceClient cmsClient){
		if(picUrls!=null){
			for(Map<String,Map<String,String>> picMap:picUrls){
				Iterator<String> urlKeys = picMap.keySet().iterator();
				while(urlKeys.hasNext()){
					String nodeUid = urlKeys.next();
					String binary  = cmsClient.getNodeBinaryByUid(nodeUid);
					Map<String,Object> appDetailMp = JsonUtil.convertJson2Object(binary, Map.class);
					List<Map<String,String>> appDetailLs = (List<Map<String,String>>)appDetailMp.get("rows");
					if(appDetailLs.size()>0){
						Map<String,String> binaMp = appDetailLs.get(0);
						
						String appPic     = binaMp.get(APP_APPPIC);
						String appMxPic   = binaMp.get(APP_APPMXPIC);
						String appDescPic = binaMp.get(APP_APPDESCPIC);
						String adverPic   = binaMp.get(APP_ADVERPIC);
						String priceTable   = binaMp.get(APP_PRICETABLE);
						String appdown    = binaMp.get(APP_APPDOWN);
						Map<String,String> picUrl = picMap.get(nodeUid);
						// 下载图片到本地
						if(!StringUtil.isEmpty(appPic)){
							String appPicDir = getPicdir(baseDir,picUrl.get(APP_APPPIC));
							IOUtil.inputStream2File(IOUtil.str2InputStream(appPic), appPicDir);
						}
						
						if(!StringUtil.isEmpty(appMxPic)){
							String appPicDir = getPicdir(baseDir,picUrl.get(APP_APPMXPIC));
							IOUtil.inputStream2File(IOUtil.str2InputStream(appMxPic), appPicDir);
						}
						
						if(!StringUtil.isEmpty(appDescPic)){
							String appPicDir = getPicdir(baseDir,picUrl.get(APP_APPDESCPIC));
							IOUtil.inputStream2File(IOUtil.str2InputStream(appDescPic), appPicDir);
						}
						
						if(!StringUtil.isEmpty(adverPic)){
							String appPicDir = getPicdir(baseDir,picUrl.get(APP_ADVERPIC));
							IOUtil.inputStream2File(IOUtil.str2InputStream(adverPic), appPicDir);
						}
						
						if(!StringUtil.isEmpty(priceTable)){
							String priceTableDir = getPicdir(baseDir,picUrl.get(APP_PRICETABLE));
							IOUtil.inputStream2File(IOUtil.str2InputStream(priceTable), priceTableDir);
						}
						
						if(!StringUtil.isEmpty(appdown)){
							String appdownDir = getPicdir(baseDir,picUrl.get(APP_APPDOWN));
							IOUtil.inputStream2File(IOUtil.str2InputStream(appdown), appdownDir);
						}
					}
				}
			}
		}
	}
	/**从cms下载图片到本地*/
	public void downloadPicFromCms(String baseDir,List<String> picUrls){
		if(picUrls!=null){
			for(String picUrl:picUrls){
				String picPath = getPicdir(baseDir,picUrl);
				if(StringUtil.isEmpty(picPath))continue;
				String pichttpUrl = cmsPath;
				if(picUrl.startsWith(".//")){
					pichttpUrl = cmsPath + picUrl.substring(2);
				}else if(picUrl.startsWith("./")){
					pichttpUrl = cmsPath + picUrl.substring(1);
				}else if(picUrl.startsWith("/")){
					pichttpUrl = cmsPath + picUrl;
				}else{
					pichttpUrl = cmsPath + "/" + picUrl;
				}
				
				httpClient.getFileFromRemote(pichttpUrl, picPath);
			}
		}
	}
	
	private String getPicdir(String baseDir,String picUrl){
		if(!StringUtil.isEmpty(picUrl) && picUrl.lastIndexOf("/")>-1){
			String prePath = picUrl.substring(0,picUrl.lastIndexOf("/"));
			prePath = prePath.replace("\\", "/");
			if(prePath.startsWith(".//")){
				prePath = prePath.substring(2);
			}else if(prePath.startsWith("./")){
				prePath = prePath.substring(1);
			}
			
			prePath = prePath.replace("/", File.separator);
			String basePath = baseDir;
			if(prePath.startsWith(File.separator)){
				basePath = baseDir + prePath;
			}else{
				basePath = baseDir + File.separator + prePath;
			}
			File dirFile = new File(basePath);
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			
			return basePath + File.separator + picUrl.substring(picUrl.lastIndexOf("/")+1);
		}
		return null;
	}
	
	public HttpClientHelper getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClientHelper httpClient) {
		this.httpClient = httpClient;
	}

	public String getCmsPath() {
		return cmsPath;
	}

	public void setCmsPath(String cmsPath) {
		this.cmsPath = cmsPath;
	}
}
