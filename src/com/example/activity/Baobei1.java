package com.example.activity;

import android.app.Activity;
import android.os.Bundle;

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

import com.example.entity.CangkuItem;
import com.example.taotao.R;

import com.example.utils.AsyncImageLoader;
import com.example.utils.AsyncImageLoader.ImageCallback;
import com.example.utils.Util;
import com.example.utils.APIUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

//�ֿ��еı���
public class Baobei1 extends Activity{
	public static final String TAG = "Baobei1";
	
	List<CangkuItem> ois;
	ListView onSaleList2;
	LinearLayout loadingLayout,exit,fanhui;
	 AlertDialog alertDialog;
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        MyApplication.getInstance().addActivity(this);
	        setContentView(R.layout.baobei1);
	        init();
			loadOnCangList();
			fanhui.setOnClickListener(new Fanhui());
			exit.setOnClickListener(new Exit());
	       }

		public void init() {
			onSaleList2 = (ListView) this.findViewById(R.id.cangkuList);
			loadingLayout = (LinearLayout) findViewById(R.id.loadingLayoutcang);
			exit = (LinearLayout) findViewById(R.id.exitLayout);
			fanhui = (LinearLayout) findViewById(R.id.addLayout);
		}
		class Exit implements OnClickListener {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Baobei1.this, MainActivity.class);								
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
		 * ��ȡ�ֿ���Ʒ�б���߳�
		 */
		public void loadOnCangList() {
			new Thread() {
				@Override
				public void run() {
					loadingLayout.setVisibility(View.VISIBLE);
					String testUrl = "http://gw.api.taobao.com/router/rest";				
					String result = APIUtil.getResult(testUrl,onCangGet());
					
					JSONArray data;
					try {
						JSONObject data1 = new JSONObject(result);
						
						JSONObject items_inventory_get_response = data1.getJSONObject("items_inventory_get_response");						
						int total_results = items_inventory_get_response.getInt("total_results");
						if(total_results==0){
							Intent intent = new Intent();
							intent.setClass(Baobei1.this, Error.class);								
						    finish();
						    startActivity(intent);
						    
						}
						JSONObject items = items_inventory_get_response.getJSONObject("items");
						data = items.getJSONArray("item");
						
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
								ois = new ArrayList<CangkuItem>();
							}

							CangkuItem osi = new CangkuItem();
							
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
				if (ois != null) { 
					onSaleList2.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View view,int arg2, long arg3) {
							Object obj = view.getTag();
							if (obj != null) {
								Intent intent = new Intent();
								String num_iid = obj.toString();
								Util.num_iid=num_iid;
								intent.setClass(Baobei1.this, ShangpinActivityCangku.class);								
							    startActivity(intent);
							}
						}

					});
					OnSaleAdapter adapter = new OnSaleAdapter(Baobei1.this);
					onSaleList2.setAdapter(adapter);

					loadingLayout.setVisibility(View.GONE); // ������UI������ת�Ľ�������ͼ��ʧ
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
					// ��ȡԭ���ڴ��б������Ŀ��Ϣ
					satusView = convertView;
				} else {
					satusView = LayoutInflater.from(getApplication()).inflate(R.layout.baobei2_item, null);
				}
				WeiBoHolder wh = new WeiBoHolder(); // ��Ϣʵ��
				wh.tupian = (ImageView) satusView.findViewById(R.id.tupian);
				wh.biaoti = (TextView) satusView.findViewById(R.id.biaoti);
				wh.qixian = (TextView) satusView.findViewById(R.id.qixian);
				wh.jiage = (TextView) satusView.findViewById(R.id.jiage);
				CangkuItem wt = ois.get(position);
				if (wt != null) {
					satusView.setTag(wt.getNum_iid());
					wh.biaoti.setText(wt.getTitle());
					wh.qixian.setText(wt.getDelist_time() + "��Ч�ڣ�"+ wt.getValid_thru());
					wh.jiage.setText(wt.getPrice() + " " + "Ԫ",TextView.BufferType.SPANNABLE);
					
					Drawable cachedImage = asyncImageLoader.loadDrawable(
							wt.getPic_url(), wh.tupian, new ImageCallback() {
								@Override
								public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
									imageView.setImageDrawable(imageDrawable);
								}
							});
					if (cachedImage == null) {
						// ���û��ͼƬ������һ�������ͼƬ������ʾ
						wh.tupian.setImageResource(R.drawable.ic_android);
					} else {
						// �����ͼƬ��������ͼƬ
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
		 * ƴװ����Ĳ�������ƴ������
		 * 
		 * @return
		 */
		public String onCangGet() {
			TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
			apiparamsMap.put("format", "json");
			apiparamsMap.put("method", "taobao.items.inventory.get");
			apiparamsMap.put("sign_method", "md5");
			apiparamsMap.put("app_key", Util.APPKEY);
			apiparamsMap.put("v", "2.0");
			apiparamsMap.put("session", Util.access_token); 
			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date());
			apiparamsMap.put("timestamp", timestamp);
			apiparamsMap
					.put("fields",
							"num_iid,title,type,pic_url,num,props,valid_thru,price,delist_time");// ��Ҫ��ȡ���ֶ�

			// ����ǩ��
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
