/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 */
package com.dx.util.alibaba;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @ClassName SDKConfig
 * @Description 配置文件asdk.properties配置信息类
 *              声明：以下代码只是为了方便接入方测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障
 */
public class SDKConfig {
	public static final String FILE_NAME = "alipay.asdk.properties";

	/**
	 * 定义常量
	 */
	/** URL前缀常量 */
	public static final String TRANS_URL_PREFIX = "asdk.frontTransUrl";
	/** 商户入驻 */
	public static final String INDIRECTCREATE_URL_SUFFIX = "asdk.indirectCreate";
	/** 商户信息查询 */
	public static final String INDIRECTQUERY_URL_SUFFIX = "asdk.indirectQuery";
	/** 商户信息修改 */
	public static final String INDIRECMODIFY_URL_SUFFIX = "asdk.indirectModify";
	/** 扫码支付 */
	public static final String SCANCODEPAY_URL_SUFFIX = "asdk.tradePrecreate";
	/** 条码支付 */
	public static final String TRADEPAY_URL_SUFFIX = "asdk.tradePay";
	/** 统一下单 */
	public static final String TRADECREATE_URL_SUFFIX = "asdk.tradeCreate";
	/** 支付管理---统一收单交易撤销接口 */
	public static final String TRADECANCEL_URL_SUFFIX = "asdk.tradeCancel";
	/** 支付管理---统一收单交易退款查询 */
	public static final String TRADEREFUNDQUERY_URL_SUFFIX = "asdk.tradeRefundQuery";
	/** 支付管理----统一收单交易退款接口 */
	public static final String TRADEREFUND_URL_SUFFIX = "asdk.tradeRefund";
	/** 支付管理----统一收单线下交易查询 */
	public static final String TRADEQUERY_URL_SUFFIX = "asdk.tradeQuery";

	/** 私钥字符串 */
	public static final String PRI_KEY = "asdk.privateKey";
	/** 公钥字符串 */
	public static final String PUB_KEY = "asdk.publicKey";
	/** 支付宝分配给开发者的应用ID */
	public static final String APP_ID = "asdk.appId";
	/** 通知地址 */
	public static final String NOTIFY_URL = "asdk.notifyUrl";
	

	/**
	 * 定义属性
	 */
	private static String transUrlPrefix;
	private static String indirectCreate;
	private static String indirectQuery;
	private static String indirectModify;
	private static String tradePrecreate;
	private static String tradePay;
	private static String tradeCreate;
	private static String tradeCancel;
	private static String traderefundQuery;
	private static String tradeRefund;
	private static String tradeQuery;
	
	
	private static String privateKey;
	private static String publicKey;
	private static String appId;
	private static String notify;
	
	public String getNotify() {
		return notify;
	}
	
	public String getIndirectCreate() {
		return indirectCreate;
	}
	
	public String getIndirectQuery() {
		return indirectQuery;
	}

	public String getIndirectModify() {
		return indirectModify;
	}

