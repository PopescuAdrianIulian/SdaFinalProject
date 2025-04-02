package com.example.orderservice.service;

import com.example.orderservice.request.AccountRequest;
import com.example.orderservice.request.AuthRequest;
import com.example.orderservice.entity.User;
import com.example.orderservice.repository.UserRepository;
import com.example.orderservice.utils.PasswordHasher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordHasher encrypt;


    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public User createNewAccount(User user) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            user.setPassword(encrypt.hashPassword(user.getPassword()));
            return userRepository.saveAndFlush(user);
        } else {
            throw new RuntimeException("Email is already used");
        }
    }

    public User updatePassword(AuthRequest authRequest) {
        User tempUser = userRepository.getUserByEmail(authRequest.getEmail());
        tempUser.setPassword(encrypt.hashPassword(authRequest.getPassword()));
        return userRepository.saveAndFlush(tempUser);
    }

    public User updateAccount(AccountRequest accountRequest) {
        User tempUser = userRepository.getUserByEmail(accountRequest.getEmail());
        tempUser.setName(accountRequest.getName());
        tempUser.setEmail(accountRequest.getEmail());
        tempUser.setPhone(accountRequest.getPhone());
        tempUser.setAddress(accountRequest.getAddress());
        return userRepository.saveAndFlush(tempUser);
    }
}
