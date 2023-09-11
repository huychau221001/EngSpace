package com.example.engspace.model;

import java.util.ArrayList;

public class ResetPasswordResponse {
    private ArrayList<String> OTP;
    private ArrayList<String> password;
    private ArrayList<String> password2;
    private String detail;

    public ArrayList<String> getOTP() {
        return OTP;
    }

    public void setOTP(ArrayList<String> OTP) {
        this.OTP = OTP;
    }

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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
