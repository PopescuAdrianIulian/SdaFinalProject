package com.example.orderservice.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHasher {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String hashPassword(String password) {
        return encoder.encode(password);
    }

    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }

}

