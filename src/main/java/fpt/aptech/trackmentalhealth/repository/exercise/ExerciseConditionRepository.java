package fpt.aptech.trackmentalhealth.repository.exercise;

import fpt.aptech.trackmentalhealth.entities.Exercise;
import fpt.aptech.trackmentalhealth.entities.ExerciseCondition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseConditionRepository extends JpaRepository<ExerciseCondition, Long> {
    List<ExerciseCondition> findByExercise(Exercise exercise);
}
