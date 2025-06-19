package fpt.aptech.trackmentalhealth.service.lesson;

import fpt.aptech.trackmentalhealth.dto.LessonDTO;
import fpt.aptech.trackmentalhealth.entities.*;

import java.util.List;

public interface LessonService {
    // Business logic cua Lesson
    List<LessonDTO> getLessonDTOs();
    LessonDTO getLessonDTOById(Integer id);
    LessonDTO createLessonDTO(Lesson lesson);
    LessonDTO updateLessonDTO(Integer id, Lesson lesson);
    void deleteLesson(Integer id);
    //Business logic cua LessonStep
    List<LessonStep> getLessonStepsByLessonId(Integer id);
    LessonStep getLessonStep(Integer id);
    LessonStep createLessonStep(LessonStep lessonStep);
    LessonStep updateLessonStep(Integer id, LessonStep lessonStep);
    void deleteLessonStep(Integer id);
}
