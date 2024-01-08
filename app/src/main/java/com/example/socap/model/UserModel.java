package com.example.socap.model;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {
    private int id;
    private String name;
    private int photo;
    private String email;
    private String phoneNumber;
    private String biography;
    private List<Community> communities;

    public UserModel(int id, String name, String email, int photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.email = email;
    }

    public UserModel(int id, String name, String email, String phoneNumber, String biography, int photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.email = email;
        this.biography = biography;
        this.phoneNumber = phoneNumber;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserModel(String name, int photo) {
        this.name = name;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPhoto() {
        return photo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public List<Community> getCommunities() {
        return communities;
    }

    public void setCommunities(List<Community> communities) {
        this.communities = communities;
    }
}
