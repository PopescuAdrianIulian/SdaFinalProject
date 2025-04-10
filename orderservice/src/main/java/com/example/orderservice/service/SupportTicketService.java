package com.example.orderservice.service;

import com.example.orderservice.entity.SupportTicket;
import com.example.orderservice.enums.TicketStatus;
import com.example.orderservice.repository.SupportTicketRepository;
import com.example.orderservice.request.support.SupportTicketRequest;
import com.example.orderservice.response.support.SupportTicketResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;

    public SupportTicket createSupportTicket(SupportTicketRequest ticketRequest) {
        SupportTicket supportTicket = ticketRequest.createSupportTicket(ticketRequest);
        return supportTicketRepository.save(supportTicket);
    }

    public SupportTicket updateSupportTicketStatus(Long id, String newStatus) {
        TicketStatus status;
        try {
            status = TicketStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid TicketStatus: {}", newStatus);
            throw new IllegalArgumentException("Invalid status: " + newStatus);
        }

        Optional<SupportTicket> optionalTicket = supportTicketRepository.findById(id);
        if (optionalTicket.isEmpty()) {
            throw new IllegalArgumentException("Support ticket with ID " + id + " not found.");
        }

        SupportTicket supportTicket = optionalTicket.get();

        if (!supportTicket.getTicketStatus().equals(TicketStatus.RESOLVED)) {
            supportTicket.setTicketStatus(status);
            supportTicket = supportTicketRepository.saveAndFlush(supportTicket);
        }

        return supportTicket;
    }

    public List<SupportTicketResponse> getAllUnresolvedTickets() {
        List<SupportTicket> unresolvedTickets = supportTicketRepository.findAll().stream()
                .filter(ticket -> ticket.getTicketStatus() != TicketStatus.RESOLVED)
                .toList();

        return unresolvedTickets.stream()
                .map(ticket -> new SupportTicketResponse().createSupportTicketResponse(ticket))
                .toList();
    }
}
