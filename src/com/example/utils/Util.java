package com.example.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

public class Util {
	public static String expires_in="";
	public static int taobao_user_id;
	public static String refresh_token = "";
	public static String access_token = "";

	public static String num_iid="";
	public static String tid="";
	public static String pingjianick="";
	public static String result="";
	public static String created="";
	public static String item_title="";
	public static String content="";
	public static String id="";
	public static String xinxi="";
	public static String num_tid;
	public static String bulletin;
	public static String desc;
	public static String title;
	public static String codeo;
	public static String oid;
	public static String top_session;

	//这是我的APP 
	public static final String APPKEY = "21599349";
	public static final String SECRET = "2eda51db6ad11101f1d40104a4645cdc";
 
	public final static String API_URL = "http://gw.api.tbsandbox.com/router/rest";
	/**
	 * 对传进来的字符串进行MD5编码
	 */
	public static byte[] getMD5(String val) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(val.getBytes());
		return md5.digest();
	}

	/**
	 * 对传进来的字节数组进行MD5编码
	 */
	public static byte[] getMD5(byte[] byteArray)
			throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(byteArray);
		return md5.digest();
	}

	/**
	 * HTTP请求链接
	 */
	public static String httpClient(String stringurl) {
		StringBuffer result = new StringBuffer();
		URLConnection urlConnection;
		try {
			URL url = new URL(stringurl);
			urlConnection = (HttpURLConnection) url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				result.append(line);
				Log.i("sssss", "line=" + line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.i("sssss", "result=" + result);
		return new String(result);
	}

	/**
	 * 连接到TOP服务器并获取数据
	 * */
	public static String getResult(String[] urlArray) {
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlArray[0]);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.connect();
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());
			out.write(urlArray[1].getBytes("utf-8"));
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}
}