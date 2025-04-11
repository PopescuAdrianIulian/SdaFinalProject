package com.example.orderservice.response.support;

import com.example.orderservice.entity.SupportTicket;
import com.example.orderservice.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupportTicketResponse {

    private Long id;

    private String title;

    private String description;

    private String email;

    private LocalDateTime createdAt;

    private TicketStatus ticketStatus;

    private Map<LocalDateTime, TicketStatus> handlingHistory;
    private Map<LocalDateTime, String> messageHistory;


    public SupportTicketResponse createSupportTicketResponse(SupportTicket supportTicket) {
        return SupportTicketResponse.builder()
                .id(supportTicket.getId())
                .title(supportTicket.getTitle())
                .description(supportTicket.getDescription())
                .email(supportTicket.getEmail())
                .createdAt(supportTicket.getCreatedAt())
                .ticketStatus(supportTicket.getTicketStatus())
                .handlingHistory(supportTicket.getHandlingHistory())
                .messageHistory(supportTicket.getMessageHistory())
                .build();
    }
}
