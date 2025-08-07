package fpt.aptech.trackmentalhealth.dto.quiz;

import fpt.aptech.trackmentalhealth.entities.DifficultLevel;
import lombok.Data;

import java.util.List;
@Data
public class QuestionCreateDTO {
    private Integer id;
    private String content;
    private String type;
    private DifficultLevel difficulty;
    private Integer score;
//    private Integer score;
    private Integer topicID; // hoặc topicId nếu cần
    private List<OptionDTO> options;
    private List<MatchingItemDTO> matchingItems; // Dữ liệu matching ở đây
}
