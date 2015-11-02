package com.ai.eduportal.remote;

import java.net.URLEncoder;

import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;
import common.ai.tools.StringUtil;

public class CodeUtil {
	private static Logger logger = LoggerFactory.getOuterCallerLog(CodeUtil.class);
	private String encryptedKey;
	public void setEncryptedKey(String encryptedKey){
//		if(StringUtil.isEmpty(CodeUtil.encryptedKey)){
//			CodeUtil.encryptedKey = encryptedKey;
//		}
		this.encryptedKey = encryptedKey;
	}
	public String lettlerEncrypted(String lettlers){
//		return EncryptionUtil.desEncode(lettlers, encryptedKey);
		return DES.encrypt(lettlers, encryptedKey);
	}
	public String getvnumber(String serialNo,String phone){
		if(StringUtil.isEmpty(serialNo) || StringUtil.isEmpty(phone)){
			return null;
		}
		if(phone.length() < 11){
			return null;
		}
		String vnumber = serialNo + phone.substring(5,11);
		String oldvnmber = vnumber;
//		vnumber = EncryptionUtil.md5Str(oldvnmber);
		vnumber = MD5.encrypt(oldvnmber);
		logger.info(oldvnmber, vnumber, "{} md5 encryped to {}");
//		String finalStr = EncryptionUtil.desEncode(vnumber,encryptedKey);
		String finalStr = DES.encrypt(vnumber,encryptedKey);
		logger.info(vnumber, finalStr, "{} des encryped to {}");
		if(finalStr.length()>=30){
			finalStr=finalStr.substring(0, 30);//截取前30位进行传递
		}
		
		return finalStr;
	}
	/***取得请求参数编码后的数据*/
	public String getParamData(String serialNo,String phone){
		String serialNoEncrypted = lettlerEncrypted(serialNo);
		logger.info(serialNo, serialNoEncrypted, "{} des encryped to {}");
		String phoneEncrypted    = lettlerEncrypted(phone);
		logger.info(phone, phoneEncrypted, "{} des encryped to {}");
		String vnumberEncrypted  = getvnumber(serialNo,phone);
		String paraData = "serialNo="+serialNoEncrypted+"&phone="+phoneEncrypted+"&vnumber="+vnumberEncrypted;
		try{
			paraData = URLEncoder.encode(paraData,"UTF-8");
		}catch(Exception e){
			paraData = null;
		}
		return paraData;
	}
	
	
	public String lettlerEncrypted2(String lettlers,String key){
//		return EncryptionUtil.desEncode(lettlers, encryptedKey);
		if(key.length()!=0){
			encryptedKey = key;
		}
		return DES.encrypt(lettlers, encryptedKey);
	}
	public String getvnumber2(String serialNo,String schoolId,String key){
		if(key.length()!=0){
			encryptedKey = key;
		}
		if(StringUtil.isEmpty(serialNo) || StringUtil.isEmpty(schoolId)){
			return null;
		}
		String vnumber = serialNo + schoolId;
		String oldvnmber = vnumber;
//		vnumber = EncryptionUtil.md5Str(oldvnmber);
		vnumber = MD5.encrypt(oldvnmber);
		logger.info(oldvnmber, vnumber, "{} md5 encryped to {}");
//		String finalStr = EncryptionUtil.desEncode(vnumber,encryptedKey);
		String finalStr = DES.encrypt(vnumber,encryptedKey);
		logger.info(vnumber, finalStr, "{} des encryped to {}");
		if(finalStr.length()>=30){
			finalStr=finalStr.substring(0, 30);//截取前30位进行传递
		}
		
		return finalStr;
	}
	
	public String getParamData2(String serialNo,String schoolId,String key,String param){
		String serialNoEncrypted = lettlerEncrypted2(serialNo,key);
		logger.info(serialNo, serialNoEncrypted, "{} des encryped to {}");
		String phoneEncrypted    = lettlerEncrypted2(schoolId,key);
		logger.info(schoolId, phoneEncrypted, "{} des encryped to {}");
		String vnumberEncrypted  = getvnumber2(serialNo,schoolId,key);
		String paraData = "serialNo="+serialNoEncrypted+param+"&vnumber="+vnumberEncrypted;
		try{
			paraData = URLEncoder.encode(paraData,"UTF-8");
		}catch(Exception e){
			paraData = null;
		}
		return paraData;
	}
	
	public String getParamData1(String serialNo,String phone){
		String serialNoEncrypted = lettlerEncrypted(serialNo);
		logger.info(serialNo, serialNoEncrypted, "{} des encryped to {}");
		String phoneEncrypted    = lettlerEncrypted(phone);
		logger.info(phone, phoneEncrypted, "{} des encryped to {}");
		String vnumberEncrypted  = getvnumber(serialNo,phone);
		String paraData = "serialNo="+serialNoEncrypted+"&phone="+phoneEncrypted+"&vnumber="+vnumberEncrypted;
//		try{
//			paraData = URLEncoder.encode(paraData,"UTF-8");
//		}catch(Exception e){
//			paraData = null;
//		}
		return paraData;
	}
}
