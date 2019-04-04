/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 */
package com.dx.util.tencent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName SDKConfig
 * @Description 配置文件tsdk.properties配置信息类
 *              声明：以下代码只是为了方便接入方测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障
 */
public class SDKConfig {
	public static final String FILE_NAME = "tencent.tsdk.properties";

	/**
	 * 定义常量
	 */
	/** URL前缀常量 */
	public static final String TRANS_URL_PREFIX = "tsdk.frontTransUrl";
	/** 订单查询接口地址 */
	public static final String ORDERQUERY_URL_SUFFIX = "tsdk.orderqueryUrlSuffix";
	/** 刷卡支付接口地址 */
	public static final String MICROPAY_URL_SUFFIX = "tsdk.micropayUrlSuffix";
	/** 预下单接口地址 */
	public static final String PREPAY_URL_SUFFIX = "tsdk.prepayUrlSuffix";
	/** 撤销订单接口地址 */
	public static final String ORDERREVERSE_URL_SUFFIX = "tsdk.orderreverseUrlSuffix";
	/** 关闭订单接口地址 */
	public static final String ORDERCLOSE_URL_SUFFIX = "tsdk.ordercloseUrlSuffix";
	/** 查询单笔退款接口地址 */
	public static final String QRYSINGLE_URL_SUFFIX = "tsdk.qrysingleUrlSuffix";
	/** 查询多笔退款接口地址 */
	public static final String QRYMULTIPLE_URL_SUFFIX = "tsdk.qrymultipleUrlSuffix";
	/** 退款接口地址 */
	public static final String REFUND_URL_SUFFIX = "tsdk.refundUrlSuffix";
	/** 下属商户录入接口地址 */
	public static final String SUBMCHADD_URL_SUFFIX = "tsdk.submchaddUrlSuffix";
	/** 下属商户查询接口地址 */
	public static final String SUBMCHQRY_URL_SUFFIX = "tsdk.submchqryUrlSuffix";

	/** 私钥字符串 */
	public static final String PRI_KEY = "tsdk.privateKey";
	/** 公钥字符串 */
	public static final String PUB_KEY = "tsdk.publicKey";
	/** 机构号 */
	public static final String MCH_ID = "tsdk.mchId";
	/** 子商户号 */
	public static final String SUB_MCH_ID = "tsdk.subMchId";
	/** 渠道商商户号 */
	public static final String CHANNEL_ID = "tsdk.channelId";
	/** 公众账号ID */
	public static final String APP_ID = "tsdk.appId";
	/** 子商户公众账号ID */
	public static final String SUB_APPID = "tsdk.subAppid";

	/**
	 * 定义属性
	 */
	private static String transUrlPrefix;
	private static String orderqueryUrlSuffix;
	private static String micropayUrlSuffix;
	private static String prepayUrlSuffix;
	private static String orderreverseUrlSuffix;
	private static String ordercloseUrlSuffix;
	private static String qrysingleUrlSuffix;
	private static String qrymultipleUrlSuffix;
	private static String refundUrlSuffix;
	private static String submchaddUrlSuffix;
	private static String submchqryUrlSuffix;

	private static String privateKey;
	private static String publicKey;
	
	private static String mchId;
	private static String subMchId;
	private static String channelId;
	private static String appId;
	private static String subAppid;

	public String getMchId() {
		return mchId;
	}

