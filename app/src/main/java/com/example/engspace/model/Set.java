package com.example.engspace.model;

public class Set {
    private String title;
    private String user;
    private String date_created;
    private int amount;
    private int user_img;
    private int icon_color;


    public Set(String title, String user, String date_created, int amount, int user_img) {
        this.title = title;
        this.user = user;
        this.date_created = date_created;
        this.amount = amount;
        this.user_img = user_img;
    }

    public Set(String title, int amount, int icon_color) {
        this.title = title;
        this.amount = amount;
        this.icon_color = icon_color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUser_img() {
        return user_img;
    }

    public void setUser_img(int user_img) {
        this.user_img = user_img;
    }

    public int getIcon_color() {
        return icon_color;
    }

    public void setIcon_color(int icon_color) {
        this.icon_color = icon_color;
    }
}
