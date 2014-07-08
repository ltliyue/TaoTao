package com.example.activity;

import android.app.Activity;
import android.os.Bundle;

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

import com.example.entity.FahuoItem;
import com.example.taotao.R;
import com.example.utils.Util;
import com.example.utils.APIUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

//已发货的宝贝
public class Fahuo2 extends Activity{
	public static final String TAG = "Fahuo2";
	
	List<FahuoItem> oi;
	ListView onFahuoList2;
	LinearLayout loadingLayout,exit,fanhui;
	 AlertDialog alertDialog;
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fa3);
	        MyApplication.getInstance().addActivity(this);	       
	        init();
			loadOnFa2List();
			fanhui.setOnClickListener(new Fanhui());
			exit.setOnClickListener(new Exit());
	       }

		public void init() {
			loadingLayout = (LinearLayout) findViewById(R.id.loadingLayout3);
			onFahuoList2 = (ListView) this.findViewById(R.id.onFahuoList3);
			exit = (LinearLayout) findViewById(R.id.exitLayout);
			fanhui = (LinearLayout) findViewById(R.id.addLayout);
		}
		class Exit implements OnClickListener {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Fahuo2.this, MainActivity.class);								
			    startActivity(intent);
			    }
		}

		class Fanhui implements OnClickListener {
	 		@Override
	 		public void onClick(View v) {  
	 		
			    finish();	 		}
	 	}
		/**
		 * 获取列表的线程
		 */
		public void loadOnFa2List() {
			new Thread() {
				@Override
				public void run() {
					loadingLayout.setVisibility(View.VISIBLE);
					String testUr = "http://gw.api.taobao.com/router/rest";				
					String resu = APIUtil.getResult(testUr,onFa2Get());					
					
					try {
						JSONObject data = new JSONObject(resu);
						JSONObject logistics_orders_get_response = data.getJSONObject("logistics_orders_get_response");						
						int total_results = logistics_orders_get_response.getInt("total_results");
						if(total_results==0){
							Intent intent = new Intent();
							intent.setClass(Fahuo2.this, Error.class);								
						    
						    finish();startActivity(intent);
						}
						JSONObject shippings = logistics_orders_get_response.getJSONObject("shippings");
						JSONArray dataa = shippings.getJSONArray("shipping");
						for (int i = 0; i < dataa.length(); i++) {
							
							JSONObject shipping = dataa.getJSONObject(i);
							
								String item_title = shipping.getString("item_title");
								String buyer_nick = shipping.getString("buyer_nick");
								String created = shipping.getString("created");
								String tid = shipping.getString("tid");
								
							if (oi == null) {
								oi = new ArrayList<FahuoItem>();
							}
							FahuoItem osi = new FahuoItem();
							
							osi.setItem_title(item_title);
							osi.setBuyer_nick(buyer_nick);
							osi.setCreated(created);
							osi.setTid(tid);
							oi.add(osi);
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
				if (oi != null) { 
					onFahuoList2.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View view,int arg2, long arg3) {
							Object obj = view.getTag();
							if (obj != null) {
								Intent intent = new Intent();
								String tid = obj.toString();
								Log.i(TAG, "load_id----result =" + tid);
								Util.tid=tid;
								intent.setClass(Fahuo2.this, WuliuActivity.class);								
							    startActivity(intent);
							}
						}

					});
					OnSaleAdapter adapter = new OnSaleAdapter(Fahuo2.this);
					onFahuoList2.setAdapter(adapter);
					loadingLayout.setVisibility(View.GONE); // 更新完UI，让总转的进度条视图消失
				}
			}
		};
		public class OnSaleAdapter extends BaseAdapter {
			private Context context;

			public OnSaleAdapter(Context con) {
				this.context = con;
			}

			@Override
			public int getCount() {
				return oi.size()-1;
			}

			@Override
			public Object getItem(int position) {
				return oi.get(position);
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
					satusView = LayoutInflater.from(getApplication()).inflate(R.layout.fa_item, null);
				}
				WeiBoHolder wh = new WeiBoHolder(); 
				wh.biaoti = (TextView) satusView.findViewById(R.id.biaoti);
				wh.qixian = (TextView) satusView.findViewById(R.id.qixian);
				wh.jiage = (TextView) satusView.findViewById(R.id.jiage);
				FahuoItem wth = oi.get(position);
				if (wth != null) {
					satusView.setTag(wth.getTid());
					wh.biaoti.setText(wth.getItem_title());
					wh.qixian.setText("购买时间："+wth.getCreated());
					wh.jiage.setText("买家用户名："+wth.getBuyer_nick());
				}
				
				return satusView;
			}

		}

		public static class WeiBoHolder {
			public TextView jiage;
			public TextView biaoti;
			public TextView qixian;
		}

		
		/**
		 * 拼装请求的参数并按拼音排序
		 * 
		 * @return
		 */
		public String onFa2Get() {
			TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
			apiparamsMap.put("format", "json");
			apiparamsMap.put("method", "taobao.logistics.orders.get");
			apiparamsMap.put("sign_method", "md5");
			apiparamsMap.put("app_key", Util.APPKEY);
			apiparamsMap.put("v", "2.0");	
			apiparamsMap.put("session", Util.access_token);
			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date());
			apiparamsMap.put("timestamp", timestamp);
			apiparamsMap.put("fields",
					"tid,seller_nick,buyer_nick,delivery_start,delivery_end,out_sid,item_title,receiver_name,created,modified,status,type,freight_payer,seller_confirm,company_name");
			// 需要获取的字段

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
			return param.toString().substring(1);
		}	        
}