	public String getAppId() {
		return appId;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public String getTransUrlPrefix() {
		return transUrlPrefix;
	}
	
	public  String getTradePrecreate() {
		return tradePrecreate;
	}

	public String getTradePay() {
		return tradePay;
	}

	public String getTradeCreate() {
		return tradeCreate;
	}
	
	public String getTradeCancel() {
		return tradeCancel;
	}

	public String getTraderefundQuery() {
		return traderefundQuery;
	}

	public String getTradeRefund() {
		return tradeRefund;
	}

	public String getTradeQuery() {
		return tradeQuery;
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
			AliPayUtil.getLogger().info("从classpath: " + SDKConfig.class.getClassLoader().getResource("").getPath()
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
				AliPayUtil.getLogger().error(FILE_NAME + "属性文件未能在classpath指定的目录下 "
						+ SDKConfig.class.getClassLoader().getResource("").getPath() + " 找到!");
				return;
			}
			loadProperties(properties);
		} catch (IOException e) {
			AliPayUtil.getLogger().error(e.getMessage(), e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					AliPayUtil.getLogger().error(e.getMessage(), e);
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
		AliPayUtil.getLogger().info("开始从属性文件中加载配置项");
		String value = null;

		value = pro.getProperty(TRANS_URL_PREFIX);
		if (StringUtils.isNotBlank(value)) {
			transUrlPrefix = value.trim();
		}
		AliPayUtil.getLogger().info("配置项：接口地址公用前缀==>" + TRANS_URL_PREFIX + "==>" + value + " 已加载");

		value = pro.getProperty(INDIRECTCREATE_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			indirectCreate = value.trim();
		}
		AliPayUtil.getLogger().info("配置项：商户入驻接口地址==>" + INDIRECTCREATE_URL_SUFFIX + "==>" + value + " 已加载");
		
		value = pro.getProperty(INDIRECTQUERY_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			indirectQuery = value.trim();
		}
		AliPayUtil.getLogger().info("配置项：商户信息查询接口地址==>" + INDIRECTQUERY_URL_SUFFIX + "==>" + value + " 已加载");
		
		value = pro.getProperty(INDIRECMODIFY_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			indirectModify = value.trim();
		}
		AliPayUtil.getLogger().info("配置项：商户信息修改接口地址==>" + INDIRECMODIFY_URL_SUFFIX + "==>" + value + " 已加载");
		
		value = pro.getProperty(SCANCODEPAY_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			tradePrecreate = value.trim();
		}
		AliPayUtil.getLogger().info("配置项：扫码支付接口地址==>" + SCANCODEPAY_URL_SUFFIX + "==>" + value + " 已加载");

		value = pro.getProperty(TRADEPAY_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			tradePay = value.trim();
		}
		AliPayUtil.getLogger().info("配置项：条码支付接口地址==>" + TRADEPAY_URL_SUFFIX + "==>" + value + " 已加载");
		
		value = pro.getProperty(TRADECREATE_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			tradeCreate = value.trim();
		}
		AliPayUtil.getLogger().info("配置项：统一下单接口地址==>" + TRADECREATE_URL_SUFFIX + "==>" + value + " 已加载");
		
		value = pro.getProperty(TRADECANCEL_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			tradeCancel = value.trim();
		}
		AliPayUtil.getLogger().info("配置项：统一收单交易撤销接口地址==>" + TRADECANCEL_URL_SUFFIX + "==>" + value + " 已加载");
		
		value = pro.getProperty(TRADEREFUNDQUERY_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			traderefundQuery = value.trim();
		}
		AliPayUtil.getLogger().info("配置项：支付管理---统一收单交易退款查询接口地址==>" + TRADEREFUNDQUERY_URL_SUFFIX + "==>" + value + " 已加载");
		
		value = pro.getProperty(TRADEREFUND_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			tradeRefund = value.trim();
		}
		AliPayUtil.getLogger().info("配置项：支付管理----统一收单交易退款接口地址==>" + TRADEREFUND_URL_SUFFIX + "==>" + value + " 已加载");
		
		value = pro.getProperty(TRADEQUERY_URL_SUFFIX);
		if (StringUtils.isNotBlank(value)) {
			tradeQuery = value.trim();
		}
		AliPayUtil.getLogger().info("配置项：支付管理----统一收单线下交易查询接口地址==>" + TRADEQUERY_URL_SUFFIX + "==>" + value + " 已加载");
		
		value = pro.getProperty(PRI_KEY);
		if (StringUtils.isNotBlank(value)) {
			privateKey = value.trim();
		}
		AliPayUtil.getLogger().info("配置项：签名私钥字符串==>" + PRI_KEY + "==>" + value + " 已加载");

		value = pro.getProperty(PUB_KEY);
		if (StringUtils.isNotBlank(value)) {
			publicKey = value.trim();
		}
		AliPayUtil.getLogger().info("配置项：签名公钥字符串==>" + PUB_KEY + "==>" + value + " 已加载");

		value = pro.getProperty(APP_ID);
		if (StringUtils.isNotBlank(value)) {
			appId = value.trim();
		}
		AliPayUtil.getLogger().info("配置项：支付宝分配给开发者的应用ID==>" + APP_ID + "==>" + value + " 已加载");
		
		value = pro.getProperty(NOTIFY_URL);
		if (StringUtils.isNotBlank(value)) {
			notify = value.trim();
		}
		AliPayUtil.getLogger().info("配置项：通知地址==>" + NOTIFY_URL + "==>" + value + " 已加载");
	}

}
