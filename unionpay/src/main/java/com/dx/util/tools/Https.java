package com.dx.util.tools;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dx.util.tencent.BaseHttpSSLSocketFactory;
/**
 * 
 * @ClassName Https
 * @Description 通讯工具类
 * @author 姑娘、请留步
 * @Date 2018年12月12日 下午6:48:27
 * @version 1.0.0
 * 
 */
public class Https {

	private static final Log _log = LogFactory.getLog(Http.class);

	/**
	 * 发送POST方法的请求
	 * 
	 * @param url 发送请求的 URL
	 * @param param 请求参数,自定义。
	 * @return 所代表远程资源的响应结果
	 */

	public static String post(String uri , String params) throws Exception {
		System.setProperty("javax.net.ssl.keyStore", "C:/certs/server.keystore");
		System.setProperty("javax.net.ssl.keyStorePassword", "111111");
		System.setProperty("javax.net.ssl.keyStoreType", "JKS");
		
		System.setProperty("javax.net.ssl.trustStore", "C:/certs/server.keystore");
		System.setProperty("javax.net.ssl.trustStorePassword", "111111");
		
		HttpsURLConnection.setDefaultHostnameVerifier(new TrustAnyVerifier());
		
		HttpsURLConnection httpURLConnection = null;
		URL url;
		try {
			// 发送请求
			url = new URL(uri);
			httpURLConnection = (HttpsURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(50000);
			httpURLConnection.setReadTimeout(50000);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
			
			PrintStream out = null;
			try {
				httpURLConnection.connect();
				out = new PrintStream(httpURLConnection.getOutputStream(), false, "UTF-8");
				out.print(params);
				out.flush();
			} catch (Exception e) {
				throw e;
			} finally {
				if (out != null) {
					out.close();
				}
			}
			// 接收响应
			InputStream in = null;
			StringBuilder sb = new StringBuilder(1024);
			BufferedReader br = null;
			String temp = null;
			try {
				if (200 == httpURLConnection.getResponseCode()) {
					in = httpURLConnection.getInputStream();
					br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
					while ((temp = br.readLine()) != null) {
						sb.append(temp);
					}
				} else {
					in = httpURLConnection.getErrorStream();
					br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
					while ((temp = br.readLine()) != null) {
						sb.append(temp);
					}
				}

				return sb.toString();
			} catch (Exception e) {
				_log.info(e);
				throw e;
			} finally {
				if (br != null) {
					br.close();
				}
				if (in != null) {
					in.close();
				}
				if (httpURLConnection != null) {
					httpURLConnection.disconnect();
				}
			}
		} catch (Exception e1) {
			throw e1;
		}
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(post("https://123.206.54.253:8443/auth-web/trans/getDynKey" ,"test"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
