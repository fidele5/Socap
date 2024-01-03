package com.example.socap.model;

import com.google.gson.annotations.SerializedName;

public class News {
    @SerializedName("id")
    private long id;
    @SerializedName("date")
    private String date;
    @SerializedName("user")
    private User user;
    @SerializedName("text")
    private String text = null;
    @SerializedName("image")
    private int photo = -1;
    @SerializedName("title")
    private String title;

    public News() {
    }

    public News(long id, String date, User user, String text, String title, int photo) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.text = text;
        this.photo = photo;
        this.title = title;
    }
    public News(long id, String date, User user, String text, String title) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.text = text;
        this.title = title;
    }
    public News(long id, String date, User user, int photo) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.photo = photo;
    }

    public long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public User getFriend() {
        return user;
    }

    public String getText() {
        return text;
    }

    public int getPhoto() {
        return photo;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFriend(User user) {
        this.user = user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
