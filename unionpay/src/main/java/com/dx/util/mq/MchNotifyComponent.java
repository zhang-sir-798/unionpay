package com.dx.util.mq;

import javax.annotation.Resource;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dx.dao.MerchantNotifyDetailDao;
import com.dx.model.MerchantNotifyDetail;
import com.dx.model.OrderDetail;
import com.dx.model.RefundDetail;
import com.dx.util.dbconfig.DataSource;
import com.dx.util.domain.Constant;
import com.dx.util.mq.producer.MchMq4ATProduct;


/**
 * @description  商户通知消息队列组件
 * @author zhangsir
 * @Date 2018年12月11日 下午1:29:00
 * @version 1.0.0
 * 
 */
@Component
public class MchNotifyComponent {

	private static final Log _log = LogFactory.getLog(MchNotifyComponent.class);
	
	@Resource
	MchMq4ATProduct mchMq4ATProduct;
	@Autowired
	private MerchantNotifyDetailDao mchNotifyDao;
	
	
	/**
	 * @description  聚合码商户通知
	 * @author zhangsir
	 * @Date 2018年12月11日 下午1:29:00
	 * @version 1.0.0
	 * 
	 */
	@DataSource("King")
	public void doNotify(OrderDetail orderDetail, boolean isFirst) {
		// 发起后台通知业务系统
		JSONObject object = createNotifyInfo(orderDetail, isFirst);
		try {
			mchMq4ATProduct.send(Constant.QUEUE_CONSUME_4_AT,object.toJSONString(),(int) Math.pow(3, 1) * 1000);
		} catch (Exception e) {
			_log.error("payOrderId=%s,sendMessage error."+ObjectUtils.defaultIfNull("", ""));
		}
	}
	/**
	 * @description  退款商户通知
	 * @author zhangsir
	 * @Date 2018年12月21日 下午2:29:15
	 * @version 1.0.0
	 * 
	 */
	@DataSource("King")
	public void doRefundNotify(RefundDetail refundDetail, boolean isFirst) {
		// 发起后台通知业务系统
		JSONObject object = createRefundInfo(refundDetail, isFirst);
		try {
			mchMq4ATProduct.send(Constant.QUEUE_REFUND_4_AT,object.toJSONString(),(int) Math.pow(3, 1) * 1000);
		} catch (Exception e) {
			_log.info("退款单号=["+refundDetail.getRefund_no()+",退款通知MQ sendMessage error.");
		}
	}
	
	
public JSONObject createNotifyInfo(OrderDetail orderDetail, boolean isFirst) {
		
		//记录商户通知记录表
		MerchantNotifyDetail MerchantNotifyDetail = new MerchantNotifyDetail();
		
		MerchantNotifyDetail.setSerial_no(orderDetail.getSerial_no());
		MerchantNotifyDetail.setCount(1);
		MerchantNotifyDetail.setMer_no(orderDetail.getMer_no());
		MerchantNotifyDetail.setNotify_url(orderDetail.getNotify_url());
		MerchantNotifyDetail.setOrder_no(orderDetail.getOrder_no());
		MerchantNotifyDetail.setTrx_type(orderDetail.getTrx_type());
		int instRes = mchNotifyDao.insert(MerchantNotifyDetail);
		_log.info("VVVV 维护商户通知明细表结果=["+instRes+"]");
		
		JSONObject object = new JSONObject();
		object.put("count", 1);
		object.put("reqKey", orderDetail.getReq_key());	
		object.put("url", orderDetail.getNotify_url());
		
		object.put("respCode", orderDetail.getResp_code());
		object.put("respMsg", orderDetail.getResp_msg());
		object.put("merNo", orderDetail.getMer_no());
		object.put("orderNo", orderDetail.getOrder_no());
		object.put("serialNo", orderDetail.getSerial_no());
		object.put("amount", orderDetail.getOrder_amount());
		object.put("orderState", orderDetail.getState());
		
		orderDetail = null;
		MerchantNotifyDetail = null;
		
		return object;
	}
	
	
	public JSONObject createRefundInfo(RefundDetail refundDetail, boolean isFirst) {
		
		
		JSONObject object = new JSONObject();
		object.put("count", 1);
		object.put("reqKey", refundDetail.getReq_key());	
		object.put("url", refundDetail.getNotify_url());
		
		object.put("respCode", "0000");
		object.put("respMsg", "success");
		object.put("merNo", refundDetail.getMer_no());
		object.put("orderNo", refundDetail.getOrder_no());
		object.put("refundNo", refundDetail.getRefund_no());
		object.put("refundAmount", refundDetail.getRefund_amount());
		object.put("orderAmount", refundDetail.getTotal_order_amount());
		object.put("refundState", refundDetail.getRefund_state());
		
		
		return object;
	}

}
