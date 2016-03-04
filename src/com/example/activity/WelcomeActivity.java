package com.example.activity;

//ª∂”≠ΩÁ√Ê
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import cn.trinea.android.common.util.HttpUtils;
import cn.trinea.android.common.util.JSONUtils;

import com.example.data.ContextData;
import com.example.taotao.R;
import com.example.utils.Util;

public class WelcomeActivity extends Activity {

	private ImageView iv;
	AlphaAnimation alphaAnimation;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private String refresh_token;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setContentView(R.layout.welcome);
		sharedPreferences = getSharedPreferences("AccessToken", 0);
		editor = sharedPreferences.edit();
		if (!sharedPreferences.getString("refresh_token", "").equalsIgnoreCase("")) {
			refresh_token = sharedPreferences.getString("refresh_token", "");
			initData();
		}
		initView();
		initListener();
	}

	private void initView() {
		// TODO Auto-generated method stub
		iv = (ImageView) this.findViewById(R.id.open_bg);
		alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
		alphaAnimation.setDuration(3000);
		iv.startAnimation(alphaAnimation);

	}

	private void initListener() {
		// TODO Auto-generated method stub
alphaAnimation.setAnimationListener(new AnimationListener() {
	@Override
	public void onAnimationEnd(Animation arg0) {
		if (refresh_token != null) {
			intent = new Intent(WelcomeActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		} else {
			intent = new Intent(WelcomeActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}
	@Override
	public void onAnimationRepeat(Animation animation) {
	}
	@Override
	public void onAnimationStart(Animation animation) {
	}
});
	}

	private void initData() {
		// TODO Auto-generated method stub
new Thread(new Runnable() {
	@Override
	public void run() {
		// TODO Auto-generated method stub
		HashMap<String, String> parasMap = new HashMap<String, String>();
		String httpUrl = ContextData.GetAccessToken;
		parasMap.put("client_id", Util.APPKEY);
		parasMap.put("client_secret", Util.SECRET);
		parasMap.put("grant_type", "refresh_token");
		parasMap.put("refresh_token", refresh_token);
		String result = HttpUtils.httpPostString(httpUrl, parasMap);
		Util.access_token = JSONUtils.getString(result, "access_token", "");
		if (Util.access_token == null) {
			refresh_token = "";
		} else {
			editor.putString("access_token", JSONUtils.getString(result, "access_token", "")).commit();
			editor.putString("refresh_token", JSONUtils.getString(result, "refresh_token", "")).commit();

		}
	}
}).start();
	}

}
