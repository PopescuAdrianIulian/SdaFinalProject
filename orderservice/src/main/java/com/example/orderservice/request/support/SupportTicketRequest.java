package com.example.orderservice.request.support;

import com.example.orderservice.entity.SupportTicket;
import com.example.orderservice.enums.TicketStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupportTicketRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be at most 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    public SupportTicket createSupportTicket(SupportTicketRequest supportTicketRequest) {
        return SupportTicket.builder()
                .title(supportTicketRequest.getTitle())
                .description(supportTicketRequest.getDescription())
                .email(supportTicketRequest.getEmail())
                .createdAt(LocalDateTime.now())
                .ticketStatus(TicketStatus.OPEN)
                .build();
    }
}
