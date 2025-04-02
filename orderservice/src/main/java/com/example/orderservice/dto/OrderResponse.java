package com.example.orderservice.dto;


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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Size size;
    private Double weight;
    private String destinationAddress;
    private String destinationContact;
    private String destinationPhone;
    private boolean fragile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean delivered;
    private Map<LocalDateTime, PackageStatus> statusHistory;

    public OrderResponse createOrderResponse(Product product){
       return OrderResponse.builder()
                .size(product.getSize())
                .weight(product.getWeight())
                .destinationAddress(product.getDestinationAddress())
                .destinationPhone(product.getDestinationPhone())
                .fragile(product.isFragile())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .delivered(product.isDelivered())
                .statusHistory(product.getStatusHistory())
                .build();
    }
}
