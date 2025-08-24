package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.dto.ExerciseHistoryDto;
import fpt.aptech.trackmentalhealth.entities.ExerciseHistory;
import fpt.aptech.trackmentalhealth.repository.exercise.ExerciseHistoryRepository;
import fpt.aptech.trackmentalhealth.service.exercise.ExerciseHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exercise-history")
@RequiredArgsConstructor
public class ExerciseHistoryController {

    private final ExerciseHistoryService exerciseHistoryService;
    private final ExerciseHistoryRepository exerciseHistoryRepository;

    /**
     * Lưu lịch sử thực hiện bài tập
     */
    @PostMapping
    public ResponseEntity<String> saveHistory(@RequestBody Map<String, Object> payload) {
        try {
            int userId = (int) payload.get("userId");
            int exerciseId = (int) payload.get("exerciseId");
            String status = (String) payload.get("status"); // "success" | "fail"
            Integer score = payload.get("score") != null ? (Integer) payload.get("score") : null;
            String title = (String) payload.getOrDefault("title", null);
            String difficultyLevel = (String) payload.getOrDefault("difficultyLevel", null);

            exerciseHistoryService.saveExerciseHistory(
                    userId, exerciseId, status, score, title, difficultyLevel
            );
            return ResponseEntity.ok("Exercise history saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Lấy danh sách lịch sử theo userId
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExerciseHistoryDto>> getHistoryByUser(@PathVariable Long userId) {
        List<ExerciseHistory> histories = exerciseHistoryRepository.findByUserId(userId);

        // convert sang DTO
        List<ExerciseHistoryDto> dtoList = histories.stream()
                .map(h -> new ExerciseHistoryDto(
                        h.getId(),
                        h.getExercise().getId(),
                        h.getTitle(),
                        h.getStatus(),
                        h.getScore(),
                        h.getDifficultyLevel()
                ))
                .toList();

        return ResponseEntity.ok(dtoList);
    }

}
