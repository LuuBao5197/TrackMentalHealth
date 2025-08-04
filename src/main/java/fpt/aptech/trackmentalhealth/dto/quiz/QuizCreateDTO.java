package fpt.aptech.trackmentalhealth.dto.quiz;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizCreateDTO {
    private String title;
    private String description;
    private Integer timeLimit;
    private List<Integer> questionIds;
}
