package fpt.aptech.trackmentalhealth.dto;

import fpt.aptech.trackmentalhealth.entities.Exercise;
import fpt.aptech.trackmentalhealth.entities.ExerciseCondition;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ExerciseDTO {
    private Integer id;
    private String title;
    private String instruction;
    private String mediaUrl;
    private String mediaType;
    private Integer estimatedDuration;
    private Integer createdById;
    private String status;
    private Instant createdAt;
    private String photo;
    private String difficultyLevel;

    private List<ExerciseConditionDTO> conditions; // 👈 thêm danh sách condition

    public ExerciseDTO() {
    }

    public ExerciseDTO(Exercise exercise) {
        this.id = exercise.getId();
        this.title = exercise.getTitle();
        this.instruction = exercise.getInstruction();
        this.mediaUrl = exercise.getMediaUrl();
        this.photo = exercise.getPhoto();
        this.mediaType = exercise.getMediaType();
        this.estimatedDuration = exercise.getEstimatedDuration();
        this.createdById = (exercise.getCreatedBy() != null) ? exercise.getCreatedBy().getId() : null;
        this.status = exercise.getStatus();
        this.createdAt = exercise.getCreatedAt();
        this.difficultyLevel = exercise.getDifficultyLevel();

        // 👇 map ExerciseCondition sang DTO
        if(exercise.getConditions() != null) {
            this.conditions = exercise.getConditions().stream()
                    .map(ExerciseConditionDTO::new)
                    .collect(Collectors.toList());
        }
    }
}
