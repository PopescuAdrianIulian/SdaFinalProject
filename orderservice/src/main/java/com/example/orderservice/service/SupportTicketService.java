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
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<String, SupportTicketResponse> kafkaTemplate;

    public SupportTicketResponse createSupportTicket(SupportTicketRequest ticketRequest) {
        LocalDateTime now = LocalDateTime.now();
        SupportTicket supportTicket = ticketRequest.createSupportTicket(ticketRequest);
        supportTicket.setHandlingHistory(Map.of(now, TicketStatus.OPEN));
        supportTicket.setMessageHistory(Map.of(now, ticketRequest.getDescription()));

        SupportTicketResponse payload = new SupportTicketResponse().createSupportTicketResponse(supportTicket);
        kafkaTemplate.send("support-notification", payload);
        log.info("Notification sent to kafka support {}", payload);
        supportTicketRepository.save(supportTicket);
        return payload;
    }

    public SupportTicketResponse updateSupportTicketStatus(Long id, String newStatus, Long adminId) {
        TicketStatus status = parseStatus(newStatus);

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        SupportTicket supportTicket = findTicketById(id);
        if (!supportTicket.getTicketStatus().equals(TicketStatus.RESOLVED)) {
            if (status == TicketStatus.PROCESSING) {
                supportTicket.setAssigneeEmail(admin.getEmail());
            }
            supportTicket.setTicketStatus(status);
            supportTicketRepository.saveAndFlush(supportTicket);
        }
        SupportTicketResponse payload = new SupportTicketResponse().createSupportTicketResponse(supportTicket);
        kafkaTemplate.send("support-notification", payload);

        return payload;
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
        return getFilteredTickets(ticket -> email.equals(ticket.getAssigneeEmail()));
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
                .filter(predicate.and(ticket -> ticket.getTicketStatus() != TicketStatus.RESOLVED))
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
