package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class UserQuizAnswerItemMatchingId implements Serializable {

    @AttributeOverrides({
            @AttributeOverride(name = "attemptId", column = @Column(name = "attempt_id")),
            @AttributeOverride(name = "questionId", column = @Column(name = "question_id"))
    })
    private UserQuizAnswerItemId answerItemKey; // khóa phụ của UserQuizAnswerItem

    private Integer pairIndex;

    public UserQuizAnswerItemMatchingId() {
    }

    public UserQuizAnswerItemMatchingId(Integer attemptId, Integer questionId, Integer pairIndex) {
        this.answerItemKey = new UserQuizAnswerItemId(attemptId, questionId);
        this.pairIndex = pairIndex;
    }
}


