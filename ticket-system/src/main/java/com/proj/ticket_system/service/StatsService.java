package com.proj.ticket_system.service;

import com.proj.ticket_system.dto.AccuracyStatsResponse;
import com.proj.ticket_system.repository.TicketClassificationRepository;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    private final TicketClassificationRepository classificationRepository;

    public StatsService(TicketClassificationRepository classificationRepository) {
        this.classificationRepository = classificationRepository;
    }

    public AccuracyStatsResponse getAccuracyStats() {
        long total = classificationRepository.count();
        long overridden = classificationRepository.countByHumanOverrideTrue();

        double accuracy = total == 0 ? 0.0 : (double) (total - overridden) / total * 100.0;

        return new AccuracyStatsResponse(total, overridden, Math.round(accuracy * 100.0) / 100.0);
    }
}