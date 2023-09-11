package com.example.engspace.model;

import java.util.ArrayList;

public class FolderResponseError {
    private ArrayList<String> is_public;
    private ArrayList<String> name;
    private ArrayList<String> description;

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
}
