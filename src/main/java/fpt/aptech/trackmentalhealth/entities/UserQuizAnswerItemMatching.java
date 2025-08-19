package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;


@Entity
@Table(name = "user_quiz_answer_item_matching")
@Data
public class UserQuizAnswerItemMatching {

    @EmbeddedId
    private UserQuizAnswerItemMatchingId id = new UserQuizAnswerItemMatchingId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("answerItemKey") // Tên field trong @Embeddable bên dưới
    @JoinColumns({
            @JoinColumn(name = "attempt_id", referencedColumnName = "attempt_id"),
            @JoinColumn(name = "question_id", referencedColumnName = "question_id")
    })
    private UserQuizAnswerItem answerItem;

    private String leftText;
    private String rightText;


}
