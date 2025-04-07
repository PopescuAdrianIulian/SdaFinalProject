package com.example.orderservice.response.parcel;

import com.example.notificationservice.enums.PackageStatus;
import com.example.notificationservice.enums.Size;
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
public class ParcelResponse {

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
    private String awb;
    private double price;

    private Map<LocalDateTime, PackageStatus> statusHistory;

}