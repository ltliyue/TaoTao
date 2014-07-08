package com.example.activity;

//首页
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.trinea.android.common.util.JSONUtils;

import com.example.entity.OnSaleItem;
import com.example.taotao.R;
import com.example.utils.APIUtil;
import com.example.utils.AsyncImageLoader;
import com.example.utils.AsyncImageLoader.ImageCallback;
import com.example.widget.MySlideMenu;
import com.example.widget.SlideMenu;

public class MainActivity extends Activity {
	private static final String TAG = "GridViewActivity";
	// 双向滑动菜单布局
	private MySlideMenu bidirSldingLayout;
	private ImageView menuImg_left, user_photo, title_back;
	private TextView user_name, title_title, title_setting, txt_cs, txt_ck, txt_cc, txt_ss;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private String taobao_user_nick, access_token, nick, avatar, level;
	private LinearLayout lin_xin;
	private View myViewUser;
	private LayoutInflater myLayoutInflater;
	private Button btn_exit;
	private String title;
	private GridView gridView;

	// 图片的文字标题
	private String[] titles = new String[] { " ", " ", " ", " ", " ", " ", " ", " ", " " };
	// 图片ID数组
	private int[] images = new int[] { R.drawable.c, R.drawable.a, R.drawable.e, R.drawable.g, R.drawable.d, R.drawable.j, R.drawable.i, R.drawable.b, R.drawable.f };

	// 显示到界面
	private Handler handler = new Handler() {
		public void handleMessage(Message message) {
			if (message.what == 0) {

				lin_xin.removeAllViews();
				for (int i = 0; i < Integer.parseInt(level); i++) {
					myViewUser = myLayoutInflater.inflate(R.layout.list_item_xin, null);
					ImageView image_xin = (ImageView) myViewUser.findViewById(R.id.image_xin);
					lin_xin.addView(myViewUser);
				}
				AsyncImageLoader asyncImageLoader = new AsyncImageLoader(); // 异步获取图片
				Drawable cachedImage = asyncImageLoader.loadDrawable(avatar, user_photo, new ImageCallback() {
					@Override
					// 这里是重写了回调接口
					public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl) {
						imageView.setImageDrawable(imageDrawable);
					}
				});

				if (cachedImage == null) {
					// 如果没有图片，就以一个载入的图片代替显示
					user_photo.setImageResource(R.drawable.ic_android);
				} else {
					// 如果有图片，就载入图片
					user_photo.setImageDrawable(cachedImage);
				}
			}
			if (message.what == 1) {
				title_title.setText(title);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_main);
		sharedPreferences = getSharedPreferences("AccessToken", 0);
		editor = sharedPreferences.edit();
		taobao_user_nick = sharedPreferences.getString("taobao_user_nick", "");
		access_token = sharedPreferences.getString("access_token", "");
		initWight();
		initListener();
		if (isNetworkAvailable(MainActivity.this) == false) {
			setNetwork();
		} else {
			getUserinfo();
			getShopinfo();
			initData();
		}
	}

	private void initData() {
		// TODO Auto-generated method stub
		user_name.setText(taobao_user_nick);
	}

	public void initWight() {
		gridView = (GridView) findViewById(R.id.gridview);
		bidirSldingLayout = (MySlideMenu) findViewById(R.id.bidir_sliding_layout);
		menuImg_left = (ImageView) findViewById(R.id.title_bar_menu_btn_left);
		menuImg_left.setVisibility(View.VISIBLE);
		user_photo = (ImageView) findViewById(R.id.user_photo);
		title_back = (ImageView) findViewById(R.id.title_back);
		title_back.setVisibility(View.GONE);
		title_setting = (TextView) findViewById(R.id.title_setting);
		title_setting.setVisibility(View.INVISIBLE);
		user_name = (TextView) findViewById(R.id.user_name);
		lin_xin = (LinearLayout) findViewById(R.id.lin_xin);
		btn_exit = (Button) findViewById(R.id.btn_exit);
		title_title = (TextView) findViewById(R.id.title_title);
		myLayoutInflater = getLayoutInflater();
		txt_cs = (TextView) findViewById(R.id.txt_cs);
		txt_ck = (TextView) findViewById(R.id.txt_ck);
		txt_cc = (TextView) findViewById(R.id.txt_cc);
		txt_ss = (TextView) findViewById(R.id.txt_ss);
	}

