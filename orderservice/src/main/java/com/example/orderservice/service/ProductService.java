package com.example.orderservice.service;

import com.example.orderservice.entity.Product;
import com.example.orderservice.entity.User;
import com.example.orderservice.enums.PackageStatus;

import com.example.orderservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class ProductService {

    private ProductRepository productRepository;

    private static final String AWB_PREFIX = "RO";


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void createProduct(Product product) {

        String awb = generateAWB();
        product.setAwb(awb);
        product.setDelivered(false);
        product.setStatus(PackageStatus.OPEN);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        Map<LocalDateTime, PackageStatus> tempStatusHistory = new java.util.HashMap<>();
        tempStatusHistory.put(LocalDateTime.now(), PackageStatus.OPEN);
        product.setStatusHistory(tempStatusHistory);
        User user = new User(); // il luam din FE / token / session / authentication ??
        product.setSender(user);
        productRepository.save(product);
    }

    public Optional<Product> findProductByAwb(String awb) {
        return productRepository.findByAwb(awb);
    }

    private String generateAWB() {
        String randomDigits = String.format("%09d", new Random().nextInt(1000000000));
        return AWB_PREFIX + randomDigits;
    }

    private void updateProduct(Product product){
        productRepository.saveAndFlush(product);
    }



}
