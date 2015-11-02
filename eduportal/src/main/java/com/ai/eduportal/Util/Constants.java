package com.ai.eduportal.Util;


/**
 * 常量类
 * 
 * @author wangys7@asiainfo.com
 * @date 2014-08-15
 */
public interface Constants {
	
    //
	public interface XXT_HTTPURL {
        String AnnounNum = "http://api.xxt.cn/jxlx/cmccgetorgmsgboard.do";    // 公告数量
        String AnnounList = "http://api.xxt.cn/jxlx/cmccgetorgmsgboardlist.do";    // 公告列表
        String Msglist = "http://api.xxt.cn/jxlx/cmccgetmsglist.do";	//班级公告朋友列表查询
        String StuScoreList = "http://api.xxt.cn/jxlx/cmccgettemplate.do";	//学生成绩表查询
        String StuAllSubjectScoreOneExam = "http://api.xxt.cn/jxlx/cmccgettemplatescore.do";	//学生某次考试所有科目成绩
        String StuOneHisScore = "http://api.xxt.cn/jxlx/cmccgetcoursescore.do";	// 学生某个科目的历史成绩
        String TeaExamteaquery = "http://api.xxt.cn/jxlx/cmccgetexamteaquery.do";	// 老师成绩表查询
        String OneExamList = "http://api.xxt.cn/jxlx/cmccgetexamunitcourselist.do";	// 班级某次考试单科目成绩列表
        String Homework = "http://api.xxt.cn/jxlx/cmccgethomework.do";	// 作业列表接口联调
        String MsgSend = "http://api.xxt.cn/jxlx/cmccmsgsend.do";	// 班级公告信息发送
        String MsgPicSend = "http://api.xxt.cn/jxlx/cmccmediafilemsgsend.do";	// 班级公告图片语音信息发送
    }
//	public interface XXT_HTTPURL {
//        String AnnounNum = "http://122.114.54.4/jxlx/cmccgetorgmsgboard.do";    // 公告数量
//        String AnnounList = "http://122.114.54.4/jxlx/cmccgetorgmsgboardlist.do";    // 公告列表
//        String Msglist = "http://122.114.54.4/jxlx/cmccgetmsglist.do";	//班级公告朋友列表查询
//        String StuScoreList = "http://122.114.54.4/jxlx/cmccgettemplate.do";	//学生成绩表查询
//        String StuAllSubjectScoreOneExam = "http://122.114.54.4/jxlx/cmccgettemplatescore.do";	//学生某次考试所有科目成绩
//        String StuOneHisScore = "http://122.114.54.4/jxlx/cmccgetcoursescore.do";	// 学生某个科目的历史成绩
//        String TeaExamteaquery = "http://122.114.54.4/jxlx/cmccgetexamteaquery.do";	// 老师成绩表查询
//        String OneExamList = "http://122.114.54.4/jxlx/cmccgetexamunitcourselist.do";	// 班级某次考试单科目成绩列表
//        String Homework = "http://122.114.54.4/jxlx/cmccgethomework.do";	// 作业列表接口联调
//        String MsgSend = "http://122.114.54.4/jxlx/cmccmsgsend.do";	// 班级公告信息发送
//        String MsgPicSend = "http://122.114.54.4/jxlx/cmccmediafilemsgsend.do";	// 班级公告图片语音信息发送
//    }
	
    //统一调用返回
	public interface XXT_SECRET {
        String AnnounNum = "wym03k10wyml3k14wym15k20";    // 公告数量
        String AnnounList = "wymfgk10wymfgk10wymfgk10";    // 公告列表
        String Msglist = "hamgff28hamgff28hamgff28";	//班级公告朋友列表查询
        String StuScoreList = "hamgff28hamgff28hamgff28";	//学生成绩表查询
        String StuAllSubjectScoreOneExam = "hamgff28hamgff28hamgff28";	//学生某次考试所有科目成绩
        String StuOneHisScore = "hamgff28hamgff28hamgff28";	// 学生某个科目的历史成绩
        String TeaExamteaquery = "wymfgk10wymfgk10wymfgk10";	// 老师成绩表查询
        String OneExamList = "wymfgk10wymfgk10wymfgk10";	// 班级某次考试单科目成绩列表
        String Homework = "wymfgk10wymfgk10wymfgk10";	// 作业列表接口联调
        String MsgSend = "hamgff28hamgff28hamgff28";	// 班级公告信息发送
        String MsgPicSend = "hamgff28hamgff28hamgff28";	// 班级公告图片语音信息发送
    }
	
	public interface IMG_CODE{
		String AuthCode = "authCode";
		String AuthCodeTime = "authCodeTime";
	}
	
	// 用户类型
	public interface USER_TYPE {
		int STUDENT = 1;// 学生
		int TEACHER = 2;// 老师
		int PARENT = 3;// 家长
	}
	
	// xxt用户类型
	public interface XXT_USER_TYPE {
		String TEACHER = "0";// 老师
		String PARENT = "2";// 家长
	}
	
	//crm用户类型
	public interface CRM_USER_TYPE {
		String STUDENT = "2";// 学生
		String TEACHER = "1";// 老师
		String PARENT = "3";// 家长
		String DEST_USER_TEA = "0";//老师
		String DEST_USER_STU = "2";//学生/家长
	}
	
	// 系统类型
	public interface SYSTEM_TYPE {
		String XXT = "1";// 校讯通
		String TNKT ="2";// 同步课堂
		String XXB ="3";// 中小学生学习报
	}
	
	// SEND_MSG
	public interface SEND_MSG {
		String TEA_NAME = "老师";// 校讯通
		String STU_NAME ="家长";// 同步课堂
		String CC = "cc";//留言
		String GG = "gg";//公告
	}
}
