package fpt.aptech.trackmentalhealth.service.exercise;

import fpt.aptech.trackmentalhealth.entities.Exercise;
import fpt.aptech.trackmentalhealth.entities.Lesson;
import fpt.aptech.trackmentalhealth.repository.exercise.ExerciseRepository;
import fpt.aptech.trackmentalhealth.repository.lesson.LessonRepository;
import fpt.aptech.trackmentalhealth.repository.lesson.LessonStepRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseServiceImp implements ExerciseService {
    ExerciseRepository exerciseRepository;

    public ExerciseServiceImp(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public List<Exercise> getExercises() {
        return exerciseRepository.findAll();
    }

    @Override
    public Exercise getExercise(Integer id) {
        return exerciseRepository.findById(id).orElseThrow(()->new RuntimeException("Exercise not found"));
    }

    @Override
    public Exercise createExercise(Exercise exercise) {
        exerciseRepository.save(exercise);
        return exercise;
    }

    @Override
    public Exercise updateExercise(Integer id, Exercise exercise) {
        exerciseRepository.save(exercise);
        return exercise;
    }

    @Override
    public void deleteExercise(Integer id) {
        Exercise exerciseDel = exerciseRepository.findById(id).orElseThrow(()->new RuntimeException("Exercise not found"));
        if(exerciseDel == null){
            throw new RuntimeException("Exercise not found");
        } else {
            exerciseRepository.delete(exerciseDel);
        }
    }
}
