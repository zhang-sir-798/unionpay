package com.dx.service;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dx.dao.OrderDetailDao;
import com.dx.dao.RefundDetailDao;
import com.dx.model.Channel;
import com.dx.model.JsapiModel;
import com.dx.model.OrderDetail;
import com.dx.model.RefundDetail;
import com.dx.util.alibaba.AliPayService;
import com.dx.util.alibaba.AliPayUtil;
import com.dx.util.dbconfig.DataSource;
import com.dx.util.domain.Constant;
import com.dx.util.domain.RetEnum;
import com.dx.util.domain.Retutil;
import com.dx.util.domain.TrxEnum;
import com.dx.util.mq.MchNotifyComponent;
import com.dx.util.tencent.SDKConfig;
import com.dx.util.tencent.WXPayService;
import com.dx.util.tencent.WXPayUtil;
import com.dx.util.tools.Http;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName ScanCodeUnifiedOrderService
 * @Description 聚合码统一下单
 * @author zhangsir
 * @Date 2018年12月3日 下午3:29:00
 * @version 1.0.0
 * 
 */
@Service
@SuppressWarnings({ "unused" })
public class QrScanRefundOrderService extends MchNotifyComponent {

	private static final Log _log = LogFactory.getLog(OrderDetailService.class);

	@Autowired
	private WXPayService wxPayService;
	@Autowired
	private AliPayService aliPayService;
	@Autowired
	private OrderDetailDao orderDetailDao;
	@Autowired
	private RouteMappingService routeService;
	@Autowired
	private RefundDetailDao refundDetailDao;

