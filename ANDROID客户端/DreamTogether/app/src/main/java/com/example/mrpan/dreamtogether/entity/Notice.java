package com.example.mrpan.dreamtogether.entity;

/**
 * Created by mrpan on 16/5/11.
 */
public class Notice extends BaseEntity {
    private String noticeid;
    private String noticetype;
    private String noticefrom;
    private String noticefrom_head;
    private String noticecontent;
    private String isDispose;//是否已处理 0未处理，1已处理

    public String getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(String noticeid) {
        this.noticeid = noticeid;
    }

    public String getNoticetype() {
        return noticetype;
    }

    public void setNoticetype(String noticetype) {
        this.noticetype = noticetype;
    }

    public String getNoticefrom() {
        return noticefrom;
    }

    public void setNoticefrom(String noticefrom) {
        this.noticefrom = noticefrom;
    }

    public String getNoticecontent() {
        return noticecontent;
    }

    public void setNoticecontent(String noticecontent) {
        this.noticecontent = noticecontent;
    }

    public String getNoticefrom_head() {
        return noticefrom_head;
    }

    public void setNoticefrom_head(String noticefrom_head) {
        this.noticefrom_head = noticefrom_head;
    }

    public String getIsDispose() {
        return isDispose;
    }

    public void setIsDispose(String isDispose) {
        this.isDispose = isDispose;
    }
}
