package com.dx.util.alibaba;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.sf.json.JSONObject;

/**
 * 支付宝核心接口
 */
public class AliPay {
	/**
	 * 向 Map 中添加公共请求参数 <br>
	 * 
	 * @param reqData
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> fillRequestData(Map<String, Object> reqData, String interfaceName) throws Exception {
		Map<String, String> reqContent = new HashMap<String, String>();
		JSONObject fromObject = JSONObject.fromObject(reqData);
		reqContent.put("biz_content", fromObject.toString()); // 请求参数的集合
		reqContent.put("app_id", SDKConfig.getConfig().getAppId()); // 支付宝分配给开发者的应用ID
		reqContent.put("method", interfaceName); // 接口名称
		reqContent.put("charset", AliPayConstants.CHARSET_UTF_8); // 请求使用的编码格式
		reqContent.put("sign_type", "RSA2");
		reqContent.put("timestamp", AliPayUtil.toDate(new Date())); // 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss
		reqContent.put("version", AliPayConstants.VERSION); // 调用的接口版本，固定为：1.0
		reqContent.put("notify_url", SDKConfig.getConfig().getNotify()); // 支付宝服务器主动通知商户服务器里指定的页面 http/https路径。
		// 非必填
		// reqContent.put("format", AliPayConstants.FORMAT); //仅支持JSON
		//
		// reqContent.put("app_auth_token", ""); //应用授权概述

		reqContent.put("sign", AliPayUtil.generateSignature(reqContent, SDKConfig.getConfig().getPrivateKey()));
		return reqContent;
	}
	public Map<String, String> dofillRequestData(Map<String, Object> reqData, String interfaceName,String privateKey , String publicKey) throws Exception {
		Map<String, String> reqContent = new HashMap<String, String>();
		JSONObject fromObject = JSONObject.fromObject(reqData);
		reqContent.put("biz_content", fromObject.toString()); // 请求参数的集合
		reqContent.put("app_id", SDKConfig.getConfig().getAppId()); // 支付宝分配给开发者的应用ID
		reqContent.put("method", interfaceName); // 接口名称
		reqContent.put("charset", AliPayConstants.CHARSET_UTF_8); // 请求使用的编码格式
		reqContent.put("sign_type", "RSA2");
		reqContent.put("timestamp", AliPayUtil.toDate(new Date())); // 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss
		reqContent.put("version", AliPayConstants.VERSION); // 调用的接口版本，固定为：1.0
		reqContent.put("notify_url", SDKConfig.getConfig().getNotify()); // 支付宝服务器主动通知商户服务器里指定的页面 http/https路径。
		// 非必填
		// reqContent.put("format", AliPayConstants.FORMAT); //仅支持JSON
		//
		// reqContent.put("app_auth_token", ""); //应用授权概述

		reqContent.put("sign", AliPayUtil.generateSignature(reqContent, SDKConfig.getConfig().getPrivateKey()));
		return reqContent;
	}
	public Map<String, String> refillRequestData(Map<String, Object> reqData, String interfaceName,String privateKey , String publicKey,String appId) throws Exception {
		Map<String, String> reqContent = new HashMap<String, String>();
		JSONObject fromObject = JSONObject.fromObject(reqData);
		reqContent.put("biz_content", fromObject.toString()); // 请求参数的集合
		reqContent.put("app_id", appId); // 支付宝分配给开发者的应用ID
		reqContent.put("method", interfaceName); // 接口名称
		reqContent.put("charset", AliPayConstants.CHARSET_UTF_8); // 请求使用的编码格式
		reqContent.put("sign_type", "RSA2");
		reqContent.put("timestamp", AliPayUtil.toDate(new Date())); // 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss
		reqContent.put("version", AliPayConstants.VERSION); // 调用的接口版本，固定为：1.0
		reqContent.put("notify_url", SDKConfig.getConfig().getNotify()); // 支付宝服务器主动通知商户服务器里指定的页面 http/https路径。
		// 非必填
		// reqContent.put("format", AliPayConstants.FORMAT); //仅支持JSON
		//
		// reqContent.put("app_auth_token", ""); //应用授权概述
		
		reqContent.put("sign", AliPayUtil.generateSignature(reqContent, SDKConfig.getConfig().getPrivateKey()));
		return reqContent;
	}

	public Map<String, String> fillRefundData(Map<String, String> reqData, String interfaceName) throws Exception {
		Map<String, String> reqContent = new HashMap<String, String>();
		JSONObject fromObject = JSONObject.fromObject(reqData);
		reqContent.put("biz_content", fromObject.toString()); // 请求参数的集合
		reqContent.put("app_id", SDKConfig.getConfig().getAppId()); // 支付宝分配给开发者的应用ID
		reqContent.put("method", interfaceName); // 接口名称
		reqContent.put("charset", AliPayConstants.CHARSET_UTF_8); // 请求使用的编码格式
		reqContent.put("sign_type", "RSA2");
		reqContent.put("timestamp", AliPayUtil.toDate(new Date())); // 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss
		reqContent.put("version", AliPayConstants.VERSION); // 调用的接口版本，固定为：1.0
		reqContent.put("notify_url", SDKConfig.getConfig().getNotify()); // 支付宝服务器主动通知商户服务器里指定的页面 http/https路径。
		// 非必填
		// reqContent.put("format", AliPayConstants.FORMAT); //仅支持JSON
		//
		// reqContent.put("app_auth_token", ""); //应用授权概述

		reqContent.put("sign", AliPayUtil.generateSignature(reqContent, SDKConfig.getConfig().getPrivateKey()));
		return reqContent;
	}

	public Map<String, String> dofillRefundData(Map<String, String> reqData, String interfaceName, String privateKey)
			throws Exception {
		Map<String, String> reqContent = new HashMap<String, String>();
		JSONObject fromObject = JSONObject.fromObject(reqData);
		reqContent.put("biz_content", fromObject.toString()); // 请求参数的集合
		reqContent.put("app_id", SDKConfig.getConfig().getAppId()); // 支付宝分配给开发者的应用ID
		reqContent.put("method", interfaceName); // 接口名称
		reqContent.put("charset", AliPayConstants.CHARSET_UTF_8); // 请求使用的编码格式
		reqContent.put("sign_type", "RSA2");
		reqContent.put("timestamp", AliPayUtil.toDate(new Date())); // 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss
		reqContent.put("version", AliPayConstants.VERSION); // 调用的接口版本，固定为：1.0
		reqContent.put("notify_url", SDKConfig.getConfig().getNotify()); // 支付宝服务器主动通知商户服务器里指定的页面 http/https路径。
		// 非必填
		// reqContent.put("format", AliPayConstants.FORMAT); //仅支持JSON
		//
		// reqContent.put("app_auth_token", ""); //应用授权概述

		reqContent.put("sign", AliPayUtil.generateSignature(reqContent, privateKey));
		return reqContent;
	}

	/**
	 * 查询
	 * 
	 * @param reqData
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> fillDataQuery(Map<String, Object> reqData, String interfaceName,
			Map<String, String> appData) throws Exception {
		Map<String, String> reqContent = new HashMap<String, String>();
		JSONObject fromObject = JSONObject.fromObject(reqData);
		reqContent.put("biz_content", fromObject.toString()); // 请求参数的集合
		reqContent.put("app_id", appData.get("app_id")); // 支付宝分配给开发者的应用ID
		reqContent.put("method", interfaceName); // 接口名称
		reqContent.put("charset", AliPayConstants.CHARSET_UTF_8); // 请求使用的编码格式
		reqContent.put("sign_type", "RSA2");
		reqContent.put("timestamp", AliPayUtil.toDate(new Date())); // 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss
		reqContent.put("version", AliPayConstants.VERSION); // 调用的接口版本，固定为：1.0

		reqContent.put("sign", AliPayUtil.generateSignature(reqContent, appData.get("privateKey")));
		return reqContent;
	}

	/**
	 * 下单
	 * 
	 * @param reqData
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> fillRequestDatas(Map<String, Object> reqData, String interfaceName,
			Map<String, String> appData) throws Exception {
		Map<String, String> reqContent = new HashMap<String, String>();
		JSONObject fromObject = JSONObject.fromObject(reqData);
		reqContent.put("biz_content", fromObject.toString()); // 请求参数的集合
		reqContent.put("app_id", appData.get("app_id")); // 支付宝分配给开发者的应用ID
		reqContent.put("method", interfaceName); // 接口名称
		reqContent.put("charset", AliPayConstants.CHARSET_UTF_8); // 请求使用的编码格式
		reqContent.put("sign_type", "RSA2");
		reqContent.put("timestamp", AliPayUtil.toDate(new Date())); // 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss
		reqContent.put("version", AliPayConstants.VERSION); // 调用的接口版本，固定为：1.0
		reqContent.put("notify_url", appData.get("notify_url")); // 支付宝服务器主动通知商户服务器里指定的页面 http/https路径。
		// 非必填
		// reqContent.put("format", AliPayConstants.FORMAT); //仅支持JSON
		//
		// reqContent.put("app_auth_token", ""); //应用授权概述

		reqContent.put("sign", AliPayUtil.generateSignature(reqContent, appData.get("privatekey")));
		return reqContent;
	}

	/**
	 * 作用：商户入驻<br>
	 * 场景：间连商户分级入驻
	 * 
	 * @param reqData
	 *            向Alipay post的请求数据
	 * @return API返回数据
	 * @throws Exception
	 */
	public String indirectCreate(Map<String, Object> reqData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix();
		Map<String, String> map = this.fillRequestData(reqData, SDKConfig.getConfig().getIndirectCreate());
		String resp = this.post(map, url, AliPayConstants.CHARSET_UTF_8);
		String methodName = (String) map.get("method");
		return this.processResponse(methodName, resp);
	}

