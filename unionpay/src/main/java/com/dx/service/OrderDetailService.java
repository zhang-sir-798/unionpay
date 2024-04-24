package com.dx.service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONObject;

import com.dx.dao.OrderDetailDao;
import com.dx.model.Channel;
import com.dx.model.Merchant;
import com.dx.model.MerchantFee;
import com.dx.model.OrderDetail;
import com.dx.model.RefundDetail;
import com.dx.util.alibaba.AliPayService;
import com.dx.util.dbconfig.DataSource;
import com.dx.util.domain.Constant;
import com.dx.util.domain.RetEnum;
import com.dx.util.domain.Retutil;
import com.dx.util.domain.TrxEnum;
import com.dx.util.tencent.WXPayService;
import com.dx.util.tools.TimeUtil;
import com.dx.util.tools.signUtil;

/**
 * 
 * @ClassName ScanCodeService
 * @Description TODO
 * @author zhangsir
 * @Date 2018年12月3日 下午3:29:00
 * @version 1.0.0
 * 
 */
@Service
@Transactional
@SuppressWarnings({"unused"})
public class OrderDetailService {

	private static final Log _log = LogFactory.getLog(OrderDetailService.class);
	
	@Autowired
	private OrderDetailDao orderDetailDao;
	@Autowired
	private WXPayService wxPayService;
	@Autowired
	private AliPayService aliPayService;
	
