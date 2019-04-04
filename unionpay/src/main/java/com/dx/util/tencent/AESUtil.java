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
        String A = "cbRfjwOKrLQCOjAU0v1Ac6cDJQhK6349ngVplZHLe010obV59BJ75z05eUzG+Um3Zk9WxNO6jtk60bBDkMLmf4dZk41wSbyx/GiNNcRp3g0GdUMjGjhlyJgMAKiLbmVwcPEQnOoikcCbwGd0VmOdWcTk67kDlE4ssYW6pgBXp5bSBVotuar2Wxi0z20HGgsz7dcIVELP9+JfGwuiVV9xkhO8sPbEO4SIO2qhkRQ0QzbDYgn9gU1Iprzv6wGxFh+Bm/lJWuiBGwhFCT2fq1xEF1nDPEyW3LOWq4daCegXpvTXoXLZp9Xp6zcdxiDCsLgj8yj+q7ZNmVNt1vTUR94ZQAI1UAtCqK+1dI89DZdbifU7o0fVm/9WQqjcOIo4WemUJU7WfCsyHXBVvx8lZezoQc5ZGZOqTEZJCVKV6OF5iqvlNnZJ1byPg7BfHZnmbIjdETFMIOkq3oPicshnNlVZ9g08DvesDZzG/KJzI8NBMFfXNMjSJyuemsZ/0jFJRtKDOWoCQetWox+mORa5BPrMwvibTLkPZL0okvnyrJgFwGtE4BFTdnN/+cfuUISXMTbeZv6UjRJwD9y8B+wi4wTSUC1QXjlZEkV+RgBNnJD/n13NWUK1nlrwev/RHLsgjoMSYnF0mYec8g1BCyW0POcp4iEDaRVMrjQnACyyeMncSxA+KawdoFEOqMuAITv3B0Q9iZOTpI7yqnpquWiphXMuKovJgLp4vnPqerMMthTeaQ/rfVVO/U6Z4K/heTZKT2Y0y7kQP2GS+r/N5Qvu+J0sihJ8opoZ0AJ7ktwGrzcmkIq/DCCtAfgdAT8x3rtSXA4f8pb7WqFssdGRqgzMZR1jlcz3LD5+Du+BWK5QmyCeynywd9b6s7oYdyU0aI75LrT5EBOtEHAMGi2rmwb1X2wlEd3g4gCr2l1EEA4dUTdhsqynG0w64VHWJkvJgTOdmzMPWYksLu463qcy6dN44WuNBG0aw7uEQQIUXeRhDEwCBQVmzXNLzwQ6wHv6O9j7yvkvZll3SuGw7bbsfdSdAfQkJkLCj/MHuEKUauiV0mQS/2M=";
        String key = "02a86cb980b1237eaa77fb86adf64657";
        String B = getRefundDecrypt(A, key);
        System.out.println(B);
    }
}
