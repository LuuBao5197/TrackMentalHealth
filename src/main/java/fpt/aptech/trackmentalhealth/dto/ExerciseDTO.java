package fpt.aptech.trackmentalhealth.dto;

import fpt.aptech.trackmentalhealth.entities.ContentCreator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

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
    private ContentCreator createdBy;
    private String status;
    private Instant createdAt;

    public ExerciseDTO() {
    }

    public ExerciseDTO(fpt.aptech.trackmentalhealth.entities.Exercise exercise) {
        this.id = exercise.getId();
        this.title = exercise.getTitle();
        this.instruction = exercise.getInstruction();
        this.mediaUrl = exercise.getMediaUrl();
        this.mediaType = exercise.getMediaType();
        this.estimatedDuration = exercise.getEstimatedDuration();
        this.createdBy = exercise.getCreatedBy();
        this.status = exercise.getStatus();
        this.createdAt = exercise.getCreatedAt();
    }
}
