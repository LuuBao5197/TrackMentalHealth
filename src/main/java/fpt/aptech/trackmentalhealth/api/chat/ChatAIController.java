package fpt.aptech.trackmentalhealth.api.chat;

import fpt.aptech.trackmentalhealth.dto.ChatDTO.ChatAIRequest;
import fpt.aptech.trackmentalhealth.entities.ChatHistory;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.chat.ChatHistoryRepository;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/chatai")
@CrossOrigin
public class ChatAIController {

    @Autowired
    private ChatHistoryRepository chatHistoryRepo;

    @Autowired
    private UserRepository usersRepo;

    private final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final String API_KEY = "Bearer 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";

    @PostMapping("/ask")
    public ResponseEntity<String> askAI(@RequestBody ChatAIRequest request) {
        try {
            String message = request.getMessage();
            int userId = request.getUserId();

            System.out.println("DEBUG request: message=" + message + ", userId=" + userId);

            if (message == null || message.isEmpty()) {
                return ResponseEntity.badRequest().body("Thiếu message");
            }

            Optional<Users> optionalUser = usersRepo.findById(userId);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.badRequest().body("User không tồn tại");
            }

            Users user = optionalUser.get();

            // 📝 Lưu tin nhắn user
            ChatHistory userChat = new ChatHistory();
            userChat.setUser(user);
            userChat.setRole("user");
            userChat.setMessage(message);
            userChat.setTimestamp(LocalDateTime.now());
            chatHistoryRepo.save(userChat);

            // 🔗 Gọi OpenAI
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", API_KEY);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-4o-mini");

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", message));
            body.put("messages", messages);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                Map data = response.getBody();
                if (data != null && data.get("choices") != null) {
                    List choices = (List) data.get("choices");
                    if (!choices.isEmpty()) {
                        Map choice = (Map) choices.get(0);
                        Map messageMap = (Map) choice.get("message");
                        String aiContent = (String) messageMap.get("content");

                        // 📝 Lưu tin nhắn AI
                        ChatHistory aiChat = new ChatHistory();
                        aiChat.setUser(user);
                        aiChat.setRole("ai");
                        aiChat.setMessage(aiContent);
                        aiChat.setTimestamp(LocalDateTime.now());
                        chatHistoryRepo.save(aiChat);

                        return ResponseEntity.ok(aiContent);
                    }
                }
                return ResponseEntity.ok("AI không trả lời được.");
            } else {
                return ResponseEntity.status(response.getStatusCode()).body("Lỗi gọi AI");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
        }
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<ChatHistory>> getHistory(@PathVariable int userId) {
        Optional<Users> optionalUser = usersRepo.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<ChatHistory> chats = chatHistoryRepo.findByUserIdOrderByTimestampAsc(userId);
        return ResponseEntity.ok(chats);
    }

}
