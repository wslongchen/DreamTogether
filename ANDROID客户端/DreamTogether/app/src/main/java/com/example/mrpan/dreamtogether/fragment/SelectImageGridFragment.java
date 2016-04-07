package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.ImageItem;
import com.example.mrpan.dreamtogether.utils.AlbumHelper;
import com.example.mrpan.dreamtogether.utils.BitmapUtils;
import com.example.mrpan.dreamtogether.utils.ImageGridAdapter;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.view.TitleBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mrpan on 16/3/31.
 */
public class SelectImageGridFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = "SelectImageGrid";

    public static final String EXTRA_IMAGE_LIST = "imagelist";

    private View currentView = null;
    private Context context = null;


    private FragmentTransaction transaction;

    private TitleBar titleBar;

    List<ImageItem> dataList;
    GridView gridView;
    ImageGridAdapter adapter;
    AlbumHelper helper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.activity_image_grid, container, false);
        ViewGroup viewGroup = (ViewGroup) currentView.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(currentView);
        }
        context = getActivity();
        helper = AlbumHelper.getHelper();
        helper.init(context);
        Bundle bundle=getArguments();
        dataList=getDataList();
//        dataList = (List<ImageItem>) bundle.getSerializable(
//                EXTRA_IMAGE_LIST);

        initView();
        //bt = (Button) currentView.findViewById(R.id.bt);
//        bt.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                ArrayList<String> list = new ArrayList<String>();
//                Collection<String> c = adapter.map.values();
//                Iterator<String> it = c.iterator();
//                for (; it.hasNext();) {
//                    list.add(it.next());
//                }
//
//                if (BitmapUtils.act_bool) {
//                    transaction = getFragmentManager().beginTransaction();
//                    //transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);
//                    transaction.replace(R.id.other_layout, OtherActivity.fragmentHashMap.get(DreamPostFragment.TAG));
//                    //transaction.addToBackStack(null);
//                    transaction.commit();
//                    BitmapUtils.act_bool = false;
//                }
//                for (int i = 0; i < list.size(); i++) {
//                    if (BitmapUtils.drr.size() < 9) {
//                        BitmapUtils.drr.add(list.get(i));
//                    }
//                }
//                getActivity().finish();
//            }
//
//        });
        return currentView;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(context, "最多选择9张图片", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };

    private void initView() {
        titleBar=(TitleBar)currentView.findViewById(R.id.top_bar);
        gridView = (GridView) currentView.findViewById(R.id.photo_img_gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        titleBar.showLeftStrAndRightStr(getResources().getString(R.string.app_name), "取消", "", this, this);
        adapter = new ImageGridAdapter(getActivity(), dataList,
                mHandler);
        adapter.selectTotal=BitmapUtils.drr.size();
        gridView.setAdapter(adapter);
        BitmapUtils.act_bool = true;
        adapter.setTextCallback(new ImageGridAdapter.TextCallback() {
            public void onListen(int count) {
                titleBar.updateRightStr("完成" + "(" +count + ")");
                MyLog.i("ddd", BitmapUtils.drr.size() + ":size,count:" + count);
            }
        });
        titleBar.updateRightStr("完成" + "(" + BitmapUtils.drr.size() + ")");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /**
                 * 鏍规嵁position鍙傛暟锛屽彲浠ヨ幏寰楄窡GridView鐨勫瓙View鐩哥粦瀹氱殑瀹炰綋绫伙紝鐒跺悗鏍规嵁瀹冪殑isSelected鐘舵
                 * �锛� 鏉ュ垽鏂槸鍚︽樉绀洪�涓晥鏋溿� 鑷充簬閫変腑鏁堟灉鐨勮鍒欙紝涓嬮潰閫傞厤鍣ㄧ殑浠ｇ爜涓細鏈夎鏄�
                 */
                if (dataList.get(position).isSelected) {
                    dataList.get(position).isSelected = false;
                } else {
                    dataList.get(position).isSelected = true;
                }
                /**
                 * 閫氱煡閫傞厤鍣紝缁戝畾鐨勬暟鎹彂鐢熶簡鏀瑰彉锛屽簲褰撳埛鏂拌鍥�
                 */
                adapter.notifyDataSetChanged();


            }

        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titleBarRightStr:
                if(BitmapUtils.drr.size()==0)
                {
                    BitmapUtils.max=0;
                    BitmapUtils.bmp.clear();
                }
                ArrayList<String> list = new ArrayList<String>();
                Collection<String> c = adapter.map.values();
                Iterator<String> it = c.iterator();
                for (; it.hasNext();) {
                    list.add(it.next());
                }
                for (int i = 0; i < list.size(); i++) {
                    if (BitmapUtils.drr.size() < 9) {
                        BitmapUtils.drr.add(list.get(i));
                    }
                }
                if (BitmapUtils.act_bool) {
                    transaction = getFragmentManager().beginTransaction();
                    //transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);
                    transaction.replace(R.id.other_layout, OtherActivity.fragmentHashMap.get(DreamPostFragment.TAG));
                   // transaction.addToBackStack(null);
//                    transaction.remove(SelectImageGridFragment.this);
                    transaction.commit();
                    BitmapUtils.act_bool = false;
                }
                //getActivity().finish();
                break;
            default:
                break;
        }
    }

    public List<ImageItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<ImageItem> dataList) {
        this.dataList = dataList;
    }
}
