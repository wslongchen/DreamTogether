package com.example.mrpan.dreamtogether.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;
import java.util.Random;

/**
 * Created  on 2016/2/24.
 * 生成二维码的工具类
 */
public class MakeQRCodeUtils {


    /**
     * 根据指定内容生成自定义宽高的二维码图片
     *
     * param logoBm
     *            logo图标
     * param content
     *            需要生成二维码的内容
     * param width
     *            二维码宽度
     * param height
     *            二维码高度
     * throws WriterException
     *             生成二维码异常
     */
    public static Bitmap makeQRImage(Bitmap logoBmp, String content,
                                     int QR_WIDTH, int QR_HEIGHT) throws WriterException {
        try {

            // 需要引入core包

            // 图像数据转换，使用了矩阵转换
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 容错率
           // hints.put(EncodeHintType.MARGIN, 2); // default is 4
            hints.put(EncodeHintType.MAX_SIZE, 350);
            hints.put(EncodeHintType.MIN_SIZE, 100);

            BitMatrix bitMatrix = new QRCodeWriter().encode(content,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {

                // 下面这里按照二维码的算法，逐个生成二维码的图片，//两个for循环是图片横列扫描的结果
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        if (x < QR_WIDTH / 2 && y < QR_HEIGHT / 2) {
                            pixels[y * QR_WIDTH + x] = 0xFF0094FF;// 蓝色
                            Integer.toHexString(new Random().nextInt());
                        } else if (x < QR_WIDTH / 2 && y > QR_HEIGHT / 2) {
                            pixels[y * QR_WIDTH + x] = 0xFFFED545;// 黄色
                        } else if (x > QR_WIDTH / 2 && y > QR_HEIGHT / 2) {
                            pixels[y * QR_WIDTH + x] = 0xFF5ACF00;// 绿色
                        } else {
                            pixels[y * QR_WIDTH + x] = 0xFF000000;// 黑色
                        }

                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;// 白色
                    }

                }
            }
            // ------------------添加图片部分------------------//

            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            // 设置像素点

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            // 获取图片宽高
            int logoWidth = logoBmp.getWidth();
            int logoHeight = logoBmp.getHeight();

            if (QR_WIDTH == 0 || QR_HEIGHT == 0) {
                return null;
            }

            if (logoWidth == 0 || logoHeight == 0) {
                return bitmap;
            }

            // 图片绘制在二维码中央，合成二维码图片
            // logo大小为二维码整体大小的1/4
            float scaleFactor = QR_WIDTH * 1.0f / 4 / logoWidth;
            try {
                Canvas canvas = new Canvas(bitmap);
                canvas.drawBitmap(bitmap, 0, 0, null);
                canvas.scale(scaleFactor, scaleFactor, QR_WIDTH / 2,
                        QR_HEIGHT / 2);
                canvas.drawBitmap(logoBmp, (QR_WIDTH - logoWidth) / 2,
                        (QR_HEIGHT - logoHeight) /2, null);
                canvas.save(Canvas.ALL_SAVE_FLAG);
                canvas.restore();
                return bitmap;
            } catch (Exception e) {
                bitmap = null;
                e.getStackTrace();
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return null;
    }
    /**
     * 获取十六进制的颜色代码.例如  "#6E36B4" , For HTML ,
     * @return String
     */
    public static String getRandColorCode(){
        String r,g,b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length()==1 ? "0" + r : r ;
        g = g.length()==1 ? "0" + g : g ;
        b = b.length()==1 ? "0" + b : b ;

        return r+g+b;
    }
    /**
     * 根据指定内容生成自定义宽高的二维码图片
     *
     * @param content
     *            需要生成二维码的内容
     * @param width
     *            二维码宽度
     * @param height
     *            二维码高度
     * @throws WriterException
     *             生成二维码异常
     */
    public static Bitmap makeQRImage(String content, int width, int height)
            throws WriterException {


        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 图像数据转换，使用了矩阵转换
        BitMatrix bitMatrix = new QRCodeWriter().encode(content,
                BarcodeFormat.QR_CODE, width, height, hints);
        int[] pixels = new int[width * height];
        // 按照二维码的算法，逐个生成二维码的图片，两个for循环是图片横列扫描的结果
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bitMatrix.get(x, y))
                    pixels[y * width + x] = 0xff000000;
                else
                    pixels[y * width + x] = 0xffffffff;
            }
        }
        // 生成二维码图片的格式，使用ARGB_8888
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    /**
     * 从资源文件中获取图片
     *
     * @param context
     *            上下文
     * @param drawableId
     *            资源文件id
     * @return
     */
    public static Bitmap gainBitmap(Context context, int drawableId) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                drawableId);
        return bmp;
    }
    /**
     * 在图片右下角添加水印
     *
     * @param srcBMP
     *            原图
     * @param markBMP
     *            水印图片
     * @return 合成水印后的图片
     */
    public static Bitmap composeWatermark(Bitmap srcBMP, Bitmap markBMP) {
        if (srcBMP == null) {
            return null;
        }
        // 创建一个新的和SRC长度宽度一样的位图
        Bitmap newb = Bitmap.createBitmap(srcBMP.getWidth(),
                srcBMP.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newb);
        // 在 0，0坐标开始画入原图
        cv.drawBitmap(srcBMP, 0, 0, null);
        // 在原图的右下角画入水印
        cv.drawBitmap(markBMP, srcBMP.getWidth() - markBMP.getWidth()*4/5,
                srcBMP.getHeight()*2/7 , null);
        // 保存
        cv.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        cv.restore();

        return newb;
    }
    /**
     * 给二维码图片加背景
     *
     */
    public static Bitmap addBackground(Bitmap foreground,Bitmap background){
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();
        Bitmap newmap = Bitmap
                .createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(foreground, (bgWidth - fgWidth) / 2,
                (bgHeight - fgHeight) *3 / 5+70, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return newmap;
    }
}
