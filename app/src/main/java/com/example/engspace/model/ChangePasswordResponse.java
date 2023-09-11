package com.example.engspace.model;

import java.util.ArrayList;

public class ChangePasswordResponse {
    private ArrayList<String> password;
    private ArrayList<String> password2;
    private ArrayList<String> old_password;
    private ArrayList<String> non_field_errors;

    public ArrayList<String> getPassword() {
        return password;
    }

    public void setPassword(ArrayList<String> password) {
        this.password = password;
    }

    public ArrayList<String> getPassword2() {
        return password2;
    }

    public void setPassword2(ArrayList<String> password2) {
        this.password2 = password2;
    }

    public ArrayList<String> getOld_password() {
        return old_password;
    }

    public void setOld_password(ArrayList<String> old_password) {
        this.old_password = old_password;
    }

    public ArrayList<String> getNon_field_errors() {
        return non_field_errors;
    }

    public void setNon_field_errors(ArrayList<String> non_field_errors) {
        this.non_field_errors = non_field_errors;
    }
}
