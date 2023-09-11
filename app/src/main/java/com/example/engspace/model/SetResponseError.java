package com.example.engspace.model;

import java.util.ArrayList;

public class SetResponseError {
    private ArrayList<String> is_public;
    private ArrayList<String> name;
    private ArrayList<String> description;
    private ArrayList<String> image;
    private ArrayList<String> user;
    private ArrayList<String> topic;
    private ArrayList<SetDetailResponseError> set_details;

    public ArrayList<String> getIs_public() {
        return is_public;
    }

    public void setIs_public(ArrayList<String> is_public) {
        this.is_public = is_public;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<String> description) {
        this.description = description;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public ArrayList<String> getUser() {
        return user;
    }

    public void setUser(ArrayList<String> user) {
        this.user = user;
    }

    public ArrayList<String> getTopic() {
        return topic;
    }

    public void setTopic(ArrayList<String> topic) {
        this.topic = topic;
    }

    public ArrayList<SetDetailResponseError> getSet_details() {
        return set_details;
    }

    public void setSet_details(ArrayList<SetDetailResponseError> set_details) {
        this.set_details = set_details;
    }
}
