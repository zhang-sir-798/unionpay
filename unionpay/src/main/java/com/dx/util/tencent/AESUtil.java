package com.dx.util.tencent;


import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * Created by gds on 2018/7/26.
 */
@SuppressWarnings({"restriction","unused"})
public class AESUtil {

    
	
	public static String getRefundDecrypt(String reqInfoSecret, String key) {
        String result = "";
        try {
            Security.addProvider(new BouncyCastleProvider());
            sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            byte[] bt = decoder.decodeBuffer(reqInfoSecret);
            String b = new String(bt);
            
            String md5key = DigestUtils.md5Hex(key).toLowerCase();
           // String md5key = MD5.getMD5(key).toLowerCase();
            SecretKey secretKey = new SecretKeySpec(md5key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] resultbt = cipher.doFinal(bt);
            result = new String(resultbt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        String A = "11";
        String B = "11";
        System.out.println(B);
    }
}
