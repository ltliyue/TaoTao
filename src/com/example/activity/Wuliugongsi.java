package com.example.activity;
//物流公司信息
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

import com.example.entity.WuliuItem;
import com.example.taotao.R;
import com.example.utils.AsyncImageLoader;
import com.example.utils.Util;
import com.example.utils.APIUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Wuliugongsi extends Activity{
	public static final String TAG = "Wuliugongsi";

	List<WuliuItem> osis;
	ListView gongsi;
	LinearLayout loadingLayout,exit,fanhui;
	 AlertDialog alertDialog;

	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.wuliugongsi);
	        MyApplication.getInstance().addActivity(this);
	        init();
			loadOnSaleList();
			fanhui.setOnClickListener(new Fanhui());
			exit.setOnClickListener(new Exit());
	       }

		public void init() {
			loadingLayout = (LinearLayout) findViewById(R.id.loadingLayout);
			gongsi = (ListView) this.findViewById(R.id.gongsi);

			exit = (LinearLayout) findViewById(R.id.exitLayout);
			fanhui = (LinearLayout) findViewById(R.id.addLayout);
		}
		class Exit implements OnClickListener {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Wuliugongsi.this, MainActivity.class);								
			    startActivity(intent);
			}
		}
		class Fanhui implements OnClickListener {
	 		@Override
	 		public void onClick(View v) {  
	 			
	 			Wuliugongsi.this.finish();
	 		}
	 	}

		/**
		 * 获取列表的线程
		 */
		public void loadOnSaleList() {
			new Thread() {
				@Override
				public void run() {
					loadingLayout.setVisibility(View.VISIBLE);
					String testUrl = "http://gw.api.taobao.com/router/rest";				
					String result = APIUtil.getResult(testUrl,onSaleGet());
					JSONArray data;
					try {
						JSONObject data1 = new JSONObject(result);
						
						JSONObject logistics_companies_get_response = data1.getJSONObject("logistics_companies_get_response");						
						JSONObject logistics_companies = logistics_companies_get_response.getJSONObject("logistics_companies");
						data = logistics_companies.getJSONArray("logistics_company");
						for (int i = 0; i < data.length(); i++) {
							JSONObject logistics_company = data.getJSONObject(i);
							
								String code = logistics_company.getString("code");
								String name = logistics_company.getString("name");
								String id = logistics_company.getString("id");
							if (osis == null) {
								osis = new ArrayList<WuliuItem>();
							}

							WuliuItem osi = new WuliuItem();
							
							osi.setCode(code);
							osi.setName(name);
							osi.setId(id);
							
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
					gongsi.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View view,int arg2, long arg3) {
							Object obj = view.getTag();
							if (obj != null) {
								Intent intent = new Intent();
								String id = obj.toString();
								Log.i(TAG, "load_id----result =" + id);
								Util.id=id;
								intent.setClass(Wuliugongsi.this, Xianxiafahuo.class);								
							    finish();
							    startActivity(intent);
							    
							}
						}

					});

					OnSaleAdapter adapter = new OnSaleAdapter(Wuliugongsi.this);
					gongsi.setAdapter(adapter);

					loadingLayout.setVisibility(View.GONE); // 更新完UI，让总转的进度条视图消失

				}
			}
		};
		public class OnSaleAdapter extends BaseAdapter {
			private AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
			private Context context;

			public OnSaleAdapter(Context con) {
				this.context = con;
			}

			@Override
			public int getCount() {
				return osis.size();
			}

			@Override
			public Object getItem(int position) {
				return osis.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View satusView = null;
				if (convertView != null) {
					// 获取原来内存中保存的条目信息
					satusView = convertView;
				} else {
					satusView = LayoutInflater.from(getApplication()).inflate(
							R.layout.wuliugongsi_item, null);
				}

				WeiBoHolder wh = new WeiBoHolder(); // 消息实体
				wh.bianhao = (TextView) satusView.findViewById(R.id.bianhao);
				wh.mingzi = (TextView) satusView.findViewById(R.id.mingzi);
				
				WuliuItem wb = osis.get(position);
				if (wb != null) {
					satusView.setTag(wb.getId());
					wh.bianhao.setText(wb.getCode());
					wh.mingzi.setText(wb.getName());
				}
				return satusView;
			}
		}
		public static class WeiBoHolder {
			
			public TextView bianhao;
			public TextView mingzi;
		}

		/**
		 * 拼装请求的参数并按拼音排序
		 * 
		 * @return
		 */
		public String onSaleGet() {
			TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
			apiparamsMap.put("format", "json");
			apiparamsMap.put("method", "taobao.logistics.companies.get");
			apiparamsMap.put("sign_method", "md5");
			apiparamsMap.put("app_key", Util.APPKEY);
			apiparamsMap.put("v", "2.0");
			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date());
			apiparamsMap.put("timestamp", timestamp);
			apiparamsMap
					.put("fields","id,code,name");// 需要获取的字段

			// 生成签名
			String sign = APIUtil.md5Signature(apiparamsMap, Util.SECRET);
			apiparamsMap.put("sign", sign);
			StringBuilder param = new StringBuilder();
			for (Iterator<Map.Entry<String, String>> it = apiparamsMap.entrySet()
					.iterator(); it.hasNext();) {
				Map.Entry<String, String> e = it.next();
				param.append("&").append(e.getKey()).append("=")
						.append(e.getValue());
			}

			Log.i(TAG, "param.toString().substring(1) = "+ param.toString().substring(1));
			return param.toString().substring(1);
		}	        
}
