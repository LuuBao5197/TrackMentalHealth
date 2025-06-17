package fpt.aptech.trackmentalhealth.repository.lesson;

import fpt.aptech.trackmentalhealth.entities.LessonStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonStepRepository extends JpaRepository<LessonStep, Integer> {
    @Query(
            "select l from LessonStep l where l.lesson.id =:id"
    )
    List<LessonStep> getLessonStepsByLessonId(@Param("id") Integer id);
}
