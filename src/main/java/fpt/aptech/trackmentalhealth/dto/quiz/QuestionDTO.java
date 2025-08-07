package fpt.aptech.trackmentalhealth.dto.quiz;

import fpt.aptech.trackmentalhealth.entities.DifficultLevel;
import fpt.aptech.trackmentalhealth.entities.Topic;
import lombok.Data;

import java.util.List;
@Data
public class QuestionDTO {
    private Integer id;
    private String content;
    private String type;
    private DifficultLevel difficulty;
    private Integer score;
    private String topicName; // hoặc topicId nếu cần
    private List<OptionDTO> options;
    private List<TopicDTO> topics;
    private List<MatchingItemDTO> matchingItems; // Dữ liệu matching ở đây

    // getters, setters, constructor
}