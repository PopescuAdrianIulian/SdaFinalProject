package com.example.orderservice.controller;

import com.example.orderservice.request.product.ProductRequest;
import com.example.orderservice.entity.Product;
import com.example.orderservice.entity.User;
import com.example.orderservice.service.ProductService;
import com.example.orderservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;


    @PostMapping("/createProduct")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        User sender = userService.getUserByEmail(productRequest.getEmail());
        Product product = productRequest.createNewProduct(sender);
        productService.createProduct(product);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/{awb}")
    public ResponseEntity<?> getProductStatusByAwb(@Valid @PathVariable String awb) {
        return ResponseEntity.ok(productService.getProductStatusByAwb(awb));
    }

    @GetMapping("/allProducts/{email}")
    public ResponseEntity<?> getAllProductsByUser(@Valid @PathVariable String email) {
        return ResponseEntity.ok(productService.getAllProductsByUser(email));
    }
}
