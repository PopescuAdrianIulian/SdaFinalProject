package com.example.orderservice.entity;

import com.example.orderservice.config.MapJsonConverter;
import com.example.orderservice.enums.PackageStatus;
import com.example.orderservice.enums.Size;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
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

    private boolean fragile;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(unique = true)
    @NotBlank(message = "AWB cannot be empty")
    private String awb;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Package status cannot be null")
    private PackageStatus status;

    private boolean delivered;

    @Convert(converter = MapJsonConverter.class)
    @Column(columnDefinition = "JSON")
    private Map<LocalDateTime, PackageStatus> statusHistory;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

}
