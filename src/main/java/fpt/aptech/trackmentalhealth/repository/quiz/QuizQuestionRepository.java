package fpt.aptech.trackmentalhealth.repository.quiz;

import fpt.aptech.trackmentalhealth.entities.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Integer> {
    @Query(
            "Select q from QuizQuestion q where q.quiz.id =:quizId"
    )
    List<QuizQuestion> findByQuizId(@PathVariable  Integer quizId);
}
