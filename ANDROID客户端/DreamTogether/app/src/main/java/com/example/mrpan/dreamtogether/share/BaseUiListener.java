package com.example.mrpan.dreamtogether.share;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * Created by mrpan on 16/5/23.
 */
public class BaseUiListener implements IUiListener {

    int type;

    public BaseUiListener(int type){
        this.type=type;
    }

    @Override
    public void onComplete(Object o) {

    }

    @Override
    public void onError(UiError uiError) {

    }

    @Override
    public void onCancel() {

    }
}
