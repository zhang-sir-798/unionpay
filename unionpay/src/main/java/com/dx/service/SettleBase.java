package com.dx.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.dx.model.Merchant;
import com.dx.model.MerchantFee;

public class SettleBase {/*

	private static final Log log = LogFactory.getLog(SettleBase.class);

	*//**
	 * 计算结算，分润等信息
	 * 
	 * @param request
	 * @return
	 *//*
	public Map<String, String> executeSettle(Map<String, String> reqMap, Merchant mer) throws Exception {
		Map<String, String> retMap = new HashMap<String, String>();
		BigDecimal merCalFee = new BigDecimal(0);
		BigDecimal agentCalFee = new BigDecimal(0);
		BigDecimal rootAgentCalFee = new BigDecimal(0);
		BigDecimal agentProfit = new BigDecimal(0);
		BigDecimal agentSettle = new BigDecimal(0);
		BigDecimal agentProfitLV1 = new BigDecimal(0);
		BigDecimal agentSettleLV1 = new BigDecimal(0);

		Map<String, String> params = new HashMap<String, String>();
		String CDFlag = "";
		if ("01".equals(reqMap.get("cardAttr"))) {
			CDFlag = "DEBIT";// 借记卡
		} else if ("00".equals(reqMap.get("cardAttr"))) {
			CDFlag = "CREDIT";// 贷记卡
		}

		String feeType = mer.getSettleType();// S0 D0 T1
		String orderNo = reqMap.get("transSeqId");
		String amount = reqMap.get("txnAmt");
		params.put("merId", mer.getId().toString());
		params.put("feeType", mer.getSettleType());
		params.put("amount", reqMap.get("txnAmt"));
		params.put("CDFlag", CDFlag);

		// 1.商户费率处理
		Map<String, String> merFee = getMerFee(params);
		log.info("##开始计算商户[" + mer.getMer_no() + "]" + CDFlag + "卡手续费：" + "，交易金额:" + amount + "，费率："
				+ merFee.get("merRatio") + "，封顶值:" + merFee.get("merUpLimit") + "，进位方式:" + "N" + "，用户结算类型:" + feeType
				+ "单号: " + orderNo);
		merCalFee = calcFee(amount, null, merFee.get("merUpLimit"), "N", "RC", merFee.get("merRatio"));

		// 2.代理商费率处理
		// 2.1 一级代理
		params.put("agentOrMerType", "AGENT");
		params.put("AgentId", Integer.toString(mer.getRootAgentId()));
		Map<String, String> rootAgentFee = getAgentFee(params);
		rootAgentCalFee = calcFee(amount, null, rootAgentFee.get("agentUpLimit"), "N", "RC",
				rootAgentFee.get("agentRatio"));
		log.info("一级代理手续费：" + rootAgentCalFee);

		// 2.2 二级代理
		params.put("AgentId", Integer.toString(mer.getAgentId()));
		params.put("RootAgentId", Integer.toString(mer.getRootAgentId()));
		params.put("agentOrMerType", "PROMOTER");
		Map<String, String> agentFee = getAgentFee(params);
		agentCalFee = calcFee(amount, null, agentFee.get("agentUpLimit"), "N", "RC", agentFee.get("agentRatio"));
		log.info("二级代理手续费：" + agentCalFee);

		// 计算获取商户结算金额
		BigDecimal settleAmtBig = new BigDecimal(amount).subtract(merCalFee);

		// 二级代理分润
		agentProfit = merCalFee.subtract(agentCalFee);

		// 二级代理结算金额
		agentSettle = new BigDecimal(amount).subtract(agentCalFee);

		// 一级代理分润
		agentProfitLV1 = agentCalFee.subtract(rootAgentCalFee);
		// 一级代理结算金额
		agentSettleLV1 = new BigDecimal(amount).subtract(rootAgentCalFee);
		log.info("##商户[" + mer.getMer_no() + "]结算金额：" + settleAmtBig + "分" + "，商户手续费：" + merCalFee + "，推广员结算金额："
				+ agentSettle + "分" + "，推广员分润：" + agentProfit + "分" + "，拓展机构结算金额：" + agentSettleLV1 + "分" + "，拓展机构分润："
				+ agentProfitLV1 + "分,单号: " + orderNo);

		retMap.put("merFee", merCalFee.toString()); // 商户手续费
		retMap.put("merSettleAmt", settleAmtBig.toString());// 商户结算金额

		retMap.put("agentLv1Profit", agentProfitLV1.toString());// 一级代理分润
		retMap.put("agentLv1SettleAmt", agentSettleLV1.toString());// 一级代理结算金额

		retMap.put("agentLv2SettleAmt", agentSettle.toString());// 二级代理结算金额
		retMap.put("agentLv2Profit", agentProfit.toString());// 二级代理分润
		retMap.put("merNo", mer.getMer_no());// 商编
		return retMap;

	}

	*//**
	 * amount 交易金额 floorLimit 手续费最低值 upLimit 手续费封顶值 scaleType 进位方式 feeType费率类型
	 * feeRatio费率 fixed 固定值
	 *//*

	public BigDecimal calcFee(String amount, String floorLimit, String upLimit, String scaleType, String feeType,
			String feeRatio) {

		// TODO 区分返回值是否有问题，确定scalType界面记录的是什么

		BigDecimal bigAmount = new BigDecimal(amount);
		BigDecimal rateFee = null;
		// 费率比值 例如 proType=B2CT1
		if (feeType.equals("R")) {// 固定值
			return new BigDecimal("fixed");
		} else if (feeType.equals("R")) {// 比例
			BigDecimal fee = new BigDecimal(feeRatio);
			rateFee = bigAmount.multiply(fee);
			return rateFee.setScale(0, RoundingMode.UP);
		} else if (feeType.equals("RC")) { // 比例+封顶
			// 根据通道产品费率是全进位还是四舍五入 --标志
			BigDecimal fee = new BigDecimal(feeRatio);
			rateFee = bigAmount.multiply(fee);
			BigDecimal upLimitBig = new BigDecimal(upLimit);
			if (!StringUtils.isBlank(upLimit)) {
				if (rateFee.compareTo(upLimitBig) > 0) {// 最高
					return upLimitBig;
				} else {
					return rateFee.setScale(0, RoundingMode.UP);
				}
			} else {
				return rateFee.setScale(0, RoundingMode.UP);
			}

		} else if (feeType.equals("RFLR")) { // 比例+最低值
			BigDecimal floorLimitBig = new BigDecimal(floorLimit);
			BigDecimal fee = new BigDecimal(feeRatio);
			rateFee = bigAmount.multiply(fee);
			if (rateFee.compareTo(floorLimitBig) < 0) {// 最小
				return floorLimitBig;
			} else {
				if ("N".equals(scaleType)) {
					return rateFee.setScale(0, RoundingMode.UP);
				} else {
					return rateFee.setScale(0, RoundingMode.HALF_UP);
				}
			}
		} else if (feeType.equals("RCFLR")) { // 比例+最低值+封顶值
			BigDecimal fee = new BigDecimal(feeRatio);
			rateFee = bigAmount.multiply(fee);
			BigDecimal floorLimitBig = new BigDecimal(floorLimit);
			BigDecimal upLimitBig = new BigDecimal(upLimit);
			if (rateFee.compareTo(floorLimitBig) > 0 && rateFee.compareTo(floorLimitBig) < 0) {
				if ("N".equals(scaleType)) {
					if ("N".equals(scaleType)) {
						return rateFee.setScale(0, RoundingMode.UP);
					} else {
						return rateFee.setScale(0, RoundingMode.HALF_UP);
					}
				} else {
					if ("N".equals(scaleType)) {
						return rateFee.setScale(0, RoundingMode.UP);
					} else {
						return rateFee.setScale(0, RoundingMode.HALF_UP);
					}
				}
			} else if (rateFee.compareTo(floorLimitBig) < 0) {// 最高
				return floorLimitBig;
			} else if (rateFee.compareTo(upLimitBig) > 0) {
				return floorLimitBig;
			}
		}
		return rateFee;

	}

	// 取商户费率
	public Map<String, String> getMerFee(Map<String, String> params) {
		Map<String, String> retMap = new HashMap<String, String>();
		String feeType = params.get("feeType");
		String CDFlag = params.get("CDFlag");
		Map<String, String> reqMap = new HashMap<String, String>();

		reqMap.put("merId", params.get("merId"));
		reqMap.put("feeType", params.get("feeType"));
		reqMap.put("amount", params.get("amount"));
		// sql 已经通过金额 区分出具体的费率
		MerchantFee merFee = merchantFeeDao.getMerchantFee(reqMap);
		if (merFee != null) {

			if ("D1".equals(feeType)) {
				if ("CREDIT".equals(CDFlag)) {
					retMap.put("merRatio", merFee.getD1CreditFee());
					if (merFee.getD1CreditCapp() != null) {
						retMap.put("merUpLimit", merFee.getD1DebitCapp());
					} else {
						retMap.put("merUpLimit", "");
					}
				} else {
					retMap.put("merRatio", merFee.getD1DebitFee());
					if (merFee.getD1DebitCapp() != null) {
						retMap.put("merUpLimit", merFee.getD1DebitCapp());
					} else {
						retMap.put("merUpLimit", "");
					}

				}
			} else if ("S0".equals(feeType)) {
				if ("CREDIT".equals(CDFlag)) {
					retMap.put("merRatio", merFee.getS0CreditFee());

					if (merFee.getS0CreditCapp() != null) {
						retMap.put("merUpLimit", merFee.getS0CreditCapp());
					} else {
						retMap.put("merUpLimit", "");
					}

				} else {
					retMap.put("merRatio", merFee.getS0DebitFee());

					if (merFee.getS0DebitCapp() != null) {
						retMap.put("merUpLimit", merFee.getS0DebitCapp());
					} else {
						retMap.put("merUpLimit", "");
					}

				}
			} else if ("T1".equals(feeType)) {
				if ("CREDIT".equals(CDFlag)) {
					retMap.put("merRatio", merFee.getT1CreditFee());
					if (merFee.getT1CreditCapp() != null) {
						retMap.put("merUpLimit", merFee.getT1CreditCapp());
					} else {
						retMap.put("merUpLimit", "");
					}

				} else {
					retMap.put("merRatio", merFee.getT1DebitFee());
					if (merFee.getT1DebitCapp() != null) {
						retMap.put("merUpLimit", merFee.getT1DebitCapp());
					} else {
						retMap.put("merUpLimit", "");
					}

				}
			}
		}
		if (!"".equals(retMap.get("merUpLimit"))) {
			String UpLimit = retMap.get("merUpLimit");
			BigDecimal upLimit = new BigDecimal(UpLimit);
			BigDecimal upLimitT = upLimit.multiply(new BigDecimal(100));// 乘以100(单位：分)
			retMap.put("merUpLimit", upLimitT.toString());
		}
		return retMap;
	}

	// 取代理商费率
	public Map<String, String> getAgentFee(Map<String, String> params) {
		Map<String, String> retMap = new HashMap<String, String>();
		String feeType = params.get("feeType");
		String CDFlag = params.get("CDFlag");
		Map<String, String> reqMap = new HashMap<String, String>();

		reqMap.put("agentId", params.get("AgentId"));
		reqMap.put("feeType", params.get("agentOrMerType"));
		reqMap.put("amount", params.get("amount"));
		// sql 已经通过金额 区分出具体的费率
		AgentFee agentFee = agentFeeDao.getAgentFee(reqMap);
		if (agentFee != null) {

			if ("D1".equals(feeType)) {
				if ("CREDIT".equals(CDFlag)) {
					retMap.put("agentRatio", agentFee.getD1CreditFee());
					if (agentFee.getD1CreditCapp() != null) {
						retMap.put("agentUpLimit", agentFee.getD1DebitCapp());
					} else {
						retMap.put("agentUpLimit", "");
					}

				} else {
					retMap.put("agentRatio", agentFee.getD1DebitFee());
					if (agentFee.getD1DebitCapp() != null) {
						retMap.put("agentUpLimit", agentFee.getD1DebitCapp());
					} else {
						retMap.put("agentUpLimit", "");
					}

				}
			} else if ("S0".equals(feeType)) {
				if ("CREDIT".equals(CDFlag)) {
					retMap.put("agentRatio", agentFee.getS0CreditFee());

					if (agentFee.getS0CreditCapp() != null) {
						retMap.put("agentUpLimit", agentFee.getS0CreditCapp());
					} else {
						retMap.put("agentUpLimit", "");
					}

				} else {
					retMap.put("agentRatio", agentFee.getS0DebitFee());

					if (agentFee.getS0DebitCapp() != null) {
						retMap.put("agentUpLimit", agentFee.getS0DebitCapp());
					} else {
						retMap.put("agentUpLimit", "");
					}

				}
			} else if ("T1".equals(feeType)) {
				if ("CREDIT".equals(CDFlag)) {
					retMap.put("agentRatio", agentFee.getT1CreditFee());
					if (agentFee.getT1CreditCapp() != null) {
						retMap.put("agentUpLimit", agentFee.getT1CreditCapp());
					} else {
						retMap.put("agentUpLimit", "");
					}

				} else {
					retMap.put("agentRatio", agentFee.getT1DebitFee());
					if (agentFee.getT1DebitCapp() != null) {
						retMap.put("agentUpLimit", agentFee.getT1DebitCapp());
					} else {
						retMap.put("agentUpLimit", "");
					}

				}
			}
		} else {
			Map<String, String> reqMapT = new HashMap<String, String>();

			reqMapT.put("agentId", params.get("RootAgentId"));
			reqMapT.put("feeType", params.get("agentOrMerType"));
			reqMapT.put("amount", params.get("amount"));
			agentFee = agentFeeDao.getAgentFee(reqMapT);
			if (agentFee != null) {
				if ("D1".equals(feeType)) {
					if ("CREDIT".equals(CDFlag)) {
						retMap.put("agentRatio", agentFee.getD1CreditFee());
						if (agentFee.getD1CreditCapp() != null) {
							retMap.put("agentUpLimit", agentFee.getD1DebitCapp());
						} else {
							retMap.put("agentUpLimit", "");
						}

					} else {
						retMap.put("agentRatio", agentFee.getD1DebitFee());
						if (agentFee.getD1DebitCapp() != null) {
							retMap.put("agentUpLimit", agentFee.getD1DebitCapp());
						} else {
							retMap.put("agentUpLimit", "");
						}

					}
				} else if ("S0".equals(feeType)) {
					if ("CREDIT".equals(CDFlag)) {
						retMap.put("agentRatio", agentFee.getS0CreditFee());

						if (agentFee.getS0CreditCapp() != null) {
							retMap.put("agentUpLimit", agentFee.getS0CreditCapp());
						} else {
							retMap.put("agentUpLimit", "");
						}

					} else {
						retMap.put("agentRatio", agentFee.getS0DebitFee());

						if (agentFee.getS0DebitCapp() != null) {
							retMap.put("agentUpLimit", agentFee.getS0DebitCapp());
						} else {
							retMap.put("agentUpLimit", "");
						}

					}
				} else if ("T1".equals(feeType)) {
					if ("CREDIT".equals(CDFlag)) {
						retMap.put("agentRatio", agentFee.getT1CreditFee());
						if (agentFee.getT1CreditCapp() != null) {
							retMap.put("agentUpLimit", agentFee.getT1CreditCapp());
						} else {
							retMap.put("agentUpLimit", "");
						}

					} else {
						retMap.put("agentRatio", agentFee.getT1DebitFee());
						if (agentFee.getT1DebitCapp() != null) {
							retMap.put("agentUpLimit", agentFee.getT1DebitCapp());
						} else {
							retMap.put("agentUpLimit", "");
						}

					}
				}
			}
		}

		if (!"".equals(retMap.get("agentUpLimit"))) {
			String UpLimit = retMap.get("agentUpLimit");
			BigDecimal upLimit = new BigDecimal(UpLimit);
			BigDecimal upLimitT = upLimit.multiply(new BigDecimal(100));// 乘以100(单位：分)
			retMap.put("agentUpLimit", upLimitT.toString());
		}

		return retMap;
	}
*/}
