package com.example.mrpan.dreamtogether.entity;

import java.util.List;

/**
 * Created by mrpan on 16/3/23.
 */
public class UserPosts extends BaseEntity{
    private int ret;
    private List<User> post;

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
}