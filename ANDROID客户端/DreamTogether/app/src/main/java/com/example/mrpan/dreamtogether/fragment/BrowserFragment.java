package com.example.mrpan.dreamtogether.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.http.HttpHelper;
import com.example.mrpan.dreamtogether.view.NoScrollGridView;
import com.example.mrpan.dreamtogether.view.TitleBar;
import com.example.mrpan.dreamtogether.view.WaterRefreshView.WaterDropListView;

/**
 * Created by mrpan on 16/5/4.
 */
public class BrowserFragment extends Fragment{
    public final static String TAG="Browser";


    private Context context;

    private TitleBar titleBar;

    private FragmentTransaction transaction;

    private View currentView;

    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView=inflater.inflate(R.layout.browser_fragment,container,false);
        ViewGroup viewGroup= (ViewGroup) currentView.getParent();
        if(viewGroup!=null){
            viewGroup.removeView(currentView);
        }
        context=getActivity();
        initView();
        return currentView;
    }

    private void initView(){
        webView=(WebView)currentView.findViewById(R.id.browser);
        webView.loadUrl("file:///android_asset/index.html");
        WebSettings webSettings =   webView.getSettings();
        webSettings.setUseWideViewPort(false);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
    }
}