	/**
	 * 作用：商户信息查询<br>
	 * 场景：商户信息查询
	 * 
	 * @param reqData
	 *            向Alipay post的请求数据
	 * @return API返回数据
	 * @throws Exception
	 */
	public String indirectQuery(Map<String, Object> reqData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix();
		Map<String, String> map = this.fillRequestData(reqData, SDKConfig.getConfig().getIndirectQuery());
		String resp = this.post(map, url, AliPayConstants.CHARSET_UTF_8);
		String methodName = map.get("method");
		return this.processResponse(methodName, resp);
	}

	/**
	 * 作用：商户信息修改<br>
	 * 场景：商户分级与信息修改
	 * 
	 * @param reqData
	 *            向Alipay post的请求数据
	 * @return API返回数据
	 * @throws Exception
	 */
	public String indirectModify(Map<String, Object> reqData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix();
		Map<String, String> map = this.fillRequestData(reqData, SDKConfig.getConfig().getIndirectModify());
		String resp = this.post(map, url, AliPayConstants.CHARSET_UTF_8);
		String methodName = map.get("method");
		return this.processResponse(methodName, resp);
	}

	/**
	 * 作用：扫码支付<br>
	 * 场景：收银员通过收银台或商户后台调用支付宝接口，生成二维码后，展示给用户，由用户扫描二维码完成订单支付。
	 * 
	 * @param reqData
	 *            向Alipay post的请求数据
	 * @return API返回数据
	 * @throws Exception
	 */
	public String tradePrecreate(Map<String, Object> reqData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix();
		Map<String, String> map = this.fillRequestData(reqData, SDKConfig.getConfig().getTradePrecreate());
		String resp = this.post(map, url, AliPayConstants.CHARSET_UTF_8);
		String methodName = map.get("method");
		return this.processResponse(methodName, resp);
	}

