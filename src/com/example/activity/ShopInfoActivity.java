package com.example.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taotao.R;
import com.example.utils.APIUtil;

/**
 * 店铺信息
 */
public class ShopInfoActivity extends Activity {

	public static final String TAG = "DianpuActivity";
	private ImageView title_back;
	private TextView title_title, title_setting;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private String taobao_user_nick;
	private String title, desc, bulletin;
	private TextView biaoti, gonggao, miaoshu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.dianpu);
		sharedPreferences = getSharedPreferences("AccessToken", 0);
		editor = sharedPreferences.edit();
		taobao_user_nick = sharedPreferences.getString("taobao_user_nick", "");
		initWidgt();
		initListener();
		getUserinfo();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getUserinfo();
	}

	public void initWidgt() {
		title_back = (ImageView) findViewById(R.id.title_back);
		title_title = (TextView) findViewById(R.id.title_title);
		title_title.setText("店铺信息");
		title_setting = (TextView) findViewById(R.id.title_setting);
		title_setting.setText("编辑");
		biaoti = (TextView) findViewById(R.id.biaoti);
		gonggao = (TextView) findViewById(R.id.gonggao);
		miaoshu = (TextView) findViewById(R.id.miaoshu);
	}

	private void initListener() {
		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		title_setting.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ShopInfoActivity.this, ShopUpdateActivity.class);
				intent.putExtra("shopinfo", title + "@" + bulletin + "@" + desc);
				startActivity(intent);
			}
		});
	}

	public void getUserinfo() {
new Thread() {
	@Override
	public void run() {
		Map<String, String> parasMap = new HashMap<String, String>();
		parasMap.put("method", "taobao.shop.get");
		parasMap.put("fields", "title,nick,desc,bulletin");
		parasMap.put("nick", taobao_user_nick);
		String result = APIUtil.getShopInfo(parasMap);
		try {
			JSONObject data1 = new JSONObject(result);
			JSONObject shop_get_response = data1.getJSONObject("shop_get_response");
			JSONObject shop = shop_get_response.getJSONObject("shop");
			title = shop.getString("title");
			bulletin = shop.getString("bulletin");
			desc = shop.getString("desc");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Message message = handler.obtainMessage(0);
		handler.sendMessage(message);
	}
}.start();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			biaoti.setText("店铺名称：\n  " + title);
			gonggao.setText("店铺公告：\n  " + bulletin);
			miaoshu.setText("店铺描述：\n  " + Html.fromHtml(desc));
		}
	};

}
