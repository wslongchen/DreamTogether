package com.example.mrpan.dreamtogether.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.mrpan.dreamtogether.MyApplication;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.utils.MySharePreference;
import com.example.mrpan.dreamtogether.utils.Network;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by mrpan on 15/12/3.
 */
public class HttpHelper {

    public static final int NULL_INPUTSTREAM = -1;
    public static final int REQUEST_FAIL = -2;
    public static final int URL_Exception = -3;
    public static final int IO_Exception = -4;
    // 无网络访问权限
    public static final int Con_Permission = -5;

    //
    public static final String TAG = "HttpGetClient";
    private static HttpHelper mHttpGetClient = null;
    private static ExecutorService threadPool = null;

    //配置文件设置

    MySharePreference appPreference = null;

    private HttpHelper() {
        //
        int size = Runtime.getRuntime().availableProcessors();
        threadPool = Executors.newFixedThreadPool(size);
        if (null != MyApplication.getInstance())
            appPreference = new MySharePreference(MyApplication.getInstance());
    }

    //
    public static synchronized HttpHelper getInstance() {
        if (null == mHttpGetClient) {
            mHttpGetClient = new HttpHelper();
        }
        return mHttpGetClient;
    }

    public void asyHttpGetRequest(String url, HttpResponseCallBack httpCallBack) {


        if (null != appPreference) {
            int permission = appPreference.getInt(Config.TYPE_CONN, 0);
            Network netUtils =new Network(MyApplication.getInstance());
            Network.NetWorkState state = netUtils.getConnectState();
            if (state.equals(Network.NetWorkState.MOBILE)) {

                if (permission == Config.TYPE_ALL) {
                    threadPool.execute(getGetHttpThread(url, httpCallBack));
                } else if (permission == Config.TYPE_WIFI) {
                    httpCallBack.onFailure(0, Con_Permission,
                            "请在设置中打开MOBILE连接 ");
                    MyLog.i(TAG, "未发送请求，用户设置了网络限制");
                }
                // 未知网络
                else {
                    threadPool.execute(getGetHttpThread(url, httpCallBack));
                }

            } else {
                threadPool.execute(getGetHttpThread(url, httpCallBack));
            }

        } else {
            threadPool.execute(getGetHttpThread(url, httpCallBack));
        }

    }

    public void asyHttpPostRequest(String url,String JsonStr, HttpResponseCallBack httpCallBack) {


        if (null != appPreference) {
            int permission = appPreference.getInt(Config.TYPE_CONN, 0);
            Network netUtils =new Network(MyApplication.getInstance());
            Network.NetWorkState state = netUtils.getConnectState();
            if (state.equals(Network.NetWorkState.MOBILE)) {

                if (permission == Config.TYPE_ALL) {
                    threadPool.execute(getPostHttpThread(url, JsonStr, httpCallBack));
                } else if (permission == Config.TYPE_WIFI) {
                    httpCallBack.onFailure(0, Con_Permission,
                            "请在设置中打开MOBILE连接 ");
                    MyLog.i(TAG, "未发送请求，用户设置了网络限制");
                }
                // 未知网络
                else {
                    threadPool.execute(getPostHttpThread(url, JsonStr, httpCallBack));
                }

            } else {
                threadPool.execute(getPostHttpThread(url, JsonStr, httpCallBack));
            }

        } else {
            threadPool.execute(getPostHttpThread(url, JsonStr, httpCallBack));
        }

    }

    public void asyHttpPostRequest(String url,List<NameValuePair> params, HttpResponseCallBack httpCallBack) {


        if (null != appPreference) {
            int permission = appPreference.getInt(Config.TYPE_CONN, 0);
            Network netUtils =new Network(MyApplication.getInstance());
            Network.NetWorkState state = netUtils.getConnectState();
            if (state.equals(Network.NetWorkState.MOBILE)) {

                if (permission == Config.TYPE_ALL) {
                    threadPool.execute(getPostHttpThread(url, params, httpCallBack));
                } else if (permission == Config.TYPE_WIFI) {
                    httpCallBack.onFailure(0, Con_Permission,
                            "请在设置中打开MOBILE连接 ");
                    MyLog.i(TAG, "未发送请求，用户设置了网络限制");
                }
                // 未知网络
                else {
                    threadPool.execute(getPostHttpThread(url, params, httpCallBack));
                }

            } else {
                threadPool.execute(getPostHttpThread(url, params, httpCallBack));
            }

        } else {
            threadPool.execute(getPostHttpThread(url, params, httpCallBack));
        }

    }

