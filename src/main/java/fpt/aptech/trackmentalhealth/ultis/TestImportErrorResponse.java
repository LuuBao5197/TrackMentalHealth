package fpt.aptech.trackmentalhealth.ultis;

import java.util.List;

public class TestImportErrorResponse {
    private String message;
    private List<String> errors;

    public TestImportErrorResponse(String message, List<String> errors) {
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
