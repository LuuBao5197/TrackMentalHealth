package fpt.aptech.trackmentalhealth.repository.exercise;

import fpt.aptech.trackmentalhealth.entities.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Integer> {
    List<Exercise> findByCreatedBy_Id(Integer creatorId);
}
