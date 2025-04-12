package com.example.orderservice.response.support;

import com.example.notificationservice.enums.TicketStatus;
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

}
