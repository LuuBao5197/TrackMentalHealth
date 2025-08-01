package fpt.aptech.trackmentalhealth.dto.quiz;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class QuizAttemptRequest {
    private Integer quizId;
    private Integer userId;
    private List<AnswerItemDTO> answers;

    @Getter
    @Setter
    public static class AnswerItemDTO {
        private Integer questionId;
        private String userInput;
        private List<Integer> selectedOptionIds;
    }
}
