package com.example.notificationservice.service;

import com.example.notificationservice.enums.PackageStatus;
import com.example.notificationservice.enums.TicketStatus;
import com.example.orderservice.response.parcel.ParcelResponse;
import com.example.orderservice.response.support.SupportTicketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    @Async
    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public String createBodyForParcel(ParcelResponse payload) {
        StringBuilder sb = new StringBuilder();

        sb.append("We are pleased to provide you with an update on your package. Below are the details of your shipment:\n\n");

        sb.append("Package Details:\n");
        sb.append(" - AWB: ").append(payload.getAwb()).append("\n");
        sb.append(" - Size: ").append(payload.getSize()).append("\n");
        sb.append(" - Weight: ").append(payload.getWeight()).append(" kg\n");
        sb.append(" - Fragile: ").append(payload.isFragile() ? "Yes" : "No").append("\n\n");

        sb.append(" - Delivery Price: ").append(String.format("%.2f", payload.getPrice())).append(" Ron\n\n");

        sb.append("Delivery Information:\n");
        sb.append(" - Destination Address: ").append(payload.getDestinationAddress()).append("\n");
        sb.append(" - Contact Person: ").append(payload.getDestinationContact()).append("\n");
        sb.append(" - Phone Number: ").append(payload.getDestinationPhone()).append("\n\n");
        sb.append(" - Destination email: ").append(payload.getDestinationEmail()).append("\n\n");

        sb.append("Order Timeline:\n");
        sb.append(" - Order Placed: ").append(payload.getCreatedAt()).append("\n");
        sb.append(" - Last Updated: ").append(payload.getUpdatedAt()).append("\n\n");

        sb.append("Delivery Status: ").append(payload.isDelivered() ? "Delivered" : "Not Delivered").append("\n\n");

        sb.append("Tracking History:\n");
        Map<LocalDateTime, PackageStatus> history = payload.getStatusHistory();
        if (history != null && !history.isEmpty()) {
            for (Map.Entry<LocalDateTime, PackageStatus> entry : history.entrySet()) {
                sb.append(" - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        } else {
            sb.append(" No tracking history available.\n");
        }

        sb.append("\nIf you have any questions or need further assistance, please contact our support team.\n\n");
        sb.append("Thank you for choosing our service!\n\n");
        sb.append("Best regards,\n");
        sb.append("QickShip\n");

        return sb.toString();
    }

    public String createBodyForSupportTicket(SupportTicketResponse ticket) {
        StringBuilder sb = new StringBuilder();

        sb.append("Hello,\n\n");
        sb.append("Here are the details and current status of your support ticket:\n\n");

        sb.append("Ticket Information:\n");
        sb.append(" - Title: ").append(ticket.getTitle()).append("\n");
        sb.append(" - Description: ").append(ticket.getDescription()).append("\n");
        sb.append(" - Email: ").append(ticket.getEmail()).append("\n");
        sb.append(" - Created At: ").append(ticket.getCreatedAt()).append("\n");
        sb.append(" - Current Status: ").append(ticket.getTicketStatus()).append("\n\n");

        sb.append("Handling History:\n");
        Map<LocalDateTime, TicketStatus> handlingHistory = ticket.getHandlingHistory();
        if (handlingHistory != null && !handlingHistory.isEmpty()) {
            for (Map.Entry<LocalDateTime, TicketStatus> entry : handlingHistory.entrySet()) {
                sb.append(" - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        } else {
            sb.append(" No status updates yet.\n");
        }

        sb.append("\nMessage History:\n");
        Map<LocalDateTime, String> messageHistory = ticket.getMessageHistory();
        if (messageHistory != null && !messageHistory.isEmpty()) {
            for (Map.Entry<LocalDateTime, String> entry : messageHistory.entrySet()) {
                sb.append(" - ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        } else {
            sb.append(" No messages yet.\n");
        }

        sb.append("\nThank you for reaching out to our support team.\n");
        sb.append("We'll continue to keep you informed regarding any updates.\n\n");

        sb.append("Best regards,\n");
        sb.append("QickShip\n");

        return sb.toString();
    }

}

