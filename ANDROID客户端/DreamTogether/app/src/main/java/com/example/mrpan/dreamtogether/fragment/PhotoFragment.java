package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.utils.BitmapUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class PhotoFragment extends Fragment{

	public static final String TAG="PhotoFragment";

	private ArrayList<View> listViews = null;
	private ViewPager pager;
	private MyPageAdapter adapter;
	private int count;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
//	public List<String> del = new ArrayList<String>();
	public int max;

	RelativeLayout photo_relativeLayout;

	private View currentView;

	private TextView count_str;

	private Context context;

	private boolean isIndex=false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.activity_photo, container, false);
		ViewGroup viewGroup = (ViewGroup) currentView.getParent();
		if (viewGroup != null) {
			viewGroup.removeView(currentView);
		}
		context = getActivity();

		photo_relativeLayout = (RelativeLayout) currentView.findViewById(R.id.photo_relativeLayout);
		photo_relativeLayout.setBackgroundColor(0x70000000);
		count_str=(TextView)currentView.findViewById(R.id.photo_count);
		Bundle bundle = getArguments();
		String[] imgs=bundle.getStringArray("imgs");
		if(imgs!=null){
			isIndex=true;
			if(imgs.length>0){
				for(int i=0;i<imgs.length;i++){
					Bitmap bitmap=ImageLoader.getInstance().loadImageSync("http://"+imgs[i]);
					if(bitmap!=null)
						bmp.add(bitmap);
					System.out.println(imgs[i]);
				}
			}
		}else{
			for (int i = 0; i < BitmapUtils.bmp.size(); i++) {
				bmp.add(BitmapUtils.bmp.get(i));
			}
			for (int i = 0; i < BitmapUtils.drr.size(); i++) {
				drr.add(BitmapUtils.drr.get(i));
			}
			max = BitmapUtils.max;
		}



//		Button photo_bt_exit = (Button) currentView.findViewById(R.id.photo_bt_exit);
//		photo_bt_exit.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//
//				//getActivity().finish();
//			}
//		});
		//Button photo_bt_del = (Button) currentView.findViewById(R.id.photo_bt_del);
//		photo_bt_del.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				if (listViews.size() == 1) {
//					BitmapUtils.bmp.clear();
//					BitmapUtils.drr.clear();
//					BitmapUtils.max = 0;
//					//FileUtils.deleteDir();
//					//finish();
//				} else {
//					String newStr = drr.get(count).substring(
//							drr.get(count).lastIndexOf("/") + 1,
//							drr.get(count).lastIndexOf("."));
//					bmp.remove(count);
//					drr.remove(count);
//					del.add(newStr);
//					max--;
//					pager.removeAllViews();
//					listViews.remove(count);
//					adapter.setListViews(listViews);
//					adapter.notifyDataSetChanged();
//				}
//			}
//		});
//		Button photo_bt_enter = (Button) currentView.findViewById(R.id.photo_bt_enter);
//		photo_bt_enter.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View v) {
//
//				BitmapUtils.bmp = bmp;
//				BitmapUtils.drr = drr;
//				BitmapUtils.max = max;
//				for (int i = 0; i < del.size(); i++) {
//					//FileUtils.delFile(del.get(i)+".JPEG");
//				}
//				//getActivity().finish();
//			}
//		});

		pager = (ViewPager) currentView.findViewById(R.id.viewpager);
		pager.setOnPageChangeListener(pageChangeListener);
		for (int i = 0; i < bmp.size(); i++) {
			initListViews(bmp.get(i));//
		}



		adapter = new MyPageAdapter(listViews);// 构造adapter
		pager.setAdapter(adapter);// 设置适配器

		int id = bundle.getInt("ID", 0);
		pager.setCurrentItem(id);
		String state = (id+1)+"/"+bmp.size();
		count_str.setText(state);
		return currentView;
	}

	private void initListViews(Bitmap bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		ImageView img = new ImageView(getActivity());// 构造textView对象
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isIndex){
					getActivity().finish();
				}else{
					getFragmentManager().popBackStack();
				}
			}
		});
		listViews.add(img);// 添加view
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {// 页面选择响应函数
			String state = (arg0+1)+"/"+bmp.size();
			count_str.setText(state);
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

		}

		public void onPageScrollStateChanged(int arg0) {// 滑动状态改变

		}
	};


	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;// content

		private int size;// 页数

		public MyPageAdapter(ArrayList<View> listViews) {// 构造函数
															// 初始化viewpager的时候给的一个页面
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {// 返回数量
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {// 销毁view对象
			((ViewPager) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {// 返回view对象
			try {
				((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}


}
