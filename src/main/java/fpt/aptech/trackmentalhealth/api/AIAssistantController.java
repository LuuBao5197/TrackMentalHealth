package fpt.aptech.trackmentalhealth.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AIAssistantController {

    @Value("${openai.api.key}")
    private String apiKey;

    @PostMapping("/analyze-mood")
    public ResponseEntity<Map<String, String>> analyzeMood(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);


            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-4o-mini");
            body.put("messages", List.of(Map.of("role", "user", "content", prompt)));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://api.openai.com/v1/chat/completions",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );
            System.out.println("✅ Response status: " + response.getStatusCode());
            System.out.println("✅ Response body: " + response.getBody());

            if (response.getStatusCode() == HttpStatus.OK) {
                List choices = (List) response.getBody().get("choices");
                if (!choices.isEmpty()) {
                    Map message = (Map) ((Map) choices.get(0)).get("message");
                    String content = (String) message.get("content");
                    return ResponseEntity.ok(Map.of("result", content));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(500).body(Map.of("result", "Không thể phân tích lúc này."));
    }
}
