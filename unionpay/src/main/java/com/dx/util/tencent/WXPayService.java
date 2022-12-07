package com.dx.util.tencent;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dx.model.Channel;
import com.dx.model.Merchant;
import com.dx.model.OrderDetail;
import com.dx.model.RefundDetail;
import com.dx.service.OrderDetailService;
import com.dx.util.domain.Constant;
import com.dx.util.domain.RetEnum;

@SuppressWarnings("unused")
public class WXPayService {

	private static final Log _log = LogFactory.getLog(WXPayService.class);
	
	private static WXPay wxpay;
	// out_trade_no 商户订单号 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母，且在同一个商户号下唯一
	// 刷卡支付、统一下单时生成， 其他操作如查询订单，撤销等需手动录入已生成的out_trade_no
	private static String out_trade_no = "11" + System.currentTimeMillis();
	// private String out_trade_no = "11";

	static {
		wxpay = new WXPay();
	}

	public static void main(String[] args) throws Exception {
		WXPayService wtest = new WXPayService();

		// 下属商户录入
		 //wtest.doSubmchManageAdd();
		// 下属商户查询
		// wtest.doSubmchQry();

		// 关闭订单
		// wtest.doOrderClose();
		// 撤销订单
		// wtest.doOrderReverse();
		// 申请退款
		// wtest.doOrderRefund();
		// 查询单笔退款
		 //wtest.doOrderQrySingle();
		// 查询所有退款
		// wtest.doOrderQryMultiple();
		// 异步通知
		// wtest.doParseNotifyInfo();

		// 刷卡支付(被扫)
		// wtest.doOrderMicoPay();
		// 统一下单(主扫)
		// wtest.doOrderPrePay();
		// 查询订单
		// wtest.doOrderQuery();

	}

