package fpt.aptech.trackmentalhealth.dto.quiz;

import fpt.aptech.trackmentalhealth.entities.QuizQuestion;
import fpt.aptech.trackmentalhealth.entities.QuizResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class QuizDetailDTO {
    private Integer id;
    private String title;
    private String description;
    private Integer numberOfQuestions;
    private Integer totalScore;
    private Integer timeLimit;
    private List<QuestionDTO> quizQuestions;
    private List<QuizResultDTO> quizResults;
}