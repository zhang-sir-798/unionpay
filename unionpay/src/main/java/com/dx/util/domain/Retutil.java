package com.dx.util.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.dx.model.JsapiModel;
import com.dx.model.OrderDetail;
import com.dx.model.RefundDetail;
import com.dx.util.tools.signUtil;

/**
 * @author: zhangsir
 * 
 * @date: 18/12/13
 * @description:
 */
public class Retutil {
	private static final Log _log = LogFactory.getLog(Retutil.class);

	/**
	 * 构建成功返回结果(内部)
	 * 
	 * @param baseParam
	 * @param obj
	 * @return
	 */
	public static Map<String, String> createSuccessBiz(JSONObject biz) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(Constant.RT_INSIDE, Constant.T_INSIDE);
		resultMap.put(Constant.QR_BIZ, biz.toString());
		if (biz.get("trx_type") != null) {
			resultMap.put("trx_type", biz.getString("trx_type"));
		}
		if (biz.get("commonCode") != null) {
			resultMap.put("commonCode", biz.getString("commonCode"));
		}
		return resultMap;
	}

	/**
	 * 构建成功返回结果(查询)
	 * 
	 * @param baseParam
	 * @param obj
	 * @return
	 */
	public static Map<String, String> createSuccessBizQ(JSONObject biz, String outsideCode) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(Constant.RT_INSIDE, Constant.T_INSIDE);
		resultMap.put(Constant.RT_OUTSIDE, outsideCode);
		resultMap.put(Constant.QR_BIZ, biz.toString());

		return resultMap;
	}

	/**
	 * 构建成功返回结果(内部)
	 * 
	 * @param baseParam
	 * @param obj
	 * @return
	 */
	public static Map<String, String> createInnoBiz(Map<String, String> biz) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(Constant.RT_INSIDE, Constant.T_INSIDE);

		List<String> keys = new ArrayList<String>(biz.keySet());

		for (String key : keys) {
			if (key == null || key.equals(""))
				continue;
			resultMap.put(key, biz.get(key));
		}

		return resultMap;
	}

	/**
	 * 构建退款成功返回结果(内部)
	 * 
	 * @param baseParam
	 * @param obj
	 * @return
	 */
	public static Map<String, String> createInnoBiz(Map<String, String> biz, JSONObject jObject,
			OrderDetail orderDetail) {
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(Constant.RT_INSIDE, Constant.T_INSIDE);

		JSONObject params = new JSONObject();
		params.put(Constant.RETURN_PARAM_RETCODE, biz.get("respCode"));
		params.put(Constant.RETURN_PARAM_RETMSG, biz.get("respMsg"));
		params.put("refundState", biz.get(Constant.RT_REFUND_ORDERSTATE));

		params.put("refundReason", jObject.getString("refundReason"));
		params.put("refundAmount", jObject.getString("refundAmount"));
		params.put("refundNo", jObject.getString("refundNo"));
		params.put("orderNo", jObject.getString("orderNo"));
		params.put("orderAmount", orderDetail.getOrder_amount());
		params.put(Constant.RESULT_PARAM_SIGN, signUtil.encrypt(params.toJSONString(), orderDetail.getReq_key()));

		resultMap.put(Constant.QR_BIZ, params.toString());

		return resultMap;
	}

	/**
	 * 构建成功返回结果(外部)
	 * 
	 * @param baseParam
	 * @param obj
	 * @return
	 */
	public static Map<String, String> createSuccessBizWithSign(JSONObject respJson, String key) {
		Map<String, String> resultMap = new HashMap<String, String>();

		resultMap.put(Constant.RT_INSIDE, Constant.T_INSIDE);
		respJson.put(Constant.RESULT_PARAM_SIGN, signUtil.encrypt(respJson.toJSONString(), key));
		resultMap.put(Constant.QR_BIZ, respJson.toString());

		return resultMap;
	}

	/**
	 * 构建失败返回结果(nosign)
	 * 
	 * @param rpcBaseParam
	 * @param retEnum
	 * @return
	 */
	public static Map<String, String> createFailBiz(RetEnum retEnum, String errMsg) {
		if (retEnum == null) {
			retEnum = RetEnum.RET_C_NO_FAILED;
		}

		Map<String, String> resultMap = new HashMap<String, String>();

		resultMap.put(Constant.RT_INSIDE, Constant.FALSE_INSIDE_RETCODE);

		JSONObject biz = new JSONObject();
		biz.put(Constant.RETURN_PARAM_RETCODE, retEnum.getCode());

		if (!StringUtils.isEmpty(errMsg)) {
			biz.put(Constant.RETURN_PARAM_RETMSG, retEnum.getMessage() + errMsg);
		} else {

			biz.put(Constant.RETURN_PARAM_RETMSG, retEnum.getMessage());
		}
		resultMap.put(Constant.QR_BIZ, biz.toJSONString());
		return resultMap;
	}

	/**
	 * 构建失败返回结果(sign)
	 * 
	 * @param rpcBaseParam
	 * @param retEnum
	 * @return
	 */
	public static Map<String, String> createFailBizWithSign(RetEnum retEnum, String key) {

		if (retEnum == null) {
			retEnum = RetEnum.RET_C_NO_FAILED;
		}
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put(Constant.RT_INSIDE, Constant.FALSE_INSIDE_RETCODE);
		JSONObject biz = new JSONObject();
		biz.put(Constant.RETURN_PARAM_RETCODE, retEnum.getCode());
		biz.put(Constant.RETURN_PARAM_RETMSG, retEnum.getMessage());

		biz.put(Constant.RESULT_PARAM_SIGN, signUtil.encrypt(biz.toString(), key));
		resultMap.put(Constant.QR_BIZ, biz.toJSONString());
		return resultMap;
	}

	/**
	 * 构建成功返回结果(内部)
	 * 
	 * @param baseParam
	 * @param obj
	 * @return
	 */
	public static String business(Map<String, String> map) {
		String result = "";
		if (!map.isEmpty() && map != null) {
			result = map.get(Constant.QR_BIZ);
		} else {
			result = createFailBiz(null, null).get(Constant.QR_BIZ);
		}
		_log.info("VVVV 响应下游信息 response message:" + result);
		return result;
	}

	/**
	 * 构建成功返回结果(内部)
	 * 
	 * @param baseParam
	 * @param obj
	 * @return
	 */
	public static String businessQ(String params) {
		JSONObject jsonParam = JSONObject.parseObject(params);
		OrderDetail orderDetail = jsonParam.getObject("orderDetail", OrderDetail.class);

		JSONObject biz = new JSONObject();
		biz.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_SUCCESS.getCode());
		biz.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_SUCCESS.getMessage());
		biz.put("amount", orderDetail.getOrder_amount());
		biz.put("merNo", orderDetail.getMer_no());
		biz.put("orderNo", orderDetail.getOrder_no());
		biz.put("serialNo", orderDetail.getSerial_no());
		biz.put("orderState", orderDetail.getState());
		biz.put(Constant.RESULT_PARAM_SIGN, signUtil.encrypt(biz.toString(), orderDetail.getReq_key()));

		_log.info("VVVV 商户订单号=[" + orderDetail.getOrder_no() + "] ,订单状态为终态 , respMessage=" + biz.toString());
		return biz.toString();
	}

	/**
	 * 构建成功返回结果(内部)
	 * 
	 * @param baseParam
	 * @param obj
	 * @return
	 */
	public static String businessREQ(String params) {
		JSONObject jsonParam = JSONObject.parseObject(params);
		RefundDetail refundDetail = jsonParam.getObject("refundDetail", RefundDetail.class);

		JSONObject biz = new JSONObject();
		biz.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_SUCCESS.getCode());
		biz.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_SUCCESS.getMessage());
		biz.put("refundAmount", refundDetail.getRefund_amount());
		biz.put("orderAmount", refundDetail.getTotal_order_amount());
		biz.put("merNo", refundDetail.getMer_no());
		biz.put("orderNo", refundDetail.getOrder_no());
		biz.put("refundNo", refundDetail.getRefund_no());
		biz.put("refundState", refundDetail.getRefund_state());

		biz.put(Constant.RESULT_PARAM_SIGN, signUtil.encrypt(biz.toString(), refundDetail.getReq_key()));

		_log.info("VVVV 商户退款订单号=[" + refundDetail.getRefund_no() + "] ,订单状态为终态 , respMessage=" + biz.toString());
		return biz.toString();
	}

	/**
	 * 构建成功返回结果(内部)
	 * 
	 * @param baseParam
	 * @param obj
	 * @return
	 */
	public static ModelAndView errpage(Map<String, String> map) {
		String jsModel = "jsApi";
		JsapiModel Jsapi = new JsapiModel();
		String result = map.get(Constant.QR_BIZ);
		Map<String, Object> model = new HashMap<String, Object>();
		JSONObject jsonParam = JSONObject.parseObject(result);
		Jsapi.setIsSuccess("false");
		Jsapi.setErrMsg(jsonParam.getString("respMsg"));
		model.put("form", Jsapi);
		ModelAndView errModel = new ModelAndView(jsModel, model);

		jsModel = null;
		Jsapi = null;
		result = null;
		model = null;
		jsonParam = null;
		map = null;

		return errModel;
	}

}