	@DataSource("King")
	public Map<String, String> orderDetailSelect(String params) throws Exception {
		JSONObject jsonParam = JSONObject.parseObject(params);
		Map<String, String> bizResult = new HashMap<String, String>();
		
		String serial_no = jsonParam.getString("serial_no");

		OrderDetail orderDetail= orderDetailDao.selectBySerialNo(serial_no);
		if(orderDetail == null) {
			String errorMessage = "请联系商家或重新下单。";
			_log.info("VVVV未查询到此订单  serial_no=[" + serial_no + "] , ret errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_S_ORDER_INVALID, errorMessage);
		}	
		jsonParam.put("orderDetail", orderDetail);	
		bizResult = Retutil.createSuccessBiz(jsonParam);	
		jsonParam=null;
		serial_no=null;
		orderDetail=null;
		return bizResult;

	}
	
	@DataSource("King")
	public boolean dealOrder(String serial_no ,RefundDetail refundDetail) throws Exception {
		
		OrderDetail orderDetail= orderDetailDao.selectBySerialNo(serial_no);
		if(orderDetail == null) {
			String errorMessage = "请联系商家或重新下单。";
			_log.info("VVVV未查询到此订单  serial_no=[" + serial_no + "] , ret errorMessage=[" + errorMessage + "]");
			return	false;
		}	
		
		if(orderDetail.getOrder_amount().equals(refundDetail.getRefund_amount())) {
			orderDetail.setState(Constant.PAY_STATUS_REFUND_SUCCESSCOMPLETE);
		} else {
			orderDetail.setState(Constant.PAY_STATUS_REFUNDPART);
		}
		orderDetailDao.updateByPrimaryKey(orderDetail);
		_log.info("VVVV此订单维护成功  serial_no=[" + serial_no + "] , 订单状态=["+orderDetail.getState()+ "]" );
		return true;
		
	}
	
	
	@DataSource("King")
	public OrderDetail orderDetailSelectNotify(String params) throws Exception {
		JSONObject jsonParam = JSONObject.parseObject(params);
		Map<String, String> bizResult = new HashMap<String, String>();
		
		String serial_no = jsonParam.getString("serial_no");

		OrderDetail orderDetail= orderDetailDao.selectBySerialNo(serial_no);
		if(orderDetail == null) {
			
			_log.info("VVVV未查询到此订单  serial_no=[" + serial_no + "]");
			return null;
		}	
		
		return orderDetail;

	}
	
	public Map<String, String> orderDetailValidate(String params) throws Exception {
		JSONObject jsonParam = JSONObject.parseObject(params);
		Map<String, String> bizResult = new HashMap<String, String>();
		OrderDetail orderDetail = jsonParam.getObject("orderDetail", OrderDetail.class);	
		if(Constant.PAY_STATUS_INIT.equals(orderDetail.getState())) {
			bizResult = Retutil.createSuccessBiz(jsonParam);	
			jsonParam=null;
			orderDetail=null;
			return bizResult;
		}
		bizResult = Retutil.createFailBiz(RetEnum.RET_V_REPEAT_SCAN,null);
		jsonParam=null;
		orderDetail=null;
		return bizResult;
	}
	

	@DataSource("King")
	@Transactional(rollbackFor=Exception.class)
	public boolean orderDetailUpdate(OrderDetail order) throws Exception {
		
		int ret = orderDetailDao.updateByPrimaryKey(order);
		if (ret < 1) {
			_log.info("数据库更新失败，平台单号=[" + order.getSerial_no()+"]");
			return false;
		}
		_log.info("订单维护成功，平台单号=["+order.getSerial_no()+"]");
		return true;
		
	}
	
	
	@DataSource("King")
	@Transactional(rollbackFor=Exception.class)
	public Map<String, String> orderDetailBuiled(String params) throws Exception {
		JSONObject jsonParam = JSONObject.parseObject(params);
		Map<String, String> bizResult = new HashMap<String, String>();
		
		Date sendDate = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sendTime = sf.format(sendDate);
		
		String orderNo = jsonParam.getString("orderNo");// 商户订单号
		String amount = jsonParam.getString("amount"); // 支付金额（单位分）
		String body = jsonParam.getString("body"); // 商品描述信息
		String merNo = jsonParam.getString("merNo"); // 商户编号
		String notifyUrl = jsonParam.getString("notifyUrl"); // 支付结果回调URL
		String orgId = jsonParam.getString("orgId"); // 机构号
	
		String trxType = jsonParam.getString("trxType"); // 支付类型
		String version = jsonParam.getString("version"); // 版本号
		String merClientIp = jsonParam.getString("clientIp"); // 客户端下单ip
		String timeStart = jsonParam.getString("timeStart");
		String timeExpire = jsonParam.getString("timeExpire");
			
		Merchant merchantInfo = jsonParam.getObject("merchant", Merchant.class);
		MerchantFee merchantFee = jsonParam.getObject("merchantFee", MerchantFee.class);
		// 平台单号 serialNo 日期(14) +随机数 (16)
		String serialNo = new SimpleDateFormat("yyyyMMddHHmmss").format(sendDate) + getSerialNo(16);
	
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setSerial_no(serialNo);
		orderDetail.setOrder_no(orderNo);
		orderDetail.setOrder_amount(amount);
		orderDetail.setBody(body);
		orderDetail.setMer_no(merNo);
		orderDetail.setNotify_url(notifyUrl);
		orderDetail.setClient_ip(merClientIp);
		orderDetail.setStart_time(timeStart);
		orderDetail.setExpire_time(timeExpire);
		orderDetail.setReq_key(merchantInfo.getReq_key());
		orderDetail.setState(Constant.PAY_STATUS_INIT);
		int insRes = 0;
		try {
			insRes = orderDetailDao.insert(orderDetail);
		} catch (DuplicateKeyException e) {
			_log.error("VVVV 商户单号重复：orderNo=[" + orderNo +"]");
			String errorMessage = "request params[orderNo] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_NO_INVALID, errorMessage);
		} 
		
		JSONObject respJson  = new JSONObject();
		respJson.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_SUCCESS.getCode());
		respJson.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_SUCCESS.getMessage());
		respJson.put("qrCode", sign_qrcode(serialNo));
		respJson.put("trxType", jsonParam.getString("trxType"));
		respJson.put("merNo", merchantInfo.getMer_no());
		respJson.put("serialNo", serialNo);
		respJson.put("orderNo", orderNo);
		respJson.put("amount", amount);
		respJson.put("orgId", orgId);
		respJson.put("orderState", orderDetail.getState());
			
		bizResult = Retutil.createSuccessBizWithSign(respJson , merchantInfo.getReq_key());	
		
		respJson=null;
		jsonParam = null;
		merchantFee = null;
		merchantInfo = null;
		
