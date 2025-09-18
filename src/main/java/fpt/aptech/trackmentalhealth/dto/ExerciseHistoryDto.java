package fpt.aptech.trackmentalhealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExerciseHistoryDto {
    private Long id;
    private Integer exerciseId;
    private String title;
    private String status;
    private Integer score;
    private String difficultyLevel;
}
