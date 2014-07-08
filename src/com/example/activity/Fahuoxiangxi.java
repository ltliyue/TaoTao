package com.example.activity;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.client.HttpClient;
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
//发货详细信息
public class Fahuoxiangxi extends Activity {
	public static final String TAG = "Fahuoxiangxi";
	LinearLayout  exit,fanhui;
	 AlertDialog alertDialog;
	URL url = null;
	
	TextView yonghuming;     //名字
	TextView dizhi;      //价格
	TextView youbian;    //数量
	TextView shouhuoren;    //数量

	// 签名值
	String sign;
	String result;
	String buyer_nick,state,zip;
	HttpClient client; 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fahuoxinxi);
        
        MyApplication.getInstance().addActivity(this);       
        initWidgt();        
        loadShangpin();
        fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());
    }
    
    public void initWidgt(){ 
    	
    	yonghuming = (TextView) this.findViewById(R.id.yonghuming);
    	dizhi = (TextView) this.findViewById(R.id.dizhi);
    	youbian = (TextView) this.findViewById(R.id.youbian);
    	shouhuoren= (TextView) this.findViewById(R.id.shouhuoren);
    	exit = (LinearLayout) findViewById(R.id.addLayout);
		fanhui = (LinearLayout) findViewById(R.id.exitLayout);
    } 

    class Exit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Fahuoxiangxi.this, MainActivity.class);								
		    startActivity(intent);
		    }
	}

	class Fanhui implements OnClickListener {
 		@Override
 		public void onClick(View v) {  
		    finish(); 		
		    }
 	}
	public void loadShangpin() {
		new Thread() {
			@Override
			public void run() { 
				String testUrl = "http://gw.api.taobao.com/router/rest";				
				
				try {
					JSONObject data1 = new JSONObject(result);
					
					JSONObject logistics_orders_detail_get_response = data1.getJSONObject("logistics_orders_detail_get_response");					    					
					JSONObject shippings = logistics_orders_detail_get_response.getJSONObject("shippings");
					JSONObject shipping = shippings.getJSONObject("shipping");
					
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
	}		
};
    	public static String getShangpin()
    	{
    		
            TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
            apiparamsMap.put("format", "json");
            apiparamsMap.put("method", "taobao.item.get");
            apiparamsMap.put("sign_method","md5");
            apiparamsMap.put("app_key",Util.APPKEY);
            apiparamsMap.put("v", "2.0");
            apiparamsMap.put("num_iid",Util.num_iid);
            String timestamp =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            apiparamsMap.put("timestamp",timestamp);
            apiparamsMap.put("fields","num_iid,title,price,cid,pic_url,num,valid_thru, post_fee,express_fee,ems_fee,outer_id");//需要获取的字段
            apiparamsMap.put("nick", "ltliyue");
            //生成签名
            String sign = APIUtil.md5Signature(apiparamsMap,Util.SECRET);
            apiparamsMap.put("sign", sign);
            StringBuilder param = new StringBuilder();
            for 
            (
           	Iterator<Map.Entry<String, String>> it = apiparamsMap.entrySet().iterator(); it.hasNext();
            ) {
               Map.Entry<String, String> e = it.next();
               param.append("&").append(e.getKey()).append("=").append(e.getValue());
            	}
            return param.toString().substring(1);
        }

	}

