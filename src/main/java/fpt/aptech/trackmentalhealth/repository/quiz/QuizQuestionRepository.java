package fpt.aptech.trackmentalhealth.repository.quiz;

import fpt.aptech.trackmentalhealth.entities.QuizQuestion;
import fpt.aptech.trackmentalhealth.entities.QuizQuestionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, QuizQuestionId> {
    List<QuizQuestion> findByQuiz_Id(Integer quizId);

}
