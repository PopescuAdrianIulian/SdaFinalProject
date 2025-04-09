package com.example.orderservice.response.parcel;


import com.example.orderservice.entity.Parcel;
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
public class ParcelResponse {

    private Size size;
    private Double weight;
    private String destinationAddress;
    private String destinationContact;
    private String destinationPhone;
    private String destinationEmail;
    private boolean fragile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean delivered;
    private String email;
    private String awb;
    private double price;

    private Map<LocalDateTime, PackageStatus> statusHistory;

    public ParcelResponse createParcelResponse(Parcel parcel) {
        return ParcelResponse.builder()
                .size(parcel.getSize())
                .weight(parcel.getWeight())
                .destinationAddress(parcel.getDestinationAddress())
                .destinationPhone(parcel.getDestinationPhone())
                .destinationContact(parcel.getDestinationContact())
                .fragile(parcel.isFragile())
                .destinationEmail(parcel.getDestinationEmail())
                .createdAt(parcel.getCreatedAt())
                .updatedAt(parcel.getUpdatedAt())
                .delivered(parcel.isDelivered())
                .statusHistory(parcel.getStatusHistory())
                .email(parcel.getSender().getEmail())
                .price(parcel.getPrice())
                .awb(parcel.getAwb())
                .build();
    }
}
