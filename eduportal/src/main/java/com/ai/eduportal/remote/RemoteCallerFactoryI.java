package com.ai.eduportal.remote;

public interface RemoteCallerFactoryI {
	int TYPE_XXT    = 1;
	int TYPE_TBKT   = 2;
	int TYPE_ZXXXXB = 3;
	
	/**校讯通接口调用   @param serialNo 请求调用接口时的序列号**/
	public String getXxtResponse(String phone, String serialNo);
	/**学习报接口调用   @param serialNo 请求调用接口时的序列号**/
	public String getXxbResponse(String phone, String serialNo);
	/**同步课堂接口调用 @param serialNo 请求调用接口时的序列号**/
	public String getTbkResponse(String phone, String serialNo);

}