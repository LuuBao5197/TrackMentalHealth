package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.dto.quiz.OptionDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.QuestionDTO;
import fpt.aptech.trackmentalhealth.entities.Option;
import fpt.aptech.trackmentalhealth.entities.Question;
import fpt.aptech.trackmentalhealth.entities.Topic;
import fpt.aptech.trackmentalhealth.repository.quiz.QuestionRepository;
import fpt.aptech.trackmentalhealth.repository.quiz.TopicRepository;
import fpt.aptech.trackmentalhealth.ultis.ConvertDTOtoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TopicService topicService;
    @Override
    public Question createQuestion(Question q) {
        return questionRepository.save(q);
    }

    @Override
    public Page<QuestionDTO> getAllQuestions(String keyword, Integer topicId, String type, Pageable pageable) {
        Page<Question> questions;

        if ((keyword == null || keyword.isBlank()) && topicId == null && (type == null || type.isBlank())) {
            questions = questionRepository.findAll(pageable);
        } else {
            questions = questionRepository.searchWithFilters(keyword, topicId, type, pageable);
        }

        // Chuyển đổi mỗi entity -> DTO và giữ nguyên phân trang
        return questions.map(question -> {
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setContent(question.getContent());
            questionDTO.setId(question.getId());
            questionDTO.setType(question.getType());
            questionDTO.setScore(question.getScore());
            questionDTO.setTopicName(question.getTopic().getName());

            List<OptionDTO> optionDTOS = ConvertDTOtoEntity.convertOptionsToOptionDTO(question.getOptions());
            questionDTO.setOptions(optionDTOS);
            return questionDTO;
        });
    }


    @Override
    public Question getQuestionById(Integer id) {
        return questionRepository.findById(id).orElse(null);
    }
}