		_log.info("VVVV orderNo[" + orderNo + "], ATCodeUnionPay return value ==" + bizResult);
		return bizResult;

	}
	
	/*@DataSource("King")
	@Transactional(rollbackFor=Exception.class)
	public Map<String, String> orderDetailFixedBuiled(String params) throws Exception {
		JSONObject jsonParam = JSONObject.parseObject(params);
		Map<String, String> bizResult = new HashMap<String, String>();
		
		
		String qrCodeNum = jsonParam.getString("qrCodeNum");// 二维码编号
		String amount = jsonParam.getString("transAmt");// 支付金额（单位分）
		
		FixedCodeDetail fixedDetail = fixedCodeDetailDao.selectByQrNum(qrCodeNum);
		if(fixedDetail ==null) {
			String errorMessage = "please wait a moment.";
			_log.info("VVVV 固码信息未查到 ,errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_REPEAT_YL, errorMessage);
		}
		
		Date sendDate = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sendTime = sf.format(sendDate);
			
		String body = fixedDetail.getMer_name(); // 商品描述信息
		String merNo = fixedDetail.getMer_no(); // 商户编号
		String notifyUrl = fixedDetail.getNotify_url(); // 支付结果回调URL
		String orgId = fixedDetail.getOrg_no(); // 机构号
		
		String timeStart =  TimeUtil.getStartTime(sendDate);
		String timeExpire = TimeUtil.getExpireTime(sendDate);
		
		// 平台单号 serialNo 日期(14) +随机数 (16)
		String serialNo = new SimpleDateFormat("yyyyMMddHHmmss").format(sendDate) + getSerialNo(16);
	
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setSerial_no(serialNo);
		orderDetail.setOrder_amount(amount);
		orderDetail.setBody(body);
		orderDetail.setMer_no(merNo);
		orderDetail.setNotify_url(notifyUrl);

		orderDetail.setStart_time(timeStart);
		orderDetail.setExpire_time(timeExpire);
		orderDetail.setReq_key("agent_key");
		orderDetail.setState(Constant.PAY_STATUS_INIT);
		int insRes = 0;
		try {
			insRes = orderDetailDao.insert(orderDetail);
		} catch (DuplicateKeyException e) {
			_log.error("VVVV 商户单号重复：serialNo=[" + serialNo +"]");
			String errorMessage = "request params[serialNo] error.";
			_log.info("VVVV serialNo=[" + serialNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_NO_INVALID, errorMessage);
		} 
		
		JSONObject respJson  = new JSONObject();
				
		respJson.put("serialNo", serialNo);
		respJson.put("amount", amount);
				
		bizResult = Retutil.createSuccessBiz(respJson);	
		
		respJson=null;
		jsonParam = null;
		
		
		_log.info("VVVV serialNo=[" + serialNo + "], ATCodeUnionPay return value ==" + bizResult);
		return bizResult;

	}*/
	
	
	@DataSource("King")
	@Transactional(rollbackFor=Exception.class)
	public Map<String, String> orderDetailQuery(String params) throws Exception {
		JSONObject jsonParam = JSONObject.parseObject(params);
		Map<String, String> respMsg = new HashMap<String , String>();
		Map<String, String> bizResult = new HashMap<String, String>();
		OrderDetail orderDetail = jsonParam.getObject("orderDetail", OrderDetail.class);
		Channel channel = jsonParam.getObject("channel", Channel.class);
		String trxType = orderDetail.getTrx_type();
		switch (trxType) {
		case "010201":
			respMsg = aliPayService.doTradeQuery(orderDetail , channel.getPrivate_key() , channel.getPublic_key());
			break;
		case "010101":
			respMsg = wxPayService.doOrderQuery(orderDetail, channel.getPrivate_key() , channel.getPublic_key());
			break;
		default:	
			return Retutil.createFailBiz(null,null);
		}
		
		JSONObject respJson  = new JSONObject();
		respJson.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_SUCCESS.getCode());
		respJson.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_SUCCESS.getMessage());
		respJson.put("amount", orderDetail.getOrder_amount());
		respJson.put("merNo", orderDetail.getMer_no());
		respJson.put("orderNo", orderDetail.getOrder_no());
		respJson.put("serialNo", orderDetail.getSerial_no());
		respJson.put("orderState", respMsg.get(Constant.RT_ORDERSTATE));
		
		bizResult = Retutil.createSuccessBizWithSign(respJson , orderDetail.getReq_key());	
		
		return bizResult;
	
	}
	
	public static String sign_qrcode(String serialNo) throws UnsupportedEncodingException {
		//String url = "http://127.0.0.1/unionpay/gateway/unifiedorder?qr=";
		String url = "http://127.0.0.1/unionpay/gateway/unifiedorder?qr=";
		String signKey = "11";
		String sign = signUtil.MAC(serialNo , signKey);
		String qrCode = url + serialNo+sign;
		String ret = java.net.URLEncoder.encode(qrCode, "utf-8");
		
		return ret;
	}

	public static String getSerialNo(int len) {
		String[] baseString = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
				"I", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		String[] baseString2 = { "A", "B", "C", "D", "F", "G", "H", "I", "J", "K", "L", "M", "N", "P", "Q", "R", "S",
				"T", "U", "V", "W", "X", "Y", "Z" };
		Random random = new Random();
		int length = baseString.length;
		String randomString = "";
		for (int i = 0; i < length; i++) {
			randomString += baseString[random.nextInt(length)];
		}
		random = new Random(System.currentTimeMillis());
		String resultStr = baseString2[random.nextInt(baseString2.length - 1)];
		for (int i = 0; i < len - 1; i++) {
			resultStr += randomString.charAt(random.nextInt(randomString.length() - 1));
		}
		return resultStr;
	}

}
