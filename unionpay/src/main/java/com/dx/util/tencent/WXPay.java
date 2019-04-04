package com.dx.util.tencent;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dx.model.Channel;

/**
 * 微信支付核心步骤，如订单查询等
 */
public class WXPay {
	/**
	 * 向 Map 中添加 appid、mch_id、sub_mch_id、sign <br>
	 * @param reqData
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> fillRequestData(Map<String, String> reqData) throws Exception {

		reqData.put("appid", reqData.get("appid"));
		reqData.put("mch_id", reqData.get("mch_id"));
		reqData.put("sub_mch_id", reqData.get("sub_mch_id"));
		reqData.put("sign", WXPayUtil.generateSignature(reqData, SDKConfig.getConfig().getPrivateKey()));
		return reqData;
	}
	public Map<String, String> dofillRequestData(Map<String, String> reqData ,  String privatekey) throws Exception {

		reqData.put("appid", reqData.get("appid"));
		reqData.put("mch_id", reqData.get("mch_id"));
		reqData.put("sub_mch_id", reqData.get("sub_mch_id"));
		reqData.put("sign", WXPayUtil.generateSignature(reqData, privatekey));
		return reqData;
	}
	public Map<String, String> refillRequestData(Map<String, String> reqData ,Channel channel , String sub_mer_id) throws Exception {

		reqData.put("appid", channel.getApp_id());
		reqData.put("mch_id", channel.getMer_id());
		reqData.put("sub_mch_id", sub_mer_id);
		reqData.put("sign", WXPayUtil.generateSignature(reqData, channel.getPrivate_key()));
		return reqData;
	}
	/**
	 * 判断xml数据的sign是否有效，必须包含sign字段，否则返回false。
	 *
	 * @param reqData
	 *            向wxpay post的请求数据
	 * @return 签名是否有效
	 * @throws Exception
	 */
	public boolean isResponseSignatureValid(Map<String, String> reqData) throws Exception {
		String signStr = reqData.get(WXPayConstants.FIELD_SIGN);
		if (StringUtils.isBlank(signStr)) {
			return false;
		}
		String content = WXPayUtil.getSignContent(reqData, "sign");
		return WXPayUtil.rsa256CheckContent(content, signStr,SDKConfig.getConfig().getPublicKey());
	}
	public boolean doIsResponseSignatureValid(Map<String, String> reqData , String publicKey) throws Exception {
		String signStr = reqData.get(WXPayConstants.FIELD_SIGN);
		if (StringUtils.isBlank(signStr)) {
			return false;
		}
		String content = WXPayUtil.getSignContent(reqData, "sign");
		return WXPayUtil.rsa256CheckContent(content, signStr , publicKey);
	}
	

	public String post(Map<String, String> reqData, String reqUrl, String encoding) {
		Map<String, String> rspData = new HashMap<String, String>();
		WXPayUtil.getLogger().info("请求银联地址:" + reqUrl);
		// 发送后台请求数据
		WXPayRequest hc = new WXPayRequest(reqUrl, 30000, 30000);// 连接超时时间，读超时时间（可自行判断，修改）
		String resultString = "";
		try {
			int status = hc.send(reqData, encoding);
			if (200 == status) {
				resultString = hc.getResult();
				if (null != resultString && !"".equals(resultString)) {
					// 将返回结果转换为map
					Map<String, String> tmpRspData = WXPayUtil.convertString2Map(resultString);
					rspData.putAll(tmpRspData);
				}
			} else {
				WXPayUtil.getLogger().info("返回http状态码[" + status + "]，请检查请求报文或者请求地址是否正确");
			}
		} catch (Exception e) {
			WXPayUtil.getLogger().error(e.getMessage(), e);
		}
		return resultString;
	}

	/**
	 * 功能：http Get方法<br>
	 */
	public static String get(String reqUrl, String encoding) {
		WXPayUtil.getLogger().info("请求银联地址:" + reqUrl);
		// 发送后台请求数据
		WXPayRequest hc = new WXPayRequest(reqUrl, 30000, 30000);
		try {
			int status = hc.sendGet(encoding);
			if (200 == status) {
				String resultString = hc.getResult();
				if (null != resultString && !"".equals(resultString)) {
					return resultString;
				}
			} else {
				WXPayUtil.getLogger().info("返回http状态码[" + status + "]，请检查请求报文或者请求地址是否正确");
			}
		} catch (Exception e) {
			WXPayUtil.getLogger().error(e.getMessage(), e);
		}
		return null;
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
			throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
		}

		if (return_code.equals(WXPayConstants.FAIL)) {
			return respData;
		} else if (return_code.equals(WXPayConstants.SUCCESS)) {
			if (this.isResponseSignatureValid(respData)) {
				return respData;
			} else {
				throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
			}
		} else {
			throw new Exception(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
		}
	}
	
