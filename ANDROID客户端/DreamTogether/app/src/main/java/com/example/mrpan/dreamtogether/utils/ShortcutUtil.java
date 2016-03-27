package com.example.mrpan.dreamtogether.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;

/**
 * 创建快捷方式的工具类
 */
public class ShortcutUtil {
    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
    private Context context;
    private int redPointHeight = 0;
    private int redPointTextSize = 0;
    private Paint paint;
    private int width = 0;
    private Matrix matrix;

    public ShortcutUtil(Context context) {
        this.context = context;
        // 创建画笔
        paint = new Paint();
        resetPaint();
        width = DensityUtil.dip2px(this.context,56);
    }

    /**
     * 添加一个带有消息数量的快捷方式
     * @param name 快捷方式名
     * @param size 数量
     * @param bitmap 图标
     * @param cls 点击跳转目标
     */
    public void addShortcut(String name, int size, int bitmap,Class cls){
        String s;
        if(size<0)
            s = "";
        else if(size>99)
            s="99+";
        else
            s=size+"";
        addShortcut(name,s,bitmap,cls);
    }

    /**
     * 创建一个带有文字提示的快捷方式
     * @param name 图标名
     * @param size 小红点内容
     * @param bitmap 图片地址
     * @param cls 跳转目标
     */
    @SuppressLint("NewApi")
    public void addShortcut(String name, String size, int bitmap,Class cls){
        // 创建一张空白图片
        Bitmap bit = BitmapFactory.decodeResource(context.getResources(), bitmap);//直接拿出来的是不可更改的位图
//        Bitmap b = Bitmap.createBitmap(bit.getWidth(), bit.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap b = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        redPointHeight = (int) (b.getHeight()*0.35);
        redPointTextSize = (int) (redPointHeight*0.7);
        paint.setTextSize(redPointTextSize);
        // 创建一张画布
        Canvas canvas = new Canvas(b);
        getCroppedRoundBitmap(bit, width / 2);
        canvas.drawRect(0, 0, width, width, paint);
        //画红色小点
        resetPaint();
        paint.setColor(Color.RED);
        int left;
        if(size.length()>1)
            left = width - redPointHeight - (size.length()-1) * redPointTextSize/2;
        else
            left = width - redPointHeight;
        if(left<0){
            left = 0;
        }
        canvas.drawRoundRect(left ,
                0, width, redPointHeight, redPointHeight / 2, redPointHeight / 2, paint);
        //画字
        paint.setColor(Color.WHITE);
        Paint.FontMetrics fm = paint.getFontMetrics();
        float textY = redPointHeight/2-fm.descent + (fm.descent - fm.ascent) / 2;
        int textX = width-(width - left)/2;
        if(size.length()*redPointTextSize/2>width-redPointHeight)
            size = size.substring(0,(width-redPointHeight)/redPointTextSize);
        canvas.drawText(size,textX,textY,paint);
        addShortcut(name,b,cls);
    }

    /**
     * 生成一个指定图片的快捷方式
     * @param name 名称
     * @param bitmap 图片
     * @param cls 跳转目标
     */
    public void addShortcut(String name, Bitmap bitmap,Class cls) {
        Intent addShortCutIntent = new Intent(ACTION_ADD_SHORTCUT);

        // 不允许重复创建
        addShortCutIntent.putExtra("duplicate", false);// 经测试不是根据快捷方式的名字判断重复的
        // 应该是根据快链的Intent来判断是否重复的,即Intent.EXTRA_SHORTCUT_INTENT字段的value
        // 但是名称不同时，虽然有的手机系统会显示Toast提示重复，仍然会建立快链
        // 屏幕上没有空间时会提示
        // 注意：重复创建的行为MIUI和三星手机上不太一样，小米上似乎不能重复创建快捷方式
        //用于点击快捷方式要启动的程序，这里就启动本程序了
        Intent startIntent = new Intent(context, cls);
        //快捷方式的名称
        addShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        //快捷方式的图标
        addShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
//        addShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        //将快捷方式与要启动的程序关联起来
        addShortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, startIntent);
        // 发送广播
        context.sendBroadcast(addShortCutIntent);
    }

    public void deleteShortcut(String name,Class cls) {
        // remove shortcut的方法在小米系统上不管用，在三星上可以移除
        Intent intent = new Intent(ACTION_REMOVE_SHORTCUT);
        // 名字
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        //用于点击快捷方式要启动的程序，这里就启动本程序了
        Intent startIntent = new Intent(context, cls);
        //将快捷方式与要启动的程序关联起来
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, startIntent);
        // 发送广播
        context.sendBroadcast(intent);
    }
    /**
     * 使用渲染来绘制圆形图片
     *
     * @param bmp  图片
     * @param radius 半径
     * @return
     */
    public void getCroppedRoundBitmap(Bitmap bmp, int radius) {
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        Shader mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
        scale = Math.max(radius * 2.0f / bmp.getWidth(), radius * 2.0f / bmp.getHeight());
        if(matrix==null){
            matrix = new Matrix();
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        matrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(matrix);
        // 设置shader
        paint.setShader(mBitmapShader);
    }

    private void resetPaint(){
        paint.reset();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setTextSize(redPointTextSize);
    }

}
