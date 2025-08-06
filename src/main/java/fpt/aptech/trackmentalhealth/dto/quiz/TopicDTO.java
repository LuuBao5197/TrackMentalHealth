package fpt.aptech.trackmentalhealth.dto.quiz;

import lombok.Data;

import java.util.List;
@Data
public class TopicDTO {
    private Integer id;
    private String name; // e.g., "Depression", "Anxiety", etc.
    private boolean deleted;
}
