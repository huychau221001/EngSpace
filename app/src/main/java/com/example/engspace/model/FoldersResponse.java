package com.example.engspace.model;

import java.util.ArrayList;

public class FoldersResponse {
    private int count;
    private String next;
    private String previous;
    private ArrayList<FolderResponse> results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public ArrayList<FolderResponse> getResults() {
        return results;
    }

    public void setResults(ArrayList<FolderResponse> results) {
        this.results = results;
    }
}
