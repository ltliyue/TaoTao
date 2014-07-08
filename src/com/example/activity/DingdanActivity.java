package com.example.activity;

import com.example.taotao.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class DingdanActivity extends ListActivity {
	LinearLayout  exit,fanhui;
	 AlertDialog alertDialog;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.dingdan);
        initWight();
        setListAdapter(ArrayAdapter.createFromResource(getApplicationContext(),
        R.array.dingdan_array, R.layout.list_item));
        fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());
		
    }
    public void initWight() {
 		// 最下面的四个按钮
		exit = (LinearLayout) findViewById(R.id.exitLayout);
		fanhui = (LinearLayout) findViewById(R.id.addLayout);
    	
 		}
    class Exit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(DingdanActivity.this, MainActivity.class);								
		    startActivity(intent);
		   	}
	}

	class Fanhui implements OnClickListener {
 		@Override
 		public void onClick(View v) {  
 		
 			DingdanActivity.this.finish();
 		}
 	}

        
        protected void onListItemClick(ListView l, View v, int position, long id) {
          super.onListItemClick(l, v, position, id);

          Intent intent= new Intent();
          if(position==0){

          intent.setClass(DingdanActivity.this,Dingdan1.class);
          DingdanActivity.this.startActivity(intent);
          }
          if(position==1){

              intent.setClass(DingdanActivity.this,Dingdan2.class);
              DingdanActivity.this.startActivity(intent);
              }
          if(position==2){

              intent.setClass(DingdanActivity.this,Dingdan3.class);
              DingdanActivity.this.startActivity(intent);
              }
          if(position==3){

              intent.setClass(DingdanActivity.this,Dingdan4.class);
              DingdanActivity.this.startActivity(intent);              
              }
          if(position==4){

              intent.setClass(DingdanActivity.this,Dingdan5.class);
              DingdanActivity.this.startActivity(intent);
              }
          if(position==5){

              intent.setClass(DingdanActivity.this,Tuikuan.class);
              DingdanActivity.this.startActivity(intent);
              }
        
         }

}
