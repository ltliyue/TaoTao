package com.example.activity;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import cn.trinea.android.common.util.HttpUtils;
import cn.trinea.android.common.util.JSONUtils;

import com.example.data.ContextData;
import com.example.taotao.R;
import com.example.utils.Util;
import com.example.widget.MyProgressWebView;

/**
 * /登陆界面
 * 
 */
public class LoginActivity extends Activity {
	public static final String TAG = "MainActivity";
	private MyProgressWebView myWebView;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private Intent intent;
	private String url1 = ContextData.LoginURL;
	private String resultURL;
	private String code = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		sharedPreferences = getSharedPreferences("AccessToken", 0);
		editor = sharedPreferences.edit();
		initView();
		initListener();
		initWebView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		myWebView = (MyProgressWebView) findViewById(R.id.webviewid);
	}

private void initWebView() {
	WebSettings webSettings = myWebView.getSettings();
	webSettings.setBuiltInZoomControls(true);// 启用内置缩放装置
	webSettings.setSupportZoom(false);// 支持缩放
	webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩
	myWebView.setWebViewClient(new WebViewClient() {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			System.out.println("系统出错");
		}

		@Override
		public void onPageFinished(WebView view, String url) {
resultURL = myWebView.getUrl();
if (resultURL != null) {
	resultURL = myWebView.getUrl().split("&")[0];
	System.out.println(resultURL);
	if (resultURL.contains("code=")) {
		code = resultURL.substring(resultURL.indexOf("code=") + 5, resultURL.length());
		Log.i(TAG, "code = " + code);
		// 如果获得的code不为空，那么跳到httppost
		if (code != "") {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HashMap<String, String> parasMap = new HashMap<String, String>();
					String httpUrl = ContextData.GetAccessToken;
					parasMap.put("grant_type", "authorization_code");
					parasMap.put("code", code);
					parasMap.put("client_id", Util.APPKEY);
					parasMap.put("client_secret", Util.SECRET);
					parasMap.put("redirect_uri", "http://www.oauth.net/2/");
					String result = HttpUtils.httpPostString(httpUrl, parasMap);
					editor.putString("taobao_user_nick", JSONUtils.getString(result, "taobao_user_nick", "")).commit();
					Util.access_token = JSONUtils.getString(result, "access_token", "");
					editor.putString("access_token", JSONUtils.getString(result, "access_token", "")).commit();
					editor.putString("refresh_token", JSONUtils.getString(result, "refresh_token", "")).commit();
				}
			}).start();
			intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		} else {
			Toast.makeText(LoginActivity.this, "系统错误", ContextData.toastTime).show();
		}
	}
}
		}
	});
	myWebView.setDownloadListener(new DownloadListener() {
		@Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
			if (url != null && url.startsWith("http://"))
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		}
	});
	if (url1 != null) {
		myWebView.loadUrl(url1);
	} else {
		Toast.makeText(LoginActivity.this, "当前文件不存在", ContextData.toastTime).show();
	}
}

	private void initListener() {
		// TODO Auto-generated method stub

	}
}
