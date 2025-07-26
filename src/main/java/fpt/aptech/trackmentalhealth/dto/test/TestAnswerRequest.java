package fpt.aptech.trackmentalhealth.dto.test;

import lombok.Data;

import java.util.List;

@Data
public class TestAnswerRequest {
    private Integer userId;
    private Integer testId;
    private List<AnswerDetail> answers;
    private String result;

    @Data
    public static class AnswerDetail {
        private Integer questionId;
        private Integer selectedOptionId;
    }
}
