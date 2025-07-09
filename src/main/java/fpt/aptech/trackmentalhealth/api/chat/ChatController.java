package fpt.aptech.trackmentalhealth.api.chat;

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
    public List<ChatMessage> getMessagesBySessionId(@PathVariable int sessionId) {
        return chatService.getChatMessagesByChatSessionId(sessionId);
    }

    @GetMapping("/session/{userId}")
    public List<ChatSession> getChatSessionsByUserId(@PathVariable int userId) {
        return chatService.getChatSessionByUserId(userId);
    }

    @GetMapping("/session/{user1}/{user2}")
    public ChatSession getChatSessionByFromAndTo(@PathVariable int user1,
                                                 @PathVariable int user2) {
        return chatService.getChatSessionByFromAndTo(user1, user2);
    }
}
