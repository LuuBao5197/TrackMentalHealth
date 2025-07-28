package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.entities.Question;
import fpt.aptech.trackmentalhealth.entities.Quiz;
import fpt.aptech.trackmentalhealth.entities.QuizQuestion;
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

    // phuong thuc lay danh sach cau hoi tu ngan hang
    Page<Question> getQuestions(Pageable pageable, String category);

    Question getQuestion(Integer questionId);

    Question createQuestion(Question question);

    Question updateQuestion(Integer questionId, Question question);

    void deleteQuestion(Integer questionId);

    // phuong thuc them cau hoi cho bai quiz
    List<QuizQuestion> getQuizQuestionsByQuizId(Integer quizId);
    List<QuizQuestion> createMultipleQuizQuestions(List<QuizQuestion> quizQuestions);
    List<QuizQuestion> updateMultipleQuizQuestions(List<QuizQuestion> quizQuestions);


}
