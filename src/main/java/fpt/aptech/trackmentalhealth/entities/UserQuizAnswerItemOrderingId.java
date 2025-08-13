package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class UserQuizAnswerItemOrderingId implements Serializable {

    private UserQuizAnswerItemId attemptQuestionId; // nhúng ID cha
    private Integer itemIndex;

    public UserQuizAnswerItemOrderingId() {
    }

    public UserQuizAnswerItemOrderingId(Integer attemptId, Integer questionId, Integer itemIndex) {
        this.attemptQuestionId = new UserQuizAnswerItemId(attemptId, questionId);
        this.itemIndex = itemIndex;
    }
}
