package fpt.aptech.trackmentalhealth.dto;

import fpt.aptech.trackmentalhealth.dto.LessonStepDto;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class LessonDto {
    private Integer id;
    private String title;
    private String description;
    private String status;
    private String photo;
    private Instant createdAt;
    private Instant updatedAt;
    private Integer createdBy; // ID của user tạo bài học
    private List<LessonStepDto> lessonSteps;
}
