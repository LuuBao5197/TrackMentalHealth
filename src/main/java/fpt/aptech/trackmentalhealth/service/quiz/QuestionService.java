package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.dto.quiz.QuestionDTO;
import fpt.aptech.trackmentalhealth.entities.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {
    Question createQuestion(Question question);
    Page<QuestionDTO> getAllQuestions(String keyword, Integer topicId, String type, Pageable pageable);
    Question getQuestionById(Integer id);
    Integer getMaxScoreOfQuestion(Integer id);
}
