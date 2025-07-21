package fpt.aptech.trackmentalhealth.entities;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class QuizQuestionId implements Serializable {
    private Long quizId;
    private Long questionId;
}
