package com.dx.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dx.dao.ChannelMerchantDao;

import com.dx.dao.FixedQrRepositoryDao;
import com.dx.dao.MerchantDao;
import com.dx.dao.OrderDetailDao;
import com.dx.dao.RoutemappingDao;
import com.dx.model.Channel;
import com.dx.model.ChannelMerchant;

import com.dx.model.FixedQrRepository;
import com.dx.model.JsapiModel;
import com.dx.model.Merchant;
import com.dx.model.OrderDetail;
import com.dx.model.Routemapping;
import com.dx.util.alibaba.AliPayService;
import com.dx.util.alibaba.AliPayUtil;
import com.dx.util.dbconfig.DataSource;
import com.dx.util.domain.Constant;
import com.dx.util.domain.RetEnum;
import com.dx.util.domain.Retutil;
import com.dx.util.domain.TrxEnum;
import com.dx.util.tencent.WXPayService;
import com.dx.util.tools.Http;
import com.dx.util.tools.signUtil;
import com.pay.common.util.Md5Util;

import net.sf.json.JSONObject;

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
public class QrScanUnifiedOrderService {

	private static final Log _log = LogFactory.getLog(OrderDetailService.class);

	@Autowired
	private MerchantDao merchantDao;
	@Autowired
	private WXPayService wxPayService;
	@Autowired
	private AliPayService aliPayService;
	@Autowired
	private OrderDetailDao orderDetailDao;
	@Autowired
	private RouteMappingService routeService;
	@Autowired
	private ChannelMerchantDao channelMerchantDao;
	@Autowired
	private RoutemappingDao routemappingDao;
	@Autowired
	private FixedQrRepositoryDao fixedQrRepositoryDao;