	/**
	 * 作用：扫码支付<br>
	 * 场景：收银员通过收银台或商户后台调用支付宝接口，生成二维码后，展示给用户，由用户扫描二维码完成订单支付。
	 * 
	 * @param reqData
	 *            向Alipay post的请求数据
	 * @return API返回数据
	 * @throws Exception
	 */
	public String tradePrecreates(Map<String, Object> reqData, Map<String, String> appData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix();
		Map<String, String> map = this.fillRequestDatas(reqData, SDKConfig.getConfig().getTradePrecreate(), appData);
		String resp = this.post(map, url, AliPayConstants.CHARSET_UTF_8);
		String methodName = map.get("method");
		return this.doProcessResponse(methodName, resp, appData);
	}

	/**
	 * 作用：条码支付<br>
	 * 场景：收银员使用扫码设备读取用户手机支付宝“付款码”/声波获取设备（如麦克风）读取用户手机支付宝的声波信息后，
	 * 将二维码或条码信息/声波信息通过本接口上送至支付宝发起支付。
	 * 
	 * @param reqData
	 *            向Alipay post的请求数据
	 * @return API返回数据
	 * @throws Exception
	 */
	public String tradePay(Map<String, Object> reqData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix();
		Map<String, String> map = this.fillRequestData(reqData, SDKConfig.getConfig().getTradePay());
		String resp = this.post(map, url, AliPayConstants.CHARSET_UTF_8);
		String methodName = map.get("method");
		return this.processResponse(methodName, resp);
	}

	/**
	 * 作用：统一下单<br>
	 * 场景：商户通过该接口进行交易的创建下单
	 * 
	 * @param reqData
	 *            向Alipay post的请求数据
	 * @return API返回数据
	 * @throws Exception
	 */
	public String tradeCreate(Map<String, Object> reqData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix();
		Map<String, String> map = this.fillRequestData(reqData, SDKConfig.getConfig().getTradeCreate());
		String resp = this.post(map, url, AliPayConstants.CHARSET_UTF_8);
		String methodName = map.get("method");
		return this.processResponse(methodName, resp);
	}

