package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.entities.Quiz;

import java.util.List;

public interface QuizService {
    Quiz createQuiz(Quiz quiz);
    Quiz assignQuestionsToQuiz(Integer quizId, List<Integer> questionIds);
}
