package fpt.aptech.trackmentalhealth.repository.exercise;

import fpt.aptech.trackmentalhealth.entities.ExerciseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExerciseHistoryRepository extends JpaRepository<ExerciseHistory, Long> {

    // Tìm lịch sử bài tập theo userId
    List<ExerciseHistory> findByUserId(Long userId);

    // Tìm lịch sử bài tập theo userId và exerciseId
    List<ExerciseHistory> findByUserIdAndExerciseId(Long userId, Long exerciseId);

    // Tìm lịch sử bài tập theo userId và trạng thái (ví dụ: COMPLETED, FAILED)
    List<ExerciseHistory> findByUserIdAndStatus(Long userId, String status);

    // Tìm lịch sử bài tập theo userId và mức độ khó
    List<ExerciseHistory> findByUserIdAndDifficultyLevel(Long userId, String difficultyLevel);

    // Tìm lịch sử bài tập trong khoảng thời gian
    List<ExerciseHistory> findByUserIdAndCompletedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}