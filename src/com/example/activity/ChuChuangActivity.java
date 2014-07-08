package com.example.activity;
//�����Ƽ���ť
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.utils.APIUtil;
import com.example.utils.Util;

public class ChuChuangActivity extends Activity {
	public static final String TAG = "ChuChuangActivity";
	
	String sign;
	String result;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadList();
   
    }
  
	public void loadList() {
		sign = getParams(); // ���APIǩ��
		publishFeedThread();
	}

	public String getParams() 
	{

	      TreeMap<String, String> apiparamsMap01 = new TreeMap<String, String>();
	      String num=Util.num_iid;
	      apiparamsMap01.put("format", "json");
	      apiparamsMap01.put("method", "taobao.item.recommend.add");
	      apiparamsMap01.put("sign_method","md5");
	      apiparamsMap01.put("app_key",Util.APPKEY);
	      apiparamsMap01.put("v", "2.0");
	      apiparamsMap01.put("session", Util.access_token);
	      String timestamp01 =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	      apiparamsMap01.put("timestamp",timestamp01);
	      apiparamsMap01.put("num_iid",num);
	      //����ǩ��
	      String sign = APIUtil.md5Signature(apiparamsMap01,Util.SECRET);

      TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
            
	   apiparamsMap.put("format", "json");
       apiparamsMap.put("method", "taobao.item.recommend.add");
       apiparamsMap.put("sign_method","md5");
       apiparamsMap.put("app_key",Util.APPKEY);
       apiparamsMap.put("v", "2.0");
       apiparamsMap.put("session", Util.access_token);
       String timestamp =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
       apiparamsMap.put("timestamp",timestamp);
       apiparamsMap.put("num_iid",num);
       apiparamsMap.put("sign", sign);
      
      StringBuilder param = new StringBuilder();
     
       for 
       (
      	Iterator<Map.Entry<String, String>> it = apiparamsMap.entrySet().iterator(); it.hasNext();
       ) {
          Map.Entry<String, String> e = it.next();
          param.append("&").append(e.getKey()).append("=").append(e.getValue());
       	}
       Log.i(TAG, "TEST==" +param.toString().substring(1) );
       return param.toString().substring(1);
      
   }
	public void publishFeedThread() {
		new Thread() {
			@Override
			public void run() {
				int what=1;
				//��������õ�����
					String testUrl = "http://gw.api.taobao.com/router/rest";				
					result = APIUtil.getResult(testUrl,getParams());
					
					//�����õ�������
					try {
						JSONObject data= new JSONObject(result);
						
						JSONObject item_recommend_add_response = data.getJSONObject("item_recommend_add_response");					    					
						JSONObject item = item_recommend_add_response.getJSONObject("item");
						String num_iid = item.getString("num_iid");
						
						Log.i(TAG, "TEST==" + num_iid);	
					
					} catch (JSONException e) {
						e.printStackTrace();
					}
					Message msg = new Message();   
		            msg.what = what;
		            Bundle bundle = new Bundle();     
	                bundle.putString("result", result);  //��Bundle�д������    
	                msg.setData(bundle);   				 //mes����Bundle��������    
	                handler.sendMessage(msg);			 //��activity�е�handler������Ϣ    	 
				}
			}.start();			
		}		 
		/**
		 * ����UI�߳�ListView
		 */
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
		        Toast.makeText(ChuChuangActivity.this, "��Ʒ�ѳɹ��Ƽ�������", Toast.LENGTH_LONG).show();
		        Intent intent = new Intent();
		        intent.setClass(ChuChuangActivity.this, Baobei2.class);
		        finish();
		       	startActivity(intent);
			}
		};
}

