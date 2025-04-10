package com.example.orderservice.service;

import com.example.orderservice.entity.SupportTicket;
import com.example.orderservice.entity.User;
import com.example.orderservice.enums.TicketStatus;
import com.example.orderservice.repository.SupportTicketRepository;
import com.example.orderservice.repository.UserRepository;
import com.example.orderservice.request.support.SupportTicketRequest;
import com.example.orderservice.response.support.SupportTicketResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final UserRepository userRepository;

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

    public List<SupportTicketResponse> getAllOpenTickets(){
        List<SupportTicket> openTickets = supportTicketRepository.findAll().stream()
                .filter(ticket -> ticket.getTicketStatus() == TicketStatus.OPEN)
                .toList();
       System.out.println("Retrieving OpenTickets");

        return openTickets.stream()
                .map(ticket -> new SupportTicketResponse().createSupportTicketResponse(ticket))
                .toList();
    }

    public List<SupportTicketResponse> getMyAssignedTickets(Long id){
        User user = userRepository.getUserById(id)
                .orElseThrow( () -> new RuntimeException("User with ID " + id + " not found"));
        String email = user.getEmail();
        List<SupportTicket> myTickets = supportTicketRepository.findAll().stream()
                .filter(ticket -> email.equals(ticket.getAssignee()))
                .toList();
        System.out.println("Retrieving Assigned Tickets");
        return myTickets.stream()
                .map(ticket -> new SupportTicketResponse().createSupportTicketResponse(ticket))
                .toList();
    }

    public List<SupportTicketResponse> getMyAssignedTicketsInProgress(Long id){
        User user = userRepository.getUserById(id)
                .orElseThrow( () -> new RuntimeException("User with ID " + id + " not found"));
        String email = user.getEmail();
        List<SupportTicket> openTickets = supportTicketRepository.findAll().stream()
                .filter(ticket -> ticket.getTicketStatus() == TicketStatus.OPEN && email.equals(ticket.getAssignee()))
                .toList();
        System.out.println("Retrieving Assigned and in Progress Tickets");

        return openTickets.stream()
                .map(ticket -> new SupportTicketResponse().createSupportTicketResponse(ticket))
                .toList();
    }

}
