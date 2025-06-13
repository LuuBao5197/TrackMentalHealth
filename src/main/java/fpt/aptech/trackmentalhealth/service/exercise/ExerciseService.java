package fpt.aptech.trackmentalhealth.service.exercise;

import fpt.aptech.trackmentalhealth.entities.Exercise;


import java.util.List;

public interface ExerciseService {
    // Business logic cua Exercise
    List<Exercise> getExercises();
    Exercise getExercise(Integer id);
    Exercise createExercise(Exercise exercise);
    Exercise updateExercise(Integer id, Exercise exercise);
    void deleteExercise(Integer id);
}
