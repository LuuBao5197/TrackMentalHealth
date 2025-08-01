package fpt.aptech.trackmentalhealth.dto.quiz;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizSubmissionDTO {
    private Integer userId;
    private Integer quizId;
    private List<AnswerSubmissionDTO> answers;
}
