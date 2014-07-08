package com.example.activity;
//∆¿º€π‹¿Ì
import com.example.taotao.R;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class PingjiaActivity extends ListActivity {
	LinearLayout exit,fanhui;
	 AlertDialog alertDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pingjia);
        MyApplication.getInstance().addActivity(this);
        
        initWight() ;
        setListAdapter(ArrayAdapter.createFromResource(getApplicationContext(),
        		R.array.count_array, R.layout.list_item));
        fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());
		
}
    public void initWight() {
		exit = (LinearLayout) findViewById(R.id.exitLayout);
		fanhui = (LinearLayout) findViewById(R.id.addLayout);
    	
 		}
    class Exit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(PingjiaActivity.this, MainActivity.class);								
		    startActivity(intent);
		    }
	}

	class Fanhui implements OnClickListener {
 		@Override
 		public void onClick(View v) {  
 			PingjiaActivity.this.finish();
 		}
 	}
      
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
      //TODO Auto-generated
    super.onListItemClick(l, v, position, id);

    Intent intent= new Intent();
    if(position==0){

    intent.setClass(PingjiaActivity.this,Pingjia00.class);
    PingjiaActivity.this.startActivity(intent);
    }
    else if(position==1){
    intent.setClass(PingjiaActivity.this,Pingjia01.class);
    PingjiaActivity.this.startActivity(intent);
    }
    else if(position==2){
    intent.setClass(PingjiaActivity.this,Pingjia02.class);
    PingjiaActivity.this.startActivity(intent);
    }
    else if(position==3){
        intent.setClass(PingjiaActivity.this,Pingjia03.class);
        PingjiaActivity.this.startActivity(intent);
        }
    else if(position==4){
        intent.setClass(PingjiaActivity.this,PingJiaXinYong.class);
        PingjiaActivity.this.startActivity(intent);
        }
    }
    
    
   
    
}


