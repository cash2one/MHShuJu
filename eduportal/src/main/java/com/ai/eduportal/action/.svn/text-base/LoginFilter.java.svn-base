package com.ai.eduportal.action;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.eduportal.Util.EduCookieUtil;
import com.ai.eduportal.bean.User;
import com.ai.frame.util.SpringContextHelper;
import com.ai.frame.web.util.SystemPropUtil;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

public class LoginFilter implements Filter {
	private String[] filterUrls;
	public void destroy() {
		filterUrls = null;
	}
	private boolean needFilter(String serverletPath){
		if(filterUrls!=null){
			for(String filterUrl:filterUrls){
				if(serverletPath.indexOf(filterUrl)>-1){
					return true;
				}
			}
		}
		return false;
	}
	private String getRelayState(HttpServletRequest httpReq){
		String relayState="http://www.ha.10086.cn/edu/module/turnIndex.html";
		String cacheKey1 = httpReq.getParameter("cacheKey");
		if(!StringUtil.isEmpty(cacheKey1)){
			relayState = relayState + "?cacheKey="+cacheKey1;
			try{
				relayState = URLEncoder.encode(relayState, "UTF-8");
			}catch(Exception e){
				// ignore
			}
		}
		return relayState;
	}
	/**
	 * 单点登录判断
	 * @throws IOException 
	 */
//	private boolean checksso(Object user, String requestURL,HttpServletRequest httpReq,HttpServletResponse httpResp) throws IOException {
//		String cmtokenid= MyService.getCookieValue(httpReq, httpResp, "cmtokenidHeNan");
////		String referer  = httpReq.getHeader("Referer");
//		Object tmpLogin = httpReq.getSession().getAttribute(User.TMPLOGINU);
//		if(tmpLogin!=null && tmpLogin instanceof User){
//			return true;
//		}
//		
//		if (StringUtil.isEmpty(cmtokenid)) {
//			httpReq.getSession().removeAttribute(User.LOGIN_USER);
//			httpReq.getSession().removeAttribute(User.CURRENT_USER);
//			httpReq.getSession().removeAttribute(User.USER_LIST);
//			MyService.truncateSsoCookie(httpReq, httpResp);
//		}else{
//			//判断cmtokenid和UID是否一致
//			String UID= MyService.getCookieValue(httpReq, httpResp, "UID");	
//			String redirectUrl = "";
//			if(!MyService.isCmtokenidSameAsUID(cmtokenid, UID)){
//				
//				String RelayState = getRelayState(httpReq);
//				redirectUrl = "http://www.ha.10086.cn/edu/login/redirect.jsp?RelayState="+RelayState;
//				httpResp.sendRedirect(redirectUrl);
//				return false;
//			}else{
//				if(user==null){//sso已经登录，但是门户没有登录
//					
//					String RelayState = getRelayState(httpReq);
//					redirectUrl = "http://www.ha.10086.cn/edu/login/redirect.jsp?RelayState="+RelayState;
//					httpResp.sendRedirect(redirectUrl);
//					return false;	
//				}else{//已经登录的情况，报活
//					
//					//报活
//					MyService.activeSSO_(httpReq, httpResp);
//				}
//			}
//		}
//		
//		return true;
//	}
//	@SuppressWarnings("unchecked")
//	private boolean checksso(String requestURL,HttpServletRequest httpReq,HttpServletResponse httpResp) throws IOException {
//		Object user = httpReq.getSession().getAttribute(User.LOGIN_USER);
//		if(user == null){
//			String accesstoken = httpReq.getParameter("accesstoken");
//			String token = (String)httpReq.getSession().getAttribute(User.LOGINTOKEN);
//			if(token != null && token.equals(accesstoken)){
////				User loginuser = new User();
//				String userStr = (String)httpReq.getSession().getAttribute(User.TMPLOGINU);
//				User loginuser = User.convertEduLoginUser(userStr);
//				if(loginuser!=null && loginuser.getUserId() > 0){
//					httpReq.getSession(true).setAttribute(User.LOGIN_USER, loginuser);
//				}
//				
////				Map<String,Object> usermp = JsonUtil.convertJson2Object(userStr, Map.class);
////				Object body = usermp.get("body");
////				if(body!=null){
////					Map<String,Object> userbd = (Map<String,Object>)body;
////					String uid     = StringUtil.obj2TrimStr(userbd.get("id"));
////					String unm     = StringUtil.obj2TrimStr(userbd.get("name"));
////					String account = StringUtil.obj2TrimStr(userbd.get("account"));
////					String phone   = StringUtil.obj2TrimStr(userbd.get("phone"));
////					String typeid  = StringUtil.obj2TrimStr(userbd.get("type"));
////					String sex     = StringUtil.obj2TrimStr(userbd.get("sex"));
////					String city    = StringUtil.obj2TrimStr(userbd.get("city"));
////					String mail    = StringUtil.obj2TrimStr(userbd.get("mail"));
////					
////					loginuser.setUserId(StringUtil.str2Int(uid));
////					loginuser.setUserName(unm);
////					loginuser.setCityCode(StringUtil.str2Int(city));
////					loginuser.setEmail(mail);
////					loginuser.setMobile(StringUtil.str2Long(account)==0?StringUtil.str2Long(phone):StringUtil.str2Long(account));
////					loginuser.setSex(StringUtil.str2Int(sex));
////					
////					int roleId = StringUtil.str2Int(typeid);
////					String typeName = "未知";
////					if(roleId == 2){
////						typeName = "学生";
////					}else if(roleId == 3){
////						typeName = "家长";
////					}else if(roleId == 5){
////						typeName = "家长、老师";
////					}else if(roleId == 6){
////						typeName = "学校管理员";
////					}else if(roleId == 1){
////						typeName = "老师";
////					}
////					loginuser.setTypeid(roleId);
////					loginuser.setTypename(typeName);
////					
////					httpReq.getSession().setAttribute(User.LOGIN_USER, loginuser);
////				}
//				
//			}
//		}
//		return true;
//	}
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest req, ServletResponse resp,FilterChain filter) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest)req;
		HttpServletResponse httpResp = (HttpServletResponse)resp;
		Object user = httpReq.getSession().getAttribute(User.LOGIN_USER);
		
		String requestURL=EduCookieUtil.getCurrentURI(httpReq);

