package com.ai.eduportal.Util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证码工具类
 * 
 */
public class ImageServlet extends HttpServlet {
	private static int width = 68; 
    private static int height = 22; 
    
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	// 生成数字和字母的验证码
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		// 得到该图片的绘图对象
		Graphics g = img.getGraphics();
		Random r = new Random();
		// 设定背景色
//		g.setColor(getRandColor(200, 250)); 
		g.setColor(new Color(255,255,255,255)); 
		// 设定字体 
        //g.setFont(new Font("楷体", Font.BOLD, 22));  
        g.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 16));
        
		// 填充整个图片的颜色
		g.fillRect(0, 0, width, height);
		Random random = new Random(); 
        // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到  
        g.setColor(getRandColor(160, 200)); 
        for (int i = 0; i < 12; i++){ 
            int x = random.nextInt(width); 
            int y = random.nextInt(height); 
            int xl = random.nextInt(12); 
            int yl = random.nextInt(12); 
            g.drawLine(x, y, x + xl, y + yl); 
        } 
	        
		// 向图片中输出数字和字母
		StringBuffer sb = new StringBuffer();
//		ABCDEFGHIJKLMNOPQRSTUVWXYZ
		char[] ch = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		int index, len = ch.length;
		for (int i = 0; i < 4; i++) {
			index = r.nextInt(len);
			g.setColor(new Color(255,171,82));
			g.drawString("" + ch[index], (i * 15) + 3, 18);// 写什么数字，在图片 的什么位置画
			sb.append(ch[index]);
		}
		request.getSession().setAttribute(Constants.IMG_CODE.AuthCode, sb.toString());
		response.setContentType("image/jpeg");
		ImageIO.write(img, "JPG", response.getOutputStream());  
		// 图象生效
		// g.dispose();
	}
	
	/** 
     * 获得随即颜色值 
     *  
     * @param fc 
     * @param bc 
     * @return 
     */ 
    private Color getRandColor(int fc, int bc) { 
        // 给定范围获得随机颜色 
        Random random = new Random(); 
        if (fc > 255) 
            fc = 255; 
        if (bc > 255) 
            bc = 255; 
        int r = fc + random.nextInt(bc - fc); 
        int g = fc + random.nextInt(bc - fc); 
        int b = fc + random.nextInt(bc - fc); 
        return new Color(r, g, b); 
    } 
}