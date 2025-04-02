package com.example.orderservice.controller;

import com.example.orderservice.dto.AuthRequest;
import com.example.orderservice.dto.AuthResponse;
import com.example.orderservice.entity.User;
import com.example.orderservice.repository.UserRepository;
import com.example.orderservice.service.JwtService;
import com.example.orderservice.service.UserService;
import com.example.orderservice.utils.PasswordHasher;
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

    @Autowired
    private PasswordHasher hash;

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
    User user = userRepository.getByEmail(authRequest.getEmail());
    if(user == null){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Username");
    }
    if(!hash.checkPassword(authRequest.getPassword(), user.getPassword())){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Password");
    }
    String token = jwtService.generateToken(user);
    return ResponseEntity.ok(new AuthResponse(token));
}

@PostMapping("/create")
    public ResponseEntity<?> createNewAccount(@RequestBody User user){
    if(!userRepository.existsByEmail(user.getEmail())){
        user.setPassword(hash.hashPassword(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created new " + user.getUserType());
    }
    return ResponseEntity.status(HttpStatus.CONFLICT).body("Email address already in use");
}
}
