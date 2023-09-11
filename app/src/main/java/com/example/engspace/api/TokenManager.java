package com.example.engspace.api;

public interface TokenManager {
    String getToken();

    boolean hasToken();

    void clearToken();

    String refreshToken();
}