package com.proj.ticket_system.dto;

public class AccuracyStatsResponse {

    private long totalClassified;
    private long overriddenCount;
    private double accuracyPercentage;

    public AccuracyStatsResponse(long totalClassified, long overriddenCount, double accuracyPercentage) {
        this.totalClassified = totalClassified;
        this.overriddenCount = overriddenCount;
        this.accuracyPercentage = accuracyPercentage;
    }

    public long getTotalClassified() { return totalClassified; }
    public long getOverriddenCount() { return overriddenCount; }
    public double getAccuracyPercentage() { return accuracyPercentage; }
}