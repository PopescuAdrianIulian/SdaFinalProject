package com.example.orderservice.controller;

import com.example.orderservice.entity.User;
import com.example.orderservice.request.auth.AccountRequest;
import com.example.orderservice.request.auth.AuthRequest;
import com.example.orderservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(userService.login(authRequest));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNewAccount(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.createNewAccount(user));
    }

    @PostMapping("/update/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(userService.updatePassword(authRequest));
    }

    @PostMapping("/update/accountDetails")
    public ResponseEntity<?> updateAccount(@Valid @RequestBody AccountRequest accountRequest) {
        return ResponseEntity.ok(userService.updateAccount(accountRequest));
    }
}

