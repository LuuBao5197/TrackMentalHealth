package fpt.aptech.trackmentalhealth.repository.lesson;

import fpt.aptech.trackmentalhealth.entities.LessonStep;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonStepRepository extends JpaRepository<LessonStep, Integer> {
    @Transactional
    void deleteByLessonId(Integer lessonId);
}
