package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.entities.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuizService {
    Page<Quiz> findAll(Pageable pageable);
    Quiz createQuiz(Quiz quiz);
    Quiz assignQuestionsToQuiz(Integer quizId, List<Integer> questionIds);
    Page<Quiz> searchQuizzes(String keyword, Pageable pageable);
}
