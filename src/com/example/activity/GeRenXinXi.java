package com.example.activity;

//个人信息
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
import com.example.utils.AsyncImageLoader;
import com.example.utils.Util;
import com.example.utils.AsyncImageLoader.ImageCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GeRenXinXi extends Activity {
	public static final String TAG = "GeRenXinXi";
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private String taobao_user_nick, access_token;
	TextView xinxiname;
	TextView vip;
	TextView promoted;
	TextView xinxixinyong;
	TextView xinyongdengji;
	TextView pingjiazongshu;
	TextView haopingshu;
	ImageView xinxitupian;

	String result;
	String nick;
	String avatar;
	String level;
	String score;
	String total_num;
	String good_num;
	String created;
	String promoted_type;
	String vip_info;
	LinearLayout exit, fanhui;
	AlertDialog alertDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gerenxinxi);
		MyApplication.getInstance().addActivity(this);
		sharedPreferences = getSharedPreferences("AccessToken", 0);
		editor = sharedPreferences.edit();
		taobao_user_nick = sharedPreferences.getString("taobao_user_nick", "");
		access_token = sharedPreferences.getString("access_token", "");
		initWidgt();
		initListener();
		getUserinfo();
	}

	public void initWidgt() {

		xinxiname = (TextView) this.findViewById(R.id.xinxiname);
		vip = (TextView) this.findViewById(R.id.vip);
		promoted = (TextView) this.findViewById(R.id.promoted);
		xinxixinyong = (TextView) this.findViewById(R.id.xinxixinyong);
		xinyongdengji = (TextView) this.findViewById(R.id.xinyongdengji);
		pingjiazongshu = (TextView) this.findViewById(R.id.pingjiazongshu);
		haopingshu = (TextView) this.findViewById(R.id.haopingshu);
		xinxitupian = (ImageView) this.findViewById(R.id.xinxitupian);
		exit = (LinearLayout) findViewById(R.id.exitLayout);
		fanhui = (LinearLayout) findViewById(R.id.addLayout);
	}

	private void initListener() {
		fanhui.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(GeRenXinXi.this, MainActivity.class);
				finish();
				startActivity(intent);
			}
		});

	}

	public void getUserinfo() {
		new Thread() {
			@Override
			public void run() {
				// 发送请求得到数据
				Map<String, String> parasMap = new HashMap<String, String>();
				parasMap.put("method", "taobao.user.seller.get");
				parasMap.put("access_token", access_token);
				parasMap.put("format", "json");
				parasMap.put("v", "2.0");
				parasMap.put(
						"fields",
						"user_id,nick,sex,seller_credit,type,has_more_pic,item_img_num,item_img_size,prop_img_num,prop_img_size,auto_repost,promoted_type,status,alipay_bind,consumer_protection,avatar,liangpin,sign_food_seller_promise,has_shop,is_lightning_consignment,has_sub_stock,is_golden_seller,vip_info,magazine_subscribe,vertical_market,online_gaming");

				String result = APIUtil.getShopInfo(parasMap);
				System.out.println(result);
				// 解析得到的数据
				try {
					JSONObject data1 = new JSONObject(result);

					JSONObject user_seller_get_response = data1.getJSONObject("user_seller_get_response");
					JSONObject user = user_seller_get_response.getJSONObject("user");
					nick = user.getString("nick");
					avatar = user.getString("avatar");
					promoted_type = user.getString("promoted_type");
					vip_info = user.getString("vip_info");
					JSONObject seller_credit = user.getJSONObject("seller_credit");
					level = seller_credit.getString("level");
					score = seller_credit.getString("score");
					total_num = seller_credit.getString("total_num");
					good_num = seller_credit.getString("good_num");

				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (nick!=null) {
					Message message = handler.obtainMessage(0);
					handler.sendMessage(message);
				}
			}
		}.start();
	}

	// 显示到界面
	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			xinxitupian.setImageResource(R.drawable.ic_android);
			AsyncImageLoader asyncImageLoader = new AsyncImageLoader(); // 异步获取图片
			Drawable cachedImage = asyncImageLoader.loadDrawable(avatar, xinxitupian, new ImageCallback() {
				@Override
				// 这里是重写了回调接口
				public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl) {
					imageView.setImageDrawable(imageDrawable);
				}
			});

			if (cachedImage == null) {
				// 如果没有图片，就以一个载入的图片代替显示
				xinxitupian.setImageResource(R.drawable.ic_android);
			} else {
				// 如果有图片，就载入图片
				xinxitupian.setImageDrawable(cachedImage);
			}
			Log.i(TAG, "234567");
			xinxiname.setText(nick);
			pingjiazongshu.setText(total_num);
			haopingshu.setText(good_num);
			xinyongdengji.setText(level);
			xinxixinyong.setText(score);
			promoted.setText(promoted_type);
			vip.setText(vip_info);
			Log.i(TAG, "111111==========");
		}
	};
}
