package com.example.mrpan.dreamtogether.entity;

/**
 * Created by mrpan on 16/3/29.
 */
public class Meta extends BaseEntity {
    private int dumeta_id;
    private int user_id;
    private int post_id;
    private String meta_key;
    private String meta_value;

    public int getDumeta_id() {
        return dumeta_id;
    }

    public void setDumeta_id(int dumeta_id) {
        this.dumeta_id = dumeta_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMeta_key() {
        return meta_key;
    }

    public void setMeta_key(String meta_key) {
        this.meta_key = meta_key;
    }

    public String getMeta_value() {
        return meta_value;
    }

    public void setMeta_value(String meta_value) {
        this.meta_value = meta_value;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}
