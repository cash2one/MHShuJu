package com.ai.eduportal.remote.app;

import java.io.File;

import com.ai.eduportal.remote.RemoteCallerF4AppFactory;
import com.ai.eduportal.remote.RemoteHttpCaller;
import com.ai.eduportal.remote.RemoteHttpCallerI;
import com.ai.frame.bean.InputObject;
import com.ai.frame.web.control.CallCoreService;
import com.ai.frame.web.util.SystemPropUtil;
import common.ai.httpclient.tools.HttpClientHelper;
import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;

public class AppRemoteCaller implements AppRemoteCallerI {
	private HttpClientHelper httpClient;
	private String logservice;
	private String logmethod;
	private Logger logger = LoggerFactory.getOuterCallerLog(RemoteCallerF4AppFactory.class);
	private String httpUrl;
	private CallCoreService callCoreService;
	
	/***
	 * 学校公告数目接口
	 * @param schoolID   学校ID
	 * 
	 * */	
	public String getAnnouncementNum(String reqStr,String schoolID){
//		httpUrl = Constants.XXT_HTTPURL.AnnounNum;
		httpUrl = SystemPropUtil.getString("edu.he.xxt.AnnounNum");
		return getCallerResp(httpUrl,reqStr,schoolID,"getAnnouncementNum");
	}
	
	/***
	 * 学校公告列表接口
	 * @param schoolID   学校ID
	 * 
	 * */
	public String getAnnouncement(String reqStr,String schoolID){
//		httpUrl = Constants.XXT_HTTPURL.AnnounList;
		httpUrl = SystemPropUtil.getString("edu.he.xxt.AnnounList");
		return getCallerResp(httpUrl,reqStr,schoolID,"getAnnouncement");
	}

	/***
     * 班级短信公告接口
     * @param gradeID   班级ID
     * @param startPg
     * @param limit
     * @return
     */
	public String getGradeAnnouncement(String gradeID,int startPg,int limit){
		String reqStr = gradeID;
		return getCallerResp(httpUrl,reqStr,gradeID,"getGradeAnnouncement");
	}
	/***
     * 班级公告朋友圈信息列表
     * @param friendId    朋友圈ID
     * @param startPg
     * @param limit
     * @return
     */
	public String getFriendAnnouncement(String reqStr,String userId){
//		httpUrl = Constants.XXT_HTTPURL.Msglist;
		httpUrl = SystemPropUtil.getString("edu.he.xxt.Msglist");
		return getCallerResp(httpUrl,reqStr,userId ,"getFriendAnnouncement");
	}
	/***
     * 班级公告信息发送
     * @param friendId    朋友圈ID
     * @param msg         要发送的信息
     * @param startPg
     * @param limit
     * @return
     */
	public String sendAnnouncement(String reqStr,String userId){
		//TODO 拼装请求参数
//		httpUrl = Constants.XXT_HTTPURL.MsgSend;
		httpUrl = SystemPropUtil.getString("edu.he.xxt.MsgSend");
		return getCallerResp(httpUrl,reqStr,userId ,"sendAnnouncement");
	}
	/***
	 * 作业列表接口联调
	 * @param userId   xxt侧对应的用户ID
	 * @param type     用户类型
	 *                 1:代表老师，2:代表家长/学生
	 * 
	 * */
	public String getWorkContent(String reqStr,String userId){
//		httpUrl = Constants.XXT_HTTPURL.Homework;
		httpUrl = SystemPropUtil.getString("edu.he.xxt.Homework");
		return getCallerResp(httpUrl,reqStr,userId,"getWorkContent");
	}
	
	/***
	 * 学生成绩表查询
	 * @param userId   xxt侧对应的用户ID
	 * 
	 * */
	public String getStScoreLs(String reqStr,String userId){
//		httpUrl = Constants.XXT_HTTPURL.StuScoreList;
		httpUrl = SystemPropUtil.getString("edu.he.xxt.StuScoreList");
		return getCallerResp(httpUrl,reqStr,userId,"getStScoreLs");
	}
	/***
	 * 学生某次考试所有科目成绩
	 * @param userId   xxt侧对应的用户ID
	 * 
	 * */
	public String getStCourseScoreLs(String reqStr,String userId){
//		httpUrl = Constants.XXT_HTTPURL.StuAllSubjectScoreOneExam;
		httpUrl = SystemPropUtil.getString("edu.he.xxt.StuAllSubjectScoreOneExam");
		return getCallerResp(httpUrl,reqStr,userId,"getStCourseScoreLs");
	}
	/***
	 * 学生某个科目的历史成绩
	 * @param userId   xxt侧对应的用户ID
	 * 
	 * */
	public String getStCourseHisScoreLs(String reqStr,String userId){
//		httpUrl = Constants.XXT_HTTPURL.StuOneHisScore;
		httpUrl = SystemPropUtil.getString("edu.he.xxt.StuOneHisScore");
		return getCallerResp(httpUrl,reqStr,userId,"getStCourseHisScoreLs");
	}
	
