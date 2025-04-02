package com.example.orderservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.orderservice.response.ProductResponse;
import com.example.orderservice.entity.Product;
import com.example.orderservice.entity.User;
import com.example.orderservice.enums.PackageStatus;
import com.example.orderservice.repository.ProductRepository;
import com.example.orderservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private static final String AWB_PREFIX = "RO";


    public void createProduct(Product product) {
        User sender = userRepository.getUserByEmail(product.getSender().getEmail());
        if (sender == null) {
            throw new RuntimeException("User not found for email: " + product.getSender().getEmail());
        }
        product.setSender(sender);
        String awb = generateAWB();
        product.setAwb(awb);
        product.setDelivered(false);
        product.setStatus(PackageStatus.OPEN);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Map<LocalDateTime, PackageStatus> tempStatusHistory = new java.util.HashMap<>();
        tempStatusHistory.put(LocalDateTime.now(), PackageStatus.OPEN);
        product.setStatusHistory(tempStatusHistory);
        productRepository.save(product);
        log.info("Product created with AWB: {}", awb);
    }


    public static String generateAWB() {
        String randomDigits = String.format("%09d", new Random().nextInt(1000000000));
        log.info("The awb was generated {}", AWB_PREFIX + randomDigits);
        return AWB_PREFIX + randomDigits;
    }


    public List<ProductResponse> getAllProductsByUser(String email) {
        User tempUser = userRepository.getUserByEmail(email);
        if (tempUser == null) {
            throw new RuntimeException("User not found for email: " + email);
        }
        ProductResponse productResponse = new ProductResponse();
        List<ProductResponse> allProducts = tempUser.getProducts()
                .stream()
                .map(productResponse::createProductResponse)
                .toList();
        return allProducts;
    }

    public ProductResponse getProductStatusByAwb(String awb) {
        Product product = productRepository.findProductByAwb(awb)
                .orElseThrow(() -> new RuntimeException("Product not found for AWB: " + awb));
        ProductResponse productResponse = new ProductResponse();
        return productResponse.createProductResponse(product);
    }


}
