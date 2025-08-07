package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.dto.quiz.OptionDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.QuestionDTO;
import fpt.aptech.trackmentalhealth.entities.DifficultLevel;
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
            questionDTO.setDifficulty(question.getDifficulty());
            if(question.getTopic() != null) {
                questionDTO.setTopicName(question.getTopic().getName());
            }
            List<OptionDTO> optionDTOS = ConvertDTOtoEntity.convertOptionsToOptionDTO(question.getOptions());
            questionDTO.setOptions(optionDTOS);
            return questionDTO;
        });
    }


    @Override
    public Question getQuestionById(Integer id) {
        return questionRepository.findById(id).orElse(null);
    }

    @Override
    public Integer getMaxScoreOfQuestion(Integer id) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question == null) {
            return 0;
        }
        Integer maxScore = 0;
        // DOI VOI CAC DANG CAU HOI LOAI TRU BASED-SCORED THI PHAN LOAI CAU HOI VA CHO DIEM CUA CAU HOI
        if( question.getDifficulty() != null ) {
            return question.getScore();
        }
        // DOI VOI DANG CAU HOI DAC BIET BASED_SCORED THI DIEM CUA CUA HOI PHU THUOC VAO DIEM CAC DAP AN CUA NO
        for (Option option : question.getOptions()) {
            if(option.getScore() > maxScore) {
                maxScore = option.getScore();
            }
        }
        return maxScore;
    }
}
