package fpt.aptech.trackmentalhealth.dto.test;

import lombok.Data;

@Data
public class TestResultDTO {
    private Integer id;
    private Integer minScore;
    private Integer maxScore;
    private String resultText;
}
