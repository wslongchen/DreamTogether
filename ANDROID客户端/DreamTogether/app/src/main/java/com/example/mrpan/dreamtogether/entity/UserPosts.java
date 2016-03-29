package com.example.mrpan.dreamtogether.entity;

import java.util.List;

/**
 * Created by mrpan on 16/3/23.
 */
public class UserPosts extends BaseEntity{
    private int ret;
    private List<User> post;
    private List<Meta> meta;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public List<User> getPost() {
        return post;
    }

    public void setPost(List<User> post) {
        this.post = post;
    }

    public List<Meta> getMeta() {
        return meta;
    }

    public void setMeta(List<Meta> meta) {
        this.meta = meta;
    }
}