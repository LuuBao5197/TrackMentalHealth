package fpt.aptech.trackmentalhealth.dto.test.history;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTestHistoryDTO {
    private Integer attemptId;
    private String testTitle;
    private Instant startedAt;
    private Instant completedAt;
    private Integer totalScore;
    private String resultLabel;
    private List<UserTestDetailDTO> detailDTOList;
}
