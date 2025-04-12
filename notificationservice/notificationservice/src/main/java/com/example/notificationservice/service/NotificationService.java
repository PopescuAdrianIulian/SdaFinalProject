package com.example.notificationservice.service;

import com.example.orderservice.response.parcel.ParcelResponse;
import com.example.orderservice.response.support.SupportTicketResponse;
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
    public void handleDeliveryChange(ParcelResponse parcelResponse) {
        log.info("Fetching data {}", parcelResponse);

        String body = emailService.createBodyForParcel(parcelResponse);
        String toSender = parcelResponse.getEmail();
        String toDestination = parcelResponse.getDestinationEmail();
        String subject = "Tracking service";

        try {
            emailService.sendEmail(toSender, subject, body);
            emailService.sendEmail(toDestination, subject, body);
            log.info("Sending email to {}", toSender);
            log.info("Sending email to {}", toDestination);
        } catch (Exception e) {
            log.error("Failed to send email to {}", toSender, e);
            log.error("Failed to send email to {}", toDestination, e);
        }
    }

    @KafkaListener(topics = "support-notification", groupId = "tracking-alert")
    public void handleNotification(SupportTicketResponse supportTicketResponse) {
        log.info("Fetching data {}", supportTicketResponse);

        String body = emailService.createBodyForSupportTicket(supportTicketResponse);
        String toSender = supportTicketResponse.getEmail();
        String subject = "Support service";

        try {
            emailService.sendEmail(toSender, subject, body);
            log.info("Sending email to {}", toSender);
        } catch (Exception e) {
            log.error("Failed to send email to {}", toSender, e);
        }
    }
}
