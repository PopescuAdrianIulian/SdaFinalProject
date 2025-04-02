package com.example.orderservice.dto;

import com.example.orderservice.entity.Product;
import com.example.orderservice.entity.User;
import com.example.orderservice.enums.PackageStatus;
import com.example.orderservice.enums.Size;
import com.example.orderservice.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.example.orderservice.service.ProductService.generateAWB;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private Size size;
    private Double weight;
    private String destinationAddress;
    private String destinationContact;
    private String destinationPhone;
    private boolean fragile;
    private LocalDateTime createdAt;
    private String email;

    public Product createNewProduct(UserService userService){
        Map<LocalDateTime, PackageStatus> statusHistory = new HashMap<>();
        statusHistory.put(this.getCreatedAt(), PackageStatus.OPEN);

        User sender = userService.getByEmail(this.getEmail());
        if (sender == null) {
            throw new RuntimeException("User not found for email: " + this.getEmail());
        }

        return Product.builder()
                .size(this.getSize())
                .weight(this.getWeight())
                .destinationAddress(this.getDestinationAddress())
                .destinationContact(this.getDestinationContact())
                .destinationPhone(this.getDestinationPhone())
                .fragile(this.isFragile())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getCreatedAt())
                .awb(generateAWB())
                .status(PackageStatus.OPEN)
                .delivered(false)
                .statusHistory(statusHistory)
                .sender(sender)
                .build();
    }
}

