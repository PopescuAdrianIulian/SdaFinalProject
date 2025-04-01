package com.example.orderservice.controller;

import com.example.orderservice.dto.AuthRequest;
import com.example.orderservice.dto.AuthResponse;
import com.example.orderservice.entity.User;
import com.example.orderservice.repository.UserRepository;
import com.example.orderservice.service.JwtService;
import com.example.orderservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
    User user = userRepository.getByEmail(authRequest.getEmail());
    if(user == null){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username");
    }
    if(!user.getPassword().equals(authRequest.getPassword())){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password");
    }
    String token = jwtService.generateToken(user);
    return ResponseEntity.ok(new AuthResponse(token));
}
}
