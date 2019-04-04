package com.dx.util.alibaba;

/**
 * 支付宝支付常量
 */
public interface AliPayConstants {
	
	String VERSION = "1.0";
	String FAIL = "Fail";
	String SUCCESS  = "Success";
	String FIELD_SIGN = "sign";
	String FIELD_SIGN_TYPE = "sign_type";
	String SHA256withRSA = "SHA256withRSA";
	String MD5 = "MD5";
	String KEY_ALGORITHM = "RSA";
	String CHARSET_UTF_8 = "UTF-8";
	String FORMAT = "JSON";
	boolean isIfValidateRemoteCert = false;//测试false,生产true
	String AMPERSAND = "&";
	String EQUAL = "=";
	String LEFT_BRACE = "{";
	String RIGHT_BRACE = "}";
}

