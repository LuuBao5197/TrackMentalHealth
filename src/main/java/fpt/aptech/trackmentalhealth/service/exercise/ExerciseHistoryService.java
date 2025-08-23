package fpt.aptech.trackmentalhealth.service.exercise;

import fpt.aptech.trackmentalhealth.entities.Exercise;
import fpt.aptech.trackmentalhealth.entities.ExerciseHistory;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.exercise.ExerciseHistoryRepository;
import fpt.aptech.trackmentalhealth.repository.exercise.ExerciseRepository;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExerciseHistoryService {

    @Autowired
    private ExerciseHistoryRepository exerciseHistoryRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveExerciseHistory(int userId, int exerciseId, String status, Integer score, String feedback, String difficultyLevel) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found"));

        ExerciseHistory history = ExerciseHistory.builder()
                .user(user)
                .exercise(exercise)
                .completedAt(LocalDateTime.now())
                .status(status)
                .score(score)
                .feedback(feedback)
                .difficultyLevel(difficultyLevel)
                .build();

        exerciseHistoryRepository.save(history);
    }
}