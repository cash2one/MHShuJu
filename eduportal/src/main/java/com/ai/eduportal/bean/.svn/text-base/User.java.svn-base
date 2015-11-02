package com.ai.eduportal.bean;

import java.util.List;
import java.util.Map;

import common.ai.tools.JsonUtil;
import common.ai.tools.StringUtil;

public class User {
	public static final String LOGIN_USER    = "edu_login_user";
	public static final String CURRENT_USER  = "edu_current_user";
	public static final String USER_LIST     = "edu_user_list";
	public static final String LOGINTOKEN    = "edu_loginToken";
	public static final String TMPLOGINU     = "edu_tmplogin";
	public static final String TEACHLOGINU   = "edu_teacherlogin";
	public static final String PARENTLOGINU  = "edu_parentlogin";
	
	public static final int STUDENT = 2;// 学生
	public static final int TEACHER = 1;// 老师
	public static final int PARENT = 3;// 家长
	
	private String userId;
	private String userName;
	private Long mobile;
	private Long realName;
	private Integer sex;//1男 2女 0性别不确定
	private String email;
	private Integer cityCode;
	private int typeid;
	private String typename;
	private Integer sysType;
	private Long otherUserId;
	//2015年2月 添加用户头像
	private String userIcon;
	//2015-3-18 添加只有一个角色标识=-1
	private int isMulRole;
	//2015-4-22 平台跳转时候的orgId(schoolId)
	private Long orgId;//组织id
	
