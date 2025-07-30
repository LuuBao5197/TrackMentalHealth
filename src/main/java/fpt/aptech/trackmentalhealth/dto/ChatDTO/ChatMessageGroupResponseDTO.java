package fpt.aptech.trackmentalhealth.dto.ChatDTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageGroupResponseDTO {
    private int id;
    private String content;
    private int senderId;
    private String senderName;
    private int groupId;
    private LocalDateTime sendAt;
}
