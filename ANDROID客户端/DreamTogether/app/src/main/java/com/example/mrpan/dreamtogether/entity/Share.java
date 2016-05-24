package com.example.mrpan.dreamtogether.entity;

/**
 * Created by mrpan on 16/5/23.
 */
public class Share extends BaseEntity {
    String TITLE;
    String SUMMARY;
    String TARGET_URL;
    String IMAGE_URL;
    String AUDIO_URL;
    String APP_NAME;

    public Share(){
        APP_NAME="梦想";
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getSUMMARY() {
        return SUMMARY;
    }

    public void setSUMMARY(String SUMMARY) {
        this.SUMMARY = SUMMARY;
    }

    public String getTARGET_URL() {
        return TARGET_URL;
    }

    public void setTARGET_URL(String TARGET_URL) {
        this.TARGET_URL = TARGET_URL;
    }

    public String getIMAGE_URL() {
        return IMAGE_URL;
    }

    public void setIMAGE_URL(String IMAGE_URL) {
        this.IMAGE_URL = IMAGE_URL;
    }

    public String getAPP_NAME() {
        return APP_NAME;
    }

    public void setAPP_NAME(String APP_NAME) {
        this.APP_NAME = APP_NAME;
    }

    public String getAUDIO_URL() {
        return AUDIO_URL;
    }

    public void setAUDIO_URL(String AUDIO_URL) {
        this.AUDIO_URL = AUDIO_URL;
    }
}
