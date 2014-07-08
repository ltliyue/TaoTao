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

//退款信息
public class Tuikuan extends Activity{
	public static final String TAG = "Tuikuan";
	
	List<WuliuItem> ois;
	ListView tuikuan;
	LinearLayout loadingLayout,exit,fanhui;
	 AlertDialog alertDialog;

	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.tuikuan);
	        MyApplication.getInstance().addActivity(this);
	        init();
			loadOnFaList();
			fanhui.setOnClickListener(new Fanhui());
			exit.setOnClickListener(new Exit());
	       }

		public void init() {
			loadingLayout = (LinearLayout) findViewById(R.id.loadingLayout01);
			tuikuan = (ListView) this.findViewById(R.id.tuikuan);
			exit = (LinearLayout) findViewById(R.id.exitLayout);
			fanhui = (LinearLayout) findViewById(R.id.addLayout);
		}
		class Exit implements OnClickListener {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Tuikuan.this, MainActivity.class);								
			    startActivity(intent);
			    }
		}

		class Fanhui implements OnClickListener {
	 		@Override
	 		public void onClick(View v) {  
	 			
	 			Tuikuan.this.finish();
	 		}
	 	}

		/**
		 * 获取列表的线程
		 */
		public void loadOnFaList() {
			new Thread() {
				@Override
				public void run() {
					loadingLayout.setVisibility(View.VISIBLE);
					String testUrl = "http://gw.api.taobao.-]=/router/rest";				
					String result = APIUtil.getResult(testUrl,onFaGet());
					Log.i(TAG, "loadOnSaleList--0--result =");
					
					
					try {
						JSONObject data1 = new JSONObject(result);
						
						JSONObject refunds_receive_get_response = data1.getJSONObject("refunds_receive_get_response");						
						int total_results = refunds_receive_get_response.getInt("total_results");
						JSONObject refunds = refunds_receive_get_response.getJSONObject("refunds");
						JSONArray data2 = refunds.getJSONArray("refund");
						
						if(total_results==0){
							Intent intent = new Intent();
							intent.setClass(Tuikuan.this, Error.class);								
						    startActivity(intent);
						}
						
						for (int i = 0; i < data2.length(); i++) {
							
							JSONObject refund = data2.getJSONObject(i);
								String title = refund.getString("title");
								String desc = refund.getString("desc");
								String total_fee = refund.getString("total_fee");
								String buyer_nick = refund.getString("buyer_nick");
								String modified = refund.getString("modified");
								String oid = refund.getString("oid");
								
								
							if (ois == null) {
								ois = new ArrayList<WuliuItem>();
							}
							WuliuItem osi = new WuliuItem();
							
							osi.setTitle(title);
							osi.setBuyer_nick(buyer_nick);
							osi.setModified(modified);
							osi.setTotal_fee(total_fee);
							osi.setDesc(desc);
							osi.setOid(oid);
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
					tuikuan.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View view,int arg2, long arg3) {
							Object obj = view.getTag();
							if (obj != null) {
								Intent intent = new Intent();
								String oid = obj.toString();
								Log.i(TAG, "load_id----result =" + oid);
								Util.oid=oid;
								intent.setClass(Tuikuan.this, Shangpin_fahuo.class);								
							    startActivity(intent);
							    finish();
							}
						}

					});
					OnSaleAdapter adapter = new OnSaleAdapter(Tuikuan.this);
					tuikuan.setAdapter(adapter);

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
					satusView = LayoutInflater.from(getApplication()).inflate(R.layout.tuikuan_item, null);
				}
				WeiBoHolder wh = new WeiBoHolder(); // 消息实体
				wh.biaoti = (TextView) satusView.findViewById(R.id.tui_biaoti);
				wh.qixian = (TextView) satusView.findViewById(R.id.tui_shijian);
				wh.jiage = (TextView) satusView.findViewById(R.id.tui_jiage);
				wh.yuanyin = (TextView) satusView.findViewById(R.id.tui_yuanyin);
				WuliuItem wt = ois.get(position);
				if (wt != null) {
					satusView.setTag(wt.getOid());
					wh.biaoti.setText(wt.getTitle());
					wh.qixian.setText("退款时间："+wt.getModified());
					wh.jiage.setText("买家用户名："+wt.getBuyer_nick());
					wh.yuanyin.setText("退款原因："+wt.getDesc());
				}
				
				return satusView;
			}

		}

		public static class WeiBoHolder {
			public TextView jiage;
			public TextView biaoti;
			public TextView qixian;
			public TextView yuanyin;
		}

		
		/**
		 * 拼装请求的参数并按拼音排序
		 * 
		 * @return
		 */
		public String onFaGet() {
			TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
			apiparamsMap.put("format", "json");
			apiparamsMap.put("method", "taobao.refunds.receive.get ");
			apiparamsMap.put("sign_method", "md5");
			apiparamsMap.put("app_key", Util.APPKEY);
			apiparamsMap.put("v", "2.0");
			apiparamsMap.put("session", Util.access_token); 
			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date());
			apiparamsMap.put("timestamp", timestamp);
			apiparamsMap.put("fields",
					"refund_id, tid, title, buyer_nick, seller_nick, total_fee, status, created, refund_fee, oid, good_status, company_name, sid, payment, reason, desc, has_good_return, modified, order_status");
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
