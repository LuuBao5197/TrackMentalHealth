package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.dto.quiz.SubmitQuizRequest;
import fpt.aptech.trackmentalhealth.entities.UserQuizAttempt;

public interface UserQuizAttemptService {
    public UserQuizAttempt submitQuiz(SubmitQuizRequest request);
}