	@DataSource("King")
	public Map<String, String> refundOrderQuery(String params) throws Exception {

		JSONObject jsonParam = JSONObject.parseObject(params);
		Map<String, String> respMsg = new HashMap<String, String>();
		Map<String, String> bizResult = new HashMap<String, String>();
		RefundDetail refundDetail = jsonParam.getObject("refundDetail", RefundDetail.class);
		Channel channel = jsonParam.getObject("channel", Channel.class);
		String trxType = refundDetail.getTrx_type();
		switch (trxType) {
		case "010201":
			respMsg = aliPayService.doTradeRefundQuery(refundDetail , channel.getPrivate_key(), channel.getPublic_key(),channel.getApp_id());
			break;
		case "010101":
			respMsg = wxPayService.doOrderQrySingle(refundDetail, channel);
			break;
		default:
			return Retutil.createFailBiz(null, null);
		}

		JSONObject respJson = new JSONObject();
		respJson.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_SUCCESS.getCode());
		respJson.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_SUCCESS.getMessage());
		respJson.put("refundAmount", refundDetail.getRefund_amount());
		respJson.put("merNo", refundDetail.getMer_no());
		respJson.put("orderNo", refundDetail.getOrder_no());
		respJson.put("refundNo", refundDetail.getRefund_no());
		respJson.put("orderAmount", refundDetail.getTotal_order_amount());

		respJson.put("refundState", respMsg.get(Constant.RT_REFUND_ORDERSTATE));

		bizResult = Retutil.createSuccessBizWithSign(respJson, refundDetail.getReq_key());

		return bizResult;

	}

	@DataSource("King")
	public Map<String, String> refundOrder(String jsonParam) throws Exception {
		Map<String, String> bizResult = null;
		Map<String, String> respMsg = new HashMap<String, String>();
		JSONObject jsonParams = JSONObject.parseObject(jsonParam);
		String refundNo = jsonParams.getString("refundNo"); // 退款请求单号
		String refundAmount = jsonParams.getString("refundAmount"); // 退款金额（单位分）
		String refundUrl = jsonParams.getString("refundUrl"); // 退款结果回调URL
		OrderDetail orderDetail = jsonParams.getObject("orderDetail", OrderDetail.class);// 系统商户
		Channel channel = jsonParams.getObject("channel", Channel.class);// 系统商户
		String routeCode = orderDetail.getTrx_type();

		switch (routeCode) {
		case "010101":
			_log.info("VVVV 开始微信退款请求request message :" + jsonParam);
			respMsg = refund4Tencent(orderDetail, refundNo, refundAmount, jsonParams,channel.getPrivate_key(),channel.getPrivate_key());
			break;
		case "010201":
			// alibaba
			_log.info("VVVV 开始阿里退款请求request message :" + jsonParam);
			respMsg = refund4AliPay(orderDetail, refundNo, refundAmount, jsonParams ,channel.getPrivate_key(),channel.getPrivate_key());
			break;
		default:
			return Retutil.createFailBiz(RetEnum.RET_S_ORDER_INVALID, "请联系商家。");
		}

		// 统一处理返回信息对下游
		bizResult = Retutil.createInnoBiz(respMsg, jsonParams, orderDetail);
		return bizResult;
	}

	@DataSource("King")
	@Transactional(rollbackFor = Exception.class)
	public Map<String, String> refund4AliPay(OrderDetail orderDetail, String refundNo, String refundAmount,
			JSONObject jsonParams,String privateKey,String publicKey) throws Exception {
		Map<String, String> bizResult = new HashMap<String, String>();
		Map<String, String> commonData = new HashMap<String, String>();

		commonData.put("out_request_no", refundNo); // 请求退款接口时，传入的退款请求号，如果在退款请求时未传入，则该值为创建交易时的外部交易号
		commonData.put("refund_amount", refundAmount); // 需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
		commonData.put("out_trade_no", orderDetail.getSerial_no()); // 原支付请求的商户订单号,和支付宝交易号不能同时为空
		commonData.put("refund_reason", jsonParams.getString("refundReason")); // 原支付请求的商户订单号,和支付宝交易号不能同时为空

		Map<String, String> respMsg = aliPayService.doTradeRefund(commonData, privateKey, publicKey);

		if (RetEnum.RET_SUCCESS.getCode().equals(respMsg.get(Constant.RETURN_PARAM_RETCODE))) {

			bizResult.put(Constant.RETURN_PARAM_RETCODE, respMsg.get(Constant.RETURN_PARAM_RETCODE));
			bizResult.put(Constant.RETURN_PARAM_RETMSG, respMsg.get(Constant.RETURN_PARAM_RETMSG));
			bizResult.put(Constant.RT_REFUND_ORDERSTATE, Constant.REFUND_STATUS_SUCCESS);

			if (orderDetail.getOrder_amount().equals(respMsg.get("refund_fee"))) {
				orderDetail.setState(Constant.PAY_STATUS_REFUND_SUCCESSCOMPLETE);
			} else {
				orderDetail.setState(Constant.PAY_STATUS_REFUNDPART);
			}
			// 把appid 等信息存上 用于退款 查询等
			orderDetailDao.updateByPrimaryKey(orderDetail);

		} else if ("qsuccess".equals(respMsg.get(Constant.RETURN_PARAM_RETCODE))) {
			bizResult.put(Constant.RETURN_PARAM_RETCODE, "1000");
			bizResult.put(Constant.RETURN_PARAM_RETMSG, respMsg.get(Constant.RETURN_PARAM_RETMSG));
			bizResult.put(Constant.RT_REFUND_ORDERSTATE, Constant.REFUND_STATUS_ING);

		} else {
			bizResult.put(Constant.RETURN_PARAM_RETCODE, respMsg.get(Constant.RETURN_PARAM_RETCODE));
			bizResult.put(Constant.RETURN_PARAM_RETMSG, respMsg.get(Constant.RETURN_PARAM_RETMSG));
			bizResult.put(Constant.RT_REFUND_ORDERSTATE, Constant.REFUND_STATUS_REQ_F);

		}
		// TODO 创建退款订单
		RefundDetail refundDetail = refundDetailDao.selectByRefundNo(refundNo);
		if (refundDetail == null) {
			RefundDetail reDetail = new RefundDetail();
			reDetail.setMer_no(jsonParams.getString("merNo"));
			reDetail.setRefund_no(jsonParams.getString("refundNo"));
			reDetail.setNotify_url(jsonParams.getString("refundUrl"));
			reDetail.setRefund_reason(jsonParams.getString("refundReason"));
			reDetail.setTotal_order_amount(orderDetail.getOrder_amount());
			reDetail.setRefund_amount(jsonParams.getString("refundAmount"));
			reDetail.setOrder_no(orderDetail.getOrder_no());
			reDetail.setSerial_no(orderDetail.getSerial_no());
			reDetail.setRefund_state(bizResult.get(Constant.RT_REFUND_ORDERSTATE));
			reDetail.setReq_key(orderDetail.getReq_key());
			reDetail.setTrx_type(orderDetail.getTrx_type());
			if (bizResult.get(Constant.RT_REFUND_ORDERSTATE).equals(Constant.REFUND_STATUS_SUCCESS)) {
				reDetail.setNotify_time(AliPayUtil.pDates(respMsg.get("gmt_refund_pay")));
				reDetail.setRefund_detail_item_list(respMsg.get("refund_detail_item_list"));
			}
			reDetail.setChannel_id(orderDetail.getChannel_id());
			refundDetailDao.insert(reDetail);
		} else {
			refundDetail.setRefund_state(bizResult.get(Constant.RT_REFUND_ORDERSTATE));
			if (bizResult.get(Constant.RT_REFUND_ORDERSTATE).equals(Constant.REFUND_STATUS_SUCCESS)) {
				refundDetail.setNotify_time(AliPayUtil.pDates(respMsg.get("gmt_refund_pay")));
				refundDetail.setRefund_detail_item_list(respMsg.get("refund_detail_item_list"));
			}
			refundDetailDao.updateByPrimaryKey(refundDetail);
		}

		if (Constant.REFUND_STATUS_SUCCESS.equals(bizResult.get(Constant.RT_REFUND_ORDERSTATE))) {
			// 通知
			doRefundNotify(refundDetail, true);
		}

		return bizResult;
	}

	@DataSource("King")
	@Transactional(rollbackFor = Exception.class)
	public Map<String, String> refund4Tencent(OrderDetail orderDetail, String refundNo, String refundAmount,
			JSONObject jsonParams,String privateKey ,String publicKey) throws Exception {

		Map<String, String> bizResult = new HashMap<String, String>();
		Map<String, String> commonData = new HashMap<String, String>();
		commonData.put("out_trade_no", orderDetail.getSerial_no());
		commonData.put("out_refund_no", refundNo);
		commonData.put("refund_fee", refundAmount);
		commonData.put("total_fee", orderDetail.getOrder_amount());
		commonData.put("refund_desc", jsonParams.getString("refundReason"));
		commonData.put("notify_url", jsonParams.getString("refundUrl"));
		commonData.put("channel_id", orderDetail.getChannel_id());

		commonData.put("appid", orderDetail.getApp_id());
		commonData.put("mch_id", orderDetail.getChan_mer_id());
		commonData.put("sub_mch_id", orderDetail.getSub_mch_id());

		Map<String, String> respMsg = wxPayService.doOrderRefund(commonData, privateKey , publicKey);

		if (!RetEnum.RET_SUCCESS.getCode().equals(respMsg.get(Constant.RETURN_PARAM_RETCODE))) {

			bizResult.put(Constant.RETURN_PARAM_RETCODE, respMsg.get(Constant.RETURN_PARAM_RETCODE));
			bizResult.put(Constant.RETURN_PARAM_RETMSG, respMsg.get(Constant.RETURN_PARAM_RETMSG));
			bizResult.put(Constant.RT_REFUND_ORDERSTATE, Constant.REFUND_STATUS_REQ_F);
		} else {

			bizResult.put(Constant.RETURN_PARAM_RETCODE, "0000");
			bizResult.put(Constant.RETURN_PARAM_RETMSG, respMsg.get(Constant.RETURN_PARAM_RETMSG));
			bizResult.put(Constant.RT_REFUND_ORDERSTATE, Constant.REFUND_STATUS_SUCCESS);
		}
		RefundDetail refundDetail = refundDetailDao.selectByRefundNo(refundNo);
		if (refundDetail == null) {
			RefundDetail reDetail = new RefundDetail();
			reDetail.setRefund_no(jsonParams.getString("refundNo"));
			reDetail.setNotify_url(jsonParams.getString("refundUrl"));
			reDetail.setMer_no(jsonParams.getString("merNo"));
			reDetail.setRefund_reason(jsonParams.getString("refundReason"));
			reDetail.setTotal_order_amount(orderDetail.getOrder_amount());
			reDetail.setRefund_amount(jsonParams.getString("refundAmount"));
			reDetail.setOrder_no(orderDetail.getOrder_no());
			reDetail.setSerial_no(orderDetail.getSerial_no());
			reDetail.setRefund_state(bizResult.get(Constant.RT_REFUND_ORDERSTATE));
			reDetail.setReq_key(orderDetail.getReq_key());
			reDetail.setTrx_type(orderDetail.getTrx_type());
			reDetail.setChannel_id(orderDetail.getChannel_id());
			reDetail.setSub_mer_id(orderDetail.getSub_mch_id());
			refundDetailDao.insert(reDetail);
		} else {
			if(Constant.REFUND_STATUS_SUCCESS.equals(refundDetail.getRefund_state())) {
				refundDetail.setRefund_state(bizResult.get(Constant.RT_REFUND_ORDERSTATE));
				refundDetailDao.updateByPrimaryKey(refundDetail);
				_log.info("VVVV 已经存在的退款订单,初次成功,更新订单状态SUCCESS,退款单号=["+refundDetail.getRefund_no()+"]");
			}
			_log.info("VVVV 已经存在的退款订单,二次请求更新状态,退款单号=["+refundDetail.getRefund_no()+"]");	
		}

		jsonParams = null;
		orderDetail = null;
		return bizResult;

	}

	@DataSource("King")
	@Transactional(rollbackFor = Exception.class)
	public RefundDetail selectByRefundNo(String refundNo) throws Exception {

		return refundDetailDao.selectByRefundNo(refundNo);
	}

	@DataSource("King")
	@Transactional(rollbackFor = Exception.class)
	public boolean update(RefundDetail refund) throws Exception {

		int ret = refundDetailDao.updateByPrimaryKey(refund);
		if (ret < 1)
			return false;
		return true;
	}

	public static String getOpenId(String codeId) {
		String openid = "";
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
		StringBuffer bf = new StringBuffer();
		bf.append("appid=");
		bf.append("appid");
		bf.append("&secret=");
		bf.append("secret");
		bf.append("&code=");
		bf.append(codeId);
		bf.append("&grant_type=authorization_code");

		/*
		 * Map<String, String> params = new HashMap<String, String>();
		 * params.put("appid", wx_appid); params.put("secret", wx_secret);
		 * params.put("code", codeId); params.put("grant_type", "authorization_code");
		 */

		// String respstr = http.get(url+"?"+params);
		String respstr = Http.post(url, bf.toString());
		_log.info("TTTT 调用微信公众号获取openid接口获得的信息：" + respstr);
		net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(respstr);
		if (json.has("openid")) {
			openid = json.get("openid").toString();
		}
		bf = null;
		url = null;
		json = null;
		return openid;
	}

	public static void main(String[] args) {
		getOpenId("081smBgs1KBhfm0piths1MkYgs1smBgU");
	}

}
