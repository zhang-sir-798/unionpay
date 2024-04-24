package com.dx.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.dx.model.RefundDetail;
import com.dx.service.MchNotifyService;
import com.dx.service.OrderDetailService;
import com.dx.service.QrScanRefundOrderService;
import com.dx.service.ValidatePayService;
import com.dx.util.alibaba.AliPayUtil;
import com.dx.util.domain.Constant;
import com.dx.util.domain.RetEnum;
import com.dx.util.domain.Retutil;
import com.dx.util.tencent.AESUtil;
import com.dx.util.tencent.WXPay;
import com.dx.util.tencent.WXPayConstants;
import com.dx.util.tencent.WXPayUtil;

/**
 * 
 * @ClassName Refund4ATController
 * @Description 聚合码退款接口
 * @author zhangsir
 * @Date 2018年12月18日 下午1:29:00
 * @version 1.0.0
 * 
 */
@Controller
public class Refund4ATController {

	private static final Log _log = LogFactory.getLog(Refund4ATController.class);
	@Autowired
	private ValidatePayService valiPayService;
	@Autowired
	private QrScanRefundOrderService refundService;
	@Autowired
	private MchNotifyService mchNotifyService;
	@Autowired
	private OrderDetailService orderService;
	@Autowired
	private QrScanRefundOrderService refundDetailService;
	

