package com.example.mrpan.dreamtogether.entity;

/**
 * Created by mrpan on 16/6/6.
 */
public class Place extends BaseEntity{
    private String addr;
    private String latitude;
    private String lontitude;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLontitude() {
        return lontitude;
    }

    public void setLontitude(String lontitude) {
        this.lontitude = lontitude;
    }
}
