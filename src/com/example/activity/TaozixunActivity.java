package com.example.activity;

//�Ա���Ѷ
import com.example.taotao.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class TaozixunActivity extends Activity {
	private WebView webview;
	LinearLayout exit, fanhui;
	AlertDialog alertDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zixun);
		MyApplication.getInstance().addActivity(this);
		initWight();
		fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());

		webview = (WebView) findViewById(R.id.zixun);
		webview.getSettings().setJavaScriptEnabled(true);
		// ������Ҫ��ʾ����ҳ
		webview.loadUrl("http://m.taobao.com");

		webview.setWebViewClient(new HelloWebViewClient());
	}

	public void initWight() {
		exit = (LinearLayout) findViewById(R.id.exitLayout);
		fanhui = (LinearLayout) findViewById(R.id.addLayout);
	}

	class Exit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(TaozixunActivity.this, MainActivity.class);
			startActivity(intent);
		}
	}

	class Fanhui implements OnClickListener {
		@Override
		public void onClick(View v) {

			TaozixunActivity.this.finish();
		}
	}

	@Override
	// ���û���
	// ����Activity���onKeyDown(int keyCoder,KeyEvent event)����
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
			webview.goBack(); // goBack()��ʾ����WebView����һҳ��
			return true;
		}
		return false;
	}

	private class HelloWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
