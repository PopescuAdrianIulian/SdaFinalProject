package com.example.orderservice.service;

import com.example.orderservice.entity.User;
import com.example.orderservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            // mesaj ca exista user
        } else {
           String password = user.getPassword();
            userRepository.save(user);
        }
    }

    public boolean logIn(String email, String password){
        if(userRepository.existsByEmail(email)){
            User userLogIn = userRepository.getByEmail(email);
            if(password.equals(userLogIn.getPassword())){
                //generate JWT Token
                return true;
            }
            return false;
        }
        return false;
    }


}
