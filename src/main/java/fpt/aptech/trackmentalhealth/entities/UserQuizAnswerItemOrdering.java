package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Entity
@Getter
@Setter
public class UserQuizAnswerItemOrdering {

    @EmbeddedId
    private UserQuizAnswerItemOrderingId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("attemptQuestionId") // trùng với tên field trong UserQuizAnswerItemOrderingId
    @JoinColumns({
            @JoinColumn(name = "attempt_id", referencedColumnName = "attempt_id"),
            @JoinColumn(name = "question_id", referencedColumnName = "question_id")
    })
    private UserQuizAnswerItem answerItem;

    private Integer itemId;
    private String text;
    private Integer userOrder;


}