	public void doOrderMicoPay() {
		System.out.println("刷卡支付");
		HashMap<String, String> data = new HashMap<String, String>();
		/**
		 * 组装请求报文
		 */
		data.put("auth_code", "11"); // 扫码支付授权码，设备读取用户微信中的条码或者二维码信息（注：用户刷卡条形码规则：18位纯数字，以10、11、12、13、14、15开头）
		data.put("fee_type", "CNY"); // 符合ISO 4217标准的三位字母代码，默认人民币：CNY
		data.put("total_fee", "1"); // 订单总金额，单位为分，只能为整数
		data.put("device_info", "11"); // 终端设备号(商户自定义，如门店编号)
		data.put("body", "腾讯充值中心-QQ会员充值"); // 商品或支付单简要描述，格式要求：门店品牌名-城市分店名-实际商品名称
		List<Object> goodsDetails = new LinkedList<Object>();
		Map<String, Object> goodsDetail = new LinkedHashMap<String, Object>();
		goodsDetail.put("goods_id", "商品编码"); // 商品编码 由半角的大小写字母、数字、中划线、下划线中的一种或几种组成
		goodsDetail.put("wxpay_goods_id", "1001"); // 微信支付定义的统一商品编号（没有可不传）
		goodsDetail.put("goods_name", "iPhone6s 16G"); // 商品的实际名称
		goodsDetail.put("quantity", 1); // 用户购买的数量
		goodsDetail.put("price", 11); // 单位为：分。如果商户有优惠，需传输商户优惠后的单价(例如：用户对一笔100元的订单使用了商场发的优惠券100-50，则活动商品的单价应为原单价-50)
		goodsDetails.add(goodsDetail);
		Map<String, Object> detail = new LinkedHashMap<String, Object>();
		/**
		 * 订单原价 cost_price 1.商户侧一张小票订单可能被分多次支付，订单原价用于记录整张小票的交易金额。
		 * 2.当订单原价与支付金额不相等，则不享受优惠。 3.该字段主要用于防止同一张小票分多次支付，以享受多次优惠的情况，正常支付订单不必上传此参数。
		 */
		detail.put("cost_price", 11);
		detail.put("receipt_id", "wx123"); // 商家小票ID
		detail.put("goods_detail", goodsDetails); // 单品信息，使用Json数组格式提交
		data.put("detail", JsonUtil.toJsonNotNull(detail)); // 单品优惠活动信息
		data.put("attach", "说明"); // 商家数据包，原样返回
		data.put("spbill_create_ip", "8.8.8.8"); // APP和网页支付提交用户端Ip，Native支付填调用微信支付API的机器IP。
		data.put("goods_tag", "WXG"); // 订单优惠标记，代金券或立减优惠功能的参数
		data.put("notify_url", "11"); // 接收银联异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
		data.put("trade_type", "JSAPI"); // JSAPI 公众号支付 NATIVE 扫码支付 APP APP支付
		data.put("limit_pay", "no_credit"); // no_credit--指定不能使用信用卡支付
		data.put("openid", "11"); // 用户在商户appid 下的唯一标识
		data.put("sub_openid", "11"); // 子商户appid下用户唯一标识，如需返回则请求时需要传sub_appid
		Map<String, Object> identity = new LinkedHashMap<String, Object>();
		identity.put("type", "IDCARD"); // 证件类型
		identity.put("number", "11"); // 证件号，如身份证号
		identity.put("name", "张三"); // 证件姓名
		data.put("identity", JsonUtil.toJsonNotNull(identity)); // 实名支付信息
		// 场景信息
		data.put("scene_info",
				"{\"store_info\" : {\"id\": \"SZTX001\",\"name\": \"腾大餐厅\",\"area_code\": \"440305\",\"address\": \"科技园中一路腾讯大厦\"}}");
		data.put("channel_id", SDKConfig.getConfig().getChannelId()); // 渠道商商户号 微信支付分配给收单服务商的 ID
		// 商户订单号 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母，且在同一个商户号下唯一
		data.put("out_trade_no", out_trade_no);
		data.put("nonce_str", WXPayUtil.generateUUID()); // 随机字符串，不长于32位。推荐随机数生成算法

		try {
			wxpay.microPay(data); // 提交刷卡支付
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> doOrderQuery(OrderDetail orderDetail, String privateKey ,String publicKey) {
		_log.info("查询订单");
		HashMap<String, String> retMap = new HashMap<String, String>();
		HashMap<String, String> data = new HashMap<String, String>();
		/**
		 * 组装请求报文
		 */
		// data.put("sub_appid", SDKConfig.getConfig().getSubAppid()); // 子商户公众账号ID
		// 微信分配的子商户公众账号ID
		data.put("channel_id", orderDetail.getChannel_id()); // 渠道商商户号 微信支付分配给收单服务商的 ID
		data.put("out_trade_no", orderDetail.getSerial_no()); // 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母，且在同一个商户号下唯一
		data.put("nonce_str", WXPayUtil.generateUUID()); // 随机字符串，不长于32位。推荐随机数生成算法
		data.put("appid", orderDetail.getApp_id());
		data.put("mch_id", orderDetail.getChan_mer_id());
		data.put("sub_mch_id", orderDetail.getSub_mch_id());
		
		Map<String, String> msg = new HashMap<String, String>();
		try {
			msg = wxpay.orderQuery(data ,privateKey,publicKey); // 查询订单
		} catch (Exception e) {
			e.printStackTrace();
			retMap.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_PARAM_WX.getCode());
			retMap.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_PARAM_WX.getMessage() + "通讯超时。");
			return retMap;
		}
		if (WXPayConstants.FAIL.equals(msg.get("result_code"))) {
			retMap.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_PARAM_WX.getCode());
			retMap.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_PARAM_WX.getMessage() + msg.get("err_code_des"));
			return retMap;
		}
		
		msg.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_SUCCESS.getCode());
		msg.put(Constant.RT_REFUND_ORDERSTATE, orderState(msg));
		return msg;

	}
	public String orderState(Map<String,String> msg) {
		String result =Constant.PAY_STATUS_UNKONW;
		if(msg.get("trade_state") == null) {
			return Constant.PAY_STATUS_FAILED;
		}
		
		String trade_state = msg.get("trade_state");
		switch (trade_state) {
		case "SUCCESS":
			result = Constant.PAY_STATUS_SUCCESS;
			break;
		case "REFUND":
			result = Constant.PAY_STATUS_REFUND;
			break;
		case "NOTPAY":
			result = Constant.PAY_STATUS_INIT;
			break;
		case "CLOSED":
			result = Constant.PAY_STATUS_EXPIRED;
			break;
		case "REVOKED":
			result = Constant.PAY_STATUS_REVOKED;
			break;
		case "USERPAYING":
			result = Constant.PAY_STATUS_PAYING;
			break;
		case "PAYERROR":
			result = Constant.PAY_STATUS_FAILED;
			break;
		default:	
			return result = Constant.PAY_STATUS_UNKONW;
		}
		return result;
	}
	public String refundState(Map<String,String> msg) {
		String result =Constant.PAY_STATUS_UNKONW;
		if(msg.get("refund_status") == null) {
			return Constant.PAY_STATUS_FAILED;
		}
		
		String trade_state = msg.get("refund_status");
		switch (trade_state) {
		case "SUCCESS":
			result = Constant.REFUND_STATUS_SUCCESS;
			break;
		case "REFUNDCLOSE":
			result = Constant.REFUND_STATUS_CLOSE;
			break;
		case "PROCESSING":
			result = Constant.REFUND_STATUS_ING;
			break;
		case "CHANGE":
			result = Constant.REFUND_STATUS_REQ_F;
			break;
		default:	
			return result = Constant.PAY_STATUS_UNKONW;
		}
		return result;
	}

	// 撤销订单

	public void doOrderReverse() {
		System.out.println("撤销订单");
		HashMap<String, String> data = new HashMap<String, String>();
		/**
		 * 组装请求报文
		 */
		data.put("out_trade_no", out_trade_no); // 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母，且在同一个商户号下唯一
		data.put("nonce_str", WXPayUtil.generateUUID()); // 随机字符串，不长于32位。推荐随机数生成算法
		data.put("channel_id", SDKConfig.getConfig().getChannelId()); // 渠道商商户号 微信支付分配给收单服务商的 ID
		try {
			wxpay.reverse(data); // 撤销订单
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 申请退款

	public Map<String, String> doOrderRefund(Map<String,String> params ,String privateKey ,String publicKey) {
		System.out.println("申请退款 ");
		HashMap<String, String> data = new HashMap<String, String>();
		HashMap<String, String> retMap = new HashMap<String, String>();
		/**
		 * 组装请求报文
		 */
		data.put("out_trade_no", params.get("out_trade_no")); // 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母，且在同一个商户号下唯一
		data.put("nonce_str", WXPayUtil.generateUUID()); // 随机字符串，不长于32位。推荐随机数生成算法
		data.put("out_refund_no", params.get("out_trade_no")); // 商户退款单号 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母，同一退款单号多次请求只退一笔
		data.put("refund_fee", params.get("refund_fee")); // 退款总金额，单位为分，只能为整数，可部分退款
		data.put("total_fee", params.get("total_fee")); // 订单总金额，单位为分，只能为整数
		data.put("refund_fee_type", "CNY"); // 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY
		/**
		 * refund_account 退款资金来源 仅针对老资金流商户使用
		 * REFUND_SOURCE_UNSETTLED_FUNDS---未结算资金退款（默认使用未结算资金退款）
		 * REFUND_SOURCE_RECHARGE_FUNDS---可用余额退款
		 */
		//data.put("refund_account", "REFUND_SOURCE_UNSETTLED_FUNDS");
		data.put("refund_desc", params.get("refund_desc")); // 退款原因 若商户传入，会在下发给用户的退款消息中体现退款原因
		data.put("notify_url",  Constant.WxConstant.TRANS_REFUND); // 退款结果通知url 异步接收银联退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数。
		data.put("channel_id",  params.get("channel_id")); // 渠道商商户号 微信支付分配给收单服务商的 ID
		
		data.put("appid", params.get("appid"));
		data.put("mch_id", params.get("mch_id"));
		data.put("sub_mch_id", params.get("sub_mch_id"));
		
		Map<String, String> msg = new HashMap<String, String>();
		try {
			msg =wxpay.refund(data, privateKey , publicKey); // 申请退款
		} catch (Exception e) {
			e.printStackTrace();
			retMap.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_PARAM_WX.getCode());
			retMap.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_PARAM_WX.getMessage() + "通讯超时。");
			return retMap;
		}
		
		if (!WXPayConstants.SUCCESS.equals(msg.get("result_code"))) {
			retMap.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_PARAM_WX.getCode());
			retMap.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_PARAM_WX.getMessage() + msg.get("err_code_des"));
			return retMap;
		}
		
		msg.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_SUCCESS.getCode());
		msg.put(Constant.RETURN_PARAM_RETMSG, "申请"+RetEnum.RET_SUCCESS.getMessage() );
		
		return msg;
	}

	// 关闭订单

	public void doOrderClose() {
		System.out.println("关闭订单");
		HashMap<String, String> data = new HashMap<String, String>();
		/**
		 * 组装请求报文
		 */
		data.put("out_trade_no", out_trade_no); // 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母，且在同一个商户号下唯一
		data.put("nonce_str", WXPayUtil.generateUUID()); // 随机字符串 商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
		data.put("channel_id", SDKConfig.getConfig().getChannelId()); // 渠道商商户号 微信支付分配给收单服务商的 ID
		try {
			wxpay.closeOrder(data); // 关闭订单
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 查询单笔退款

	public Map<String, String> doOrderQrySingle(RefundDetail refundDetail,Channel channel) {
		System.out.println("查询单笔退款");
		HashMap<String, String> retMap = new HashMap<String, String>();
		HashMap<String, String> data = new HashMap<String, String>();
		/**
		 * 组装请求报文
		 */
		data.put("out_trade_no", refundDetail.getSerial_no()); // 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母，且在同一个商户号下唯一
		data.put("out_refund_no", refundDetail.getRefund_no()); // 商户退款单号 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母，同一退款单号多次请求只退一笔
		data.put("nonce_str", WXPayUtil.generateUUID()); // 随机字符串，不长于32位。推荐随机数生成算法
		data.put("channel_id", channel.getChannel_id()); // 渠道商商户号 微信支付分配给收单服务商的 ID
		Map<String, String> msg = new HashMap<String, String>();
		try {
			msg =wxpay.refundQuery(data , channel ,refundDetail.getSub_mer_id()); // 查询单笔退款
		} catch (Exception e) {
			e.printStackTrace();
			retMap.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_PARAM_WX.getCode());
			retMap.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_PARAM_WX.getMessage() + "通讯超时。");
			return retMap;
		}
		
		if (WXPayConstants.FAIL.equals(msg.get("result_code"))) {
			retMap.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_PARAM_WX.getCode());
			retMap.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_PARAM_WX.getMessage() + msg.get("err_code_des"));
			return retMap;
		}
		
		msg.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_SUCCESS.getCode());
		msg.put(Constant.RT_ORDERSTATE, refundState(msg));
		
		return msg;
	}

	// 查询所有退款

	public void doOrderQryMultiple() {
		System.out.println("查询所有退款");
		HashMap<String, String> data = new HashMap<String, String>();
		/**
		 * 组装请求报文
		 */
		data.put("out_trade_no", out_trade_no); // 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母，且在同一个商户号下唯一
		data.put("out_refund_no", out_trade_no); // 商户退款单号 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母，同一退款单号多次请求只退一笔
		data.put("nonce_str", WXPayUtil.generateUUID()); // 随机字符串，不长于32位。推荐随机数生成算法
		data.put("offset", "0"); // 记录起始位置
		data.put("count", "10"); // 每页笔数 每页返回记录数，最大值限制为20
		data.put("channel_id", SDKConfig.getConfig().getChannelId()); // 渠道商商户号 微信支付分配给收单服务商的 ID
		try {
			wxpay.refundAllQuery(data); // 查询所有退款
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 下属商户录入

	public Map<String, String> doSubmchManageAdd(Merchant merchant) {
		_log.info("微信侧接口--下属商户录入");
		
		String business = "11";
		
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("merchant_name", merchant.getMer_name());// 商户名称  该名称是公司主体全称，绑定公众号时会对主体一致性校验
		data.put("merchant_shortname", merchant.getShort_name()); // 商户简称  该名称是显示给消费者看的商户名称
		data.put("service_phone", merchant.getLegal_contact()); // 客服电话  方便银联在必要时能联系上商家，会在支付详情展示给消费者
		data.put("contact", merchant.getLegal_name()); // 联系人
		data.put("contact_phone", merchant.getLegal_contact()); // 联系电话
		data.put("contact_email", ""); // 联系邮箱
		data.put("business", business); // 经营类目   行业类目，请填写对应的ID 
		data.put("contact_wechatid_type", ""); // 联系人微信账号类型
		data.put("contact_wechatid", ""); // 联系人微信帐号  微信号：打开微信，在"个人信息"中查看到的"微信号"
		data.put("merchant_remark", merchant.getMer_no()); // 商户备注    同一个受理机构，特约商户“商户备注”唯一。商户备注重复时，生成商户识别码失败，并返回提示信息“商户备注已存在，请修改后重新提交”
		data.put("channel_id", SDKConfig.getConfig().getChannelId()); //渠道商商户号  微信支付分配给收单服务商的 ID
		Map<String, String> respData =null;
		try {
			respData = wxpay.submchAdd(data); // 下属商户录入
		} catch (Exception e) {
			e.printStackTrace();
		}		
		respData.put("WXC_business", business);
		
		return respData;
	}

	// 下属商户查询

	public void doSubmchQry() {
		System.out.println("下属商户查询");
		HashMap<String, String> data = new HashMap<String, String>();
		/**
		 * 组装请求报文
		 */
		data.put("merchant_remark", "101");// 商户备注 同一个受理机构，特约商户“商户备注”唯一
		data.put("channel_id", SDKConfig.getConfig().getChannelId()); // 渠道商商户号 微信支付分配给收单服务商的 ID
		try {
			wxpay.submchQry(data); // 下属商户查询
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 统一下单test

	public Map<String, String> doOrderPrePay(Map<String, String> commonData , String privatekey , String publicKey) {
		_log.info("统一下单");
		HashMap<String, String> retMap = new HashMap<String, String>();
		HashMap<String, String> data = new HashMap<String, String>();

		/**
		 * 组装请求报文
		 */
		data.put("out_trade_no", commonData.get("out_trade_no")); // 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母，且在同一个商户号下唯一。
		data.put("fee_type", "CNY"); // 符合ISO 4217标准的三位字母代码，默认人民币：CNY
		data.put("total_fee", commonData.get("total_fee")); // 订单总金额，只能为整数
		data.put("device_info", "WEB"); // 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
		data.put("trade_type", "JSAPI"); // 交易类型 JSAPI:公众号支付 NATIVE:扫码支付 APP:APP支付
		data.put("nonce_str", WXPayUtil.generateUUID()); // 随机字符串，不长于32位。推荐随机数生成算法
		data.put("body", commonData.get("body")); // 商品或支付单简要描述，格式要求：门店品牌名-城市分店名-实际商品名称
		data.put("spbill_create_ip", commonData.get("spbill_create_ip")); // 终端IP
																			// APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
		data.put("notify_url", Constant.WxConstant.TRANS_NOTIFY); // 通知地址, 接收银联异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
		data.put("sign_type", "RSA");
		data.put("channel_id", commonData.get("channel_id")); // 渠道商商户号 微信支付分配给收单服务商的 ID
		data.put("time_start", commonData.get("time_start")); // 交易起始时间
																// 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010
		data.put("time_expire", commonData.get("time_expire"));
		// data.put("time_start", TimeUtil.getFormatTime(date, JsonUtil.TIME_FORMAT));
		// // 交易起始时间 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为11
		// data.put("time_expire", TimeUtil.getFormatTime(TimeUtil.addDay(date,
		// JsonUtil.TIME_EXPIRE),JsonUtil.TIME_FORMAT));
		data.put("openid", commonData.get("openid"));

		data.put("appid", commonData.get("appid"));
		data.put("mch_id", commonData.get("mch_id"));
		data.put("sub_mch_id", commonData.get("sub_mch_id"));

		Map<String, String> msg = new HashMap<String, String>();
		try {
			msg = wxpay.doPrepayOrder(data, privatekey , publicKey);
			_log.info("VVVV 微信侧统一下单 respMessage：" + msg);

		} catch (Exception e) {
			e.printStackTrace();
			retMap.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_PARAM_WX.getCode());
			retMap.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_PARAM_WX.getMessage() + "通讯超时。");
			return retMap;
		}

		if (WXPayConstants.FAIL.equals(msg.get("result_code"))) {
			retMap.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_PARAM_WX.getCode());
			retMap.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_PARAM_WX.getMessage() + msg.get("err_code_des"));
			return retMap;
		}
		msg.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_SUCCESS.getCode());
		return msg;
	}

	public void doParseNotifyInfo() throws Exception {
		System.out.println("支付结果通知");
		String notifyInfo = "11";
		wxpay.processResponseXml(notifyInfo);
	}

}
