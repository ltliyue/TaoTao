package com.example.activity;
//线下发货
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.net.URL;

import com.example.taotao.R;
import com.example.utils.Util;
import com.example.utils.APIUtil;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Xianxiafahuo extends Activity {
	public static final String TAG = "Xianxiafahuo";
	URL url = null;	
	Button ok,not;
	EditText gongsii,danhao;  
	String feedgongsi;
	String feeddanhao;
	TextView daima;
	// 签名值
	String sign,feedSig;
	LinearLayout exit,fanhui;
	 AlertDialog alertDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xianxia);
        MyApplication.getInstance().addActivity(this);

        initWidgt(); 
        fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());
		
        ok.setOnClickListener(new Ok());
		not.setOnClickListener(new Not());
		daima.setOnClickListener(new Daima());
		
	    class Publish implements OnClickListener {
			@Override
			public void onClick(View v) {
				gongsii.setText(Util.codeo);
				feedgongsi = gongsii.getText().toString().trim();
				feeddanhao = danhao.getText().toString().trim();		
				Log.i(TAG, feedgongsi+ " 1 "+feeddanhao+"  2 ");
			}
		}
    }
    private void initWidgt() {
		
    	ok=(Button) this.findViewById(R.id.ok);
    	not=(Button) this.findViewById(R.id.not);
    	gongsii=(EditText) this.findViewById(R.id.gongsii);
    	danhao=(EditText) this.findViewById(R.id.danhao);
    	daima=(TextView) this.findViewById(R.id.daima);	
    	
    	exit = (LinearLayout) findViewById(R.id.exitLayout);
		fanhui = (LinearLayout) findViewById(R.id.addLayout);
	}
    
    class Exit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Xianxiafahuo.this, MainActivity.class);								
		    startActivity(intent);
		    }
	}

	class Fanhui implements OnClickListener {
 		@Override
 		public void onClick(View v) {  
 			
 			Xianxiafahuo.this.finish();
 		}
 	}

   
	class Ok implements OnClickListener {
		@Override
		public void onClick(View v) {  	
			
			String testUrl = "http://gw.api.taobao.com/router/rest";
			Toast.makeText(Xianxiafahuo.this, "发货成功~", Toast.LENGTH_LONG);					
			String result = APIUtil.getResult(testUrl,getShop());
			Intent intent = new Intent();
			intent.setClass(Xianxiafahuo.this, FahuoActivity.class);
			
			finish();
		    startActivity(intent);
		}
	}
	class Not implements OnClickListener {
		@Override
		public void onClick(View v) { 
			finish();			
		}
	}
	class Daima implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Xianxiafahuo.this, Wuliugongsi.class);	
		    startActivity(intent);
		}
	}
	public String getShop(){
		String a = feedgongsi;
		String b =feeddanhao;  
    	
            TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
            apiparamsMap.put("format", "json");
            apiparamsMap.put("method", "taobao.logistics.offline.send");
            apiparamsMap.put("sign_method","md5");
            apiparamsMap.put("app_key",Util.APPKEY);
            apiparamsMap.put("session", Util.access_token);
            apiparamsMap.put("tid",Util.tid);
            apiparamsMap.put("v", "2.0");
            String timestamp =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            apiparamsMap.put("timestamp",timestamp);
            
            apiparamsMap.put("company_code",a);
            apiparamsMap.put("out_sid",b);
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

