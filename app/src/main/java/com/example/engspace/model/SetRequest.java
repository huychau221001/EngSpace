package com.example.engspace.model;

import java.util.ArrayList;

public class SetRequest {
    private boolean is_public;
    private String name;
    private String description;
    private String image;
    private int topic;
    private ArrayList<SetDetailResponse> set_details;

    public boolean isIs_public() {
        return is_public;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public ArrayList<SetDetailResponse> getSet_details() {
        return set_details;
    }

    public void setSet_details(ArrayList<SetDetailResponse> set_details) {
        this.set_details = set_details;
    }
}
