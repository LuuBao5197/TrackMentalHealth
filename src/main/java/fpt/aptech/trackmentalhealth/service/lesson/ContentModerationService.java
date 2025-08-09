package fpt.aptech.trackmentalhealth.service.lesson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ContentModerationService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean isSensitiveContent(String content) {
        // Bỏ qua nếu nội dung null hoặc rỗng
        if (content == null || content.trim().isEmpty()) {
            return false;
        }

        // Giới hạn độ dài content để tránh lỗi 400
        if (content.length() > 1000) {
            content = content.substring(0, 1000);
        }

        try {
            String apiUrl = "https://api.openai.com/v1/moderations";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + openAiApiKey);
            headers.set("Content-Type", "application/json");

            Map<String, String> requestBodyMap = Map.of("input", content);
            String requestBody = objectMapper.writeValueAsString(requestBodyMap);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);

            // Kiểm tra response để xác định nội dung nhạy cảm
            return response.getBody() != null && response.getBody().contains("\"flagged\": true");

        } catch (HttpClientErrorException e) {
            System.err.println("OpenAI API returned 400 Bad Request: " + e.getResponseBodyAsString());
            return true; // Giả định nội dung nhạy cảm nếu có lỗi từ API
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to OpenAI API. Please check your API key or network connection.", e);
        }
    }

    public void checkApiConnection() {
        try {
            isSensitiveContent("test connection");
            System.out.println("OpenAI API connection is successful.");
        } catch (RuntimeException e) {
            throw new RuntimeException("OpenAI API connection failed. Please check your API key or network.", e);
        }
    }
}