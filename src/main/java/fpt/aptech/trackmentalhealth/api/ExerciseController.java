package fpt.aptech.trackmentalhealth.api;


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
    public ResponseEntity<List<Exercise>> getExercises() {
        List<Exercise> Exercises =  exerciseService.getExercises();
        return ResponseEntity.ok().body(Exercises);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Exercise> getExerciseById(@PathVariable Integer id) {
        Exercise exercise = exerciseService.getExercise(id);
        return ResponseEntity.ok().body(exercise);
    }
    @PostMapping("/")
    public ResponseEntity<Exercise> createExercise(@RequestBody Exercise exercise) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exerciseService.createExercise(exercise));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exercise> updateExercise(@PathVariable Integer id, @RequestBody Exercise exercise) {
        return ResponseEntity.status(HttpStatus.OK).body(exerciseService.updateExercise(id, exercise));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Exercise> deleteArticle(@PathVariable Integer id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}