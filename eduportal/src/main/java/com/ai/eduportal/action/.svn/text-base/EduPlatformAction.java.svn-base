package com.ai.eduportal.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import com.ai.eduportal.Util.Constants;
import com.ai.eduportal.Util.EduCookieUtil;
import com.ai.eduportal.bean.User;
import com.ai.eduportal.remote.outer.EduPlatformSvcI;
import com.ai.frame.bean.OutputObject;
import com.ai.frame.util.ControlConstants;
import com.ai.frame.web.util.ControlFactory;
import com.ai.frame.web.util.SystemPropUtil;
import com.ai.frame.web.xml.bean.Action;
import com.ai.frame.web.xml.bean.Parameter;
import common.ai.encryption.tools.EncryptionUtil;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;
import common.ai.tools.ClassUtil;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

public class EduPlatformAction extends Strut2BaseAction{
	private Logger logger = LoggerFactory.getCustomerLog(this.getClass(),"heEdulog");
	public static final String PHONEREGEX = "(13[4-9]{1}[0-9]{8})|(15[0,1,2,5,6,7,8,9]{1}[0-9]{8})|(18[2,3,4,7,8]{1}[0-9]{8})|(147[0-9]{8})";
	public static final String NOTRTNKEY = "unknow key";
	private static final String EDUPLATFORMACTION = "/hn/eduPlatformAction";
	public static final String SUCCESS = "success";
	public static final String INDEX = "index";
	public static final String RTNCODE_SUC = "0";
	private EduPlatformSvcI eduPlatformSvc;
	public void setEduPlatformSvc(EduPlatformSvcI eduPlatformSvc) {
		this.eduPlatformSvc = eduPlatformSvc;
	}
	public String excute(){
		return super.excute();
	}

