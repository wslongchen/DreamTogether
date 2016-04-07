package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.ImageBucket;
import com.example.mrpan.dreamtogether.utils.AlbumHelper;
import com.example.mrpan.dreamtogether.utils.DreamPostGridAdapter;
import com.example.mrpan.dreamtogether.utils.ImageBucketAdapter;
import com.example.mrpan.dreamtogether.view.TitleBar;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mrpan on 16/3/31.
 */
public class PicSelectFragment extends Fragment {
    public static final String TAG = "PicSelect";

    private View currentView = null;
    private Context context = null;


    private FragmentTransaction transaction;

    private TitleBar titleBar;

    List<ImageBucket> dataList;
    GridView gridView;
    ImageBucketAdapter adapter;// 自定义的适配器
    AlbumHelper helper;
    public static final String EXTRA_IMAGE_LIST = "imagelist";
    public static Bitmap bitmap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.activity_image_bucket, container, false);
        ViewGroup viewGroup = (ViewGroup) currentView.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(currentView);
        }
        context = getActivity();

        helper = AlbumHelper.getHelper();
        helper.init(context);

        initData();
        initView();
        return currentView;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // /**
        // * 这里，我们假设已经从网络或者本地解析好了数据，所以直接在这里模拟了10个实体类，直接装进列表中
        // */
        // dataList = new ArrayList<Entity>();
        // for(int i=-0;i<10;i++){
        // Entity entity = new Entity(R.drawable.picture, false);
        // dataList.add(entity);
        // }
        dataList = helper.getImagesBucketList(false);
        bitmap= BitmapFactory.decodeResource(
                getResources(),
                R.drawable.icon_addpic_unfocused);
    }

    /**
     * 初始化view视图
     */
    private void initView() {
        gridView = (GridView) currentView.findViewById(R.id.gridview);
        adapter = new ImageBucketAdapter(getActivity(), dataList);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /**
                 * 根据position参数，可以获得跟GridView的子View相绑定的实体类，然后根据它的isSelected状态，
                 * 来判断是否显示选中效果。 至于选中效果的规则，下面适配器的代码中会有说明
                 */
                // if(dataList.get(position).isSelected()){
                // dataList.get(position).setSelected(false);
                // }else{
                // dataList.get(position).setSelected(true);
                // }
                /**3
                 * 通知适配器，绑定的数据发生了改变，应当刷新视图
                 */
                // adapter.notifyDataSetChanged();
                transaction = getFragmentManager().beginTransaction();
                //Bundle bundle = new Bundle();
                //bundle.putSerializable(PicSelectFragment.EXTRA_IMAGE_LIST,
                  //      (Serializable) dataList.get(position).imageList);
                ((SelectImageGridFragment)OtherActivity.fragmentHashMap.get(SelectImageGridFragment.TAG)).setDataList(dataList.get(position).imageList);

                transaction.replace(R.id.other_layout, OtherActivity.fragmentHashMap.get(SelectImageGridFragment.TAG));
                //OtherActivity.fragmentHashMap.get(SelectImageGridFragment.TAG).setArguments(bundle);
                //transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);
                //transaction.addToBackStack(null);
                transaction.commit();
            }

        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
