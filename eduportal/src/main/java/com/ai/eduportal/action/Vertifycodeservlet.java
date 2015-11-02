package com.ai.eduportal.action;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ai.logger.Logger;
import common.ai.logger.LoggerFactory;
import common.ai.tools.IOUtil;

public class Vertifycodeservlet extends HttpServlet {
	private Logger logger = LoggerFactory.getActionLog(this.getClass());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int width = 68;
		int height= 32;
		
		char[] ch = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		int index, len = ch.length;
		Random random = new Random();
		String[] lettles = new String[4];
		
		String prefix    = "randomCode/";
		StringBuffer randomcode = new StringBuffer();
		for (int i = 0; i < 4; i++) {
			index = random.nextInt(len);
			char lettle = ch[index];
			lettles[i]  = prefix + lettle + ".png";
			randomcode.append(lettle);
		}
		
		BufferedImage imageNew = new BufferedImage(width, height,  BufferedImage.TYPE_INT_RGB);
		int[]  whitebgArray    = readImageArray(prefix + "bgpng.png",18,6);
		int[]  rightbgArray    = readImageArray(prefix + "rtbgpng.png",5,32);
		int i = 0;
		for(String lettle:lettles){
			int[]  lettleArray    = readImageArray(lettle,18,26);
			imageNew.setRGB(i*18 - i*3, 0, 18, 26, lettleArray, 0, 18);
			
			imageNew.setRGB(i*18 - i*3, 26, 18, 6, whitebgArray, 0, 18);
			i++;
		}
		imageNew.setRGB(63, 0, 5, 32, rightbgArray, 0, 5);
		
		try {
			ImageIO.write(imageNew,  "png",  response.getOutputStream());//写图片
			
			request.getSession().setAttribute("captchaToken", randomcode.toString());
			
			logger.info("create random code[{}] success.", null, randomcode.toString());
		} catch (IOException e) {
			logger.error("create random code[{}] error:", e, randomcode.toString());
		}
	}

	// 从图片中读取RGB
	public int[] readImageArray(String name, int width, int height) {
		InputStream in = null;
		try {
			in = this.getClass().getClassLoader().getResourceAsStream(name);
			BufferedImage imageOne = ImageIO.read(in);

			// 从图片中读取RGB
			int[] imageArrayOne = new int[width * height];
			imageArrayOne = imageOne.getRGB(0, 0, width, height, imageArrayOne,
					0, width);

			return imageArrayOne;
		} catch (Exception e) {
			System.err.println("从图片中读取RGB失败：" + e.getMessage());
			return null;
		}finally{
			IOUtil.closeInputStream(in);
		}
	}
}