	/**登录退出接口（接口7）***/
	public String eduLogout(){
		logger.info("he edu call interface [{}] started{}----------------->", null, "","Logout");
		Object user = getRequest().getSession().getAttribute(User.LOGIN_USER);
		if(user!=null && user instanceof User){
			User loginuser = (User)user;
			String[] mastFields = new String[]{"id","xxtCode","extend"};
			Map<String,String> params = convertMap(getParamVals(),getParameters(EduPlatformSvcI.EDULOGOUT_UID));
			if(paramsCheck(params,mastFields)){
				if(!checkEduLCtoken(params.get("extend"))){
					sendErrorMsg("extend字段验证失败.");
					
					return null;
				}
				
				String userId  = params.get("id");
				String xxtCode = params.get("xxtCode");
				String reaCode = SystemPropUtil.getString("edu.he.xxtcode");
				if(loginuser.getUserId().equals(userId)&&
						StringUtil.trim(reaCode).equals(xxtCode)){
					
//					getRequest().getSession().removeAttribute(User.LOGIN_USER);
//					getRequest().getSession().removeAttribute(User.CURRENT_USER);
//					getRequest().getSession().removeAttribute(User.LOGINTOKEN);
//					getRequest().getSession().removeAttribute(User.TMPLOGINU);
//					getRequest().getSession().removeAttribute(User.USER_LIST);
//					getRequest().getSession().removeAttribute(User.PARENTLOGINU);
//					getRequest().getSession().removeAttribute(User.TEACHLOGINU);
//
//					getRequest().getSession().invalidate();
					EduCookieUtil.clearLoginInfos(getRequest().getSession());
					
					sendSucMsg("登出成功。");
					logger.info("{}the he edu called the [{}] is success.----------------->", null, "","logout");
					return null;
				}else{
					logger.error("the he edu called the [{}]'s param is error:{}.----------------->", null, "logout",xxtCode);
				}
			}
			
			sendErrorMsg("登出失败。");
			logger.error("{}the he edu call the interface[{}] error.----------------->", null,"", "logout");
			return null;
		}
		sendErrorMsg("你还没有登录。");
		logger.error("the edu has't {}logined{}.----------------->", null, "","");
		
		return null;
	}
	protected String heEduCallLocal(String uid,String[] mastFields,PreCaller precaller){
		logger.info("{}he edu call interface [{}] started----------------->", null, "",uid);
		String method = getRequest().getMethod();
		if(!"post".equalsIgnoreCase(method)){
			sendErrorMsg("请使用POST提交数据。");
			
			return null;
		}
		Map<String,String> params = convertMap(getParamVals(),getParameters(uid));
		if(paramsCheck(params,mastFields)){
			if(!checkEduLCtoken(params.get("extend"))){
				sendErrorMsg("extend字段验证失败.");
				
				return null;
			}
			if(precaller !=null){
				//调用后台前预处理参数
				String rtn = precaller.predo(params);
				if(!StringUtil.isEmpty(rtn)){
					
					sendErrorMsg(rtn);
					return null;
				}
			}
			
			String loginRtn = eduPlatformSvc.heEduCallLocal(params,uid);
			
			logger.info(".----------------->{}he edu call interface [{}] return is:{}", null, "",loginRtn);
			sendJson(loginRtn,false);
			
			return null;
		}
		
		return null;
	}
	/**和教育调用门户接口有返回值相关**/
	protected String heEduCallLocal(String uid,String[] mastFields){
		return heEduCallLocal(uid,mastFields,null);
	}
	/**判断HE教育调用门户接口的extend字段**/
	private boolean checkEduLCtoken(String extend){
		String aessalt = SystemPropUtil.getString("edu.he.aes.salt");
		String aeskey  = SystemPropUtil.getString("edu.he.aes.key");
		String token   = SystemPropUtil.getString("edu.he.token");
		
		try {
			byte[] decodearr = EncryptionUtil.decryptBase64Bytes(extend);
			byte[] decodebyte= EncryptionUtil.aesDecode(decodearr, aeskey.getBytes(), aessalt);
			String decodestr = new String(decodebyte,"UTF-8");
			if(!StringUtil.isEmpty(decodestr)){
				String[] tmp = decodestr.split(",");
				if(tmp.length == 3){
					String accestoken = tmp[0];
					if(token.equals(accestoken)){
						logger.info("{}token[{}]值验证成功.", null, "",accestoken);
						return true;
					}else{
						logger.error("{}token[{}]值验证失败.", null, "",accestoken);
					}
				}else{
					logger.error("{}extend[{}]值格式不对.", null, "",decodestr);
				}
			}else{
				logger.error("{}解密extend[{}]值失败.", null, "",extend);
			}
		} catch (Exception e) {
			logger.error("验证和教育extend[{}]值失败.{}", e, extend);
		} 
		return false;
		
	}
	/**和教育用户登录验证接口.接口1有返回值相关**/
	protected String heEduLoginCheckCallLocal(String uid,String[] mastFields){
		return heEduCallLocal(uid,mastFields,new PreCaller(){
			public String predo(Map<String, String> params) {
				String aespwd  = params.get("password");
				String account = params.get("account");
				aespwd = makecrmd5pwd(aespwd,account);
				if(StringUtil.isEmpty(aespwd)){
					return "登录密码解密失败.";
				}
				
				params.put("password", aespwd);
				return null;
			}
			
		});
		
	}
	/**和教育用户登录验证接口.接口1有返回值相关**/
	protected String synUserpwd2LocalCallLocal(String uid,String[] mastFields){
		return heEduCallLocal(uid,mastFields,new PreCaller(){

			public String predo(Map<String, String> params) {
//				String aespwd  = params.get("password");
//				aespwd = decodeAespwd(aespwd);
//				if(StringUtil.isEmpty(aespwd)){
//					return "同步密码解密失败.";
//				}
//				params.put("password", aespwd);
				return null;
			}
			
		});
	}
	private String makecrmd5pwd(String aespwd,String account){
		logger.info(aespwd, "he edu login's aes pwd is {}.");

		try{
			String hepwd = decodeAespwd(aespwd);
			logger.info(hepwd, "he edu login's pwd is {}.");
			if(!StringUtil.isEmpty(hepwd)){
				hepwd = account + hepwd;
				byte[] md5arr    = EncryptionUtil.md5(hepwd.getBytes());
				String md5base64str = EncryptionUtil.encodeStrBase64(md5arr);
				
				logger.info(md5base64str, "he edu login's base64 md5 pwd is {}.");
				return md5base64str;
			}
			return null;
		}catch(Exception e){
			logger.error("he edu pwd [{}] re code md5 error:{}", e, aespwd);
			
			return null;
		}
		
	}
	private String decodeAespwd(String aespwd){
		String aessalt = SystemPropUtil.getString("edu.he.aes.salt");
		String aeskey  = SystemPropUtil.getString("edu.he.aes.key");
		logger.info(aespwd, "he edu login's aes pwd is {}.");
		logger.info(aeskey, "he edu login's aes aeskey is {}.");
		logger.info(aessalt, "he edu login's aes aessalt is {}.");
		try{
			byte[] decodearr = EncryptionUtil.decryptBase64Bytes(aespwd);
			byte[] decodebyte= EncryptionUtil.aesDecode(decodearr, aeskey.getBytes(), aessalt);
			String hepwd = new String(decodebyte,"UTF-8");
			logger.info(hepwd, "he edu login's pwd is {}.");
			
			return hepwd;
		}catch(Exception e){
			logger.error("he edu AES pwd [{}] decode error:{}", e, aespwd);
			
			return null;
		}
		
	}
	/**和教育用户登录验证接口.接口1*/
	public String loginCheck(){
		String[] mastFields = new String[]{"account","password","role","extend"};
		return heEduLoginCheckCallLocal(EduPlatformSvcI.LOGINCHECK_UID,mastFields);
	}

