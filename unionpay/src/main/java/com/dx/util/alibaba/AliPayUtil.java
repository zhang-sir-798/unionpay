package com.dx.util.alibaba;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 阿里支付工具，如数据类型转换，加密等
 */
public class AliPayUtil {

	/**
	 * 将Map存储的对象，转换为key=value&key=value的字符
	 *
	 * @param requestParam
	 * @param coder
	 * @return
	 */
	public static String getRequestParamString(Map<String, String> requestParam, String coder) {
		if (null == coder || "".equals(coder)) {
			coder = "UTF-8";
		}
		StringBuffer sf = new StringBuffer("");
		String reqstr = "";
		if (null != requestParam && 0 != requestParam.size()) {
			for (Entry<String, String> en : requestParam.entrySet()) {
				try {
					sf.append(en.getKey() + "=" + (null == en.getValue() || "".equals(en.getValue()) ? ""
							: URLEncoder.encode(en.getValue(), coder)) + "&");
				} catch (UnsupportedEncodingException e) {
					return "";
				}
			}
			reqstr = sf.substring(0, sf.length() - 1);
		}
		return reqstr;
	}

	/**
	 * 将形如key=value&key=value的字符串转换为相应的Map对象
	 * 
	 * @param result
	 * @return
	 */
	public static Map<String, String> convertResultStringToMap(String result) {
		Map<String, String> map = null;

		if (result != null && !"".equals(result.trim())) {
			if (result.startsWith("{") && result.endsWith("}")) {
				result = result.substring(1, result.length() - 1);
			}
			map = parseQString(result);
		}
		return map;
	}

	/**
	 * 日志
	 * 
	 * @return
	 */
	public static Logger getLogger() {
		Logger logger = LoggerFactory.getLogger("alipay java sdk");
		return logger;
	}

	public static String toAmount(long amount) {
		return new BigDecimal(amount).divide(new BigDecimal(100)).toString();
	}

