package fpt.aptech.trackmentalhealth.dto.quiz.History;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserQuizHistoryDTO {
    private Integer attemptId;
    private String quizTitle;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalScore;
    private String resultLabel;
}
