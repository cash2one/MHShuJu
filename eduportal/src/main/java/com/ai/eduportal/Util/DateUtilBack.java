package com.ai.eduportal.Util;

import java.util.Calendar;
import java.util.Date;

import common.ai.tools.DateUtil;

public class DateUtilBack {
	public static boolean dateCompare(String date1String,String date2String){
		Date date1 = DateUtil.string2Date(date1String, "yyyy-MM-dd HH:mm:ss");
		Date date2 = DateUtil.string2Date(date2String, "yyyy-MM-dd HH:mm:ss");
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);
		if (c1.before(c2)){
			return true;
		}else{
			return false;//没有失效
		}
	}
}
