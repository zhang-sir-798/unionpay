package com.dx.util.tools;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;




public class Encrypt {
	private CodeUtil cu=new CodeUtil();
	private static final String KEY_MD5 = "MD5";
	private static final String AES_CBC_PCK_ALG = "AES/CBC/PKCS5Padding";
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static final char HEX_DIGITSUpper[] = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	/**
	 *验签sha-256
	 * 
	 **/
	public boolean checkSign(Map<String,String> map ,String trxMsgKey) throws UnsupportedEncodingException,Exception{
		String signature =map.get("signature");
		map.remove("signature");
		String signold=cu.byte2HexStr(cu.decodeB64(signature.getBytes("UTF-8"))).toLowerCase();
		
		String mab1=to_singStr(map);
		String mab2=mab1+"&secKey="+trxMsgKey;//LJY
		String sign=encrypt(mab2).toLowerCase();
        return sign.equals(signold);
	}
	
	public String to_singStr(Map<String,String> map) {
		 StringBuffer sb = new StringBuffer();
	        List<String> keys = new ArrayList<String>(map.keySet());
	        Collections.sort(keys);
	        for(String key:keys){
	        	sb.append(key+"="+map.get(key)+"&");
	        }
			String str=sb.toString();
			return str.substring(0,str.length()-1);
	}
	   /**
     * 对字符串加密,加密算法使用MD5,SHA-1,SHA-256,默认使用SHA-256
     * 
     * @param strSrc
     *            要加密的字符串
     * @param encName
     *            加密类型
     * @return
     */
    public  String encrypt(String str) {
        try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes("UTF-8"));
            return cu.byte2HexStr(md.digest());
        } catch (NoSuchAlgorithmException e) {
        	e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return null;
    }
    

	public static byte[] encryptToMD5(String data) {
		byte[] digestdata = null;
		try {
			// 得到MD5
			MessageDigest alga = MessageDigest.getInstance(KEY_MD5);
			alga.update(data.getBytes());
			digestdata = alga.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return digestdata;
	}

	/**
	 * 计算使用MD5加密的签名
	 * @param data
	 * @param token
	 * @return
	 */
	public static String MD5(String pwd) {
		StringBuffer signatureData = new StringBuffer(pwd);
		byte[] byteMD5 = encryptToMD5(signatureData.toString());
		return toHexString(byteMD5);

	}
	
	public static String MD5ToUpper(String pwd) {
		try {
			return DigestUtils.md5Hex(pwd.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			return "";
		}

	}

	

	public static String toHexString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}
	
	public static String toHexUpperString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITSUpper[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITSUpper[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	// 获得url的加密字符串
	public static String getKeyedDigest(String strSrc, String key) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(strSrc.getBytes("UTF8"));
			String result = "";
			byte[] temp;
			temp = md5.digest(key.getBytes("UTF8"));
			for (int i = 0; i < temp.length; i++) {
				result += Integer.toHexString(
						(0x000000ff & temp[i]) | 0xffffff00).substring(6);
			}
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//获取UUID
	public static String getUuid(){
		String uid="";
		UUID uuid=UUID.randomUUID();
		uid=uuid.toString().replace("-", "").trim();
		return uid;
	}
	
	/**
	 * RSA签名
	 * @param singnatureStr
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public  String rsaSign(String singnatureStr, PrivateKey privateKey) throws Exception {
		try {

			java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
			signature.initSign(privateKey);
			signature.update(singnatureStr.getBytes("UTF-8"));
			byte[] signed = signature.sign();
			return new String(Base64.encodeBase64(signed));
		} catch (Exception e) {
			throw new Exception("RSAcontent = " + singnatureStr + "; charset = ", e);
		}
	}
	  /**
     * rsa验签
     * 
     * @param content
     * @param aesKey
     * @param charset
     * @return
     * @throws Exception
     */
	 public  boolean rsaCheckContent(String content, String sign, PublicKey publicKey) throws Exception {
	        try {
            //PublicKey pubKey = getPublicKeyFromX509("RSA",new ByteArrayInputStream(publicKey.getBytes()));
            
	            java.security.Signature signature = java.security.Signature
	                .getInstance("SHA1WithRSA");

	            signature.initVerify(publicKey);

	                signature.update(content.getBytes("UTF-8"));
	            //验签暂时未开
	                // return signature.verify(Base64.decodeBase64(sign.getBytes()));
	            return true;
	        } catch (Exception e) {
	            throw e;//这里有个他们自定义的异常
	        }
	    }
	
	  /**
     * AES加密
     * 
     * @param content
     * @param aesKey
     * @param charset
     * @return
     * @throws Exception
     */
    public   String aesEncrypt(String content, String aesKey)throws Exception {

        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
            IvParameterSpec iv = new IvParameterSpec(new byte[16]);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(aesKey.getBytes()), "AES"), iv);

            byte[] encryptBytes = cipher.doFinal(content.getBytes("UTF-8"));
            return new String(Base64.encodeBase64(encryptBytes));
        } catch (Exception e) {
            throw e;
        }

    }
    /**
     * AES解密
     * 
     * @param content
     * @param key
     * @param charset
     * @return
     * @throws Exception
     */
    public String aesDecrypt(String content, String key)throws Exception {
        try {
        	  Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
              IvParameterSpec iv = new IvParameterSpec(new byte[16]);
              cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(key.getBytes()), "AES"), iv);

            byte[] cleanBytes = cipher.doFinal(Base64.decodeBase64(content.getBytes()));
            return new String(cleanBytes,  "UTF-8");
        } catch (Exception e) {
        	throw e;
        }
        
    }

}
