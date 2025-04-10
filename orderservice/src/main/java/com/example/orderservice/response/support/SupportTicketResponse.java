package com.example.orderservice.response.support;

import com.example.orderservice.entity.SupportTicket;
import com.example.orderservice.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupportTicketResponse {

    private String title;

    private String description;

    private String email;

    private LocalDateTime createdAt;

    private TicketStatus ticketStatus;

    public SupportTicketResponse createSupportTicketResponse(SupportTicket supportTicket) {
        return SupportTicketResponse.builder()
                .title(supportTicket.getTitle())
                .description(supportTicket.getDescription())
                .email(supportTicket.getEmail())
                .createdAt(supportTicket.getCreatedAt())
                .ticketStatus(supportTicket.getTicketStatus())
                .build();
    }
}