    private Runnable getGetHttpThread(final String urlStr,
                                      final HttpResponseCallBack httpCallBack) {

        return new Runnable() {
            int responseCode = -1;
            InputStream inputStream = null;

            BufferedReader reader = null;
            HttpURLConnection conn = null;
            URL url = null;

            @Override
            public void run() {
                try {
                    url = new URL(urlStr);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);
                    conn.setDoInput(true);
                    conn.setDoOutput(false);
                    conn.setUseCaches(true);
                    conn.setRequestProperty("User-agent", "Mozilla/5.0");
                    conn.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    conn.connect();
                    // outputStream = conn.getOutputStream();
                    // outputStream.write(params.toString().getBytes());
                    responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        inputStream = conn.getInputStream();
                        if (null != inputStream) {
                            reader = new BufferedReader(new InputStreamReader(
                                    inputStream, "utf-8"));
                            StringBuilder strBuilder = new StringBuilder();
                            // int i = 0;
                            // char[] buf = new char[1024];
                            // while ((i = reader.read(buf)) != -1) {
                            // strBuilder.append(buf, 0, i);
                            // }

                            String line = null;
                            while (null != (line = reader.readLine()))
                                strBuilder.append(line);

                            httpCallBack.onSuccess(urlStr,
                                    strBuilder.toString());
                        } else {
                            httpCallBack.onFailure(responseCode,
                                    NULL_INPUTSTREAM, "读取数据失败！");
                            MyLog.i(TAG, "读取数据失败！");
                        }

                    } else {
                        httpCallBack.onFailure(responseCode, REQUEST_FAIL,
                                "请求失败！");
                    }

                } catch (MalformedURLException e) {
                    httpCallBack.onFailure(responseCode, URL_Exception,
                            e.getMessage());
                    MyLog.i(TAG, e.toString());
                } catch (IOException e) {
                    httpCallBack.onFailure(responseCode, IO_Exception,
                            e.getMessage());
                    MyLog.i(TAG, e.toString());
                } finally {
                    try {
                        if (null != reader)
                            reader.close();
                    } catch (IOException ex) {
                        MyLog.i(TAG, ex.toString());
                    }
                    try {
                        if (null != inputStream)
                            inputStream.close();
                    } catch (IOException ex) {
                        MyLog.i(TAG, ex.toString());
                    }
                }

                if (null != conn)
                    conn.disconnect();
            }
        };

    }
    private Runnable getPostHttpThread(final String adress_Http, final String params, final HttpResponseCallBack httpCallBack){

        return new Runnable() {
            int responseCode = -1;
            OutputStream outtStream = null;
            String returnLine = "";
            BufferedReader reader = null;
            HttpURLConnection conn = null;
            URL url = null;

            @Override
            public void run() {
                try {
                    url= new URL(adress_Http);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");
                    conn.setUseCaches(false);
                    conn.setInstanceFollowRedirects(true);
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.connect();
                    DataOutputStream out = new DataOutputStream(conn
                            .getOutputStream());
                    byte[] content = params.getBytes("utf-8");
                    out.write(content, 0, content.length);
                   responseCode=conn.getResponseCode();
                    if (responseCode == 200) {
                        reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                        String line = "";
                        while ((line = reader.readLine()) != null) {
                            // line = new String(line.getBytes(), "utf-8");
                            returnLine += line;
                        }
                        httpCallBack.onSuccess(adress_Http,
                                returnLine);

                    } else {
                        httpCallBack.onFailure(responseCode, REQUEST_FAIL,
                                "请求失败！");
                    }

                } catch (MalformedURLException e) {
                    httpCallBack.onFailure(responseCode, URL_Exception,
                            e.getMessage());
                    MyLog.i(TAG, e.toString());
                } catch (IOException e) {
                    httpCallBack.onFailure(responseCode, IO_Exception,
                            e.getMessage());
                    MyLog.i(TAG, e.toString());
                } finally {
                    try {
                        if (null != reader)
                            reader.close();
                    } catch (IOException ex) {
                        MyLog.i(TAG, ex.toString());
                    }
                    try {
                        if (null != outtStream)
                        {
                            outtStream.flush();
                            outtStream.close();
                        }
                    } catch (IOException ex) {
                        MyLog.i(TAG, ex.toString());
                    }
                }

                if (null != conn)
                    conn.disconnect();
            }
        };
    }

    private Runnable getPostHttpThread(final String adress_Http, final List<NameValuePair> params, final HttpResponseCallBack httpCallBack){

        return new Runnable() {
            int responseCode = -1;
            URL url = null;

            @Override
            public void run() {
                try{

                    HttpPost httpRequest =new HttpPost(adress_Http);
                    //发出HTTP request
                   // HttpEntity s=;
                    httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
                    //取得HTTP response
                    HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
                    //若状态码为200 ok
                    if(httpResponse.getStatusLine().getStatusCode()==200){
                        //取出回应字串
                        //String strResult= EntityUtils.toString(httpResponse.getEntity());
                        String strResult = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

                        httpCallBack.onSuccess(adress_Http,
                                URLDecoder.decode(strResult, "utf-8"));


                    } else {
                        httpCallBack.onFailure(responseCode, REQUEST_FAIL,
                                "请求失败！");
                    }
                }catch(ClientProtocolException e){
                    httpCallBack.onFailure(responseCode, URL_Exception,
                            e.getMessage());
                    MyLog.i(TAG, e.toString());
                } catch (UnsupportedEncodingException e) {
                    httpCallBack.onFailure(responseCode, URL_Exception,
                            e.getMessage());
                    MyLog.i(TAG, e.toString());
                } catch (IOException e) {
                    httpCallBack.onFailure(responseCode, IO_Exception,
                            e.getMessage());
                    MyLog.i(TAG, e.toString());
                }
            }
        };
    }

    /**
     * 获取网络图片资源
     * @param url
     * @return
     */
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return bitmap;

    }

}
