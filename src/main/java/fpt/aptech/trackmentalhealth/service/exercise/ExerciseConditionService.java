package fpt.aptech.trackmentalhealth.service.exercise;

import fpt.aptech.trackmentalhealth.dto.ExerciseConditionDTO;
import fpt.aptech.trackmentalhealth.entities.Exercise;
import fpt.aptech.trackmentalhealth.entities.ExerciseCondition;
import fpt.aptech.trackmentalhealth.repository.exercise.ExerciseConditionRepository;
import fpt.aptech.trackmentalhealth.repository.exercise.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseConditionService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseConditionRepository exerciseConditionRepository;


    public ExerciseConditionDTO addCondition(Integer exerciseId, ExerciseCondition condition) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        if (exercise.getMediaType() == null ||
                !exercise.getMediaType().equalsIgnoreCase("camera")) {
            throw new IllegalArgumentException(
                    "Conditions can only be added to exercises with mediaType = 'camera'"
            );
        }

        condition.setExercise(exercise);
        ExerciseCondition saved = exerciseConditionRepository.save(condition);

        return new ExerciseConditionDTO(
                saved.getId(),
                saved.getType(),
                saved.getDescription(),
                saved.getDuration(),
                saved.getExercise().getId()
        );
    }

    public List<ExerciseConditionDTO> getConditionsByExercise(Integer exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        return exerciseConditionRepository.findByExercise(exercise)
                .stream()
                .map(cond -> new ExerciseConditionDTO(
                        cond.getId(),
                        cond.getType(),
                        cond.getDescription(),
                        cond.getDuration(),
                        cond.getExercise().getId()
                ))
                .toList();
    }
}

