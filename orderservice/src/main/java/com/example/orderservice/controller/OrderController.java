package com.example.orderservice.controller;

import com.example.orderservice.entity.Product;
import com.example.orderservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    @Autowired
    private ProductService productService;

    @PostMapping("/order")
    public ResponseEntity<Void> createOrder(@RequestBody Product product) {
        productService.createProduct(product);
        return null;
    }

    @GetMapping("/{awb}")
    public ResponseEntity<Product> getOrderStatusByAwb(@PathVariable String awb) {
        Optional<Product> order = productService.findProductByAwb(awb);
        if (order.isEmpty()) {
            throw new RuntimeException("Shipment not found");
        }
        return ResponseEntity.ok(order.get());
    }

}
