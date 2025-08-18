package fpt.aptech.trackmentalhealth.dto.quiz.History;

import fpt.aptech.trackmentalhealth.dto.quiz.OptionDTO;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;



@Data
public class UserQuizAnswerDTO {
    private Integer questionId;
    private String questionText;
    private String questionType;
    private Integer score;

    // User's choices for Single/Multi choice
    private List<OptionDTO> selectedOptions;

    // User's answers for Matching
    private List<MatchingPairDTO> matchingAnswers;

    // User's answers for Ordering
    private List<OrderingAnswerDTO> orderingAnswers;

    // User's input for text questions
    private String userInput;
}


