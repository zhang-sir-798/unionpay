package com.dx.controller;

import com.alibaba.fastjson.JSONObject;
import com.dx.service.OrderDetailService;
import com.dx.service.QrScanUnifiedOrderService;
import com.dx.service.RouteMappingService;
import com.dx.service.ValidatePayService;
import com.dx.util.domain.Constant;
import com.dx.util.domain.Retutil;
import com.dx.util.domain.TrxEnum;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @ClassName DynamicConsume4ATController
 * @Description 聚合码支付
 * @author zhangsir
 * @Date 2018年12月3日 下午1:29:00
 * @version 1.0.0
 * 
 */
@Controller
public class DynamicConsume4ATController {

	private static final Log _log = LogFactory.getLog(DynamicConsume4ATController.class);
	@Autowired
	private OrderDetailService orderService;
	@Autowired
	private RouteMappingService routeService;
	@Autowired
	private ValidatePayService valiPayService;
	@Autowired
	private QrScanUnifiedOrderService unifiedService;

	/**
	 * 统一下单接口-请求动态二维码:
	 * 
	 * 1)先验证接口参数以及签名信息,包括商户，产品 2)验证通过创建支付订单 3)返回下单数据
	 * 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/gateway/qrcode", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String qrcode(@RequestParam String params) {
		_log.info("##开始接收AT聚合码业务统一下单请求 ,请求参数params=" + params);

		String respMessage = "";
		Map<String, String> map = new HashMap<String, String>();

		try {

			map = valiPayService.ATcodeValidateParams(params);
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				return Retutil.business(map);
			}
			
			map = orderService.orderDetailBuiled(map.get(Constant.QR_BIZ));
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
	 * 统一下单接口-真实下单: 1)聚合支付下单微信侧与阿里侧
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/gateway/unifiedorder", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public void unifiedorder(HttpServletRequest request, HttpServletResponse response) {
		String requrl = getUrl(request);
		String redirecturl = "";

		_log.info("###### 开始接收AT聚合码业务统一下单真实请求 ######");
		Map<String, String> map = null;
		try {

			map = valiPayService.ATcodeValidateSign(request);
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				_log.info("签名验证失败，交易存在风险。");
				response.sendRedirect(requrl + "/error.jsp");
				return;
			}

			switch (map.get("commonCode")) {

			case "Fixed":
				
				if ("010101".equals(map.get("trx_type"))) {
					redirecturl = gatewayTencentPayF(map.get(Constant.QR_BIZ), requrl, getIpAddr(request));
					response.sendRedirect(redirecturl);
					return;
				} else if ("010201".equals(map.get("trx_type")))  {
					redirecturl = gatewayAlibabaPay(map.get(Constant.QR_BIZ), requrl);
					response.sendRedirect(redirecturl);
					return;
				} else {
					_log.info("请用正确的App扫码,用户扫描方式有误,request=[" + map + "]");
					response.sendRedirect(requrl + "/error.jsp");
					return;
				}

			case "Dynamic":
				
				if (request.getHeader("user-agent").indexOf("MicroMessenger/") != -1) {
					redirecturl = gatewayTencentPay(map.get(Constant.QR_BIZ), requrl, getIpAddr(request));
					response.sendRedirect(redirecturl);
					return;
				} else if (request.getHeader("user-agent").indexOf("AlipayClient/") != -1) {
					redirecturl = gatewayAlibabaPay(map.get(Constant.QR_BIZ), requrl);
					response.sendRedirect(redirecturl);
					return;
				} else {
					_log.info("请用微信或者支付宝App扫码,用户扫描方式有误,request=[" + map + "]");
					response.sendRedirect(requrl + "/error.jsp");
					return;
				}

			default:
				_log.info("码类型转换失败，请检查[" + map + "]");
				break;
			}

		} catch (Exception e) {
			_log.error(e);
		}
	}

	public String gatewayAlibabaPay(String params, String requrl) {
		String redirect = "";
		JSONObject jsonParam = JSONObject.parseObject(params);
		jsonParam.put("trx_type", TrxEnum.ZFB_D_J.getCode());
		Map<String, String> map = new HashMap<String, String>();

		try {

			map = orderService.orderDetailSelect(jsonParam.toString());
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				return requrl + "/errAli.jsp";
			}

			map = orderService.orderDetailValidate(map.get(Constant.QR_BIZ));
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				return requrl + "/errRepeat.jsp";
			}

			map = routeService.selectRoute4AT(map.get(Constant.QR_BIZ));
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				return requrl + "/errAli.jsp";
			}
			
			map = unifiedService.unified4AliPay(map.get(Constant.QR_BIZ));
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				return requrl + map.get("qr_code"); 
			}
			redirect = map.get("qr_code");
			map = null;
			jsonParam = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return redirect;
	}

	/**
	 * 微信redirect:
	 *
	 * @param request
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/gateway/tencent", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ModelAndView tencentp(HttpServletRequest request, HttpServletResponse response) {
		_log.info("###### 接受微信redirect消息: ######");
		Map<String, Object> model = new HashMap<String, Object>();
		Map<String, String> map = new HashMap<String, String>();
		String jsModel = "jsApi";
		try {

			map = orderService.orderDetailSelect(info(request, TrxEnum.WX_D_J.getCode()));
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				return Retutil.errpage(map);
			}

			map = orderService.orderDetailValidate(map.get(Constant.QR_BIZ));
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				return Retutil.errpage(map);
			}

			map = routeService.selectRoute4AT(map.get(Constant.QR_BIZ));
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				return Retutil.errpage(map);
			}
			
			model = unifiedService.unified4Tencent(map.get(Constant.QR_BIZ));
			map = null;
		} catch (Exception e) {
			_log.error(e);
		}

		return new ModelAndView(jsModel, model);

	}

	/**
	 * 本接口功能：查询订单 接口业务流程： 1)先验证接口参数， 2)根据通道编号,调用服务接口 3)返回开通结果数据
	 * 
	 * @param params
	 * @return jsonString
	 */
	@RequestMapping(value = "/gateway/query", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String orderquery(@RequestParam String params) {
		_log.info("##开始接收AT聚合码业务统一查询请求 ,请求参数params=" + params);

		String respMessage = "";
		Map<String, String> map = new HashMap<String, String>();

		try {

			map = valiPayService.ATValidate4Query(params);
			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_INSIDE))) {
				return Retutil.business(map);
			}

