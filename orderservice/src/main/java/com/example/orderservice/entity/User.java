package com.example.orderservice.entity;

import com.example.orderservice.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(mappedBy = "sender")
    private List<Product> products = new ArrayList<>();

//    public Long getId() {
//        return id;
//    }
//
//    public User setId(Long id) {
//        this.id = id;
//        return this;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public User setName(String name) {
//        this.name = name;
//        return this;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public User setEmail(String email) {
//        this.email = email;
//        return this;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public User setPhone(String phone) {
//        this.phone = phone;
//        return this;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public User setAddress(String address) {
//        this.address = address;
//        return this;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public User setPassword(String password) {
//        this.password = password;
//        return this;
//    }
//
//    public UserType getUserType() {
//        return userType;
//    }
//
//    public User setUserType(UserType userType) {
//        this.userType = userType;
//        return this;
//    }
//
//    public List<Product> getProducts() {
//        return products;
//    }
//
//    public User setProducts(List<Product> products) {
//        this.products = products;
//        return this;
//    }
}