	@DataSource("King")
	// @DataSource("Queen")//生产走从库
	@Transactional(rollbackFor = Exception.class)
	public Map<String, String> superMerCore(String jsonParam) throws Exception {
		String errorMessage = "";
		Map<String, String> bizResult = null;
		com.alibaba.fastjson.JSONObject data = com.alibaba.fastjson.JSONObject.parseObject(jsonParam);
		com.alibaba.fastjson.JSONObject merInfo = data.getJSONObject("merInfo");
		
		
		
		// 1.先插入数据 merchant
		Merchant m = new Merchant();

		if ("ENTERPRISE".equals(merInfo.getString("mertype"))) {// 企业商户
			// 企业商户判断
			m.setLicense_no(merInfo.getString("licenseNo"));
			m.setLicense_name(merInfo.getString("licenseName"));
			m.setLicense_start_time(merInfo.getString("licenseStartTime"));
			m.setLicense_end_time(merInfo.getString("licenseEndTime"));
			m.setBiz_domain(merInfo.getString("bizDomain"));
			m.setMain_business(merInfo.getString("mainBusiness"));
			m.setTax_id(merInfo.getString("taxId"));
			m.setOrg_code(merInfo.getString("orgCode"));
			m.setMcc32(merInfo.getString("mcc32"));
		}

		m.setType(merInfo.getString("type"));//
		m.setLegal_name(merInfo.getString("legalName"));//
		m.setLegal_contact(merInfo.getString("legalContact"));//
		m.setLegal_id(merInfo.getString("legalId"));//
		m.setSettle_bank(merInfo.getString("settlebank"));//
		m.setSettle_branch_bank(merInfo.getString("settlebranchbank"));//
		m.setSettle_bank_prov(merInfo.getString("settlebankprov"));//
		m.setSettle_bank_city(merInfo.getString("settlebankcity"));//
		m.setSettle_branch_no(merInfo.getString("settleBranchNo"));//
		m.setSettle_name(merInfo.getString("settleName"));//
		m.setSettle_no(merInfo.getString("settleNo"));//
		m.setSettle_mobile(merInfo.getString("settleMobile"));//
		m.setId_start_time(merInfo.getString("idStartTime"));//
		m.setId_end_time(merInfo.getString("idEndTime"));//
		m.setMer_name(merInfo.getString("merName"));//
		m.setShort_name(merInfo.getString("shortName"));//
		m.setAddress(merInfo.getString("address"));//
		m.setComments(merInfo.getString("comments"));//
		m.setMer_type(merInfo.getString("mertype"));//
		m.setMcc(merInfo.getString("mcc"));//
		m.setSettle_type(merInfo.getString("settleType"));//

		m.setPay_type(merInfo.getString("payType"));
		m.setMer_no(randomByMerNo());

		Date time = new Date();
		m.setState("NEW");
		m.setCreator("interface");
		m.setCreate_time(time);
		m.setUpdator("interface");
		m.setUpdate_time(time);
		m.setLine("_接口首次创建");
		try {
			m.setReq_key(this.reqKeyMd5(m));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			errorMessage = "please wait a moment.";
			_log.info("VVVV orderNo=[" + m.getMer_no() + "],errorMessage=[reqKey 生成有误]");
			return Retutil.createFailBiz(RetEnum.RET_V_REPEAT_YL, errorMessage);
		}
		try {
			// 注意唯一主键
			merchantDao.insert(m);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "please wait a moment.";
			_log.info("VVVV orderNo=[" + m.getMer_no() + "],errorMessage=[merchant 保存失败 insert db time]");
			return Retutil.createFailBiz(RetEnum.RET_V_REPEAT_YL, errorMessage);
		}
		// 主营业务，经营类目 会根据base_category 查询获得 自己制作转换表
		// 2.交互上游，成功后则路由
		/// 微信//微信//微信//微信//微信//微信//微信//微信//微信//微信//微信//微信//微信//微信//微信///////
		Map<String, String> wxPayResult = wxPayService.doSubmchManageAdd(m);
		if (wxPayResult.get("return_code").equals("SUCCESS")) {
			if (wxPayResult.get("result_code").equals("SUCCESS")) {
				// channel表暂时写死微信，后期更改为活的
				String CM_channel_id = "13";
				ChannelMerchant cm = new ChannelMerchant();
				Date now = new Date();
				// 判断上游商户类型//微信侧动码
				cm.setMerchant_name(m.getMer_name());
				cm.setMerchant_shortname(m.getShort_name());
				cm.setService_phone(m.getLegal_contact());
				cm.setContact(m.getLegal_name());
				cm.setContact_phone(m.getLegal_contact());
				cm.setContact_email("");
				cm.setBusiness(wxPayResult.get("WXC_business"));
				cm.setChannel_id(Long.valueOf(CM_channel_id));
				cm.setContact_weachatid_type("");
				cm.setContact_wechatid("");
				cm.setMerchant_remark(m.getMer_no());
				// 通道商编
				cm.setSub_mch_id(wxPayResult.get("sub_mch_id"));
				try {
					wxPayResult.remove("sign");
					cm.setData_json(JSONObject.fromObject(wxPayResult).toString());
				} catch (Exception e) {
					e.printStackTrace();
					cm.setData_json("解析异常");
				}

				cm.setType(TrxEnum.WX_D_J.getCode());
				cm.setStatus("ACTIVE");

				cm.setCreate_time(now);
				cm.setUpdate_time(now);
				cm.setCreator("interface");
				cm.setUpdate_name("interface");

				try {
					channelMerchantDao.insert(cm);
				} catch (Exception e) {
					e.printStackTrace();
					errorMessage = "please wait a moment.";
					_log.info(
							"VVVV orderNo=[" + m.getMer_no() + "],errorMessage=[channelMerchant 保存失败 insert db time]");
					return Retutil.createFailBiz(RetEnum.RET_V_REPEAT_YL, errorMessage);
				}

				Routemapping r = new Routemapping();

				// TODO 渠道号暂时写死
				_log.info("#######渠道号暂时写死 ##   24006513          ###############################33");
				r.setChnl_id(Long.valueOf("24006513"));

				r.setSub_mer_id(cm.getSub_mch_id());
				r.setMer_no(m.getMer_no());
				r.setTran_type(TrxEnum.WX_D_J.getCode());
				r.setStatus("ACTIVE");

				r.setCreator("interface");
				r.setUpdate_name("interface");
				r.setCreate_time(now);
				r.setUpdate_time(now);

				try {
					routemappingDao.insert(r);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		/// 支付宝//支付宝//支付宝//支付宝//支付宝//支付宝//支付宝//支付宝//支付宝//支付宝//支付宝//支付宝///////
		Map<String, String> aliPayResult = aliPayService.doIndirectCreate(m);
		if (aliPayResult.get("code").equals("10000") && aliPayResult.get("sub_merchant_id") != null) {
			// channel表暂时写死支付宝，后期更改为活的
			String CM_channel_id = "14";
			ChannelMerchant cm = new ChannelMerchant();
			Date now = new Date();
			// 阿里侧动码
			cm.setMerchant_name(m.getMer_name());
			cm.setMerchant_shortname(m.getShort_name());
			cm.setService_phone(m.getLegal_contact());
			cm.setContact(m.getLegal_name());
			cm.setContact_phone(m.getLegal_contact());
			cm.setContact_email("");
			cm.setBusiness(wxPayResult.get("ALIC_business"));
			cm.setChannel_id(Long.valueOf(CM_channel_id));
			cm.setContact_weachatid_type("");
			cm.setContact_wechatid("");
			cm.setMerchant_remark(m.getMer_no());
			// 通道商编
			cm.setSub_mch_id(aliPayResult.get("sub_mch_id"));
			try {
				wxPayResult.remove("sign");
				cm.setData_json(JSONObject.fromObject(wxPayResult).toString());
			} catch (Exception e) {
				e.printStackTrace();
				cm.setData_json("解析异常");
			}

			cm.setType(TrxEnum.ZFB_D_J.getCode());
			cm.setStatus("ACTIVE");
			cm.setCreate_time(now);
			cm.setUpdate_time(now);
			cm.setCreator("interface");
			cm.setUpdate_name("interface");

			try {
				channelMerchantDao.insert(cm);
			} catch (Exception e) {
				e.printStackTrace();
				errorMessage = "please wait a moment.";
				_log.info("VVVV orderNo=[" + m.getMer_no() + "],errorMessage=[channelMerchant 保存失败 insert db time]");
				return Retutil.createFailBiz(RetEnum.RET_V_REPEAT_YL, errorMessage);
			}

			Routemapping r = new Routemapping();

			// TODO 渠道号暂时写死
			_log.info("#######渠道号暂时写死 ##   2088201916734621 = org_pid  ###############################33");
			r.setChnl_id(Long.valueOf("2088201916734621"));

			r.setSub_mer_id(cm.getSub_mch_id());
			r.setMer_no(m.getMer_no());
			r.setTran_type(TrxEnum.WX_D_J.getCode());
			r.setStatus("ACTIVE");

			r.setCreator("interface");
			r.setUpdate_name("interface");
			r.setCreate_time(now);
			r.setUpdate_time(now);

			try {
				routemappingDao.insert(r);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		/// 银联 消费收款码产品//银联 消费收款码产品//银联 消费收款码产品//银联 消费收款码产品//银联 消费收款码产品//银联 消费收款码产品//银联
		/// 消费收款码产品/////////
		/** 银联二维码不通次处 不开启 **/
		Map<String, String> unionPayResult = demo.doBindQrcode(m);

		if ("10000".equals(unionPayResult.get("respCode")) && unionPayResult.get("merId") != null) {
			// 成功的逻辑
			// channel表暂时写死支付宝，后期更改为活的
			String CM_channel_id = "15";
			ChannelMerchant cm = new ChannelMerchant();
			Date now = new Date();
			// 阿里侧动码
			cm.setMerchant_name(m.getMer_name());
			cm.setMerchant_shortname(m.getShort_name());
			cm.setService_phone(m.getLegal_contact());
			cm.setContact(m.getLegal_name());
			cm.setContact_phone(m.getLegal_contact());
			cm.setContact_email("");
			cm.setBusiness(m.getMcc());//经营类目
			cm.setChannel_id(Long.valueOf(CM_channel_id));
			cm.setContact_weachatid_type("");
			cm.setContact_wechatid("");
			cm.setMerchant_remark(m.getMer_no());
			// 通道商编
			cm.setSub_mch_id(unionPayResult.get("merId"));
			try {
				wxPayResult.remove("sign");
				cm.setData_json(JSONObject.fromObject(wxPayResult).toString());
			} catch (Exception e) {
				e.printStackTrace();
				cm.setData_json("解析异常");
			}
			
			cm.setType(TrxEnum.YLB_G_J.getCode());
			cm.setStatus("ACTIVE");
			cm.setCreate_time(now);
			cm.setUpdate_time(now);
			cm.setCreator("interface");
			cm.setUpdate_name("interface");

			try {
				channelMerchantDao.insert(cm);
			} catch (Exception e) {
				e.printStackTrace();
				errorMessage = "please wait a moment.";
				_log.info("VVVV orderNo=[" + m.getMer_no() + "],errorMessage=[channelMerchant 保存失败 insert db time]");
				return Retutil.createFailBiz(RetEnum.RET_V_REPEAT_YL, errorMessage);
			}
			
			Routemapping r = new Routemapping();

			// TODO 渠道号暂时写死
			_log.info("#######渠道号暂时写死 ##   C0001591 = expandcode  ###############################33");
			r.setChnl_id(Long.valueOf("2088201916734621"));

			r.setSub_mer_id(cm.getSub_mch_id());
			r.setMer_no(m.getMer_no());
			r.setTran_type(TrxEnum.YLB_G_J.getCode());
			r.setStatus("ACTIVE");

			r.setCreator("interface");
			r.setUpdate_name("interface");
			r.setCreate_time(now);
			r.setUpdate_time(now);

			try {
				routemappingDao.insert(r);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		/** 银联二维码不通次处 不开启 **/

		//3.自身绑定的业务逻辑
		
		FixedQrRepository fixedQrRepository = data.getObject("fixedQrRepository", FixedQrRepository.class);
		
		fixedQrRepository.setMer_no(m.getMer_no());
		if (unionPayResult.get("merId") != null) {
			fixedQrRepository.setUnion_mer_id(unionPayResult.get("merId"));
		}
		fixedQrRepository.setBind_time(new Date());
		fixedQrRepositoryDao.insert(fixedQrRepository);
		
		
		bizResult = Retutil.createInnoBiz(null);
		return bizResult;

	}

	@DataSource("King")
	// @DataSource("Queen")//生产走从库
	public Map<String, String> unified4AliPay(String jsonParam) throws Exception {
		Map<String, String> bizResult = null;
		com.alibaba.fastjson.JSONObject jsonParams = com.alibaba.fastjson.JSONObject.parseObject(jsonParam);
		String sub_mer_id = jsonParams.getString("sub_mer_id");// 通道商编
		Channel channel = jsonParams.getObject("channel", Channel.class);// 系统商户
		OrderDetail orderDetail = jsonParams.getObject("orderDetail", OrderDetail.class);// 系统商户

		Map<String, String> commonData = new HashMap<String, String>();
		commonData.put("out_trade_no", orderDetail.getSerial_no());
		if ("Fixed".equals(jsonParams.getString("commonCode"))) {
			commonData.put("total_amount", jsonParams.getString("transAmt"));
		} else {
			commonData.put("total_amount", orderDetail.getOrder_amount());
		}

		commonData.put("subject", orderDetail.getBody());

		commonData.put("timeout_express", AliPayUtil.mkDate(orderDetail.getStart_time(), orderDetail.getExpire_time()));
		commonData.put("qr_code_timeout_express", commonData.get("timeout_express"));
		commonData.put("timestamp", AliPayUtil.toDates(orderDetail.getStart_time()));
		commonData.put("notify_url", Constant.AlipayConstant.TRANS_NOTIFY);

		commonData.put("org_pid", channel.getChannel_id());
		commonData.put("merchant_id", sub_mer_id);
		commonData.put("app_id", channel.getApp_id());
		commonData.put("privatekey", channel.getPrivate_key());
		commonData.put("publickey", channel.getPublic_key());
		
		Map<String, String> respMsg = aliPayService.doTradePrecreate(commonData);

		if (!RetEnum.RET_SUCCESS.getCode().equals(respMsg.get(Constant.RETURN_PARAM_RETCODE))) {
			orderDetail.setTrx_type(TrxEnum.ZFB_D_J.getCode());
			orderDetail.setState(Constant.PAY_STATUS_FAILED);
			orderDetail.setResp_code(respMsg.get(Constant.RETURN_PARAM_RETCODE));
			orderDetail.setResp_msg(respMsg.get(Constant.RETURN_PARAM_RETMSG));

			orderDetail.setApp_id(channel.getApp_id());
			orderDetail.setOrder_amount(commonData.get("total_amount"));

			orderDetail.setChannel_id(channel.getChannel_id());// org_id
			orderDetail.setSub_mch_id(sub_mer_id);
			orderDetailDao.updateByPrimaryKey(orderDetail);
			respMsg.put("qr_code", "/errAli.jsp");
			bizResult = Retutil.createInnoBiz(respMsg);
			return bizResult;
		}

		orderDetail.setTrx_type(TrxEnum.ZFB_D_J.getCode());
		orderDetail.setState(Constant.PAY_STATUS_PAYING);
		orderDetail.setApp_id(channel.getApp_id());
		orderDetail.setChannel_id(channel.getChannel_id());// org_id
		orderDetail.setSub_mch_id(sub_mer_id);
		orderDetail.setOrder_amount(commonData.get("total_amount"));
		// 把appid 等信息存上 用于退款 查询等
		// TODO 计算 手续费等信息
		orderDetailDao.updateByPrimaryKey(orderDetail);

		bizResult = Retutil.createInnoBiz(respMsg);

		channel = null;
		respMsg = null;
		commonData = null;
		sub_mer_id = null;
		jsonParams = null;
		orderDetail = null;
		return bizResult;

	}

	@DataSource("King")
	public Map<String, Object> unified4Tencent(String params) throws Exception {
		JsapiModel Jsapi = new JsapiModel();
		com.alibaba.fastjson.JSONObject jsonParams = com.alibaba.fastjson.JSONObject.parseObject(params);

		String sub_mer_id = jsonParams.getString("sub_mer_id");// 通道商编
		String code = jsonParams.getString("code_no");// 微信token
		String client_ip = jsonParams.getString("client_ip");// 通道商编
		OrderDetail orderDetail = jsonParams.getObject("orderDetail", OrderDetail.class);// 系统商户
		Channel channel = jsonParams.getObject("channel", Channel.class);// 系统商户

		_log.info("TTTT 使用code [" + code + "] 换取openId , serial_no =" + orderDetail.getSerial_no());

		String openid = getOpenId(code);

		Map<String, String> commonData = new HashMap<String, String>();
		commonData.put("out_trade_no", orderDetail.getSerial_no());
		if ("fx".equals(jsonParams.getString("order_amount"))) {
			commonData.put("total_fee", orderDetail.getOrder_amount());
		} else {
			commonData.put("total_fee", jsonParams.getString("order_amount"));
		}
		
		commonData.put("body", orderDetail.getBody());
		commonData.put("spbill_create_ip", client_ip);
		commonData.put("time_start", orderDetail.getStart_time());
		commonData.put("time_expire", orderDetail.getExpire_time());
		// 生产
		commonData.put("openid", openid);
		// 测试
		//commonData.put("openid", channel.getUpdate_name());
		commonData.put("channel_id", channel.getChannel_id());
		commonData.put("appid", channel.getApp_id());
		commonData.put("mch_id", channel.getMer_id());
		commonData.put("sub_mch_id", sub_mer_id);
		
		Map<String, String> respMsg = wxPayService.doOrderPrePay(commonData , channel.getPrivate_key() ,channel.getPublic_key());
		Map<String, Object> model = new HashMap<String, Object>();
		if (!RetEnum.RET_SUCCESS.getCode().equals(respMsg.get(Constant.RETURN_PARAM_RETCODE))) {
			Jsapi.setIsSuccess("false");
			Jsapi.setErrMsg(respMsg.get(Constant.RETURN_PARAM_RETMSG));
			model.put("form", Jsapi);

			orderDetail.setState(Constant.PAY_STATUS_FAILED);
			orderDetail.setResp_code(respMsg.get(Constant.RETURN_PARAM_RETCODE));
			orderDetail.setResp_msg(respMsg.get(Constant.RETURN_PARAM_RETMSG));
			orderDetail.setTrx_type(TrxEnum.WX_D_J.getCode());
			orderDetail.setApp_id(channel.getApp_id());
			orderDetail.setChan_mer_id(channel.getMer_id());
			orderDetail.setChannel_id(channel.getChannel_id());
			orderDetail.setSub_mch_id(sub_mer_id);
			orderDetail.setOrder_amount(commonData.get("total_fee"));
			orderDetailDao.updateByPrimaryKey(orderDetail);
			return model;
		}
		JSONObject data = JSONObject.fromObject(respMsg.get("wc_pay_data"));

		Jsapi.setIsSuccess("true");
		Jsapi.setAppId(data.getString("appId"));
		Jsapi.setTimeStamp(data.getString("timeStamp"));
		Jsapi.setNonceStr(data.getString("nonceStr"));
		Jsapi.setPackages(data.getString("package"));
		Jsapi.setSignType(data.getString("signType"));
		Jsapi.setPaySign(data.getString("paySign"));

		orderDetail.setTrx_type(TrxEnum.WX_D_J.getCode());
		orderDetail.setState(Constant.PAY_STATUS_PAYING);
		orderDetail.setApp_id(channel.getApp_id());
		orderDetail.setChan_mer_id(channel.getMer_id());
		orderDetail.setChannel_id(channel.getChannel_id());
		orderDetail.setSub_mch_id(sub_mer_id);
		orderDetail.setOrder_amount(commonData.get("total_fee"));
		// 把appid 等信息存上 用于退款 查询等
		// TODO 计算 手续费等信息
		orderDetailDao.updateByPrimaryKey(orderDetail);

		model.put("form", Jsapi);
		code = null;
		openid = null;
		channel = null;
		respMsg = null;
		commonData = null;
		sub_mer_id = null;
		jsonParams = null;
		orderDetail = null;
		return model;

	}

	public static String getOpenId(String codeId) {
		String openid = "";
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
		StringBuffer bf = new StringBuffer();
		bf.append("appid=");
		bf.append("wxa35fdd49af4a2b27");
		bf.append("&secret=");
		bf.append("d4ea2066d45d5e18506b713801757465");
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

	private static String randomByMerNo() {
		StringBuffer sb = new StringBuffer();
		int a[] = new int[10];
		for (int i = 0; i < a.length; i++) {
			a[i] = (int) (10 * (Math.random()));
			sb.append(a[i]);
		}
		return sb.toString();
	}

	public String reqKeyMd5(Merchant mer) throws Exception {
		StringBuffer sb = new StringBuffer();

		sb.append(mer.getMer_no()).append("@#").append(mer.getLegal_id()).append("#$").append(mer.getSettle_no())
				.append("%!").append(mer.getPay_type());

		return signUtil.MD5(URLEncoder.encode(sb.toString(), "UTF-8"));
	}

	public static void main(String[] args) {
		getOpenId("081smBgs1KBhfm0piths1MkYgs1smBgU");
	}

}
