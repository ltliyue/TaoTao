package com.example.activity;

import com.example.taotao.R;
import com.example.utils.Util;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
//ËÑË÷±¦±´
public class Baobei3 extends Activity{
	public static final String TAG = "Baobei3";
    EditText chazhao;
    Button button;
    String xinxi;
    LinearLayout  exit,fanhui;
	 AlertDialog alertDialog;
	 /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       MyApplication.getInstance().addActivity(this);
       
       setContentView(R.layout.baobei3);
       initWidgt();
       fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());
 
       button.setOnClickListener(new OnClickListener(){
       	Intent intent = new Intent();
       	public void onClick(View v) {
       		xinxi = chazhao.getText().toString().trim();
       		Util.xinxi=xinxi;
       		intent.setClass(Baobei3.this, SouSuoBaoBei.class);
   		    startActivity(intent);
   		 finish();
       	}
       	  
       	});
     
      }
  
   public void initWidgt(){
	   chazhao = (EditText) findViewById(R.id.chazhaobaobei);
       button = (Button) findViewById(R.id.queding);
     	exit = (LinearLayout) findViewById(R.id.exitLayout);
		fanhui = (LinearLayout) findViewById(R.id.addLayout);
   }
   class Exit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Baobei3.this, MainActivity.class);								
		    startActivity(intent);
		    }
	}

	class Fanhui implements OnClickListener {
		@Override
		public void onClick(View v) {  
		    finish();		}
	}

}



