package com.example.orderservice.dto;

public class AuthRequest {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public AuthRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AuthRequest setPassword(String password) {
        this.password = password;
        return this;
    }


}
