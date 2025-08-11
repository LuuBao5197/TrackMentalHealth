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
    private final QuizServiceImpl quizService;
    private final QuestionServiceImpl questionServiceImpl;

    @Transactional
    public UserQuizAttempt submitQuiz(SubmitQuizRequest request) {
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        UserQuizAttempt attempt = new UserQuizAttempt();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setStartTime(LocalDateTime.now());
        attempt.setTotalScore(0);
        attempt = attemptRepository.save(attempt);
        int idx = 0;
        int idx1 = 0;
        int totalScore = 0;
        Integer maxScore = 0;
        List<UserQuizAnswerItem> answerItems = new ArrayList<>();

        for (SubmitQuizRequest.QuestionAnswerDto answerDto : request.getAnswers()) {
            Question question = questionRepository.findById(answerDto.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            UserQuizAnswerItem answerItem = new UserQuizAnswerItem();
            answerItem.setId(new UserQuizAnswerItemId(attempt.getId(), question.getId()));
            answerItem.setAttempt(attempt);
            answerItem.setQuestion(question);
            maxScore += questionServiceImpl.getMaxScoreOfQuestion(question.getId());

            int questionScore = 0;

            switch (question.getType()) {
                case "TEXT_INPUT":
                    answerItem.setUserInput(answerDto.getUserInput());
                    // Tùy logic chấm điểm
                    if (answerDto.getUserInput() != null &&
                            answerDto.getUserInput().trim().equalsIgnoreCase(question.getOptions().getFirst().getContent().trim())) {
                        questionScore = question.getOptions().getFirst().getScore();
                    }
                    break;

                case "SINGLE_CHOICE":
                    UserQuizAnswerItemOption userQuizAnswerItemOption = new UserQuizAnswerItemOption();
                    userQuizAnswerItemOption.setQuestion(question);
                    userQuizAnswerItemOption.setAttempt(attempt);
                    Option optionSC = optionRepository.findById(answerDto.getSelectedOptionIds().getFirst())
                            .orElseThrow(() -> new RuntimeException("Option not found"));
                    userQuizAnswerItemOption.setOption(optionSC);
                    List<UserQuizAnswerItemOption>  userQuizAnswerItemOptions = new ArrayList<>();
                    userQuizAnswerItemOptions.add(userQuizAnswerItemOption);
                    answerItem.setSelectedOptions(userQuizAnswerItemOptions);

                    Integer correctOption = question.getOptions().stream()
                            .filter(Option::isCorrect) // lọc option có isCorrect = true
                            .findFirst() // lấy phần tử đầu tiên
                            .map(Option::getId) // lấy content
                            .orElse(null); // nếu không tìm thấy thì trả null
                    if(answerDto.getSelectedOptionIds().get(0).equals(correctOption)) {
                        questionScore = question.getOptions().getFirst().getScore();
                    }
                    break;


                case "MULTI_CHOICE":


                    UserQuizAttempt finalAttempt = attempt;
                    List<UserQuizAnswerItemOption> userQuizAnswerItemOptions1 = new ArrayList<>();
                    answerDto.getSelectedOptionIds().forEach(optionId -> {
                        UserQuizAnswerItemOption userQuizAnswerItemOption1 = new UserQuizAnswerItemOption();
                        Option optionMC = optionRepository.findById(optionId)
                                .orElseThrow(() -> new RuntimeException("Option not found"));
                        userQuizAnswerItemOption1.setQuestion(question);
                        userQuizAnswerItemOption1.setAttempt(finalAttempt);
                        userQuizAnswerItemOption1.setOption(optionMC);
                        userQuizAnswerItemOption1.setId(new UserQuizAnswerItemOptionId(
                                finalAttempt.getId(), question.getId(), optionMC.getId()
                        ));;
                        userQuizAnswerItemOptions1.add(userQuizAnswerItemOption1);
                    });
                    answerItem.setSelectedOptions(userQuizAnswerItemOptions1);
                    // Logic get mark
                    // get List option correst
                    List<Integer> correctOptions = new ArrayList<>();
                    for (Option option : question.getOptions()) {
                        if(option.isCorrect()) {
                            correctOptions.add(option.getId());
                        }
                    }
                    if(correctOptions.equals(answerDto.getSelectedOptionIds())){
                        questionScore = question.getScore();
                    }
                    break;
                case "SCORE_BASED":
                    List<UserQuizAnswerItemOption> selectedOptions = new ArrayList<>();
                    if (answerDto.getSelectedOptionIds() != null) {

                            Option option = optionRepository.findById(answerDto.getSelectedOptionIds().getFirst())
                                    .orElseThrow(() -> new RuntimeException("Option not found"));
                            UserQuizAnswerItemOption optionAnswer = new UserQuizAnswerItemOption();
                            optionAnswer.setId(new UserQuizAnswerItemOptionId(
                                    attempt.getId(), question.getId(), option.getId()
                            ));
                            optionAnswer.setAttempt(attempt);
                            optionAnswer.setQuestion(question);
                            optionAnswer.setOption(option);
                            optionAnswer.setAnswerItem(answerItem);
                            questionScore += option.getScore();

                            selectedOptions.add(optionAnswer);

                    }
                    answerItem.setSelectedOptions(selectedOptions);
                    break;

                case "MATCHING":
                    List<UserQuizAnswerItemMatching> matchingList = new ArrayList<>();
                    if (answerDto.getMatchingPairs() != null) {

                        for (SubmitQuizRequest.MatchingPairDto pair : answerDto.getMatchingPairs()) {
                            UserQuizAnswerItemMatching matching = new UserQuizAnswerItemMatching();
                            matching.setId(new UserQuizAnswerItemMatchingId(
                                    attempt.getId(),
                                    question.getId(),
                                    idx++
                            ));
                            matching.setAnswerItem(answerItem);
                            matching.setLeftText(pair.getLeftText());
                            matching.setRightText(pair.getRightText());
                            matchingList.add(matching);
                        }
                        boolean allCorrect = answerDto.getMatchingPairs().stream()
                                .allMatch(pair -> isMatchingPairCorrect(question, pair.getLeftText().trim(), pair.getRightText().trim()));
                        if(allCorrect){
                            questionScore = question.getScore();
                        }
                    }
                    answerItem.setMatchingAnswers(matchingList);
                    break;

                case "ORDERING":
                    List<UserQuizAnswerItemOrdering> orderingList = new ArrayList<>();
                    if (answerDto.getOrderingItems() != null) {
                        for (SubmitQuizRequest.OrderingItemDto itemDto : answerDto.getOrderingItems()) {
                            UserQuizAnswerItemOrdering ordering = new UserQuizAnswerItemOrdering();
                           UserQuizAnswerItemOrderingId id = new UserQuizAnswerItemOrderingId(attempt.getId(),question.getId(),idx1++);
                            ordering.setId(id);
                            ordering.setAnswerItem(answerItem);
                            ordering.setItemId(itemDto.getItemId());
                            ordering.setText(itemDto.getText());
                            ordering.setUserOrder(itemDto.getUserOrder());
                            orderingList.add(ordering);
                        }

                        // Chấm điểm nếu đúng thứ tự
                        boolean allCorrect = answerDto.getOrderingItems().stream()
                                .allMatch(pair -> isCorrectOrder(question, pair.getText(), pair.getUserOrder()));
                        if(allCorrect){
                            questionScore = question.getScore();
                        }
                    }
                    answerItem.setOrderingAnswers(orderingList);
                    break;
            }

            answerItem.setScore(questionScore);
            totalScore += questionScore;
            answerItems.add(answerItem);
        }

        attempt.setAnswerItems(answerItems);


        attempt.setTotalScore((int) ((double) totalScore / maxScore * 100));
        attempt.setEndTime(LocalDateTime.now());
        attempt.setResultLabel(calculateResultLabel((int) ((double) totalScore / maxScore * 100), quiz));

        return attemptRepository.save(attempt);
    }


    private String calculateResultLabel(int score, Quiz quiz) {
        return quiz.getQuizResults().stream()
                .filter(r -> score >= r.getMinScore() && score <= r.getMaxScore())
                .map(QuizResult::getResultLabel)
                .findFirst()
                .orElse("Không xác định");
    }

    private boolean isMatchingPairCorrect(Question question, String leftText, String rightText) {
        return question.getMatchingItems().stream()
                .anyMatch(item ->
                        item.getLeftItem().equalsIgnoreCase(leftText != null ? leftText.trim() : "") &&
                                item.getRightItem().equalsIgnoreCase(rightText != null ? rightText.trim() : "")
                );
    }
    private boolean isCorrectOrder(Question question, String content, Integer userOrder) {
        return question.getOrderingItems().stream().
                anyMatch(item -> item.getContent().equalsIgnoreCase(content)
                        && Objects.equals(item.getCorrectOrder(), userOrder));
    }

}
