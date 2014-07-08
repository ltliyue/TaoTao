package com.example.activity;
//来自买家的评价
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

import com.example.entity.PJia;
import com.example.utils.APIUtil;
import com.example.utils.Util;
import com.example.taotao.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Pingjia00 extends Activity {
	public static final String TAG = "Pingjia00";

	List<PJia>pijia;
	ListView pingjiaList;
	LinearLayout loadingLayout,exit,fanhui;
	 AlertDialog alertDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ping00);
        MyApplication.getInstance().addActivity(this);
        init();
        loadPingjiaList();
        fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());
}
    public void init() {
		loadingLayout = (LinearLayout) findViewById(R.id.loadingLayout);
		pingjiaList = (ListView) this.findViewById(R.id.pingjiaList);
		TextView tv= (TextView)findViewById(R.id.empty00); 
		pingjiaList.setEmptyView(tv); 
		exit = (LinearLayout) findViewById(R.id.exitLayout);
		fanhui = (LinearLayout) findViewById(R.id.addLayout);
	}
    class Exit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Pingjia00.this, MainActivity.class);								
		    startActivity(intent);
		    }
	}

	class Fanhui implements OnClickListener {
 		@Override
 		public void onClick(View v) {  
		    finish(); 		}
 	}

    /**
	 * 获取评价列表的线程
	 */
	public void loadPingjiaList() {
		new Thread() {
			@Override
			public void run() {
				loadingLayout.setVisibility(View.VISIBLE);
				String pingjiaUrl = "http://gw.api.taobao.com/router/rest";
				String stresult = APIUtil.getResult(pingjiaUrl,onPingjiaGet());
				JSONArray data;
				try {
					JSONObject data1 = new JSONObject(stresult);
					
					JSONObject traderates_get_response = data1.getJSONObject("traderates_get_response");						
					JSONObject trade_rates = traderates_get_response.getJSONObject("trade_rates");
					int total_results = traderates_get_response.getInt("total_results");
					if(total_results==0){
						Intent intent = new Intent();
						intent.setClass(Pingjia00.this, Error.class);
						finish();
					    startActivity(intent);
					}
					data = trade_rates.getJSONArray("trade_rate");
					Log.i(TAG, "loadOnSaleList---2 =");
					for (int i = 0; i < data.length(); i++) {
						JSONObject trade_rate = data.getJSONObject(i);
						
							String tid = trade_rate.getString("tid");
							String oid = trade_rate.getString("oid");
							String role = trade_rate.getString("role");
							String nick = trade_rate.getString("nick");
							String result = trade_rate.getString("result");
							String created = trade_rate.getString("created");
							String rated_nick = trade_rate.getString("rated_nick");
							String item_price = trade_rate.getString("item_price");
							String item_title = trade_rate.getString("item_title");
							String content = trade_rate.getString("content");
							Log.i(TAG, "loadOnSaleList---2 =");

						if (pijia == null) {
							pijia = new ArrayList<PJia>();
						}
						
						PJia oi = new PJia();
						
						oi.setTid(tid);
						oi.setOid(oid);
						oi.setRole(role);
						oi.setNick(nick);
						oi.setResult(result);
						oi.setCreated(created);
						oi.setRated_nick(rated_nick);
						oi.setItem_price(item_price);
						oi.setItem_title(item_title);
						oi.setContent(content);
						
						pijia.add(oi);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				Message message = new Message();
				handler.sendMessage(message);
			}
		}.start();

	}

	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			if (pijia != null) { 

				OnSaleAdapter adapter = new OnSaleAdapter();
				pingjiaList.setAdapter(adapter);
				loadingLayout.setVisibility(View.GONE); // 更新完UI，让总转的进度条视图消失

			}
		}
	};
	public class OnSaleAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			return pijia.size();
		}

		@Override
		public Object getItem(int position) {
			return pijia.get(position);
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
						R.layout.pijia_item, null);
			}

			WHolder wt = new WHolder(); // 消息实体
			wt.nick = (TextView) satusView.findViewById(R.id.nick);
			wt.content = (TextView) satusView.findViewById(R.id.content);
			wt.created = (TextView) satusView.findViewById(R.id.created);
			PJia wb = pijia.get(position);
			if (wb != null) {
				satusView.setTag(wb.getTid());
				wt.content.setText(wb.getContent());
				wt.created.setText(wb.getCreated() );
				wt.nick.setText(wb.getNick());
				
			}
			return satusView;
		}

	}

	public static class WHolder {
		 TextView nick;
		 TextView content;
		 TextView created;
	}

	/**
	 * 拼装请求的参数并按拼音排序
	 * 
	 * @return
	 */
	public String onPingjiaGet() {
		TreeMap<String, String> apiparamsMap00 = new TreeMap<String, String>();
		apiparamsMap00.put("format", "json");
		apiparamsMap00.put("method", "taobao.traderates.get");
		apiparamsMap00.put("sign_method", "md5");
		apiparamsMap00.put("app_key", Util.APPKEY);
		apiparamsMap00 .put("v", "2.0");
		apiparamsMap00.put("session", Util.access_token); 
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		apiparamsMap00.put("timestamp", timestamp);
		apiparamsMap00.put("rate_type", "get");
		apiparamsMap00.put("role","buyer");
		apiparamsMap00
				.put("fields",
		"tid,oid,role,nick,result,created,rated_nick,item_title,item_price,content,reply,num_iid");// 需要获取的字段

		// 生成签名
		String sign = APIUtil.md5Signature(apiparamsMap00, Util.SECRET);
		apiparamsMap00.put("sign", sign);
		StringBuilder param = new StringBuilder();
		for (Iterator<Map.Entry<String, String>> it = apiparamsMap00.entrySet()
				.iterator(); it.hasNext();) {
			Map.Entry<String, String> e = it.next();
			param.append("&").append(e.getKey()).append("=")
					.append(e.getValue());
		}

		Log.i(TAG, "param.toString().substring(1) = "+ param.toString().substring(1));
		return param.toString().substring(1);
	}	        
}



