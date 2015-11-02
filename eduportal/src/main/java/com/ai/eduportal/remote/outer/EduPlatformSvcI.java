package com.ai.eduportal.remote.outer;

import java.util.Map;

import com.ai.eduportal.bean.User;
import com.ai.frame.bean.OutputObject;

public interface EduPlatformSvcI {
	String EDU_CRMRTNCODE = "0000";
	String EDU_RTNCODE = "ret";
	String EDU_RTNMSG  = "msg";
	String EDU_RTNSUC  = "0";
	String EDU_RTNERR  = "-1";
	String EDU_CONFERR = "-2";
	String EDULOGOUT_UID  = "EDUlogout";
	String LOGINCHECK_UID = "loginCheck";
	String GETUSERDTL_UID = "getUserDtl";
	String GETSTDPRT_UID  = "getStudentParent";
	String DUMP_UID       = "gotoDump";
	String GOTOHEEDU_UID  = "gotoHeEdu";
	String USERORDER_UID  = "edu_userOrder";
	String CLASSTCH_UID   = "classTeacher";
	String LOCALLOGIN_UID = "localLoginAuth";
	String SYNCEDUPWD_UID = "syncHeEduPwd2Local";
	String EDUQUTH_UID    = "heEduquth";
	
	String SYNCEDUUSER_UID= "syncEduUser";
	String SYNCUSERCLS_UID= "syncUserClass";
	String syncUserRsp_UID= "syncUserRelationship";
	String SYNCEDUCLS_UID = "syncEduClass";
	String SYNCSCHOOL_UID = "syncEduSchool";
	String SYNCEDUUSERORDER_UID = "syncEduUserOrder";
	String SYNCHEEDUPWD_UID = "syncHeEduPwd";
	String SYNCORDERGROUP_UID = "syncOrderGroup";
	
	String QUERYPRODUCTS_UID  = "queryproducts";
	String QUERYGROUPS_UID    = "querygroups";
	String QUERYPDTSGP_UID    = "queryproductsgroup";
	String GROUPBOSSCODE_UID  = "groupBoss_code";
	String ORDERGROUP_UID     = "order_group";
	
	/**“和教育”云平台跳转校讯通.接口4**/
	public String gotoDump(Map<String,String> params);
	/**用户登录验证接口(和校讯通等子平台验证)**/
	public OutputObject localLoginAuth(Map<String,String> params);
	/**跳转到和教育平台.接口5**/
	public OutputObject gotoHeEdu(Map<String,String> params);

	/**用户密码同步.和教育调用省平台.接口18*/
	public String syncHeEduPwd2Local(Map<String,String> params);
	/**校讯通信息同步到和教育平台**/
	public OutputObject sync2HeEdu(Map<String,String> params,String uid);
	/**校讯通调用和教育平台.查询接口**/
	public String queryHeEdu(Map<String,String> params,String uid,String listField);
	/**和教育调用门户接口有返回值相关**/
	public String heEduCallLocal(Map<String,String> params,String uid);
	/**根据用户id和角色id查询登录用户信息**/
	public User getUserByidrole(String userId,String roleId);
//	/**获取token*/
//	public String getToken();
//	public String getLoginUser();
}
