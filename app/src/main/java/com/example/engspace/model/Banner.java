package com.example.engspace.model;

public class Banner {
    private String title;
    private String content;
    private String color;
    private int image;

    public Banner(String title, String content, String color, int image) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
