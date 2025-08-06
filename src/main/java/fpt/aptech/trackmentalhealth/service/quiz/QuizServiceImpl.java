package fpt.aptech.trackmentalhealth.service.quiz;


import fpt.aptech.trackmentalhealth.entities.Question;
import fpt.aptech.trackmentalhealth.entities.Quiz;
import fpt.aptech.trackmentalhealth.entities.QuizQuestion;
import fpt.aptech.trackmentalhealth.entities.QuizQuestionId;
import fpt.aptech.trackmentalhealth.repository.quiz.QuestionRepository;
import fpt.aptech.trackmentalhealth.repository.quiz.QuizQuestionRepository;
import fpt.aptech.trackmentalhealth.repository.quiz.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizServiceImpl implements QuizService {
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Override
    public Page<Quiz> findAll(Pageable pageable) {
        return quizRepository.findAll(pageable);
    }

    @Override
    public Quiz createQuiz(Quiz quiz) {
        if(quiz.getQuizQuestions() != null) {
            quiz.getQuizQuestions().forEach(question -> question.setQuiz(quiz));
        } else {
            return null;
        }
        return quizRepository.save(quiz);
    }

    @Override
    public Quiz assignQuestionsToQuiz(Integer quizId, List<Integer> questionIds) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();

        for (Integer qId : questionIds) {
            Question question = questionRepository.findById(qId).orElseThrow();
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setId(new QuizQuestionId(quizId, qId));
            quizQuestion.setQuiz(quiz);
            quizQuestion.setQuestion(question);
            quizQuestionRepository.save(quizQuestion);
        }

        return quiz;
    }

    @Override
    public Page<Quiz> searchQuizzes(String keyword, Pageable pageable) {
        return quizRepository.searchQuiz(keyword, pageable);
    }
}
