package fpt.aptech.trackmentalhealth.api.chat;

import fpt.aptech.trackmentalhealth.dto.ChatDTO.ChatMessageDTO;
import fpt.aptech.trackmentalhealth.dto.ChatDTO.ChatMessageGroupResponseDTO;
import fpt.aptech.trackmentalhealth.dto.ChatDTO.GroupMessageDTO;
import fpt.aptech.trackmentalhealth.entities.*;
import fpt.aptech.trackmentalhealth.service.chat.ChatService;
import fpt.aptech.trackmentalhealth.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Controller
public class ChatSocketController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // 💬 1-1 Chat
    @MessageMapping("/chat/{sessionId}")
    public void sendPrivateMessage(@DestinationVariable int sessionId, @Payload ChatMessage chatMessage) {
        ChatSession session = chatService.getChatSessionById(sessionId);
        if (session == null) {
            System.err.println("❌ Session không tồn tại với ID: " + sessionId);
            return;
        }

        chatMessage.setSession(session);
        chatMessage.setTimestamp(new Date());
        chatMessage.setIsRead(false);

        Users sender = userService.findById(String.valueOf(chatMessage.getSender().getId()));
        if (sender == null) {
            System.err.println("❌ Sender không hợp lệ");
            return;
        }

        ChatMessage savedMessage = chatService.sendMessage(chatMessage);

        String senderName = sender.getFullname() != null ? sender.getFullname() : "user";
        ChatMessageDTO dto = new ChatMessageDTO(savedMessage.getMessage(), sender.getId(), senderName);

        // Gửi cho tất cả client trong session
        messagingTemplate.convertAndSend("/topic/chat/" + sessionId, dto);

        // Gửi riêng đến người nhận (nếu muốn)
        Users receiver = (session.getSender().getId() == sender.getId())
                ? session.getReceiver()
                : session.getSender();

        if (receiver != null) {
            messagingTemplate.convertAndSend("/topic/messages/" + receiver.getId(), dto);
        }
    }

    // 👥 Group Chat
    @MessageMapping("/chat.group.send")
    public void sendGroupMessage(@Payload GroupMessageDTO dto) {
        // Lấy thông tin nhóm và người gửi
        ChatGroup group = chatService.getChatGroupById(dto.getGroupId());
        Users sender = userService.findById(String.valueOf(dto.getSenderId()));

        // Kiểm tra null
        if (group == null || sender == null) {
            System.err.println("❌ Group hoặc Sender không tồn tại");
            return;
        }

        // Tạo tin nhắn nhóm
        ChatMessageGroup newMsg = new ChatMessageGroup();
        newMsg.setGroup(group);
        newMsg.setSender(sender);
        newMsg.setContent(dto.getContent());
        newMsg.setSendAt(new Date());

        // Lưu tin nhắn
        ChatMessageGroup saved = chatService.saveMessageToGroup(newMsg);

        // Tạo DTO để gửi về frontend
        ChatMessageGroupResponseDTO response = new ChatMessageGroupResponseDTO();
        response.setId(saved.getId());
        response.setGroupId(group.getId());
        response.setContent(saved.getContent());
        Date date = saved.getSendAt();
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        response.setSendAt(localDateTime);
        response.setSenderName(sender.getFullname());

        // Gửi DTO đến các client trong nhóm
        messagingTemplate.convertAndSend("/topic/group/" + dto.getGroupId(), response);
    }


}
