package com.example.orderservice.entity;

import com.example.orderservice.config.MapJsonConverter;
import com.example.orderservice.enums.PackageStatus;
import com.example.orderservice.enums.Size;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
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
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    public Product setId(Long id) {
        this.id = id;
        return this;
    }

    public Product setSize(Size size) {
        this.size = size;
        return this;
    }

    public Product setWeight(Double weight) {
        this.weight = weight;
        return this;
    }

    public Product setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
        return this;
    }

    public Product setDestinationContact(String destinationContact) {
        this.destinationContact = destinationContact;
        return this;
    }

    public Product setDestinationPhone(String destinationPhone) {
        this.destinationPhone = destinationPhone;
        return this;
    }

    public Product setFragile(boolean fragile) {
        this.fragile = fragile;
        return this;
    }

    public Product setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Product setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Product setAwb(String awb) {
        this.awb = awb;
        return this;
    }

    public Product setStatus(PackageStatus status) {
        this.status = status;
        return this;
    }

    public Product setDelivered(boolean delivered) {
        this.delivered = delivered;
        return this;
    }

    public Product setStatusHistory(Map<LocalDateTime, PackageStatus> statusHistory) {
        this.statusHistory = statusHistory;
        return this;
    }

    public Product setSender(User sender) {
        this.sender = sender;
        return this;
    }
}
