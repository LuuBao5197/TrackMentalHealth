package fpt.aptech.trackmentalhealth.dto.quiz;

import lombok.Data;

import java.util.List;
@Data
public class AnswerSubmissionDTO {
    private Integer questionId;
    private List<Integer> selectedOptionIds;
    private String userInput;
}
