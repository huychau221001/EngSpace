package com.example.engspace.model;

import java.util.ArrayList;

public class FolderSetResponse {
    private ArrayList<String> folder_id;
    private ArrayList<String> set_id;

    public ArrayList<String> getFolder_id() {
        return folder_id;
    }

    public void setFolder_id(ArrayList<String> folder_id) {
        this.folder_id = folder_id;
    }

    public ArrayList<String> getSet_id() {
        return set_id;
    }

    public void setSet_id(ArrayList<String> set_id) {
        this.set_id = set_id;
    }
}
