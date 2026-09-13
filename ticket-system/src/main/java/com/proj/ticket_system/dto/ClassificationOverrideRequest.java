package com.proj.ticket_system.dto;

import com.proj.ticket_system.entity.Category;
import com.proj.ticket_system.entity.Priority;
import jakarta.validation.constraints.NotNull;

public class ClassificationOverrideRequest {

    @NotNull(message = "finalCategory is required")
    private Category finalCategory;

    @NotNull(message = "finalPriority is required")
    private Priority finalPriority;

    public Category getFinalCategory() { return finalCategory; }
    public void setFinalCategory(Category finalCategory) { this.finalCategory = finalCategory; }

    public Priority getFinalPriority() { return finalPriority; }
    public void setFinalPriority(Priority finalPriority) { this.finalPriority = finalPriority; }
}