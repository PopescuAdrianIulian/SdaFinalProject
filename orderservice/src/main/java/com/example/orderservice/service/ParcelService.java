package com.example.orderservice.service;

import com.example.orderservice.entity.Parcel;
import com.example.orderservice.entity.User;
import com.example.orderservice.enums.PackageStatus;
import com.example.orderservice.repository.ParcelRepository;
import com.example.orderservice.repository.UserRepository;
import com.example.orderservice.request.parcel.ParcelRequest;
import com.example.orderservice.response.parcel.ParcelResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParcelService {

    @Value("${NOTIFICATION_TOPIC}")
    private String NOTIFICATION_TOPIC;
    private final KafkaTemplate<String, ParcelResponse> kafkaTemplate;
    private final ParcelRepository parcelRepository;
    private final UserRepository userRepository;


    public ParcelResponse createParcel(ParcelRequest parcelRequest) {
        Map<LocalDateTime, PackageStatus> tempStatusHistory = new java.util.HashMap<>();
        User sender = userRepository.getUserByEmail(parcelRequest.getEmail());
        if (sender == null) {
            throw new RuntimeException("User not found for email: " + parcelRequest.getEmail());
        }

        Parcel parcel = parcelRequest.createNewParcel(sender);
        parcel.setSender(sender);
        String awb = generateAWB();
        parcel.setAwb(awb);
        parcel.setDelivered(false);
        parcel.setStatus(PackageStatus.OPEN);
        parcel.setCreatedAt(LocalDateTime.now());
        parcel.setUpdatedAt(LocalDateTime.now());
        parcel.setPrice(generatePrice(parcel));
        tempStatusHistory.put(LocalDateTime.now(), PackageStatus.OPEN);
        parcel.setStatusHistory(tempStatusHistory);
        parcelRepository.save(parcel);

        ParcelResponse payload = new ParcelResponse().createParcelResponse(parcel);
//        kafkaTemplate.send(NOTIFICATION_TOPIC, payload);
        log.info("Parcel created with AWB: {}", awb);
        return payload;
    }


    public static String generateAWB() {
        String randomDigits = String.format("%09d", new Random().nextInt(1000000000));
        String AWB_PREFIX = "RO";
        return AWB_PREFIX + randomDigits;
    }

    public static Double generatePrice(Parcel parcel) {
        double basePrice = 0;
        switch (parcel.getSize()) {
            case SMALL -> basePrice = 10;
            case MEDIUM -> basePrice = 15;
            case LARGE -> basePrice = 20;
            case EXTRALARGE -> basePrice = 25;
        }
        basePrice += parcel.getWeight() * 2;
        if (parcel.isFragile()) {
            basePrice += 5;
        }
        return basePrice;
    }


    public List<ParcelResponse> getAllParcelsByUser(String email) {
        User tempUser = userRepository.getUserByEmail(email);
        if (tempUser == null) {
            throw new RuntimeException("User not found for email: " + email);
        }
        Set<Parcel> combinedSet = new HashSet<>();
        combinedSet.addAll(parcelRepository.findParcelByDestinationEmail(email));
        combinedSet.addAll(tempUser.getParcels());

        ParcelResponse parcelResponse = new ParcelResponse();
        return combinedSet
                .stream()
                .map(parcelResponse::createParcelResponse)
                .toList();
    }

    public ParcelResponse getParcelStatusByAwb(String awb) {
        Parcel parcel = parcelRepository.findParcelByAwb(awb)
                .orElseThrow(() -> new RuntimeException("Parcel not found for AWB: " + awb));
        ParcelResponse parcelResponse = new ParcelResponse();
        return parcelResponse.createParcelResponse(parcel);
    }



}
