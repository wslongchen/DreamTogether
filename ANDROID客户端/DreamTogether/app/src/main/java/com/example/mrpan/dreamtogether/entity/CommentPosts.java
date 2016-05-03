package com.example.mrpan.dreamtogether.entity;

import java.util.List;

/**
 * Created by mrpan on 16/5/1.
 */
public class CommentPosts extends BaseEntity {
    private int ret;
    private List<Comment> post;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public List<Comment> getPost() {
        return post;
    }

    public void setPost(List<Comment> post) {
        this.post = post;
    }
}
