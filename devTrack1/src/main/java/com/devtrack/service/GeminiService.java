package com.devtrack.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String API_KEY;

    private final WebClient webClient =
            WebClient.create("https://generativelanguage.googleapis.com");

    public String analyze(String prompt) {
        try {
            String response = webClient.post()
                    .uri("/v1/models/gemini-1.5-flash:generateContent?key=" + API_KEY)
                    .bodyValue("""
                    {
                      "contents": [{
                        "parts": [{
                          "text": "%s"
                        }]
                      }]
                    }
                    """.formatted(prompt))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return "AI temporarily unavailable.";
        }
    }
}