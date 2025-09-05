package fpt.aptech.trackmentalhealth.dto;

import fpt.aptech.trackmentalhealth.entities.ExerciseCondition;
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
    private Integer stepOrder;

    // 👇 constructor từ entity
    public ExerciseConditionDTO(ExerciseCondition condition) {
        this.id = condition.getId();
        this.type = condition.getType();
        this.description = condition.getDescription();
        this.duration = condition.getDuration();
        this.exerciseId = (condition.getExercise() != null) ? condition.getExercise().getId() : null;
        this.stepOrder = condition.getStepOrder();
    }
}
