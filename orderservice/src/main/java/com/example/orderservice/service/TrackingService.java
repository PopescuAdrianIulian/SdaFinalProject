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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrackingService {

    @Value("${NOTIFICATION_TOPIC}")
    private String NOTIFICATION_TOPIC;
    private final ParcelRepository parcelRepository;
    private final KafkaTemplate<String, ParcelResponse> kafkaTemplate;

    public ParcelResponse changeDeliveryStatus(String awb, String newStatus) {
        PackageStatus status;
        try {
            status = PackageStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid PackageStatus: {}", newStatus);
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }
        Parcel tempParcel = parcelRepository.findParcelByAwb(awb).orElseThrow(() ->
                new RuntimeException("Parcel with AWB " + awb + " not found"));
        log.info("Changing the status to {}", status);
        Map<LocalDateTime, PackageStatus> statusHistory = tempParcel.getStatusHistory();
        statusHistory.put(LocalDateTime.now(), status);
        tempParcel.setStatusHistory(statusHistory);
        tempParcel.setStatus(status);
        tempParcel.setUpdatedAt(LocalDateTime.now());
        parcelRepository.saveAndFlush(tempParcel);
        ParcelResponse payload = new ParcelResponse().createParcelResponse(tempParcel);
        kafkaTemplate.send(NOTIFICATION_TOPIC, payload);
        log.info("Sending payload for the notification service {}", payload);

        return payload;
    }

    public ParcelResponse setParcelAsDelivered(String awb) {
        Parcel tempParcel = parcelRepository.findParcelByAwb(awb).orElseThrow();
        if (tempParcel.isDelivered()) {
            throw new RuntimeException("Already delivered");
        }
        log.info("Parcel is delivered {}", tempParcel.getAwb());
        log.info("Parcel price is {}", tempParcel.getPrice());

        tempParcel.setDelivered(true);
        tempParcel.setUpdatedAt(LocalDateTime.now());
        tempParcel.setStatus(PackageStatus.DELIVERED);
        Map<LocalDateTime, PackageStatus> statusHistory = tempParcel.getStatusHistory();
        statusHistory.put(LocalDateTime.now(), PackageStatus.DELIVERED);
        parcelRepository.saveAndFlush(tempParcel);
        ParcelResponse payload = new ParcelResponse().createParcelResponse(tempParcel);
        kafkaTemplate.send(NOTIFICATION_TOPIC, payload);
        log.info("Sending payload for the notification service {}", payload);
        return payload;
    }


    public List<ParcelResponse> getAllParcels() {
        log.info("Retrieving all Parcels");
        return parcelRepository.findAll()
                .stream()
                .map(parcel -> new ParcelResponse().createParcelResponse(parcel))
                .toList();
    }

    public List<ParcelResponse> getAllUndeliveredParcels() {
        List<Parcel> parcels = parcelRepository.findAll().stream()
                .filter(parcel -> parcel.getStatus() != PackageStatus.DELIVERED && parcel.getStatus() != PackageStatus.RETURNED)
                .collect(Collectors.toList());

        ParcelResponse parcelResponse = new ParcelResponse();
        return parcels.stream()
                .map(parcelResponse::createParcelResponse)
                .toList();
    }


}
