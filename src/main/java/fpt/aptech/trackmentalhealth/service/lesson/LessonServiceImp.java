package fpt.aptech.trackmentalhealth.service.lesson;

import fpt.aptech.trackmentalhealth.dto.LessonDTO;
import fpt.aptech.trackmentalhealth.entities.Lesson;
import fpt.aptech.trackmentalhealth.entities.LessonStep;
import fpt.aptech.trackmentalhealth.repository.lesson.LessonRepository;
import fpt.aptech.trackmentalhealth.repository.lesson.LessonStepRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class LessonServiceImp implements LessonService {
    LessonStepRepository lessonStepRepository;
    LessonRepository lessonRepository;

    public LessonServiceImp(LessonStepRepository lessonStepRepository, LessonRepository lessonRepository) {
        this.lessonStepRepository = lessonStepRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public List<LessonDTO> getLessonDTOs() {
        List<Lesson> lessons = lessonRepository.findAll();
        return lessons.stream().map(LessonDTO::new).toList();
    }

    @Override
    public Lesson getLesson(Integer id) {
        return lessonRepository.findById(id).orElseThrow(()->new RuntimeException("Lesson not found"));
    }

    @Override
    public Lesson createLesson(Lesson lesson) {
        lessonRepository.save(lesson);
        return lesson;
    }

    @Override
    public Lesson updateLesson(Integer id, Lesson lesson) {
        lessonRepository.save(lesson);
        return lesson;
    }

    @Override
    public void deleteLesson(Integer id) {
        Lesson lessonDel = lessonRepository.findById(id).orElseThrow(()->new RuntimeException("Lesson not found"));
        if(lessonDel == null){
            throw new RuntimeException("Lesson not found");
        } else {
            lessonRepository.delete(lessonDel);
        }
    }

    @Override
    public List<LessonStep> getLessonStepsByLessonId(Integer id) {
        return lessonStepRepository.getLessonStepsByLessonId(id);
    }

    @Override
    public LessonStep getLessonStep(Integer id) {
        return lessonStepRepository.findById(id).orElseThrow(()->new RuntimeException("Lesson Step not found"));
    }

    @Override
    public LessonStep createLessonStep(LessonStep lessonStep) {
        lessonStepRepository.save(lessonStep);
        return lessonStep;
    }

    @Override
    public LessonStep updateLessonStep(Integer id, LessonStep lessonStep) {
        lessonStepRepository.save(lessonStep);
        return lessonStep;
    }

    @Override
    public void deleteLessonStep(Integer id) {
        LessonStep lessonStepDel = lessonStepRepository.findById(id).orElseThrow(()->new RuntimeException("Lesson Step not found"));
        lessonStepRepository.delete(lessonStepDel);
    }
}
