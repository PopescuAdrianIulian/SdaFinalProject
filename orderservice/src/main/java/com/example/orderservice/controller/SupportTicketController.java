package com.example.orderservice.controller;

import com.example.orderservice.enums.TicketStatus;
import com.example.orderservice.request.support.SupportTicketRequest;
import com.example.orderservice.service.SupportTicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/support")
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    @PostMapping("/create")
    public ResponseEntity<?> createSupportTicket(@Valid @RequestBody SupportTicketRequest ticketRequest) {
        return ResponseEntity.ok(supportTicketService.createSupportTicket(ticketRequest));
    }

    @PostMapping("/{id}/{newStatus}")
    public ResponseEntity<?> updateSupportTicket(@Valid @PathVariable Long id, TicketStatus newStatus) {
        return ResponseEntity.ok(supportTicketService.updateSupportTicket(id, newStatus));
    }
}
