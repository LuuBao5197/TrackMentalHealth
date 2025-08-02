package fpt.aptech.trackmentalhealth.api.quiz;

import fpt.aptech.trackmentalhealth.entities.QuizResult;
import fpt.aptech.trackmentalhealth.service.quiz.QuizResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quiz-results")
public class QuizResultController {
    @Autowired
    private QuizResultService quizResultService;

    @PostMapping
    public ResponseEntity<QuizResult> create(@RequestBody QuizResult result) {
        return ResponseEntity.ok(quizResultService.createResult(result));
    }
}
