package com.dx.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.dx.dao.FixedBatchNoDao;
import com.dx.dao.FixedQrRepositoryDao;

import com.dx.model.Agent;
import com.dx.model.FixedBatchNo;
import com.dx.model.FixedQrRepository;

import com.dx.util.dbconfig.DataSource;
import com.dx.util.domain.Constant;
import com.dx.util.domain.RetEnum;
import com.dx.util.domain.Retutil;
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
public class FixedDetailService {

	private static final Log _log = LogFactory.getLog(FixedDetailService.class);

	@Autowired
	private FixedQrRepositoryDao fixedQrRepositoryDao;
	
	@Autowired
	private FixedBatchNoDao fixedBatchNoDao;

	@DataSource("King")
	@Transactional(rollbackFor = Exception.class)
	public Map<String, String> batchQrcodeYL(String params) throws Exception {
		JSONObject jsonParam = JSONObject.parseObject(params);
		
		Agent agent = jsonParam.getObject("agent", Agent.class);
		Map<String, String> bizResult = new HashMap<String, String>();

		// 生成数量
		String orgId = jsonParam.getString("orgId"); // 机构号
		Integer qrCodeNum = Integer.valueOf(jsonParam.getString("qrCodeNum"));

		FixedBatchNo pojo = new FixedBatchNo();

		pojo.setCreate_time(new Date());
		pojo.setGen_count(String.valueOf(qrCodeNum));
		pojo.setAgent_no(orgId);
		pojo.setAgent_key(jsonParam.getString("agentKey"));
		pojo.setYl_batch_no(jsonParam.getString("ylBatchNo"));
		pojo.setRemark(agent.getAgent_notify_url());
		fixedBatchNoDao.insert(pojo);

		// 返回信息
		JSONObject respJson = new JSONObject();
		respJson.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_SUCCESS.getCode());
		respJson.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_SUCCESS.getMessage());

		respJson.put("batchNo", jsonParam.getString("ylBatchNo"));
		respJson.put("orgRequestId", jsonParam.getString("requestId"));
		respJson.put("orgId", orgId);

		bizResult = Retutil.createSuccessBizWithSign(respJson, jsonParam.getString("agentKey"));

		_log.info("VVVV YLBatchNo[" + jsonParam.getString("ylBatchNo") + "], ATCodeUnionPay return value ==" + bizResult);

		respJson = null;

		return bizResult;

	}

	// 生成自己的码
	@DataSource("King")
	@Transactional(rollbackFor = Exception.class)
	public Map<String, String> batchQrcode(String params) throws Exception {
		JSONObject jsonParam = JSONObject.parseObject(params);
		Map<String, String> bizResult = new HashMap<String, String>();

		// 生成数量
		String orgId = jsonParam.getString("orgId"); // 机构号
		Integer qrCodeNum = Integer.valueOf(jsonParam.getString("qrCodeNum"));
		List<FixedQrRepository> fixedCodeDetailList = new ArrayList<FixedQrRepository>();
		String iBatchNo = "B" + getQrNum(9);
		for (int i = 0; i < qrCodeNum; i++) {

			FixedQrRepository qrRepository = new FixedQrRepository();
			qrRepository.setBatch_no(iBatchNo);
			qrRepository.setCreate_time(new Date());
			String qrNum = getQrNum(28);
			qrRepository.setQr_num(qrNum);
			qrRepository.setQr_code(sign_qrcode(qrNum));
			qrRepository.setQr_status("00");
			qrRepository.setGen_count(String.valueOf(qrCodeNum));
			qrRepository.setAgent_no(orgId);
			if (jsonParam.get("ylBatchNo") != null) {
				qrRepository.setYl_batch_no(jsonParam.getString("ylBatchNo"));
			}

			fixedCodeDetailList.add(qrRepository);
		}

		fixedQrRepositoryDao.insertListQrRepository(fixedCodeDetailList);

		// 返回信息
		JSONObject respJson = new JSONObject();
		respJson.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_SUCCESS.getCode());
		respJson.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_SUCCESS.getMessage());

		respJson.put("batchNo", iBatchNo);
		respJson.put("orgRequestId", jsonParam.getString("requestId"));
		respJson.put("orgId", orgId);

		bizResult = Retutil.createSuccessBizWithSign(respJson, jsonParam.getString("agentKey"));

		_log.info("VVVV iBatchNo[" + iBatchNo + "], ATCodeUnionPay return value ==" + bizResult);

		respJson = null;

		return bizResult;

	}
	
	
	@DataSource("King")
	// @DataSource("Quenen")生产走从库
	public Map<String, String> batchQrcodeQueryYL(String paramsD) throws Exception {

		JSONObject params = JSONObject.parseObject(paramsD);
		Map<String, String> bizResult = new HashMap<String, String>();
		String errorMessage = "";

		// 支付参数
		String requestId = params.getString("requestId");
		String batchNo = params.getString("batchNo");// 批次号
		String orgId = params.getString("orgId"); // 机构号
		String sign = params.getString("sign"); // 签名
		String version = params.getString("version"); // 版本号

		// 验证请求参数有效性（必选项）
		if (StringUtils.isBlank(requestId)) {
			errorMessage = "request params[requestId] error.";
			_log.info("VVVV orderNo=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		_log.info("VVVV requestId=[" + requestId + "] , validateService beging .......");

		if (StringUtils.isBlank(orgId)) {
			errorMessage = "request params[orgId] error.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (StringUtils.isBlank(version)) {
			errorMessage = "request params[version] error.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (StringUtils.isEmpty(sign)) {
			errorMessage = "request params[sign] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(batchNo)) {
			errorMessage = "request params[batchNo] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		
		
		FixedBatchNo FixedBatchNo = fixedBatchNoDao.selectByBacthNo(batchNo);
		if (FixedBatchNo == null) {
			errorMessage = "request params[batchNo] is error.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		String agentKey = FixedBatchNo.getAgent_key(); // 是否生成共享秘钥，交易一个商户一个秘钥
		if (StringUtils.isBlank(agentKey)) {
			errorMessage = "商户创建异常[orgId=" + orgId + "] record in db.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + ",未找到请求秘钥reqkey]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (!signUtil.verify(params, agentKey)) {
			errorMessage = "签名验证失败";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_SIGN_FAIL, errorMessage);
		}

		// TODO 去查询银联二维码，存到yl 白码库中
		/**** 银联消费收款码产品--固码 ***/
		Map<String, String> bizMap = new HashMap<String, String>();
		Map<String, String> respMap = null;
		bizMap.put("batchNo", batchNo);
		try {
			respMap = demo.doBbatchQrcodeQuery(bizMap);
		} catch (Exception e) {
			errorMessage = "请稍后再试。";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_REPEAT_YL, errorMessage);
		}
		String qrStr = respMap.get("qrCodeInfo");
		qrStr = qrStr.substring(1, qrStr.length() - 1);
		qrStr = qrStr.replace("},{", "},,{");
		String[] qrStrs = qrStr.split(",,");
		List<FixedQrRepository> qrRepositoryList = new ArrayList<FixedQrRepository>();
		for (String qrJson : qrStrs) {
			net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(qrJson);
			FixedQrRepository QrRepository = new FixedQrRepository();
			QrRepository.setBatch_no(batchNo);
			QrRepository.setYl_batch_no(batchNo);
			QrRepository.setBind_state("unbind");
			QrRepository.setAgent_key(FixedBatchNo.getAgent_key());
			QrRepository.setAgent_no(FixedBatchNo.getAgent_no());
			QrRepository.setCreate_time(new Date());
			QrRepository.setQr_code(json.getString("qrCode"));
			QrRepository.setQr_num(json.getString("qrNum"));
			QrRepository.setQr_status(json.getString("qrStatus"));
			QrRepository.setNotify_url(FixedBatchNo.getRemark());
			// ylQrRepository.setExpandCode("上海银行的机构号");
			QrRepository.setQr_status("unuse");
			qrRepositoryList.add(QrRepository);
		}
		
		fixedQrRepositoryDao.insertListQrRepository(qrRepositoryList);
		/****/
		
		// 返回信息
		JSONObject respJson = new JSONObject();
		respJson.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_SUCCESS.getCode());
		respJson.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_SUCCESS.getMessage());

		respJson.put("batchNo", batchNo);
		respJson.put("orgRequestId", requestId);
		respJson.put("orgId", orgId);
		respJson.put("qrCodeInfo", qrStr);

		bizResult = Retutil.createSuccessBizWithSign(respJson, agentKey);

		_log.info("VVVV requestId[" + requestId + "], ATCodeUnionPay return value ==" + bizResult);

		respJson = null;

		return bizResult;

	}
	
	

	@DataSource("King")
	// @DataSource("Quenen")生产走从库
	public Map<String, String> batchQrcodeQuery(String paramsD) throws Exception {

		JSONObject params = JSONObject.parseObject(paramsD);
		Map<String, String> bizResult = new HashMap<String, String>();
		String errorMessage = "";

		// 支付参数
		String requestId = params.getString("requestId");
		String batchNo = params.getString("batchNo");// 批次号
		String orgId = params.getString("orgId"); // 机构号
		String sign = params.getString("sign"); // 签名
		String version = params.getString("version"); // 版本号

		// 验证请求参数有效性（必选项）
		if (StringUtils.isBlank(requestId)) {
			errorMessage = "request params[requestId] error.";
			_log.info("VVVV orderNo=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		_log.info("VVVV requestId=[" + requestId + "] , validateService beging .......");

		if (StringUtils.isBlank(orgId)) {
			errorMessage = "request params[orgId] error.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (StringUtils.isBlank(version)) {
			errorMessage = "request params[version] error.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (StringUtils.isEmpty(sign)) {
			errorMessage = "request params[sign] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(batchNo)) {
			errorMessage = "request params[batchNo] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		List<FixedQrRepository> fixedQrList = fixedQrRepositoryDao.selectAll();
		if (fixedQrList.isEmpty()) {
			errorMessage = "request params[batchNo] is error.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		String agentKey = fixedQrList.get(0).getAgent_key(); // 是否生成共享秘钥，交易一个商户一个秘钥
		if (StringUtils.isBlank(agentKey)) {
			errorMessage = "商户创建异常[orgId=" + orgId + "] record in db.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + ",未找到请求秘钥reqkey]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (!signUtil.verify(params, agentKey)) {
			errorMessage = "签名验证失败";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_SIGN_FAIL, errorMessage);
		}

		// TODO 去查询银联二维码，存到yl 白码库中
		/**** 银联消费收款码产品--固码 ***/
		/*Map<String, String> bizMap = new HashMap<String, String>();
		Map<String, String> respMap = null;
		bizMap.put("batchNo", fixedQrList.get(0).getYl_batch_no());
		try {
			respMap = demo.doBbatchQrcodeQuery(bizMap);
		} catch (Exception e) {
			errorMessage = "请稍后再试。";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_REPEAT_YL, errorMessage);
		}
		String qrStr = respMap.get("qrCodeInfo");
		qrStr = qrStr.substring(1, qrStr.length() - 1);
		qrStr = qrStr.replace("},{", "},,{");
		String[] qrStrs = qrStr.split(",,");
		List<YlQrRepository> ylqrRepositoryList = new ArrayList<YlQrRepository>();
		for (String qrJson : qrStrs) {
			net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(qrJson);
			YlQrRepository ylQrRepository = new YlQrRepository();
			ylQrRepository.setBatchNo(batchNo);
			ylQrRepository.setYlBatchNo(bizMap.get("batchNo"));
			ylQrRepository.setAgentNo(fixedQrList.get(0).getAgent_no());
			ylQrRepository.setCreateTime(new Date());
			ylQrRepository.setQrCode(json.getString("qrCode"));
			ylQrRepository.setQrNum(json.getString("qrNum"));
			ylQrRepository.setQrStatus(json.getString("qrStatus"));
			// ylQrRepository.setExpandCode("上海银行的机构号");
			ylQrRepository.setQrStatus("unuse");
			ylqrRepositoryList.add(ylQrRepository);
		}

		ylqrRepositoryDao.insertYlQrCodelist(ylqrRepositoryList);*/
		/****/

		// 查询出自己的码
		ArrayList<Object> qrList = new ArrayList<>();
		Map<String, String> qrMap = new HashMap<String, String>();
		for (FixedQrRepository pojo : fixedQrList) {
			qrMap.put("qrCode", pojo.getQr_code());
			qrMap.put("qrStatus", "00");
			qrMap.put("qrNum", pojo.getQr_num());
			qrList.add(qrMap);
			qrMap.clear();
		}

		// 返回信息
		JSONObject respJson = new JSONObject();
		respJson.put(Constant.RETURN_PARAM_RETCODE, RetEnum.RET_SUCCESS.getCode());
		respJson.put(Constant.RETURN_PARAM_RETMSG, RetEnum.RET_SUCCESS.getMessage());

		respJson.put("batchNo", batchNo);
		respJson.put("orgRequestId", requestId);
		respJson.put("orgId", orgId);
		respJson.put("qrCodeInfo", qrList);

		bizResult = Retutil.createSuccessBizWithSign(respJson, agentKey);

		_log.info("VVVV requestId[" + requestId + "], ATCodeUnionPay return value ==" + bizResult);

		respJson = null;

		return bizResult;

	}

	public static String sign_qrcode(String serialNo) throws UnsupportedEncodingException {
		// String url = "https://127.0.0.1:8443/unionpay/gateway/unifiedorder?qr=";
		String url = "http://127.0.0.1/unionpay/gateway/qrfixed?qr=";
		String signKey = "111";
		String sign = signUtil.MAC(serialNo, signKey);
		// 地址+4+28+8
		String qrCode = url + "62FX" + serialNo + sign;
		String ret = java.net.URLEncoder.encode(qrCode, "utf-8");

		return ret;
	}

	public static String getQrNum(int len) {
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
