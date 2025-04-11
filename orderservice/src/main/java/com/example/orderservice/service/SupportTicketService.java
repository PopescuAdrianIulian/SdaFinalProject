package com.example.orderservice.service;

import com.example.orderservice.entity.SupportTicket;
import com.example.orderservice.enums.TicketStatus;
import com.example.orderservice.repository.SupportTicketRepository;
import com.example.orderservice.repository.UserRepository;
import com.example.orderservice.request.support.SupportTicketRequest;
import com.example.orderservice.response.support.SupportTicketResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupportTicketService {

    private final SupportTicketRepository supportTicketRepository;
    private final UserRepository userRepository;

    public SupportTicket createSupportTicket(SupportTicketRequest ticketRequest) {
        LocalDateTime now = LocalDateTime.now();

        SupportTicket supportTicket = ticketRequest.createSupportTicket(ticketRequest);
        supportTicket.setHandlingHistory(Map.of(now, TicketStatus.OPEN));
        supportTicket.setMessageHistory(Map.of(now, ticketRequest.getDescription()));

        return supportTicketRepository.save(supportTicket);
    }

    public SupportTicket updateSupportTicketStatus(Long id, String newStatus) {
        TicketStatus status = parseStatus(newStatus);
        SupportTicket ticket = findTicketById(id);

        if (!ticket.getTicketStatus().equals(TicketStatus.RESOLVED)) {
            ticket.setTicketStatus(status);
            supportTicketRepository.saveAndFlush(ticket);
        }

        return ticket;
    }

    public List<SupportTicketResponse> getAllOpenTickets() {
        log.info("Retrieving open tickets");
        return getFilteredTickets(ticket -> ticket.getTicketStatus() == TicketStatus.OPEN);
    }

    public void addMessageToSupportTicket(Long id, String message) {
        SupportTicket ticket = findTicketById(id);
        ticket.getMessageHistory().put(LocalDateTime.now(), message);
        supportTicketRepository.saveAndFlush(ticket);
        log.info("Added message to ticket ID {}: {}", id, message);
    }

    public List<SupportTicketResponse> getMyAssignedTickets(Long userId) {
        String email = findUserEmailById(userId);
        log.info("Retrieving assigned tickets for user: {}", email);
        return getFilteredTickets(ticket -> email.equals(ticket.getAssignee()));
    }

    public List<SupportTicketResponse> getMyAssignedTicketsInProgress(Long userId) {
        String email = findUserEmailById(userId);
        log.info("Retrieving assigned and in-progress tickets for user: {}", email);
        return getFilteredTickets(ticket ->
                ticket.getTicketStatus() == TicketStatus.OPEN && email.equals(ticket.getAssignee()));
    }

    public SupportTicketResponse findSupportTicketById(Long id) {
        return toResponse(findTicketById(id));
    }

    public List<SupportTicketResponse> getAllTicketsByEmail(String email) {
        List<SupportTicket> tickets = supportTicketRepository.findAllByEmail(email);
        return toResponses(tickets);
    }

    private TicketStatus parseStatus(String status) {
        try {
            return TicketStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid TicketStatus: {}", status);
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    private SupportTicket findTicketById(Long id) {
        return supportTicketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Support ticket with ID " + id + " not found."));
    }

    private String findUserEmailById(Long userId) {
        return userRepository.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found"))
                .getEmail();
    }

    private List<SupportTicketResponse> getFilteredTickets(Predicate<SupportTicket> predicate) {
        return supportTicketRepository.findAll().stream()
                .filter(predicate)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private SupportTicketResponse toResponse(SupportTicket ticket) {
        return new SupportTicketResponse().createSupportTicketResponse(ticket);
    }

    private List<SupportTicketResponse> toResponses(List<SupportTicket> tickets) {
        return tickets.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
