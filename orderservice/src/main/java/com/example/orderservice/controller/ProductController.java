package com.example.orderservice.controller;

import com.example.orderservice.request.product.ProductRequest;
import com.example.orderservice.response.product.ProductResponse;
import com.example.orderservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/createProduct")
    public ResponseEntity<Void> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{awb}")
    public ResponseEntity<ProductResponse> getProductStatusByAwb(@Valid @PathVariable String awb) {
        return ResponseEntity.ok(productService.getProductStatusByAwb(awb));
    }

    @GetMapping("/allProducts/{email}")
    public ResponseEntity<List<ProductResponse>> getAllProductsByUser(@Valid @PathVariable String email) {
        return ResponseEntity.ok(productService.getAllProductsByUser(email));
    }
}
