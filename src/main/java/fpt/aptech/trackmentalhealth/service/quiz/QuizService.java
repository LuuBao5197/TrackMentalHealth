package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.dto.quiz.QuestionDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.QuizDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.QuizDetailDTO;
import fpt.aptech.trackmentalhealth.entities.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuizService {
    Page<QuizDTO> findAll(Pageable pageable);
    QuizDetailDTO findOne(Integer id);
    Quiz createQuiz(Quiz quiz);
    Quiz assignQuestionsToQuiz(Integer quizId, List<Integer> questionIds);
    Page<QuizDTO> searchQuizzes(String keyword, Pageable pageable);
    List<QuestionDTO> getQuestionsFromQuiz(Integer quizId);
}
