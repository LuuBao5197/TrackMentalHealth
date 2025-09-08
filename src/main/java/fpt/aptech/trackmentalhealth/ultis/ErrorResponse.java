package fpt.aptech.trackmentalhealth.ultis;

import java.util.List;

public class ErrorResponse {
    private String message;
    private List<String> errors;

    public ErrorResponse(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }

    // Getters and setters (hoặc dùng Lombok @Data nếu bạn có)
    public String getMessage() {
        return message;
    }

    public List<String> getErrors() {
        return errors;
    }
}
