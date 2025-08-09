package fpt.aptech.trackmentalhealth.service.exercise;

import fpt.aptech.trackmentalhealth.dto.ExerciseDTO;
import fpt.aptech.trackmentalhealth.entities.Exercise;


import java.util.List;

public interface ExerciseService {

    // Business logic cua Lesson
    List<ExerciseDTO> getExerciseDTOs();
    ExerciseDTO getExerciseDTOById(Integer id);
    ExerciseDTO createExerciseDTO(Exercise exercise);
    ExerciseDTO updateExerciseDTO(Integer id, Exercise exercise);
    void deleteExercise(Integer id);
    List<ExerciseDTO> getExercisesByCreatorId(Integer creatorId);
    void approveExercise(Integer id);

}
