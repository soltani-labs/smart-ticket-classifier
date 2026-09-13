package com.proj.ticket_system.dto;

import com.proj.ticket_system.entity.Category;
import com.proj.ticket_system.entity.Priority;
import com.proj.ticket_system.entity.Ticket;
import com.proj.ticket_system.entity.TicketClassification;
import com.proj.ticket_system.entity.TicketStatus;

import java.time.LocalDateTime;

public class TicketResponse {

    private Long id;
    private String subject;
    private String description;
    private String customerEmail;
    private TicketStatus status;
    private LocalDateTime createdAt;

    private Category aiCategory;
    private Priority aiPriority;
    private String aiSummary;
    private boolean humanOverride;
    private Category finalCategory;
    private Priority finalPriority;

    public static TicketResponse from(Ticket ticket) {
        TicketResponse res = new TicketResponse();
        res.id = ticket.getId();
        res.subject = ticket.getSubject();
        res.description = ticket.getDescription();
        res.customerEmail = ticket.getCustomerEmail();
        res.status = ticket.getStatus();
        res.createdAt = ticket.getCreatedAt();

        TicketClassification c = ticket.getClassification();
        if (c != null) {
            res.aiCategory = c.getAiCategory();
            res.aiPriority = c.getAiPriority();
            res.aiSummary = c.getAiSummary();
            res.humanOverride = c.isHumanOverride();
            res.finalCategory = c.getFinalCategory();
            res.finalPriority = c.getFinalPriority();
        }
        return res;
    }

    public Long getId() { return id; }
    public String getSubject() { return subject; }
    public String getDescription() { return description; }
    public String getCustomerEmail() { return customerEmail; }
    public TicketStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Category getAiCategory() { return aiCategory; }
    public Priority getAiPriority() { return aiPriority; }
    public String getAiSummary() { return aiSummary; }
    public boolean isHumanOverride() { return humanOverride; }
    public Category getFinalCategory() { return finalCategory; }
    public Priority getFinalPriority() { return finalPriority; }
}