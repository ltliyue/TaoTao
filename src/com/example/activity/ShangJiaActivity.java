package com.example.activity;
//商品上架
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.utils.APIUtil;
import com.example.utils.Util;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;


public class ShangJiaActivity extends Activity {
	public static final String TAG = "ShangJiaActivity";
	String sign;
	String result;
	LinearLayout loadingLayout,exit,fanhui;
	 AlertDialog alertDialog;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        loadList();
        
    }
  
	class Fanhui implements OnClickListener {
		@Override
		public void onClick(View v) {  
			
			ShangJiaActivity.this.finish();
		}
	}
	public void loadList() {
		sign = getParams(); // 获得API签名
		publishFeedThread();
	}


	public String getParams() 
	{

	       TreeMap<String, String> apiparamsMap01 = new TreeMap<String, String>();
	       String num=Util.num_iid;
	      apiparamsMap01.put("format", "json");
	      apiparamsMap01.put("method", "taobao.item.update.listing");
	      apiparamsMap01.put("sign_method","md5");
	      apiparamsMap01.put("app_key",Util.APPKEY);
	      apiparamsMap01.put("v", "2.0");
	      apiparamsMap01.put("session", Util.access_token);
	      String timestamp01 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	      apiparamsMap01.put("timestamp",timestamp01);
	      apiparamsMap01.put("num_iid",num);
	      apiparamsMap01.put("num","3");
	      //生成签名
	      String sign = APIUtil.md5Signature(apiparamsMap01,Util.SECRET);

      TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
            
	   apiparamsMap.put("format", "json");
       apiparamsMap.put("method", "taobao.item.update.listing");
       apiparamsMap.put("sign_method","md5");
       apiparamsMap.put("app_key",Util.APPKEY);
       apiparamsMap.put("v", "2.0");
       apiparamsMap.put("session", Util.access_token);
       String timestamp =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
       apiparamsMap.put("timestamp",timestamp);
       apiparamsMap.put("num_iid",num);
       apiparamsMap.put("num","3");
       apiparamsMap.put("sign", sign);
      
      StringBuilder param = new StringBuilder();
     
       for 
       (
      	Iterator<Map.Entry<String, String>> it = apiparamsMap.entrySet().iterator(); it.hasNext();
       ) {
          Map.Entry<String, String> e = it.next();
          param.append("&").append(e.getKey()).append("=").append(e.getValue());
       	}
       Log.i(TAG, "TEST==" +param.toString().substring(1) );
       return param.toString().substring(1);
      
   }
	public void publishFeedThread() {
		new Thread() {
			@Override
			public void run() {
				int what=1;
				//发送请求得到数据
					String testUrl = "http://gw.api.taobao.com/router/rest";				
					result = APIUtil.getResult(testUrl,getParams());
					
					//解析得到的数据
					try {
						JSONObject data= new JSONObject(result);
						
						JSONObject item_update_listing_response = data.getJSONObject("item_update_listing_response");					    					
						JSONObject item = item_update_listing_response.getJSONObject("item");
						String num_iid = item.getString("num_iid");
						String modified=item.getString("modified");
						
						Log.i(TAG, "TEST==" +modified );
						Log.i(TAG, "TEST==" + num_iid);	
					
					} catch (JSONException e) {
						e.printStackTrace();
					}
					Message msg = new Message();   
		            msg.what = what;
		            Bundle bundle = new Bundle();     
	                bundle.putString("result", result);  //往Bundle中存放数据    
	                msg.setData(bundle);   		//mes利用Bundle传递数据    
	                handler.sendMessage(msg);	//用activity中的handler发送消息    
	 
				}
			}.start();
			
		}
		 
		/**
		 * 更新UI线程ListView
		 */
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				
		            	Toast.makeText(ShangJiaActivity.this, "商品已成功上架,转到出售中宝贝", Toast.LENGTH_LONG).show();
		            	Intent intent = new Intent();
		            	intent.setClass(ShangJiaActivity.this, Baobei0.class);
		       		    startActivity(intent);
		       		 finish();
			}
		};

}


