package com.example.mrpan.dreamtogether.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.MyApplication;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.Md5Utils;
import com.example.mrpan.dreamtogether.utils.MyLog;
import com.example.mrpan.dreamtogether.utils.MySharePreference;
import com.example.mrpan.dreamtogether.utils.Network;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


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

    public void asyHttpPostRequest(String url,Map<String,String> params,List<String> files, HttpResponseCallBack httpCallBack) {


        if (null != appPreference) {
            int permission = appPreference.getInt(Config.TYPE_CONN, 0);
            Network netUtils =new Network(MyApplication.getInstance());
            Network.NetWorkState state = netUtils.getConnectState();
            if (state.equals(Network.NetWorkState.MOBILE)) {

                if (permission == Config.TYPE_ALL) {
                    threadPool.execute(getPostHttpThread(url, params,files, httpCallBack));
                } else if (permission == Config.TYPE_WIFI) {
                    httpCallBack.onFailure(0, Con_Permission,
                            "请在设置中打开MOBILE连接 ");
                    MyLog.i(TAG, "未发送请求，用户设置了网络限制");
                }
                // 未知网络
                else {
                    threadPool.execute(getPostHttpThread(url, params,files, httpCallBack));
                }

            } else {
                threadPool.execute(getPostHttpThread(url,params, files, httpCallBack));
            }

        } else {
            threadPool.execute(getPostHttpThread(url, params,files, httpCallBack));
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
    private Runnable getPostHttpThread(final String adress_Http, final String path, final HttpResponseCallBack httpCallBack){

        return new Runnable() {
            int responseCode = -1;
            OutputStream outtStream = null;
            String returnLine = "";
            BufferedReader reader = null;
            HttpURLConnection conn = null;
            URL url = null;
            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "******";

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
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    conn.connect();
                    DataOutputStream out = new DataOutputStream(conn
                            .getOutputStream());
                    out.writeBytes(twoHyphens + boundary + end);
                    out.writeBytes("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
                            + path.substring(path.lastIndexOf("/") + 1) + "\"" + end);
                    out.writeBytes(end);
                    FileInputStream fis = new FileInputStream(path);
                    byte[] buffer = new byte[8192]; // 8k
                    int count = 0;
                    // 读取文件
                    while ((count = fis.read(buffer)) != -1) {
                        out.write(buffer, 0, count);
                    }
                    fis.close();
                    out.writeBytes(end);
                    out.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    out.flush();
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


    private Runnable getPostHttpThread(final String adress_Http,final Map<String,String> map, final List<String> files, final HttpResponseCallBack httpCallBack){

        return new Runnable() {
            int responseCode = -1;
            OutputStream outtStream = null;
            String returnLine = "";
            BufferedReader reader = null;
            HttpURLConnection conn = null;
            URL url = null;
            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "******";

            @Override
            public void run() {
                try {
                    String dataStr=null;
                    if (map != null && map.size() != 0 && !map.isEmpty()) {
                        StringBuffer sb = new StringBuffer( );
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            // 如果请求参数中有中文，需要进行URLEncoder编码 gbk/utf8
                            sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8"));
                            sb.append("&");
                        }
                        sb.deleteCharAt(sb.length() - 1);
                        dataStr=sb.toString();
                    }

//                    if (map != null && !map.isEmpty()) {
//                        StringBuffer sb = new StringBuffer( );
//                        for (Map.Entry<String, String> entry : map.entrySet())
//                            sb.append("&").append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8"));
//                        dataStr=sb.toString().substring(1);
//                    }
                    url= new URL(adress_Http+"&"+dataStr);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");
                    conn.setUseCaches(false);
                    conn.setInstanceFollowRedirects(true);
                    conn.setRequestProperty("Content-Type", "application/octet-stream");
                    conn.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                    //conn.connect();
                    DataOutputStream out = new DataOutputStream(conn
                            .getOutputStream());


                    for(int i=0;i<files.size();i++){
                        String name=files.get(i);
                        String path=name.substring(name.lastIndexOf("/") + 1);
                        out.writeBytes(twoHyphens + boundary + end);
                        out.writeBytes("Content-Disposition: form-data; name=\"file"+i+"\"; filename=\""
                                + path + "\"" + end);
                        out.writeBytes(end);
                        FileInputStream fis = new FileInputStream(name);

                        byte[] buffer = new byte[1024]; // 8k
                        int length = -1;
                        while ((length = fis.read(buffer)) != -1) {
                            out.write(buffer, 0, length);
                        }
                        out.writeBytes(end);
                        fis.close();

                    }

                    out.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    out.flush();
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

    public static byte[] httpGet(final String url) {
        if (url == null || url.length() == 0) {
            return null;
        }

        HttpClient httpClient = getNewHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse resp = httpClient.execute(httpGet);
            if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {

                return null;
            }

            return EntityUtils.toByteArray(resp.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] httpPost(String url, String entity) {
        if (url == null || url.length() == 0) {

            return null;
        }

        HttpClient httpClient = getNewHttpClient();

        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new StringEntity(entity));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse resp = httpClient.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                MyLog.i(TAG, "httpGet fail, status code = " + resp.getStatusLine().getStatusCode());
                return null;
            }

            return EntityUtils.toByteArray(resp.getEntity());
        } catch (Exception e) {
            MyLog.i(TAG, "httpPost exception, e = " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    private static class SSLSocketFactoryEx extends SSLSocketFactory {

        SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,	String authType) throws java.security.cert.CertificateException {
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host,	port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
    public String verfyRealName(String userId, String realName,String idcard, String img1, String img2) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);
        params.put("realName", realName);
        params.put("idcard", idcard);
        List<String> files = new ArrayList<String>();
        files.add(img1);
        files.add(img2);

        /** 时间戳/MD5加密 */
        long TimeStamp = System.currentTimeMillis();
        String string3 = "sfd,.*-app" + TimeStamp + "verfyrealname";
        String string2 = md5(string3);
        params.put("sign", string2);
        params.put("timestamp", "" + TimeStamp);

        String url = "http://demo.sanfendai.cn/app/uc/verfyRealName";
        return uploadFileClient(url, params, img1, img2);
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static String uploadFileClient(String url,
                                          Map<String, String> params, String img1, String img2) {

        File targetFile = new File(img1);// 指定上传文件
        File targetFile2 = new File(img2);

        PostMethod filePost = new PostMethod(url);

        try {
            // 通过以下方法可以模拟页面参数提交
            // filePost.setParameter("name", "中文");
            // filePost.setParameter("pass", "1234");
            byte[] buffer = new byte[1024];
            Part[] parts = { new FilePart("img1", targetFile),
                    new FilePart("img2", targetFile2),new StringPart("sign",params.get("sign").toString()),new StringPart("timestamp",params.get("timestamp").toString()),
                    new StringPart("userId",params.get("userId").toString()),new StringPart("idcard",params.get("idcard").toString()),
                        new StringPart("realName",params.get("realName"))};
            if ((params != null) && !params.isEmpty()) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    filePost.setParameter(param.getKey(), param.getValue());
                     MyLog.i("data",param.getKey()+":key,value:"+param.getValue());
                }
            }
            filePost.setRequestEntity(new MultipartRequestEntity(parts,
                    filePost.getParams()));
            org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
            client.getHttpConnectionManager().getParams()
                    .setConnectionTimeout(5000);
            int status = client.executeMethod(filePost);
            MyLog.i("TEST",status+",1111");
            if (status == HttpStatus.SC_OK) {
                String str = "";
                str = filePost.getResponseBodyAsString();
                MyLog.i("TEST",str);
                return str;
                // 上传成功
            } else {
                String str = "";
                str = filePost.getResponseBodyAsString();
                MyLog.i("TEST",str);
                return str;
                // 上传失败
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            filePost.releaseConnection();
        }
        return "";
    }

    public String uploadFileClient2(String url, Map<String, String> params,String img1,String image2)
    {

        File targetFile = new File(img1);// 指定上传文件
        File targetFile2 = new File(image2);

        PostMethod filePost = new PostMethod(url);

        try
        {

            // 通过以下方法可以模拟页面参数提交
            // filePost.setParameter("name", "中文");
            // filePost.setParameter("pass", "1234");
            byte[] buffer = new byte[1024];
            Part[] parts =
                    { new FilePart("img1",
                            targetFile),new FilePart("img2",
                            targetFile2)};
            if ((params != null) && !params.isEmpty()) {
                for (Map.Entry<String, String> param : params.entrySet()) {
                    filePost.setParameter(param.getKey(),param.getValue());
                }
            }
            filePost.setRequestEntity(new MultipartRequestEntity(parts,
                    filePost.getParams()));
            org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
            client.getHttpConnectionManager().getParams()
                    .setConnectionTimeout(5000);
            int status = client.executeMethod(filePost);

            if (status == HttpStatus.SC_OK)
            {
                String str = "";
                str = filePost.getResponseBodyAsString();
                return str;
                // 上传成功
            } else
            {
                String str = "";
                str = filePost.getResponseBodyAsString();
                return str;
                // 上传失败
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        } finally
        {
            filePost.releaseConnection();
        }
        return "";
    }


}
