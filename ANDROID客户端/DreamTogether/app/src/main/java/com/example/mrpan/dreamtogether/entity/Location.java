package com.example.mrpan.dreamtogether.entity;

/**
 * Created by mrpan on 16/5/26.
 */
public class Location extends BaseEntity{
    private String latitude;//纬度
    private String lontitude;//经度
    private String addr;

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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