	/**
	 * 作用：统一收单交易撤销<br>
	 * 场景：支付交易返回失败或支付系统超时，调用该接口撤销交易。如果此订单用户支付失败，支付宝系统会将此订单关闭；
	 * 如果用户支付成功，支付宝系统会将此订单资金退还给用户。注意：只有发生支付系统超时或者支付结果未知时可调用撤销，
	 * 其他正常支付的单如需实现相同功能请调用申请退款API。提交支付交易后调用【查询订单API】，没有明确的支付结果再调用【撤销订单API】。
	 * 
	 * @param reqData
	 *            向Alipay post的请求数据
	 * @return API返回数据
	 * @throws Exception
	 */
	public String tradeCancle(Map<String, Object> reqData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix();
		Map<String, String> map = this.fillRequestData(reqData, SDKConfig.getConfig().getTradeCancel());
		String resp = this.post(map, url, AliPayConstants.CHARSET_UTF_8);
		String methodName = map.get("method");
		return this.processResponse(methodName, resp);
	}

	/**
	 * 作用：统一收单交易退款查询<br>
	 * 场景：商户可使用该接口查询自已通过alipay.trade.refund提交的退款请求是否执行成功。
	 * 该接口的返回码10000，仅代表本次查询操作成功，不代表退款成功。
	 * 如果该接口返回了查询数据，则代表退款成功，如果没有查询到则代表未退款成功，可以调用退款接口进行重试。重试时请务必保证退款请求号一致。
	 * 
	 * @param reqData
	 *            向Alipay post的请求数据
	 * @return API返回数据
	 * @throws Exception
	 */
	public String tradeRefundQuery(Map<String, Object> reqData ,String privateKey , String publicKey,String appId) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix();
		Map<String, String> map = this.refillRequestData(reqData, SDKConfig.getConfig().getTraderefundQuery(), privateKey ,  publicKey , appId);
		String resp = this.post(map, url, AliPayConstants.CHARSET_UTF_8);
		String methodName = map.get("method");
		return this.processResponses(methodName, resp , publicKey);
	}

	/**
	 * 作用：统一收单交易退款<br>
	 * 场景：当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
	 * 支付宝将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。
	 * 交易超过约定时间（签约时设置的可退款时间）的订单无法进行退款支付宝退款支持单笔交易分多次退款，
	 * 多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。一笔退款失败后重新提交，要采用原来的退款单号。 总退款金额不能超过用户实际支付金额
	 * 
	 * @param reqData
	 *            向Alipay post的请求数据
	 * @return API返回数据
	 * @throws Exception
	 */
	public String tradeRefund(Map<String, String> reqData, String privateKey, String publicKey) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix();
		Map<String, String> map = this.dofillRefundData(reqData, SDKConfig.getConfig().getTradeRefund(), privateKey);
		String resp = this.post(map, url, AliPayConstants.CHARSET_UTF_8);
		String methodName = map.get("method");
		return this.processResponses(methodName, resp, publicKey);
	}

	/**
	 * 作用：统一收单线下交易查询<br>
	 * 场景：该接口提供所有支付宝支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。
	 * 需要调用查询接口的情况：当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知；
	 * 调用支付接口后，返回系统错误或未知交易状态情况；调用alipay.trade.pay，返回INPROCESS的状态；
	 * 调用alipay.trade.cancel之前，需确认支付状态；
	 * 
	 * @param reqData
	 *            向Alipay post的请求数据
	 * @return API返回数据
	 * @throws Exception
	 */
	public String tradeQuery(Map<String, Object> reqData, Map<String, String> appData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix();
		Map<String, String> map = this.fillDataQuery(reqData, SDKConfig.getConfig().getTradeQuery(), appData);
		String resp = this.post(map, url, AliPayConstants.CHARSET_UTF_8);
		String methodName = map.get("method");
		return this.doProcessResponse(methodName, resp, appData);
	}

	/**
	 * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
	 * 
	 * @param xmlStr
	 *            API返回的相应的数据
	 * @return Map类型数据
	 * @throws Exception
	 */
	public String processResponse(String methodName, String respStr) throws Exception {

		JSONObject respjSON = JSONObject.fromObject(respStr);
		String sign = respjSON.getString("sign");
		methodName = methodName.replace(".", "_");
		String rstStr = respjSON.getString(methodName + "_response");
		if (isResponseSignatureValid(sign, rstStr)) {
			return rstStr;
		} else {
			throw new Exception(String.format("Invalid sign value in resp: %s", rstStr));
		}
	}

	public String processResponses(String methodName, String respStr, String publicKey) throws Exception {

		JSONObject respjSON = JSONObject.fromObject(respStr);
		String sign = respjSON.getString("sign");
		methodName = methodName.replace(".", "_");
		String rstStr = respjSON.getString(methodName + "_response");
		if (isResponseSignatureValids(sign, rstStr, publicKey)) {
			return rstStr;
		} else {
			throw new Exception(String.format("Invalid sign value in resp: %s", rstStr));
		}
	}

	public String doProcessResponse(String methodName, String respStr, Map<String, String> appData) throws Exception {

		JSONObject respjSON = JSONObject.fromObject(respStr);
		String sign = respjSON.getString("sign");
		methodName = methodName.replace(".", "_");
		String rstStr = respjSON.getString(methodName + "_response");
		if (doIsResponseSignatureValid(sign, rstStr, appData)) {
			return rstStr;
		} else {
			throw new Exception(String.format("Invalid sign value in resp: %s", rstStr));
		}
	}

	// 扫码支付结果通知
	public String processNotifyResponse(String respStr) throws Exception {
		Map<String, String> map = AliPayUtil.convertResultStringToMap(respStr);
		String sign = map.remove("sign");
		map.remove("sign_type");
		String resp = AliPayUtil.getSignContent(map);
		if (isResponseSignatureValid(sign, resp)) {
			return respStr;
		} else {
			throw new Exception(String.format("Invalid sign value in resp: %s", resp));
		}
	}

	/**
	 * 判断sign是否有效，必须包含sign字段，否则返回false。
	 *
	 * @param reqData
	 *            向Alipay post的请求数据
	 * @return 签名是否有效
	 * @throws Exception
	 */
	public boolean isResponseSignatureValid(String sign, String content) throws Exception {
		// 获取sign 和 content
		if (StringUtils.isBlank(sign)) {
			return false;
		}
		return AliPayUtil.rsa256CheckContent(content, sign, SDKConfig.getConfig().getPublicKey());
	}

	public boolean isResponseSignatureValids(String sign, String content, String publicKey) throws Exception {
		// 获取sign 和 content
		if (StringUtils.isBlank(sign)) {
			return false;
		}
		return AliPayUtil.rsa256CheckContent(content, sign, publicKey);
	}

	public boolean doIsResponseSignatureValid(String sign, String content, Map<String, String> appData)
			throws Exception {
		// 获取sign 和 content
		if (StringUtils.isBlank(sign)) {
			return false;
		}
		return AliPayUtil.rsa256CheckContent(content, sign, appData.get("publickey"));
	}

	public String post(Map<String, String> reqData, String reqUrl, String encoding) {
		Map<String, String> rspData = new HashMap<String, String>();
		AliPayUtil.getLogger().info("请求银联地址:" + reqUrl);
		// 发送后台请求数据
		AliPayRequest hc = new AliPayRequest(reqUrl, 30000, 30000);// 连接超时时间，读超时时间（可自行判断，修改）
		String resultString = "";
		try {
			int status = hc.send(reqData, encoding);
			if (200 == status) {
				resultString = hc.getResult();
				if (null != resultString && !"".equals(resultString)) {
					// 将返回结果转换为map
					Map<String, String> tmpRspData = AliPayUtil.convertResultStringToMap(resultString);
					rspData.putAll(tmpRspData);
				}
			} else {
				AliPayUtil.getLogger().info("返回http状态码[" + status + "]，请检查请求报文或者请求地址是否正确");
			}
		} catch (Exception e) {
			AliPayUtil.getLogger().error(e.getMessage(), e);
		}
		return resultString;
	}

	/**
	 * 功能：http Get方法<br>
	 */
	public static String get(String reqUrl, String encoding) {
		AliPayUtil.getLogger().info("请求银联地址:" + reqUrl);
		// 发送后台请求数据
		AliPayRequest hc = new AliPayRequest(reqUrl, 30000, 30000);
		try {
			int status = hc.sendGet(encoding);
			if (200 == status) {
				String resultString = hc.getResult();
				if (null != resultString && !"".equals(resultString)) {
					return resultString;
				}
			} else {
				AliPayUtil.getLogger().info("返回http状态码[" + status + "]，请检查请求报文或者请求地址是否正确");
			}
		} catch (Exception e) {
			AliPayUtil.getLogger().error(e.getMessage(), e);
		}
		return null;
	}

}
