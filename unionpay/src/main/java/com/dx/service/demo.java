package com.dx.service;

import com.dx.model.Merchant;
import com.unionpay.utils.*;

import net.sf.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class demo {

	public static String url = "https://smbp.95516.com/xwins/gateway/batchGenerateQrcode";
	protected static final String aesKey = "U6tIyxMKfcLcg+trazjphA==";
	protected static final String rsaKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKHDyGodZlZdeIiSHBFpkXuDX0xqPao+TrlcIKKQIEJfXN8nlKkHZbHlsB39ZpGMRqwfutrYH1vXpk6beU2y6ceWaIGCBlrU+kUQqWQywXzBU+xdtzEsTvMYw7CNslWtW/UyAKC2wCF5B16lUxfGVhMlKMyi5ltK2UfoxAyeBtUxAgMBAAECgYA3PZB3DEsiOxEqMhr7pz1THG2yvqUIOfbQPbG3UdhyUfZ5Dh8qCaiks3cNBre8BFct+Dfq+9rZli7osRcffXVvcmWTVoN3VaLtPq7QJ621yc+N1pyUUMx9eyExdVdqK6WbaDmxvMjpBP8FtzuMD9a8AALgI3SGwZ8F0k5dyr7rQQJBAOul9YdaAi3C6eY4/ik9H+tx/k/PXYRCNKbQlfCbBw2ybGB+RaMfUpbMAkHBS1PvWpqPMRSM1CgEVe3w2EBWdK0CQQCvvE3jcXvq9CeU0a/y8DyJr1Qnrw6Sw7FRV2R6rFZSP0GOgtYs5oSxXWreFlv83r0KLZ+xb7GeNowdF+16ga8VAkB2FMtb77Z3x9v9CUIS2BZOnOiahYaO574KeSspTgXSzz2PfLbYbrGr7r6SxJFClgYIvQgTDB6jjsigWreCRdBhAkALcytn/Ebkog/KJHdgTsLC/cdv5nn+mf19nUIQtBXTugddb6zgtAxFOh7yZWaM7T5H4X0cJHjgJBGw+5QffT+ZAkApRQccz6N/0BNR7tYPzM6cnJHviF1O1uJGSBafTpDA97LeDEo3BQNpqt4mmmRNVO3yxZBB6jB5CQaL6Japv2WH";

	// protected static String rsaKey = ApiUtil.getPublicKeyFromPem();

	public static void main(String[] args) throws Exception {

		/*
		 * int index = 1; System.out.println((index == 0 ? "" : "&"));
		 * 
		 * 
		 * Map<String, Object> paramMap = new HashMap<String, Object>(); //批量请求二维码
		 * testA(paramMap);
		 * 
		 * String type = CommonUtil.checkUrl(paramMap, url);
		 * System.out.println("urlType:" + type);
		 * 
		 * Map<String, String> requestMap = CommonUtil.dealWithParam(paramMap, rsaKey,
		 * aesKey); System.out.println("requestMap :" + requestMap); String result =
		 * HttpClient.post(url, requestMap, "UTF-8"); System.out.println("result :" +
		 * result);
		 */

	}

	public static Map<String, String> doBatchQrcode(Map<String, String> bizMap) throws Exception {
		System.out.println("测试方法 init...");
		// 公共参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		normalParam(paramMap);
		// 接口特定参数
		// batchQrcode(bizMap);
		paramMap.put("bizContent", bizMap);

		String type = CommonUtil.checkUrl(paramMap, url);
		System.out.println("urlType:" + type);

		Map<String, String> requestMap = CommonUtil.dealWithParam(paramMap, rsaKey, aesKey);
		System.out.println("request messahe :" + requestMap);
		String result = HttpClient.post(url, requestMap, "UTF-8");
		System.out.println("response message :" + result);
		return JSONToMap(result);

	}

	public static Map<String, String> doBbatchQrcodeQuery(Map<String, String> bizMap) throws Exception {
		System.out.println("测试方法 init...");
		// 公共参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		normalParam(paramMap);
		// 接口特定参数
		// batchQrcode(bizMap);
		paramMap.put("bizContent", bizMap);

		String type = CommonUtil.checkUrl(paramMap, url);
		System.out.println("urlType:" + type);

		Map<String, String> requestMap = CommonUtil.dealWithParam(paramMap, rsaKey, aesKey);
		System.out.println("request messahe :" + requestMap);
		String result = HttpClient.post(url, requestMap, "UTF-8");
		System.out.println("response message :" + result);
		return JSONToMap(result);

	}

	public static Map<String, String> doBindQrcode(Merchant mer) throws Exception {
		System.out.println("测试方法 init...");
		// 公共参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		normalParam(paramMap);
		// 接口特定参数
		paramMap.put("bizContent", bindQrcode(mer));

		String type = CommonUtil.checkUrl(paramMap, url);
		System.out.println("urlType:" + type);

		Map<String, String> requestMap = CommonUtil.dealWithParam(paramMap, rsaKey, aesKey);
		System.out.println("request messahe :" + requestMap);
		String result = HttpClient.post(url, requestMap, "UTF-8");
		System.out.println("response message :" + result);
		return JSONToMap(result);

	}

	// 绑定
	protected static Map<String, String> bindQrcode(Merchant mer) {

		Map<String, String> encryptedInfo = new HashMap<String, String>();
		encryptedInfo.put("accNo", mer.getSettle_no());
		encryptedInfo.put("phoneNo", mer.getSettle_mobile());
		Map<String, String> bizMap = new HashMap<String, String>();	
		bizMap.put("encryptedInfo", mapToJSON(encryptedInfo));
		
		bizMap.put("expandName", "shandetj");
		bizMap.put("merName", mer.getMer_name());
		bizMap.put("certifTp", "01");
		//需要修改为活参
		bizMap.put("qrCode", "https://qr.95516.com/00010002/01182341505319001023559100830252");
		
		bizMap.put("certifId", mer.getLegal_id());
		bizMap.put("customerNm", mer.getLegal_name());
		bizMap.put("merLng", "117.20");// 经度
		bizMap.put("merLat", "39.12");// 纬
		//需要修改为活参
		bizMap.put("mchntType", "0302");// 商户类型(新)
		
		bizMap.put("accType", "1");

		return bizMap;
	}

	// 批量申请
	protected static void batchQrcode(Map<String, String> bizMap) {
		Integer qrCodeNum = Integer.valueOf(10);
		bizMap.put("qrCodeNum", String.valueOf(qrCodeNum));
	}

	// 批量查询
	protected static void batchQrcodeQuery(Map<String, Object> bizMap) {
		bizMap.put("batchNo", "00000317");
	}

	// 二维码查询
	protected static void queryQrcode(Map<String, Object> bizMap) {
		bizMap.put("qrCode", "https://qr.95516.com/00010002/62422309557386655861967507435823");
	}

	// 4.2.17收款码名称修改
	protected static void modifyQrcode(Map<String, Object> bizMap) {
		bizMap.put("merId", "QRC330100000274");
		bizMap.put("qrNum", "483600006100000000");
		bizMap.put("qrCodeName", "粉色发设");
	}

	// 4.2.15一户多码申请
	protected static void multiQrcode(Map<String, Object> bizMap) {
		List<String> list = new ArrayList();

		bizMap.put("merId", "QRC331090000042");
		bizMap.put("applyNum", "0");
		bizMap.put("applyNameList", list.toString());
	}

	// 4.2.5图片上传
	protected static void image(Map<String, Object> bizMap) {
		File file = new File("C:\\21028119831105153x.jpg");
		bizMap.put("imgContent", file.toString());
		bizMap.put("merId", "QRC370200002775");
		bizMap.put("imgType", "02");
	}

	protected static void normalParam(Map<String, Object> paramMap) {
		paramMap.put("version", "1.0");
		paramMap.put("expandcode", "C0001591");
		paramMap.put("encoding", "UTF-8");
		paramMap.put("requestId", "11");
		paramMap.put("signMethod", "RSA2");
		paramMap.put("signature", "sss");
	}

	/**
	 * json字符串转map
	 * 
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, String> JSONToMap(String jsonStr) {
		Map<String, String> map = new HashMap<String, String>();
		// 转换为json对象
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		// 遍历json
		Set<String> set = jsonObject.keySet();
		for (String key : set) {
			map.put(key, jsonObject.getString(key));
		}
		return map;
	}

	public static String mapToJSON(Map<String, String> map) {
		JSONObject pack = new JSONObject();
		pack.accumulateAll(map);
		return pack.toString();
	}
}
