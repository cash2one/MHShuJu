package com.ai.eduportal.interceptor;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ai.eduportal.bean.User;
import com.ai.frame.bean.InputObject;
import com.ai.frame.web.interceptor.HttpRequestWrapper;
import com.ai.frame.web.interceptor.paramProcess.AbstractParamProcess;
import com.ai.frame.web.interceptor.paramProcess.ParamProcessResult;
import com.ai.frame.web.xml.bean.Parameter;
import common.ai.tools.ClassUtil;
import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;


public class EduSessionParamProcess extends AbstractParamProcess{

	public EduSessionParamProcess(InputObject inputObject,
			HttpRequestWrapper request, com.ai.frame.web.interceptor.RequestInterceptor reqInterceptor) {
		super(inputObject, request, reqInterceptor);
	}
	public ParamProcessResult excute(Parameter param){
		HttpServletRequest req = request.getRequest();
		String keyVal = getValueFromSession(req,param.getKey());
		if(keyVal!=null){
            inputObject.addParams(param.getKey(), param.getToKey(),keyVal);
        }
		
		return getSucResult();
	}
	/**从session中取值*/
    @SuppressWarnings("unchecked")
	protected String getValueFromSession(HttpServletRequest request,String key){
    	String[] keys = key.split("[.]");
        if (keys != null && keys.length == 2) {
        	Object sessionVal = request.getSession().getAttribute(keys[0]);
        	if (sessionVal instanceof Map ) {
        		Map sessionMap = (Map)sessionVal;
        		return sessionObj2Str(sessionMap.get(keys[1]));
        	}else if (sessionVal != null) {
                Object keyVal = ClassUtil.invokFieldGetMethodVal(sessionVal, keys[1]);
                return sessionObj2Str(keyVal);
            }
        }else if(keys != null && keys.length == 1){
            Object sessionVal = request.getSession().getAttribute(keys[0]);
            
            return sessionObj2Str(sessionVal);
        }
        return "";
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
}
