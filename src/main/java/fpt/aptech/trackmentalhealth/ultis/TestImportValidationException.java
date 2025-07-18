package fpt.aptech.trackmentalhealth.ultis;

import java.util.List;

public class TestImportValidationException extends RuntimeException {
    private final List<String> errors;

    public TestImportValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
