package fpt.aptech.trackmentalhealth.dto.quiz;

import lombok.Data;

import java.util.List;

@Data
public class SubmitQuizRequest {
    private Integer quizId;
    private Integer userId;
    private List<QuestionAnswerDto> answers;


    @Data
    public class QuestionAnswerDto {
        private Integer questionId;
        private String userInput; // For text/number input
        private List<Integer> selectedOptionIds; // For multi-choice
    }
}