	/***
	 * 老师成绩表查询
	 * @param userId   xxt侧对应的用户ID
	 * 
	 * */
	public String getTeScoreLs(String reqStr,String userId){
//		httpUrl = Constants.XXT_HTTPURL.TeaExamteaquery;
		httpUrl = SystemPropUtil.getString("edu.he.xxt.TeaExamteaquery");
		return getCallerResp(httpUrl,reqStr,userId,"getStScoreLs");
	}
	/***
	 * 班级成绩接口联调
	 * @param gradeId   xxt侧对应的班级ID
	 * @param courseId  xxt侧对应的科目ID
	 * @param examId    某次考试的ID
	 * 
	 * */
	public String getGradeScore(String reqStr,String gradeId){
//		httpUrl = Constants.XXT_HTTPURL.OneExamList;
		httpUrl = SystemPropUtil.getString("edu.he.xxt.OneExamList");
		return getCallerResp(httpUrl,reqStr,gradeId,"getGradeScore");
	}
	
	private String getCallerResp(String httpUrl,String reqStr,String userId,String method){
		long start = System.currentTimeMillis();
		RemoteHttpCallerI caller = new RemoteHttpCaller(httpClient,httpUrl);
		String respStr = caller.getResponse(reqStr);
		logger.debug("remote http called,url={},reqStr={},respStr={}", null, httpUrl,reqStr,respStr);
		remoteCallerLog(method,userId,reqStr,respStr,System.currentTimeMillis() - start);
		return respStr;
	}
	/**
	 * remote http调用
	 * @param phone      手机号码
	 * @param serialNo   请求调用时的序列
	 * @param reqStr     请求参数
	 * @param respStr    返回参数
	 * @param useTimes   用时(ms)
	 */
	private void remoteCallerLog(final String method,final String userId,final String reqStr,final String respStr,final long useTimes){
		new Thread(new Runnable() {
			public void run() {
				InputObject in = new InputObject();
				in.setService(logservice);
				in.setMethod(logmethod);
				in.getParams().put("appOperlog", method);
				in.getParams().put("logOperator", userId);
				in.getParams().put("reqStr", reqStr);
				in.getParams().put("respStr", respStr);
				in.getParams().put("useTimes", String.valueOf(useTimes));
				callCoreService.execute(in);
			}
		}).start();
	}

	public void setHttpClient(HttpClientHelper httpClient) {
		this.httpClient = httpClient;
	}

	public void setLogservice(String logservice) {
		this.logservice = logservice;
	}

	public void setLogmethod(String logmethod) {
		this.logmethod = logmethod;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public void setCallCoreService(CallCoreService callCoreService) {
		this.callCoreService = callCoreService;
	}

	public String sendPicAnnouncement(String reqStr, String fileName,File msgFile, String userId) {
//		httpUrl = Constants.XXT_HTTPURL.MsgPicSend;
		httpUrl = SystemPropUtil.getString("edu.he.xxt.MsgPicSend");
		return getCallerResp(httpUrl,reqStr,fileName,msgFile,userId ,"sendPicAnnouncement");
	}
	
	private String getCallerResp(String httpUrl,String reqStr, String fileName,File msgFile,String userId,String method){
		long start = System.currentTimeMillis();
		RemoteHttpCallerI caller = new RemoteHttpCaller(httpClient,httpUrl);
		String respStr = caller.getResponse(reqStr,fileName,msgFile);
		logger.debug("remote http called,url={},reqStr={},respStr={}", null, httpUrl,reqStr,respStr);
		remoteCallerLog(method,userId,reqStr,respStr,System.currentTimeMillis() - start);
		return respStr;
	}
}
