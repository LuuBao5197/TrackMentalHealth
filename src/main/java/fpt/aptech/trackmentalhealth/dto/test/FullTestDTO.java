package fpt.aptech.trackmentalhealth.dto.test;

import lombok.Data;

import java.util.List;
@Data
public class FullTestDTO {
    private Long id; // null nếu là create
    private String title;
    private String description;
    private String instructions;
    private List<QuestionDTO> questions;
    private Boolean hasResult;
}
