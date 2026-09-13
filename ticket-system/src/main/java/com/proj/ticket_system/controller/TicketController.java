package com.proj.ticket_system.controller;

import com.proj.ticket_system.dto.*;
import com.proj.ticket_system.entity.Category;
import com.proj.ticket_system.entity.Priority;
import com.proj.ticket_system.entity.TicketStatus;
import com.proj.ticket_system.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody TicketRequest request) {
        TicketResponse response = ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<TicketResponse> getTickets(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) TicketStatus status
    ) {
        return ticketService.getTickets(category, priority, status);
    }

    @GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable Long id) {
        return ticketService.getTicket(id);
    }

    @PatchMapping("/{id}/classification")
    public TicketResponse overrideClassification(
            @PathVariable Long id,
            @Valid @RequestBody ClassificationOverrideRequest request
    ) {
        return ticketService.overrideClassification(id, request);
    }
}