			if (!Constant.T_INSIDE.equals(map.get(Constant.RT_OUTSIDE))) {
				return Retutil.businessQ(map.get(Constant.QR_BIZ));
			}

			map = orderService.orderDetailQuery(map.get(Constant.QR_BIZ));
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

	public String gatewayTencentPay(String params, String requrl, String ip) {
		JSONObject jsonParam = JSONObject.parseObject(params);
		String serial_no = jsonParam.getString("serial_no") + "@" + ip+"@"+"fx";
		StringBuffer bf = new StringBuffer();
		try {
			bf.append("https://open.weixin.qq.com/connect/oauth2/authorize");// wx_oauth2
			bf.append("?appid=");
			bf.append("wxa35fdd49af4a2b27");
			bf.append("&redirect_uri=");
			bf.append(java.net.URLEncoder
					.encode("http://127.0.0.1/gateway/tencentp/?qr_id=", "utf-8").toString());
			// bf.append(
			// java.net.URLEncoder.encode("https://127.0.0.1/gateway/tencentp.php?qr_id=",
			// "utf-8").toString() );
			bf.append(serial_no);
			bf.append("&response_type=code&scope=snsapi_base&state=");
			bf.append(serial_no);
			bf.append("#wechat_redirect");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_log.info("##跳转到微信jsapi网页授权地址的路径为sendUrl：" + bf.toString() + "##" + ",平台单号=[" + serial_no + "]");

		return bf.toString();
	}
	
	
	public String gatewayTencentPayF(String params, String requrl, String ip) {
		JSONObject jsonParam = JSONObject.parseObject(params);
		String serial_no = jsonParam.getString("serial_no") + "@" + ip +"@" + jsonParam.getString("transAmt");
		StringBuffer bf = new StringBuffer();
		try {
			bf.append("https://xxxx.com/connect/oauth2/authorize");// wx_oauth2
			bf.append("?appid=");
			bf.append("xxxx");
			bf.append("&redirect_uri=");
			bf.append(java.net.URLEncoder.encode("http://127.0.0.1/gateway/tencentp/?qr_id=", "utf-8").toString());
			bf.append(serial_no);
			bf.append("&response_type=code&scope=snsapi_base&state=");
			bf.append(serial_no);
			bf.append("#wechat_redirect");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_log.info("##跳转到微信jsapi网页授权地址的路径为sendUrl：" + bf.toString() + "##" + ",平台单号=[" + serial_no + "]");

		return bf.toString();
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
		String order_amount = message.split("[@]")[2];

		jsonInfo.put("serial_no", serial_no);
		jsonInfo.put("client_ip", client_ip);
		jsonInfo.put("order_amount", order_amount);
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
		System.out.println(java.net.URLDecoder.decode(
				"https://127.0.0.1",
				"UTF-8"));
	}

}
