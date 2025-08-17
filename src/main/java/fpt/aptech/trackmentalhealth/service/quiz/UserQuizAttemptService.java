package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.dto.quiz.History.UserQuizAttemptDetailDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.History.UserQuizHistoryDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.SubmitQuizRequest;
import fpt.aptech.trackmentalhealth.entities.UserQuizAttempt;

import java.util.List;

public interface UserQuizAttemptService {
    public UserQuizAttempt submitQuiz(SubmitQuizRequest request);

    public List<UserQuizHistoryDTO> getUserQuizHistory(Integer userId);
    public UserQuizAttemptDetailDTO getAttemptDetail(Integer attemptId);
}
