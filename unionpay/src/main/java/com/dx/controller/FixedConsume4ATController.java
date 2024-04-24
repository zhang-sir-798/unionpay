package com.dx.controller;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONObject;
import com.dx.service.FixedDetailService;
import com.dx.service.QrScanUnifiedOrderService;
import com.dx.service.ValidatePayService;
import com.dx.util.domain.Constant;
import com.dx.util.domain.Retutil;

/**
 * 
 * @ClassName FixedConsume4ATController
 * @Description 聚合码支付-固定码接口
 * @author zhangsir
 * @Date 2018年12月3日 下午1:29:00
 * @version 1.0.0
 * 
 */
@Controller
public class FixedConsume4ATController {

	private static final Log _log = LogFactory.getLog(FixedConsume4ATController.class);
	@Autowired
	private FixedDetailService fixedDetailService;
	@Autowired
	private ValidatePayService valiPayService;
	@Autowired
	private QrScanUnifiedOrderService qrOrderService;
	
	
	/**
	 * 批量请求二维码:
	 * 
	 * 1)先验证接口参数以及签名信息,包括商户，产品
	 * 2)验证通过创建支付订单 
	 * 3)返回下单数据
	 * 
	 * @param params
	 * @return 
	 */
	@RequestMapping(value = "/gateway/batchGenerateQrcode" , method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String batchGenerateQrcode(@RequestParam String params) {
		_log.info("##开始接收批量请求二维码 ,请求参数params=" + params);
		
		String respMessage = "";
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			
			map = valiPayService.fixdValidateParams(params);
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				return Retutil.business(map);
			}
			
			//map = fixedDetailService.batchQrcode(map.get(Constant.QR_BIZ));
			map = fixedDetailService.batchQrcodeYL(map.get(Constant.QR_BIZ));
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
	 * 批量查询二维码:
	 * 
	 * 1)先验证接口参数以及签名信息,包括商户，产品
	 * 2)验证通过创建支付订单 
	 * 3)返回下单数据
	 * 
	 * @param params
	 * @return 
	 */
	@RequestMapping(value = "/gateway/batchQrcodeQuery" , method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String qrcode(@RequestParam String params) {
		_log.info("##开始接收AT聚合码业务统一下单请求 ,请求参数params=" + params);
		
		String respMessage = "";
		Map<String, String> map = new HashMap<String, String>();

		try {
			
			//map = fixedDetailService.batchQrcodeQuery(params);
			map = fixedDetailService.batchQrcodeQueryYL(params);
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
	 * 绑定二维码：
	 * 
	 * 1)绑定二维码
	 * 绑定的事 银联二维码支付产品还是银联消费收款码产品（有区别）
	 * 2)当前接口 先选择 银联二维码产品。
	 * @param params
	 * @return 
	 */
	@RequestMapping(value = "/gateway/bindQrCode" , method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String bindQrCode(@RequestParam String params) {
		_log.info("##开始接收AT聚合码业务统一下单请求 ,请求参数params=" + params);
		
		String respMessage = "";
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			//验证结束 , 修改成，进入后先查询上户
			map = valiPayService.fixdBindValidateParams(params);
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				return Retutil.business(map);
			}
			
			//先与上游交互 添加商户 配置路由
			
			map = qrOrderService.superMerCore(map.get(Constant.QR_BIZ));
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
	 * 固定码下单（仅用于 聚合微信支付宝的前置码）:
	 * 
	 * 1)用二维码能查询出商户信息。
	 * 
	 * 2)区分出那个终端扫的码
	 * 3)返回下单数据
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/gateway/qrfixed", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ModelAndView qrfixed(HttpServletRequest request, HttpServletResponse response) {	
		
		return valiPayService.FixedCodeValidateSign(request);
		
	}
	
	

	public String getUrl(HttpServletRequest request) {
		StringBuffer bf = new StringBuffer();
		bf.append(request.getScheme());
		bf.append("://");
		bf.append(request.getServerName());
		bf.append(":");
		bf.append(request.getServerPort());
		bf.append(request.getContextPath());
		
		return bf.toString();

	}

	public String info(HttpServletRequest request, String trx_type) {

		JSONObject jsonInfo = new JSONObject();
		String message = request.getParameter("qr_id");
		String serial_no = message.split("[@]")[0];
		String client_ip = message.split("[@]")[1];
		
		jsonInfo.put("serial_no",serial_no );
		jsonInfo.put("client_ip",client_ip );
		jsonInfo.put("code_no", request.getParameter("code"));
		if (trx_type != null && trx_type != "") {
			jsonInfo.put("trx_type", trx_type);
		}

		return jsonInfo.toString();

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
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(java.net.URLDecoder.decode("https://127.0.0.1) );
	}

}
