package fpt.aptech.trackmentalhealth.service.quiz;


import fpt.aptech.trackmentalhealth.entities.Question;
import fpt.aptech.trackmentalhealth.entities.Quiz;
import fpt.aptech.trackmentalhealth.entities.QuizQuestion;
import fpt.aptech.trackmentalhealth.entities.QuizQuestionId;
import fpt.aptech.trackmentalhealth.repository.quiz.QuestionRepository;
import fpt.aptech.trackmentalhealth.repository.quiz.QuizQuestionRepository;
import fpt.aptech.trackmentalhealth.repository.quiz.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Quiz createQuiz(Quiz quiz) {
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
}
