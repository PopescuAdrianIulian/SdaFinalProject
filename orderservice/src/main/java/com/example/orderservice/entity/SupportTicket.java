package com.example.orderservice.entity;

import com.example.orderservice.config.MessageHistoryJsonConverter;
import com.example.orderservice.config.TicketStatusJsonConverter;
import com.example.orderservice.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String email;

    private LocalDateTime createdAt;

    private String assigneeEmail;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    @Convert(converter = TicketStatusJsonConverter.class)
    @Column(columnDefinition = "JSON")
    private Map<LocalDateTime, TicketStatus> handlingHistory;

    @Convert(converter = MessageHistoryJsonConverter.class)
    @Column(columnDefinition = "JSON")
    private Map<LocalDateTime, String> messageHistory;

}
