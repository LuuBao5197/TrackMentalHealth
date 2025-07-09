package fpt.aptech.trackmentalhealth.api;


import fpt.aptech.trackmentalhealth.dto.ExerciseDTO;
import fpt.aptech.trackmentalhealth.entities.Exercise;
import fpt.aptech.trackmentalhealth.service.exercise.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercise")
public class ExerciseController {
    @Autowired
    ExerciseService exerciseService;

    // API CUA Exercise //
    @GetMapping("/")
    public ResponseEntity<List<ExerciseDTO>> getAllExercises() {
        List<ExerciseDTO> exercises = exerciseService.getExerciseDTOs();
        return ResponseEntity.ok(exercises);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseDTO> getExerciseById(@PathVariable Integer id) {
        ExerciseDTO exerciseDTO = exerciseService.getExerciseDTOById(id);
        return ResponseEntity.ok().body(exerciseDTO);
    }

    @PostMapping("/")
    public ResponseEntity<ExerciseDTO> createArticle(@RequestBody Exercise exercise) {
        ExerciseDTO exerciseDTO = exerciseService.createExerciseDTO(exercise);
        return ResponseEntity.status(HttpStatus.CREATED).body(exerciseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseDTO> updateArticle(@PathVariable Integer id, @RequestBody Exercise exercise) {
        ExerciseDTO exerciseDTO = exerciseService.updateExerciseDTO(id, exercise);
        return ResponseEntity.status(HttpStatus.OK).body(exerciseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Exercise> deleteArticle(@PathVariable Integer id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}