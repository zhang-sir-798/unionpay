package com.dx.util.tencent;

/**
 * 微信支付常量
 */
public interface WXPayConstants {
	
	String FAIL = "FAIL";
	String SUCCESS  = "SUCCESS";
	String FIELD_SIGN = "sign";
	String FIELD_SIGN_TYPE = "sign_type";
	String SHA256withRSA = "SHA256withRSA";
	String MD5 = "MD5";
	String KEY_ALGORITHM = "RSA";
	String CHARSET_UTF_8 = "UTF-8";
	boolean isIfValidateRemoteCert = false;//测试false,生产true
	String AMPERSAND = "&";
	String EQUAL = "=";
}

