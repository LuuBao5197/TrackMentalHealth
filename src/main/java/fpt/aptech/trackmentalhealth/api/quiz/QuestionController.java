package fpt.aptech.trackmentalhealth.api.quiz;

import fpt.aptech.trackmentalhealth.dto.quiz.*;
import fpt.aptech.trackmentalhealth.entities.*;
import fpt.aptech.trackmentalhealth.repository.quiz.OptionRepository;
import fpt.aptech.trackmentalhealth.service.quiz.QuestionService;
import fpt.aptech.trackmentalhealth.service.quiz.TopicService;
import fpt.aptech.trackmentalhealth.ultis.ConvertDTOtoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

        if (questionDTO.getDifficulty() != null) {
            newQuestion.setDifficulty(questionDTO.getDifficulty());
        }
        if(questionDTO.getScore() != null) {
            newQuestion.setScore(questionDTO.getScore());
        }

        // Gán Topic nếu có
        if (questionDTO.getTopicID() != null) {
            Topic topic = topicService.getTopicById(questionDTO.getTopicID());
            newQuestion.setTopic(topic);
        }

        // Gán Matching Items (chưa gọi save vội)
        if (questionDTO.getMatchingItems() != null) {
            List<MatchingItemDTO> matchingItemsDTO = questionDTO.getMatchingItems();
            List<MatchingItem> matchingItemList = new ArrayList<>();
            for (MatchingItemDTO matchingItemDTO : matchingItemsDTO) {
                MatchingItem matchingItem = new MatchingItem();
                matchingItem.setLeftItem(matchingItemDTO.getLeftItem());
                matchingItem.setRightItem(matchingItemDTO.getRightItem());
                matchingItem.setQuestion(newQuestion); // set ngược
                matchingItemList.add(matchingItem);
            }
            newQuestion.setMatchingItems(matchingItemList);
        }

        // Gán Options
        List<OptionDTO> options = questionDTO.getOptions();
        if (options != null) {
            List<Option> optionList = new ArrayList<>();
            for (OptionDTO optionDTO : options) {
                Option newOption = new Option();
                newOption.setContent(optionDTO.getContent());
                newOption.setCorrect(optionDTO.isCorrect());
                newOption.setScore(optionDTO.getScore());
                newOption.setQuestion(newQuestion);
                optionList.add(newOption);
            }
            newQuestion.setOptions(optionList); // nếu bạn có setOptions trong entity
        }

        //Gan OrderingItem
        List<OrderingItemDTO> orderingItemsDTO = questionDTO.getOrderingItems();
        if(orderingItemsDTO != null) {
            List<OrderingItem> orderingItemList = new ArrayList<>();
            for (OrderingItemDTO orderingItemDTO : orderingItemsDTO) {
                OrderingItem orderingItem = new OrderingItem();
                orderingItem.setQuestion(newQuestion);
                orderingItem.setContent(orderingItemDTO.getContent());
                orderingItem.setCorrectOrder(orderingItemDTO.getCorrectOrder());
                orderingItemList.add(orderingItem);
            }
            newQuestion.setOrderingItems(orderingItemList);
        }


        // Chỉ bây giờ mới lưu
       questionService.createQuestion(newQuestion);

        return ResponseEntity.status(HttpStatus.CREATED).body("OK Created Question");
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