	/**用户详细信息获取.接口2*/
	public String getUserDtl(){
		String[] mastFields = new String[]{"account","extend"};
		return heEduCallLocal(EduPlatformSvcI.GETUSERDTL_UID,mastFields);
	}

	/**班级中学生与家长信息获取.接口3**/
	public String getStdPrt(){
		String[] mastFields = new String[]{"classId","extend"};
		return heEduCallLocal(EduPlatformSvcI.GETSTDPRT_UID,mastFields);
	}
	/**取得用户订购记录.接口8*/
	public String userOrder(){
		String[] mastFields = new String[]{"phone","extend"};
		return heEduCallLocal(EduPlatformSvcI.USERORDER_UID,mastFields);
	}

	/**班级教师信息获取.接口9*/
	public String classTeacher(){
		String[] mastFields = new String[]{"id","extend"};
		return heEduCallLocal(EduPlatformSvcI.CLASSTCH_UID,mastFields);
	}
	/**用户密码同步接口.接口18*/
	public String synUserpwd2Local(){
		String[] mastFields = new String[]{"id","password","extend","timeStamp"};
		return synUserpwd2LocalCallLocal(EduPlatformSvcI.SYNCEDUPWD_UID,mastFields);
	}

	/**19（23） 产品包月订购、包月退订、点播*/
	public String orderGroup(){//和教育调用门户接口
		String[] mastFields = new String[]{"groupBillingId","bossCode","dn","type","extend"};
		//return queryHeEdu(EduPlatformSvcI.ORDERGROUP_UID,null,mastFields);
		
		return heEduCallLocal(EduPlatformSvcI.ORDERGROUP_UID,mastFields);
	}
	/**“和教育”云平台跳转校讯通.接口4***/
	public String gotoDump(){
		logger.info("dump", "he edu call interface [{}] started----------------->");
		String method = getRequest().getMethod();
		if(!"post".equalsIgnoreCase(method)){
			sendErrorMsg("请使用POST提交数据。");
			
			return null;
		}
		Map<String,String> params = convertMap(getParamVals(),getParameters(EduPlatformSvcI.DUMP_UID));
		String[] mastFields = new String[]{"id","role","extend"};
		
		if(paramsCheck(params,mastFields)){
			if(!checkEduLCtoken(params.get("extend"))){
				sendErrorMsg("extend字段验证失败.");
				
				return null;
			}
			
			String loginRtn = eduPlatformSvc.gotoDump(params);
			
			logger.info("----------------->he edu call interface [{}] 's return is {}",null,"dump",loginRtn);
			sendJson(loginRtn,false);
			
			return null;
		}
		return null;
	}
	/**6 （双向）登录鉴权接口*/
	public String eduAuth(){
		logger.info("eduAuth", "he edu call interface [{}] started----------------->");
		Map<String,String> params = convertMap(getParamVals(),getParameters(EduPlatformSvcI.DUMP_UID));
		String[] mastFields = new String[]{"accesstoken","extend"};
		if(paramsCheck1(params,mastFields)){ //校验入参
			if(!checkEduLCtoken(params.get("extend"))){
				logger.error("{}验证和教育extend{}值失败.", null, "","");
				return INDEX;
			}
			
			String[] tokens = decodeHeLctoken(params.get("accesstoken"));
			if(tokens!=null){//accesstoken校验成功
				Object user = getRequest().getSession().getAttribute(User.LOGIN_USER);
				if(user!=null && user instanceof User){//判断当前是否已经登录
					User luser = (User)user;
					String luserId = luser.getUserId();
					logger.info(luserId, "edu system has logined[{}].");
					Object cuser = getRequest().getSession().getAttribute(User.CURRENT_USER);
					if(cuser!=null && cuser instanceof User){
						User lcuser = (User)cuser;
						int type = lcuser.getTypeid();
						if(tokens[0].equals(luserId) && tokens[1].equals(String.valueOf(type))){
							logger.info("", "auth success {}.");
							return SUCCESS;
						}else{
							logger.error("用户[{}]和角色[{}]与登录的信息不符.", null, tokens[0],tokens[1]);
							//退出之前的用户信息
							EduCookieUtil.clearLoginInfos(getRequest().getSession());
							
							//登录亲用户信息
							User nluser = eduPlatformSvc.getUserByidrole(tokens[0],tokens[1]);
							if(nluser!=null){
								getRequest().getSession(true).setAttribute(User.LOGIN_USER, nluser);
								return SUCCESS;
							}else{
								return INDEX;
							}
						}
					}
					
				}else{//没有登录的情况，模拟登录
					logger.info("", "edu system has's login[{}].");
					User luser = eduPlatformSvc.getUserByidrole(tokens[0],tokens[1]);
					if(luser!=null){
						getRequest().getSession(true).setAttribute(User.LOGIN_USER, luser);
						
						logger.info("", "login success,auth success{} .");
						return SUCCESS;
					}
				}
				
			}else{//accesstoken校验失败
				logger.error("{}accesstoken[{}] validate error.", null, "",params.get("accesstoken"));
				
				return INDEX;
			}
		}else{//校验入参失败跳转到未登录的首页
			logger.error("{}the request params is error{}.", null, "","");
		}
		
		return INDEX;
	}
	private String[] decodeHeLctoken(String accesstoken){
		String aessalt = SystemPropUtil.getString("edu.aes.salt.1");
		String aeskey  = SystemPropUtil.getString("edu.aes.key.1");
		
		try {
			String decodestr = EncryptionUtil.aesDecode(accesstoken, aeskey, aessalt);
			byte[] decodearr = EncryptionUtil.hexStrDecode2Bytes(decodestr);
			decodestr = new String(decodearr,"UTF-8");
			
			if(!StringUtil.isEmpty(decodestr)){
				String[] tmp = decodestr.split(",");
				if(tmp.length == 4){
					return tmp;
				}else{
					logger.error("{}accesstoken[{}] parttern error.", null, "",decodestr);
					sendErrorMsg("accesstoken格式不对.");
					return null;
				}
			}else{
				logger.info("{}accesstoken[{}] decode error.", null, "",decodestr);
				sendErrorMsg("accesstoken不能为空.");
				return null;
			}
		} catch (Exception e) {
			logger.error("accesstoken[{}]decode error:{}.", e, accesstoken);
			sendErrorMsg("accesstoken解密失败.");
			return null;
		} 
	}
	
