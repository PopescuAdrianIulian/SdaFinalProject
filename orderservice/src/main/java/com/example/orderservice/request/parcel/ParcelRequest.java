package com.example.orderservice.request.parcel;

import com.example.orderservice.entity.Parcel;
import com.example.orderservice.entity.User;
import com.example.orderservice.enums.PackageStatus;
import com.example.orderservice.enums.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.example.orderservice.service.ParcelService.generateAWB;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParcelRequest {


    @NotNull(message = "Size cannot be null")
    private Size size;

    @NotNull(message = "Weight cannot be null")
    @Positive(message = "Weight must be a positive number")
    private Double weight;

    @NotBlank(message = "Destination address cannot be empty")
    private String destinationAddress;

    @NotBlank(message = "Destination contact cannot be empty")
    private String destinationContact;

    @NotBlank(message = "Phone contact cannot be empty")
    private String destinationPhone;

    @NotBlank(message = "Email contact cannot be empty")
    private String destinationEmail;

    private boolean fragile;

    private double price;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email;

    public Parcel createNewParcel(User sender) {
        Map<LocalDateTime, PackageStatus> statusHistory = new HashMap<>();
        statusHistory.put(LocalDateTime.now(), PackageStatus.OPEN);

        return Parcel.builder()
                .size(this.getSize())
                .weight(this.getWeight())
                .destinationAddress(this.getDestinationAddress())
                .destinationContact(this.getDestinationContact())
                .destinationPhone(this.getDestinationPhone())
                .destinationEmail(this.getDestinationEmail())
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