	public Map<String, String> doProcessResponseXml(String xmlStr ,String publicKey) throws Exception {
		String RETURN_CODE = "return_code";
		String return_code;
		Map<String, String> respData = WXPayUtil.xmlToMap(xmlStr);
		if (respData.containsKey(RETURN_CODE)) {
			return_code = respData.get(RETURN_CODE);
		} else {
			throw new Exception(String.format("No `return_code` in XML: %s", xmlStr));
		}

		if (return_code.equals(WXPayConstants.FAIL)) {
			return respData;
		} else if (return_code.equals(WXPayConstants.SUCCESS)) {
			if (this.doIsResponseSignatureValid(respData , publicKey)) {
				return respData;
			} else {
				throw new Exception(String.format("Invalid sign value in XML: %s", xmlStr));
			}
		} else {
			throw new Exception(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
		}
	}

	/**
	 * 作用：查询订单<br>
	 * 场景：该接口提供所有微信支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑。 需要调用查询接口的情况：
	 *  1)	当商户后台、网络、服务器等出现异常，商户系统最终未接收到支付通知； 
	 *  2)	调用支付接口后，返回系统错误或未知交易状态情况； 
	 *  3)	调用刷卡支付API，返回USERPAYING的状态； 
	 *  4)	调用关单或撤销接口API之前，需确认支付状态。
	 * @param reqData
	 *            向wxpay post的请求数据
	 * @return API返回数据
	 * @throws Exception
	 */
	public Map<String, String> orderQuery(Map<String, String> reqData ,String privatekey , String publicKey) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix() + SDKConfig.getConfig().getOrderqueryUrlSuffix();
		this.dofillRequestData(reqData , privatekey);
		String respXml = this.post(reqData, url, WXPayConstants.CHARSET_UTF_8);
		return this.doProcessResponseXml(respXml,publicKey);
	}

	/**
	 * 作用：提交刷卡支付<br>
	 * 场景：收银员使用扫码设备读取微信用户刷卡授权码以后，将二维码或条码信息传送至商户收银台，由商户收银台或者商户后台调用该接口发起支付。
	 * @param reqData
	 *            向wxpay post的请求数据
	 * @return API返回数据
	 * @throws Exception
	 */
	public Map<String, String> microPay(Map<String, String> reqData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix() + SDKConfig.getConfig().getMicropayUrlSuffix();
		this.fillRequestData(reqData);
		String respXml = this.post(reqData, url, WXPayConstants.CHARSET_UTF_8);
		return this.processResponseXml(respXml);
	}
	
	/**
	 * 作用：关闭订单<br>
	 * 场景：以下情况需要调用关单接口：
	 * 1) 商户订单支付失败需要生成新单号重新发起支付，要对原订单号调用关单，避免重复支付；
	 * 2) 系统下单后，用户支付超时，系统退出不再受理，避免用户继续，请调用关单接口。
	 * @param reqData
	 *          向wxpay post的请求数据
	 * @return  API返回数据
	 * @throws Exception
	 */
	public Map<String, String> closeOrder(Map<String, String> reqData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix() + SDKConfig.getConfig().getOrdercloseUrlSuffix();
		this.fillRequestData(reqData);
		String respXml = this.post(reqData, url, WXPayConstants.CHARSET_UTF_8);
		return this.processResponseXml(respXml);
	}
	
	/**
	 * 作用：统一下单<br>
	 * 场景：除被扫支付场景以外，商户系统先通过银联调用该接口在微信支付服务后台生成预支付交易单，
	 * 返回正确的预支付交易回话标识后再按扫码、JSAPI、APP等不同场景生成交易串调起支付。
	 * @param reqData
	 *          向wxpay post的请求数据
	 * @return  API返回数据
	 * @throws Exception
	 */
	public Map<String, String> prepayOrder(Map<String, String> reqData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix() + SDKConfig.getConfig().getPrepayUrlSuffix();
		this.fillRequestData(reqData);
		
		reqData.put("sign", WXPayUtil.generateSignature(reqData, SDKConfig.getConfig().getPrivateKey()));
		String respXml = this.post(reqData, url, WXPayConstants.CHARSET_UTF_8);
		return this.processResponseXml(respXml);
	}
	
	/**
	 * 作用：统一下单<br>
	 * 场景：除被扫支付场景以外，商户系统先通过银联调用该接口在微信支付服务后台生成预支付交易单，
	 * 返回正确的预支付交易回话标识后再按扫码、JSAPI、APP等不同场景生成交易串调起支付。
	 * @param reqData
	 *          向wxpay post的请求数据
	 * @return  API返回数据
	 * @throws Exception
	 */
	public Map<String, String> doPrepayOrder(Map<String, String> reqData ,  String privatekey , String publicKey) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix() + SDKConfig.getConfig().getPrepayUrlSuffix();
		this.dofillRequestData(reqData , privatekey);
		
