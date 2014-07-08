package com.example.activity;

import com.example.taotao.R;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
//·¢»õ
public class FahuoActivity extends ListActivity {
	 LinearLayout exit,fanhui;
	 AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.fahuo);
        initWight();
        fanhui.setOnClickListener(new Fanhui());
		exit.setOnClickListener(new Exit());
		
        setListAdapter(ArrayAdapter.createFromResource(getApplicationContext(),
        		R.array.fahuo_array, R.layout.list_view));
    }
    public void initWight() {
		exit = (LinearLayout) findViewById(R.id.exitLayout);
		fanhui = (LinearLayout) findViewById(R.id.addLayout);
    	
 		}

    class Exit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(FahuoActivity.this, MainActivity.class);								
		    startActivity(intent);
		   }
	}

	class Fanhui implements OnClickListener {
 		@Override
 		public void onClick(View v) {  
 			
 			FahuoActivity.this.finish();
 		}
 	}
    
    
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
      super.onListItemClick(l, v, position, id);

      Intent intent= new Intent();
      if(position==0){

      intent.setClass(FahuoActivity.this,Fahuo0.class);
      FahuoActivity.this.startActivity(intent);
      }
      else if(position==1){
      intent.setClass(FahuoActivity.this,Fahuo1.class);
      FahuoActivity.this.startActivity(intent);
      }
      else if(position==2){
      intent.setClass(FahuoActivity.this,Fahuo2.class);
      FahuoActivity.this.startActivity(intent);
      }
      else if(position==3){
          intent.setClass(FahuoActivity.this,Wuliugongsi.class);
          FahuoActivity.this.startActivity(intent);
          }}
     
    
}
