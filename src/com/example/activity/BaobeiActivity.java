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


public class BaobeiActivity extends ListActivity {
	LinearLayout exit, fanhui;
	AlertDialog alertDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);

		setContentView(R.layout.baobei);
		initWight();

		setListAdapter(ArrayAdapter.createFromResource(getApplicationContext(), R.array.baibei_array, R.layout.list_view));
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
			finish();
		}
	}

	class Fanhui implements OnClickListener {
		@Override
		public void onClick(View v) {
			finish();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent = new Intent();
		if (position == 0) {
			intent.setClass(BaobeiActivity.this, Baobei0.class);
			BaobeiActivity.this.startActivity(intent);
		} else if (position == 1) {
			intent.setClass(BaobeiActivity.this, Baobei1.class);
			BaobeiActivity.this.startActivity(intent);
		} else if (position == 2) {
			intent.setClass(BaobeiActivity.this, Baobei2.class);
			BaobeiActivity.this.startActivity(intent);
		}

		else if (position == 3) {
			intent.setClass(BaobeiActivity.this, Baobei3.class);
			BaobeiActivity.this.startActivity(intent);
		}

	}
}
