package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.entities.Question;
import fpt.aptech.trackmentalhealth.entities.Quiz;
import fpt.aptech.trackmentalhealth.entities.QuizQuestion;
import fpt.aptech.trackmentalhealth.repository.quiz.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizServiceImp implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Override
    public Page<Quiz> getQuizzes(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    @Override
    public Quiz getQuizzesByQuizId(Integer quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
    }

    @Override
    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz updateQuiz(Integer quizId, Quiz quiz) {
        Quiz existing = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        existing.setTitle(quiz.getTitle());
        existing.setStatus(quiz.getStatus());
        existing.setDescription(quiz.getDescription());

        // thêm các trường khác nếu có
        return quizRepository.save(existing);
    }

    @Override
    public void deleteQuiz(Integer quizId) {
        quizRepository.deleteById(quizId);
    }

    @Override
    public Quiz sendQuizForApproval(Quiz quiz) {
        quiz.setStatus("PENDING_APPROVAL"); // Ví dụ cờ trạng thái
        return quizRepository.save(quiz);
    }

    @Override
    public Page<Question> getQuestions(Pageable pageable, String category) {
        if (category != null && !category.isEmpty()) {
            return questionRepository.findByCategory(category, pageable);
        }
        return questionRepository.findAll(pageable);
    }

    @Override
    public Question getQuestion(Integer questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
    }

    @Override
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public Question updateQuestion(Integer questionId, Question question) {
        Question existing = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        existing.setContent(question.getContent());
        existing.setCategory(question.getCategory());
        return questionRepository.save(existing);
    }

    @Override
    public void deleteQuestion(Integer questionId) {
        questionRepository.deleteById(questionId);
    }

    @Override
    public List<QuizQuestion> getQuizQuestionsByQuizId(Integer quizId) {
        return quizQuestionRepository.findByQuizId(quizId);
    }

    @Override
    public List<QuizQuestion> createMultipleQuizQuestions(List<QuizQuestion> quizQuestions) {
        return quizQuestionRepository.saveAll(quizQuestions);
    }

    @Override
    public List<QuizQuestion> updateMultipleQuizQuestions(List<QuizQuestion> quizQuestions) {
        return quizQuestionRepository.saveAll(quizQuestions);
    }
}
