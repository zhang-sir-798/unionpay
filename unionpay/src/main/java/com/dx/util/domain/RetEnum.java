package com.dx.util.domain;

/**
 * RPC调用返回码枚举类
 * 对应方法调用返回值中的rpcRetCode和rpcRetMsg
 * 所有响应码应以子系统开头命名，录入routeServie(路由子系统)  R + 0001 
 */
public enum RetEnum {


    RET_SUCCESS("0000", "请求成功"),
    CARD_SUCCESS("0001", "已绑卡"),

    //公共错误信息码
    RET_C_NO_FAILED("C001","请求失败"),
    RET_C_NO_COMMUNICATION("C002","通讯异常"),
    RET_C_NO_SIGN("C003","验签失败"),
    //路由信息
    RET_R_EXCEPTION("R001", "路由信息异常,"),
    RET_R_NOFOUND("R002", "没有相关路由信息,"),
    //payrule错误信息码
    RET_V_INVALID("V001","请求参数无效,"),
    RET_V_PROD_INVALID("V002","商户产品尚未开通,"),
    RET_V_MER_INVALID("V003","商户创建异常,"),
    RET_V_PROD_LAYERED_INVALID("V004","商户分层产品尚未开通,"),
    RET_V_CARDBIN_INVALID("V005","卡bin验证失败,"),
    RET_V_AMONT_INVALID("V006","金额超出限制,"),
    RET_V_EXCEPTION("V007", "远程服务调用处理异常"),
    RET_V_SIGN_FAIL("V008", "签名计算错误"),
    RET_V_NO_INVALID("V009","订单号重复,"),
    RET_V_REPEAT_SCAN("V010","每个二维码仅可扫描一次,请联系商家重新下单。"),
    RET_V_REPEAT_ORDER("V011","订单状态异常，请稍后查询"),
    RET_V_REPEAT_YL("V012","交易阻塞,"),
    
    //settle错误信息码
    RET_SET_AGT_LAYERED_INVALID("S002","代理商分层产品尚未开通,"),
    RET_SET_MER_FEE_INVALID("S004","商户费率配置无效,"),
    RET_SET_AGT_FEE_INVALID("S005","代理商费率配置无效,"),
    RET_SET_CHL_FEE_INVALID("S006","通道费率配置无效,"),
    
    //scancode错误信息码
    RET_S_MER_FEE_INVALID("X001","订单号重复,"),
    RET_S_CHAL_INVALID("X002","通道不可用,"),
    RET_S_ORDER_INVALID("X003","订单状态异常,"),
    
    RET_PARAM_NOT_FOUND("0101", "参数不存在,"),
    RET_PARAM_INVALID("0102", "无效的参数,"),
    
    
    RET_PARAM_WX("W999", "微信侧返回信息,"),
    RET_PARAM_ALI("A999", "支付宝侧返回信息,"),
    
    // 未知错误
    RET_UNKNOWN_ERROR("9999", "未知错误");

    private String code;
    private String message;

    private RetEnum(String code, String message) { 
    	this.code = code;
        this.message = message; 
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static RetEnum getRetEnum(String code) {
        if (code == null) {
            return null;
        }

        RetEnum[] values = RetEnum.values();
        for (RetEnum e : values) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
