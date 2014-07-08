package com.example.activity;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//查看评价
public class ChaKanpjiaActivity extends Activity {
	public static final String TAG = "ChaKanpjiaActivity";
	
	TextView mingcheng;     
	TextView riqi;      
	TextView leixing;    
	TextView xinxi;    
	TextView neirong;    
	String nick;
	String result;
	String created;
	String item_title;
	String content;
    String tid=Util.num_tid;
	// 签名值
	String sign,feedSig;
	String stresult;
	HttpClient client; 
	LinearLayout  exit,fanhui;
	 AlertDialog alertDialog;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.chakanpingjia);
               
        initWidgt();        
        loadShangpin();	
        fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());
    }
    
    public void initWidgt(){ 
    	
    	mingcheng = (TextView) this.findViewById(R.id.mingneirong);
    	riqi = (TextView) this.findViewById(R.id.creatneirong);
    	leixing = (TextView) this.findViewById(R.id.pingjialeixing);
    	xinxi= (TextView) this.findViewById(R.id.baobeixinxi);
    	neirong= (TextView) this.findViewById(R.id.pinglunneirong);
    	exit = (LinearLayout) findViewById(R.id.exitLayout);
		fanhui = (LinearLayout) findViewById(R.id.addLayout);
    } 
    class Exit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(ChaKanpjiaActivity.this, MainActivity.class);								
		    startActivity(intent);
		    }
	}

	class Fanhui implements OnClickListener {
 		@Override
 		public void onClick(View v) {  
 			ChaKanpjiaActivity.this.finish();
 		}
 	}

    public void loadShangpin() {
		new Thread() {
			@Override
			public void run() { 
				String testUrl = "http://gw.api.taobao.com/router/rest";				
				result = APIUtil.getResult(testUrl,getShangpin());
				
				try {
					JSONObject data1 = new JSONObject(result);
					
					JSONObject traderates_get_response = data1.getJSONObject("traderates_get_response");					    					
					JSONObject trade_rate = traderates_get_response.getJSONObject("trade_rate");
					nick = trade_rate.getString("nick");
					created = trade_rate.getString("created");
					result = trade_rate.getString("result");
					content = trade_rate.getString("content");
					item_title=trade_rate.getString("item_title");
				   					
					Log.i(TAG, "TEST==" );
					
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
			
				mingcheng.setText(nick);
				leixing.setText(result);
				riqi.setText(created);
				neirong.setText(content);
				xinxi.setText(item_title);
			
	}		
};
     
   public static String getShangpin()
   {	
       	  TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
		  apiparamsMap.put("format", "json");
		  apiparamsMap.put("method", "taobao.traderates.get");
		  apiparamsMap.put("sign_method", "md5");
		  apiparamsMap.put("app_key", Util.APPKEY);
		  apiparamsMap.put("v", "2.0");
		  apiparamsMap.put("session", Util.access_token); 
		  String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		  apiparamsMap.put("timestamp", timestamp);
		  apiparamsMap.put("rate_type", "get");
		  apiparamsMap.put("role","buyer");
		  apiparamsMap.put("tid",Util.num_tid);
          apiparamsMap
				.put("fields",
						"nick,result,created,item_title,content,");// 需要获取的字段

      
            
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

