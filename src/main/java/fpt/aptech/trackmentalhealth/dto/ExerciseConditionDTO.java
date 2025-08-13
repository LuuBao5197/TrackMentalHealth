package fpt.aptech.trackmentalhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseConditionDTO {
    private Long id;
    private String type;
    private String description;
    private String duration;
    private Integer exerciseId;
}
