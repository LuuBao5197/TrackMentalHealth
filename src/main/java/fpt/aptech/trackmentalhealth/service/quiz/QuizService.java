package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.entities.Question;
import fpt.aptech.trackmentalhealth.entities.Quiz;
import fpt.aptech.trackmentalhealth.repository.quiz.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface QuizService {
    Page<Quiz> getQuizzes(Pageable pageable);
    Quiz getQuizzesByQuizId(Integer quizId);
    Quiz createQuiz(Quiz quiz);
    Quiz updateQuiz(Integer quizId, Quiz quiz);
    void deleteQuiz(Integer quizId);
    Quiz sendQuizForApproval(Quiz quiz);

    Page<Question> getQuestionsForQuiz(Integer quizId, Pageable pageable);
    Page<Question> getQuestions(Pageable pageable);



}
