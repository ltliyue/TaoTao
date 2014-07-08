package com.example.activity;
//商品发货操作
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.taotao.R;
import com.example.utils.AsyncImageLoader;
import com.example.utils.AsyncImageLoader.ImageCallback;
import com.example.utils.Util;
import com.example.utils.APIUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

public class Shangpin_fahuo extends Activity {
	public static final String TAG = "Shangpin_fahuo";
	Dialog dialog;
	LinearLayout  exit,fanhui;
	 AlertDialog alertDialog;
	ImageView tupian;      //图片	
	TextView mingcheng;     //名字
	TextView jiaoyi;      //价格
	TextView maijia;    //数量
	TextView shijian;    //数量
	TextView danjia;    //数量
	TextView yunfei;    //数量
	TextView shuliang;    //数量
	TextView zongji;    //数量
	TextView dizhi;    //数量
	
	Button wuxu,xianxia;
	// 签名值
	String sign,feedSig;
	String result;
	String pic_path,title,tid,buyer_nick,created;
	String price,post_fee,num,payment,receiver_city,receiver_district,receiver_address;
	HttpClient client; 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shangpin_fahuo);
        MyApplication.getInstance().addActivity(this);
        initWidgt();        
        loadShangpin();		
        wuxu.setOnClickListener(new OnClickListener(){
           	public void onClick(View v) {
       		    OpenDialog();
           	}
           	  
           	});
        fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());
        xianxia.setOnClickListener(new OnClickListener(){
           	Intent intent = new Intent();
           	public void onClick(View v) {
           		intent.setClass(Shangpin_fahuo.this, Xianxiafahuo.class);
           		finish();
       		    startActivity(intent);
           	}
           	  
           	});

    }
    
    public void initWidgt(){ 
    	
    	tupian = (ImageView) this.findViewById(R.id.ding_tupian2);  	
    	mingcheng = (TextView) this.findViewById(R.id.ding_mingcheng2);
    	jiaoyi = (TextView) this.findViewById(R.id.ding_jiaoyi2);
    	maijia = (TextView) this.findViewById(R.id.ding_maijia2);
    	shijian= (TextView) this.findViewById(R.id.ding_shijian2);
    	danjia= (TextView) this.findViewById(R.id.ding_danjia2);
    	yunfei= (TextView) this.findViewById(R.id.ding_yunfei2);
    	shuliang= (TextView) this.findViewById(R.id.ding_shuliang2);
    	zongji= (TextView) this.findViewById(R.id.ding_zongji2);
    	dizhi= (TextView) this.findViewById(R.id.ding_dizhi2);
    	
    	wuxu=(Button) this.findViewById(R.id.wuxu2);
		xianxia=(Button) this.findViewById(R.id.xianxia2);
    	
		exit = (LinearLayout) findViewById(R.id.exitLayout);
		fanhui = (LinearLayout) findViewById(R.id.addLayout);
    } 
    class Exit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Shangpin_fahuo.this, MainActivity.class);								
		    startActivity(intent);
		   	}
	}

	class Fanhui implements OnClickListener {
 		@Override
 		public void onClick(View v) {  
 		
		    finish(); 		}
 	}


	public void OpenDialog() {
		alertDialog = new AlertDialog.Builder(Shangpin_fahuo.this)
				.setTitle("发货")
				.setMessage("您是否要使用无需物流发货？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    	Intent intent = new Intent();
            			intent.setClass(Shangpin_fahuo.this, Wuxuwuliu.class);	
            			Toast.makeText(Shangpin_fahuo.this, "发货成功~", Toast.LENGTH_LONG);
            			finish();
            		    startActivity(intent);
            		    
                    }
				})

				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				}).show();
	}


	public void loadShangpin() {
		new Thread() {
			@Override
			public void run() { 
				String testUrl = "http://gw.api.taobao.com/router/rest";				
				result = APIUtil.getResult(testUrl,getShangpin());
				Log.i(TAG, "loadshop----result =" + result);
				
				try {
					JSONObject data1 = new JSONObject(result);
					
					JSONObject trade_fullinfo_get_response = data1.getJSONObject("trade_fullinfo_get_response");					    					
					JSONObject trade = trade_fullinfo_get_response.getJSONObject("trade");
					pic_path = trade.getString("pic_path");
					tid=trade.getString("tid");
					buyer_nick=trade.getString("buyer_nick");
					created=trade.getString("created");
					price = trade.getString("price");
					post_fee=trade.getString("post_fee");
					num = trade.getString("num");
					payment=trade.getString("payment");
					receiver_city=trade.getString("receiver_city");
					receiver_district=trade.getString("receiver_district");
					receiver_address=trade.getString("receiver_address");
					
					JSONObject orders = trade.getJSONObject("orders");	
					JSONArray data3 = orders.getJSONArray("order");
					for (int j = 0; j < data3.length(); j++) {
						JSONObject order = data3.getJSONObject(j);
						title = order.getString("title");
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
			
			tupian.setImageResource(R.drawable.ic_android);
			AsyncImageLoader asyncImageLoader = new AsyncImageLoader();  // 异步获取图片
			Drawable cachedImage = asyncImageLoader.loadDrawable(pic_path, tupian, new ImageCallback() {
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
				jiaoyi.setText(tid);
				maijia.setText(buyer_nick);
				shijian.setText(created);
				danjia.setText(price);
				yunfei.setText(post_fee);
				shuliang.setText(num);
				zongji.setText(payment);
				dizhi.setText(receiver_city+receiver_district+receiver_address);
				
	}		
};
    	public static String getShangpin()
    	{
    		
            TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
            apiparamsMap.put("format", "json");
            apiparamsMap.put("method", "taobao.trade.fullinfo.get");
            apiparamsMap.put("sign_method","md5");
            apiparamsMap.put("app_key",Util.APPKEY);
            apiparamsMap.put("v", "2.0");
            apiparamsMap.put("session", Util.access_token); 
            
            apiparamsMap.put("tid",Util.tid);
            
            String timestamp =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            apiparamsMap.put("timestamp",timestamp);
            apiparamsMap.put("fields","seller_nick,buyer_nick,title,type,created,sid,tid,seller_rate,buyer_rate,status,payment,discount_fee,adjust_fee,post_fee,total_fee,pay_time,end_time,modified,consign_time,buyer_obtain_point_fee,point_fee,real_point_fee,received_payment,commission_fee,pic_path,num_iid,num_iid,num,price,cod_fee,cod_status,shipping_type,receiver_name,receiver_state,receiver_city,receiver_district,receiver_address,receiver_zip,receiver_mobile,receiver_phone,orders.title,orders.pic_path,orders.price,orders.num,orders.iid,orders.num_iid,orders.sku_id,orders.refund_status,orders.status,orders.oid,orders.total_fee,orders.payment,orders.discount_fee,orders.adjust_fee,orders.sku_properties_name,orders.item_meal_name,orders.buyer_rate,orders.seller_rate,orders.outer_iid,orders.outer_sku_id,orders.refund_id,orders.seller_type");//需要获取的字段
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

