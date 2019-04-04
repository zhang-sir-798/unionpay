package com.dx.util.tencent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 微信支付工具，如数据类型转换，加密等
 */
public class WXPayUtil {

	/**
	 * XML格式字符串转换为Map
	 */
	public static Map<String, String> xmlToMap(String strXML) throws Exception {
		try {
			Map<String, String> data = new HashMap<String, String>();
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			//禁用外部实体引用
			documentBuilderFactory.setExpandEntityReferences(false);
			documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
			
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
			org.w3c.dom.Document doc = documentBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getDocumentElement().getChildNodes();
			for (int idx = 0; idx < nodeList.getLength(); ++idx) {
				Node node = nodeList.item(idx);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					org.w3c.dom.Element element = (org.w3c.dom.Element) node;
					data.put(element.getNodeName(), element.getTextContent());
				}
			}
			try {
				stream.close();
			} catch (Exception ex) {
				// do nothing
			}
			return data;
		} catch (Exception ex) {
			WXPayUtil.getLogger().warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}",
					ex.getMessage(), strXML);
			throw ex;
		}

	}

	/**
	 * 将Map转换为XML格式的字符串
	 */
	public static String mapToXml(Map<String, String> data) throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		org.w3c.dom.Document document = documentBuilder.newDocument();
		org.w3c.dom.Element root = document.createElement("xml");
		document.appendChild(root);
		for (String key : data.keySet()) {
			String value = data.get(key);
			if (value == null) {
				value = "";
			}
			value = value.trim();
			org.w3c.dom.Element filed = document.createElement(key);
			filed.appendChild(document.createTextNode(value));
			root.appendChild(filed);
		}
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		DOMSource source = new DOMSource(document);
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		transformer.transform(source, result);
		String output = writer.getBuffer().toString(); // .replaceAll("\n|\r",
														// "");
		try {
			writer.close();
		} catch (Exception ex) {
		}
		return output;
	}

	/**
	 * 日志
	 * 
	 * @return
	 */
	public static Logger getLogger() {
		Logger logger = LoggerFactory.getLogger("wxpay java sdk");
		return logger;
	}


	/**
	 * 生成 uuid， 即用来标识一笔单，也用做 nonce_str
	 * 
	 * @return
	 */
	public static String generateUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
	}

	/**
	 * 生成带有 sign 的 XML 格式字符串
	 *
	 * @param data
	 *            Map类型数据
	 * @param key
	 *            API密钥
	 * @param signType
	 *            签名类型
	 * @return 含有sign字段的XML
	 */
	public static String generateSignedXml(final Map<String, String> data, String key)
			throws Exception {
		String sign = generateSignature(data, key);
		data.put(WXPayConstants.FIELD_SIGN, sign);
		return mapToXml(data);
	}

	public static String generateSignature(Map<String, String> data, String key) throws Exception {
		String stringData = getSignContent(data, "sign");
		return rsa256Sign(stringData,key);
	}
	
	public static boolean rsa256CheckContent(String content, String sign, String key)
			throws Exception {
		try {
			WXPayUtil.getLogger().info("公钥字符串： "+key);
			PublicKey pubKey = restorePublicKey(Base64.decodeBase64(key));

			Signature signature = Signature.getInstance(WXPayConstants.SHA256withRSA);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(WXPayConstants.CHARSET_UTF_8));
			boolean result = signature.verify(Base64.decodeBase64(sign.getBytes()));
			WXPayUtil.getLogger().info("验签结果： "+result);
			return result;
		} catch (Exception e) {
			throw new Exception("RSAcontent = " + content + ",sign=" + sign + ",key = " + key, e);
		}
	}
	
	public static String getSignContent(Map<String, String> sortedParams, String... values) {
		for (String value : values) {
			sortedParams.remove(value);
		}
		StringBuffer content = new StringBuffer();
		List<String> keys = new ArrayList<String>(sortedParams.keySet());
		Collections.sort(keys);
		int index = 0;
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = sortedParams.get(key);
			if (StringUtils.isNotBlank(key)
					&& StringUtils.isNotBlank(value)){
				content.append((index == 0 ? "" : "&") + key + "=" + value);
				++index;
			}
		}
		String stringData = content.toString();
		WXPayUtil.getLogger().info("待签名或验签内容 : "+stringData);
		return stringData;
	}
	
	protected static String rsa256Sign(String content, String key) throws Exception {
		try {
			WXPayUtil.getLogger().info("私钥字符串： "+key);
			PrivateKey priKey = restorePrivateKey(Base64.decodeBase64(key));
			Signature signature = Signature.getInstance(WXPayConstants.SHA256withRSA);
			signature.initSign(priKey);
			signature.update(content.getBytes(WXPayConstants.CHARSET_UTF_8));
			byte[] signed = signature.sign();
			String sign = new String(base64Encode(signed), WXPayConstants.CHARSET_UTF_8);
			WXPayUtil.getLogger().info("生成签名sign : "+sign);
			return sign;
		} catch (Exception e) {
			throw new RuntimeException("RSAcontent = " + content + "; charset = " + WXPayConstants.CHARSET_UTF_8, e);
		}
	}

	/**
	 * 将Map中的数据转换成key1=value1&key2=value2的形式 不包含签名域signature
	 * 
	 * @param data
	 *            待拼接的Map数据
	 * @return 拼接好后的字符串
	 */
	public static String coverMap2String(Map<String, String> data) {
		TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			if (WXPayConstants.FIELD_SIGN.equals(en.getKey().trim())) {
				continue;
			}
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			sf.append(en.getKey() + WXPayConstants.EQUAL + en.getValue() + WXPayConstants.AMPERSAND);
		}
		return sf.substring(0, sf.length() - 1);
	}

	/**
	 * 将形如key=value&key=value的字符串转换为相应的Map对象
	 * 
	 * @param result
	 * @return
	 */
	public static Map<String, String> convertString2Map(String result) {
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
			KeyFactory factory = KeyFactory.getInstance(WXPayConstants.KEY_ALGORITHM);
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
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
				keyBytes);
		try {
			KeyFactory factory = KeyFactory.getInstance(WXPayConstants.KEY_ALGORITHM);
			PrivateKey privateKey = factory
					.generatePrivate(pkcs8EncodedKeySpec);
			return privateKey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
