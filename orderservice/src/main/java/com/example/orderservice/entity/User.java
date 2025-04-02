package com.example.orderservice.entity;

import com.example.orderservice.enums.UserType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phone;

    private String address;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @JsonManagedReference
    @OneToMany(mappedBy = "sender")
    private List<Product> products = new ArrayList<>();


}