package com.example.engspace.model;

public class Folder {
    private String title;
    private String user;
    private int amount;
    private int user_img;

    public Folder(String title, String user, int amount, int user_img) {
        this.title = title;
        this.user = user;
        this.amount = amount;
        this.user_img = user_img;
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
}
