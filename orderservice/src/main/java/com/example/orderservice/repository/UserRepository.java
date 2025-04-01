package com.example.orderservice.repository;

import com.example.orderservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
     boolean existsByEmail(String email);
     boolean existsByName(String email);
     User getByEmail(String email);

}
