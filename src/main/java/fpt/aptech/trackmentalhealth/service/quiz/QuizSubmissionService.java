package fpt.aptech.trackmentalhealth.service.quiz;


import fpt.aptech.trackmentalhealth.dto.quiz.AnswerSubmissionDTO;
import fpt.aptech.trackmentalhealth.dto.quiz.QuizSubmissionDTO;
import fpt.aptech.trackmentalhealth.entities.*;
import fpt.aptech.trackmentalhealth.repository.quiz.*;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QuizSubmissionService {

    private final UserQuizAttemptRepository attemptRepo;
    private final UserQuizAnswerItemRepository answerItemRepo;
    private final UserQuizAnswerItemOptionRepository optionRepo;
    private final QuestionRepository questionRepo;
    private final OptionRepository optionRepository;
    private final UserRepository userRepo;
    private final QuizRepository quizRepo;

    @Transactional
    public void submitQuiz(QuizSubmissionDTO dto) {
        UserQuizAttempt attempt = new UserQuizAttempt();
        attempt.setUser(userRepo.findById(dto.getUserId()).orElseThrow());
        attempt.setQuiz(quizRepo.findById(dto.getQuizId()).orElseThrow());
        attempt.setStartTime(LocalDateTime.now());
        attemptRepo.save(attempt);

        for (AnswerSubmissionDTO ans : dto.getAnswers()) {
            Question question = questionRepo.findById(Math.toIntExact(ans.getQuestionId())).orElseThrow();

            UserQuizAnswerItem item = new UserQuizAnswerItem();
            item.setId(new UserQuizAnswerItemId(Math.toIntExact(attempt.getId()), question.getId()));
            item.setAttempt(attempt);
            item.setQuestion(question);
            item.setUserInput(ans.getUserInput());

            // Handle multi-options
            for (Integer optId : ans.getSelectedOptionIds()) {
                Option option = optionRepository.findById(Math.toIntExact(optId)).orElseThrow();

                UserQuizAnswerItemOption optionLink = new UserQuizAnswerItemOption();
                optionLink.setId(new UserQuizAnswerItemOptionId(Math.toIntExact(attempt.getId()), question.getId(), Math.toIntExact(optId)));
                optionLink.setAttempt(attempt);
                optionLink.setQuestion(question);
                optionLink.setOption(option);
                optionLink.setAnswerItem(item);

                item.getSelectedOptions().add(optionLink);
            }

            answerItemRepo.save(item);
        }
    }
}
