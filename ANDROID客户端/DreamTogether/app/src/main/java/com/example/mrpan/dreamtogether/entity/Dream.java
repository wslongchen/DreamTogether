package com.example.mrpan.dreamtogether.entity;

/**
 * Created by mrpan on 16/3/16.
 */
public class Dream extends BaseEntity{
    private int ID;
    private String wordcircle_author;
    private String wordcircle_date;
    private String wordcircle_content;
    private String wordcircle_titile;
    private String wordcircle_status;
    private String wordcircle_password;
    private String wordcircle_guid;
    private String wordcircle_type;
    private String wordcircle_comment_status;
    private String wordcircle_comment_count;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getWordcircle_date() {
        return wordcircle_date;
    }

    public void setWordcircle_date(String wordcircle_date) {
        this.wordcircle_date = wordcircle_date;
    }

    public String getWordcircle_author() {
        return wordcircle_author;
    }

    public void setWordcircle_author(String wordcircle_author) {
        this.wordcircle_author = wordcircle_author;
    }

    public String getWordcircle_content() {
        return wordcircle_content;
    }

    public void setWordcircle_content(String wordcircle_content) {
        this.wordcircle_content = wordcircle_content;
    }

    public String getWordcircle_titile() {
        return wordcircle_titile;
    }

    public void setWordcircle_titile(String wordcircle_titile) {
        this.wordcircle_titile = wordcircle_titile;
    }

    public String getWordcircle_status() {
        return wordcircle_status;
    }

    public void setWordcircle_status(String wordcircle_status) {
        this.wordcircle_status = wordcircle_status;
    }

    public String getWordcircle_password() {
        return wordcircle_password;
    }

    public void setWordcircle_password(String wordcircle_password) {
        this.wordcircle_password = wordcircle_password;
    }

    public String getWordcircle_guid() {
        return wordcircle_guid;
    }

    public void setWordcircle_guid(String wordcircle_guid) {
        this.wordcircle_guid = wordcircle_guid;
    }

    public String getWordcircle_type() {
        return wordcircle_type;
    }

    public void setWordcircle_type(String wordcircle_type) {
        this.wordcircle_type = wordcircle_type;
    }

    public String getWordcircle_comment_status() {
        return wordcircle_comment_status;
    }

    public void setWordcircle_comment_status(String wordcircle_comment_status) {
        this.wordcircle_comment_status = wordcircle_comment_status;
    }

    public String getWordcircle_comment_count() {
        return wordcircle_comment_count;
    }

    public void setWordcircle_comment_count(String wordcircle_comment_count) {
        this.wordcircle_comment_count = wordcircle_comment_count;
    }
}
