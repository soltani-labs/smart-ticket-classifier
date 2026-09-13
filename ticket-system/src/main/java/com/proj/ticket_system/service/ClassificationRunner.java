package com.proj.ticket_system.service;

import com.proj.ticket_system.dto.LlmClassificationResult;
import com.proj.ticket_system.entity.Ticket;
import com.proj.ticket_system.entity.TicketClassification;
import com.proj.ticket_system.entity.TicketStatus;
import com.proj.ticket_system.repository.TicketRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ClassificationRunner {

    private final TicketRepository ticketRepository;
    private final LlmClassificationService llmClassificationService;

    public ClassificationRunner(TicketRepository ticketRepository,
                                LlmClassificationService llmClassificationService) {
        this.ticketRepository = ticketRepository;
        this.llmClassificationService = llmClassificationService;
    }

    @Async
    public void classify(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket == null) {
            return;
        }

        LlmClassificationResult result;
        try {
            result = llmClassificationService.classify(ticket);
        } catch (Exception e) {
            result = null;
        }

        if (result == null) {
            ticket.setStatus(TicketStatus.UNCLASSIFIED);
            ticketRepository.save(ticket);
            return;
        }

        TicketClassification classification = new TicketClassification(
                ticket, result.getCategory(), result.getPriority(), result.getSummary()
        );
        ticket.setClassification(classification);
        ticket.setStatus(TicketStatus.CLASSIFIED);
        ticketRepository.save(ticket);
    }
}