		reqData.put("sign", WXPayUtil.generateSignature(reqData, privatekey));
		String respXml = this.post(reqData, url, WXPayConstants.CHARSET_UTF_8);
		return this.doProcessResponseXml(respXml , publicKey);
	}
	
	
	/**
	 * 作用：撤销订单<br>
	 * 场景：支付交易返回失败或支付系统超时，调用该接口撤销交易。
	 *     如果此订单用户支付失败，银联会将此订单关闭；
	 *     如果用户支付成功，银联会将此订单资金退还给用户。
     *     注意：7天以内的交易单可调用撤销，其他正常支付的单如需实现相同功能请调用申请退款API。
     *     提交支付交易后调用【查询订单API】，没有明确的支付结果再调用【撤销订单API】。
     *     调用支付接口后请勿立即调用撤销订单API，建议支付后至少15s后再调用撤销订单接口。
	 * @param reqData
	 *          向wxpay post的请求数据
	 * @return  API返回数据
	 * @throws Exception
	 */
	public Map<String, String> reverse(Map<String, String> reqData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix() + SDKConfig.getConfig().getOrderreverseUrlSuffix();
		this.fillRequestData(reqData);
		String respXml = this.post(reqData, url, WXPayConstants.CHARSET_UTF_8);
		return this.processResponseXml(respXml);
	}
	
	/**
	 * 作用：申请退款<br>
	 * 场景：当交易发生之后一段时间内，由于买家或者卖家的原因需要退款时，卖家可以通过退款接口将支付款退还给买家，
	                     银联将在收到退款请求并且验证成功之后，按照退款规则将支付款按原路退到买家帐号上。 
	 *  注意：
	 *  1、交易时间超过一年的订单无法提交退款； 
	 *  2、银联退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。申请退款总金额不能超过订单金额。 一笔退款失败后重新提交，请不要更换退款单号，请使用原商户退款单号。
	 *  3、请求频率限制：150qps，即每秒钟正常的申请退款请求次数不超过150次。错误或无效请求频率限制：6qps，即每秒钟异常或错误的退款申请请求不超过6次。
	 *  4、每个支付订单的部分退款次数不能超过50次。
	 * @param reqData
	 *          向wxpay post的请求数据
	 * @return  API返回数据
	 * @throws Exception
	 */
	public Map<String, String> refund(Map<String, String> reqData ,String privateKey ,String publicKey) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix() + SDKConfig.getConfig().getRefundUrlSuffix();
		this.dofillRequestData(reqData,privateKey);
		String respXml = this.post(reqData, url, WXPayConstants.CHARSET_UTF_8);
		return this.doProcessResponseXml(respXml , publicKey);
	}
	
	/**
	 * 作用：退款查询<br>
	 * 场景：提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，用零钱支付的退款20分钟内到账，银行卡支付的退款3个工作日后重新查询退款状态。
	 * @param reqData
	 *          向wxpay post的请求数据
	 * @return  API返回数据
	 * @throws Exception
	 */
	public Map<String, String> refundQuery(Map<String, String> reqData ,Channel channel , String sub_mer_id) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix() + SDKConfig.getConfig().getQrysingleUrlSuffix();
		this.refillRequestData(reqData , channel , sub_mer_id);
		String respXml = this.post(reqData, url, WXPayConstants.CHARSET_UTF_8);
		return this.doProcessResponseXml(respXml , channel.getPublic_key());
	}
	
	/**
	 * 作用：查询多笔退款<br>
	 * 场景：提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，用零钱支付的退款20分钟内到账，银行卡支付的退款3个工作日后重新查询退款状态。
	 * @param reqData
	 *          向wxpay post的请求数据
	 * @return  API返回数据
	 * @throws Exception
	 */
	public Map<String, String> refundAllQuery(Map<String, String> reqData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix() + SDKConfig.getConfig().getQrymultipleUrlSuffix();
		this.fillRequestData(reqData);
		String respXml = this.post(reqData, url, WXPayConstants.CHARSET_UTF_8);
		return this.processResponseXml(respXml);
	}
	
	/**
	 * 作用：下属商户录入<br>
	 * 场景： 收单机构将下属特约商户基本资料信息通过银联报备给微信，在微信支付侧生成特约商户识别码后方可支付。
	 * 特约商户识别码是区分子商户交易、结算和清分的标志。
	 * 收单机构调用该接口将特约商户资料通过银联提交给微信侧，微信根据提交的资料情况判断商户资料正确性，并通过银联向收单机构返回识别码；
	 * 如商户资料有误，返回相应的错误码。
	 * @param reqData
	 *          向wxpay post的请求数据
	 * @return  API返回数据
	 * @throws Exception
	 */
	public Map<String, String> submchAdd(Map<String, String> reqData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix() + SDKConfig.getConfig().getSubmchaddUrlSuffix();
		this.fillRequestData(reqData);
		String respXml = this.post(reqData, url, WXPayConstants.CHARSET_UTF_8);
		return this.processResponseXml(respXml);
	}
	
	/**
	 * 作用：下属商户查询<br>
	 * 场景：提供给收单机构报备后的商户查询。通过MCHID（识别码），返回商户全部资料信息。
	 * @param reqData
	 *          向wxpay post的请求数据
	 * @return  API返回数据
	 * @throws Exception
	 */
	public Map<String, String> submchQry(Map<String, String> reqData) throws Exception {
		String url = SDKConfig.getConfig().getTransUrlPrefix() + SDKConfig.getConfig().getSubmchqryUrlSuffix();
		this.fillRequestData(reqData);
		String respXml = this.post(reqData, url, WXPayConstants.CHARSET_UTF_8);
		return this.processResponseXml(respXml);
	}

}
