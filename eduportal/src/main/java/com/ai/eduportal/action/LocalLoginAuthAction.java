package com.ai.eduportal.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ai.eduportal.Util.Constants;
import com.ai.eduportal.Util.EduCookieUtil;
import com.ai.eduportal.bean.User;
import com.ai.eduportal.remote.outer.EduPlatformSvcI;
import com.ai.frame.bean.InputObject;
import com.ai.frame.bean.OutputObject;
import com.ai.frame.util.ControlConstants;
import com.ai.frame.web.util.ControlFactory;
import com.ai.frame.web.xml.bean.Action;
import com.ai.frame.web.xml.bean.Parameter;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

public class LocalLoginAuthAction extends Strut2BaseAction{
	private static final String EDUPLATFORMACTION = "/hn/eduPlatformAction";
	private Logger logger = LoggerFactory.getCustomerLog(this.getClass(),"heEdulog");
	public static final String SUCCESS = "success";
	public static final String INDEX = "index";
	public static final String NOTRTNKEY = "unknow key";
	private EduPlatformSvcI eduPlatformSvc;
	public void setEduPlatformSvc(EduPlatformSvcI eduPlatformSvc) {
		this.eduPlatformSvc = eduPlatformSvc;
	}
	public String clearSession(){
		HttpSession session = getRequest().getSession();
		if(session!=null){
//			session.removeAttribute(User.LOGIN_USER);
//			session.removeAttribute(User.CURRENT_USER);
//			session.removeAttribute(User.USER_LIST);
//			session.removeAttribute(User.LOGINTOKEN);
//			session.removeAttribute(User.TMPLOGINU);
//			session.removeAttribute(User.PARENTLOGINU);
//			session.removeAttribute(User.TEACHLOGINU);
			EduCookieUtil.clearLoginInfos(session);
		}
		return null;
	}
	public String noSessionGoto(){
		clearSession();
		
		String gotoUrl = getRequest().getParameter("goto");
		
		try {
			if(!StringUtil.isEmpty(gotoUrl)){
				getResponse().sendRedirect(gotoUrl);
			}
		} catch (Exception e) {
			logger.error("send redirect to {} error:{}", e, gotoUrl);
			return SUCCESS;
		}
		
		return null;
	}
	public String anonymousLogin(){
		try {
			User loginuser = (User)getRequest().getSession().getAttribute(User.LOGIN_USER);
			
			User user = new User();
			user.setUserId(loginuser.getUserId());
			user.setUserName(loginuser.getUserName());
			user.setMobile(loginuser.getMobile());
			user.setSex(1);
			user.setRealName(loginuser.getRealName());
			user.setTypeid(99);//游客
			user.setTypename("游客");
			
			List<User> otherUsers = new ArrayList<User>();
			User user2 = new User();
			user2.setUserId(loginuser.getUserId());
			user2.setUserName(loginuser.getUserName());
			user2.setMobile(loginuser.getMobile());
			user2.setSex(1);
			user2.setRealName(loginuser.getRealName());
			user2.setTypeid(99);//游客
			user2.setTypename("游客");
			otherUsers.add(user2);
			
			getRequest().getSession(true).setAttribute(User.CURRENT_USER, user);
			getRequest().getSession(true).setAttribute(User.USER_LIST,otherUsers);
		} catch (Exception e) {
		}
		
		return SUCCESS;
	}
	
	/**登录入口**/
	public String loginAuth() {
		InputObject inobj = getInputObject();
//		String isFirst = inobj.getParams().get("isFirst");
//		if (StringUtil.isEmpty(isFirst) || !"1".equals(isFirst)) {
			Object authcode = getRequest().getSession().getAttribute(Constants.IMG_CODE.AuthCode);
			Object athcodeT = getRequest().getSession().getAttribute(Constants.IMG_CODE.AuthCodeTime);
			String codestr = StringUtil.obj2TrimStr(authcode);
			String authCode = inobj.getParams().get("authCode");
			getRequest().getSession().removeAttribute(Constants.IMG_CODE.AuthCode);
			getRequest().getSession().removeAttribute(Constants.IMG_CODE.AuthCodeTime);
			
			if (StringUtil.isEmpty(codestr)) {
				sendErrorMsg("你还没有获取验证码.");
				return null;
			} else if (StringUtil.isEmpty(authCode)) {
				sendErrorMsg("验证码必须输入.");
				return null;
			} else if (!authCode.equalsIgnoreCase(codestr)) {
				sendErrorMsg("验证码输入不正确.");
				return null;
			} else {
				long nowtime = Calendar.getInstance().getTime().getTime();
				long authcodetime = 0;
				if(athcodeT!=null && athcodeT instanceof Date){
					Date athcodeDate = (Date)athcodeT;
					authcodetime = athcodeDate.getTime();
				}
				if((nowtime - authcodetime) > 900000){//15分钟过期900000
					sendErrorMsg("验证码已过期.");
					return null;
				}
			}
//		} 
		
		long time = System.currentTimeMillis();
		OutputObject outobj = getOutputObject();
		logger.info(""+(System.currentTimeMillis() - time), "call crm login interface used:{}");
		
		if (outobj != null&& ControlConstants.RTNCODE_SUC.equals(outobj.getReturnCode())) {// 登录成功
			User loginuser = User.convertEduLoginUser(outobj.getBean());
			if (!StringUtil.isEmpty(loginuser.getUserId())) {
				getRequest().getSession(true).setAttribute(User.LOGIN_USER,loginuser);
			}
		}
		sendJson(convertOutputObject2Json(outobj));
		return null;
	}
	
