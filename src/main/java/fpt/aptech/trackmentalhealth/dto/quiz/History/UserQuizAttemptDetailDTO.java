package fpt.aptech.trackmentalhealth.dto.quiz.History;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class UserQuizAttemptDetailDTO {
    private Integer attemptId;
    private String quizTitle;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalScore;
    private String resultLabel;
    private List<UserQuizAnswerDTO> answers;
}
