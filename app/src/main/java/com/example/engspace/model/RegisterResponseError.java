package com.example.engspace.model;

import java.util.ArrayList;

public class RegisterResponseError {
    private ArrayList<String> email;
    private ArrayList<String> username;
    private ArrayList<String> password;
    private ArrayList<String> first_name;
    private ArrayList<String> last_name;

    public ArrayList<String> getEmail() {
        return email;
    }

    public void setEmail(ArrayList<String> email) {
        this.email = email;
    }

    public ArrayList<String> getUsername() {
        return username;
    }

    public void setUsername(ArrayList<String> username) {
        this.username = username;
    }

    public ArrayList<String> getPassword() {
        return password;
    }

    public void setPassword(ArrayList<String> password) {
        this.password = password;
    }

    public ArrayList<String> getFirst_name() {
        return first_name;
    }

    public void setFirst_name(ArrayList<String> first_name) {
        this.first_name = first_name;
    }

    public ArrayList<String> getLast_name() {
        return last_name;
    }

    public void setLast_name(ArrayList<String> last_name) {
        this.last_name = last_name;
    }
}
