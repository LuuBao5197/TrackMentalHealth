package fpt.aptech.trackmentalhealth.service.exercise;

import fpt.aptech.trackmentalhealth.dto.ExerciseConditionDTO;
import fpt.aptech.trackmentalhealth.entities.Exercise;
import fpt.aptech.trackmentalhealth.entities.ExerciseCondition;
import fpt.aptech.trackmentalhealth.repository.exercise.ExerciseConditionRepository;
import fpt.aptech.trackmentalhealth.repository.exercise.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseConditionService {

    private static final Logger logger = LoggerFactory.getLogger(ExerciseConditionService.class);
    private final ExerciseRepository exerciseRepository;
    private final ExerciseConditionRepository exerciseConditionRepository;

    @Transactional
    public ExerciseConditionDTO addCondition(Integer exerciseId, ExerciseConditionDTO conditionDTO) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found with ID: " + exerciseId));

        if (!"camera".equalsIgnoreCase(exercise.getMediaType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conditions can only be added to exercises with mediaType = 'camera'");
        }

        ExerciseCondition condition = new ExerciseCondition();
        condition.setType(conditionDTO.getType());
        condition.setDescription(conditionDTO.getDescription());
        condition.setDuration(conditionDTO.getDuration());
        condition.setExercise(exercise);

        if (conditionDTO.getStepOrder() == null) {
            int nextOrder = exerciseConditionRepository.findByExerciseOrderByStepOrderAsc(exercise)
                    .stream()
                    .mapToInt(ExerciseCondition::getStepOrder)
                    .max()
                    .orElse(0) + 1;
            condition.setStepOrder(nextOrder);
        } else {
            condition.setStepOrder(conditionDTO.getStepOrder());
        }

        ExerciseCondition saved = exerciseConditionRepository.save(condition);
        logger.info("Condition added with ID: {} for exercise ID: {}", saved.getId(), exerciseId);

        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ExerciseConditionDTO> getConditionsByExercise(Integer exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found with ID: " + exerciseId));

        List<ExerciseConditionDTO> conditions = exerciseConditionRepository
                .findByExerciseOrderByStepOrderAsc(exercise)
                .stream()
                .map(this::mapToDTO)
                .toList();

        logger.info("Fetched {} conditions for exercise ID: {}", conditions.size(), exerciseId);
        return conditions;
    }

    @Transactional
    public ExerciseConditionDTO updateCondition(Integer exerciseId, Long conditionId, ExerciseConditionDTO conditionDTO) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found with ID: " + exerciseId));

        ExerciseCondition condition = exerciseConditionRepository.findById(conditionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Condition not found with ID: " + conditionId));

        if (!condition.getExercise().getId().equals(exerciseId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Condition does not belong to the specified exercise");
        }

        condition.setType(conditionDTO.getType());
        condition.setDescription(conditionDTO.getDescription());
        condition.setDuration(conditionDTO.getDuration());
        condition.setStepOrder(conditionDTO.getStepOrder());

        ExerciseCondition updated = exerciseConditionRepository.save(condition);
        logger.info("Condition updated with ID: {} for exercise ID: {}", conditionId, exerciseId);
        return mapToDTO(updated);
    }

    @Transactional
    public void deleteCondition(Integer exerciseId, Long conditionId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found with ID: " + exerciseId));

        ExerciseCondition condition = exerciseConditionRepository.findById(conditionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Condition not found with ID: " + conditionId));

        if (!condition.getExercise().getId().equals(exerciseId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Condition does not belong to the specified exercise");
        }

        exerciseConditionRepository.delete(condition);
        logger.info("Condition deleted with ID: {} for exercise ID: {}", conditionId, exerciseId);
    }

    private ExerciseConditionDTO mapToDTO(ExerciseCondition condition) {
        return new ExerciseConditionDTO(
                condition.getId(),
                condition.getType(),
                condition.getDescription(),
                condition.getDuration(),
                condition.getExercise().getId(),
                condition.getStepOrder()
        );
    }
}