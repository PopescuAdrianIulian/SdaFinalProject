package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.ProductRequest;
import com.example.orderservice.entity.Product;
import com.example.orderservice.entity.User;
import com.example.orderservice.repository.ProductRepository;
import com.example.orderservice.repository.UserRepository;
import com.example.orderservice.service.JwtService;
import com.example.orderservice.service.ProductService;
import com.example.orderservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final ProductService productService;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderController(ProductService productService, UserRepository userRepository, ProductRepository productRepository) {
        this.productService = productService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody ProductRequest productRequest) {
        // You can now directly use the autowired services

        // Create Product using ProductRequest (productService and userService are already injected)
        Product product = productRequest.createNewProduct(productService.getUserService());

        // Save the product using the injected productService
        productService.createProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED).body("Product created successfully");
    }

    @GetMapping("/{awb}")
    public ResponseEntity<?> getOrderStatusByAwb(@PathVariable String awb) {
        Product order = productService.findProductByAwb(awb).orElseThrow();
        OrderResponse orderResponse = new OrderResponse();
        return ResponseEntity.ok(orderResponse.createOrderResponse(order));
    }

    @GetMapping("/allOrders")
    public ResponseEntity<?> getAllOrdersByUser(@RequestBody String email) {
        User tempUser = userRepository.getUserByEmail(email);
        OrderResponse orderResponse = new OrderResponse();
        List<OrderResponse> allProducts = tempUser.getProducts().stream()
                .map(product -> orderResponse.createOrderResponse(product)).toList();
        return (ResponseEntity<?>) allProducts;
    }
}
