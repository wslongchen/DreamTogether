package com.example.mrpan.dreamtogether.entity;

import java.util.List;
import java.util.Objects;

/**
 * Created by mrpan on 16/3/18.
 */
public class DreamPosts extends BaseEntity{
    private int ret;
    private List<Dream> post;
    Object s;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public List<Dream> getPosts() {
        return post;
    }

    public void setPosts(List<Dream> posts) {
        this.post = posts;
    }
}