	/**
	 * 聚合码统一退款申请接口:
	 * 
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/gateway/refund" , method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String refund(@RequestParam String params) {
		_log.info("##开始接收AT聚合码业务统一退款请求 ,请求参数params=" + params);
		
		String respMessage = "";
		Map<String, String> map = new HashMap<String, String>();

		try {
			
			map = valiPayService.refundValidateParams(params);
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				return Retutil.business(map);
			}
			
			map = refundService.refundOrder(map.get(Constant.QR_BIZ));
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				return Retutil.business(map);
			}
			
			respMessage = Retutil.business(map);
			map = null;
			return respMessage;

		} catch (Exception e) {
			_log.error(e);
			return Retutil.business(null);
		}

	}
	
	
	/**
	 * 聚合码统一退款查询接口:
	 * 
	 * 
	 * @param params
	 * @return
	 */ 
	@RequestMapping(value = "/gateway/refundq" , method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String refundQuery(@RequestParam String params) {
		_log.info("##开始接收AT聚合码业务统一退款查询请求 ,请求参数params=" + params);
		
		String respMessage = "";
		Map<String, String> map = new HashMap<String, String>();

		try {

			map = valiPayService.refundQueryValidateParams(params);
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				return Retutil.business(map);
			}
			
			if(!Constant.T_INSIDE.equals(map.get(Constant.RT_OUTSIDE))) {
				return Retutil.businessREQ(map.get(Constant.QR_BIZ));
			}
			
			map = refundService.refundOrderQuery(map.get(Constant.QR_BIZ));
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				return Retutil.business(map);
			}
			
			respMessage = Retutil.business(map);
			map = null;
			return respMessage;

		} catch (Exception e) {
			_log.error(e);
			return Retutil.business(null);
		}

	}
	
	/**
	 * 
	 * @Description 微信退款通知
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/notify/refundt", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void wxPayNotify(HttpServletRequest request, HttpServletResponse response) {
		JSONObject messages = new JSONObject();
		try {
			doWeChatRefund(messages);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		String ip = getIpAddr(request);
		_log.info("##微信退款回调访问IP:{" + ip + "}");
		try {
			response.setContentType("text/xml");
			JSONObject messages = doWeChatRefundNotify(request, response);
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(messages.getString("respString").getBytes());
			out.flush(); 
			out.close();
			_log.info("##响应微信后台message:" + messages + ",响应上游后开启回调下游等业务。"); 
			// TODO 通知下游等。。。
			doWeChatRefund(messages);

		} catch (Exception e) {
			e.printStackTrace();
		}*/

	}
	/**
	 * 微信退款回调
	 * 
	 * PrintWriter out = response.getWriter(); 
	 * out.write(noticeStr);
	 * 
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unused")
	public JSONObject doWeChatRefundNotify(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		JSONObject bizparams = new JSONObject();
		
		_log.info("##收到微信退款通知,开始处理异步通知请求。");
		req.setCharacterEncoding("utf-8");
		String resString = parseRequst(req);
		_log.info("##收到微信通知,开始处理异步通知请求。请求报文：" + resString);

		String respString = Constant.WxConstant.RESP_FAIL;

		if (!StringUtils.isEmpty(resString)) {
			
			Map<String, String> reqParam = processResponseXml(resString);

			if (RetEnum.RET_SUCCESS.getCode().equals(reqParam.get("respCode"))) {

				Map<String, String> AESParams = proSignResponseXml(
						AESUtil.getRefundDecrypt(reqParam.get("req_info"), Constant.WxConstant.APP));

				// 待修改
				if (Constant.WxConstant.SUCCESS.equals(AESParams.get("refund_status"))) {
					
					String orderId = AESParams.get("out_trade_no");// 商户订单号
					String transactionId = AESParams.get("transaction_id");// 微信支付订单号

					String refundAmt = AESParams.get("refund_fee");// 申请退款金额
					String settlementRefundAmt = AESParams.get("settlement_refund_fee");// 退款金额

					String refundNo = AESParams.get("out_refund_no");// 商户退款订单号
					String recvAccout = AESParams.get("refund_recv_accout");// 退款入账账户
					
					RefundDetail refundDetail = refundDetailService.selectByRefundNo(refundNo);
					if (refundDetail != null) {
						if (settlementRefundAmt.equals(refundDetail.getRefund_amount())) {
							
							refundDetail.setNotify_time(AliPayUtil.pDates( AESParams.get("success_time")));
							refundDetail.setRefund_state(Constant.REFUND_STATUS_SUCCESS);
						
							if(refundDetailService.update(refundDetail) && orderService.dealOrder(orderId , refundDetail)) {			
								
								respString = Constant.WxConstant.RESP_OK;
								bizparams.put("refundDetail", refundDetail);
								_log.info("退款订单维护成功，退款单号=" + refundNo + ",退款金额=" + settlementRefundAmt);
							}					
						}
					}
				}
			}
		}
		bizparams.put("respString", respString);
		
		return bizparams;
	}
	
	public void doWeChatRefund(JSONObject params) throws Exception {
		//RefundDetail refundDetail = params.getObject("refundDetail", RefundDetail.class);
		
		/** 1. 退款限额回滚 **/
		// processMerchantFeeDayLimit(orderDetailSel);// 修改商户单日限额
		/** 2. 手续费回滚**/
		// processPaymentAndRecharge(orderDetailSel);// 代付额度，代付，充值
		/** 3. 通知下游 **/
		
		
		RefundDetail or = new RefundDetail();
		or.setReq_key("11");
		or.setNotify_url("https://127.0.0.1/notify/test");
		or.setMer_no("11");
		or.setOrder_no("11");
		
		or.setSerial_no("11");
		or.setRefund_no("11");
		or.setRefund_amount("1");	
		or.setRefund_state("REFUNDSUCCESS");	
		
		mchNotifyService.doRefundNotify(or, true);

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
	public Map<String, String> proSignResponseXml(String xmlStr) {
		Map<String, String> respData = new HashMap<String, String>();
		try {
			respData = WXPayUtil.xmlToMap(xmlStr);
		} catch (Exception e1) {
			respData.put("respCode", RetEnum.RET_C_NO_FAILED.getCode());
			respData.put("respMsg", RetEnum.RET_C_NO_FAILED.getMessage() + ",报文格式转换出现异常。");
			return respData;
		}

		return respData;
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
