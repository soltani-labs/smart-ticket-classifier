package com.proj.ticket_system.repository;

import com.proj.ticket_system.entity.TicketClassification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketClassificationRepository extends JpaRepository<TicketClassification, Long> {

    Optional<TicketClassification> findByTicketId(Long ticketId);

    long countByHumanOverrideTrue();
}