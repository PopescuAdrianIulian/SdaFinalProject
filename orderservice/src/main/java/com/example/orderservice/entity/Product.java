package com.example.orderservice.entity;

import com.example.orderservice.config.MapJsonConverter;
import com.example.orderservice.enums.PackageStatus;
import com.example.orderservice.enums.Size;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
    private Size size;

    private Double weight;

    private String destinationAddress;
    private String destinationContact;
    private String destinationPhone;

    private boolean fragile;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(unique = true)
    private String awb;

    @Enumerated(EnumType.STRING)
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
