package com.example.activity;

//物流信息
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.taotao.R;
import com.example.utils.Util;
import com.example.utils.APIUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WuliuActivity extends Activity {
	public static final String TAG = "WuliuActivity";
	LinearLayout exit, fanhui;
	AlertDialog alertDialog;
	URL url = null;

	TextView jiaoyi; // 名字
	TextView fangshi; // 价格
	TextView yundan; // 数量
	TextView zhuangtai; // 数量
	TextView xinxi; // 数量

	// 签名值
	String sign, feedSig;
	String result;
	String company_name, status, tid, status_desc, out_sid;
	HttpClient client;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wuliu);
		MyApplication.getInstance().addActivity(this);

		initWidgt();
		loadShangpin();
		fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());
	}

	public void initWidgt() {

		jiaoyi = (TextView) this.findViewById(R.id.wu_jiaoyi);
		fangshi = (TextView) this.findViewById(R.id.wu_fangshi);
		yundan = (TextView) this.findViewById(R.id.wu_yundan);
		zhuangtai = (TextView) this.findViewById(R.id.wu_zhuangtai);
		xinxi = (TextView) this.findViewById(R.id.wu_xinxi);

		exit = (LinearLayout) findViewById(R.id.exitLayout);
		fanhui = (LinearLayout) findViewById(R.id.addLayout);
	}

	class Exit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(WuliuActivity.this, MainActivity.class);
			startActivity(intent);
		}
	}

	class Fanhui implements OnClickListener {
		@Override
		public void onClick(View v) {
			WuliuActivity.this.finish();
		}
	}

	public void loadShangpin() {
		new Thread() {
			@Override
			public void run() {
				String testUrl = "http://gw.api.taobao.com/router/rest";
				result = APIUtil.getResult(testUrl, getShangpin());
				try {
					JSONObject data1 = new JSONObject(result);

					JSONObject logistics_trace_search_response = data1.getJSONObject("logistics_trace_search_response");
					tid = logistics_trace_search_response.getString("tid");
					company_name = logistics_trace_search_response.getString("company_name");
					out_sid = logistics_trace_search_response.getString("out_sid");
					status = logistics_trace_search_response.getString("status");

					JSONObject trace_list = logistics_trace_search_response.getJSONObject("trace_list");
					JSONArray data3 = trace_list.getJSONArray("transit_step_info");
					for (int j = 0; j < data3.length(); j++) {
						JSONObject transit_step_info = data3.getJSONObject(j);
						status_desc = transit_step_info.getString("status_desc");
					}
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

			jiaoyi.setText(tid);
			fangshi.setText(company_name);
			yundan.setText(out_sid);
			zhuangtai.setText(status);
			xinxi.setText(status_desc);

		}
	};

	public static String getShangpin() {

		TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
		apiparamsMap.put("format", "json");
		apiparamsMap.put("method", "taobao.logistics.trace.search");
		apiparamsMap.put("sign_method", "md5");
		apiparamsMap.put("app_key", Util.APPKEY);
		apiparamsMap.put("v", "2.0");
		apiparamsMap.put("tid", Util.tid);
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		apiparamsMap.put("timestamp", timestamp);
		apiparamsMap.put("seller_nick", "ltliyue");// 需要获取的字段
		// 生成签名
		String sign = APIUtil.md5Signature(apiparamsMap, Util.SECRET);
		apiparamsMap.put("sign", sign);
		StringBuilder param = new StringBuilder();
		for (Iterator<Map.Entry<String, String>> it = apiparamsMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, String> e = it.next();
			param.append("&").append(e.getKey()).append("=").append(e.getValue());
		}
		return param.toString().substring(1);
	}

}
