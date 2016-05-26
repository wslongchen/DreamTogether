package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.utils.BitmapUtils;
import com.example.mrpan.dreamtogether.utils.FileUtils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class PhotoFragment extends Fragment{

	public static final String TAG="PhotoFragment";

	private ArrayList<View> listViews = null;
	private ViewPager pager;
	private MyPageAdapter adapter;
	private int currentItem=0;

	private ProgressBar image_progressBar;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();
	public int max;

	RelativeLayout photo_relativeLayout;

	private ImageView selected;

	private View currentView;

	private FragmentTransaction transaction;

	private TextView count_str;

	private Context context;

	private boolean isIndex=false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		getActivity().getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
//				WindowManager.LayoutParams. FLAG_FULLSCREEN);
		currentView = inflater.inflate(R.layout.activity_photo, container, false);
		ViewGroup viewGroup = (ViewGroup) currentView.getParent();
		if (viewGroup != null) {
			viewGroup.removeView(currentView);
		}
		context = getActivity();
		image_progressBar=(ProgressBar)currentView.findViewById(R.id.image_progressBar);
		photo_relativeLayout = (RelativeLayout) currentView.findViewById(R.id.photo_relativeLayout);
		photo_relativeLayout.setBackgroundColor(0x70000000);
		count_str=(TextView)currentView.findViewById(R.id.photo_count);
		Bundle bundle = getArguments();
		String[] imgs=bundle.getStringArray("imgs");
		if(imgs!=null){
			isIndex=true;
			if(imgs.length>0){
				for(int i=0;i<imgs.length;i++){
//					Bitmap bitmap=ImageLoader.getInstance().loadImageSync("http://"+imgs[i]);
//					if(bitmap!=null)
//						bmp.add(bitmap);
					initListUrls("http://"+imgs[i]);
				}
				pager = (ViewPager) currentView.findViewById(R.id.viewpager);
				pager.setOnPageChangeListener(pageChangeListener);

				adapter = new MyPageAdapter(listViews);// 构造adapter
				pager.setAdapter(adapter);// 设置适配器

				int id = bundle.getInt("ID", 0);
				pager.setCurrentItem(id);
				String state = (id+1)+"/"+imgs.length;
				count_str.setText(state);
			}
		}else{
			for (int i = 0; i < BitmapUtils.bmp.size(); i++) {
				bmp.add(BitmapUtils.bmp.get(i));
			}
			for (int i = 0; i < BitmapUtils.drr.size(); i++) {
				drr.add(BitmapUtils.drr.get(i));
			}
			for (int i = 0; i < bmp.size(); i++) {
				initListViews(bmp.get(i));
			}
			max = BitmapUtils.max;

			pager = (ViewPager) currentView.findViewById(R.id.viewpager);
			pager.setOnPageChangeListener(pageChangeListener);





			adapter = new MyPageAdapter(listViews);// 构造adapter
			pager.setAdapter(adapter);// 设置适配器
			pager.getAdapter().notifyDataSetChanged();
			int id = bundle.getInt("ID", 0);
			currentItem=id;
			String state = (id+1)+"/"+bmp.size();
		//	pager.setCurrentItem(currentItem);

			MyLog.i(TAG, "state:"+state+",pagerCurrentItem:"+pager.getCurrentItem()+",pagerSize"+pager.getAdapter().getCount());
			count_str.setText(state);

		}



//		Button photo_bt_exit = (Button) currentView.findViewById(R.id.photo_bt_exit);
//		photo_bt_exit.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//
//				//getActivity().finish();
//			}
//		});
		if(!isIndex){
			selected=(ImageView)currentView.findViewById(R.id.isselected);
			selected.setVisibility(View.VISIBLE);
			selected.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (listViews.size() == 1) {
						BitmapUtils.bmp.clear();
						BitmapUtils.drr.clear();
						BitmapUtils.max = 0;
						//FileUtils.deleteDir();
						//finish();
					} else {
						String newStr = drr.get(currentItem).substring(
								drr.get(currentItem).lastIndexOf("/") + 1,
								drr.get(currentItem).lastIndexOf("."));
						bmp.remove(currentItem);
						drr.remove(currentItem);
						del.add(newStr);
						max--;
						pager.removeAllViews();
						selected.setVisibility(View.GONE);
						listViews.remove(currentItem);
						adapter.setListViews(listViews);
						adapter.notifyDataSetChanged();
					}
				}
			});
		}

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
//					BitmapUtils.bmp = bmp;
//				BitmapUtils.drr = drr;
//				BitmapUtils.max = max;
//				for (int i = 0; i < del.size(); i++) {
//					//FileUtils.delFile(del.get(i)+".JPEG");
//				}
					transaction = getFragmentManager().beginTransaction();
					//transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);
					transaction.replace(R.id.other_layout, OtherActivity.fragmentHashMap.get(DreamPostFragment.TAG));
//					transaction.addToBackStack(null);
//                    transaction.remove(SelectImageGridFragment.this);
					transaction.commit();
				}
			}
		});
		listViews.add(img);// 添加view
	}

	private void initListUrls(String url) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		ImageView img = new ImageView(getActivity());// 构造textView对象
		img.setBackgroundColor(0xff000000);
		ImageLoader.getInstance().displayImage(url, img, null, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				super.onLoadingStarted(imageUri, view);
				image_progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				super.onLoadingFailed(imageUri, view, failReason);
				String message = null;
				switch (failReason.getType()) {
					case IO_ERROR:
						message = "网络异常";
						break;
					case DECODING_ERROR:
						message = "图片解析失败";
						break;
					case NETWORK_DENIED:
						message = "图片加载失败";
						break;
					case OUT_OF_MEMORY:
						message = "内存溢出";
						break;
					case UNKNOWN:
						message = "未知错误";
						break;
				}
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();
				image_progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				super.onLoadingComplete(imageUri, view, loadedImage);
				image_progressBar.setVisibility(View.GONE);
			}
		}, new ImageLoadingProgressListener() {
			@Override
			public void onProgressUpdate(String s, View view, int i, int i1) {
				int progress=0;
				if(i!=0&&i<=i1){
					progress=(int)((float) i/(float)i1*100);
					//loadingText.setText(""+progress+"%");
					image_progressBar.setProgress(progress);
				}
			}
		});
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
			currentItem=arg0;


		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

		}

		public void onPageScrollStateChanged(int arg0) {// 滑动状态改变
			currentItem=arg0;
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

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		currentItem=0;
		bmp.clear();
		drr.clear();
		del.clear();
		listViews.clear();
	}
}