	/**跳转到和教育平台.接口5*/
	@SuppressWarnings("rawtypes")
	public String gotoHeEdu(){
		logger.info("gotoHeEdu", "call HE EDU interface [{}] started----------------->");
		Object user = getRequest().getSession().getAttribute(User.LOGIN_USER);
		if(user ==null || !(user instanceof User)){
			sendErrorMsg("你还没有登录.");
		}else{
			Map<String,String> params = convertMap(getParamVals(),getParameters(EduPlatformSvcI.GOTOHEEDU_UID));
			params.put("extend", getHeExtend());
			String role = params.get("role");
			String herole = role;
			String userId = params.get("id");
			
			logger.info(userId, herole,"the input userid param is {} and role param is {}----------------->");
			
			if(Constants.USER_TYPE.STUDENT == StringUtil.str2Int(role)){//学生类型,要传家长信息过去
				Object puser = getRequest().getSession().getAttribute(User.PARENTLOGINU);
				if(puser instanceof Map){
					Map pmuser  = (Map)puser;
					User pluser = (User)pmuser.get(Constants.CRM_USER_TYPE.PARENT);
					if(pluser!=null){
						userId = pluser.getUserId();
						
						logger.info(userId, Constants.CRM_USER_TYPE.PARENT,"current choose user's parent userid is [{}] and role is [{}]----------------->");
					}else{
						logger.info(userId, "cant't get user [{}]'s parent user info.----------------->");
					}
					
				}else{
					logger.info("get parent user id is error", Constants.CRM_USER_TYPE.PARENT,"current choose user is [{}] and role is [{}]----------------->");
				}
				
				herole = Constants.CRM_USER_TYPE.PARENT;
			}else{//老师类型
				herole = Constants.CRM_USER_TYPE.TEACHER;
				
				logger.info(userId, herole,"current choose user is [{}] and role is [{}]----------------->");
			}
			params.put("role", herole);
			params.put("id",   userId);
			
			logger.info(herole, "call gotoHeEdu's role param is {}.");
			logger.info(userId, "call gotoHeEdu's userId param is {}.");
			
			OutputObject outbean = eduPlatformSvc.gotoHeEdu(params);
			
			if(RTNCODE_SUC.equals(outbean.getReturnCode())){//成功
				String callbackurl = outbean.getBean().get("callbackurl");
				String accesstoken = outbean.getBean().get("accesstoken");
				callbackurl = contactUrl(callbackurl,"accesstoken="+accesstoken);
				
				sendSucMsg(callbackurl);

			}else{
				sendErrorMsg(outbean.getReturnMessage());
			}
			
			logger.info("----------------->call HE EDU interface [{}] ended,the rtncode is {} and the rntmsg is {}",null,"gotoHeEdu", outbean.getReturnCode(),outbean.getReturnMessage());
		}
		
		return null;
	}
	/**校讯通调用和教育平台.查询接口**/
	protected String queryHeEdu(String uid,String listField,String[] mastFields){
		logger.info(uid, "call HE EDU interface [{}] started----------------->");
		Map<String,String> params = convertMap(getParamVals(),getParameters(uid));
		if(paramsCheck(params,mastFields)){
			params.put("extend", getHeExtend());
			String sendJson = eduPlatformSvc.queryHeEdu(params,uid,listField);
			
			logger.info(uid,sendJson, "----------------->call HE EDU interface [{}] 's rtn is:{}");
			sendJson(sendJson,false);
			return null;
		}
		return null;
	}
	/**19 产品查询*/
	public String queryproducts(){
		String[] mastFields = new String[]{"timeStamp","province"};
		return queryHeEdu(EduPlatformSvcI.QUERYPRODUCTS_UID,"products",mastFields);
	}
	/**20 产品组查询*/
	public String querygroups(){
		String[] mastFields = new String[]{"timeStamp","province"};
		return queryHeEdu(EduPlatformSvcI.QUERYGROUPS_UID,"groups",mastFields);
	}
	/**21 产品组合查询*/
	public String queryproductsgroup(){
		String[] mastFields = new String[]{"timeStamp","province","bossCode","productCodes","groupName","groupDesc","groupIcon"};
		return queryHeEdu(EduPlatformSvcI.QUERYPDTSGP_UID,null,mastFields);
	}
	/**22 产品组绑定业务*/
	public String groupBossCode(){
		String[] mastFields = new String[]{"timeStamp","province","bossCode","groupCode"};
		return queryHeEdu(EduPlatformSvcI.GROUPBOSSCODE_UID,null,mastFields);
	}
	
