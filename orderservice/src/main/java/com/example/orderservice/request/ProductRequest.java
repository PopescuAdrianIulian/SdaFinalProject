package com.example.orderservice.request;

import com.example.orderservice.entity.Product;
import com.example.orderservice.entity.User;
import com.example.orderservice.enums.PackageStatus;
import com.example.orderservice.enums.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.example.orderservice.service.ProductService.generateAWB;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {


    private Size size;
    private Double weight;
    private String destinationAddress;
    private String destinationContact;
    private String destinationPhone;
    private boolean fragile;
    private String email;

    public Product createNewProduct(User sender) {
        Map<LocalDateTime, PackageStatus> statusHistory = new HashMap<>();
        statusHistory.put(LocalDateTime.now(), PackageStatus.OPEN);

        return Product.builder()
                .size(this.getSize())
                .weight(this.getWeight())
                .destinationAddress(this.getDestinationAddress())
                .destinationContact(this.getDestinationContact())
                .destinationPhone(this.getDestinationPhone())
                .fragile(this.isFragile())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .awb(generateAWB())
                .status(PackageStatus.OPEN)
                .delivered(false)
                .statusHistory(statusHistory)
                .sender(sender)
                .build();
    }
}

