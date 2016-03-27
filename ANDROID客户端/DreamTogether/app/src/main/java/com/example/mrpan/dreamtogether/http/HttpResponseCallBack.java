package com.example.mrpan.dreamtogether.http;

/**
 * Created by mrpan on 15/12/4.
 */
public interface HttpResponseCallBack {

    public void onSuccess(String url, String result);

    public void onFailure(int httpResponseCode, int errCode, String err);


}