	public static String toDate(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	public static String toDates(String sdate) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String ret = "";
		Date startDate;
		try {
			startDate = formatter.parse(sdate);
			ret = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	public static Date pDates(String sdate) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date startDate = null;
		try {
			startDate = formatter.parse(sdate);

		} catch (ParseException e) {
			
			e.printStackTrace();
		}

		return startDate;
	}

	public static String mkDate(String sDate, String eDate) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

		Date startDate;
		Date endDate;
		long between = 0;
		long min = 0;

		try {
			startDate = formatter.parse(sDate);
			endDate = formatter.parse(eDate);
			between = (endDate.getTime() - startDate.getTime());// 得到两者的毫秒数
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		min = between / (60 * 1000);

		return Long.toString(min) + "m";
	}

	public static boolean subDate(String sDate, String eDate) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

		Date startDate;
		Date endDate;
		long between = 0;
		long min = 0;

		try {
			startDate = formatter.parse(sDate);
			endDate = formatter.parse(eDate);
			between = (endDate.getTime() - startDate.getTime());// 得到两者的毫秒数
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		min = between / (1000);

		if (60 * 1 <= min && min <= 60 * 120) {
			return true;
		}

		return false;
	}

	public static String generateSignature(Map<String, String> data, String key) throws Exception {
		String stringData = getSignContent(data);
		AliPayUtil.getLogger().info("签名内容 : " + stringData);
		return rsa256Sign(stringData, key);
	}

	public static boolean rsa256CheckContent(String content, String sign, String key) throws Exception {
		try {
			AliPayUtil.getLogger().info("公钥字符串： " + key);
			PublicKey pubKey = restorePublicKey(Base64.decodeBase64(key));

			Signature signature = Signature.getInstance(AliPayConstants.SHA256withRSA);

			signature.initVerify(pubKey);

			AliPayUtil.getLogger().info("验签内容： " + content);
			signature.update(content.getBytes(AliPayConstants.CHARSET_UTF_8));
			boolean result = signature.verify(Base64.decodeBase64(sign.getBytes()));
			AliPayUtil.getLogger().info("验签结果： " + result);
			return result;
		} catch (Exception e) {
			throw new Exception("RSAcontent = " + content + ",sign=" + sign + ",key = " + key, e);
		}
	}

	public static String getSignContent(Map<String, String> sortedParams) {
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(sortedParams.keySet());
		Collections.sort(keys);
		int index = 0;
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = sortedParams.get(key);
			if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
				content.append((index == 0 ? "" : "&") + key + "=" + value);
				++index;
			}
		}
		String stringData = content.toString();
		return stringData;
	}

	protected static String rsa256Sign(String content, String key) throws Exception {
		try {
			AliPayUtil.getLogger().info("私钥字符串： " + key);
			PrivateKey priKey = restorePrivateKey(Base64.decodeBase64(key));
			Signature signature = Signature.getInstance(AliPayConstants.SHA256withRSA);
			signature.initSign(priKey);
			signature.update(content.getBytes(AliPayConstants.CHARSET_UTF_8));
			byte[] signed = signature.sign();
			String sign = new String(base64Encode(signed), AliPayConstants.CHARSET_UTF_8);
			AliPayUtil.getLogger().info("生成签名sign : " + sign);
			return sign;
		} catch (Exception e) {
			throw new RuntimeException("RSAcontent = " + content + "; charset = " + AliPayConstants.CHARSET_UTF_8, e);
		}
	}

	/**
	 * 解析应答字符串，生成应答要素
	 * 
	 * @param str
	 *            需要解析的字符串
	 * @return 解析的结果map
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> parseQString(String str) {

		Map<String, String> map = new HashMap<String, String>();
		int len = str.length();
		StringBuilder temp = new StringBuilder();
		char curChar;
		String key = null;
		boolean isKey = true;
		boolean isOpen = false;// 值里有嵌套
		char openName = 0;
		if (len > 0) {
			for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
				curChar = str.charAt(i);// 取当前字符
				if (isKey) {// 如果当前生成的是key

					if (curChar == '=') {// 如果读取到=分隔符
						key = temp.toString();
						temp.setLength(0);
						isKey = false;
					} else {
						temp.append(curChar);
					}
				} else {// 如果当前生成的是value
					if (isOpen) {
						if (curChar == openName) {
							isOpen = false;
						}

					} else {// 如果没开启嵌套
						if (curChar == '{') {// 如果碰到，就开启嵌套
							isOpen = true;
							openName = '}';
						}
						if (curChar == '[') {
							isOpen = true;
							openName = ']';
						}
					}

					if (curChar == '&' && !isOpen) {// 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
						putKeyValueToMap(temp, isKey, key, map);
						temp.setLength(0);
						isKey = true;
					} else {
						temp.append(curChar);
					}
				}

			}
			putKeyValueToMap(temp, isKey, key, map);
		}
		return map;
	}

	private static void putKeyValueToMap(StringBuilder temp, boolean isKey, String key, Map<String, String> map) {
		if (isKey) {
			key = temp.toString();
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, "");
		} else {
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, temp.toString());
		}
	}

	/**
	 * BASE64编码
	 * 
	 * @param inputByte
	 *            待编码数据
	 * @return 解码后的数据
	 * @throws IOException
	 */
	public static byte[] base64Encode(byte[] inputByte) throws IOException {
		return Base64.encodeBase64(inputByte);
	}

	/**
	 * 还原公钥
	 * 
	 * @param keyBytes
	 * @return
	 */
	public static PublicKey restorePublicKey(byte[] keyBytes) {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
		try {
			KeyFactory factory = KeyFactory.getInstance(AliPayConstants.KEY_ALGORITHM);
			PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);
			return publicKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 还原私钥
	 * 
	 * @param keyBytes
	 * @return
	 */
	public static PrivateKey restorePrivateKey(byte[] keyBytes) {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
		try {
			KeyFactory factory = KeyFactory.getInstance(AliPayConstants.KEY_ALGORITHM);
			PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
			return privateKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		// System.out.println(mkDate("20181207104919","20181207114900"));
		System.out.println(subDate("20181207104900", "20181207104901"));
	}

}
