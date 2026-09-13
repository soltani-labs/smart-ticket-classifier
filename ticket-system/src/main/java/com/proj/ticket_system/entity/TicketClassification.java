package com.proj.ticket_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ticket_classifications")
public class TicketClassification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "ticket_id", nullable = false, unique = true)
    private Ticket ticket;

    @Enumerated(EnumType.STRING)
    private Category aiCategory;

    @Enumerated(EnumType.STRING)
    private Priority aiPriority;

    @Column(length = 500)
    private String aiSummary;

    @Column(nullable = false)
    private boolean humanOverride = false;

    @Enumerated(EnumType.STRING)
    private Category finalCategory;

    @Enumerated(EnumType.STRING)
    private Priority finalPriority;

    protected TicketClassification() {
    }

    public TicketClassification(Ticket ticket, Category aiCategory, Priority aiPriority, String aiSummary) {
        this.ticket = ticket;
        this.aiCategory = aiCategory;
        this.aiPriority = aiPriority;
        this.aiSummary = aiSummary;
        this.finalCategory = aiCategory;
        this.finalPriority = aiPriority;
    }

    public Long getId() { return id; }

    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }

    public Category getAiCategory() { return aiCategory; }
    public void setAiCategory(Category aiCategory) { this.aiCategory = aiCategory; }

    public Priority getAiPriority() { return aiPriority; }
    public void setAiPriority(Priority aiPriority) { this.aiPriority = aiPriority; }

    public String getAiSummary() { return aiSummary; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }

    public boolean isHumanOverride() { return humanOverride; }
    public void setHumanOverride(boolean humanOverride) { this.humanOverride = humanOverride; }

    public Category getFinalCategory() { return finalCategory; }
    public void setFinalCategory(Category finalCategory) { this.finalCategory = finalCategory; }

    public Priority getFinalPriority() { return finalPriority; }
    public void setFinalPriority(Priority finalPriority) { this.finalPriority = finalPriority; }
}