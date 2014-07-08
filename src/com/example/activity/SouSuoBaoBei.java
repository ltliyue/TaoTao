package com.example.activity;
//搜索宝贝
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
import com.example.entity.SouSuo;
import com.example.taotao.R;
import com.example.utils.APIUtil;
import com.example.utils.AsyncImageLoader;
import com.example.utils.Util;
import com.example.utils.AsyncImageLoader.ImageCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SouSuoBaoBei extends Activity{
	public static final String TAG = "ShouSuoBaoBei";
	List<SouSuo>baobei;
	ListView baobeiList;
	LinearLayout loadingLayout,exit,fanhui;
	 AlertDialog alertDialog;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sousuobaobei);
        MyApplication.getInstance().addActivity(this);
        init();
		loadOnSaleList();
		fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());
       }

	public void init() {
		loadingLayout = (LinearLayout) findViewById(R.id.loadingLayoutbaobei);
		baobeiList = (ListView) this.findViewById(R.id.baobeilist);
		exit = (LinearLayout) findViewById(R.id.exitLayout);
		fanhui = (LinearLayout) findViewById(R.id.addLayout);
	}
	
	class Exit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(SouSuoBaoBei.this, MainActivity.class);								
		    startActivity(intent);
		    }
	}

	class Fanhui implements OnClickListener {
 		@Override
 		public void onClick(View v) {  
 			
 			SouSuoBaoBei.this.finish();
 		}
 	}
 		
	public void loadOnSaleList() {
		new Thread() {
			@Override
			public void run() {
				loadingLayout.setVisibility(View.VISIBLE);
				String testUrl = "http://gw.api.taobao.com/router/rest";				
				String result = APIUtil.getResult(testUrl,onSaleGet());
				
				JSONArray data;
				try {
					JSONObject data1 = new JSONObject(result);
					
					JSONObject products_search_response = data1.getJSONObject("products_search_response");						
					int total_results = products_search_response.getInt("total_results");
	
					if(total_results==0){
						Intent intent = new Intent();
						intent.setClass(SouSuoBaoBei.this, Error.class);								
					    startActivity(intent);
					}
					JSONObject products = products_search_response.getJSONObject("products");
					data = products.getJSONArray("product");
					
					for (int i = 0; i < data.length(); i++) {
						JSONObject item = data.getJSONObject(i);
							String product_id = item.getString("product_id");
							String name = item.getString("name");
							String cid = item.getString("cid");
							String props = item.getString("props");
							String tsc = item.getString("tsc");
							String pic_url = item.getString("pic_url");
							String price = item.getString("price");
							
						if (baobei == null) {
							baobei = new ArrayList<SouSuo>();
						}
						
						SouSuo bao = new SouSuo();
						
						bao.setProduct_id(product_id);
						bao.setName(name);
						bao.setCid(cid);
						bao.setProps(props);
						bao.setTsc(tsc);
						bao.setPic_url(pic_url);
						bao.setPrice(price);

						baobei.add(bao);
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
			OnSaleAdapter adapter = new OnSaleAdapter(SouSuoBaoBei.this);
			baobeiList.setAdapter(adapter);
			loadingLayout.setVisibility(View.GONE); // 更新完UI，让总转的进度条视图消失
	
       }
	};
	
public class OnSaleAdapter extends BaseAdapter {
	private AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
	private Context context;

	public OnSaleAdapter(Context con) {
		this.context = con;
	}

	@Override
	public int getCount() {
		return baobei.size();
	}

	@Override
	public Object getItem(int position) {
		return baobei.get(position);
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
					R.layout.baobei_item, null);
		}

		WeiBoHolder wh = new WeiBoHolder(); //消息实体
		wh.tupian = (ImageView) satusView.findViewById(R.id.tupiansou);
		wh.name = (TextView) satusView.findViewById(R.id.namesou);
		wh.price = (TextView) satusView.findViewById(R.id.pricesou);
		SouSuo wb = baobei.get(position);
		if (wb != null) {
			
			wh.name.setText("宝贝名称:"+wb.getName());
			
			wh.price.setText("价格:"+wb.getPrice()+"元");
			Drawable cachedImage = asyncImageLoader.loadDrawable(
					wb.getPic_url(), wh.tupian, new ImageCallback() {
						@Override
						public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
							imageView.setImageDrawable(imageDrawable);
						}
					});
			if (cachedImage == null) {
				// 如果没有图片，就以一个载入的图片代替显示
				wh.tupian.setImageResource(R.drawable.ic_android);
			} else {
				// 如果有图片，就载入图片
				wh.tupian.setImageDrawable(cachedImage);
			}
		}
		return satusView;
	}

}



 public static class WeiBoHolder {
    public ImageView tupian;
	public TextView name;
	public TextView price;
}


	public String onSaleGet() {
		Log.i(TAG, "onSaleGet----");
		TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
		apiparamsMap.put("format", "json");
		apiparamsMap.put("method", "taobao.products.search");
		apiparamsMap.put("sign_method", "md5");
		apiparamsMap.put("app_key", Util.APPKEY);
		apiparamsMap.put("v", "2.0");
		apiparamsMap.put("session", Util.access_token); 
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		apiparamsMap.put("timestamp", timestamp);
		apiparamsMap.put("q", Util.xinxi);
		apiparamsMap
				.put("fields",
						"product_id,name,pic_url,cid,props,price,tsc");// 需要获取的字段

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

		Log.i(TAG, "param.toString().substring(1) = "+ param.toString().substring(1));
		return param.toString().substring(1);
	}	        
}

					
				