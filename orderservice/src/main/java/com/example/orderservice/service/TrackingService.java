package com.example.orderservice.service;

import com.example.orderservice.entity.Product;
import com.example.orderservice.enums.PackageStatus;
import com.example.orderservice.repository.ProductRepository;
import com.example.orderservice.repository.UserRepository;
import com.example.orderservice.response.product.ProductResponse;
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
    private static String NOTIFICATION_TOPIC;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, ProductResponse> kafkaTemplate;

    public ProductResponse changeDeliveryStatus(String awb, PackageStatus newStatus) {
        Product tempProduct = productRepository.findProductByAwb(awb).orElseThrow();
        log.info("Changing the status to {}", newStatus);
        Map<LocalDateTime, PackageStatus> statusHistory = tempProduct.getStatusHistory();
        statusHistory.put(LocalDateTime.now(), newStatus);
        tempProduct.setStatusHistory(statusHistory);
        tempProduct.setStatus(newStatus);
        tempProduct.setUpdatedAt(LocalDateTime.now());
        productRepository.saveAndFlush(tempProduct);
        ProductResponse payload = new ProductResponse().createProductResponse(tempProduct);
        kafkaTemplate.send(NOTIFICATION_TOPIC, payload);
        log.info("Sending payload for the notification service {}", payload);
        return payload;
    }

    public ProductResponse setProductAsDelivered(String awb) {
        Product tempProduct = productRepository.findProductByAwb(awb).orElseThrow();
        log.info("Product is delivered {}", tempProduct.getAwb());
        tempProduct.setDelivered(true);
        tempProduct.setUpdatedAt(LocalDateTime.now());
        productRepository.saveAndFlush(tempProduct);
        ProductResponse payload = new ProductResponse().createProductResponse(tempProduct);
        kafkaTemplate.send(NOTIFICATION_TOPIC, payload);
        log.info("Sending payload for the notification service {}", payload);
        return payload;
    }

    public List<ProductResponse> getAllProducts() {
        log.info("Retrieving all products");
        return productRepository.findAll()
                .stream()
                .map(product -> new ProductResponse().createProductResponse(product))
                .toList();
    }


}
