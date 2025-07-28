package fpt.aptech.trackmentalhealth.repository.lesson;

import fpt.aptech.trackmentalhealth.entities.UserLessonProgress;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.entities.Lesson;
import fpt.aptech.trackmentalhealth.entities.LessonStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserLessonProgressRepository extends JpaRepository<UserLessonProgress, Integer> {

    // Lấy tất cả tiến trình theo user
    List<UserLessonProgress> findByUser(Users user);

    // Lấy tiến trình theo user và lesson (nhiều step)
    List<UserLessonProgress> findByUserAndLessonOrderByStepCompleted_IdAsc(Users user, Lesson lesson);

    // Lấy tiến trình 1 step cụ thể
    Optional<UserLessonProgress> findByUserAndLessonAndStepCompleted(Users user, Lesson lesson, LessonStep stepCompleted);

    // Kiểm tra đã tồn tại tiến trình step này chưa
    boolean existsByUserAndLessonAndStepCompleted(Users user, Lesson lesson, LessonStep stepCompleted);
}
