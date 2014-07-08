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

import com.example.entity.WuliuItem;
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

//等待发货
public class Fahuo0 extends Activity{
	public static final String TAG = "Fahuo0";
	
	List<WuliuItem> ois;
	ListView onFahuoList;
	LinearLayout loadingLayout,exit,fanhui;
	 AlertDialog alertDialog;
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fa1);
	        MyApplication.getInstance().addActivity(this);
	        init();
			loadOnFaList();
			fanhui.setOnClickListener(new Fanhui());
			exit.setOnClickListener(new Exit());
	       }

		public void init() {
			loadingLayout = (LinearLayout) findViewById(R.id.loadingLayout01);
			onFahuoList = (ListView) this.findViewById(R.id.onFahuoList);
			exit = (LinearLayout) findViewById(R.id.exitLayout);
			fanhui = (LinearLayout) findViewById(R.id.addLayout);
		}
		class Exit implements OnClickListener {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Fahuo0.this, MainActivity.class);								
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
		public void loadOnFaList() {
			new Thread() {
				@Override
				public void run() {
					loadingLayout.setVisibility(View.VISIBLE);
					String testUrl = "http://gw.api.taobao.com/router/rest";				
					String result = APIUtil.getResult(testUrl,onFaGet());
					
					try {
						JSONObject data1 = new JSONObject(result);
						
						JSONObject logistics_orders_get_response = data1.getJSONObject("logistics_orders_get_response");						
						int total_results = logistics_orders_get_response.getInt("total_results");
						if(total_results==0){
							Intent intent = new Intent();
							intent.setClass(Fahuo0.this, Error.class);	
							finish();
						    startActivity(intent);
						}
						JSONObject shippings = logistics_orders_get_response.getJSONObject("shippings");
						JSONArray data2 = shippings.getJSONArray("shipping");
						
						
						
						for (int i = 0; i < data2.length(); i++) {
							
							JSONObject shipping = data2.getJSONObject(i);
								String item_title = shipping.getString("item_title");
								String freight_payer = shipping.getString("freight_payer");
								String buyer_nick = shipping.getString("buyer_nick");
								String created = shipping.getString("created");
								String type = shipping.getString("type");
								String receiver_name = shipping.getString("receiver_name");
								String seller_confirm = shipping.getString("seller_confirm");
								String status = shipping.getString("status");
								String seller_nick = shipping.getString("seller_nick");
								String tid = shipping.getString("tid");
								
							if (ois == null) {
								ois = new ArrayList<WuliuItem>();
							}
							WuliuItem osi = new WuliuItem();
							
							osi.setItem_title(item_title);
							osi.setFreight_payer(freight_payer);
							osi.setBuyer_nick(buyer_nick);
							osi.setCreated(created);
							osi.setType(type);
							osi.setReceiver_name(receiver_name);
							osi.setSeller_confirm(seller_confirm);
							osi.setStatus(status);
							osi.setSeller_nick(seller_nick);
							osi.setTid(tid);
							ois.add(osi);
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
				if (ois != null) { 
					onFahuoList.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View view,int arg2, long arg3) {
							Object obj = view.getTag();
							if (obj != null) {
								Intent intent = new Intent();
								String tid = obj.toString();
								Log.i(TAG, "load_id----result =" + tid);
								Util.tid=tid;
								intent.setClass(Fahuo0.this, Shangpin_fahuo.class);								
							    startActivity(intent);
							}
						}

					});
					OnSaleAdapter adapter = new OnSaleAdapter(Fahuo0.this);
					onFahuoList.setAdapter(adapter);

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
				return ois.size();
			}

			@Override
			public Object getItem(int position) {
				return ois.get(position);
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
				Log.i(TAG, "TEST===5=======");
				WeiBoHolder wh = new WeiBoHolder();
				wh.biaoti = (TextView) satusView.findViewById(R.id.biaoti);
				wh.qixian = (TextView) satusView.findViewById(R.id.qixian);
				wh.jiage = (TextView) satusView.findViewById(R.id.jiage);
				WuliuItem wt = ois.get(position);
				if (wt != null) {
					satusView.setTag(wt.getTid());
					wh.biaoti.setText(wt.getItem_title());
					wh.qixian.setText("购买时间："+wt.getCreated());
					wh.jiage.setText("买家用户名："+wt.getBuyer_nick());
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
		public String onFaGet() {
			TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
			apiparamsMap.put("format", "json");
			apiparamsMap.put("method", "taobao.logistics.orders.get");
			apiparamsMap.put("sign_method", "md5");
			apiparamsMap.put("app_key", Util.APPKEY);
			apiparamsMap.put("v", "2.0");
			apiparamsMap.put("status", "CREATED");
			apiparamsMap.put("seller_confirm", "no");
			apiparamsMap.put("session", Util.access_token); 
			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date());
			apiparamsMap.put("timestamp", timestamp);
			apiparamsMap.put("fields",
					"buyer_nick,created,freight_payer,item_title,modified,receiver_name,seller_confirm,seller_nick,status,tid,type");
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

			Log.i(TAG, "param.~~~~~~= "+ param.toString().substring(1));
			return param.toString().substring(1);
		}	        
}
