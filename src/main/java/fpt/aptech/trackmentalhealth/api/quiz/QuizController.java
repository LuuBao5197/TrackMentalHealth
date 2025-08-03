package fpt.aptech.trackmentalhealth.api.quiz;

import fpt.aptech.trackmentalhealth.entities.Quiz;
import fpt.aptech.trackmentalhealth.service.quiz.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    @Autowired
    private QuizService quizService;

    @PostMapping
    public ResponseEntity<Quiz> create(@RequestBody Quiz quiz) {
        return ResponseEntity.ok(quizService.createQuiz(quiz));
    }

    @PostMapping("/{quizId}/questions")
    public ResponseEntity<Quiz> assignQuestions(
            @PathVariable Integer quizId,
            @RequestBody List<Integer> questionIds) {
        return ResponseEntity.ok(quizService.assignQuestionsToQuiz(quizId, questionIds));
    }
}
