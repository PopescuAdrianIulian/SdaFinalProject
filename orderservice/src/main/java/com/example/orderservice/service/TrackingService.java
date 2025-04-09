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

@Service
@Slf4j
@RequiredArgsConstructor
public class TrackingService {

    @Value("${NOTIFICATION_TOPIC}")
    private String NOTIFICATION_TOPIC;
    private final ParcelRepository parcelRepository;
    private final KafkaTemplate<String, ParcelResponse> kafkaTemplate;

    public ParcelResponse changeDeliveryStatus(String awb, PackageStatus newStatus) {
        Parcel tempParcel = parcelRepository.findParcelByAwb(awb).orElseThrow();
        log.info("Changing the status to {}", newStatus);
        Map<LocalDateTime, PackageStatus> statusHistory = tempParcel.getStatusHistory();
        statusHistory.put(LocalDateTime.now(), newStatus);
        tempParcel.setStatusHistory(statusHistory);
        tempParcel.setStatus(newStatus);
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


}
