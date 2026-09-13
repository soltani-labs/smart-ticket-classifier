package com.proj.ticket_system.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proj.ticket_system.dto.LlmClassificationResult;
import com.proj.ticket_system.entity.Category;
import com.proj.ticket_system.entity.Priority;
import com.proj.ticket_system.entity.Ticket;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class LlmClassificationService {

    private final RestClient llmRestClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LlmClassificationService(RestClient llmRestClient) {
        this.llmRestClient = llmRestClient;
    }

    @Retryable(
            retryFor = Exception.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public LlmClassificationResult classify(Ticket ticket) {
        String prompt = buildPrompt(ticket);

        Map<String, Object> body = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        String rawResponse = llmRestClient.post()
                .body(body)
                .retrieve()
                .body(String.class);

        return parseResponse(rawResponse);
    }

    // called automatically once classify() has exhausted all retry attempts
    @Recover
    public LlmClassificationResult recover(Exception ex, Ticket ticket) {
        return null; // signals the caller: could not classify, fall back to UNCLASSIFIED
    }

    private String buildPrompt(Ticket ticket) {
        return """
                Classify this support ticket. Respond ONLY with raw JSON, no markdown, no code fences, in this exact shape:
                {"category": "BILLING|TECHNICAL|ACCOUNT|OTHER", "priority": "LOW|MEDIUM|HIGH|URGENT", "summary": "one line summary"}

                Subject: %s
                Description: %s
                """.formatted(ticket.getSubject(), ticket.getDescription());
    }

    private LlmClassificationResult parseResponse(String rawResponse) {
        try {
            JsonNode root = objectMapper.readTree(rawResponse);
            String text = root.path("candidates").get(0)
                    .path("content").path("parts").get(0)
                    .path("text").asText()
                    .trim()
                    .replaceAll("^```json", "")
                    .replaceAll("^```", "")
                    .replaceAll("```$", "")
                    .trim();

            JsonNode parsed = objectMapper.readTree(text);

            Category category = Category.valueOf(parsed.get("category").asText());
            Priority priority = Priority.valueOf(parsed.get("priority").asText());
            String summary = parsed.get("summary").asText();

            return new LlmClassificationResult(category, priority, summary);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse LLM response", e);
        }
    }
}