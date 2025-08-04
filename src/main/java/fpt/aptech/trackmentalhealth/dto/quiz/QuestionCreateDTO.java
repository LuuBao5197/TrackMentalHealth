package fpt.aptech.trackmentalhealth.dto.quiz;

import lombok.Data;

import java.util.List;
@Data
public class QuestionCreateDTO {
    private Integer id;
    private String content;
    private String type;
    private Integer score;
    private Integer topicID; // hoặc topicId nếu cần
    private List<OptionDTO> options;
}
