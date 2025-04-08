package com.example.orderservice.entity;

import com.example.orderservice.enums.UserType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

//    @NotBlank(message = "Name cannot be empty")
    private String name;

//    @NotBlank(message = "Email cannot be empty")
    private String email;

//    @NotBlank(message = "Phone cannot be empty")
    private String phone;

//    @NotBlank(message = "Address cannot be empty")
    private String address;

//    @NotBlank(message = "Password cannot be empty")
//    @Pattern(regexp = "^(?=.*\\d)(?=.*[\\W_]).{8,}$")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @JsonManagedReference
    @OneToMany(mappedBy = "sender")
    private List<Parcel> parcels = new ArrayList<>();


}