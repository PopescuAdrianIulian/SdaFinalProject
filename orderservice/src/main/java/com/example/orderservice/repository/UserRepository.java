package com.example.orderservice.repository;

import com.example.orderservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
     boolean existsByEmail(String email);
     boolean existsByName(String email);
     User getUserByEmail(String email);

//     User getUserByEmail(String email);

}
