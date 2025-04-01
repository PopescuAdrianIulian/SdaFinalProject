package com.example.orderservice.dto;

public class AuthResponse {

    private String token;


    public AuthResponse(String token) {
        this.token = token;

    }

    public String getToken() {
        return token;
    }

    public AuthResponse setToken(String token) {
        this.token = token;
        return this;
    }

}
