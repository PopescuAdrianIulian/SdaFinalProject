package com.example.orderservice.service;

import com.example.orderservice.entity.User;
import com.example.orderservice.repository.UserRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByEmail(String email){
        return userRepository.getUserByEmail(email);
    }
}
