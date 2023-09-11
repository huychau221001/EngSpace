package com.example.engspace.model;

import java.util.ArrayList;
import java.util.Date;

public class SetResponse {
    private int id;
    private boolean is_public;
    private String name;
    private String description;
    private String image;
    private Date date_created;
    private Date date_updated;
    private UserResponse user;
    private int topic;
    private ArrayList<Integer> set_folders;
    private int amount;
    private ArrayList<SetDetailResponse> set_details;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(Date date_updated) {
        this.date_updated = date_updated;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public ArrayList<Integer> getSet_folders() {
        return set_folders;
    }

    public void setSet_folders(ArrayList<Integer> set_folders) {
        this.set_folders = set_folders;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ArrayList<SetDetailResponse> getSet_details() {
        return set_details;
    }

    public void setSet_details(ArrayList<SetDetailResponse> set_details) {
        this.set_details = set_details;
    }
}
