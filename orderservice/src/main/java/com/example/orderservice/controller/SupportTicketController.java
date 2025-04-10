package com.example.orderservice.controller;

import com.example.orderservice.request.support.SupportTicketRequest;
import com.example.orderservice.response.support.SupportTicketResponse;
import com.example.orderservice.service.SupportTicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.internal.CreateKeySecondPass;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> updateSupportTicket(@Valid @PathVariable Long id,
                                                 @PathVariable String newStatus) {
        return ResponseEntity.ok(supportTicketService.updateSupportTicketStatus(id, newStatus));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<SupportTicketResponse>> getAllUnresolvedTickets() {
        return ResponseEntity.ok(supportTicketService.getAllUnresolvedTickets());
    }

    @GetMapping("/myTickets/{id}")
    public ResponseEntity<List<SupportTicketResponse>> getMyTickets(@Valid @PathVariable Long id){
        return ResponseEntity.ok(supportTicketService.getMyAssignedTickets(id));
    }

    @GetMapping("/openTickets")
    public ResponseEntity<List<SupportTicketResponse>> getOpenTickets(){
        return ResponseEntity.ok(supportTicketService.getAllOpenTickets());
    }

    @GetMapping("/myOpenTickets")
    public ResponseEntity<List<SupportTicketResponse>> getMyAssignedTicketsInProgress(@Valid @PathVariable Long id){
        return ResponseEntity.ok(supportTicketService.getMyAssignedTicketsInProgress(id));
    }


}