//		String redirectUrl = "";

//		System.out.println(requestURL);
		
//		if(requestURL.indexOf("sso.js") > -1){
			/**
			 * 单点登录判断
			 */
//		if(!checksso(user,requestURL,httpReq,httpResp)){
//			return;
//		}
//		checksso(requestURL,httpReq,httpResp);
		
//			String cacheKey1 = httpReq.getParameter("cacheKey");
//			String cmtokenid= MyService.getCookieValue(httpReq, httpResp, "cmtokenidHeNan");
//			if (StringUtil.isEmpty(cmtokenid)) {
//				httpReq.getSession().removeAttribute(User.LOGIN_USER);
//				httpReq.getSession().removeAttribute(User.CURRENT_USER);
//				httpReq.getSession().removeAttribute(User.USER_LIST);
//				MyService.truncateSsoCookie(httpReq, httpResp);
//			}else{
//				//判断cmtokenid和UID是否一致
//				String UID= MyService.getCookieValue(httpReq, httpResp, "UID");			
//				if(!MyService.isCmtokenidSameAsUID(cmtokenid, UID)){
//					System.out.println("------------ss0 00000----------------"+requestURL);
//					//清除单点登录的cookie
//					MyService.truncateSsoCookie2(httpReq, httpResp);
//
//					String RelayState=requestURL;
//					//RelayState="http://www.ha.10086.cn"+RelayState;	
//					RelayState="http://www.ha.10086.cn/edu/module/turnIndex.html";
//					if(!StringUtil.isEmpty(cacheKey1)){
//						RelayState = RelayState + "?cacheKey="+cacheKey1;
//						try{
//							RelayState = URLEncoder.encode(RelayState, "UTF-8");
//						}catch(Exception e){
//							// ignore
//						}
//					}
//					redirectUrl = "http://www.ha.10086.cn/edu/login/redirect.jsp?RelayState="+RelayState;
//					httpResp.sendRedirect(redirectUrl);
//					return;
//				}else{
//					if(user==null){
//						System.out.println("------------ss0 111111----------------");
//						//从cookie组装session失败,需要到认证中心取用户信息
////						String RelayState="http://www.ha.10086.cn"+requestURL;	
//						String RelayState="http://www.ha.10086.cn/edu/module/turnIndex.html";
//						if(!StringUtil.isEmpty(cacheKey1)){
//							RelayState = RelayState + "?cacheKey="+cacheKey1;
//							try{
//								RelayState = URLEncoder.encode(RelayState, "UTF-8");
//							}catch(Exception e){
//								// ignore
//							}
//						}
//						redirectUrl = "http://www.ha.10086.cn/edu/login/redirect.jsp?RelayState="+RelayState;
//						httpResp.sendRedirect(redirectUrl);
//						return;		
//					}else{
//						System.out.println("------------ss0 222222----------------"+((User)user).getUserName());
//						//报活
//						MyService.activeSSO_(httpReq, httpResp);				
//					}
//				}		
//			}		
			/**
			 * end
			 */		
