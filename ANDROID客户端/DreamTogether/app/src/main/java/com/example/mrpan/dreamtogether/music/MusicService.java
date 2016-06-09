package com.example.mrpan.dreamtogether.music;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.mrpan.dreamtogether.R;
import com.example.mrpan.dreamtogether.utils.Config;
import com.example.mrpan.dreamtogether.utils.MyLog;

import java.io.IOException;

/**
 * Created by mrpan on 16/6/7.
 */
public class MusicService extends Service {

    private static MusicService instance=null;
    private MusicService mInstance=null;

    MediaPlayer mediaPlayer = new MediaPlayer();

    private int current_position;
    private final IBinder binder = new MyBinder();

    private ProgressChangeReceiver progressChangeReceiver=new ProgressChangeReceiver();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    public static MusicService getInstance() {
        return instance;
    }
    
    private class ProgressChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            current_position = intent.getIntExtra("current_position", 0);
            boolean isTouch=intent.getBooleanExtra("touch",false);
            if(isTouch){
                mediaPlayer.pause();
            }else{
                    mediaPlayer.seekTo(current_position*1024);
                    mediaPlayer.start();
            }


        }
    }

    private void play(){
        try{
            mediaPlayer.reset();
            mediaPlayer=MediaPlayer.create(this, R.raw.music);//重新设置要播放的音频
            mediaPlayer.start();//开始播放
        }catch(Exception e){
            e.printStackTrace();//输出异常信息
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        instance=this;
        play();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer arg0) {
                // TODO Auto-generated method stub
                // play();//重新开始播放
            }
        });
        Intent intent1=new Intent();
        intent1.setAction("info");
        current_position = mediaPlayer.getCurrentPosition();
        intent1.putExtra("duration", ((int) (mediaPlayer.getDuration() / 1000)));
        sendBroadcast(intent1);
        new Thread(new Runnable() {
            public void run() {
               if(mediaPlayer!=null){
                   while (mediaPlayer.isPlaying()) {
                       Intent intent = new Intent();
                       intent.setAction("current");
                       current_position = mediaPlayer.getCurrentPosition();
                       intent.putExtra("current", ((int)(current_position/1000)));
                       //MyLog.i("current",((int)(current_position/1000))+",duration:"+ ((int)(mediaPlayer.getDuration()/1000)));
                       sendBroadcast(intent);
                       try {
                           Thread.sleep(1000);
                       } catch (InterruptedException e) {
                           // TODO Auto-generated catch block
                           e.printStackTrace();
                       }
                   }
               }
            }
        }).start();

        IntentFilter intentProgressChangeFilter = new IntentFilter();

        intentProgressChangeFilter.addAction(Config.ACTION_PROGRESSBAR);

        registerReceiver(progressChangeReceiver, intentProgressChangeFilter);
    }

    @Override
    public void onDestroy() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.release();//释放资源
        unregisterReceiver(progressChangeReceiver);
        stopSelf();
        MyLog.i("music","dddddddddddd");
        super.onDestroy();
    }
}