	/**校讯通信息同步到和教育平台**/
	protected String sync2HeEdu(String uid,String[] mastFields){
		HttpServletRequest req = getRequest();
		String remoteHost = req.getRemoteHost();
		String remoteAddr = req.getRemoteAddr();
		logger.info(remoteHost, uid, "----->then client host [{}] called the [{}] interface started.<---------");
		logger.info(remoteAddr, uid, "----->then client addr [{}] called the [{}] interface started.<---------");
		
		Map<String,String> params = convertMap(getParamVals(),getParameters(uid));
		if(paramsCheck(params,mastFields)){
			
			params.put("extend", getHeExtend());
			OutputObject outbean = eduPlatformSvc.sync2HeEdu(params,uid);
			
			logger.info(uid,outbean.getReturnCode(), "----->called the [{}] interface end.rtnCode is {}.<---------");
			logger.info(uid,outbean.getReturnMessage(), "----->called the [{}] interface end.rtnMsg is {}.<---------");
			
			sendEduMsg(outbean);
			return null;
		}
		return null;
	}
	private String getHeExtend(){
		String aessalt = SystemPropUtil.getString("edu.he.aes.salt");
		String aeskey  = SystemPropUtil.getString("edu.he.aes.key");
		String token   = SystemPropUtil.getString("edu.lc.token");
		long   nowtm   = Calendar.getInstance().getTime().getTime();
		String random  = randomNum(6);
		
		String orignextend = token + "," + nowtm + "," + random;
		logger.info(orignextend, "make call the he edu interface 's extend[{}]");
		try {
			byte[] encode = EncryptionUtil.aesEncode(orignextend.getBytes("UTF-8"), aeskey.getBytes(), aessalt);
			String encodeStr = EncryptionUtil.encodeStrBase64(encode);
			logger.info(encodeStr, "make call the he edu interface 's encode extend[{}]");
			return encodeStr;
		} catch (Exception e) {
			logger.error("[{}]make call the he edu extend error.{}", e, random);
		}
		return org.apache.commons.lang.StringUtils.EMPTY;
	}
	private String randomNum(int size){
		Random random = new Random();
		Set<Integer> nums = new TreeSet<Integer>();
		while(nums.size()<size){
			 int randomNum = random.nextInt(10);
			 nums.add(randomNum);
		}
		
		return org.apache.commons.lang.StringUtils.join(nums, "");
	}
	/**10 用户信息同步*/
	public String syncEduUser(){
		String[] mastFields = new String[]{"timeStamp","operation","id","name","nickName","account","type"};
		return sync2HeEdu(EduPlatformSvcI.SYNCEDUUSER_UID,mastFields);
	}
	/**11 用户班级信息同步*/
	public String syncUserClass(){
		String[] mastFields = new String[]{"timeStamp","operation","userId","classId","role"};
		return sync2HeEdu(EduPlatformSvcI.SYNCUSERCLS_UID,mastFields);
	}
	/**12 用户关系同步*/
	public String syncUserRelationship(){
		String[] mastFields = new String[]{"timeStamp","operation","id","parentId"};
		return sync2HeEdu(EduPlatformSvcI.syncUserRsp_UID,mastFields);
	}
	/**13 班级信息同步*/
	public String syncClass(){
		String[] mastFields = new String[]{"timeStamp","operation","id","name","type","schoolId","schoolNme"};
		return sync2HeEdu(EduPlatformSvcI.SYNCEDUCLS_UID,mastFields);
	}
	/**14 学校信息同步*/
	public String syncSchool(){
		String[] mastFields = new String[]{"timeStamp","operation","id","name","province","city","type"};
		return sync2HeEdu(EduPlatformSvcI.SYNCSCHOOL_UID,mastFields);
	}
	/**15 用户校讯通业务信息同步*/
	public String syncUserOrder(){
		String[] mastFields = new String[]{"timeStamp","phone","name","status"};
		return sync2HeEdu(EduPlatformSvcI.SYNCEDUUSERORDER_UID,mastFields);
	}
	/**20(24) 订购关系同步*/
	public String syncOrderGroup(){//这个由另一个系统调用过来,通过json或是xml方式调用
		String[] mastFields = new String[]{"timeStamp","province","groupBillingId","bossCode","dn","type","source"};
//		String[] mastFields = new String[]{"timeStamp","province","bossCode","dn","type","source"};
//		return sync2HeEduOrderGroup(EduPlatformSvcI.SYNCORDERGROUP_UID,mastFields);
		return sync2HeEdu(EduPlatformSvcI.SYNCORDERGROUP_UID,mastFields);
	}
	/**17 用户账号密码同步*/
	public String syncHeEduPwd(){
		String[] mastFields = new String[]{"timeStamp","id"};
		return sync2HeEdu(EduPlatformSvcI.SYNCHEEDUPWD_UID,mastFields);
	}
	
