package com.example.mrpan.dreamtogether.entity;

/**
 * Created by mrpan on 16/3/16.
 */
public class Dream extends BaseEntity{
    private int ID;
    private User post_author;
    private String post_date;
    private String post_content;
    private String post_titile;
    private String post_status;
    private String post_password;
    private String post_guid;
    private String post_type;
    private String post_comment_status;
    private String post_comment_count;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public User getPost_author() {
        return post_author;
    }

    public void setPost_author(User post_author) {
        this.post_author = post_author;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_titile() {
        return post_titile;
    }

    public void setPost_titile(String post_titile) {
        this.post_titile = post_titile;
    }

    public String getPost_status() {
        return post_status;
    }

    public void setPost_status(String post_status) {
        this.post_status = post_status;
    }

    public String getPost_password() {
        return post_password;
    }

    public void setPost_password(String post_password) {
        this.post_password = post_password;
    }

    public String getPost_guid() {
        return post_guid;
    }

    public void setPost_guid(String post_guid) {
        this.post_guid = post_guid;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getPost_comment_status() {
        return post_comment_status;
    }

    public void setPost_comment_status(String post_comment_status) {
        this.post_comment_status = post_comment_status;
    }

    public String getPost_comment_count() {
        return post_comment_count;
    }

    public void setPost_comment_count(String post_comment_count) {
        this.post_comment_count = post_comment_count;
    }
}
