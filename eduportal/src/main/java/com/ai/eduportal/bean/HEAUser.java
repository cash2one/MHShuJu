package com.ai.eduportal.bean;

//用户信息(学校,年级,班级id)
public class HEAUser {
	public static final String HEA_CURRENT_USER = "hea_current_user";
	public static final String HEA_LOGIN_USER = "hea_login_user";
	public static final String HEA_PT_USER = "hea_pt_user";
	private String userId;// 用户id
	private long mobile;// 用户手机号码
	private int typeId;// 用户类型(学生,老师)
	private String sex;//性别(1:男)
	private String userName;// 用户姓名
	private String city;// 市
	private String region;// 区
	private String schoolName;// 学校
	private long schoolId;
	private String gradeName;// 年级
	private long gradeId;
	private long xxtUid;
	private String imgUrl;
	private long orgId;//组织id
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getMobile() {
		return mobile;
	}

	public void setMobile(long mobile) {
		this.mobile = mobile;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public long getGradeId() {
		return gradeId;
	}

	public void setGradeId(long gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	
	public long getXxtUid() {
		return xxtUid;
	}

	public void setXxtUid(long xxtUid) {
		this.xxtUid = xxtUid;
	}
	
	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	
}
