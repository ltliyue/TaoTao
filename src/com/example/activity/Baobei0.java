package com.example.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.adapter.OnSaleAdapter;
import com.example.entity.OnSaleItem;
import com.example.taotao.R;
import com.example.utils.AsyncImageLoader;
import com.example.utils.AsyncImageLoader.ImageCallback;
import com.example.utils.Util;
import com.example.utils.APIUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

//出售中商品
public class Baobei0 extends Activity {
	public static final String TAG = "Baobei0";
	private int pageno;
	private Button up, down;
	private int total_results;
	List<OnSaleItem> osis;
	ListView onSaleList;
	LinearLayout loadingLayout;
	AlertDialog alertDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.baobei0);
		init();
		loadOnSaleList();
		initListener();
	}

	public void init() {
		onSaleList = (ListView) this.findViewById(R.id.onSaleList);
		loadingLayout = (LinearLayout) findViewById(R.id.loadingLayout);
		up = (Button) findViewById(R.id.up);
		down = (Button) findViewById(R.id.down);
		pageno = 1;
	}

	private void initListener() {
		// TODO Auto-generated method stub
		up.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (pageno == 1) {
					up.setEnabled(false);
				} else {
					up.setEnabled(true);
					pageno--;
					loadOnSaleList();
				}
			}
		});
		down.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				up.setEnabled(true);
				if (pageno <= total_results / 10) {
					pageno++;
					loadOnSaleList();
				}
			}
		});
	}

	class Fanhui implements OnClickListener {
		@Override
		public void onClick(View v) {
			finish();
		}
	}

	/**
	 * 获取在售商品列表的线程
	 */
	public void loadOnSaleList() {
		loadingLayout.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run() {
				String testUrl = "http://gw.api.taobao.com/router/rest";
				String result = APIUtil.getResult(testUrl, onSaleGet(pageno));

				JSONArray data;
				try {
					JSONObject data1 = new JSONObject(result);
					JSONObject items_onsale_get_response = data1.getJSONObject("items_onsale_get_response");
					total_results = items_onsale_get_response.getInt("total_results");
					if (total_results == 0) {
						Intent intent = new Intent();
						intent.setClass(Baobei0.this, Error.class);
						finish();
						startActivity(intent);
					}
					JSONObject items = items_onsale_get_response.getJSONObject("items");
					data = items.getJSONArray("item");
					if (osis != null) {
						osis.clear();
					}
					for (int i = 0; i < data.length(); i++) {
						JSONObject item = data.getJSONObject(i);

						if (osis == null) {
							osis = new ArrayList<OnSaleItem>();
						}

						OnSaleItem osi = new OnSaleItem();

						osi.setNum_iid(item.getString("num_iid"));
						osi.setPic_url(item.getString("pic_url"));
						osi.setPrice(item.getString("price"));
						osi.setTitle(item.getString("title"));
						osi.setValid_thru(item.getString("valid_thru"));
						osi.setDelist_time(item.getString("delist_time"));

						osis.add(osi);
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
			if (osis != null) {
				onSaleList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
						Object obj = view.getTag();
						if (obj != null) {
							Intent intent = new Intent();
							String num_iid = obj.toString();
							Util.num_iid = num_iid;
							intent.setClass(Baobei0.this, ShangpinActivity.class);
							startActivity(intent);
						}
					}

				});

				OnSaleAdapter adapter = new OnSaleAdapter(Baobei0.this, osis);
				onSaleList.setAdapter(adapter);
				loadingLayout.setVisibility(View.GONE); // 更新完UI，让总转的进度条视图消失
			}
		}
	};

	/**
	 * 拼装请求的参数并按拼音排序
	 * 
	 * @return
	 */
	public String onSaleGet(int pageno) {
		Log.i(TAG, "onSaleGet----");
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
		apiparamsMap.put("format", "json");
		apiparamsMap.put("method", "taobao.items.onsale.get");
		apiparamsMap.put("sign_method", "md5");
		apiparamsMap.put("app_key", Util.APPKEY);
		apiparamsMap.put("v", "2.0");
		apiparamsMap.put("page_no", pageno + "");
		apiparamsMap.put("page_size", "10");
		apiparamsMap.put("session", Util.access_token);
		apiparamsMap.put("timestamp", timestamp);
		apiparamsMap.put("fields", "num_iid,title,type,pic_url,num,props,valid_thru,price,delist_time");// 需要获取的字段

		// 生成签名
		String sign = APIUtil.md5Signature(apiparamsMap, Util.SECRET);
		apiparamsMap.put("sign", sign);
		StringBuilder param = new StringBuilder();
		for (Iterator<Map.Entry<String, String>> it = apiparamsMap.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, String> e = it.next();
			param.append("&").append(e.getKey()).append("=").append(e.getValue());
		}

		Log.i(TAG, "param.toString().substring(1) = " + param.toString().substring(1));
		return param.toString().substring(1);
	}

}
