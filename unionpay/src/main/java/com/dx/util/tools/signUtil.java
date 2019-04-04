package com.dx.util.tools;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import sun.misc.BASE64Encoder;

/**
 * 
 * @ClassName signUtil
 * @Description 加密工具类
 * @author zhangsir
 * @Date 2018年11月3日 下午3:29:00
 * @version 1.0.0
 * 
 */


@SuppressWarnings({ "unchecked", "restriction" })
public class signUtil {
	
	private static final Log _log = LogFactory.getLog(signUtil.class);
	
	
	/**
	 * 加密算法：
	 * MD5算法 转bease64编码。
	 * @param params
	 * @return string
	 */
	public static String encrypt(String json, String macKey) {

		Map<String, String> contentData = JSON.parseObject(json, Map.class);

		String macStr = "";
		Object[] key_arr = contentData.keySet().toArray();
		Arrays.sort(key_arr);
		for (Object key : key_arr) {
			Object value = contentData.get(key);
			if (value != null) {
				if (!key.equals("sign")) {
					macStr += value.toString();
				}
			}
		}
		_log.info("加密原串：" + macStr);
		_log.info("加密原串Key：" + macKey);
		String rMac = "";
		try {
			rMac = sign_MD5(macStr, macKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rMac;
	}
	/**
	 * 验签：
	 * MD5算法 转bease64编码。
	 * @param params
	 * @return boolean
	 */
	public static boolean verify(JSONObject params, String key) {
		String sign = (String) params.get("sign"); // 签名
		String makeSign = "";
		params.remove("sign"); // 不参与签名
		try {

			Map<String, String> contentData = JSON.parseObject(params.toString(), Map.class);
			String macStr = "";
			Object[] key_arr = contentData.keySet().toArray();
			Arrays.sort(key_arr);
			for (Object keys : key_arr) {
				Object value = contentData.get(keys);
				if (value != null) {
					if (!keys.equals("sign")) {
						macStr += value.toString();
					}
				}
			}
			_log.info("加密原串：" + macStr);
			_log.info("秘钥Key：" + key);
			makeSign = sign_MD5(macStr, key);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!makeSign.equalsIgnoreCase(sign)) {
			return false;
		}
		return true;
	}

	public static String sign_MD5(String macStr, String mackey) throws Exception {
		String s = MD5Encode(macStr + mackey);

		BASE64Encoder base64en = new BASE64Encoder();
		String basestr = base64en.encode(s.getBytes("utf-8"));
		_log.info("BASE64结果" + basestr);
		return basestr;
	}

	public static String MD5Encode(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			md.update(sourceStr.getBytes("UTF-8"));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		_log.info("MD5结果" + result);
		return result;
	}
	/**
	 * 报件MD5：
	 * MD5算法 
	 * @param params
	 * @return boolean
	 */
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes(Charset.forName("UTF-8"));
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str).toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * MAC算法 (Message Authentication Codes) 带秘密密钥的Hash函数： 
	 * 消息的散列值由只有通信双方知道的秘密密钥K来控制,此时Hash值称作MAC。
	 * 
	 * @param params
	 * @return boolean
	 */
	public static String MAC(String macStr, String key) {
		String macByte = "";
		try {
			macByte = GenXorData(macStr.getBytes("GBK"), 0);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String macAsc = bcd2Str(macByte.getBytes());
		// 加密
		try {

			String initKey = "22222222222222222222222222222222";
			// 解析密钥明文
			String keyde = bcd2Str(decrypt3(key, initKey));

			byte[] leftByte = encrypt3(macAsc.substring(0, 16), keyde);
			byte[] macByteAll = new byte[16];
			System.arraycopy(leftByte, 0, macByteAll, 0, 8);
			System.arraycopy(string2Bytes(macAsc.substring(16, 32)), 0, macByteAll, 8, 8);

			String temp = GenXorData(macByteAll, 0);

			byte[] mac = encrypt3(temp, keyde);

			return bcd2Str(bcd2Str(mac).getBytes()).substring(0, 8);
		} catch (Exception e) {

			e.printStackTrace();
			return "";
		}
	}

	public static String GenXorData(byte[] bBuf, int iStart) {

		int i = 0;
		int nLen = 0;
		int nDataLen = 0;
		int nXorDataLen = 0;
		byte[] s1 = new byte[8];
		byte[] s2 = new byte[8];
		byte[] buf = bBuf;
		nDataLen = buf.length;
		nLen = 8 - (nDataLen % 8);
		nLen = (nLen == 8) ? 0 : nLen;
		nXorDataLen = (nDataLen + nLen); // 不足8的倍数，用0x00补齐。
		byte[] pBuf = new byte[nXorDataLen];

		System.arraycopy(buf, 0, pBuf, 0, nDataLen);
		System.arraycopy(pBuf, 0, s1, 0, 8);
		for (i = 8; i < nXorDataLen; i += 8) {
			System.arraycopy(pBuf, i, s2, 0, 8);
			s1 = setxor(s1, s2);
		}
		return bcd2Str(s1);
	}

	public static byte[] setxor(byte[] b1, byte[] b2) {

		byte[] snbyte = new byte[b1.length];
		for (int i = 0, j = 0; i < b1.length; i++, j++) {
			snbyte[i] = (byte) (b1[i] ^ b2[j]);
		}
		return snbyte;
	}

	public static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

	public static byte[] decrypt3(String data, String keyStr) throws Exception {
		byte[] keyByte = ASCII_To_BCD(keyStr.getBytes());
		byte input[] = ASCII_To_BCD(data.getBytes());
		byte keyBytes[] = new byte[24];
		System.arraycopy(keyByte, 0, keyBytes, 0, 16);
		System.arraycopy(keyByte, 0, keyBytes, 16, 8);
		SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede");
		Cipher cipher = Cipher.getInstance("DESede/ECB/NOPADDING");
		cipher.init(2, key);
		byte cipherText[] = new byte[cipher.getOutputSize(input.length)];
		int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
		ctLength += cipher.doFinal(cipherText, ctLength);
		return cipherText;
	}

	public static byte[] encrypt3(String data, String keyStr) throws Exception {
		byte[] keyByte = ASCII_To_BCD(keyStr.getBytes());
		byte input[] = ASCII_To_BCD(data.getBytes());
		byte keyBytes[] = new byte[24];
		System.arraycopy(keyByte, 0, keyBytes, 0, 16);
		System.arraycopy(keyByte, 0, keyBytes, 16, 8);
		SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede");
		Cipher cipher = Cipher.getInstance("DESede/ECB/NOPADDING");
		cipher.init(1, key);
		byte cipherText[] = new byte[cipher.getOutputSize(input.length)];
		int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
		ctLength += cipher.doFinal(cipherText, ctLength);
		return cipherText;
	}

	public static byte[] ASCII_To_BCD(byte[] ascii) {
		int asc_len = ascii.length;
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	private static byte asc_to_bcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}

	public static byte[] string2Bytes(String str) {
		byte[] b = new byte[str.length() / 2];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
		}
		return b;
	}
	
	public static void main(String[] args) {
		String sing = MAC("20181204170951A01F133G26H04J51", "65917A01F943CBB02D58791725569DA2");

		// 45383034 45383034
		System.out.println(sing);
	}

}
