package fpt.aptech.trackmentalhealth.service.lesson;

import fpt.aptech.trackmentalhealth.entities.*;
import fpt.aptech.trackmentalhealth.repository.lesson.UserLessonProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserLessonProgressService {

    @Autowired
    private UserLessonProgressRepository progressRepository;

    public UserLessonProgress addStepProgress(Users user, Lesson lesson, LessonStep stepCompleted) {
        // Kiểm tra nếu đã tồn tại step này thì không lưu nữa
        boolean exists = progressRepository.existsByUserAndLessonAndStepCompleted(user, lesson, stepCompleted);
        if (exists) {
            throw new RuntimeException("Step đã được đánh dấu hoàn thành rồi.");
        }

        UserLessonProgress progress = new UserLessonProgress();
        progress.setUser(user);
        progress.setLesson(lesson);
        progress.setStepCompleted(stepCompleted);
        progress.setCompletedAt(Instant.now());

        return progressRepository.save(progress);
    }


    public List<UserLessonProgress> getProgressByUser(Users user) {
        return progressRepository.findByUser(user);
    }

    public Optional<UserLessonProgress> getProgressByUserAndLesson(Users user, Lesson lesson) {
        // Trả về tiến trình của lesson đầu tiên hoặc throw nếu cần.
        List<UserLessonProgress> list = progressRepository.findByUserAndLessonOrderByStepCompleted_IdAsc(user, lesson);
        if (list.isEmpty()) return Optional.empty();
        return Optional.of(list.get(list.size() - 1)); // Trả về bước cuối cùng đã hoàn thành
    }

    public List<UserLessonProgress> getProgressStepsByUserAndLesson(Users user, Lesson lesson) {
        return progressRepository.findByUserAndLessonOrderByStepCompleted_IdAsc(user, lesson);
    }
}
