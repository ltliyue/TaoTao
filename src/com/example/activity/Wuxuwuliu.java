package com.example.activity;
//无需物流发货
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.net.URL;

import com.example.utils.Util;
import com.example.utils.APIUtil;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;


public class Wuxuwuliu extends Activity {
	public static final String TAG = "Wuxuwuliu";
	 AlertDialog alertDialog;
	URL url = null;	
	Button button;  
	// 签名值
	String sign,feedSig;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getShop();
        
        String testUrl = "http://gw.api.taobao.com/router/rest";				
		String result = APIUtil.getResult(testUrl,getShop());
		
		Intent intent = new Intent();
		intent.setClass(Wuxuwuliu.this, Fahuo1.class);								
	    startActivity(intent);
		finish();
    }


    public static String getShop(){
    	
            TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
            apiparamsMap.put("format", "json");
            apiparamsMap.put("method", "taobao.logistics.dummy.send");
            apiparamsMap.put("sign_method","md5");
            apiparamsMap.put("app_key",Util.APPKEY);
            apiparamsMap.put("session", Util.access_token);
            apiparamsMap.put("tid",Util.tid);
            apiparamsMap.put("v", "2.0");
            String timestamp =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            apiparamsMap.put("timestamp",timestamp);
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

