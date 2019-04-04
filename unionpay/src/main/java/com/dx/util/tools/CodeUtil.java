package com.dx.util.tools;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;

public class CodeUtil {
	/**
	 * map转json字符串
	 * 
	 * @param map
	 * @return
	 */
	public String mapToJSON(Map<String, String> map) {
		JSONObject pack = new JSONObject();
		pack.accumulateAll(map);
		return pack.toString();
	}

	/**
	 * json字符串转map
	 * 
	 * @param jsonStr
	 * @return
	 */
	public Map<String, String> JSONToMap(String jsonStr) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		@SuppressWarnings("unchecked")
		Set<String> set = jsonObject.keySet();
		for (String key : set) {
			map.put(key, jsonObject.getString(key));
		}
		return map;
	}

	public String asciiToString(byte[] value) {
		try {
			return new String(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public String amountTo12(String amount) {
		BigDecimal b = new BigDecimal(amount);
		int amount12 = b.multiply(new BigDecimal("100")).intValue();
		return amount12 + "";
	}

	/**
	 * bytes转换成十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public String byte2HexStr(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	public byte[] hexStr2Bytes(String src) {
		int m = 0, n = 0;
		int l = src.length() / 2;
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = uniteBytes(src.substring(i * 2, m), src.substring(m, n));
		}
		return ret;
	}

	private byte uniteBytes(String src0, String src1) {
		byte b0 = Byte.decode("0x" + src0).byteValue();
		b0 = (byte) (b0 << 4);
		byte b1 = Byte.decode("0x" + src1).byteValue();
		byte ret = (byte) (b0 | b1);
		return ret;
	}

	/**
	 * Base64解码
	 * 
	 * @param bytes
	 * @return
	 */
	public byte[] decodeB64(final byte[] bytes) {
		return Base64.decodeBase64(bytes);
	}

	/**
	 * 二进制数据编码为BASE64字符串
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public String encodeB64(final byte[] bytes) {
		try {
			return new String(Base64.encodeBase64(bytes), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String generalStr(Map<String, String> reqMap) throws Exception {
		StringBuffer mabStr = new StringBuffer();
		for (String key : reqMap.keySet()) {
			mabStr.append(key);
			mabStr.append("=");
			mabStr.append(reqMap.get(key));
			mabStr.append("&");
		}
		String bodyStr = mabStr.toString();
		bodyStr = bodyStr.substring(0, bodyStr.length() - 1);
		return bodyStr;
	}

	public static String encode(String encode) {
		return encode.replace("+", "!").replace("/", "*").replaceAll("\r|\n", "");
	}
}
