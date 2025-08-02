package fpt.aptech.trackmentalhealth.api.quiz;

import fpt.aptech.trackmentalhealth.entities.Question;
import fpt.aptech.trackmentalhealth.service.quiz.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @PostMapping
    public ResponseEntity<Question> create(@RequestBody Question question) {
        return ResponseEntity.ok(questionService.createQuestion(question));
    }

    @GetMapping
    public ResponseEntity<List<Question>> getAll() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }
}
