package fpt.aptech.trackmentalhealth.service.quiz;

import fpt.aptech.trackmentalhealth.dto.quiz.SubmitQuizRequest;
import fpt.aptech.trackmentalhealth.entities.*;
import fpt.aptech.trackmentalhealth.repository.quiz.OptionRepository;
import fpt.aptech.trackmentalhealth.repository.quiz.QuestionRepository;
import fpt.aptech.trackmentalhealth.repository.quiz.QuizRepository;
import fpt.aptech.trackmentalhealth.repository.quiz.UserQuizAttemptRepository;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserQuizAttemptServiceImpl implements UserQuizAttemptService {

    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final UserQuizAttemptRepository attemptRepository;

    @Transactional
    public UserQuizAttempt submitQuiz(SubmitQuizRequest request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        UserQuizAttempt attempt = new UserQuizAttempt();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setStartTime(LocalDateTime.now()); // You can pass start time in request if needed
        attempt.setTotalScore(0);
        List<UserQuizAnswerItem> answerItems = new ArrayList<>();
        int totalScore = 0;
        attempt = attemptRepository.save(attempt);

        for (SubmitQuizRequest.QuestionAnswerDto answerDto : request.getAnswers()) {
            Question question = questionRepository.findById(answerDto.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            UserQuizAnswerItem answerItem = new UserQuizAnswerItem();
            UserQuizAnswerItemId id = new UserQuizAnswerItemId();
            id.setAttemptId(attempt.getId());
            id.setQuestionId(question.getId());
            answerItem.setId(id);
            answerItem.setAttempt(attempt);
            answerItem.setQuestion(question);
            answerItem.setUserInput(answerDto.getUserInput());

            List<UserQuizAnswerItemOption> selectedOptions = new ArrayList<>();
            int questionScore = 0;

            if (answerDto.getSelectedOptionIds() != null) {
                for (Integer optionId : answerDto.getSelectedOptionIds()) {
                    Option option = optionRepository.findById(optionId)
                            .orElseThrow(() -> new RuntimeException("Option not found"));

                    UserQuizAnswerItemOption optionAnswer = new UserQuizAnswerItemOption();
                    UserQuizAnswerItemOptionId optionIdEmb = new UserQuizAnswerItemOptionId(
                            attempt.getId(), question.getId(), option.getId()
                    );
                    optionAnswer.setId(optionIdEmb);
                    optionAnswer.setAttempt(attempt);
                    optionAnswer.setQuestion(question);
                    optionAnswer.setOption(option);
                    optionAnswer.setAnswerItem(answerItem);

                    if (option.isCorrect()) {
                        questionScore += option.getScore();
                    }

                    selectedOptions.add(optionAnswer);
                }
            }

            // Assign score and collected options
            answerItem.setScore(questionScore);
            answerItem.setSelectedOptions(selectedOptions);
            totalScore += questionScore;

            answerItems.add(answerItem);
        }

        attempt.setAnswerItems(answerItems);
        attempt.setTotalScore(totalScore);
        attempt.setEndTime(LocalDateTime.now());

        // Optional: set label based on score and quiz result scale
        attempt.setResultLabel(calculateResultLabel(totalScore, quiz));


        return attemptRepository.save(attempt);
    }

    private String calculateResultLabel(int score, Quiz quiz) {
        return quiz.getQuizResults().stream()
                .filter(r -> score >= r.getMinScore() && score <= r.getMaxScore())
                .map(QuizResult::getResultLabel)
                .findFirst()
                .orElse("Không xác định");
    }
}
