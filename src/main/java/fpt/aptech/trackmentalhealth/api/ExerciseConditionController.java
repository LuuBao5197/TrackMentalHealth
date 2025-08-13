package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.dto.ExerciseConditionDTO;
import fpt.aptech.trackmentalhealth.entities.ExerciseCondition;
import fpt.aptech.trackmentalhealth.service.exercise.ExerciseConditionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises/{exerciseId}/conditions")
@RequiredArgsConstructor
public class ExerciseConditionController {

    private final ExerciseConditionService exerciseConditionService;

    @PostMapping
    public ResponseEntity<ExerciseConditionDTO> addCondition(
            @PathVariable Integer exerciseId,
            @RequestBody ExerciseCondition condition
    ) {
        return ResponseEntity.ok(exerciseConditionService.addCondition(exerciseId, condition));
    }

    @GetMapping
    public ResponseEntity<List<ExerciseConditionDTO>> getConditionsByExercise(
            @PathVariable Integer exerciseId
    ) {
        return ResponseEntity.ok(exerciseConditionService.getConditionsByExercise(exerciseId));
    }

}
