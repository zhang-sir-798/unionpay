package com.dx.util.domain;

import java.io.File;

public class Constant {

	// 订单状态
	public final static String PAY_STATUS_EXPIRED = "EXPIRED"; // 订单过期
	public final static String PAY_STATUS_UNKONW = "UNKONW"; // 订单未知
	public final static String PAY_STATUS_FAILED = "FAILED"; // 支付失败// 支付中-真实订单创建失败-用户已扫
	public final static String PAY_STATUS_INIT = "INIT"; // 初始态，用户未扫码
	public final static String PAY_STATUS_PAYING = "PAYING"; // 订单进行中// 支付中-真实订单创建成功-用户已扫
	public final static String PAY_STATUS_SUCCESS = "SUCCESS"; // 支付成功
	public final static String PAY_STATUS_COMPLETE = "COMPLETE"; // 业务完成
	public final static String PAY_STATUS_REFUND = "REFUND"; // 转入退款
	public final static String PAY_STATUS_REVOKED = "REVOKED"; // 已撤销(刷卡支付)
	public final static String PAY_STATUS_SUCCESSDEFECT = "SUCCESSDEFECT";// 有缺陷的成功

	public final static String PAY_STATUS_REFUNDPART = "REFUNDPART";// 部分退款
	public final static String PAY_STATUS_REFUND_SUCCESSCOMPLETE = "REFUNDCOMPLETE";// 全部退款,订单表中为终态，禁止继续退款

	public final static String REFUND_STATUS_SUCCESS = "REFUNDSUCCESS";// 对于退款表，此为终态，单次退款成功
	public final static String REFUND_STATUS_ING = "REFUNDING";// 退款申请成功,正在退款中
	public final static String REFUND_STATUS_REQ_F = "REFUNDFAILED";// 退款失败
	public final static String REFUND_STATUS_CLOSE = "REFUNDCLOSE";// 退款失败

	// 业务参数
	public final static String VERSION = "1.0.0";// 版本
	public final static String RESP_UTF8 = "UTF-8"; // 通知业务系统使用的编码
	public static final String T_INSIDE = "True";
	public static final String FALSE_INSIDE_RETCODE = "False";
	public static final String RT_INSIDE = "InsideCode";
	public static final String RT_OUTSIDE = "OutsideCode";
	public static final String RT_ORDERSTATE = "orderState";
	public static final String RT_REFUND_ORDERSTATE = "refundState";
	public static final String QR_BIZ = "Biz";
	public static final String RETURN_PARAM_RETCODE = "respCode";
	public static final String RETURN_PARAM_RETMSG = "respMsg";
	public static final String RESULT_PARAM_SIGN = "sign";

	public static final String RETURN_VALUE_SUCCESS = "SUCCESS";
	public static final String RETURN_VALUE_FAIL = "FAIL";
	public static final String RETURN_VALUE_FAIL_MSG = "通讯异常,";
	// Mq队列名称
	//交易通知
	public static final String QUEUE_CONSUME_4_AT = "queue.nofify.consume.AT";
	//退款通知
	public static final String QUEUE_REFUND_4_AT = "queue.notify.refund.AT";

	public static class WxConstant {
		public final static String TRADE_TYPE_APP = "APP"; // APP支付
		public final static String TRADE_TYPE_JSPAI = "JSAPI"; // 公众号支付或小程序支付
		public final static String TRADE_TYPE_NATIVE = "NATIVE"; // 原生扫码支付
		public final static String TRADE_TYPE_MWEB = "MWEB"; // H5支付
		public final static String TRANS_NOTIFY = "http://xxxx/unionpay/notify/consumeft"; // 微信回调地址
		public final static String TRANS_REFUND = "http://xxxx/notify/refundt"; // 微信回调地址
		public static final String RESP_OK = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

		public static final String RESP_FAIL = "fail";
		public static final String SUCCESS = "SUCCESS";

		public static final String APP = "d4ea2066d45d5e18506b713801757465";

	}

	public static class AlipayConstant {
		public final static String CONFIG_PATH = "alipay" + File.separator + "alipay"; // 支付宝移动支付
		public final static String TRADE_STATUS_WAIT = "WAIT_BUYER_PAY"; // 交易创建,等待买家付款
		public final static String TRADE_STATUS_CLOSED = "TRADE_CLOSED"; // 交易关闭
		public final static String TRADE_STATUS_SUCCESS = "TRADE_SUCCESS"; // 交易成功
		public final static String TRADE_STATUS_FINISHED = "TRADE_FINISHED"; // 交易成功且结束
		public static final String RETURN_ALIPAY_VALUE_SUCCESS = "success";
		public static final String RETURN_ALIPAY_VALUE_FAIL = "fail";
		public final static String TRANS_NOTIFY = "http://xxxx/unionpay/notify/consumefa.html"; // ali回调地址
	}
	
	// 银联B2C
	public static class UnionpayConstant {
		public final static String UNION_FRONT_URL = "http://xxxx/middlepaytrx/notify/unionPay/front";
		public final static String UNION_BACK_URL = "http://xxxx/middlepaytrx/notify/unionPay/back";
	}

	public final static String NOTIFY_UNIONPAYCODE_URL = "http://xxxx/middletrxpay/notify/pay/unionPayCodeNotify"; // 银联二维码回调地址

	public final static String NOTIFY_UNIONPAYCODE_RH_URL = "http://xxxx/middlepaytrx/notify/pay/unionPayCodeRHNotify"; // 融汇通道回调地址

	public final static String NOTIFY_UNIONPAYCODE_RHBIND_URL = "http://xxxx/middlepaytrx/notify/pay/unionPayCodeRHBINDNotify"; // 融汇通道回调地址
	// 按机构身份接入是修改

	public final static String CERTPATH = "/home/app/certs/unionkey/acp_test_sign_inst.pfx";
	// public final static String CERTPATH = "f:/certs/acp_test_sign_inst.pfx";

	public final static String PAYMENT_IP = "127.0.0.1";
	public final static int PAYMENT_PORT = 8888;
	public final static String PAYMENT_CALLBACK = "http://xxxx/middlepaytrx/notify/pay/paySysNotify";

	public final static String QUICK_PAY_CONCODE_OPENK = "OPENK"; // 快捷开通发短信
	public final static String QUICK_PAY_CONCODE_OPENKCHECK = "OPENKCHECK"; // 快捷开通确认
	public final static String QUICK_PAY_CONCODE_CONSUME = "CONSUME"; // 快捷支付发短信
	public final static String QUICK_PAY_CONCODE_SMSCHECK = "SMSCHECK"; // 快捷支付确认
	public final static String QUICK_PAY_CONCODE_FRONT = "FRONT"; // 银联侧开通并支付
	public final static String QUICK_PAY_CONCODE_QUERYOPEN = "QUERYOPEN"; // 快捷开通状态查询
	public final static String QUICK_PAY_CONCODE_QUERYPAY = "QUERYPAY"; // 快捷交易订单状态查询

}
