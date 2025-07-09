package fpt.aptech.trackmentalhealth.api.chat;

import fpt.aptech.trackmentalhealth.dto.ChatDTO.ChatMessageDTO;
import fpt.aptech.trackmentalhealth.entities.ChatMessage;
import fpt.aptech.trackmentalhealth.entities.Users;
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
        chatMessage.setSession(chatService.getChatSessionById(sessionId));
        chatMessage.setIsRead(false);
        // Lưu tin nhắn vào database
        ChatMessage savedMessage = chatService.sendMessage(chatMessage);
        // Lấy thông tin người gửi
        Users sender = savedMessage.getSession().getSender();
        String senderName = (sender != null) ? sender.getFullname() : "Người dùng";

        // Tạo DTO để gửi qua WebSocket
        assert sender != null;
        ChatMessageDTO dto = new ChatMessageDTO(
                savedMessage.getMessage(),
                sender.getId(),
                senderName
        );

        // Gửi DTO đến frontend (tránh lỗi Hibernate proxy)
        messagingTemplate.convertAndSend("/topic/chat/" + sessionId, dto);
    }
}
