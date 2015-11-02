package com.ai.eduportal.remote.app;

import java.io.File;

public interface AppRemoteCallerI {
	/***
	 * 学校公告数目接口
	 * @param schoolID   学校ID
	 * 
	 * */	
	public String getAnnouncementNum(String reqStr,String schoolID);
	/***
	 * 学校公告列表接口
	 * @param schoolID   学校ID
	 * 
	 * */
	public String getAnnouncement(String reqStr,String schoolID);
	/***
     * 班级短信公告接口
     * @param gradeID   班级ID
     * @param startPg
     * @param limit
     * @return
     */
	public String getGradeAnnouncement(String gradeID,int startPg,int limit);
	/***
     * 班级公告朋友圈信息列表
     * @param friendId    朋友圈ID
     * @param startPg
     * @param limit
     * @return
     */
	public String getFriendAnnouncement(String reqStr,String schoolId);
	/***
     * 班级公告信息发送
     * @param friendId    朋友圈ID
     * @param msg         要发送的信息
     * @param startPg
     * @param limit
     * @return
     */
	public String sendAnnouncement(String reqStr,String userId);
	/***
	 * 作业列表接口联调
	 * @param userId   xxt侧对应的用户ID
	 * @param type     用户类型
	 *                 1:代表老师，2:代表家长/学生
	 * 
	 * */
	public String getWorkContent(String reqStr,String userId);
	/***
	 * 学生成绩表查询
	 * @param userId   xxt侧对应的用户ID
	 * 
	 * */
	public String getStScoreLs(String reqStr,String userId);
	/***
	 * 学生某次考试所有科目成绩
	 * @param userId   xxt侧对应的用户ID
	 * 
	 * */
	public String getStCourseScoreLs(String reqStr,String userId);
	/***
	 * 学生某个科目的历史成绩
	 * @param userId   xxt侧对应的用户ID
	 * 
	 * */
	public String getStCourseHisScoreLs(String reqStr,String userId);
	
	/***
	 * 老师成绩表查询
	 * @param userId   xxt侧对应的用户ID
	 * 
	 * */
	public String getTeScoreLs(String reqStr,String userId);
	/***
	 * 班级成绩接口联调
	 * @param gradeId   xxt侧对应的班级ID
	 * @param courseId  xxt侧对应的科目ID
	 * @param examId    某次考试的ID
	 * 
	 * */
	public String getGradeScore(String reqStr,String gradeId);
	
	/***
     * 班级公告信息图片发送
     * @param friendId    朋友圈ID
     * @param msg         要发送的信息
     * @param startPg
     * @param limit
     * @return
     */
	public String sendPicAnnouncement(String reqStr,String fileName,File msgFile,String userId);
}
