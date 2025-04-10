package com.example.orderservice.service;

import com.example.orderservice.entity.SupportTicket;
import com.example.orderservice.enums.TicketStatus;
import com.example.orderservice.repository.SupportTicketRepository;
import com.example.orderservice.request.support.SupportTicketRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;


    public SupportTicket createSupportTicket(SupportTicketRequest ticketRequest) {
        SupportTicket supportTicket = ticketRequest.createSupportTicket(ticketRequest);
        return supportTicketRepository.save(supportTicket);
    }

    public SupportTicket updateSupportTicket(Long id, TicketStatus newStatus) {
        SupportTicket supportTicket = supportTicketRepository.findById(id).orElse(null);
        supportTicket.setTicketStatus(newStatus);
        return supportTicketRepository.saveAndFlush(supportTicket);
    }
}
