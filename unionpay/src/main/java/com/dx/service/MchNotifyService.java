package com.dx.service;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.dx.model.OrderDetail;
import com.dx.util.domain.Constant;
import com.dx.util.domain.RetEnum;
import com.dx.util.domain.Retutil;
import com.dx.util.mq.MchNotifyComponent;
import com.dx.util.tools.Http;
import com.dx.util.tools.Https;

@Service
public class MchNotifyService extends MchNotifyComponent {

	private static final Log _log = LogFactory.getLog(MchNotifyService.class);

	public void sendMchNotify(JSONObject params) {

		OrderDetail orderDetail = params.getObject("orderDetail", OrderDetail.class);
		String respUrl = orderDetail.getNotify_url();

		if (StringUtils.isEmpty(respUrl)) {
			_log.warn("商户通知URL为空,respUrl=[" + respUrl + "]");
			return;
		}

		JSONObject respJson = new JSONObject();

		respJson.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_SUCCESS.getCode());
		respJson.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_SUCCESS.getMessage());
		respJson.put("amount", orderDetail.getOrder_amount());
		respJson.put("merNo", orderDetail.getMer_no());
		respJson.put("orderNo", orderDetail.getOrder_no());
		respJson.put("serialNo", orderDetail.getSerial_no());	
		respJson.put("orderState", orderDetail.getState());

		Map<String, String> bizResult = Retutil.createSuccessBizWithSign(respJson, orderDetail.getReq_key());
		String httpResult = doNotifyUri(respUrl ,bizResult);
		
		_log.info("下游响应httpResult=[" + httpResult + "]");
		if (!"success".equalsIgnoreCase(httpResult)) {
			orderDetail.setResp_code("0000");
			orderDetail.setResp_msg("成功");
			doNotify(orderDetail, true);
		}

	}
	
	public String doNotifyUri(String respUrl , Map<String, String> bizResult) {
		String httpResult ="exception";
		if("https".equalsIgnoreCase(respUrl.substring(0, 5))) {
			try {
				httpResult = Https.post(respUrl, "params=" + bizResult.get(Constant.QR_BIZ));
				_log.info("VVVV通知商户报文:" + bizResult.get(Constant.QR_BIZ) + ",通知Url=[" + respUrl + "]");
			} catch (Exception e) {
				_log.info(e);
			}
		}else {
			try {
				httpResult = Http.post(respUrl, "params=" + bizResult.get(Constant.QR_BIZ));
				_log.info("VVVV通知商户报文:" + bizResult.get(Constant.QR_BIZ) + ",通知Url=[" + respUrl + "]");
			} catch (Exception e) {
				_log.info(e);
			}
		}
		
		return httpResult;
		
	}
	

	public String orderState(String state) {
		String result = Constant.PAY_STATUS_UNKONW;
		switch (state) {
		case Constant.PAY_STATUS_INIT:
			result = Constant.PAY_STATUS_INIT;
			break;
		case Constant.PAY_STATUS_SUCCESS:
			result = Constant.PAY_STATUS_INIT;
			break;
		case Constant.PAY_STATUS_PAYING:
			result = Constant.PAY_STATUS_PAYING;
			break;
		case Constant.PAY_STATUS_FAILED:
			result = Constant.PAY_STATUS_FAILED;
			break;
		case Constant.PAY_STATUS_EXPIRED:
			result = Constant.PAY_STATUS_EXPIRED;
			break;

		default:

			return result;
		}

		return result;
	}

}