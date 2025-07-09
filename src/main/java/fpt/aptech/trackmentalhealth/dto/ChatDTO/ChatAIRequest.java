package fpt.aptech.trackmentalhealth.dto.ChatDTO;

public class ChatAIRequest {
    private String message;
    private int userId;

    public ChatAIRequest() {
    }

    public ChatAIRequest(String message, int userId) {
        this.message = message;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
