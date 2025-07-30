package fpt.aptech.trackmentalhealth.service.lesson;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ContentModerationService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean isSensitiveContent(String content) {
        try {
            String apiUrl = "https://api.openai.com/v1/moderations";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + openAiApiKey);
            headers.set("Content-Type", "application/json");

            String requestBody = "{\"input\": \"" + content.replace("\"", "\\\"") + "\"}";
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
            return response.getBody().contains("\"flagged\": true");
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to OpenAI API. Please check your API key or network connection.");
        }
    }

    public void checkApiConnection() {
        try {
            isSensitiveContent("test");
        } catch (RuntimeException e) {
            throw new RuntimeException("OpenAI API connection failed. Please check your API key or network.");
        }
    }
}