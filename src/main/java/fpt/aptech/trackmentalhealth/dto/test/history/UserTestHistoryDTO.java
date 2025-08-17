package fpt.aptech.trackmentalhealth.dto.test.history;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTestHistoryDTO {
    private Integer attemptId;
    private String testTitle;
    private Instant startedAt;
    private Instant completedAt;
    private Integer totalScore;
}