	private void initListener() {
		// TODO Auto-generated method stub
		PictureAdapter adapter = new PictureAdapter(titles, images, this);
		gridView.setAdapter(adapter);
		bidirSldingLayout.setScrollEvent(gridView);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent intent = new Intent();
				switch (position) {
				case 0:
					intent.setClass(MainActivity.this, ShopInfoActivity.class);
					startActivity(intent);
					break;
				case 1:
					intent.setClass(MainActivity.this, BaobeiActivity.class);
					startActivity(intent);
					break;
				case 2:
					intent.setClass(MainActivity.this, DingdanActivity.class);
					startActivity(intent);
					break;
				case 3:
					intent.setClass(MainActivity.this, PingjiaActivity.class);
					startActivity(intent);
					break;
				case 4:
					intent.setClass(MainActivity.this, FahuoActivity.class);
					startActivity(intent);
					break;
				case 5:
					intent.setClass(MainActivity.this, GeRenXinXi.class);
					startActivity(intent);
					break;
				case 6:
					intent.setClass(MainActivity.this, WapWangWang.class);
					startActivity(intent);
					break;
				case 7:
					intent.setClass(MainActivity.this, ChaKanWuLiu.class);
					startActivity(intent);
					break;
				case 8:
					intent.setClass(MainActivity.this, TaozixunActivity.class);
					startActivity(intent);
					break;
				}

			}
		});
		menuImg_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (bidirSldingLayout.isLeftLayoutVisible()) {
					bidirSldingLayout.scrollToContentFromLeftMenu();
				} else {
					bidirSldingLayout.scrollToLeftMenu();
				}
			}
		});
		btn_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent();
				editor.putString("access_token", "").commit();
				editor.putString("refresh_token", "").commit();
				myIntent.setClass(MainActivity.this, LoginActivity.class);
				finish();
				startActivity(myIntent);
			}
		});
		txt_cs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent();

				myIntent.setClass(MainActivity.this, Baobei0.class);
				startActivity(myIntent);
			}
		});
		txt_ck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent();

				myIntent.setClass(MainActivity.this, Baobei1.class);
				startActivity(myIntent);
			}
		});
		txt_cc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent();

				myIntent.setClass(MainActivity.this, Baobei2.class);
				startActivity(myIntent);
			}
		});
		txt_ss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent();

				myIntent.setClass(MainActivity.this, Baobei3.class);
				startActivity(myIntent);
			}
		});
	}

	public void getUserinfo() {
		new Thread() {
			@Override
			public void run() {
				// 发送请求得到数据
				Map<String, String> parasMap = new HashMap<String, String>();
				parasMap.put("method", "taobao.user.seller.get");
				parasMap.put(
						"fields",
						"user_id,nick,sex,seller_credit,type,has_more_pic,item_img_num,item_img_size,prop_img_num,prop_img_size,auto_repost,promoted_type,status,alipay_bind,consumer_protection,avatar,liangpin,sign_food_seller_promise,has_shop,is_lightning_consignment,has_sub_stock,is_golden_seller,vip_info,magazine_subscribe,vertical_market,online_gaming");

				String result = APIUtil.getShopInfo(parasMap);
				System.out.println(result);
				if (result.length() < 60) {
					getUserinfo();
				} else {

					// 解析得到的数据
					try {
						JSONObject data1 = new JSONObject(result);

						JSONObject user_seller_get_response = data1.getJSONObject("user_seller_get_response");
						JSONObject user = user_seller_get_response.getJSONObject("user");
						nick = user.getString("nick");
						avatar = user.getString("avatar");
						JSONObject seller_credit = user.getJSONObject("seller_credit");
						level = seller_credit.getString("level");

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				if (nick != null) {
					Message message = handler.obtainMessage(0);
					handler.sendMessage(message);
				}
			}
		}.start();
	}

	public void getShopinfo() {
		new Thread() {
			@Override
			public void run() {

				Map<String, String> parasMap = new HashMap<String, String>();
				parasMap.put("method", "taobao.shop.get");
				parasMap.put("fields", "title,nick,desc,bulletin");
				parasMap.put("nick", taobao_user_nick);

				String result = APIUtil.getShopInfo(parasMap);
				if (result.length() < 60) {
					getShopinfo();
				} else {
					try {
						JSONObject data1 = new JSONObject(result);

						JSONObject shop_get_response = data1.getJSONObject("shop_get_response");
						JSONObject shop = shop_get_response.getJSONObject("shop");

						title = shop.getString("title");

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				Message message = handler.obtainMessage(1);
				handler.sendMessage(message);
			}
		}.start();
	}

	// setnetwork
	public void setNetwork() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setTitle("网络状态");
		builder.setMessage("当前网络不可用，是否设置网络？");
		builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent mIntent = new Intent("/");
				ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
				mIntent.setComponent(comp);
				mIntent.setAction("android.intent.action.VIEW");
				startActivity(mIntent);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				finish();
			}
		});
		builder.create();
		builder.show();
	}

	// NETWORK
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.i("NetWorkState", "Unavailabel");
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						Log.i("NetWorkState", "Availabel");
						return true;
					}
				}
			}
		}
		return false;
	}
}

// 自定义适配器
class PictureAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<Picture> pictures;

	public PictureAdapter(String[] titles, int[] images, Context context) {
		super();
		pictures = new ArrayList<Picture>();
		inflater = LayoutInflater.from(context);
		for (int i = 0; i < images.length; i++) {
			Picture picture = new Picture(titles[i], images[i]);
			pictures.add(picture);
		}
	}

	public int getCount() {
		if (null != pictures) {
			return pictures.size();
		} else {
			return 0;
		}
	}

	public Object getItem(int position) {
		return pictures.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.night_item, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.itemText);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.itemImage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.title.setText(pictures.get(position).getTitle());
		viewHolder.image.setImageResource(pictures.get(position).getImageId());
		return convertView;
	}

}

class ViewHolder {
	public TextView title;
	public ImageView image;
}

class Picture {
	private String title;
	private int imageId;

	public Picture() {
		super();
	}

	public Picture(String title, int imageId) {
		super();
		this.title = title;
		this.imageId = imageId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
}
