package com.example.notificationservice.service;

import com.example.orderservice.response.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;

    @KafkaListener(topics = "delivery-change", groupId = "tracking-alert")
    public void fetchData(ProductResponse productResponse) {
        log.info("Fetching data {}", productResponse);

        String body = emailService.createBody(productResponse);
        String to = productResponse.getEmail();
        String subject = "Tracking service";

        try {
            emailService.sendEmail(to, subject, body);
            log.info("Sending email to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}", to, e);
        }
    }
}
