package com.example.activity;
//店铺信用
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.taotao.R;
import com.example.utils.APIUtil;
import com.example.utils.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PingJiaXinYong  extends Activity {
	public static final String TAG = "PingJiaXinYong";
	TextView shangpin;
	TextView fuwu;
	TextView fahuo;
	String item_score,service_score,delivery_score;
	String sign;
	LinearLayout exit,fanhui;
	 AlertDialog alertDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pingxinyong);
        MyApplication.getInstance().addActivity(this);
        init();
        sign=getParams(); 
        loadPingjiaList();
        fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());
        
}
    public void init() {
		shangpin= (TextView) this.findViewById(R.id.shangpin);
		fuwu= (TextView) this.findViewById(R.id.fuwu);
		fahuo= (TextView) this.findViewById(R.id.fahuo);
		exit = (LinearLayout) findViewById(R.id.exitLayout);
		fanhui = (LinearLayout) findViewById(R.id.addLayout);
	}
    class Exit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(PingJiaXinYong.this, MainActivity.class);								
		    startActivity(intent);
		    }
	}

	class Fanhui implements OnClickListener {
 		@Override
 		public void onClick(View v) { 
		    finish(); 		}
 	}

    /**
   	 * 获取列表的线程
   	 */
   	public void loadPingjiaList() {
   		new Thread() {
   			@Override
   			public void run() {
   				String pingjiaUrl = "http://gw.api.taobao.com/router/rest";
   				Log.i(TAG, "result =" );
   				String stresult = APIUtil.getResult(pingjiaUrl, getParams());
   				Log.i(TAG, "loadOnSaleList----result =" + stresult);
   		
   				try {
   					
   					JSONObject data1 = new JSONObject(stresult);
   					
   					JSONObject shop_get_response = data1.getJSONObject("shop_get_response");						
   					JSONObject shop = shop_get_response.getJSONObject("shop");
   					JSONObject shop_score = shop.getJSONObject("shop_score");
   					item_score = shop_score.getString("item_score");
   					service_score = shop_score.getString("service_score");
   					delivery_score = shop_score.getString("delivery_score");
   				
 						
   					
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
   			
   				
   				shangpin.setText(item_score);
   				fuwu.setText(service_score);
   				fahuo.setText(delivery_score);
   			
   		}
   	};

  
   	/**
   	 * 拼装请求的参数并按拼音排序
   	 * 
   	 * @return
   	 */
   	public static String  getParams() {
   		TreeMap<String, String> apiparamsMap00 = new TreeMap<String, String>();
   		apiparamsMap00.put("format", "json");
   		apiparamsMap00.put("method", "taobao.shop.get");
   		apiparamsMap00.put("sign_method", "md5");
   		apiparamsMap00.put("app_key", Util.APPKEY);
   		apiparamsMap00 .put("v", "2.0");
   		apiparamsMap00.put("session", Util.access_token);
   		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
   				.format(new Date());
   		apiparamsMap00.put("timestamp", timestamp);
   		apiparamsMap00.put("nick", "ltliyue");
   	
   		apiparamsMap00
   				.put("fields",
   						"shop_score,title,nick,desc,bulletin");// 需要获取的字段

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