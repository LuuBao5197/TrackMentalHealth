package fpt.aptech.trackmentalhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
public class LessonDTO {
    private Integer id;
    private String title;
    private String description;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
    private Integer createdBy;


    public LessonDTO() {
    }

    public LessonDTO(fpt.aptech.trackmentalhealth.entities.Lesson lesson) {
        this.id = lesson.getId();
        this.title = lesson.getTitle();
        this.description = lesson.getDescription();
        this.status = lesson.getStatus();
        this.createdAt = lesson.getCreatedAt();
        this.updatedAt = lesson.getUpdatedAt();
        this.createdBy = lesson.getCreatedBy() != null ? lesson.getCreatedBy().getId() : null;
    }
}