	//为20(24)接口单独使用
	protected String sync2HeEduOrderGroup(String uid,String[] mastFields){
		HttpServletRequest req = getRequest();
		String remoteHost = req.getRemoteHost();
		String remoteAddr = req.getRemoteAddr();
		logger.info(remoteHost, uid, "----->then client host [{}] called the [{}] interface started.<---------");
		logger.info(remoteAddr, uid, "----->then client addr [{}] called the [{}] interface started.<---------");
		
		Map<String,String> params = convertMap(getParamVals(),getParameters(uid));
		if(paramsCheck(params,mastFields)){
			params.put("groupBillingId", "30");
			params.put("extend", getHeExtend());
			OutputObject outbean = eduPlatformSvc.sync2HeEdu(params,uid);
			
			logger.info(uid,outbean.getReturnCode(), "----->called the [{}] interface end.rtnCode is {}.<---------");
			logger.info(uid,outbean.getReturnMessage(), "----->called the [{}] interface end.rtnMsg is {}.<---------");
			
			sendEduMsg(outbean);
			return null;
		}
		return null;
	}
//	//为20(24)接口单独使用,第三方系统通过httclient发送json过来调此接口,处理发送过的json参数
//	protected String sync2HeEduOrderGroup(String uid,String[] mastFields){
//		HttpServletRequest req = getRequest();
//		String remoteHost = req.getRemoteHost();
//		String remoteAddr = req.getRemoteAddr();
//		logger.info(remoteHost, uid, "----->then client host [{}] called the [{}] interface started.<---------");
//		logger.info(remoteAddr, uid, "----->then client addr [{}] called the [{}] interface started.<---------");
//		
//		Map<String,String> paramvals = getSyncOrderGroupParams(req);
//		
//		Map<String,String> params = convertMap(paramvals,getParameters(uid));
//		if(paramsCheck(params,mastFields)){
//			
//			params.put("extend", getHeExtend());
//			OutputObject outbean = eduPlatformSvc.sync2HeEdu(params,uid);
//			logger.info(uid,outbean.getReturnCode(), "----->called the [{}] interface end.rtnCode is {}.<---------");
//			logger.info(uid,outbean.getReturnMessage(), "----->called the [{}] interface end.rtnMsg is {}.<---------");
//			
//			sendEduMsg(outbean);
//			return null;
//		}
//		return null;
//	}
	@SuppressWarnings("unchecked")
	private Map<String,String> getSyncOrderGroupParams(HttpServletRequest req){
		String input = null;
		StringBuffer body = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = req.getReader();
			while((input = reader.readLine()) != null) {
				body.append(input);
			}
		} catch (Exception e) {
			logger.error("{}读取请求的内容体失败:{}.", e, "");
		}finally{
			try {
				if(reader!=null){
					reader.close();
				}
			} catch (IOException e) {
			}
		}
		String paramstr = body.toString();
		logger.info(paramstr, "接收到的请求Json数据为:{}");
		