	/**
	 * 取得相应系统中对应的用户ID
	 * @param userRoles
	 * @param stype
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getSysTypeRole(Object userRoles,int stype){
		if(userRoles instanceof List){
			List roles = (List)userRoles;
			String roleId = null;
			for(Object role :roles){
				roleId = getSysTypeRoleSingle(role,stype);
				if(!StringUtil.isEmpty(roleId)){
					return roleId;
				}
			}
			return null;
		}else{
			return getSysTypeRoleSingle(userRoles,stype);
		}
	}
	public static String getSysTypeRoleSingle(Object userRole,int stype){
		if(userRole instanceof User){
			User user = (User)userRole;
			if(stype == user.getSysType()){
				return String.valueOf(user.getOtherUserId());
			}
		}
		return null;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getSysType() {
		return sysType;
	}

	public void setSysType(Integer sysType) {
		this.sysType = sysType;
	}

	public Long getOtherUserId() {
		return otherUserId;
	}

	public void setOtherUserId(Long otherUserId) {
		this.otherUserId = otherUserId;
	}
	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}
	public Long getRealName() {
		return realName;
	}
	public void setRealName(Long realName) {
		this.realName = realName;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getCityCode() {
		return cityCode;
	}
	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}
	public int getTypeid() {
		return typeid;
	}
	public void setTypeid(int typeid) {
		this.typeid = typeid;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getUserIcon() {
		return userIcon;
	}
	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}
	public int getIsMulRole() {
		return isMulRole;
	}
	public void setIsMulRole(int isMulRole) {
		this.isMulRole = isMulRole;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	public static User convertCrmLoginUser(Map<String,String> userMp){
		User loginuser = new User();
		if(userMp!=null){
			String uid     = StringUtil.trim(userMp.get("xxtuid"));
			String unm     = StringUtil.trim(userMp.get("userNm"));
			String account = StringUtil.trim(userMp.get("mobile"));
			String phone   = StringUtil.trim(userMp.get("phone"));
			String typeid  = StringUtil.trim(userMp.get("userTp"));
			String sex     = StringUtil.trim(userMp.get("sex"));
			String orgId   = StringUtil.trim(userMp.get("schoolId"));
			
			loginuser.setUserId(uid);
			loginuser.setUserName(unm);
			loginuser.setMobile(StringUtil.str2Long(account)==0?StringUtil.str2Long(phone):StringUtil.str2Long(account));
			loginuser.setSex(StringUtil.str2Int(sex));
			
			int roleId = StringUtil.str2Int(typeid);
			String typeName = getRoleName(roleId);
			
			loginuser.setTypeid(roleId);
			loginuser.setTypename(typeName);
			loginuser.setOrgId(StringUtil.str2Long(orgId));
			
			return loginuser;
		}
		return null;
	}
	
	public static User convertEduLoginUser(Map<String,String> userMp){
		User loginuser = new User();
		if(userMp!=null){
			String uid     = StringUtil.trim(userMp.get("userId"));
			String unm     = StringUtil.trim(userMp.get("userName"));
			String account = StringUtil.trim(userMp.get("mobile"));
			String phone   = StringUtil.trim(userMp.get("phone"));
			String typeid  = StringUtil.trim(userMp.get("type"));
			String sex     = StringUtil.trim(userMp.get("sex"));
			String city    = StringUtil.trim(userMp.get("area"));
			String mail    = StringUtil.trim(userMp.get("mail"));
			
			loginuser.setUserId(uid);
			loginuser.setUserName(unm);
			loginuser.setCityCode(StringUtil.str2Int(city));
			loginuser.setEmail(mail);
			loginuser.setMobile(StringUtil.str2Long(account)==0?StringUtil.str2Long(phone):StringUtil.str2Long(account));
			loginuser.setSex(StringUtil.str2Int(sex));
			
			int roleId = convertCrmRole2Local(StringUtil.str2Int(typeid));
			String typeName = getRoleName(roleId);
			
			loginuser.setTypeid(roleId);
			loginuser.setTypename(typeName);
			
			return loginuser;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static User convertEduLoginUser(String userJson){
		User loginuser = new User();
		Map<String,Object> usermp = JsonUtil.convertJson2Object(userJson, Map.class);
		Object body = usermp.get("body");
		if(body!=null){
			Map<String,Object> userbd = (Map<String,Object>)body;
			String uid     = StringUtil.obj2TrimStr(userbd.get("id"));
			String unm     = StringUtil.obj2TrimStr(userbd.get("name"));
			String account = StringUtil.obj2TrimStr(userbd.get("account"));
			String phone   = StringUtil.obj2TrimStr(userbd.get("phone"));
			String typeid  = StringUtil.obj2TrimStr(userbd.get("type"));
			String sex     = StringUtil.obj2TrimStr(userbd.get("sex"));
			String city    = StringUtil.obj2TrimStr(userbd.get("city"));
			String mail    = StringUtil.obj2TrimStr(userbd.get("mail"));
			
			loginuser.setUserId(uid);
			loginuser.setUserName(unm);
			loginuser.setCityCode(StringUtil.str2Int(city));
			loginuser.setEmail(mail);
			loginuser.setMobile(StringUtil.str2Long(account)==0?StringUtil.str2Long(phone):StringUtil.str2Long(account));
			loginuser.setSex(StringUtil.str2Int(sex));
			
			int roleId = StringUtil.str2Int(typeid);
			String typeName = getRoleName(roleId);
			
			loginuser.setTypeid(roleId);
			loginuser.setTypename(typeName);
			
			return loginuser;
		}
		return null;
	}
	public static String getRoleName(int roleId){
		String typeName = "未知";
		if(roleId == 2){
			typeName = "学生";
		}else if(roleId == 3){
			typeName = "家长";
		}else if(roleId == 5){
			typeName = "家长、老师";
		}else if(roleId == 6){
			typeName = "学校管理员";
		}else if(roleId == 1){
			typeName = "老师";
		}
		return typeName;
	}
	public static int convertHeRole(int roleId){
		if(roleId == 1){//学生
			return 2;
		}else if(roleId == 2){//老师
		    return 1;
		}else if(roleId == 3){//家长
		    return 3;
		}else{
			return roleId;
		}
	}
	public static int convertCrmRole2Local(int roleId){
		if(roleId == 1){//老师
			return 2;
		}else if(roleId == 2){//学生
		    return 1;
		}else if(roleId == 3){//家长
		    return 3;
		}else{
			return roleId;
		}
	}
	public static String convertHeRole(String roleId){
		int role = StringUtil.str2Int(roleId);
		return String.valueOf(convertHeRole(role));
	}
	
	@SuppressWarnings("rawtypes")
	public static User getSysTypeRoleUser(Object userRoles,int stype,int roleId,String userId){
		if(userRoles instanceof List){
			List roles = (List)userRoles;
			for(Object role :roles){
				User user = getSysTypeRoleSingleUser(role,stype,roleId,userId);
				if(user!=null)return user;
			}
			return null;
		}else{
			return getSysTypeRoleSingleUser(userRoles,stype);
		}
	}
	public static User getSysTypeRoleSingleUser(Object userRole,int stype,int role,String userId){
		if(userRole instanceof User){
			User user = (User)userRole;
			if(stype == user.getSysType() && role == user.getTypeid() && (userId !=null && userId.equals(user.getUserId()))){
				return user;
			}
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public static User getSysTypeRoleUser(Object userRoles,int stype){
		if(userRoles instanceof List){
			List roles = (List)userRoles;
			for(Object role :roles){
				User user = getSysTypeRoleSingleUser(role,stype);
				if(user!=null)return user;
			}
			return null;
		}else{
			return getSysTypeRoleSingleUser(userRoles,stype);
		}
	}

	public static User getSysTypeRoleSingleUser(Object userRole,int stype){
		if(userRole instanceof User){
			User user = (User)userRole;
			if(user.getSysType()!=null && (stype == user.getSysType())){
				return user;
			}
		}
		return null;
	}
}
