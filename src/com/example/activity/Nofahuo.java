package com.example.activity;

import com.example.taotao.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class Nofahuo extends Activity{
	LinearLayout exit,fanhui;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no);
        MyApplication.getInstance().addActivity(this);
        initWight();
        fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());
       }
    public void initWight() {
    	exit = (LinearLayout) findViewById(R.id.exitLayout);
		fanhui = (LinearLayout) findViewById(R.id.fanhuii);
    	
 		}
    class Exit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Nofahuo.this, MainActivity.class);
			finish();
		    startActivity(intent);
			}
	}

	class Fanhui implements OnClickListener {
 		@Override
 		public void onClick(View v) {  
 			
 			Nofahuo.this.finish();
 		}
 	}
}
