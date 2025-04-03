package com.example.orderservice.response.product;


import com.example.orderservice.entity.Product;
import com.example.orderservice.enums.PackageStatus;
import com.example.orderservice.enums.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Size size;
    private Double weight;
    private String destinationAddress;
    private String destinationContact;
    private String destinationPhone;
    private boolean fragile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean delivered;
    private String email;

    private Map<LocalDateTime, PackageStatus> statusHistory;

    public ProductResponse createProductResponse(Product product) {
        return ProductResponse.builder()
                .size(product.getSize())
                .weight(product.getWeight())
                .destinationAddress(product.getDestinationAddress())
                .destinationPhone(product.getDestinationPhone())
                .fragile(product.isFragile())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .delivered(product.isDelivered())
                .statusHistory(product.getStatusHistory())
                .email(product.getSender().getEmail())
                .build();
    }
}
