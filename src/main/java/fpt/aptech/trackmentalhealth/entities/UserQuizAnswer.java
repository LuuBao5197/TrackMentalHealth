package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class UserQuizAnswer {

    @EmbeddedId
    private UserQuizAnswerId id = new UserQuizAnswerId();

    @ManyToOne
    @MapsId("attemptId")
    @JoinColumn(name = "attempt_id")
    private UserQuizAttempt attempt;

    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "selected_option_id")
    private Option selectedOption;
//    private String userInput; // cho TEXT_INPUT
    private Double score; // Nếu mỗi đáp án có điểm riêng thì có thể lưu lại tại đây
}
