package com.ai.eduportal.action;

import java.util.HashMap;

import com.ai.eduportal.Util.Constants;
import com.ai.eduportal.remote.RemoteCallerF4AppFactoryI;
import com.ai.eduportal.service.Cms4App;
import com.ai.frame.bean.InputObject;
import com.ai.frame.bean.OutputObject;
import com.ai.frame.util.ControlConstants;
import com.ai.frame.web.util.SystemPropUtil;
import common.ai.tools.ClassUtil;
import common.ai.tools.JsonUtil;


public class EduAppAction extends Strut2BaseAction {
	private Cms4App cms4app;
	private RemoteCallerF4AppFactoryI remoteCaller;
	public void setRemoteCaller(RemoteCallerF4AppFactoryI remoteCaller) {
		this.remoteCaller = remoteCaller;
	}
	public void setCms4app(Cms4App cms4app) {
		this.cms4app = cms4app;
	}
	public String excute(){
		InputObject inobj = this.getInputObject();
		String uid = inobj.getParams().get(ControlConstants.UID);
		if("jumpdownload".equals(uid)){
			return jumpdownload();
		}
		return super.excute();
	}
	
	public String exe4Outer(){
		InputObject inobj = this.getInputObject();
		String uid = inobj.getParams().get(ControlConstants.UID);
		Class<?>[] paramcls = new Class<?>[]{InputObject.class};
		//在inputobject中加入serialNo
		OutputObject outputObject = getOutputObject(inobj);
		String returnMessage = outputObject.getReturnMessage();
		inobj.getParams().put("serialNo", returnMessage);
		String rtnjson = ClassUtil.invokMethod(String.class, remoteCaller, uid, paramcls, inobj);
		sendJson(rtnjson,true);
		return null;
	}
	public String exe4Cms(){
		InputObject inobj = this.getInputObject();
		String uid = inobj.getParams().get(ControlConstants.UID);
		Class<?>[] paramcls = new Class<?>[]{InputObject.class};
		
		String rtnjson = ClassUtil.invokMethod(String.class, cms4app, uid, paramcls, inobj);
		
		sendJson(rtnjson,true);
		
		return null;
	}
	
	//正常请求后台:请求一次返回
	public String exe4OuterNormal(){
		InputObject inobj = this.getInputObject();
		String uid = inobj.getParams().get(ControlConstants.UID);
		Class<?>[] paramcls = new Class<?>[]{InputObject.class};
		
		String rtnjson = ClassUtil.invokMethod(String.class, remoteCaller, uid, paramcls, inobj);
		sendJson(rtnjson,true);
		return null;
	}
	
	public String jumpdownload(){
		InputObject inobj = this.getInputObject();
		
		String userId = inobj.getParams().get("userId");
		String sysRoleId = inobj.getParams().get("sysRoleId");
		String orgId = inobj.getParams().get("orgId");
		String goUrl = inobj.getParams().get("goUrl");
		sysRoleId  = Constants.CRM_USER_TYPE.PARENT;//家长
	
		String fullpath = SystemPropUtil.getString("edu.full.path");
		String sendurl = fullpath+"app_forward.jsp?userId="+userId+"&sysRoleId="+sysRoleId+"&orgId="+orgId+"&goUrl="+goUrl;
		try {
			System.out.println("------------------------>"+sendurl);
			HashMap<String,String> map = new HashMap<String, String>();
			map.put("sendurl", sendurl);
			map.put("rtnCode", ControlConstants.RTNCODE_SUC);
			String jsonUrl = JsonUtil.convertObject2Json(map);
			sendJson(jsonUrl,true);
		} catch (Exception e) {
		}
		return null;
	}
}
