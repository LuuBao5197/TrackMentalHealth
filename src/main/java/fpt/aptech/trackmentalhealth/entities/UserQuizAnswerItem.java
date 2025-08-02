package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class UserQuizAnswerItem {

    @EmbeddedId
    private UserQuizAnswerItemId id = new UserQuizAnswerItemId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("attemptId")
    @JoinColumn(name = "attempt_id")
    private UserQuizAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;

    @OneToMany(mappedBy = "answerItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserQuizAnswerItemOption> selectedOptions = new ArrayList<>();

    private String userInput; // for text/number input

    private Integer score;

}