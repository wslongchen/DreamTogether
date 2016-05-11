package com.example.mrpan.dreamtogether.entity;

/**
 * Created by mrpan on 16/4/30.
 */
public class Comment extends BaseEntity{
    private int ID;
    private String post_id;
    private User comment_user_id;
    private String comment_content;
    private String comment_time;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public User getComment_user_id() {
        return comment_user_id;
    }

    public void setComment_user_id(User comment_user_id) {
        this.comment_user_id = comment_user_id;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }
}
