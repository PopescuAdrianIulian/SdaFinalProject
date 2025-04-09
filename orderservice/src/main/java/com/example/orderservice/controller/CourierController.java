package com.example.orderservice.controller;

import com.example.orderservice.entity.User;
import com.example.orderservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/courier")
@RequiredArgsConstructor
public class CourierController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createNewCourier(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.createNewAccountCourier(user));
    }
}
