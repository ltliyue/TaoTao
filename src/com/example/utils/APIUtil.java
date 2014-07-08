package com.example.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import cn.trinea.android.common.util.HttpUtils;

import com.example.data.ContextData;
import com.example.utils.Util;

public class APIUtil {

/**
 * https免签方式获得数据
 * 
 * @param params
 * @return
 */
public static String getShopInfo(Map<String, String> params) {
	if (Util.access_token != null) {
		params.put("access_token", Util.access_token);
		params.put("format", "json");
		params.put("v", "2.0");
		String result = HttpUtils.httpPostString(ContextData.GetInfos, params);
		return result;
	} else {
		return null;
	}
}

	/**
	 * 新的md5签名，首尾放secret。
	 * 
	 * @param secret
	 *            分配给您的APP_SECRET
	 */
	public static String md5Signature(TreeMap<String, String> params, String secret) {
		String result = null;
		StringBuffer orgin = getBeforeSign(params, new StringBuffer(secret));
		if (orgin == null)
			return result;
		orgin.append(secret);
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			result = byte2hex(md.digest(orgin.toString().getBytes("utf-8")));
		} catch (Exception e) {
			throw new java.lang.RuntimeException("sign error !");
		}

		return result;
	}

	/**
	 * 二行制转字符串
	 */
	private static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs.append("0").append(stmp);
			else
				hs.append(stmp);
		}

		return hs.toString().toUpperCase();
	}

	/**
	 * 添加除方法名和签名之外的系统级参数
	 * 
	 * @return
	 */
	public static Map<String, String> getSystemParams() {
		TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
		// apiparamsMap.put("session", AppConstants.TOPSESSION); //
		// 他用型需要sessionkey
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		apiparamsMap.put("timestamp", timestamp);
		apiparamsMap.put("format", "xml");
		apiparamsMap.put("app_key", Util.APPKEY);
		apiparamsMap.put("v", "2.0");
		apiparamsMap.put("sign_method", "md5");

		return apiparamsMap;
	}

	/**
	 * 拼装请求的参数
	 * 
	 * @param apiparamsMap
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getContent(Map apiparamsMap) {
		StringBuilder param = new StringBuilder();
		for (Iterator<Map.Entry<String, String>> it = apiparamsMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, String> e = it.next();
			param.append("&").append(e.getKey()).append("=").append(e.getValue());
		}
		String paramsString = param.toString().substring(1);
		System.out.println(paramsString);
		return paramsString;
	}

	/**
	 * 添加参数的封装方法
	 */
	private static StringBuffer getBeforeSign(TreeMap<String, String> params, StringBuffer orgin) {
		if (params == null)
			return null;
		Map<String, String> treeMap = new TreeMap<String, String>();
		treeMap.putAll(params);
		Iterator<String> iter = treeMap.keySet().iterator();
		while (iter.hasNext()) {
			String name = (String) iter.next();
			orgin.append(name).append(params.get(name));
		}

		return orgin;
	}

	// 汉字转码
	public static String toUtf8String(String s) {
		if (s == null || s.equals("")) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		try {
			char c;
			for (int i = 0; i < s.length(); i++) {
				c = s.charAt(i);
				if (c >= 0 && c <= 255) {
					sb.append(c);
				} else {
					byte[] b;
					b = Character.toString(c).getBytes("utf-8");
					for (int j = 0; j < b.length; j++) {
						int k = b[j];
						if (k < 0)
							k += 256;
						sb.append("%" + Integer.toHexString(k).toUpperCase());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 连接到TOP服务器并获取数据
	 * 
	 * @param urlStr
	 * @param content
	 * @return
	 */
	public static String getResult(String urlStr, String content) {
		URL url = null;
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.connect();

			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.write(content.getBytes("utf-8"));
			out.flush();
			out.close();

			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			System.out.println("getResult==" + buffer.toString());
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}

			try {
				// 原来reader的关闭在return
				// buffer.toString();之前，这样是不行，不能返回值，必须放在finally中才正确
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}
