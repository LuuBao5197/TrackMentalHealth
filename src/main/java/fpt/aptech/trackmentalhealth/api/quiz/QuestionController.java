package fpt.aptech.trackmentalhealth.api.quiz;

import fpt.aptech.trackmentalhealth.dto.quiz.OptionDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.QuestionCreateDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.QuestionDTO;
import fpt.aptech.trackmentalhealth.entities.Option;
import fpt.aptech.trackmentalhealth.entities.Question;
import fpt.aptech.trackmentalhealth.entities.Topic;
import fpt.aptech.trackmentalhealth.repository.quiz.OptionRepository;
import fpt.aptech.trackmentalhealth.service.quiz.QuestionService;
import fpt.aptech.trackmentalhealth.service.quiz.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private TopicService topicService;
    @Autowired
    private OptionRepository optionRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody QuestionCreateDTO questionDTO) {
        // Tạo và gán thông tin cho Question
        Question newQuestion = new Question();
        newQuestion.setContent(questionDTO.getContent());
        newQuestion.setType(questionDTO.getType());
//        newQuestion.setScore(questionDTO.getScore());

        // Gán Topic nếu cần (nếu bạn có entity Topic)
        if (questionDTO.getTopicID() != null) {
            Topic topic = topicService.getTopicById(questionDTO.getTopicID());
            newQuestion.setTopic(topic);
        }

        // Lưu Question trước để sinh ID
        newQuestion = questionService.createQuestion(newQuestion);

        // Gán và lưu từng Option
        List<OptionDTO> options = questionDTO.getOptions();
        if (options == null || options.isEmpty()) {
            return ResponseEntity.badRequest().body("Question must have at least one option");
        }

        for (OptionDTO optionDTO : options) {
            Option newOption = new Option();
            newOption.setContent(optionDTO.getContent());
            newOption.setCorrect(optionDTO.isCorrect());
            newOption.setQuestion(newQuestion); // Gán Question
            newOption.setScore(optionDTO.getScore());
            optionRepository.save(newOption); // Lưu Option (cần có trong service)
        }
        newQuestion = questionService.createQuestion(newQuestion);

        return ResponseEntity.status(HttpStatus.CREATED).body(newQuestion);
    }



    @GetMapping
    public ResponseEntity<Page<QuestionDTO>> getAllQuestions(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Integer topicId,
            @RequestParam(required = false) String type,
            Pageable pageable
    ) {
        Page<QuestionDTO> result = questionService.getAllQuestions(keyword, topicId, type, pageable);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Question> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }
}
