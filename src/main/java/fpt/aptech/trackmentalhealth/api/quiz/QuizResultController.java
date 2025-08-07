package fpt.aptech.trackmentalhealth.api.quiz;

import fpt.aptech.trackmentalhealth.entities.QuizResult;
import fpt.aptech.trackmentalhealth.entities.TestResult;
import fpt.aptech.trackmentalhealth.service.quiz.QuizResultService;
import fpt.aptech.trackmentalhealth.service.quiz.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-results")
public class QuizResultController {
    @Autowired
    private QuizResultService quizResultService;



    @PostMapping
    public ResponseEntity<QuizResult> create(@RequestBody  @Valid QuizResult result) {
        return ResponseEntity.ok(quizResultService.createResult(result));
    }
    @PostMapping("/multiQuizResult")
    public ResponseEntity<List<QuizResult>> createMultiTestResult(@RequestBody List<QuizResult> results) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizResultService.createMultiResult(results));
    }
}
