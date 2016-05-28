package com.example.mrpan.dreamtogether.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.view.CustomProgressDialog;
import com.example.mrpan.dreamtogether.view.loadview.Load2Dialog;
import com.example.mrpan.dreamtogether.view.loadview.LoadDialog;

/**
 * Created by mrpan on 16/3/26.
 */
public class DialogUtils {
    /**
     * 获取一个加载提示dialog
     *
     * @param context
     * @return
     */
    public static Dialog getLoadDialog2(Activity context) {
        Load2Dialog dialog = new Load2Dialog(context);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        dialog.setCancelable(true);
        dialog.show();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.5);
        p.width = (int) (d.getWidth() * 0.5);
        p.height = p.width = Math.min(p.height , p.width);
        dialog.onWindowAttributesChanged(p);
        //p.dimAmount = 0f;// 设置背景不变暗
        window.setAttributes(p);
        return dialog;
    }

    public static CustomProgressDialog getCustomProgressDialog1(Context context) {
         return new CustomProgressDialog(context, "正在加载中", R.anim.frame);
    }

    public static CustomProgressDialog getCustomProgressDialog2(Context context) {
        return new CustomProgressDialog(context, "正在加载中", R.anim.frame2);
    }

    /**
     * 获取一个加载提示dialog
     *
     * @param context
     * @return
     */
    public static Dialog getLoadDialog(Activity context) {
        LoadDialog dialog = new LoadDialog(context);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
        dialog.setCancelable(true);
        dialog.show();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.2);
        p.width = (int) (d.getWidth() * 0.2);
        dialog.onWindowAttributesChanged(p);
        //p.dimAmount = 0f;// 设置背景不变暗
        window.setAttributes(p);
        return dialog;
    }

}
