package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.dto.ExerciseDTO;
import fpt.aptech.trackmentalhealth.entities.ContentCreator;
import fpt.aptech.trackmentalhealth.entities.Exercise;
import fpt.aptech.trackmentalhealth.repository.contentcreator.ContentCreatorRepository;
import fpt.aptech.trackmentalhealth.service.exercise.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercise")
@CrossOrigin("*")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ContentCreatorRepository contentCreatorRepository;

    // GET all exercises
    @GetMapping("/")
    public ResponseEntity<List<ExerciseDTO>> getAllExercises() {
        return ResponseEntity.ok(exerciseService.getExerciseDTOs());
    }

    // GET one exercise
    @GetMapping("/{id}")
    public ResponseEntity<?> getExerciseById(@PathVariable Integer id) {
        try {
            ExerciseDTO dto = exerciseService.getExerciseDTOById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exercise not found");
        }
    }

    // CREATE
    @PostMapping("/")
    public ResponseEntity<?> createExercise(@RequestBody ExerciseDTO request) {
        try {
            Exercise exercise = new Exercise();
            exercise.setTitle(request.getTitle());
            exercise.setInstruction(request.getInstruction());
            exercise.setMediaUrl(request.getMediaUrl());
            exercise.setMediaType(request.getMediaType());
            exercise.setEstimatedDuration(request.getEstimatedDuration());
            exercise.setCreatedAt(request.getCreatedAt());
            exercise.setStatus(request.getStatus());

            if (request.getCreatedById() != null) {
                contentCreatorRepository.findById(request.getCreatedById())
                        .ifPresent(exercise::setCreatedBy);
            } else {
                exercise.setCreatedBy(null);
            }

            ExerciseDTO created = exerciseService.createExerciseDTO(exercise);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating exercise: " + e.getMessage());
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateExercise(@PathVariable Integer id, @RequestBody ExerciseDTO request) {
        try {
            Exercise exercise = new Exercise();
            exercise.setId(id); // ID must be set for update
            exercise.setTitle(request.getTitle());
            exercise.setInstruction(request.getInstruction());
            exercise.setMediaUrl(request.getMediaUrl());
            exercise.setMediaType(request.getMediaType());
            exercise.setEstimatedDuration(request.getEstimatedDuration());
            exercise.setStatus(request.getStatus());
            exercise.setCreatedAt(request.getCreatedAt());

            if (request.getCreatedById() != null) {
                contentCreatorRepository.findById(request.getCreatedById())
                        .ifPresent(exercise::setCreatedBy);
            } else {
                exercise.setCreatedBy(null);
            }

            ExerciseDTO updated = exerciseService.updateExerciseDTO(id, exercise);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating exercise: " + e.getMessage());
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExercise(@PathVariable Integer id) {
        try {
            exerciseService.deleteExercise(id);
            return ResponseEntity.ok("Exercise deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Exercise not found");
        }
    }

    // GET exercises by creator
    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<List<ExerciseDTO>> getExercisesByCreator(@PathVariable Integer creatorId) {
        try {
            List<ExerciseDTO> exercises = exerciseService.getExercisesByCreatorId(creatorId);
            return ResponseEntity.ok(exercises);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
