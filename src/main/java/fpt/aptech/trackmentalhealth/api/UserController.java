package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.dto.GetUserNameDTO;
import fpt.aptech.trackmentalhealth.dto.ProgressRequest;
import fpt.aptech.trackmentalhealth.entities.Lesson;
import fpt.aptech.trackmentalhealth.entities.LessonStep;
import fpt.aptech.trackmentalhealth.entities.UserLessonProgress;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.lesson.LessonRepository;
import fpt.aptech.trackmentalhealth.repository.lesson.LessonStepRepository;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import fpt.aptech.trackmentalhealth.service.lesson.UserLessonProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLessonProgressService progressService;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonStepRepository lessonStepRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        Optional<Users> user = userRepository.findById(id);
        return user.map(u -> {
                    GetUserNameDTO dto = new GetUserNameDTO(u.getUsername(), u.getFullname());
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/progress/update")
    public UserLessonProgress updateProgress(@RequestBody ProgressRequest request) {
        Optional<Users> userOpt = userRepository.findById(request.userId);
        Optional<Lesson> lessonOpt = lessonRepository.findById(request.lessonId);
        Optional<LessonStep> stepOpt = lessonStepRepository.findById(request.stepCompleted);

        if (userOpt.isPresent() && lessonOpt.isPresent() && stepOpt.isPresent()) {
            return progressService.addStepProgress(userOpt.get(), lessonOpt.get(), stepOpt.get());
        } else {
            throw new RuntimeException("User, Lesson or Step not found");
        }
    }

    @GetMapping("/{userId}/progress")
    public List<UserLessonProgress> getUserProgress(@PathVariable int userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return progressService.getProgressByUser(user);
    }

    @GetMapping("/{userId}/lesson/{lessonId}/progress")
    public List<UserLessonProgress> getProgressByLesson(@PathVariable int userId, @PathVariable int lessonId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        return progressService.getProgressStepsByUserAndLesson(user, lesson);
    }

    @GetMapping("/{userId}/lesson/{lessonId}/progress-percent")
    public double getProgressPercent(@PathVariable int userId, @PathVariable int lessonId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        int totalSteps = lesson.getLessonSteps().size();
        int completed = progressService.getProgressStepsByUserAndLesson(user, lesson).size();
        return totalSteps == 0 ? 0 : (completed * 100.0 / totalSteps);
    }
}
