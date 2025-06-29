package fpt.aptech.trackmentalhealth.dto;

import fpt.aptech.trackmentalhealth.entities.Exercise;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ExerciseDTO {
    private Integer id;
    private String title;
    private String instruction;
    private String mediaUrl;
    private String mediaType;
    private Integer estimatedDuration;
    private Integer createdById; // 👈 Chỉ hiển thị id
    private String status;
    private Instant createdAt;

    public ExerciseDTO() {
    }

    public ExerciseDTO(Exercise exercise) {
        this.id = exercise.getId();
        this.title = exercise.getTitle();
        this.instruction = exercise.getInstruction();
        this.mediaUrl = exercise.getMediaUrl();
        this.mediaType = exercise.getMediaType();
        this.estimatedDuration = exercise.getEstimatedDuration();
        this.createdById = (exercise.getCreatedBy() != null) ? exercise.getCreatedBy().getId() : null; // 👈 chỉ lấy id
        this.status = exercise.getStatus();
        this.createdAt = exercise.getCreatedAt();
    }
}