		return JsonUtil.convertJson2Object(paramstr, Map.class);
	}
	private boolean paramsCheck(Map<String,String> params,String[] mastFields){
		if(mastFields!=null){
			for(String field:mastFields){
				String paramval = params.get(field);
				if(StringUtil.isEmpty(paramval)){
					logger.error("{}[{}] mast input.", null, "",field);
					sendErrorMsg(field + "字段必须输入.");
					
					return false;
				}
			}
		}
		return true;
	}
	private boolean paramsCheck1(Map<String,String> params,String[] mastFields){
		if(mastFields!=null){
			for(String field:mastFields){
				String paramval = params.get(field);
				if(StringUtil.isEmpty(paramval)){
					return false;
				}
			}
		}
		return true;
	}
	
	private String contactUrl(String url,String params){
		if(url.indexOf("?")>-1){
			url = url + "&" + params;
		}else{
			url = url + "?" + params;
		}
		return url;
	}
	public void sendSucMsg(String sucMsg){
		Map<String,String> appDetails = new HashMap<String,String>();
		appDetails.put(EduPlatformSvcI.EDU_RTNMSG, sucMsg);
		appDetails.put(EduPlatformSvcI.EDU_RTNCODE, "0");
		String sendJson = JsonUtil.convertObject2Json(appDetails);
		
		sendJson(sendJson,false);
	}
	public void sendEduMsg(OutputObject outbean){
		Map<String,String> appDetails = new HashMap<String,String>();
		appDetails.put(EduPlatformSvcI.EDU_RTNMSG, outbean.getReturnMessage());
		appDetails.put(EduPlatformSvcI.EDU_RTNCODE, outbean.getReturnCode());
		String sendJson = JsonUtil.convertObject2Json(appDetails);
		
		sendJson(sendJson,false);
	}
	public void sendErrorMsg(String errMsg){
		Map<String,String> appDetails = new HashMap<String,String>();
		appDetails.put(EduPlatformSvcI.EDU_RTNMSG, errMsg);
		appDetails.put(EduPlatformSvcI.EDU_RTNCODE, "-1");
		String sendJson = JsonUtil.convertObject2Json(appDetails);
		
		sendJson(sendJson,false);
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
	private List<Parameter> getParameters(String uid){
		Action action = getAction();
		return action.getInput(uid).getParameters();
	}
	private Action getAction(){
		Action action = ControlFactory.getControl().getAction(EDUPLATFORMACTION);
		return action;
	}
    private String sessionObj2Str(Object sessionVal){
    	if(sessionVal!= null){
    		if(sessionVal instanceof Map){
    			return JsonUtil.convertObject2Json(sessionVal);
    		}else if(sessionVal instanceof User){
    			return JsonUtil.convertObject2Json(sessionVal);
    		}else{
    			return StringUtil.obj2Str(sessionVal);
    		}
    	}
    	return "";
    }
	@SuppressWarnings("rawtypes")
	protected String getValueFromSession(String key){
		String[] keys = key.split("[.]");
		if (keys != null && keys.length == 2) {
			Object sessionVal = getSession().getAttribute(keys[0]);
			if (sessionVal instanceof Map ) {
				Map sessionMap = (Map)sessionVal;
        		return sessionObj2Str(sessionMap.get(keys[1]));
			}else if (sessionVal != null) {
                Object keyVal = ClassUtil.invokFieldGetMethodVal(sessionVal, keys[1]);
                return sessionObj2Str(keyVal);
            }
		}else if(keys != null && keys.length == 1){
			Object sessionVal = getSession().getAttribute(keys[0]);
            
            return sessionObj2Str(sessionVal);
		}
		return "";
	}
	protected Map<String,String> convertMap(Map<String,String> data,List<Parameter> parameters){
    	Map<String,String> rtnMap = new HashMap<String,String>();
		if (parameters != null && parameters.size() > 0) {
			for(Parameter param:parameters){
				String key = param.getKey();
                String toKey = param.getToKey();
                String scope = param.getScope();
                String value = "";
                if(ControlConstants.PARAM_SCOPE.SESSION.equals(scope)){//如果是从session中获取
                	value = getValueFromSession(key);
                }else if(ControlConstants.PARAM_SCOPE.PROPERTIES.equals(scope)) {
                	value = StringUtil.trim(SystemPropUtil.getString(key));
                }else{
                	value = getSingleDataVal(data,key);
                }
                
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
    interface PreCaller{
    	String predo(Map<String,String> params);
    }
}
