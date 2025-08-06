package fpt.aptech.trackmentalhealth.dto.quiz;

import lombok.Data;

@Data
public class QuizDTO {
    private Integer id;
    private String title;
    private String description;
    private Integer numberOfQuestions;
    private Integer totalScore;
    private Integer timeLimit;
    private Boolean hasResults;
}
