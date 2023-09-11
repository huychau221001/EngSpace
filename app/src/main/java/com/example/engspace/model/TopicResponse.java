package com.example.engspace.model;

import java.util.ArrayList;
import java.util.Date;

public class TopicResponse {
    private int id;
    private boolean is_public;
    private String name;
    private String description;
    private Date date_created;
    private Date date_updated;
    private UserResponse user;
    private int amount;
    private ArrayList<SetResponse> topic_sets;

    public TopicResponse() {
    }

    public TopicResponse(int id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ArrayList<SetResponse> getTopic_sets() {
        return topic_sets;
    }

    public void setTopic_sets(ArrayList<SetResponse> topic_sets) {
        this.topic_sets = topic_sets;
    }

    @Override
    public String toString() {
        return name;
    }
}
