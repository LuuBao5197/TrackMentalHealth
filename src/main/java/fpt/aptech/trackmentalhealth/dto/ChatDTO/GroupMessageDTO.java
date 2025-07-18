package fpt.aptech.trackmentalhealth.dto.ChatDTO;

import lombok.Data;

@Data
public class GroupMessageDTO {
    private int groupId;
    private int senderId;
    private String content;
}
