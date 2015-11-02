package com.ai.eduportal.action;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.patchca.color.ColorFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.filter.predefined.DiffuseRippleFilterFactory;
import org.patchca.filter.predefined.DoubleRippleFilterFactory;
import org.patchca.filter.predefined.MarbleRippleFilterFactory;
import org.patchca.filter.predefined.WobbleRippleFilterFactory;
import org.patchca.font.RandomFontFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.text.renderer.BestFitTextRenderer;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;

public class VertifyPatchca extends HttpServlet{
	private static final ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
	private static final Random random = new Random();
	static{
		cs.setColorFactory(new ColorFactory() {
            public Color getColor(int x) {
                int[] c = new int[3];
                int i = random.nextInt(c.length);
                for (int fi = 0; fi < c.length; fi++) {
                    if (fi == i) {
                        c[fi] = random.nextInt(77);
                    } else {
                        c[fi] = random.nextInt(256);
                    }
                }
                return new Color(c[0], c[1], c[2]);
            }
        });
		
		RandomWordFactory wf = new RandomWordFactory();
		wf.setCharacters("23456789abcdefghigkmnpqrstuvwxyz");
        wf.setMaxLength(4);
        wf.setMinLength(4);
        RandomFontFactory ft = new RandomFontFactory();
        ft.setMaxSize(18);
        ft.setMinSize(18);
        BestFitTextRenderer tr = new BestFitTextRenderer();
        
        cs.setWidth(68);
        cs.setHeight(32);
        cs.setFontFactory(ft);
        cs.setTextRenderer(tr);
        cs.setWordFactory(wf);
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		switch (random.nextInt(5)) {
		case 0:
            cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
            break;
        case 1:
            cs.setFilterFactory(new MarbleRippleFilterFactory());
            break;
        case 2:
            cs.setFilterFactory(new DoubleRippleFilterFactory());
            break;
        case 3:
            cs.setFilterFactory(new WobbleRippleFilterFactory());
            break;
        case 4:
            cs.setFilterFactory(new DiffuseRippleFilterFactory());
            break;
		}
		
		HttpSession session = request.getSession(false);
        if (session == null) {
            session = request.getSession();
        }
        setResponseHeaders(response);
        
        String token = EncoderHelper.getChallangeAndWriteImage(cs, "png", response.getOutputStream());
        session.setAttribute("captchaToken", token);
        System.out.println("当前的SessionID=" + session.getId() + "，验证码=" + token);
	}
	protected void setResponseHeaders(HttpServletResponse response) {
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        long time = System.currentTimeMillis();
        response.setDateHeader("Last-Modified", time);
        response.setDateHeader("Date", time);
        response.setDateHeader("Expires", time);
    }
}
