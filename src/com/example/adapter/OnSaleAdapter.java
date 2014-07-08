package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.entity.OnSaleItem;
import com.example.taotao.R;
import com.example.utils.AsyncImageLoader;
import com.example.utils.ImageLoader;

public class OnSaleAdapter extends BaseAdapter {
	private AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
	private Context context;
	private List<OnSaleItem> osis;
	private ImageLoader mImageLoader;

	public OnSaleAdapter(Context con, List<OnSaleItem> osaleItems) {
		this.context = con;
		this.osis = osaleItems;
		mImageLoader = new ImageLoader();
	}

	@Override
	public int getCount() {
		return osis.size();
	}

	@Override
	public Object getItem(int position) {
		return osis.get(position);
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
			satusView = LayoutInflater.from(context).inflate(R.layout.onsale_item, null);
		}

		WeiBoHolder viewHolder = new WeiBoHolder(); // 消息实体
		viewHolder.wbicon = (ImageView) satusView.findViewById(R.id.wbicon);
		viewHolder.wbtext = (TextView) satusView.findViewById(R.id.wbtext);
		viewHolder.wbtime = (TextView) satusView.findViewById(R.id.wbtime);
		viewHolder.wbuser = (TextView) satusView.findViewById(R.id.wbuser);
		OnSaleItem wb = osis.get(position);
		if (wb != null) {
			satusView.setTag(wb.getNum_iid());
			viewHolder.wbuser.setText(wb.getTitle());
			viewHolder.wbtime.setText(wb.getDelist_time() + "  有效期：" + wb.getValid_thru() + "天");
			viewHolder.wbtext.setText(wb.getPrice() + " " + "元", TextView.BufferType.SPANNABLE);
			mImageLoader.loadImage(wb.getPic_url(), this, viewHolder);

		}
		return satusView;
	}

	public static class WeiBoHolder {
		public ImageView wbicon;
		public TextView wbuser;
		public TextView wbtime;
		public TextView wbtext;
	}
}