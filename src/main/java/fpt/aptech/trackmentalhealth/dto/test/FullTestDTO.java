package fpt.aptech.trackmentalhealth.dto.test;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
public class FullTestDTO implements Serializable {
    private Long id; // null nếu là create
    private String title;
    private String description;
    private String instructions;
    private List<QuestionDTO> questions;
    private Boolean hasResult;
    private String source; // ✅ Trường mới để hiển thị nguồn
}
