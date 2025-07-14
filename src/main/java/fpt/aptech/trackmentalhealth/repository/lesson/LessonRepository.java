package fpt.aptech.trackmentalhealth.repository.lesson;

import fpt.aptech.trackmentalhealth.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findByCreatedBy_Id(Integer creatorId);

}
