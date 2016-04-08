package com.example.mrpan.dreamtogether.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.view.NoScrollGridView;

import java.util.List;

public class DreamCircleAdapter extends BaseAdapter {

	private List<Dream> mList;
	private Context mContext;

	public DreamCircleAdapter(Context _context) {
		this.mContext = _context;
	}

	public void setData(List<Dream> _list) {
		this.mList = _list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Dream getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.wordcircle_dreams_items, parent, false);
			holder.gridView = (NoScrollGridView) convertView
					.findViewById(R.id.gridView);
			convertView.setTag(holder);
			holder.author=(TextView)convertView.findViewById(R.id.dream_author);
		} else
			holder = (ViewHolder) convertView.getTag();

		Dream mUserInfo = getItem(position);
		if (mList != null && mList.size() > 0) {
			holder.gridView.setVisibility(View.VISIBLE);
			holder.author.setText("梦想家");
			//holder.gridView.setAdapter(new MyGridAdapter(mUserInfo.getUi(),
			//		mContext));
//			holder.gridView
//					.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//						@Override
//						public void onItemClick(AdapterView<?> parent,
//								View view, int position, long id) {
//							// imageBrower(position,bean.urls);
//						}
//					});
		}
		return convertView;
	}

	public class ViewHolder {
		LinearLayout mContentimg;
		NoScrollGridView gridView;
		TextView author;
	}

}
