package com.example.activity;

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

import com.example.activity.Baobei1.Exit;
import com.example.activity.Baobei1.Fanhui;
import com.example.entity.ChuchuangItem;
import com.example.taotao.R;
import com.example.utils.APIUtil;
import com.example.utils.AsyncImageLoader;
import com.example.utils.AsyncImageLoader.ImageCallback;
import com.example.utils.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//橱窗推荐宝贝
public class Baobei2 extends Activity{
 	public static final String TAG = "Baobei2";
 	
 	List<ChuchuangItem> ois;
 	ListView onSaleList3;
 	LinearLayout loadingLayout,exit,fanhui;
	 AlertDialog alertDialog;
 	    /** Called when the activity is first created. */
 	    @Override
 	    public void onCreate(Bundle savedInstanceState) {
 	        super.onCreate(savedInstanceState);
 	       MyApplication.getInstance().addActivity(this);
 	        setContentView(R.layout.baobei2);
 	        init();
 			loadChuchuang();
 			exit.setOnClickListener(new Exit());
 			fanhui.setOnClickListener(new Fanhui());
			
 	       }

 		public void init() {
 			loadingLayout = (LinearLayout) findViewById(R.id.loadingLayout3);
 			onSaleList3 = (ListView) this.findViewById(R.id.onSaleList3);
 			exit= (LinearLayout) findViewById(R.id.exitLayout);
			fanhui= (LinearLayout) findViewById(R.id.addLayout);
 		}
 		class Exit implements OnClickListener {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Baobei2.this, MainActivity.class);								
			    startActivity(intent);
			    }
		}

		class Fanhui implements OnClickListener {
	 		@Override
	 		public void onClick(View v) {  
			    finish();
			    }
	 	}


 		/**
 		 * 获取仓库商品列表的线程
 		 */
 		public void loadChuchuang() {
 			new Thread() {
 				@Override
 				public void run() {
 					loadingLayout.setVisibility(View.VISIBLE);
 					String testUrl = "http://gw.api.taobao.com/router/rest";				
 					String result = APIUtil.getResult(testUrl,Chuchuang());
 					//String onSaleUrl = onCangGet();
 					
 					JSONArray data;
 					try {
 						JSONObject data1 = new JSONObject(result);
 						
 						JSONObject items_onsale_get_response = data1.getJSONObject("items_onsale_get_response");						
 						int total_results = items_onsale_get_response.getInt("total_results");
 						if(total_results==0){
							Intent intent = new Intent();
							intent.setClass(Baobei2.this, Error.class);	
						    finish();
						    startActivity(intent);
						}
 						JSONObject items = items_onsale_get_response.getJSONObject("items");
 						data = items.getJSONArray("item");
 						
 						Log.i(TAG, "TEST==========" + items);
 					
 						// num_iid,title,type,pic_url,num,props,valid_thru,price,delist_time
 						for (int i = 0; i < data.length(); i++) {
 							JSONObject item = data.getJSONObject(i);
 							
 								String num_iid = item.getString("num_iid");
 								String pic_url = item.getString("pic_url");
 								String price = item.getString("price");
 								String title = item.getString("title");
 								String type = item.getString("type");
 								String num = item.getString("num");
 								String props = item.getString("props");
 								String valid_thru = item.getString("valid_thru");
 								String delist_time = item.getString("delist_time");

 							if (ois == null) {
 								ois = new ArrayList<ChuchuangItem>();
 							}

 							ChuchuangItem osi = new ChuchuangItem();
 							
 							osi.setNum_iid(num_iid);
 							osi.setPic_url(pic_url);
 							osi.setPrice(price);
 							osi.setTitle(title);
 							osi.setType(type);
 							osi.setNum(num);
 							osi.setProps(props);
 							osi.setValid_thru(valid_thru);
 							osi.setDelist_time(delist_time);

 							ois.add(osi);
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
 				if (ois != null) { // 如果获取的微博消息列表不为空
 					// 当每一条微博消息被点击时的响应
 					onSaleList3.setOnItemClickListener(new OnItemClickListener() {
 						@Override
 						public void onItemClick(AdapterView<?> arg0, View view,int arg2, long arg3) {
 							Object obj = view.getTag();
 							if (obj != null) {
 								Intent intent = new Intent();
 								String num_iid = obj.toString();
 								Log.i(TAG, "load_id----result =" + num_iid);
 								Util.num_iid=num_iid;
 								intent.setClass(Baobei2.this, ShangpinChuchuang.class);								
 							    startActivity(intent);
 							}
 						}

 					});
 					OnSaleAdapter adapter = new OnSaleAdapter(Baobei2.this);
 					onSaleList3.setAdapter(adapter);

 					//adapter.notifyDataSetChanged(); // 这句话可能出错，可不加，明天测试下。。。
 					loadingLayout.setVisibility(View.GONE); // 更新完UI，让总转的进度条视图消失
 				}
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
 				return ois.size();
 			}

 			@Override
 			public Object getItem(int position) {
 				return ois.get(position);
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
 					satusView = LayoutInflater.from(getApplication()).inflate(R.layout.baobei2_item, null);
 				}
 				WeiBoHolder wh = new WeiBoHolder(); // 微博消息实体
 				wh.tupian = (ImageView) satusView.findViewById(R.id.tupian);
 				wh.biaoti = (TextView) satusView.findViewById(R.id.biaoti);
 				wh.qixian = (TextView) satusView.findViewById(R.id.qixian);
 				wh.jiage = (TextView) satusView.findViewById(R.id.jiage);
 				ChuchuangItem wt = ois.get(position);
 				if (wt != null) {
 					satusView.setTag(wt.getNum_iid());
 					wh.biaoti.setText(wt.getTitle());
 					wh.qixian.setText(wt.getDelist_time() + "有效期："+ wt.getValid_thru());
 					wh.jiage.setText(wt.getPrice() + " " + "元",TextView.BufferType.SPANNABLE);
 					
 					Drawable cachedImage = asyncImageLoader.loadDrawable(
 							wt.getPic_url(), wh.tupian, new ImageCallback() {
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
 			public ImageView wbimage2;
 			public ImageView tupian;
 			public TextView jiage;
 			public TextView biaoti;
 			public TextView qixian;
 		}

 		
 		/**
 		 * 拼装请求的参数并按拼音排序
 		 * 
 		 * @return
 		 */
 		public String Chuchuang() {
 			TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
 			apiparamsMap.put("format", "json");
 			apiparamsMap.put("method", "taobao.items.onsale.get");
 			apiparamsMap.put("sign_method", "md5");
 			apiparamsMap.put("app_key", Util.APPKEY);
 			apiparamsMap.put("v", "2.0");
 			
 			apiparamsMap.put("has_showcase", "true");
 			
 			apiparamsMap.put("session", Util.access_token); // 他用型需要sessionkey
 			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
 					.format(new Date());
 			apiparamsMap.put("timestamp", timestamp);
 			apiparamsMap
 					.put("fields",
 							"approve_status,num_iid,title,nick,type,cid,pic_url,num,props,valid_thru,list_time,price,has_discount,has_invoice,has_warranty,has_showcase,modified,delist_time,postage_id,seller_cids,outer_id");// 需要获取的字段

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
