package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.ChatMessage;
import fpt.aptech.trackmentalhealth.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class ChatSocketController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{sessionId}") // Khi client gửi lên "/app/chat/{sessionId}"
    public void send(@DestinationVariable int sessionId, ChatMessage chatMessage) {
        chatMessage.setId((int)(Math.random() * 1000000));
        chatMessage.setSession(chatService.getChatSessionById(sessionId));
        chatMessage.setIsRead(false);

        // Lưu tin nhắn vào database
        ChatMessage savedMessage = chatService.sendMessage(chatMessage);

        // Gửi tin nhắn tới tất cả client đăng ký "/topic/chat/{sessionId}"
        messagingTemplate.convertAndSend("/topic/chat/" + sessionId, savedMessage);
    }
}