	public String localLoginAuth(){
		Object user = getRequest().getSession().getAttribute(User.LOGIN_USER);
		if(user!=null && user instanceof User){
			return INDEX;
		}
		HttpServletRequest req = getRequest();
		
		logger.info(req.getRemoteAddr(),"remoteAddr:{}");
		logger.info(req.getHeader("x-forwarded-for"),"x-forwarded-for:{}");
		logger.info(req.getHeader("WL-Proxy-Client-IP"),"WL-Proxy-Client-IP:{}");
		logger.info(req.getHeader("Referer"),"Referer:{}");
		logger.info(req.getRemoteHost(),"RemoteHost:{}");
		logger.info(req.getRemoteUser(),"RemoteUser:{}");

		String referer = req.getHeader("Referer");
		if(!EduCookieUtil.canLogin(referer)){
			logger.error(referer, "===>the from {} host is denied.===>");
			return ERROR;
		}
		Map<String,String> params = convertMap(getParamVals(),getParameters(EduPlatformSvcI.LOCALLOGIN_UID));
		
		OutputObject canLogin = eduPlatformSvc.localLoginAuth(params);
		logger.info("===>call crm rtn:{}{}",null, JsonUtil.convertObject2Json(canLogin),"");
		if(canLogin !=null && ControlConstants.RTNCODE_SUC.equals(canLogin.getReturnCode())){
			User loginuser = User.convertEduLoginUser(canLogin.getBean());
			if(!StringUtil.isEmpty(loginuser.getUserId())){
				logger.info(loginuser.getUserName(),"{} logined success.");
				
				getRequest().getSession(true).setAttribute(User.LOGIN_USER, loginuser);
				getRequest().getSession(true).setAttribute(User.TMPLOGINU, loginuser);
			}
			
			logger.info("===>login error.===> the user {}{} login error.",null,JsonUtil.convertObject2Json(params),"");
			return SUCCESS;
		}else{
			logger.error("===>login error.===> can't find the user:{}{}",null,JsonUtil.convertObject2Json(params),"");
			return ERROR;
		}
	}
	@SuppressWarnings("unchecked")
	protected Map<String,String> getParamVals(){
		Map<String,String> params = new HashMap<String,String>();
		HttpServletRequest req = getRequest();
		Enumeration<String> paramEnu = req.getParameterNames();
		while(paramEnu.hasMoreElements()){
			String paramKey = paramEnu.nextElement();
			String paramVal = req.getParameter(paramKey);
			if(!StringUtil.isEmpty(paramKey)&&!StringUtil.isEmpty(paramVal)){
				params.put(paramKey, paramVal);
			}
		}
		return params;
	}
	protected List<Parameter> getParameters(String uid){
		Action action = getAction();
		return action.getInput(uid).getParameters();
	}
	protected Action getAction(){
		Action action = ControlFactory.getControl().getAction(EDUPLATFORMACTION);
		return action;
	}
	protected Map<String,String> convertMap(Map<String,String> data,List<Parameter> parameters){
    	Map<String,String> rtnMap = new HashMap<String,String>();
		if (parameters != null && parameters.size() > 0) {
			for(Parameter param:parameters){
				String key = param.getKey();
                String toKey = param.getToKey();
                String value = getSingleDataVal(data,key);
                if(!NOTRTNKEY.equals(value)){
                    rtnMap.put(toKey, value);
                }
			}
		}else{
			rtnMap.putAll(data);
		}
		return rtnMap;
    }
    protected String getSingleDataVal(Map<String,String> values,String key){
    	if(values.containsKey(key)){
            return values.get(key);
        }else{
            return NOTRTNKEY;
        }
    }
}
