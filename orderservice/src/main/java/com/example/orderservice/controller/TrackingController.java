package com.example.orderservice.controller;

import com.example.orderservice.enums.PackageStatus;
import com.example.orderservice.response.parcel.ParcelResponse;
import com.example.orderservice.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tracking")
public class TrackingController {

    private final TrackingService trackingService;

    @PostMapping("/status/{awb}/{newStatus}")
    public ResponseEntity<ParcelResponse> changeDeliveryStatus(@PathVariable String awb,
                                                               @PathVariable String newStatus) {
        return ResponseEntity.ok(trackingService.changeDeliveryStatus(awb, newStatus));
    }

    @PostMapping("/delivered/{awb}")
    public ResponseEntity<ParcelResponse> setParcelAsDelivered(@PathVariable String awb) {
        return ResponseEntity.ok(trackingService.setParcelAsDelivered(awb));
    }

    @GetMapping("/parcels")
    public ResponseEntity<List<ParcelResponse>> getAllParcels() {
        return ResponseEntity.ok(trackingService.getAllParcels());
    }

    @GetMapping("/allParcels")
    public ResponseEntity<List<ParcelResponse>> getAllUndeliveredParcels() {
        return ResponseEntity.ok(trackingService.getAllUndeliveredParcels());
    }

}
