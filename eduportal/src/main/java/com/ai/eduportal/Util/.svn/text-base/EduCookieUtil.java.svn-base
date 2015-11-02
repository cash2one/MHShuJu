package com.ai.eduportal.Util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.ai.eduportal.bean.User;
import com.ai.frame.web.util.SystemPropUtil;
import common.ai.tools.StringUtil;

public class EduCookieUtil {
	public static void clearLoginInfos(HttpSession session){
		session.removeAttribute(User.LOGIN_USER);
		session.removeAttribute(User.CURRENT_USER);
		session.removeAttribute(User.LOGINTOKEN);
		session.removeAttribute(User.TMPLOGINU);
		session.removeAttribute(User.USER_LIST);
		session.removeAttribute(User.PARENTLOGINU);
		session.removeAttribute(User.TEACHLOGINU);

		session.invalidate();
	}
	public static boolean canLogin(String referer){
		if(StringUtil.isEmpty(referer)){
			return false;
		}
		String tmp = referer;
		if(referer.indexOf("http://")>-1){
			tmp = referer.substring(7);
		}else if(referer.indexOf("https://")>-1){
			tmp = referer.substring(8);
		}
		int first = tmp.indexOf("/");
		String ipports = tmp;
		if(first > -1){
			ipports  = tmp.substring(0,first);
		}
		
		String canLoginIps = SystemPropUtil.getString("edu.login.hosts");
		
		if(!StringUtil.isEmpty(canLoginIps)){
			String[] loginIps = canLoginIps.split(",");
			for(String loginIp:loginIps){
				if(ipports.indexOf(loginIp)>-1){
					return true;
				}
			}
			return false;

		}else{
			return true;
		}
	}
	/**
	 * 获取单个COOKIES
	 * 
	 * @param req
	 * @param resp
	 * @param cookieName
	 * @return
	 */
	public static String getCookieValue(HttpServletRequest req, HttpServletResponse resp, String cookieName) {
		Cookie cookies[] = req.getCookies();
		Cookie sCookie = null;
		String scookName;
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				sCookie = cookies[i];
				scookName = sCookie.getName();
				if (cookieName.equals(scookName)) {
					return sCookie.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * 设置单个COOKIE
	 * 
	 * @param req
	 * @param resp
	 * @param cookieName
	 * @param cookieValue
	 * @param time
	 * @param domain
	 */
	public static void setSingleCookie(HttpServletRequest req, HttpServletResponse resp, String cookieName, String cookieValue, int time, String domain) {
		Cookie cookie = new Cookie(cookieName, null);
		if (time != 0) {
			cookie.setMaxAge(time);
		} else {
			cookie.setMaxAge(-1);
		}
		// cookie.setMaxAge(time);
		cookie.setPath("/");
		cookie.setValue(cookieValue);
		if (!"".equals(domain)) {
			cookie.setDomain(domain);
		}
		resp.addCookie(cookie);
	}

	public static void removeSingleCookie(HttpServletRequest req, HttpServletResponse resp, String cookieName) {
		Cookie cookie = null;
		cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		// cookie.setDomain(".ha.10086.cn");
		resp.addCookie(cookie);
	}
	
	public static String getCurrentURI(HttpServletRequest request) {
		String queryString = request.getQueryString();
		if (StringUtils.isBlank(queryString)) {
			queryString = "";
		} else {
			// 2012-08-20 跨脚本漏洞修复 start
			queryString = escapeParamString(queryString);
			// 2012-08-20 跨脚本漏洞修复 end
			queryString = "?" + queryString;
		}
		String currentURI = request.getRequestURI() + queryString;
		return currentURI;
	}
	
	public static String escapeParamString(String value) {
		
		if (value == null)
			return "";
		
		value = StringUtils.replace(value, "|", "");
		value = StringUtils.replace(value, "&amp;", "");
		value = StringUtils.replace(value, ";", "");
		value = StringUtils.replace(value, "$", "");
		value = StringUtils.replace(value, "%", "");
		value = StringUtils.replace(value, "@", "");
		value = StringUtils.replace(value, "'", "");
		value = StringUtils.replace(value, "\"", "");
		value = StringUtils.replace(value, "\\'", "");
		value = StringUtils.replace(value, "&lt;", "");
		value = StringUtils.replace(value, "&gt;", "");
		value = StringUtils.replace(value, "<", "");
		value = StringUtils.replace(value, ">", "");
		value = StringUtils.replace(value, "(", "");
		value = StringUtils.replace(value, ")", "");
		value = StringUtils.replace(value, "+", "");
		value = StringUtils.replace(value, "\n", "");
		value = StringUtils.replace(value, "\r", "");
		value = StringUtils.replace(value, ",", "");
		value = StringUtils.replace(value, "\\", "");
		
		return value;
	}
}
