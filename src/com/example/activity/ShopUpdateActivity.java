package com.example.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.taotao.R;
import com.example.utils.APIUtil;
import com.example.utils.Util;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 修改店铺信息
 * 
 * @author Meyao
 * 
 */
public class ShopUpdateActivity extends Activity {
	public static final String TAG = "DianpuActivity01";
	private ImageView title_back;
	private TextView title_title, title_setting;
	EditText biaoti; // 店铺名称
	EditText gonggao; // 店铺公告
	EditText miaoshu; // 店家描述
	String feedTitle;
	String feedDesc;
	String feedBulletin;
	String sign;
	String result;
	AlertDialog alertDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.shop_update);
		initWidgt();
		initListener();
	}

	public void initWidgt() {
		title_back = (ImageView) findViewById(R.id.title_back);
		title_title = (TextView) findViewById(R.id.title_title);
		title_title.setText("店铺信息修改");
		title_setting = (TextView) findViewById(R.id.title_setting);
		title_setting.setText("编辑");
		String shopinfo = getIntent().getStringExtra("shopinfo");
		biaoti = (EditText) this.findViewById(R.id.edit_biaoti);
		gonggao = (EditText) this.findViewById(R.id.edit_gonggao);
		miaoshu = (EditText) this.findViewById(R.id.edit_miaoshu);
		biaoti.setText(shopinfo.split("@")[0]);
		gonggao.setText(shopinfo.split("@")[1]);
		miaoshu.setText(Html.fromHtml(shopinfo.split("@")[2]));
	}

	private void initListener() {
		// TODO Auto-generated method stub
		title_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		title_setting.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				feedTitle = biaoti.getText().toString().trim();
				feedDesc = miaoshu.getText().toString().trim();
				feedBulletin = gonggao.getText().toString().trim();
				new Thread() {
					@Override
					public void run() {

						Map<String, String> parasMap = new HashMap<String, String>();
						parasMap.put("method", "taobao.shop.update");
						parasMap.put("bulletin", gonggao.getText().toString().trim());
						parasMap.put("desc", miaoshu.getText().toString().trim());
						parasMap.put("title", biaoti.getText().toString().trim());
						String result = APIUtil.getShopInfo(parasMap);

						Message msg = new Message();
						msg.what = 2;
						msg.obj = result;
						handler.sendMessage(msg); // 用activity中的handler发送消息
					}
				}.start();
				// Intent intent = new Intent();
				// intent.setClass(ShopUpdateActivity.this,
				// ShopUpdateActivity.class);
				// startActivity(intent);
			}
		});
	}

	/**
	 * 更新UI线程ListView
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 2) {
				try {
					JSONObject data = new JSONObject(String.valueOf(msg.obj));

					JSONObject shop_update_response = data.getJSONObject("shop_update_response");
					JSONObject shop = shop_update_response.getJSONObject("shop");
					String modified = shop.getString("modified");
					String sid = shop.getString("sid");

					if (sid != null) {
						Toast.makeText(ShopUpdateActivity.this, "店铺信息更改成功", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(ShopUpdateActivity.this, "店铺信息更改失败", Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
}
