package fpt.aptech.trackmentalhealth.service.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class OpenAiService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateLessonContent(String lessonTitle) {
        try {
            String apiUrl = "https://api.openai.com/v1/chat/completions";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", new Object[] {
                            Map.of("role", "system", "content", "Bạn là một chuyên gia viết nội dung cho các bài học."),
                            Map.of("role", "user", "content", "Viết nội dung bài học cho tiêu đề: " + lessonTitle)
                    },
                    "temperature", 0.7
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("choices").get(0).get("message").get("content").asText();

        } catch (Exception e) {
            e.printStackTrace();
            return "Không thể tạo nội dung. Vui lòng thử lại sau.";
        }
    }
}
