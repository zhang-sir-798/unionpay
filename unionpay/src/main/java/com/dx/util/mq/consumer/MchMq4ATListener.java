package com.dx.util.mq.consumer;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dx.dao.MerchantNotifyDetailDao;
import com.dx.model.MerchantNotifyDetail;
import com.dx.util.dbconfig.DataSource;
import com.dx.util.domain.Constant;
import com.dx.util.domain.Retutil;
import com.dx.util.mq.producer.MchMq4ATProduct;
import com.dx.util.tools.Http;
import com.dx.util.tools.Https;
import com.pay.common.util.SpringContextUtil;

/**
 * @description 队列消息监听器
 * @author zhangsir
 * @Date 2018年12月11日 下午1:29:00
 * @version 1.0.0
 * 
 */

@Component
public class MchMq4ATListener extends MchMq4ATProduct implements MessageListener {

	private static final Log _log = LogFactory.getLog(MchMq4ATListener.class);

	@Override
	@DataSource("King")
	public void onMessage(Message message) {
		String logPrefix = "【聚合码系统商户通知业务】";
		
		String respMsg = "";
		try {
			respMsg = ((TextMessage) message).getText();
			_log.info(logPrefix + "接收消息:msg=" + respMsg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		JSONObject msgObj = JSON.parseObject(respMsg);
		String respUrl = msgObj.getString("url");
		int count = msgObj.getInteger("count");
		int cnt = count + 1;
		// 控制次数
		if (cnt > 6) {
			_log.info("通知地址respUrl=[" + respUrl + "] , 的延时通知次数notifyCount > 6 , 停止通知 ,平台单号=["
					+ msgObj.getString("serialNo") + "]");
			return;
		}
		String params = proMessage(msgObj);
		
		String httpResult = doNotifyUri(respUrl,params,cnt);
		
		MerchantNotifyDetail mchNotifyDetail = new MerchantNotifyDetail();
		if ("success".equalsIgnoreCase(httpResult)) {
			MerchantNotifyDetailDao mchNotifyDao = SpringContextUtil.getBean("merchantNotifyDetailDao");
			mchNotifyDetail.setSerial_no(msgObj.getString("serialNo"));
			mchNotifyDetail.setCount(cnt);
			mchNotifyDetail.setMsg(httpResult);
			mchNotifyDao.updateBySerialNo(mchNotifyDetail);
			return; 
		} else {
			MerchantNotifyDetailDao mchNotifyDao = SpringContextUtil.getBean("merchantNotifyDetailDao");
			mchNotifyDetail.setSerial_no(msgObj.getString("serialNo"));
			mchNotifyDetail.setCount(cnt);
			mchNotifyDetail.setMsg("false");
			mchNotifyDao.updateBySerialNo(mchNotifyDetail);
			msgObj.put("count", cnt);
			// 延时时间 3的n次方 即 :0,3,9,27,81,243 s
			this.send(Constant.QUEUE_CONSUME_4_AT, msgObj.toJSONString(), (int) Math.pow(3, cnt) * 1000);

			_log.info("通知地址respUrl=[" + respUrl + "]发送延时通知成功,通知第:[" + cnt + "]次,[延时" + (int) Math.pow(3, cnt) * 1000
					+ "]秒后执行通知" + ",平台单号=[" + msgObj.getString("serialNo") + "]");
		}
	}
	
	public String doNotifyUri(String respUrl , String bizResult,int cnt) {
		String httpResult ="exception";
		if("https".equalsIgnoreCase(respUrl.substring(0, 5))) {
			try {
				_log.info("VVVV 【聚合码系统商户通知业务】" + "params=" + bizResult + ",通知Url={" + respUrl + "}");
				httpResult = Https.post(respUrl, "params=" + bizResult);
				_log.info("VVVV 【聚合码系统商户通知业务】 通知次数  notifyCount={" + cnt + "} , 响应httpResult={" + httpResult + "}");
			} catch (Exception e) {
				_log.info(e);
			}
		}else {
			try {
				_log.info("VVVV 【聚合码系统商户通知业务】" + "params=" + bizResult + ",通知Url={" + respUrl + "}");
				httpResult = Http.post(respUrl, "params=" + bizResult);
				_log.info("VVVV 【聚合码系统商户通知业务】 通知次数  notifyCount={" + cnt + "} , 响应httpResult={" + httpResult + "}");
			} catch (Exception e) {
				_log.info(e);
			}
		}
		
		return httpResult;
		
	}

	
	public String proMessage(JSONObject msgObj) {

		JSONObject respJson = new JSONObject();
		respJson.put("respCode", msgObj.getString("respCode"));
		respJson.put("respMsg", msgObj.getString("respMsg"));
		respJson.put("merNo", msgObj.getString("merNo"));
		respJson.put("orderNo", msgObj.getString("orderNo"));
		respJson.put("serialNo", msgObj.getString("serialNo"));
		respJson.put("amount", msgObj.getString("amount"));
		respJson.put("orderState", msgObj.getString("orderState"));

		Map<String, String> bizResult = Retutil.createSuccessBizWithSign(respJson, msgObj.getString("reqKey"));

		return bizResult.get(Constant.QR_BIZ);
	}

}
