package com.example.orderservice.service;

import com.example.orderservice.entity.Parcel;
import com.example.orderservice.enums.PackageStatus;
import com.example.orderservice.repository.ParcelRepository;
import com.example.orderservice.response.parcel.ParcelResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrackingService {

    @Value("${NOTIFICATION_TOPIC}")
    private String notificationTopic;
    private final ParcelRepository parcelRepository;
    private final KafkaTemplate<String, ParcelResponse> kafkaTemplate;

    public ParcelResponse changeDeliveryStatus(String awb, String newStatus) {
        PackageStatus status = getPackageStatus(newStatus);
        Parcel parcel = findParcelByAwb(awb);

        if (parcel.getStatus()==status){
            throw new RuntimeException("Parcel status is the same");
        }
        log.info("Changing the status to {}", status);
        Map<LocalDateTime, PackageStatus> statusHistory = parcel.getStatusHistory();
        statusHistory.put(LocalDateTime.now(), status);
        parcel.setStatusHistory(statusHistory);
        parcel.setStatus(status);
        parcel.setUpdatedAt(LocalDateTime.now());
        parcelRepository.saveAndFlush(parcel);

        ParcelResponse payload = createParcelResponse(parcel);
        sendNotification(payload);

        return payload;
    }

    public ParcelResponse setParcelAsDelivered(String awb) {
        Parcel parcel = findParcelByAwb(awb);

        if (parcel.isDelivered()) {
            throw new RuntimeException("Parcel already delivered");
        }

        log.info("Parcel is delivered {}", parcel.getAwb());
        log.info("Parcel price is {}", parcel.getPrice());

        parcel.setDelivered(true);
        parcel.setUpdatedAt(LocalDateTime.now());
        parcel.setStatus(PackageStatus.DELIVERED);

        Map<LocalDateTime, PackageStatus> statusHistory = parcel.getStatusHistory();
        statusHistory.put(LocalDateTime.now(), PackageStatus.DELIVERED);
        parcelRepository.saveAndFlush(parcel);

        ParcelResponse payload = createParcelResponse(parcel);
        sendNotification(payload);

        return payload;
    }

    public List<ParcelResponse> getAllParcels() {
        log.info("Retrieving all parcels");
        return parcelRepository.findAll().stream()
                .map(this::createParcelResponse)
                .toList();
    }

    public List<ParcelResponse> getAllUndeliveredParcels() {
        log.info("Retrieving all undelivered parcels");
        return parcelRepository.findAll().stream()
                .filter(parcel -> parcel.getStatus() != PackageStatus.DELIVERED && parcel.getStatus() != PackageStatus.RETURNED)
                .map(this::createParcelResponse)
                .toList();
    }

    private PackageStatus getPackageStatus(String status) {
        try {
            return PackageStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid PackageStatus: {}", status);
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    private Parcel findParcelByAwb(String awb) {
        return parcelRepository.findParcelByAwb(awb)
                .orElseThrow(() -> new RuntimeException("Parcel with AWB " + awb + " not found"));
    }

    private ParcelResponse createParcelResponse(Parcel parcel) {
        return new ParcelResponse().createParcelResponse(parcel);
    }

    private void sendNotification(ParcelResponse payload) {
        kafkaTemplate.send(notificationTopic, payload);
        log.info("Sending payload for the notification service {}", payload);
    }
}
