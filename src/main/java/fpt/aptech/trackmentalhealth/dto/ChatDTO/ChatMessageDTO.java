package fpt.aptech.trackmentalhealth.dto.ChatDTO;

// ChatMessageDTO.java (server-side)
public class ChatMessageDTO {
    private String message;
    private int senderId;
    private String senderName;

    // Constructors
    public ChatMessageDTO() {}

    public ChatMessageDTO(String message, int senderId, String senderName) {
        this.message = message;
        this.senderId = senderId;
        this.senderName = senderName;
    }

    // Getters and setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
}
