package com.dx.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 * @ClassName NotifyUnionPayCodeController
 * @Description 银联小微平台二维码回调接口
 * @author zhang_sir
 * @Date 2018年8月1日 上午11:57:13
 * @version 1.0.0
 */
@RestController
public class testController {
	
	private static int count = 0;
	private static final Log log = LogFactory.getLog(testController.class);

	/**
	 * 
	 * @Description test后台通知响应
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	@RequestMapping(value = "/notify/test", method = {RequestMethod.POST , RequestMethod.GET} )
	public void fixedCodePayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String params = request.getParameter("params");
		log.error("##测试通知 , params=["+params+"]");
		count = count+1;
		log.error("通知次数："+count);
		response.getWriter().print("false");
	}

	/**
	 * 
	 * @Description test后台通知响应
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws Exception
	 */
	@RequestMapping(value = "/notify/testt1", method = {RequestMethod.POST , RequestMethod.GET} )
	public void test(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String params = request.getParameter("params");
		log.error("##测试通知 , params=["+params+"]");
		count = count+1;
		log.error("通知次数："+count);
		response.getWriter().print("success");
	}
	

	
}
