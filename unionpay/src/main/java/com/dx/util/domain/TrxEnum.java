package com.dx.util.domain;

/**
 * 
 * @ClassName TrxEnum
 * @Description 加密工具类
 * @author zhangsir
 * @Date 2018年12月5日 上午11:29:00
 * @version 1.0.0
 * 
 */
public enum TrxEnum {

	//系统内部交易类型枚举
    WX_D_J("010101", "间联动态码-主扫-微信侧"), 
    WX_D_J_BS("010102", "间联动态码-被扫-微信侧"), 
    WX_G_J("010103", "间联固定码-微信侧"), 
    
    ZFB_D_J("010201", "间联动态码-主扫-支付宝侧"),
    ZFB_D_J_BS("010202", "间联动态码-被扫-支付宝侧"),
    ZFB_G_J("010203", "间联固定码-支付宝侧"), 
    
    YLB_D_J("010301","间联动态码-主扫-银联二维码"),
    YLB_D_J_BS("010302","间联动态码-被扫-银联二维码"),
    YLB_G_J("010303","间联固定码-银联二维码"),
    
    UN_D_Z("010401","直连动态码-主扫-聚合码"),
    UN_D_Z_BS("010402","直连动态码-被扫-聚合码"),
    UN_G_Z("010403","直连固定码-聚合码"),
    

    Q_WAP_2C("000101","全渠道-手机WAP产品-B2C"),
    Q_WAP_2B("000102","全渠道-手机WAP产品-B2B"),
    
    Q_B2C("000201","全渠道-网关产品-B2C"),
    Q_B2B("000202","全渠道-网关产品-B2B"),
    
    Q_KJ_MIX("000301","全渠道-无跳转产品-银联侧-开通并交易"),
    Q_KJ_OPN("000302","全渠道-无跳转产品-银联侧-开通"),
    Q_KJ_PAY("000303","全渠道-无跳转产品-银联侧-消费"),
    
    
    //对下游
  	AT_CODE_D_J("010801","间联动态码聚合码-主扫-自行聚合"),
    
  	
	TRX_ERROR("9999", "未知错误交易类型");

	
	
	// 交易类型编码
	private String code;
	// 交易类型中文描述
	private String message;

	private TrxEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}
}
