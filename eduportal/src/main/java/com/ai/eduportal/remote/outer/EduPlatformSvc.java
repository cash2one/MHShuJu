package com.ai.eduportal.remote.outer;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.ai.eduportal.bean.User;
import com.ai.frame.bean.InputObject;
import com.ai.frame.bean.OutputObject;
import com.ai.frame.util.ControlConstants;
import com.ai.frame.web.control.CallCoreService;
import com.ai.frame.web.util.ControlFactory;
import com.ai.frame.web.util.SystemPropUtil;
import com.ai.frame.web.xml.bean.Action;
import com.ai.frame.web.xml.bean.Output;
import com.ai.frame.web.xml.bean.Parameter;
import common.ai.encryption.tools.EncryptionUtil;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;
import common.ai.tools.ClassUtil;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

public class EduPlatformSvc implements EduPlatformSvcI{
	public static final String NOTRTNKEY = "unknow key";
	private static Logger log = LoggerFactory.getOuterCallerLog(EduPlatformSvc.class);
	private static final String EDUPLATFORMACTION = "/hn/eduPlatformAction";
	private static final String EDU_JUMP_PAGE = "auth";
	public static final String RETSUC = "0";
	private CallCoreService coreService;
	private EduPlatformHttclient httpclient;
	public void setHttpclient(EduPlatformHttclient httpclient) {
		this.httpclient = httpclient;
	}

	public void setCoreService(CallCoreService coreService) {
		this.coreService = coreService;
	}
	
	public String syncHeEduPwd2Local(Map<String,String> params){
		return getServiceJson(params,SYNCEDUPWD_UID);
	}
	
