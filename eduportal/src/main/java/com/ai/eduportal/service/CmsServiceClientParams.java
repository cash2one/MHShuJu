package com.ai.eduportal.service;

import org.apache.commons.httpclient.NameValuePair;

public class CmsServiceClientParams {
    public static final String QUERYTYPE    = "1";
    public static final String QUERYBYXPATH = "2";
    public static final String ORDER_DESC = "descending";
    public static final String ORDER_ASC  = "ascending";
    private String serviceUid = "outerGetNodeDetailPropValByCondition";
    private String binServiceUid = "outerGetNodeBinaryPropContentByCondition";
//    private String queryByPageServiceId = "outerGetNodesWithPage";
    private String serviceQueryType = QUERYBYXPATH;
    
    public static final int OFFLINE   = 2;
    public static final int ONLINE    = 1;
    public static final int PREONLINE = 3;
    
    private String queryBasecondition = "//*[@nodeName=\"EDUPORTAL\"]/*[@nodeName=\"appResourcesCmsNode\"]";

    public void setServiceUid(String serviceUid) {
        this.serviceUid = serviceUid;
    }
    public void setQueryBasecondition(String queryBasecondition) {
        this.queryBasecondition = queryBasecondition;
    }
    /**取得结点二进制属性*/
    public NameValuePair[] getNodeBinaryByUid(String nodeUid) {
		NameValuePair[] data = new NameValuePair[] {
				new NameValuePair("uid", binServiceUid),
				new NameValuePair("condition", nodeUid),
				new NameValuePair("queryType", QUERYTYPE) };
		return data;
	}
//    public void setQueryByPageServiceId(String queryByPageServiceId) {
//        this.queryByPageServiceId = queryByPageServiceId;
//    }
    /**广告*/
	public NameValuePair[] getHomeAdvertising(int state) {
		String conditions = queryBasecondition
				+ "/*[@folderFlg=\"Advertising\"]/*[@adverState=\""+state+"\"] order by @order ascending";
		
		NameValuePair[] data = new NameValuePair[] {
				new NameValuePair("uid", serviceUid),
				new NameValuePair("condition", conditions),
				new NameValuePair("queryType", serviceQueryType) };
		return data;
	}
	/**二级菜单*/
	public NameValuePair[] getActivSecMenu(int state) {
		String conditions = queryBasecondition
				+ "/*[@folderFlg=\"homeMenu\"]/*[@folderFlg=\"secondMenu\"]/*[@menuFlg=\""+state+"\" and @isActive='1']";
		
		NameValuePair[] data = new NameValuePair[] {
				new NameValuePair("uid", serviceUid),
				new NameValuePair("condition", conditions),
				new NameValuePair("queryType", serviceQueryType) };
		return data;
	}
	public NameValuePair[] getSecMenu(int state,String menuTypes) {
		String conditions = queryBasecondition
				+ "/*[@folderFlg=\"homeMenu\"]/*[@folderFlg=\"secondMenu\"]/*[@menuFlg=\""+state+"\" and (jcr:contains(@menuDomain,'"+menuTypes+"'))]";
		
		NameValuePair[] data = new NameValuePair[] {
				new NameValuePair("uid", serviceUid),
				new NameValuePair("condition", conditions),
				new NameValuePair("queryType", serviceQueryType) };
		return data;
	}
	/**二级菜单*/
	public NameValuePair[] getHomeSecMenu(int state) {
		String conditions = queryBasecondition
				+ "/*[@folderFlg=\"homeMenu\"]/*[@folderFlg=\"secondMenu\"]/*[@menuFlg=\""+state+"\"] order by @order ascending";
		
		NameValuePair[] data = new NameValuePair[] {
				new NameValuePair("uid", serviceUid),
				new NameValuePair("condition", conditions),
				new NameValuePair("queryType", serviceQueryType) };
		return data;
	}
	/**快报*/
	public NameValuePair[] getHomeExpress(int state) {
		String conditions = queryBasecondition
				+ "/*[@folderFlg=\"express\"]/*[@expressFlg=\""+state+"\"]";
		
		NameValuePair[] data = new NameValuePair[] {
				new NameValuePair("uid", serviceUid),
				new NameValuePair("condition", conditions),
				new NameValuePair("queryType", serviceQueryType) };
		return data;
	}
	/**单应用*/
	public NameValuePair[] getSingleApp(int state) {
		String conditions = queryBasecondition
				+ "/*[@folderFlg=\"singlePackge\"]/*[@packageState=\""+state+"\"] order by @order descending";
		
		NameValuePair[] data = new NameValuePair[] {
				new NameValuePair("uid", serviceUid),
				new NameValuePair("condition", conditions),
				new NameValuePair("queryType", serviceQueryType) };
		return data;
	}
//	public NameValuePair[] getAppDetail(String nodeUid) {
////		String conditions = queryBasecondition
////				+ "/*[@folderFlg=\"singlePackge\"]/*[@packageState=\""+state+"\"]";
//		
//		NameValuePair[] data = new NameValuePair[] {
//				new NameValuePair("uid", serviceUid),
//				new NameValuePair("condition", nodeUid),
//				new NameValuePair("queryType", QUERYTYPE) };
//		return data;
//	}
	public NameValuePair[] getNodeByUid(String nodeUid) {
		NameValuePair[] data = new NameValuePair[] {
				new NameValuePair("uid", serviceUid),
				new NameValuePair("condition", nodeUid),
				new NameValuePair("queryType", QUERYTYPE) };
		return data;
	}
	/**组合应用*/
	public NameValuePair[] getMutiApp(int state) {
		String conditions = queryBasecondition
				+ "/*[@folderFlg=\"muitiPackage\"]/*[@packageState=\""+state+"\"] order by @order descending";
		
		NameValuePair[] data = new NameValuePair[] {
				new NameValuePair("uid", serviceUid),
				new NameValuePair("condition", conditions),
				new NameValuePair("queryType", serviceQueryType) };
		return data;
	}
}
