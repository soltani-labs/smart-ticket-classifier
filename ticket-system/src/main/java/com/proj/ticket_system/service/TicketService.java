package com.proj.ticket_system.service;

import com.proj.ticket_system.dto.ClassificationOverrideRequest;
import com.proj.ticket_system.dto.TicketRequest;
import com.proj.ticket_system.dto.TicketResponse;
import com.proj.ticket_system.entity.Category;
import com.proj.ticket_system.entity.Priority;
import com.proj.ticket_system.entity.Ticket;
import com.proj.ticket_system.entity.TicketClassification;
import com.proj.ticket_system.entity.TicketStatus;
import com.proj.ticket_system.repository.TicketClassificationRepository;
import com.proj.ticket_system.repository.TicketRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketClassificationRepository classificationRepository;
    private final ClassificationRunner classificationRunner;

    public TicketService(TicketRepository ticketRepository,
                         TicketClassificationRepository classificationRepository,
                         ClassificationRunner classificationRunner) {
        this.ticketRepository = ticketRepository;
        this.classificationRepository = classificationRepository;
        this.classificationRunner = classificationRunner;
    }

    public TicketResponse createTicket(TicketRequest request) {
        Ticket ticket = new Ticket(request.getSubject(), request.getDescription(), request.getCustomerEmail());
        ticket = ticketRepository.save(ticket);

        // runs on a separate thread — request returns immediately with status OPEN
        classificationRunner.classify(ticket.getId());

        return TicketResponse.from(ticket);
    }

    public TicketResponse getTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found: " + id));
        return TicketResponse.from(ticket);
    }

    public List<TicketResponse> getTickets(Category category, Priority priority, TicketStatus status) {
        return ticketRepository.findWithFilters(category, priority, status)
                .stream()
                .map(TicketResponse::from)
                .toList();
    }

    public TicketResponse overrideClassification(Long ticketId, ClassificationOverrideRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found: " + ticketId));

        TicketClassification classification = classificationRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.CONFLICT, "Ticket has not been classified yet, nothing to override"));

        boolean changed = classification.getAiCategory() != request.getFinalCategory()
                || classification.getAiPriority() != request.getFinalPriority();

        classification.setFinalCategory(request.getFinalCategory());
        classification.setFinalPriority(request.getFinalPriority());
        classification.setHumanOverride(changed);

        classificationRepository.save(classification);
        return TicketResponse.from(ticket);
    }
}