	public String getSubMchId() {
		return subMchId;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getAppId() {
		return appId;
	}

	public String getSubAppid() {
		return subAppid;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public String getPrepayUrlSuffix() {
		return prepayUrlSuffix;
	}

	public String getOrderreverseUrlSuffix() {
		return orderreverseUrlSuffix;
	}

	public String getOrdercloseUrlSuffix() {
		return ordercloseUrlSuffix;
	}

	public String getQrysingleUrlSuffix() {
		return qrysingleUrlSuffix;
	}

	public String getQrymultipleUrlSuffix() {
		return qrymultipleUrlSuffix;
	}

	public String getRefundUrlSuffix() {
		return refundUrlSuffix;
	}

	public String getSubmchaddUrlSuffix() {
		return submchaddUrlSuffix;
	}

	public String getSubmchqryUrlSuffix() {
		return submchqryUrlSuffix;
	}

	public String getMicropayUrlSuffix() {
		return micropayUrlSuffix;
	}

	public String getOrderqueryUrlSuffix() {
		return orderqueryUrlSuffix;
	}

	public String getTransUrlPrefix() {
		return transUrlPrefix;
	}

	/** 操作对象. */
	private static SDKConfig config = new SDKConfig();
	/** 属性文件对象. */
	private static Properties properties;

	private SDKConfig() {
		super();
	}

	/**
	 * 获取config对象.
	 * 
	 * @return
	 */
	public static SDKConfig getConfig() {
		if (properties == null) {
			synchronized (SDKConfig.class) {
				if (properties == null) {
					loadPropertiesFromSrc();
				}
			}
		}
		return config;
	}

	/**
	 * 从classpath路径下加载配置参数
	 */
	public static void loadPropertiesFromSrc() {
		InputStream in = null;
		try {
			WXPayUtil.getLogger().info("从classpath: " + SDKConfig.class.getClassLoader().getResource("").getPath()
					+ " 获取属性文件" + FILE_NAME);
			in = SDKConfig.class.getClassLoader().getResourceAsStream(FILE_NAME);
			if (null != in) {
				properties = new Properties();
				try {
					properties.load(in);
				} catch (IOException e) {
					throw e;
				}
			} else {
				WXPayUtil.getLogger().error(FILE_NAME + "属性文件未能在classpath指定的目录下 "
						+ SDKConfig.class.getClassLoader().getResource("").getPath() + " 找到!");
				return;
			}
			loadProperties(properties);
		} catch (IOException e) {
			WXPayUtil.getLogger().error(e.getMessage(), e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					WXPayUtil.getLogger().error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 根据传入的 {@link #load(java.util.Properties)}对象设置配置参数
	 * 
	 * @param pro
	 */
	public static void loadProperties(Properties pro) {
		WXPayUtil.getLogger().info("开始从属性文件中加载配置项");
		String value = null;

		value = pro.getProperty(TRANS_URL_PREFIX);
		if (StringUtils.isNotBlank(value)) {
			transUrlPrefix = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：接口地址公用前缀==>" + TRANS_URL_PREFIX + "==>" + value + " 已加载");

		value = pro.getProperty(ORDERQUERY_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			orderqueryUrlSuffix = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：查询订单接口地址==>" + ORDERQUERY_URL_SUFFIX + "==>" + value + " 已加载");

		value = pro.getProperty(MICROPAY_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			micropayUrlSuffix = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：刷卡支付接口地址==>" + MICROPAY_URL_SUFFIX + "==>" + value + " 已加载");

		value = pro.getProperty(PREPAY_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			prepayUrlSuffix = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：预下单接口地址==>" + PREPAY_URL_SUFFIX + "==>" + value + " 已加载");

		value = pro.getProperty(ORDERREVERSE_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			orderreverseUrlSuffix = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：撤销订单接口地址==>" + ORDERREVERSE_URL_SUFFIX + "==>" + value + " 已加载");

		value = pro.getProperty(ORDERCLOSE_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			ordercloseUrlSuffix = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：关闭订单接口地址==>" + ORDERCLOSE_URL_SUFFIX + "==>" + value + " 已加载");

		value = pro.getProperty(QRYSINGLE_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			qrysingleUrlSuffix = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：查询单笔退款接口地址==>" + QRYSINGLE_URL_SUFFIX + "==>" + value + " 已加载");

		value = pro.getProperty(QRYMULTIPLE_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			qrymultipleUrlSuffix = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：查询多笔退款接口地址==>" + QRYMULTIPLE_URL_SUFFIX + "==>" + value + " 已加载");

		value = pro.getProperty(REFUND_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			refundUrlSuffix = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：退款接口地址==>" + REFUND_URL_SUFFIX + "==>" + value + " 已加载");

		value = pro.getProperty(SUBMCHADD_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			submchaddUrlSuffix = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：下属商户录入接口地址==>" + SUBMCHADD_URL_SUFFIX + "==>" + value + " 已加载");

		value = pro.getProperty(SUBMCHQRY_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			submchqryUrlSuffix = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：下属商户查询接口地址==>" + SUBMCHQRY_URL_SUFFIX + "==>" + value + " 已加载");

		value = pro.getProperty(PRI_KEY);
		if (StringUtils.isNotBlank(value)) {
			privateKey = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：签名私钥字符串==>" + PRI_KEY + "==>" + value + " 已加载");

		value = pro.getProperty(PUB_KEY);
		if (StringUtils.isNotBlank(value)) {
			publicKey = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：签名公钥字符串==>" + PUB_KEY + "==>" + value + " 已加载");

		value = pro.getProperty(MCH_ID);
		if (StringUtils.isNotBlank(value)) {
			mchId = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：机构号==>" + MCH_ID + "==>" + value + " 已加载");
		
		value = pro.getProperty(SUB_MCH_ID);
		if (StringUtils.isNotBlank(value)) {
			subMchId = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：子商户号==>" + SUB_MCH_ID + "==>" + value + " 已加载");

		value = pro.getProperty(CHANNEL_ID);
		if (StringUtils.isNotBlank(value)) {
			channelId = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：渠道商商户号==>" + CHANNEL_ID + "==>" + value + " 已加载");

		value = pro.getProperty(APP_ID);
		if (StringUtils.isNotBlank(value)) {
			appId = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：公众账号ID==>" + APP_ID + "==>" + value + " 已加载");
		
		value = pro.getProperty(SUB_APPID);
		if (StringUtils.isNotBlank(value)) {
			subAppid = value.trim();
		}
		WXPayUtil.getLogger().info("配置项：子商户公众账号ID==>" + SUB_APPID + "==>" + value + " 已加载");


	}

}
