package fpt.aptech.trackmentalhealth.dto.test;

import lombok.Data;

import java.util.List;
@Data
public class TestDetailDTO {
    private Integer id;
    private String title;
    private String description;
    private String instructions;
    private List<QuestionDTO> questions;
    private List<TestResultDTO> results;
}
