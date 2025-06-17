package fpt.aptech.trackmentalhealth.controller;
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

    @GetMapping("/{id}")
    public List<ChatMessage> getChatMessages(@PathVariable int id) {
        return chatService.getChatMessagesByChatSessionId(id);
    }

    @GetMapping("/list/{psyId}")
    public List<ChatSession> getChatSessionsByPsyId(@PathVariable int psyId) {
        return chatService.getChatSessionsByPsyId(psyId);
    }

    @PostMapping("/{id}")
    public ChatMessage addChatMessage(@PathVariable int id,
                                      @RequestBody ChatMessage chatMessage) {
        ChatMessage newMessage = new ChatMessage();
        newMessage.setId((int) (Math.random() * 1000000));
        newMessage.setSession(chatService.getChatSessionById(id));
        newMessage.setSenderId(chatMessage.getSenderId());
        newMessage.setMessage(chatMessage.getMessage());
        newMessage.setIsRead(false);
        return chatService.sendMessage(newMessage);
    }
}
