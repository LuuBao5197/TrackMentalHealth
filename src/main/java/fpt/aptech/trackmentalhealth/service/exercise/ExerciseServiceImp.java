package fpt.aptech.trackmentalhealth.service.exercise;

import fpt.aptech.trackmentalhealth.dto.ExerciseDTO;
import fpt.aptech.trackmentalhealth.entities.Exercise;
import fpt.aptech.trackmentalhealth.repository.exercise.ExerciseRepository;
import fpt.aptech.trackmentalhealth.service.lesson.ContentModerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseServiceImp implements ExerciseService {
    ExerciseRepository exerciseRepository;



    @Autowired
    private ContentModerationService contentModerationService;

    public ExerciseServiceImp(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public List<ExerciseDTO> getExerciseDTOs() {
        List<Exercise> exercises = exerciseRepository.findAll();
        return exercises.stream().map(ExerciseDTO::new).toList();
    }

    @Override
    public ExerciseDTO getExerciseDTOById(Integer id) {
        Exercise exercise = exerciseRepository.findById(id).orElseThrow(() -> new RuntimeException("Exercise not found"));
        return new ExerciseDTO(exercise);
    }

    @Override
    public ExerciseDTO createExerciseDTO(Exercise exercise) {
        // Kiểm tra kết nối API
        contentModerationService.checkApiConnection();

        // Kiểm tra nội dung nhạy cảm
        if (exercise.getTitle() != null && contentModerationService.isSensitiveContent(exercise.getTitle())) {
            throw new RuntimeException("Exercise title contains sensitive content.");
        }


        Exercise savedExercise = exerciseRepository.save(exercise);
        return new ExerciseDTO(savedExercise);
    }

    @Override
    public ExerciseDTO updateExerciseDTO(Integer id, Exercise exercise) {
        // Kiểm tra kết nối API
        contentModerationService.checkApiConnection();

        // Kiểm tra nội dung nhạy cảm
        if (exercise.getTitle() != null && contentModerationService.isSensitiveContent(exercise.getTitle())) {
            throw new RuntimeException("Exercise title contains sensitive content.");
        }


        Exercise updatedExercise = exerciseRepository.save(exercise);
        return new ExerciseDTO(updatedExercise);
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

    @Override
    public List<ExerciseDTO> getExercisesByCreatorId(Integer creatorId) {
        List<Exercise> exercises = exerciseRepository.findByCreatedBy_Id(creatorId);
        return exercises.stream().map(ExerciseDTO::new).toList();
    }

}
