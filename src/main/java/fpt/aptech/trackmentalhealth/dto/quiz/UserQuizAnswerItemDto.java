package fpt.aptech.trackmentalhealth.dto.quiz;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserQuizAnswerItemDto {
    private Integer questionId;
    private List<Integer> selectedOptionIds;
    private String userInput;
}
