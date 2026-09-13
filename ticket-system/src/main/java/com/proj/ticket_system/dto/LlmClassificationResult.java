package com.proj.ticket_system.dto;

import com.proj.ticket_system.entity.Category;
import com.proj.ticket_system.entity.Priority;

public class LlmClassificationResult {

    private final Category category;
    private final Priority priority;
    private final String summary;

    public LlmClassificationResult(Category category, Priority priority, String summary) {
        this.category = category;
        this.priority = priority;
        this.summary = summary;
    }

    public Category getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public String getSummary() { return summary; }
}