package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestionId implements Serializable {
    private Integer quizId;
    private Integer questionId;
}
