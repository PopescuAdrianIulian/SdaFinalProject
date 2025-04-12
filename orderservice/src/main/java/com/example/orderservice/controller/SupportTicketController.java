package com.example.orderservice.controller;

import com.example.orderservice.request.support.SupportTicketRequest;
import com.example.orderservice.response.support.SupportTicketResponse;
import com.example.orderservice.service.SupportTicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/support")
@RequiredArgsConstructor
public class SupportTicketController {

    private final SupportTicketService supportTicketService;

    @PostMapping("/create")
    public ResponseEntity<SupportTicketResponse> createSupportTicket(@Valid @RequestBody SupportTicketRequest ticketRequest) {
        return ResponseEntity.ok(supportTicketService.createSupportTicket(ticketRequest));
    }

    @PostMapping("/{id}/{newStatus}")
    public ResponseEntity<SupportTicketResponse> updateSupportTicketStatus(@Valid @PathVariable Long id,
                                                                           @PathVariable String newStatus) {
        return ResponseEntity.ok(supportTicketService.updateSupportTicketStatus(id, newStatus));
    }

    @PostMapping("/message/{id}")
    public ResponseEntity<Void> addMessagesToSupportTicket(@PathVariable Long id,
                                                           @RequestBody String message) {
        supportTicketService.addMessageToSupportTicket(id, message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/myTickets/{id}")
    public ResponseEntity<List<SupportTicketResponse>> getMyTickets(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(supportTicketService.getMyAssignedTickets(id));
    }

    @GetMapping("/openTickets")
    public ResponseEntity<List<SupportTicketResponse>> getOpenTickets() {
        return ResponseEntity.ok(supportTicketService.getAllOpenTickets());
    }

    @GetMapping("/supportTicket/{id}")
    public ResponseEntity<SupportTicketResponse> findSupportTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(supportTicketService.findSupportTicketById(id));
    }

    @GetMapping("/getAll/{email}")
    public ResponseEntity<List<SupportTicketResponse>> getAllTicketsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(supportTicketService.getAllTicketsByEmail(email));
    }


}
