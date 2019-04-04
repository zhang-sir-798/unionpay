package com.dx.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.dx.model.OrderDetail;
import com.dx.service.MchNotifyService;
import com.dx.service.OrderDetailService;
import com.dx.util.alibaba.AliPay;
import com.dx.util.alibaba.AliPayUtil;
import com.dx.util.domain.Constant;
import com.dx.util.domain.RetEnum;
import com.dx.util.tencent.WXPay;
import com.dx.util.tencent.WXPayConstants;
import com.dx.util.tencent.WXPayUtil;

/**
 * 
 * @ClassName Notify4ATController
 * @Description 聚合码异步通知接口
 * @author zhangsir
 * @Date 2018年12月6日 上午11:57:13
 * @version 1.0.0
 */
@RestController
public class Notify4ATController {

	private static final Log log = LogFactory.getLog(Notify4ATController.class);
	@Autowired
	private OrderDetailService orderService;
	@Autowired
	private MchNotifyService mchNotifyService;
	
	/**
	 * 
	 * @Description 微信支付后台通知响应
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/notify/consumeft", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void wxPayNotify(HttpServletRequest request, HttpServletResponse response) {

		
		/*JSONObject js = new JSONObject();
		try {
			doWeChatNotifyMch(js);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		String ip = getIpAddr(request);
		log.info("##微信支付回调get访问IP:{" + ip + "}");
		try {
			response.setContentType("text/xml");
			JSONObject messages = proWeChatOrder(request, response);
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(messages.getString("respString").getBytes());
			out.flush(); 
			out.close();
			log.info("##响应微信后台message:" + messages + ",响应上游后开启回调下游等业务。"); // TODO 通知下游等。。。
			doWeChatNotifyMch(messages);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @Description 阿里支付后台通知响应
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/notify/consumefa", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void aliPayNotify(HttpServletRequest request, HttpServletResponse response) {
		log.info("##阿里支付回调开始");
		String ip = getIpAddr(request);
		log.info("##阿里支付回调get访问IP:{" + ip + "}");
		try {
			JSONObject messages = proAliOrder(request, response);
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(messages.getString("respString").getBytes());
			out.flush();
			out.close();
			log.info("##响应阿里后台message:" + messages.getString("respString") + ",响应上游后开启回调下游等业务。");
			// TODO 通知下游等。。。
			doWeChatNotifyMch(messages);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JSONObject proAliOrder(HttpServletRequest req, HttpServletResponse response) throws Exception {
		JSONObject bizparams = new JSONObject();
		req.setCharacterEncoding("UTF-8");
		String rstNotify = parserequst(req);
		String resString = URLDecoder.decode(rstNotify, "UTF-8");
		log.info("##收到阿里通知,开始处理异步通知请求。请求报文：" + resString);

		String respString = Constant.AlipayConstant.RETURN_ALIPAY_VALUE_FAIL;
		if (!StringUtils.isEmpty(resString)) {

			Map<String, String> reqParam = processNotifyResponse(resString);

			if (RetEnum.RET_SUCCESS.getCode().equals(reqParam.get("respCode"))) {

					if ("TRADE_SUCCESS".equals(reqParam.get("trade_status"))) {
						String out_trade_no = reqParam.get("out_trade_no");// 商户订单号
						String transactionId = reqParam.get("trade_no ");// 银联交易号
						
						JSONObject jsonParam = new JSONObject();
						jsonParam.put("serial_no", out_trade_no);
						
						OrderDetail order =  orderService.orderDetailSelectNotify(jsonParam.toString());
						if (order != null && Constant.PAY_STATUS_PAYING.equals(order.getState())) {
							order.setChannel_trans_no(transactionId);
							order.setNotify_time(AliPayUtil.pDates(reqParam.get("notify_time")) );
							order.setState(Constant.PAY_STATUS_SUCCESS);
							
							if (orderService.orderDetailUpdate(order)) {
								respString = Constant.AlipayConstant.RETURN_ALIPAY_VALUE_SUCCESS;
								bizparams.put("orderDetail", order);
							}
						}
					}
				
			}
		}
		bizparams.put("respString", respString);

		return bizparams;
	}

	public JSONObject proWeChatOrder(HttpServletRequest req, HttpServletResponse response) throws Exception {
		JSONObject bizparams = new JSONObject();
		req.setCharacterEncoding("utf-8");
		String resString = parseRequst(req);
		log.info("##收到微信通知,开始处理异步通知请求。请求报文：" + resString);
		String respString = Constant.WxConstant.RESP_FAIL;
		if (!StringUtils.isEmpty(resString)) {

			Map<String, String> reqParam = processResponseXml(resString);

			if (RetEnum.RET_SUCCESS.getCode().equals(reqParam.get("respCode"))) {
				if (Constant.WxConstant.SUCCESS.equals(reqParam.get("result_code"))) {

					String out_trade_no = reqParam.get("out_trade_no");// 商户订单号
					String transactionId = reqParam.get("transaction_id");// 微信支付订单号

					JSONObject jsonParam = new JSONObject();
					jsonParam.put("serial_no", out_trade_no);
					OrderDetail order = (OrderDetail) orderService.orderDetailSelect(jsonParam.toString());
					if (order != null && Constant.PAY_STATUS_PAYING.equals(order.getState())) {
						order.setChannel_trans_no(transactionId);
						order.setNotify_time(null);
						order.setState(Constant.PAY_STATUS_SUCCESS);
						if (orderService.orderDetailUpdate(order)) {
							respString = Constant.WxConstant.RESP_OK;
							bizparams.put("orderDetail", order);
						}
					}
				}
			}
		}
		bizparams.put("respString", respString);

		return bizparams;
	}

	public void doWeChatNotifyMch(JSONObject params) throws Exception {
		/** 1. 处理订单状态 **/
		// updateOnlineOrderStatusForUnionPay(inMap);
		/** 2. 处理单日限额 **/
		// processMerchantFeeDayLimit(orderDetailSel);// 修改商户单日限额
		/** 3. 处理充值、代付 **/
		// processPaymentAndRecharge(orderDetailSel);// 代付额度，代付，充值
		/** 4. 通知下游 **/
		/*JSONObject jsparams = new JSONObject();
		OrderDetail or = new OrderDetail();
		or.setReq_key("8655DF6B92FDA121E0990EE53F58AC52");
		or.setOrder_no("test20181207193114");
		or.setSerial_no("20181207193114QLAESI9IHIL9AWE2");
		or.setOrder_amount("1");
		or.setTrx_type("010201");
		or.setNotify_url("http://127.0.0.1:8082/unionpay/notify/test");
		or.setMer_no("3125931153");
		or.setId(Long.parseLong("35"));
		or.setState("SUCCESS");

		jsparams.put("orderDetail", or);*/
		mchNotifyService.sendMchNotify(params);

	}

	// 扫码支付结果通知
	public Map<String, String> processNotifyResponse(String respStr) throws Exception {
		Map<String, String> map = AliPayUtil.convertResultStringToMap(respStr);
		String sign = map.remove("sign");
		map.remove("sign_type");
		String resp = AliPayUtil.getSignContent(map);
		if (new AliPay().isResponseSignatureValid(sign, resp)) {
			map.put("respCode", "0000");
			return map;
		} else {
			map.put("respCode", "C003");
			return map;
		}
	}

	/**
	 * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
	 * 
	 * @param xmlStr
	 *            API返回的XML格式数据
	 * @return Map类型数据
	 * @throws Exception
	 */
	public Map<String, String> processResponseXml(String xmlStr) throws Exception {
		String RETURN_CODE = "return_code";
		String return_code;
		Map<String, String> respData = WXPayUtil.xmlToMap(xmlStr);
		if (respData.containsKey(RETURN_CODE)) {
			return_code = respData.get(RETURN_CODE);
		} else {
			respData.put("respCode", RetEnum.RET_C_NO_FAILED.getCode());
			respData.put("respMsg", RetEnum.RET_C_NO_FAILED.getMessage() + ",回调报文错误。");
			return respData;
		}
		if (return_code.equals(WXPayConstants.FAIL)) {
			return respData;
		} else if (return_code.equals(WXPayConstants.SUCCESS)) {
			if (new WXPay().isResponseSignatureValid(respData)) {
				respData.put("respCode", RetEnum.RET_SUCCESS.getCode());
				return respData;
			} else {
				respData.put("respCode", RetEnum.RET_C_NO_SIGN.getCode());
				respData.put("respMsg", RetEnum.RET_C_NO_SIGN.getMessage());
				return respData;
			}
		} else {
			respData.put("respCode", RetEnum.RET_C_NO_FAILED.getCode());
			respData.put("respMsg", RetEnum.RET_C_NO_FAILED.getMessage() + ",返回码无效。");
			return respData;
		}
	}

	public static String parserequst(HttpServletRequest request) {
		String message = "";
		try {
			ServletInputStream inputStream = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

			String line = null;
			StringBuffer strbuffer = new StringBuffer(1024);
			while ((line = br.readLine()) != null) {
				strbuffer.append(line);
			}
			message = strbuffer.toString();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}

	public static String parseRequst(HttpServletRequest request) {
		String body = "";
		try {
			ServletInputStream inputStream = request.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			
			String line = null;
			StringBuffer jsonStrbuffer = new StringBuffer();
			while ((line = br.readLine()) != null) {
				jsonStrbuffer.append(line);
			}
			body = jsonStrbuffer.toString();
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return body;
	}

	/**
	 * 
	 * @Description 获取请求端IP地址
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length() = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
}
