package fpt.aptech.trackmentalhealth.service.quiz;


import fpt.aptech.trackmentalhealth.dto.quiz.QuizAttemptRequest;
import fpt.aptech.trackmentalhealth.dto.quiz.UserQuizAnswerItemDto;
import fpt.aptech.trackmentalhealth.dto.quiz.UserQuizSubmitRequest;
import fpt.aptech.trackmentalhealth.entities.*;
import fpt.aptech.trackmentalhealth.repository.quiz.*;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final UserQuizAttemptRepository attemptRepo;
    private final UserQuizAnswerItemRepository answerItemRepo;
    private final UserQuizAnswerItemOptionRepository optionRepo;
    private final UserRepository userRepo;
    private final QuizRepository quizRepo;
    private final QuestionRepository questionRepo;
    private final OptionRepository optionRepository;

    @Transactional
    public void submitQuiz(UserQuizSubmitRequest request) {
        Users user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Quiz quiz = quizRepo.findById(request.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        UserQuizAttempt attempt = new UserQuizAttempt();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setStartTime(LocalDateTime.now());
        attempt.setEndTime(LocalDateTime.now());

        int totalScore = 0;

        for (UserQuizAnswerItemDto dto : request.getAnswers()) {
            Question question = questionRepo.findById(dto.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            UserQuizAnswerItem answerItem = new UserQuizAnswerItem();
            answerItem.setAttempt(attempt);
            answerItem.setQuestion(question);
            answerItem.setUserInput(dto.getUserInput());

            UserQuizAnswerItemId itemId = new UserQuizAnswerItemId();
            itemId.setAttemptId(attempt.getId());
            itemId.setQuestionId(question.getId());
            answerItem.setId(itemId);

            List<UserQuizAnswerItemOption> selectedOptions = new ArrayList<>();
            int score = 0;

            for (Integer optionId : dto.getSelectedOptionIds()) {
                Option option = optionRepository.findById(Math.toIntExact(optionId))
                        .orElseThrow(() -> new RuntimeException("Option not found"));

                UserQuizAnswerItemOption optionEntity = new UserQuizAnswerItemOption();
                optionEntity.setAttempt(attempt);
                optionEntity.setQuestion(question);
                optionEntity.setOption(option);

                UserQuizAnswerItemOptionId optionIdEmb = new UserQuizAnswerItemOptionId(
                        attempt.getId(),
                        question.getId(),
                        option.getId()
                );
                optionEntity.setId(optionIdEmb);
                optionEntity.setAnswerItem(answerItem);

                selectedOptions.add(optionEntity);
                if (Boolean.TRUE.equals(option.getContent())) {
                    score += option.getScore();
                }
            }

            answerItem.setSelectedOptions(selectedOptions);
            answerItem.setScore(score);
            totalScore += score;

            attempt.getAnswerItems().add(answerItem);
        }

        attempt.setTotalScore(totalScore);
        attemptRepo.save(attempt); // Cascade sẽ lưu cả answerItems và options
    }


}

