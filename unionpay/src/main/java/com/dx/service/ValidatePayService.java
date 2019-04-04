package com.dx.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.dx.dao.AgentDao;
import com.dx.dao.ChannelDao;
import com.dx.dao.FixedQrRepositoryDao;
import com.dx.dao.MerchantDao;
import com.dx.dao.MerchantFeeDao;
import com.dx.dao.OrderDetailDao;
import com.dx.dao.RefundDetailDao;
import com.dx.model.Agent;
import com.dx.model.Channel;
import com.dx.model.FixedQrRepository;
import com.dx.model.FormModel;
import com.dx.model.Merchant;
import com.dx.model.OrderDetail;
import com.dx.model.RefundDetail;
import com.dx.util.alibaba.AliPayUtil;
import com.dx.util.dbconfig.DataSource;
import com.dx.util.domain.Constant;
import com.dx.util.domain.RetEnum;
import com.dx.util.domain.Retutil;
import com.dx.util.tools.TimeUtil;
import com.dx.util.tools.signUtil;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class ValidatePayService extends demo {

	private static final Log _log = LogFactory.getLog(ValidatePayService.class);
	@Autowired
	private MerchantDao merchantDao;
	@SuppressWarnings("unused")
	@Autowired
	private MerchantFeeDao merchantFeeDao;
	@Autowired
	private OrderDetailDao orderDetailDao;
	@Autowired
	private RefundDetailDao refundDetailDao;
	@Autowired
	private FixedQrRepositoryDao fixedQrRepositoryDao;
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private AgentDao agentDao;

	@DataSource("King")
	// @DataSource("Queen")//生产走从库
	public Map<String, String> fixdBindValidateParams(String pojo) throws Exception {
		JSONObject params = JSONObject.parseObject(pojo);
		Map<String, String> bizResult = new HashMap<String, String>();
		String errorMessage = "";

		// 支付参数
		String requestId = params.getString("requestId");// 请求流水
		String qrCode = params.getString("qrCode");// 二维码链接
		String orgId = params.getString("orgId"); // 机构号
		String sign = params.getString("sign"); // 签名
		String version = params.getString("version"); // 版本号
		// 商户信息域
		JSONObject merInfo = params.getJSONObject("merInfo"); // 版本号

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
			errorMessage = "request params[sign] error.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (merInfo == null) {
			errorMessage = "request params[merInfo] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (qrCode == null) {
			errorMessage = "request params[qrCode] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		String type = merInfo.getString("type");
		String merName = merInfo.getString("merName");
		String shortName = merInfo.getString("shortName");
		String settleBranchNo = merInfo.getString("settleBranchNo");
		String settleName = merInfo.getString("settleName");
		String settleNo = merInfo.getString("settleNo");
		String settleMobile = merInfo.getString("settleMobile");

		String legalName = merInfo.getString("legalName");
		String legalContact = merInfo.getString("legalContact");
		String legalId = merInfo.getString("legalId");
		String idStartTime = merInfo.getString("idStartTime");
		String idEndTime = merInfo.getString("idEndTime");
		String address = merInfo.getString("address");
		String comments = merInfo.getString("comments");
		String payType = merInfo.getString("payType");// 支付类型N：正常类
		String mcc = merInfo.getString("mcc");
		String settleBank = merInfo.getString("settleBank");// 结算银行
		String settleBranchBank = merInfo.getString("settleBranchBank");// 结算银行支行
		String settleBankProv = merInfo.getString("settleBankProv");// 结算银行省
		String settleBankCity = merInfo.getString("settleBankCity");// 结算银行市
		String merType = merInfo.getString("merType");
		String settleType = merInfo.getString("settleType");

		if (StringUtils.isEmpty(type)) {
			errorMessage = "request params[type] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(merName)) {
			errorMessage = "request params[merName] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(shortName)) {
			errorMessage = "request params[shortName] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(settleBranchNo)) {
			errorMessage = "request params[settleBranchNo] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(settleName)) {
			errorMessage = "request params[settleName] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(settleNo)) {
			errorMessage = "request params[settleNo] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(settleMobile)) {
			errorMessage = "request params[settleMobile] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(legalName)) {
			errorMessage = "request params[legalName] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(legalContact)) {
			errorMessage = "request params[legalContact] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(legalId)) {
			errorMessage = "request params[legalId] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(idStartTime)) {
			errorMessage = "request params[idStartTime] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(idEndTime)) {
			errorMessage = "request params[idEndTime] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(address)) {
			errorMessage = "request params[address] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(comments)) {
			comments = "none";
		}
		if (StringUtils.isEmpty(payType)) {
			errorMessage = "request params[payType] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(mcc)) {
			errorMessage = "request params[mcc] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(settleBank)) {
			errorMessage = "request params[settleBank] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(settleBranchBank)) {
			errorMessage = "request params[settleBranchBank] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(settleBankProv)) {
			errorMessage = "request params[settleBankProv] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(settleBankCity)) {
			errorMessage = "request params[settleBankCity] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(merType)) {
			errorMessage = "request params[merType] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isEmpty(settleType)) {
			errorMessage = "request params[settleType] is null.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if ("ENTERPRISE".equals(merType)) {

			String licenseNo = merInfo.getString("licenseNo");// 营业执照号
			String licenseName = merInfo.getString("licenseName");// 营业执照名
			String licenseStartTime = merInfo.getString("licenseStartTime");// 营业执照生效日期
			String licenseEndTime = merInfo.getString("licenseEndTime");//// 营业执照失效日期
			String bizDomain = merInfo.getString("bizDomain");// 经营范围
			String mainBusiness = merInfo.getString("mainBusiness");// 主营业务
			String taxId = merInfo.getString("taxId");// 税务登记编号
			String orgCode = merInfo.getString("orgCode");// 组织机构代码
			String mcc32 = merInfo.getString("mcc32");// 机构32域

			if (StringUtils.isEmpty(licenseNo)) {
				errorMessage = "request params[licenseNo] is null.";
				_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
				return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
			}
			if (StringUtils.isEmpty(licenseName)) {
				errorMessage = "request params[licenseName] is null.";
				_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
				return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
			}
			if (StringUtils.isEmpty(licenseStartTime)) {
				errorMessage = "request params[licenseStartTime] is null.";
				_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
				return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
			}
			if (StringUtils.isEmpty(licenseEndTime)) {
				errorMessage = "request params[licenseEndTime] is null.";
				_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
				return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
			}
			if (StringUtils.isEmpty(bizDomain)) {
				errorMessage = "request params[bizDomain] is null.";
				_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
				return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
			}
			if (StringUtils.isEmpty(mainBusiness)) {
				errorMessage = "request params[mainBusiness] is null.";
				_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
				return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
			}
			if (StringUtils.isEmpty(taxId)) {
				errorMessage = "request params[taxId] is null.";
				_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
				return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
			}
			if (StringUtils.isEmpty(orgCode)) {
				errorMessage = "request params[orgCode] is null.";
				_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
				return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
			}
			if (StringUtils.isEmpty(mcc32)) {
				errorMessage = "request params[mcc32] is null.";
				_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
				return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
			}
		}

		// TODO 切换成代理商共享秘钥的逻辑

		FixedQrRepository fixedQrRepository = fixedQrRepositoryDao.selectByQrCode(qrCode);

		if (fixedQrRepository == null) {
			errorMessage = "Can't found qrCode[qrCode=" + qrCode + "] record in db.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if ("bind".equals(fixedQrRepository.getBind_state())) {
			errorMessage = "qrCode is used [qrCode=" + qrCode + "].";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		String agentKey = fixedQrRepository.getAgent_key(); // 代理商模式，共享秘钥
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

		params.put("agentKey", agentKey);
		params.put("fixedQrRepository", fixedQrRepository);

		bizResult = Retutil.createSuccessBiz(params);

		params = null;
		errorMessage = null;

		_log.info("VVVV requestId[" + requestId + "], ATcodeValidateParams return value ==" + bizResult);
		return bizResult;
	}

	@DataSource("King")
	// @DataSource("Queen")//生产走从库
	public Map<String, String> fixdValidateParams(String pojo) throws Exception {
		JSONObject params = JSONObject.parseObject(pojo);
		Map<String, String> bizResult = new HashMap<String, String>();
		String errorMessage = "";

		// 支付参数
		String requestId = params.getString("requestId");
		String genCount = params.getString("genCount");// 数量
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
			errorMessage = "request params[sign] error.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		// TODO 切换成代理商的逻辑
		Agent agent = agentDao.selectByAgengNo(orgId);
		if (agent == null) {
			errorMessage = "Can't found merchantInfo[orgId=" + orgId + "] record in db.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (!"ACTIVE".equals(agent.getState())) {
			errorMessage = "merchantInfo state not available [orgId=" + orgId + "] record in db.";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		String agentKey = agent.getReq_key(); // 是否生成共享秘钥，交易一个商户一个秘钥
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
		/** 银联二维码不通次处 不开启 **/
		Map<String, String> bizMap = new HashMap<String, String>();
		Map<String, String> respMap = null;
		bizMap.put("qrCodeNum", genCount);

		try {
			respMap = demo.doBatchQrcode(bizMap);
		} catch (Exception e) {
			errorMessage = "请稍后再试。";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_REPEAT_YL, errorMessage);
		}
		if (!"10000".equals(respMap.get("respCode"))) {
			errorMessage = "请稍后再试。";
			_log.info("VVVV requestId=[" + requestId + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_REPEAT_YL, errorMessage);
		}
		params.put("ylBatchNo", respMap.get("batchNo"));
		/** 银联二维码不通次处 不开启 **/

		params.put("agentKey", agentKey);
		params.put("agent", agent);

		bizResult = Retutil.createSuccessBiz(params);

		params = null;
		errorMessage = null;

		_log.info("VVVV requestId[" + requestId + "], ATcodeValidateParams return value ==" + bizResult);
		return bizResult;
	}

	@DataSource("King")
	// @DataSource("Queen")//生产走从库
	public Map<String, String> ATcodeValidateParams(String pojo) throws Exception {
		JSONObject params = JSONObject.parseObject(pojo);
		Map<String, String> bizResult = new HashMap<String, String>();
		String errorMessage = "";

		// 支付参数
		String amount = params.getString("amount"); // 支付金额（单位分）
		String body = params.getString("body"); // 商品描述信息
		String merNo = params.getString("merNo"); // 商户编号
		String notifyUrl = params.getString("notifyUrl"); // 支付结果回调URL
		String orderNo = params.getString("orderNo"); // 商户订单号
		String orgId = params.getString("orgId"); // 机构号
		String sign = params.getString("sign"); // 签名
		String trxType = params.getString("trxType"); // 支付类型
		String version = params.getString("version"); // 版本号
		String clientIp = params.getString("clientIp"); // 客户端下单ip
		String timeStart = params.getString("timeStart");
		String timeExpire = params.getString("timeExpire");

		// 验证请求参数有效性（必选项）
		if (StringUtils.isBlank(orderNo)) {
			errorMessage = "request params[orderNo] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		_log.info("VVVV orderNo=[" + orderNo + "] , validateService beging .......");
		if (StringUtils.isBlank(merNo)) {
			errorMessage = "request params[merNo] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isBlank(trxType)) {
			errorMessage = "request params[trxType] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isBlank(orgId)) {
			errorMessage = "request params[orgId] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (!StringUtils.isNumeric(amount)) {
			errorMessage = "request params[amount] is null.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		BigDecimal noneAmount = new BigDecimal(0.00);
		BigDecimal transAmount = new BigDecimal(String.valueOf(amount));// 交易金额

		// a
		if (transAmount.compareTo(noneAmount) <= 0) {
			errorMessage = "交易金额低于单笔最小限额";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (StringUtils.isBlank(notifyUrl)) {
			errorMessage = "request params[notifyUrl] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isBlank(body)) {
			errorMessage = "request params[body] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isBlank(version)) {
			errorMessage = "request params[version] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isBlank(clientIp)) {
			errorMessage = "request params[clientIp] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isBlank(timeStart)) {
			errorMessage = "request params[timeStart] is not.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);

		}
		if (14 != timeStart.length()) {
			errorMessage = "request params[timeStart-format] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isBlank(timeExpire)) {
			errorMessage = "request params[timeExpire] is not.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);

		}
		if (14 != timeExpire.length()) {
			errorMessage = "request params[timeExpire-format] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (!AliPayUtil.subDate(timeStart, timeExpire)) {
			errorMessage = "request params must in [ 1m < (timeExpire - timeStart) < 120m].";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (StringUtils.isEmpty(sign)) {
			errorMessage = "request params[sign] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		// 查询商户信息
		Merchant merchantInfo = merchantDao.selectByMerNo(merNo);
		if (merchantInfo == null) {
			errorMessage = "Can't found merchantInfo[merNo=" + merNo + "] record in db.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (!"ACTIVE".equals(merchantInfo.getState())) {
			errorMessage = "merchantInfo state not available [merNo=" + merNo + "] record in db.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		String requestKey = merchantInfo.getReq_key(); // 是否生成共享秘钥，交易一个商户一个秘钥
		if (StringUtils.isBlank(requestKey)) {
			errorMessage = "商户创建异常[merNo=" + merNo + "] record in db.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + ",未找到请求秘钥reqkey]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (!signUtil.verify(params, requestKey)) {
			errorMessage = "签名验证失败";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_SIGN_FAIL, errorMessage);
		}

		/*
		 * MerchantFee merchantFee = merchantFeeDao.selectByMerNo(merNo); if
		 * (merchantFee == null) { errorMessage = "[merchantFee is not found merNo=" +
		 * merNo + "]"; _log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" +
		 * errorMessage + "]"); return Retutil.createFailBiz(RetEnum.RET_V_PROD_INVALID,
		 * errorMessage); }
		 */

		// 商户费率验证 商户，限额的验证 单笔最低，最高，单日 最低，最高。
		/*
		 * BigDecimal eachUpLimit = new BigDecimal(merchantFee.getEach_up_limit());
		 * BigDecimal eachDownLimit = new BigDecimal(merchantFee.getEach_down_limit());
		 * BigDecimal dayLimit = new BigDecimal(merchantFee.getDay_limit()); BigDecimal
		 * bdAmount = new BigDecimal(amount); errorMessage = checkLimit(eachUpLimit,
		 * eachDownLimit, dayLimit, bdAmount, errorMessage, merNo, trxType); if
		 * (!"".equals(errorMessage)) { _log.info("VVVV orderNo=[" + orderNo +
		 * "],errorMessage=[" + errorMessage + "]"); return
		 * Retutil.createFailBiz(RetEnum.RET_V_AMONT_INVALID, errorMessage); }
		 */

		params.put("reqKey", merchantInfo.getReq_key());
		params.put("notifyUrl", notifyUrl);
		params.put("merchant", merchantInfo);

		// params.put("agent", agentInfo);
		// params.put("merchantFee", merchantFee);

		bizResult = Retutil.createSuccessBiz(params);

		params = null;
		merchantInfo = null;
		errorMessage = null;

		_log.info("VVVV orderNo[" + orderNo + "], ATcodeValidateParams return value ==" + bizResult);
		return bizResult;
	}

	@DataSource("King")
	// @DataSource("Queen")//生产走从库
	public Map<String, String> ATValidate4Query(String pojo) throws Exception {
		JSONObject params = JSONObject.parseObject(pojo);
		Map<String, String> bizResult = new HashMap<String, String>();
		String errorMessage = "";

		// 查询参数
		String merNo = params.getString("merNo"); // 商户编号
		String orderNo = params.getString("orderNo"); // 商户订单号
		String timeStart = params.getString("timeStart");
		String sign = params.getString("sign"); // 签名
		String version = params.getString("version"); // 版本号

		// 验证请求参数有效性（必选项）
		if (StringUtils.isBlank(orderNo)) {
			errorMessage = "request params[orderNo] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		_log.info("VVVV orderNo=[" + orderNo + "] , validateService beging .......");
		if (StringUtils.isBlank(merNo)) {
			errorMessage = "request params[merNo] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (StringUtils.isBlank(version)) {
			errorMessage = "request params[version] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (StringUtils.isBlank(timeStart)) {
			errorMessage = "request params[timeStart] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);

		}
		if (14 != timeStart.length()) {
			errorMessage = "request params[timeStart-format] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		OrderDetail order = orderDetailDao.selectByOrderAndTime(orderNo, timeStart);
		if (order == null) {
			// TODO 去历史库查询，已切库
			order = orderDetailDao.selectHistoryByOrderAndTime(orderNo, timeStart);
		}
		if (order == null) {
			errorMessage = "[wait a moment].";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_REPEAT_ORDER, errorMessage);
		}
		// 查询通道信息
		Channel channel = channelDao.selectByChannelId(Long.parseLong(order.getChannel_id()));
		if (channel == null) {
			errorMessage = "[wait a moment].";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[通道信息为空，数据异常。]");
			return Retutil.createFailBiz(RetEnum.RET_V_REPEAT_ORDER, errorMessage);
		}

		if (!timeStart.equals(order.getStart_time())) {
			errorMessage = "request params[timeStart] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (StringUtils.isEmpty(sign)) {
			errorMessage = "request params[sign] is not.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (!signUtil.verify(params, order.getReq_key())) {
			errorMessage = "request params[sign] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_SIGN_FAIL, errorMessage);
		}

		params.put("orderDetail", order);
		params.put("channel", channel);

		bizResult = Retutil.createSuccessBizQ(params, outsideCode(order.getState()));

		params = null;
		errorMessage = null;
		_log.info("VVVV orderNo[" + orderNo + "], ATValidate4Query return value ==" + bizResult);
		return bizResult;
	}

	public Map<String, String> ATcodeValidateSign(HttpServletRequest request) throws Exception {
		String pojo = request.getParameter("qr");
		if (StringUtils.isEmpty(pojo)) {
			String errMsg = ",请求信息无效。";
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errMsg);
		}

		if (pojo.length() == 30) {

			if (StringUtils.isEmpty(request.getParameter("transAmt"))) {
				String errMsg = ",未输入金额.";
				return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errMsg);
			}
			JSONObject params = new JSONObject();
			params.put("commonCode", "Fixed");
			params.put("serial_no", pojo);
			params.put("trx_type", request.getParameter("trxType"));
			// 元转分
			params.put("transAmt", new BigDecimal(request.getParameter("transAmt")).multiply(new BigDecimal(100))
					.setScale(0, RoundingMode.UP).toString());

			return Retutil.createSuccessBiz(params);
		} else {

			String serialNo = pojo.substring(0, 30);
			String sign = pojo.substring(pojo.length() - 8, pojo.length());

			if (!sign.equals(signUtil.MAC(serialNo, "65917A01F943CBB02D58791725569DA2"))) {
				String errMsg = ",交易存在风险.";
				return Retutil.createFailBiz(RetEnum.RET_C_NO_SIGN, errMsg);
			}

			JSONObject params = new JSONObject();
			params.put("serial_no", serialNo);
			params.put("commonCode", "Dynamic");
			return Retutil.createSuccessBiz(params);
		}
	}

	@DataSource("King")
	@Transactional(rollbackFor = Exception.class)
	public ModelAndView FixedCodeValidateSign(HttpServletRequest request) {
		String forwardString = "payforHtml";
		String pojo = request.getParameter("qr");

		// 转接之后的处理？是读流还是gat参数

		System.out.print("##user-agent##" + request.getHeader("user-agent"));
		Map<String, Object> model = new HashMap<String, Object>();
		FormModel form = new FormModel();
		if (StringUtils.isEmpty(pojo)) {
			forwardString = "payErrorMsg";
			form.setMsg("参数错误");
			model.put("form", form);
			return new ModelAndView(forwardString, model);
		}
		_log.info("##开始接收固定码下单请求 ,请求参数pojo=" + pojo);
		String signParam = pojo.substring(0, 32);
		String sign = pojo.substring(pojo.length() - 8, pojo.length());
		String qrCodeNum = pojo.substring(4, 32);

		/*
		 * if (!sign.equals(signUtil.MAC(signParam,
		 * "65917A01F943CBB02D58791725569DA2"))) { forwardString = "payErrorMsg";
		 * form.setMsg("验签失败,交易存在风险"); model.put("form", form); return new
		 * ModelAndView(forwardString, model); }
		 */
		// TODO 不出意外应该会使用银联的商编，银联返回商户编号或者二维码编号
		FixedQrRepository fixedQrRepository = fixedQrRepositoryDao.selectByPrimaryKey(Long.parseLong("根据转接参数确认"));

		if (null == fixedQrRepository) {
			forwardString = "payErrorMsg";
			form.setMsg("二维码未绑定商户，暂不可用。");
			model.put("form", form);
			return new ModelAndView(forwardString, model);
		}

		Date sendDate = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sendTime = sf.format(sendDate);

		String body = fixedQrRepository.getMer_name(); // 商品描述信息
		String merNo = fixedQrRepository.getMer_no(); // 商户编号
		String notifyUrl = fixedQrRepository.getNotify_url(); // 支付结果回调URL
		String orgId = fixedQrRepository.getAgent_no(); // 代理商

		String timeStart = TimeUtil.getStartTime(sendDate);
		String timeExpire = TimeUtil.getExpireTime(sendDate);

		// 平台单号 serialNo 日期(14) +随机数 (16)
		String serialNo = new SimpleDateFormat("yyyyMMddHHmmss").format(sendDate) + getSerialNo(16);

		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setSerial_no(serialNo);
		orderDetail.setBody(body);
		orderDetail.setMer_no(merNo);
		orderDetail.setNotify_url(notifyUrl);

		if (request.getHeader("user-agent").indexOf("MicroMessenger/") != -1) {
			orderDetail.setTrx_type("010101");
		} else if (request.getHeader("user-agent").indexOf("AlipayClient/") != -1) {
			orderDetail.setTrx_type("010201");
		} else {
			orderDetail.setTrx_type("err");
		}

		orderDetail.setStart_time(timeStart);
		orderDetail.setExpire_time(timeExpire);
		orderDetail.setReq_key("agent_key");
		orderDetail.setState(Constant.PAY_STATUS_INIT);

		try {
			int insRes = orderDetailDao.insert(orderDetail);
		} catch (DuplicateKeyException e) {
			_log.error("VVVV 商户单号重复：serialNo=[" + serialNo + "]");
			String errorMessage = "request params[serialNo] error.";
			_log.info("VVVV serialNo=[" + serialNo + "],errorMessage=[" + errorMessage + "]");
		}

		JSONObject respJson = new JSONObject();

		respJson.put("serialNo", serialNo);

		form.setMcht_name(body);
		form.setMcht_code(serialNo);
		form.setTrxType(orderDetail.getTrx_type());
		model.put("form", form);
		return new ModelAndView(forwardString, model);

	}

	@DataSource("King")
	// @DataSource("Queen")//生产走从库
	public Map<String, String> refundValidateParams(String pojo) throws Exception {
		JSONObject params = JSONObject.parseObject(pojo);
		Map<String, String> bizResult = new HashMap<String, String>();
		String errorMessage = "";

		// 支付参数
		String orderNo = params.getString("orderNo"); // 商户订单号
		String refundNo = params.getString("refundNo"); // 退款请求单号
		String amount = params.getString("refundAmount"); // 退款金额（单位分）
		String refundReason = params.getString("refundReason"); // 退款原因
		String merNo = params.getString("merNo"); // 商户编号
		String refundUrl = params.getString("refundUrl"); // 退款结果回调URL

		String version = params.getString("version"); // 版本号
		String sign = params.getString("sign"); // 签名

		// 验证请求参数有效性（必选项）
		if (StringUtils.isBlank(orderNo)) {
			errorMessage = "request params[orderNo] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isBlank(refundNo)) {
			errorMessage = "request params[refundNo] error.";
			_log.info("VVVV refundNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		_log.info("VVVV orderNo=[" + orderNo + "] , validateService beging .......");
		if (StringUtils.isBlank(merNo)) {
			errorMessage = "request params[merNo] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (!StringUtils.isNumeric(amount)) {
			errorMessage = "request params[amount] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (StringUtils.isBlank(refundUrl)) {
			errorMessage = "request params[refundUrl] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (StringUtils.isBlank(version)) {
			errorMessage = "request params[version] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (StringUtils.isBlank(refundReason)) {
			errorMessage = "request params[clientIp] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (StringUtils.isEmpty(sign)) {
			errorMessage = "request params[sign] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		OrderDetail order = orderDetailDao.selectByOrderNo(orderNo);
		if (order == null) {
			// TODO 去历史库查询，已切库
			order = orderDetailDao.selectByOrderNo(orderNo);
		}
		if (order == null) {
			errorMessage = "[未查询到此订单].";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		if (!Constant.PAY_STATUS_SUCCESS.equals(order.getState())) {
			errorMessage = "[orderState 非 success 无法退款].";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (!signUtil.verify(params, order.getReq_key())) {
			errorMessage = "签名验证失败";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_SIGN_FAIL, errorMessage);
		}
		// 查询通道信息
		Channel channel = channelDao.selectByChannelId(Long.parseLong(order.getChannel_id()));
		if (channel == null) {
			errorMessage = "[wait a moment].";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[通道信息为空，数据异常。]");
			return Retutil.createFailBiz(RetEnum.RET_V_REPEAT_ORDER, errorMessage);
		}

		params.put("refundUrl", refundUrl);
		params.put("orderDetail", order);
		params.put("channel", channel);

		bizResult = Retutil.createSuccessBiz(params);

		params = null;
		errorMessage = null;
		_log.info("VVVV orderNo[" + orderNo + "], ATcodeValidateParams return value ==" + bizResult);
		return bizResult;
	}

	@DataSource("King")
	// @DataSource("Queen")//生产走从库
	public Map<String, String> refundQueryValidateParams(String pojo) throws Exception {
		JSONObject params = JSONObject.parseObject(pojo);
		Map<String, String> bizResult = new HashMap<String, String>();
		String errorMessage = "";

		String merNo = params.getString("merNo"); // 商户编号
		String orderNo = params.getString("orderNo"); // 商户订单号
		String refundNo = params.getString("refundNo"); // 退款请求单号
		String version = params.getString("version"); // 版本号
		String sign = params.getString("sign"); // 签名

		// 验证请求参数有效性（必选项）
		if (StringUtils.isBlank(orderNo)) {
			errorMessage = "request params[orderNo] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}
		_log.info("VVVV orderNo=[" + orderNo + "] , validateService beging .......");
		if (StringUtils.isBlank(refundNo)) {
			errorMessage = "request params[refundNo] error.";
			_log.info("VVVV refundNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (StringUtils.isBlank(merNo)) {
			errorMessage = "request params[merNo] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (StringUtils.isBlank(version)) {
			errorMessage = "request params[version] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (StringUtils.isEmpty(sign)) {
			errorMessage = "request params[sign] error.";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		RefundDetail refundDetail = refundDetailDao.selectByParams(orderNo, refundNo);
		if (refundDetail == null) {
			errorMessage = "[未查询到此订单].";
			_log.info("VVVV refundNo=[" + refundNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_INVALID, errorMessage);
		}

		if (!signUtil.verify(params, refundDetail.getReq_key())) {
			errorMessage = "签名验证失败";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[" + errorMessage + "]");
			return Retutil.createFailBiz(RetEnum.RET_V_SIGN_FAIL, errorMessage);
		}
		
		Channel channel = channelDao.selectByChannelId(Long.parseLong(refundDetail.getChannel_id()));
		if (channel == null) {
			errorMessage = "[wait a moment].";
			_log.info("VVVV orderNo=[" + orderNo + "],errorMessage=[通道信息为空，数据异常。]");
			return Retutil.createFailBiz(RetEnum.RET_V_REPEAT_ORDER, errorMessage);
		}
		
		params.put("refundDetail", refundDetail);
		params.put("channel", channel);

		bizResult = Retutil.createSuccessBizQ(params, reOutsideCode(refundDetail.getRefund_state()));
		params = null;
		errorMessage = null;
		_log.info("VVVV orderNo[" + orderNo + "], ATcodeValidateParams return value ==" + bizResult);
		return bizResult;
	}

	public String outsideCode(String state) {
		String result = Constant.T_INSIDE;

		switch (state) {
		case Constant.PAY_STATUS_INIT:
			result = Constant.T_INSIDE;
			break;
		case Constant.PAY_STATUS_SUCCESS:
			result = Constant.FALSE_INSIDE_RETCODE;
			break;
		case Constant.PAY_STATUS_PAYING:
			result = Constant.T_INSIDE;
			break;
		case Constant.PAY_STATUS_FAILED:
			result = Constant.FALSE_INSIDE_RETCODE;
			break;
		case Constant.PAY_STATUS_EXPIRED:
			result = Constant.FALSE_INSIDE_RETCODE;
			break;
		default:
			return result;
		}

		return result;
	}

	public String reOutsideCode(String state) {
		String result = Constant.T_INSIDE;

		switch (state) {
		case Constant.REFUND_STATUS_ING:
			result = Constant.T_INSIDE;
			break;
		case Constant.REFUND_STATUS_REQ_F:
			result = Constant.FALSE_INSIDE_RETCODE;
			break;
		case Constant.REFUND_STATUS_SUCCESS:
			result = Constant.FALSE_INSIDE_RETCODE;
			break;
		default:
			return result;
		}

		return result;
	}

	public static boolean timeState(String startTime) throws Exception {
		boolean flag;
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");

		Date bt = sf.parse(startTime);

		Date et = addHour(new Date(), 2);

		if (bt.before(et)) {
			flag = false;
		} else {
			// 表示bt大于et
			flag = true;
		}
		return flag;
	}

	public static Date addHour(Date date, int time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, time);
		return cal.getTime();
	}

	public static void main(String[] args) throws Exception {
		String pojo = "000000000000000000000000000001xxxxxxxxxx";
		String serialNo = pojo.substring(1, 30);

		System.out.println(serialNo);
		/*
		 * Map<String, String> bizMap = new HashMap<String, String>();
		 * bizMap.put("qrCodeNum", "2");
		 * 
		 * demo.doBatchQrcode(bizMap);
		 */
	}

	@SuppressWarnings("unused")
	private String checkLimit(BigDecimal eachUpLimit, BigDecimal eachDownLimit, BigDecimal dayLimit, BigDecimal amount1,
			String errorMessage, String merNo, String trxType) {
		if (amount1.compareTo(eachUpLimit) > 0) {
			return errorMessage = "交易金额高于单笔最大限额 ,  merNo=[" + merNo + "] , trxType=[" + trxType + "]";
		}
		if (amount1.compareTo(eachDownLimit) < 0) {
			return errorMessage = "交易金额低于单笔最小限额 ,  merNo=[" + merNo + "] , trxType=[" + trxType + "]";
		}
		if (amount1.compareTo(dayLimit) > 0) {
			return errorMessage = "交易金额高于单日最大限额 ,  merNo=[" + merNo + "] , trxType=[" + trxType + "]";
		}
		return "";
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