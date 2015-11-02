package com.ai.eduportal.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.ai.frame.web.cache.ThreadLocalCache;
import com.ai.frame.web.interceptor.paramProcess.ParamProcessResult;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;
import common.ai.tools.JsonUtil;

@SuppressWarnings("serial")
public class RequestInterceptor extends com.ai.frame.web.interceptor.RequestInterceptor implements Interceptor{
	private Logger logger = LoggerFactory.getActionLog(this.getClass());
	public void destroy() {
		logger.info("destroy", "{} called.");
	}

	public void init() {
		logger.info("init", "{} called.");
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		Struts2HttpRequestWrapper requestWrapper = new Struts2HttpRequestWrapper(request);
		String requestProccess = intercept(requestWrapper);
		
		if(RequestInterceptor.ERROR.equals(requestProccess)){
			//处理验证失败
			ParamProcessResult processResult = ThreadLocalCache.getInstance().getParamProcessErrorResult();
			if(requestWrapper.isMultiPartRequest()){
				PrintWriter out = null;
				try{
					out = ServletActionContext.getResponse().getWriter();
					out.println("<html><head></head>");
		            out.println("<Script LANGUAGE='javascript'> function upcallback(rtncode,msg){alert(msg);} upcallback(0,'"+processResult.getRtnMsg()+"');</script>");
		            out.println("</head>");
		            out.println("<body>");
		            out.println("</body></html>");
				}catch(Exception ex){
					logger.error("request validate", "{} error:{}",ex);
				}finally{
					if (out != null) out.close();
				}
			}else{
				ServletActionContext.getResponse().getWriter().print(JsonUtil.convertObject2Json(processResult));
			}
			
			return null;
		}
		return invocation.invoke();
	}

}
