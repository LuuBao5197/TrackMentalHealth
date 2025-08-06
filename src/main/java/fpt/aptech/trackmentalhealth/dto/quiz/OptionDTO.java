package fpt.aptech.trackmentalhealth.dto.quiz;

import fpt.aptech.trackmentalhealth.entities.Option;
import lombok.Data;

@Data
public class OptionDTO {
    private Integer id;
    private String content;
    private boolean correct;
    private Integer score;
//    private QuestionDTO question;

    public OptionDTO(Option option) {
        this.id = option.getId();
        this.content = option.getContent();
        this.correct = option.isCorrect();
        this.score = option.getScore();
    }

    public OptionDTO() {

    }
}

