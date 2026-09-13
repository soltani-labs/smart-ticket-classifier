package com.proj.ticket_system.repository;

import com.proj.ticket_system.entity.Category;
import com.proj.ticket_system.entity.Priority;
import com.proj.ticket_system.entity.Ticket;
import com.proj.ticket_system.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("""
        SELECT t FROM Ticket t
        LEFT JOIN t.classification c
        WHERE (:category IS NULL OR c.finalCategory = :category)
        AND (:priority IS NULL OR c.finalPriority = :priority)
        AND (:status IS NULL OR t.status = :status)
        ORDER BY t.createdAt DESC
        """)
    List<Ticket> findWithFilters(
            @Param("category") Category category,
            @Param("priority") Priority priority,
            @Param("status") TicketStatus status
    );
}