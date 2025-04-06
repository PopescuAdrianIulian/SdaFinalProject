package com.example.orderservice.service;

import com.example.orderservice.entity.Product;
import com.example.orderservice.entity.User;
import com.example.orderservice.enums.PackageStatus;
import com.example.orderservice.repository.ProductRepository;
import com.example.orderservice.repository.UserRepository;
import com.example.orderservice.request.product.ProductRequest;
import com.example.orderservice.response.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    @Value("${NOTIFICATION_TOPIC}")
    private String NOTIFICATION_TOPIC;
    private final KafkaTemplate<String, ProductResponse> kafkaTemplate;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    public void createProduct(ProductRequest productRequest) {
        Map<LocalDateTime, PackageStatus> tempStatusHistory = new java.util.HashMap<>();
        User sender = userRepository.getUserByEmail(productRequest.getEmail());
        if (sender == null) {
            throw new RuntimeException("User not found for email: " + productRequest.getEmail());
        }

        Product product = productRequest.createNewProduct(sender);
        product.setSender(sender);
        String awb = generateAWB();
        product.setAwb(awb);
        product.setDelivered(false);
        product.setStatus(PackageStatus.OPEN);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setPrice(generatePrice(product));
        tempStatusHistory.put(LocalDateTime.now(), PackageStatus.OPEN);
        product.setStatusHistory(tempStatusHistory);
        productRepository.save(product);

        ProductResponse payload = new ProductResponse().createProductResponse(product);
        kafkaTemplate.send(NOTIFICATION_TOPIC, payload);
        log.info("Product created with AWB: {}", awb);
    }


    public static String generateAWB() {
        String randomDigits = String.format("%09d", new Random().nextInt(1000000000));
        String AWB_PREFIX = "RO";
        return AWB_PREFIX + randomDigits;
    }

    public static Double generatePrice(Product product) {
        double basePrice = 0;
        switch (product.getSize()) {
            case SMALL -> basePrice = 10;
            case MEDIUM -> basePrice = 15;
            case LARGE -> basePrice = 20;
            case EXTRALARGE -> basePrice = 25;
        }
        basePrice += product.getWeight() * 2;
        if (product.isFragile()) {
            basePrice += 5;
        }
        return basePrice;
    }


    public List<ProductResponse> getAllProductsByUser(String email) {
        User tempUser = userRepository.getUserByEmail(email);
        if (tempUser == null) {
            throw new RuntimeException("User not found for email: " + email);
        }
        ProductResponse productResponse = new ProductResponse();
        return tempUser.getProducts()
                .stream()
                .map(productResponse::createProductResponse)
                .toList();
    }

    public ProductResponse getProductStatusByAwb(String awb) {
        Product product = productRepository.findProductByAwb(awb)
                .orElseThrow(() -> new RuntimeException("Product not found for AWB: " + awb));
        ProductResponse productResponse = new ProductResponse();
        return productResponse.createProductResponse(product);
    }


}
