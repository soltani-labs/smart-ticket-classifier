package com.proj.ticket_system.controller;

import com.proj.ticket_system.dto.AccuracyStatsResponse;
import com.proj.ticket_system.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/accuracy")
    public AccuracyStatsResponse getAccuracy() {
        return statsService.getAccuracyStats();
    }
}