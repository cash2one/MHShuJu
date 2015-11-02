package com.ai.eduportal.action.ssoutil;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import common.ai.tools.StringUtil;

public class CookiesUtil {
	public static final String COOKIES_CMTOKENID = "cmtokenid";
	
	private Map<String,String> cookies = new HashMap<String,String>();
	public CookiesUtil(HttpServletRequest req){
		Cookie cookies[] = req.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				String cookieName  = cookies[i].getName();
				String cookieValue = cookies[i].getValue();
				if(!StringUtil.isEmpty(cookieName)){
					this.cookies.put(cookieName, StringUtil.trim(cookieValue));
				}
			}
		}
	}
	public String getCookieVal(String cookieName){
		return cookies.get(cookieName);
	}
}
