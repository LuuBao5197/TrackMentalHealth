package fpt.aptech.trackmentalhealth.api.quiz;

import fpt.aptech.trackmentalhealth.dto.quiz.QuizCreateDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.QuizDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.QuizDetailDTO;
import fpt.aptech.trackmentalhealth.entities.Question;
import fpt.aptech.trackmentalhealth.entities.Quiz;
import fpt.aptech.trackmentalhealth.entities.QuizQuestion;
import fpt.aptech.trackmentalhealth.repository.quiz.QuizRepository;
import fpt.aptech.trackmentalhealth.service.quiz.QuestionService;
import fpt.aptech.trackmentalhealth.service.quiz.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    @Autowired
    private QuizService quizService;
    @Autowired
    private QuestionService questionService;
    @GetMapping
    public ResponseEntity<Map<String,Object>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<QuizDTO> quizzes;
        if (search != null && !search.isEmpty()) {
            quizzes = quizService.searchQuizzes(search, pageable);
        } else {
            quizzes = quizService.findAll(pageable);
        }
        Map<String, Object> response = new HashMap<>();

        response.put("data", quizzes.getContent());
        response.put("total", quizzes.getTotalElements());
        response.put("currentPage", quizzes.getNumber() + 1); // Page index starts at 0
        response.put("totalPages", quizzes.getTotalPages());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<QuizDetailDTO> findOne(@PathVariable Integer id) {
        QuizDetailDTO quiz = quizService.findOne(id);
        if (quiz == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Quiz> create(@RequestBody QuizCreateDTO dto) {
        Quiz quiz = new Quiz();
        quiz.setTitle(dto.getTitle());
        quiz.setDescription(dto.getDescription());
        quiz.setTimeLimit(dto.getTimeLimit());
        quiz.setNumberOfQuestions(dto.getNumberOfQuestions());

        List<QuizQuestion> quizQuestions = new ArrayList<>();
        for (Integer questionId : dto.getQuestionIds()) {
            Question question = questionService.getQuestionById(questionId);
            QuizQuestion qq = new QuizQuestion();
            qq.setQuiz(quiz);
            qq.setQuestion(question);
            quizQuestions.add(qq);
        }
        quiz.setQuizQuestions(quizQuestions);
        quiz.setTotalScore(
                quizQuestions.stream()
                        .mapToInt(qq -> qq.getQuestion().getScore() != null ? qq.getQuestion().getScore() : 0)
                        .sum()
        );
        Quiz savedQuiz = quizService.createQuiz(quiz);
        return ResponseEntity.ok(savedQuiz);
    }



    @PostMapping("/{quizId}/questions")
    public ResponseEntity<Quiz> assignQuestions(
            @PathVariable Integer quizId,
            @RequestBody List<Integer> questionIds) {
        return ResponseEntity.ok(quizService.assignQuestionsToQuiz(quizId, questionIds));
    }
}
