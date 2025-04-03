package com.example.orderservice.controller;

import com.example.orderservice.enums.PackageStatus;
import com.example.orderservice.response.product.ProductResponse;
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
    public ResponseEntity<ProductResponse> changeDeliveryStatus(@PathVariable String awb,
                                                                @PathVariable PackageStatus newStatus) {
        return ResponseEntity.ok(trackingService.changeDeliveryStatus(awb, newStatus));
    }

    @PostMapping("/delivered/{awb}")
    public ResponseEntity<ProductResponse> setProductAsDelivered(@PathVariable String awb) {
        return ResponseEntity.ok(trackingService.setProductAsDelivered(awb));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(trackingService.getAllProducts());
    }

}
