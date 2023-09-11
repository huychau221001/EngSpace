package com.example.engspace.model;

import java.util.ArrayList;

public class UserResponseError {
    private ArrayList<String> username;
    private ArrayList<String> email;
    private ArrayList<String> first_name;
    private ArrayList<String> last_name;
    private ArrayList<String> dob;
    private ArrayList<String> website;
    private ArrayList<String> bio;
    private ArrayList<String> fb_url;
    private ArrayList<String> avatar;

    public ArrayList<String> getUsername() {
        return username;
    }

    public void setUsername(ArrayList<String> username) {
        this.username = username;
    }

    public ArrayList<String> getEmail() {
        return email;
    }

    public void setEmail(ArrayList<String> email) {
        this.email = email;
    }

    public ArrayList<String> getFirst_name() {
        return first_name;
    }

    public void setFirst_name(ArrayList<String> first_name) {
        this.first_name = first_name;
    }

    public ArrayList<String> getLast_name() {
        return last_name;
    }

    public void setLast_name(ArrayList<String> last_name) {
        this.last_name = last_name;
    }

    public ArrayList<String> getDob() {
        return dob;
    }

    public void setDob(ArrayList<String> dob) {
        this.dob = dob;
    }

    public ArrayList<String> getWebsite() {
        return website;
    }

    public void setWebsite(ArrayList<String> website) {
        this.website = website;
    }

    public ArrayList<String> getBio() {
        return bio;
    }

    public void setBio(ArrayList<String> bio) {
        this.bio = bio;
    }

    public ArrayList<String> getFb_url() {
        return fb_url;
    }

    public void setFb_url(ArrayList<String> fb_url) {
        this.fb_url = fb_url;
    }

    public ArrayList<String> getAvatar() {
        return avatar;
    }

    public void setAvatar(ArrayList<String> avatar) {
        this.avatar = avatar;
    }
}
