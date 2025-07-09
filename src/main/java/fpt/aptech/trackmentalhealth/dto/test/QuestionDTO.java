package fpt.aptech.trackmentalhealth.dto.test;

import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private Integer id;
    private String questionText;
    private String questionType;
    private Integer questionOrder;

    private List<OptionDTO> options;
}
