package com.ai.eduportal.httpclient.xxt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class UpdateTest {
	/**
	 * 读取一个文件夹 的文件，复制到另外一个文件下面。
	 */
	
	public static void copyFileA2B(){
		//读取a文件夹中存在文件   bumie.jpg
		String pathA = "E:"+ File.separator+"upfiletest"+ File.separator+"a"+ File.separator+"bumie.jpg";
		String pathB = "E:"+ File.separator+"upfiletest"+ File.separator+"b"+ File.separator+"bumie.jpg";
		System.out.println(pathA);
		File bumie = new File(pathA);
		BufferedInputStream in = null;
		OutputStream out = null;
		try {
			 System.out.println("以字节为单位读取文件内容，一次读一个字节：");
	         in = new BufferedInputStream(new FileInputStream(bumie));
	         File bumie2 = new File(pathB);
	         FileOutputStream fileoutStream = new FileOutputStream(bumie2);
	         out = new BufferedOutputStream(fileoutStream);
	         
	         if(!bumie2.exists()){
	        	    File toDir=new File(bumie2.getParent());
	        	    toDir.mkdirs();
	        	    bumie2.createNewFile();
	        	   }
	         
	         int tempbyte = 0;
	            while ((tempbyte = in.read()) != -1) {
	                System.out.write(tempbyte);
	                out.write(tempbyte);
	            }
	         in.close();
	         out.close();
		} catch (Exception e) {
			 e.printStackTrace();
			 System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		copyFileA2B();
	}
}
