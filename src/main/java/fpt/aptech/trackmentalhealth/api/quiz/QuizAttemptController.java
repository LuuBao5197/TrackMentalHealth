package fpt.aptech.trackmentalhealth.api.quiz;

import fpt.aptech.trackmentalhealth.dto.quiz.SubmitQuizRequest;
import fpt.aptech.trackmentalhealth.entities.UserQuizAttempt;
import fpt.aptech.trackmentalhealth.service.quiz.UserQuizAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizAttemptController {

    private final UserQuizAttemptService userQuizAttemptService;

    @PostMapping("/submit")
    public ResponseEntity<UserQuizAttempt> submitQuiz(@RequestBody SubmitQuizRequest request) {
        UserQuizAttempt result = userQuizAttemptService.submitQuiz(request);
        return ResponseEntity.ok(result);
    }
}
