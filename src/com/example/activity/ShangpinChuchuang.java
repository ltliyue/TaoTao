package com.example.activity;
//橱窗中商品
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

import com.example.utils.AsyncImageLoader;
import com.example.utils.AsyncImageLoader.ImageCallback;
import com.example.utils.Util;
import com.example.utils.APIUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShangpinChuchuang extends Activity {
	public static final String TAG = "ShangpinChuchuang";
	LinearLayout  exit,fanhui;
	 AlertDialog alertDialog;
	URL url = null;
	
	ImageView tupian;      //图片	
	TextView mingcheng;     //名字
	TextView jiage;      //价格
	TextView shuliang;    //数量
	TextView qixian;    //数量
	TextView ping;    //数量
	TextView kuai;    //数量
	TextView ems;    //数量
	TextView bianma;    //数量
	Button button;
	// 签名值
	String sign,feedSig;
	// 获取数据后的json字符串值
	String result;
	String pic_url,title,price,num;
	String valid_thru, post_fee,express_fee,ems_fee,outer_id;
	HttpClient client; 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shangpinchuchuang);
        MyApplication.getInstance().addActivity(this);
        initWidgt();   
        loadShangpin();		
        fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());
        button.setOnClickListener(new OnClickListener(){
           	Intent intent = new Intent();
           	public void onClick(View v) {
           		intent.setClass(ShangpinChuchuang.this, QuxiaoChuChuangActivity.class);
           		finish();
           		startActivity(intent);
           	}
           	  
           	});
        
    }
    
    public void initWidgt(){ 
    	button=(Button)this.findViewById(R.id.quxiaochuchuang);
    	tupian = (ImageView) this.findViewById(R.id.tupianqu);  	
    	mingcheng = (TextView) this.findViewById(R.id.mingchengqu);
    	jiage = (TextView) this.findViewById(R.id.jiagequ);
    	shuliang = (TextView) this.findViewById(R.id.shuliangqu);
    	qixian= (TextView) this.findViewById(R.id.qixianqu);
    	ping= (TextView) this.findViewById(R.id.pingqu);
    	kuai= (TextView) this.findViewById(R.id.kuaiqu);
    	ems= (TextView) this.findViewById(R.id.emsqu);
    	bianma= (TextView) this.findViewById(R.id.bianmaqu);
    	
    	exit = (LinearLayout) findViewById(R.id.exitLayout);
		fanhui = (LinearLayout) findViewById(R.id.addLayout);
    } 
    class Exit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(ShangpinChuchuang.this, MainActivity.class);								
		    startActivity(intent);
		    }
	}

	class Fanhui implements OnClickListener {
 		@Override
 		public void onClick(View v) {  
 			
 			ShangpinChuchuang.this.finish();
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

					JSONObject item_get_response = data1.getJSONObject("item_get_response");					    					
					JSONObject item = item_get_response.getJSONObject("item");
					pic_url = item.getString("pic_url");
					title = item.getString("title");
					price = item.getString("price");
					num = item.getString("num");
					valid_thru = item.getString("valid_thru");
					post_fee = item.getString("post_fee");
					express_fee = item.getString("express_fee");
					ems_fee = item.getString("ems_fee");
					outer_id = item.getString("outer_id");
				   					
					Log.i(TAG, "TEST==========" + title);
					
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
			
			tupian.setImageResource(R.drawable.ic_android);
			AsyncImageLoader asyncImageLoader = new AsyncImageLoader();  // 异步获取图片
			Drawable cachedImage = asyncImageLoader.loadDrawable(pic_url, tupian, new ImageCallback() {
				@Override   // 这里是重写了回调接口
				public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl) {
					imageView.setImageDrawable(imageDrawable);
				}
			});
	
			if (cachedImage == null) {
				// 如果没有图片，就以一个载入的图片代替显示
				tupian.setImageResource(R.drawable.ic_android);
			} else {
				// 如果有图片，就载入图片
				tupian.setImageDrawable(cachedImage);
			}
				mingcheng.setText(title);
				jiage.setText(price);
				shuliang.setText(num);
				qixian.setText(valid_thru);
				ping.setText(post_fee);
				kuai.setText(express_fee);
				ems.setText(ems_fee);
				bianma.setText(outer_id);
	}		
};
   
    	public static String getShangpin()
    	{
    		
       	 Log.i(TAG, "testUserGet----");
            TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
            apiparamsMap.put("format", "json");
            apiparamsMap.put("method", "taobao.item.get");
            apiparamsMap.put("sign_method","md5");
            apiparamsMap.put("app_key",Util.APPKEY);
            apiparamsMap.put("session", Util.access_token); 
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

