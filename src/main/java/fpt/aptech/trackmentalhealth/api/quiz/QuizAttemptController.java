package fpt.aptech.trackmentalhealth.api.quiz;

import fpt.aptech.trackmentalhealth.dto.quiz.History.UserQuizAttemptDetailDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.History.UserQuizHistoryDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.SubmitQuizRequest;
import fpt.aptech.trackmentalhealth.entities.UserQuizAttempt;
import fpt.aptech.trackmentalhealth.service.quiz.UserQuizAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuizAttemptController {

    private final UserQuizAttemptService userQuizAttemptService;

    @PostMapping("/submit")
    public ResponseEntity<UserQuizHistoryDTO> submitQuiz(@RequestBody SubmitQuizRequest request) {
        UserQuizAttempt result = userQuizAttemptService.submitQuiz(request);
        UserQuizHistoryDTO dto = new UserQuizHistoryDTO();
        dto.setTotalScore(result.getTotalScore());
        dto.setResultLabel(result.getResultLabel());
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<UserQuizHistoryDTO>> getUserQuizHistory(@PathVariable Integer userId) {
        List<UserQuizHistoryDTO> history = userQuizAttemptService.getUserQuizHistory(userId);
        return ResponseEntity.ok(history);
    }

    // API lấy chi tiết 1 lần làm quiz
    @GetMapping("/detail/{attemptId}")
    public ResponseEntity<UserQuizAttemptDetailDTO> getAttemptDetail(@PathVariable Integer attemptId) {
        UserQuizAttemptDetailDTO detail = userQuizAttemptService.getAttemptDetail(attemptId);
        return ResponseEntity.ok(detail);
    }
}
