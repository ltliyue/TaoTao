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

//等待买家付款
public class Dingdan1 extends Activity{
	public static final String TAG = "Dingdan1";
	
	List<WuliuItem> ois;
	ListView dingdan1;
	String title;
	LinearLayout loadingLayout,exit,fanhui;
	 AlertDialog alertDialog;
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        MyApplication.getInstance().addActivity(this);
	        setContentView(R.layout.ding1);
	        init();
			loadOnFaList();
			fanhui.setOnClickListener(new Fanhui());
			exit.setOnClickListener(new Exit());
	       }

		public void init() {
			loadingLayout = (LinearLayout) findViewById(R.id.loadingLayout01);
			dingdan1 = (ListView) this.findViewById(R.id.dingdan1);
			exit = (LinearLayout) findViewById(R.id.exitLayout);
			fanhui = (LinearLayout) findViewById(R.id.addLayout);
		}
		class Exit implements OnClickListener {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Dingdan1.this, MainActivity.class);								
			    startActivity(intent);
			}
		}

		class Fanhui implements OnClickListener {
	 		@Override
	 		public void onClick(View v) {  
			    finish();	 		
			    }
	 	}


		/**
		 * 获取线程
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
						
						JSONObject trades_sold_get_response = data1.getJSONObject("trades_sold_get_response");						
						int total_results = trades_sold_get_response.getInt("total_results");

						if(total_results==0){
							Intent intent = new Intent();
							intent.setClass(Dingdan1.this, Error.class);						
						    finish();
						    startActivity(intent);
						}
						JSONObject trades = trades_sold_get_response.getJSONObject("trades");
						JSONArray data2 = trades.getJSONArray("trade");
						for (int i = 0; i < data2.length(); i++) {
							if(total_results==0){
								Intent intent = new Intent();
								intent.setClass(Dingdan1.this, Error.class);								
							    startActivity(intent);
							}
							JSONObject trade = data2.getJSONObject(i);
							
								String buyer_nick = trade.getString("buyer_nick");
								String created = trade.getString("created");
								String tid = trade.getString("tid");
									JSONObject orders = trade.getJSONObject("orders");	
									JSONArray data3 = orders.getJSONArray("order");
									for (int j = 0; j < data3.length(); j++) {
										JSONObject order = data3.getJSONObject(j);
										title = order.getString("title");
									}
							if (ois == null) {
								ois = new ArrayList<WuliuItem>();
							}
							WuliuItem osi = new WuliuItem();
							osi.setTitle(title);
							osi.setBuyer_nick(buyer_nick);
							osi.setCreated(created);
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
					dingdan1.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View view,int arg2, long arg3) {
							Object obj = view.getTag();
							if (obj != null) {
								Intent intent = new Intent();
								String tid = obj.toString();
								Log.i(TAG, "load_id----result =" + tid);
								Util.tid=tid;
								intent.setClass(Dingdan1.this, Dingdanxiangqing.class);								
							    startActivity(intent);
							}
						}

					});
					OnSaleAdapter adapter = new OnSaleAdapter(Dingdan1.this);
					dingdan1.setAdapter(adapter);

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
					satusView = LayoutInflater.from(getApplication()).inflate(R.layout.ding_item, null);
				}
				WeiBoHolder wh = new WeiBoHolder(); 
				wh.biaoti = (TextView) satusView.findViewById(R.id.ding_biaoti);
				wh.qixian = (TextView) satusView.findViewById(R.id.ding_qixian);
				wh.jiage = (TextView) satusView.findViewById(R.id.ding_jiage);
				WuliuItem wt = ois.get(position);
				if (wt != null) {
					satusView.setTag(wt.getTid());
					wh.biaoti.setText(wt.getTitle());
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
			apiparamsMap.put("method", "taobao.trades.sold.get");
			apiparamsMap.put("sign_method", "md5");
			apiparamsMap.put("app_key", Util.APPKEY);
			apiparamsMap.put("v", "2.0");
			apiparamsMap.put("status", "WAIT_BUYER_PAY");			
			apiparamsMap.put("session", Util.access_token); 
			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date());
			apiparamsMap.put("timestamp", timestamp);
			apiparamsMap.put("fields",
			"seller_nick,buyer_nick,title,type,created,sid,tid,seller_rate,buyer_rate,status,payment,discount_fee,adjust_fee,post_fee,total_fee,pay_time,end_time,modified,consign_time,buyer_obtain_point_fee,point_fee,real_point_fee,received_payment,commission_fee,pic_path,num_iid,num_iid,num,price,cod_fee,cod_status,shipping_type,receiver_name,receiver_state,receiver_city,receiver_district,receiver_address,receiver_zip,receiver_mobile,receiver_phone,orders.title,orders.pic_path,orders.price,orders.num,orders.iid,orders.num_iid,orders.sku_id,orders.refund_status,orders.status,orders.oid,orders.total_fee,orders.payment,orders.discount_fee,orders.adjust_fee,orders.sku_properties_name,orders.item_meal_name,orders.buyer_rate,orders.seller_rate,orders.outer_iid,orders.outer_sku_id,orders.refund_id,orders.seller_type");
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
