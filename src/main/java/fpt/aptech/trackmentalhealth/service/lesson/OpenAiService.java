package fpt.aptech.trackmentalhealth.service.lesson;

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

    /**
     * Kiểm tra xem chuỗi có chứa dấu tiếng Việt hay không,
     * để xác định ngôn ngữ (đơn giản).
     */
    private boolean isVietnamese(String text) {
        // Chuẩn đơn giản: nếu có ký tự dấu tiếng Việt trong text thì trả true
        return text.matches(".*[àáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡ"
                + "ùúụủũưừứựửữỳýỵỷỹđÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨ"
                + "ÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ].*");
    }

    public String generateLessonContent(String lessonTitle) {
        try {
            String apiUrl = "https://api.openai.com/v1/chat/completions";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            boolean isViet = isVietnamese(lessonTitle);

            // Tạo message system và user theo ngôn ngữ tương ứng
            String systemContent = isViet
                    ? "Bạn là một chuyên gia viết nội dung cho các bài học. Vui lòng trả lời bằng tiếng Việt."
                    : "You are an expert content writer for lessons. Please respond in English.";

            String userContent = isViet
                    ? "Viết nội dung bài học cho tiêu đề: " + lessonTitle
                    : "Write lesson content for the title: " + lessonTitle;

            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", new Object[]{
                            Map.of("role", "system", "content", systemContent),
                            Map.of("role", "user", "content", userContent)
                    },
                    "temperature", 0.7
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("choices").get(0).get("message").get("content").asText();

        } catch (Exception e) {
            e.printStackTrace();
            return isVietnamese(lessonTitle)
                    ? "Không thể tạo nội dung. Vui lòng thử lại sau."
                    : "Unable to generate content. Please try again later.";
        }
    }
}
