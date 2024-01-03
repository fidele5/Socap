package com.example.socap.model;

import com.google.gson.annotations.SerializedName;

public class Auth {
    @SerializedName("status")
    private String status;
    @SerializedName("user")
    private User user;
    @SerializedName("token")
    private String token;
    @SerializedName("message")
    private String message;

    public Auth(String status, User user, String token, String message){
        this.status = status;
        this.user = user;
        this.token = token;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
