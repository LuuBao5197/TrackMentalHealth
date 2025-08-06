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

    @PostMapping("/session/create")
    public ChatSession createChatSession(@RequestBody ChatSession chatSession) {
        return chatService.createChatSession(chatSession);
    }

    @PostMapping("/session/initiate/{senderId}/{receiverId}")
    public ChatSession initiateChatSession(@PathVariable int senderId, @PathVariable int receiverId) {
        ChatSession existing = chatService.getChatSessionByFromAndTo(senderId, receiverId);
        if (existing != null) {
            return existing;
        }
        ChatSession newSession = new ChatSession();
        newSession.setSender(chatService.findUserById(senderId));  // cần method findUserById trong service
        newSession.setReceiver(chatService.findUserById(receiverId));
        newSession.setStartTime(java.time.LocalDateTime.now());
        newSession.setStatus("active");

        return chatService.createChatSession(newSession);
    }


    @PutMapping("/changeStatus/{sessionId}/{receiverId}")
    public void changeStatus(@PathVariable int sessionId, @PathVariable int receiverId) {
        chatService.changeStatusIsRead(sessionId, receiverId);
    }

    //check unread
    @GetMapping("/has-unread/{receiverId}")
    public boolean hasUnreadMessages(@PathVariable int receiverId) {
        return chatService.hasUnreadMessages(receiverId);
    }

    @GetMapping("/lastest-message/{sessionId}")
    public ChatMessage getLastestMessageBySessionId(@PathVariable int sessionId){
        return chatService.getLatestMessage(sessionId);
    }

}
