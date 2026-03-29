package com.devtrack.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class OllamaService {

    public String analyze(String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:11434/api/generate";

        Map<String, Object> body = Map.of(
                "model", "llama3",
                "prompt", prompt,
                "stream", false
        );

        Map response = restTemplate.postForObject(url, body, Map.class);

        return response.get("response").toString();
    }
}