//		}

//		String serverletPath = httpReq.getServletPath();
		String serverletPath = requestURL;
//		System.out.println("------------serverletPath----------------"+serverletPath);
//		System.out.println("------------needFilter(serverletPath)----------------"+needFilter(serverletPath));
		if(user!=null){
//			System.out.println("------------00000----------------");
			String prev = httpReq.getParameter("prev");
			if(StringUtil.str2Int(prev) == 1){
				Map params = httpReq.getParameterMap();
				
				String cacheKey = "cache" + StringUtil.getUUID();
				SpringContextHelper.instance.getOscache().put(cacheKey, JsonUtil.convertObject2Json(convertMap(params)));

				httpReq.setAttribute("cacheKey", cacheKey);
			}
//			System.out.println("------------111111----------------");
			filter.doFilter(req, resp);
			return;
		}else if(needFilter(serverletPath)){
//			System.out.println("------------333333333----------------");
			Map params = httpReq.getParameterMap();
			
			String cacheKey = "cache" + StringUtil.getUUID();

			SpringContextHelper.instance.getOscache().put(cacheKey, JsonUtil.convertObject2Json(convertMap(params)));
			
			String appBase = SystemPropUtil.getString("edu.full.path");
			httpResp.sendRedirect(appBase+"module/login.html?cacheKey="+cacheKey);
			return ;
		}else{
//			System.out.println("------------22222----------------");
			filter.doFilter(req, resp);
			return;
		}
	}
	@SuppressWarnings("unchecked")
	private Map<String,String> convertMap(Map params){
		Map<String,String> rtnMap = new HashMap<String,String>();
		Iterator keyIt = params.keySet().iterator();
		while(keyIt.hasNext()){
			String key = (String)keyIt.next();
			rtnMap.put(key, getCacheVal(key,params));
		}
		return rtnMap;
	}
	@SuppressWarnings("unchecked")
	private String getCacheVal(String fieldVal,Map cacheMap){
		Object obj = cacheMap.get(fieldVal);
		if(obj instanceof String[]){
			String[] urls = (String[])obj;
			return urls[0]; 
		}else if (obj instanceof String){
			return String.valueOf(obj); 
		}
		
		return null;
	}
	public void init(FilterConfig filterConfig) throws ServletException {
		String filterUrls = filterConfig.getInitParameter("filterUrls");
		if(!StringUtil.isEmpty(filterUrls)){
			this.filterUrls = filterUrls.split(",");
		}
	}

}
