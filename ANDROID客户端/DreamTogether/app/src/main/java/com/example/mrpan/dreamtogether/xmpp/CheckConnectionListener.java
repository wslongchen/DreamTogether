package com.example.mrpan.dreamtogether.xmpp;

import android.widget.Toast;

import org.jivesoftware.smack.ConnectionListener;

/**
 * Created by mrpan on 16/5/9.
 */
public class CheckConnectionListener implements ConnectionListener {

    private MySystemService context;

    public CheckConnectionListener(MySystemService context) {
        this.context = context;
    }

    @Override
    public void connectionClosed() {
        // TODO Auto-generated method stub

    }

    @Override
    public void connectionClosedOnError(Exception e) {
        if (e.getMessage().equals("stream:error (conflict)")) {
            Toast.makeText(context, "您的账号在异地登录",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void reconnectingIn(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reconnectionFailed(Exception arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void reconnectionSuccessful() {
        // TODO Auto-generated method stub

    }
}