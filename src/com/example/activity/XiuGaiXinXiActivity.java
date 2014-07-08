package com.example.activity;
//修改商品信息
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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class XiuGaiXinXiActivity  extends Activity{

	public static final String TAG = "XiuGaiXinXiActivity";
	LinearLayout exit,fanhui;
	EditText mingcheng;     //名字
	EditText jiage;      //价格
	EditText shuliang;    //数量
	EditText qixian;   
	EditText ping;    
	EditText kuai;    
	EditText ems;    
	Button button;
	String name;
	String price;
	String num;
	String date;
	String pingyou;
	String kuaidi;
	String youems;
	String result;
	String sign;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baobeixiugai);
        MyApplication.getInstance().addActivity(this);

        initWidgt();   
        button.setOnClickListener(new Publish());
        fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());
	}
	 public void initWidgt(){ 
	    	button=(Button)this.findViewById(R.id.xiugaibutton); 	
	    	jiage = (EditText) this.findViewById(R.id.xiugaijige);
	    	shuliang = (EditText) this.findViewById(R.id.xiugaishuliang);
	    	ping= (EditText) this.findViewById(R.id.xiugaipingyou);
	    	kuai= (EditText) this.findViewById(R.id.xiugaikuaidi);
	    	ems= (EditText) this.findViewById(R.id.xiugaiems);
	    	
	    	exit = (LinearLayout) findViewById(R.id.exitLayout);
			fanhui = (LinearLayout) findViewById(R.id.addLayout);
	    } 
	 class Exit implements OnClickListener {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(XiuGaiXinXiActivity.this, MainActivity.class);
				finish();
			    startActivity(intent);
			    }
		}
		class Fanhui implements OnClickListener {
	 		@Override
	 		public void onClick(View v) {  
	 			
	 			XiuGaiXinXiActivity.this.finish();
	 		}
	 	}

	 class Publish implements OnClickListener {
			@Override
			public void onClick(View v) {
				price = jiage.getText().toString().trim();
				num = shuliang.getText().toString().trim();
				pingyou = ping.getText().toString().trim();
				kuaidi = kuai.getText().toString().trim();
				youems = ems.getText().toString().trim();				
				Log.i(TAG, " 1 "+price+pingyou+"  2 "+youems+"3 "+kuaidi);				
				loadShangpin();
			}
		}
	 
		
	 public void loadShangpin() {
			new Thread() {
				@Override
				public void run() { 
					String testUrl = "http://gw.api.taobao.com/router/rest";				
					result = APIUtil.getResult(testUrl,onCangGet());
					Log.i(TAG, "loadshop----result =" + result);
					
					try {
						JSONObject data1 = new JSONObject(result);

						JSONObject item_update_response = data1.getJSONObject("item_update_response");					    					
						JSONObject item = item_update_response.getJSONObject("item");
						String num_iid = item.getString("num_iid");
						String modified = item.getString("modified");
						String price = item.getString("price");
					
						Log.i(TAG, "TEST==="+ num_iid+"   "+modified+"    "+price);
						
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
				Toast.makeText(XiuGaiXinXiActivity.this, "信息修改成功", Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(XiuGaiXinXiActivity.this, ShangpinActivity.class);
    		    finish();
    		    startActivity(intent);
    		    
	       }
		};
	    	public  String getShangpin()
	    	{
	    		 String b0 = price;
	    		 String c0 =num;  
	    		 String d0 =pingyou; 
	    		 String g0 =kuaidi; 
	    		 String f0 =youems; 
	       	 Log.i(TAG, "testUserGet----");
	       	 Log.i(TAG, Util.access_token);
	            TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
	            apiparamsMap.put("format", "json");
	            apiparamsMap.put("method", "taobao.item.update");
	            apiparamsMap.put("sign_method","md5");
	            apiparamsMap.put("app_key",Util.APPKEY);
	            apiparamsMap.put("session",Util.access_token);
	            apiparamsMap.put("v", "2.0");
	            apiparamsMap.put("num_iid",Util.num_iid);
	            String timestamp =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	            apiparamsMap.put("timestamp",timestamp);
	            apiparamsMap.put("price",b0);
	            apiparamsMap.put("num",c0);
	            apiparamsMap.put("post_fee",d0);
	            apiparamsMap.put("express_fee",g0);
	            apiparamsMap.put("ems_fee",f0);
	            
	            //生成签名
	            String sign = APIUtil.md5Signature(apiparamsMap,Util.SECRET);
	           
	    	      TreeMap<String, String> apiparamsMap01 = new TreeMap<String, String>();
		    		 
	    	      apiparamsMap01.put("format", "json");
	    	      apiparamsMap01.put("method", "taobao.item.update ");
	    	      apiparamsMap01.put("sign_method","md5");
	    	      apiparamsMap01.put("app_key",Util.APPKEY);
	    	      apiparamsMap01.put("session", Util.access_token);
	    	      apiparamsMap01.put("v", "2.0");
	    	      apiparamsMap01.put("num_iid",Util.num_iid);
	    	      String timestamp01 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	    	      apiparamsMap01.put("timestamp",timestamp01);
		          apiparamsMap01.put("price",b0);
		          apiparamsMap01.put("num",c0);
		          apiparamsMap01.put("post_fee",d0);
		          apiparamsMap01.put("express_fee",g0);
		          apiparamsMap01.put("ems_fee",f0);
		          
		          apiparamsMap01.put("sign", sign);
		          StringBuilder param = new StringBuilder();
		          
		          for 
		          (
		         	Iterator<Map.Entry<String, String>> it = apiparamsMap01.entrySet().iterator(); it.hasNext();
		          ) {
		             Map.Entry<String, String> e = it.next();
		             param.append("&").append(e.getKey()).append("=").append(e.getValue());
		          	}
		          Log.i(TAG, "TEST==" +param.toString().substring(1) );
		          return param.toString().substring(1);
	    	      
	       }
	    	
	    	public String onCangGet() {
	    		String bb = price;
	    		 String cc =num;  
	    		 String dd =pingyou; 
	    		 String gg =kuaidi; 
	    		 String ff =youems; 
				TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
				apiparamsMap.put("format", "json");
				apiparamsMap.put("method", "taobao.item.update");
				apiparamsMap.put("sign_method", "md5");
				apiparamsMap.put("app_key", Util.APPKEY);
				apiparamsMap.put("v", "2.0");
				apiparamsMap.put("session", Util.access_token); 
				String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(new Date());
				apiparamsMap.put("timestamp", timestamp);
				apiparamsMap.put("num_iid",Util.num_iid);
				apiparamsMap.put("price",bb);
	            apiparamsMap.put("num",cc);
	            apiparamsMap.put("post_fee",dd);
	            apiparamsMap.put("express_fee",gg);
	            apiparamsMap.put("ems_fee",ff);
	            
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

	  

