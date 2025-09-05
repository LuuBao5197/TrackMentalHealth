package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.dto.ExerciseConditionDTO;
import fpt.aptech.trackmentalhealth.entities.ExerciseCondition;
import fpt.aptech.trackmentalhealth.service.exercise.ExerciseConditionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/exercises/{exerciseId}/conditions")
@RequiredArgsConstructor
@Validated
public class ExerciseConditionController {

    private static final Logger logger = LoggerFactory.getLogger(ExerciseConditionController.class);
    private final ExerciseConditionService exerciseConditionService;

    @PostMapping
    public ResponseEntity<ExerciseConditionDTO> addCondition(
            @PathVariable Integer exerciseId,
            @Valid @RequestBody ExerciseConditionDTO conditionDTO
    ) {
        logger.info("Adding condition for exercise ID: {}", exerciseId);
        ExerciseConditionDTO savedCondition = exerciseConditionService.addCondition(exerciseId, conditionDTO);
        return new ResponseEntity<>(savedCondition, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExerciseConditionDTO>> getConditionsByExercise(
            @PathVariable Integer exerciseId
    ) {
        logger.info("Fetching conditions for exercise ID: {}", exerciseId);
        List<ExerciseConditionDTO> conditions = exerciseConditionService.getConditionsByExercise(exerciseId);
        return ResponseEntity.ok(conditions);
    }

    @PutMapping("/{conditionId}")
    public ResponseEntity<ExerciseConditionDTO> updateCondition(
            @PathVariable Integer exerciseId,
            @PathVariable Long conditionId,
            @Valid @RequestBody ExerciseConditionDTO conditionDTO
    ) {
        logger.info("Updating condition ID: {} for exercise ID: {}", conditionId, exerciseId);
        ExerciseConditionDTO updatedCondition = exerciseConditionService.updateCondition(exerciseId, conditionId, conditionDTO);
        return ResponseEntity.ok(updatedCondition);
    }

    @DeleteMapping("/{conditionId}")
    public ResponseEntity<Void> deleteCondition(
            @PathVariable Integer exerciseId,
            @PathVariable Long conditionId
    ) {
        logger.info("Deleting condition ID: {} for exercise ID: {}", conditionId, exerciseId);
        exerciseConditionService.deleteCondition(exerciseId, conditionId);
        return ResponseEntity.noContent().build();
    }
}