	public String gotoDump(Map<String,String> params){
		OutputObject rtnBean = getService(params,DUMP_UID);
		if(StringUtil.str2Int(rtnBean.getReturnCode(),-1) == 0){
			HashMap<String,String> rsp = new HashMap<String,String>();
			rsp.put("callbackurl", SystemPropUtil.getString("edu.full.path") + EDU_JUMP_PAGE);
			String token = getHeLctoken(params.get("id"),params.get("role"));
			
			rsp.put("accesstoken", token);
			
			rtnBean.getBean().putAll(rsp);
		}
		
		return convert(rtnBean,DUMP_UID);
		
	}
	private String getHeLctoken(String userId,String role){
		String aessalt = SystemPropUtil.getString("edu.aes.salt.1");
		String aeskey  = SystemPropUtil.getString("edu.aes.key.1");
		
		long   nowtm   = Calendar.getInstance().getTime().getTime();
		String random  = randomNum(6);
		String origntoken = userId + "," + role + "," + nowtm + "," + random;
		
		String encodeStr = EncryptionUtil.aesEncode(origntoken, aeskey, aessalt);
		return encodeStr;
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
	/**用户登录验证接口(和校讯通等子平台验证)**/
	public OutputObject localLoginAuth(Map<String,String> params){
		String accesstoken = params.get("accesstoken");
		String userid      = params.get("id");
		String role        = params.get("role");
		String channelType = params.get("channelType");
		String aessalt = SystemPropUtil.getString("edu.aes.salt."+channelType);
		String aeskey  = SystemPropUtil.getString("edu.aes.key."+channelType);
		
		String mockUid = SystemPropUtil.getString("edu.aes.mockuid."+channelType);
		String mockRid = SystemPropUtil.getString("edu.aes.mockrid."+channelType);
		
		log.info(accesstoken,"accesstoken:{}");
		log.info(userid,"userid:{}");
		log.info(role,"role:{}");
		log.info(channelType,"channelType:{}");
		
		log.info(mockUid,"mockUid:{}");
		log.info(mockRid,"mockRid:{}");
		
		//解密
		try{
			byte[] decodearr = EncryptionUtil.decryptBase64Bytes(accesstoken);
			byte[] decodebyte= EncryptionUtil.aesDecode(decodearr, aeskey.getBytes(), aessalt);
			String decodestr = new String(decodebyte,"UTF-8");
			log.info(decodestr,"accesstoken decode:{}");
			
			if(!StringUtil.isEmpty(decodestr)){
				String[] tmp = decodestr.split(",");
				if(tmp.length == 4){
					long accesstime   = StringUtil.str2Long(tmp[3]);
					long tokentimeout = StringUtil.str2Long(SystemPropUtil.getString("edu.aes.token.timeout"));
					
					if(isTimeOk(accesstime,tokentimeout) && tmp[0].equals(userid) && tmp[1].equals(role) && tmp[2].equals(aeskey)){
//						if(userid.equals(mockUid)&&role.equals(mockRid)){
//							log.info(decodestr,"mock called. {}");
//							
//							OutputObject rtnBean = new OutputObject();
//							rtnBean.setReturnCode(ControlConstants.RTNCODE_SUC);
//							rtnBean.getBean().put("userId", mockUid);
//							rtnBean.getBean().put("userName", mockUid);
//							rtnBean.getBean().put("sex", "1");
//							rtnBean.getBean().put("type", channelType);
//							rtnBean.getBean().put("mobile", "15093265531");
//							
//							return rtnBean;
//						}
						
						OutputObject rtnBean = getService(params,LOCALLOGIN_UID);
						if(EDU_CRMRTNCODE.equals(rtnBean.getReturnCode())){
							rtnBean.setReturnCode(ControlConstants.RTNCODE_SUC);
							return rtnBean;
						}
					}
				}
			}
		}catch(Exception e){
			log.error("token={},userid={},role={},channelType{} localLoginAuth called error:{}", e, accesstoken,userid,role,channelType);
		}
		return null;
	}
	private boolean isTimeOk(long accesstime,long tokentimeout){
		Calendar cl  = Calendar.getInstance();
		long nowtime = cl.getTime().getTime();
		if((nowtime - accesstime)/1000 > tokentimeout ){
			return false;
		}
		return true;
	}
	/**和教育调用门户接口有返回值相关**/
	public String heEduCallLocal(Map<String,String> params,String uid){
		return getServiceJson(params,uid);
	}
	
	/**校讯通调用和教育平台.查询接口**/
	public String queryHeEdu(Map<String,String> params,String uid,String listField){
		OutputObject rtnBeans = httpclient.queryHeEduCallback(params,uid,listField);
		
		return convert(rtnBeans,uid);
	}
	
	/****--------------教育平台调用和教育平台接口相关------------------------------------------******/
	public OutputObject sync2HeEdu(Map<String,String> params,String uid){
		EduSyncRunner runner = new EduSyncRunner(httpclient,params,uid);
		
		return runner.syncHeEduCallback();
	}
	/**跳转到和教育平台.接口5**/
	public OutputObject gotoHeEdu(Map<String,String> params){
		Map<String,String> rtnBeans = httpclient.getHeEduJumpCallback(params);
		OutputObject rtn = new OutputObject();
		rtn.setBean(rtnBeans);
		rtn.setReturnCode(rtnBeans.get("ret"));
		rtn.setReturnMessage(rtnBeans.get("msg"));
		return rtn;
	}
	public User getUserByidrole(String userId,String roleId){
		Map<String,String> params = new HashMap<String,String>();
		params.put("userId", userId);
		params.put("roleId", roleId);
		
		OutputObject out = getService(params,EDUQUTH_UID);
		if(StringUtil.str2Int(out.getReturnCode(),-1) == 0){
			return User.convertEduLoginUser(out.getBean());
		}
		return null;
	}
	protected String getServiceJson(Map<String,String> params,String uid){
		OutputObject rtnBean = getService(params,uid);
		
		return convert(rtnBean,uid);
	}
	protected OutputObject getService(Map<String,String> params,String uid){
		Map<String,String> cparams = convertMap(params,getParameters(uid));
		 
		InputObject param = new InputObject();
		param.setParams(cparams);
		param.setService(getService(uid));
		param.setMethod(getMethod(uid));
		param.setServerIp(StringUtil.getLocalHostIp());
		
		
		OutputObject rtnBean = coreService.execute(param);
		return rtnBean;
	}
	private String getService(String uid){
		Action action = getAction();
		return action.getInput(uid).getService();
	}
	private String getMethod(String uid){
		Action action = getAction();
		return action.getInput(uid).getMethod();
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
		try {
			HttpSession session = ServletActionContext.getRequest().getSession();
			String[] keys = key.split("[.]");
			if (keys != null && keys.length == 2) {
				Object sessionVal = session.getAttribute(keys[0]);
				if (sessionVal instanceof Map ) {
					Map sessionMap = (Map)sessionVal;
					return sessionObj2Str(sessionMap.get(keys[1]));
				}else if (sessionVal != null) {
			        Object keyVal = ClassUtil.invokFieldGetMethodVal(sessionVal, keys[1]);
			        return sessionObj2Str(keyVal);
			    }
			}else if(keys != null && keys.length == 1){
				Object sessionVal = session.getAttribute(keys[0]);
			    
			    return sessionObj2Str(sessionVal);
			}
		} catch (Exception e) {
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
	private String convert(OutputObject rtnBean,String uid){
		Action cmsaction = getAction();
		if(cmsaction!=null){
			String convertor = "com.ai.eduportal.convertor.EduPlatformConvert";
			String method    = "convert";
			List<Parameter> parameters = null;
			
			Output cmsoutput = cmsaction.getOutput(uid);
			if(cmsoutput!=null){
				parameters = cmsoutput.getParameters();
				convertor = cmsoutput.getConvertor();
				method    = cmsoutput.getMethod();
				
				if(StringUtil.isEmpty(convertor)){
					convertor = "com.ai.eduportal.convertor.EduPlatformConvert";
				}
				if(StringUtil.isEmpty(method)){
					method    = "convert";
				}
			}
			
			Class<?>[] paramcls = new Class<?>[]{OutputObject.class,List.class};
			Object[]  paramvals = new Object[]{rtnBean,parameters};
			
			return ClassUtil.invokMethod(String.class, convertor, method, paramcls, paramvals);
		}else{
			log.error("{} convert to json has't get the action:{}", null, uid,EDUPLATFORMACTION);
		}
		return null;
	}
}
