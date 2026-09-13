package com.proj.ticket_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class TicketRequest {

    @NotBlank(message = "subject is required")
    private String subject;

    @NotBlank(message = "description is required")
    private String description;

    @NotBlank(message = "customerEmail is required")
    @Email(message = "customerEmail must be a valid email")
    private String customerEmail;

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
}