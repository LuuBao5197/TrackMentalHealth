package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.ChatMessage;
import fpt.aptech.trackmentalhealth.entities.ChatSession;
import fpt.aptech.trackmentalhealth.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/{sessionId}")
    public List<ChatMessage> getMessages(@PathVariable int sessionId) {
        return chatService.getChatMessagesByChatSessionId(sessionId);
    }

    @GetMapping("/session/{userId}")
    public List<ChatSession> getUserChatSessions(@PathVariable int userId) {
        return chatService.getChatSessionByUserId(userId);
    }
}
