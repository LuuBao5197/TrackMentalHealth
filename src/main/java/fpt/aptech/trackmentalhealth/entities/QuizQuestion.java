package fpt.aptech.trackmentalhealth.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quiz_question")
public class QuizQuestion {

    @EmbeddedId
    private QuizQuestionId id = new QuizQuestionId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("quizId")
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;

    private Integer orderIndex; // Thứ tự hiển thị nếu muốn

    // Các cờ phụ trợ khác nếu muốn: isRequired, note,...
}
