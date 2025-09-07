package fpt.aptech.trackmentalhealth.service.test;
import fpt.aptech.trackmentalhealth.configs.RedisCacheService;
import fpt.aptech.trackmentalhealth.dto.test.FullTestDTO;
import fpt.aptech.trackmentalhealth.dto.test.OptionDTO;
import fpt.aptech.trackmentalhealth.dto.test.QuestionDTO;
import fpt.aptech.trackmentalhealth.dto.test.TestAnswerRequest;
import fpt.aptech.trackmentalhealth.dto.test.history.UserTestHistoryDTO;
import fpt.aptech.trackmentalhealth.entities.*;
import fpt.aptech.trackmentalhealth.repository.test.*;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestServiceImp implements TestService {
    TestOptionRepository testOptionRepository;
    TestResultRepository testResultRepository;
    TestQuestionRepository testQuestionRepository;
    TestRepository testRepository;
    UserTestAttempRepository userTestAttempRepository;
    UserTestAnswerRepository userTestAnswerRepository;
    UserRepository userRepository;
    RedisCacheService redisCacheService;

    public TestServiceImp(TestOptionRepository testOptionRepository, TestResultRepository testResultRepository,
                          TestQuestionRepository testQuestionRepository, TestRepository testRepository,
                          UserTestAttempRepository userTestAttempRepository, UserTestAnswerRepository userTestAnswerRepository,
                          UserRepository userRepository, RedisCacheService redisCacheService) {
        this.testOptionRepository = testOptionRepository;
        this.testResultRepository = testResultRepository;
        this.testQuestionRepository = testQuestionRepository;
        this.testRepository = testRepository;
        this.userTestAttempRepository = userTestAttempRepository;
        this.userTestAnswerRepository = userTestAnswerRepository;
        this.userRepository = userRepository;
        this.redisCacheService =  redisCacheService;
    }



    @Override
//    @Cacheable(
//            value = "tests",
//            key = "'page=' + #pageable.pageNumber + ',size=' + #pageable.pageSize"
//    )
    public Page<FullTestDTO> getTests(Pageable pageable) {
       String cacheKey = "page=" + pageable.getPageNumber() + ",size=" + pageable.getPageSize();
       return redisCacheService.get("tests", cacheKey, ()-> {
           System.out.println(">>> Query DB getTests");
           Page<Test> testPage = testRepository.findAll(pageable);
           List<Test> testsList = testPage.getContent();
           List<FullTestDTO> fullTestDTOList = new ArrayList<>();
           for (Test test : testsList) {
               FullTestDTO fullTestDTO = new FullTestDTO();
               fullTestDTO.setId(Long.valueOf(test.getId()));
               fullTestDTO.setTitle(test.getTitle());
               fullTestDTO.setDescription(test.getDescription());
               fullTestDTO.setInstructions(test.getInstructions());
               fullTestDTO.setHasResult(!test.getResults().isEmpty());
               fullTestDTOList.add(fullTestDTO);
           }
           Page<FullTestDTO> fullTestDTOPage = new PageImpl<>(fullTestDTOList, testPage.getPageable(), testPage.getTotalElements());
           return fullTestDTOPage;
       });
    }

    @Override
//    @Cacheable(
//            value = "tests",
//            key = "'page=' + #pageable.pageNumber + ',size=' + #pageable.pageSize + ',search=' + #keyword"
//    )
    public Page<FullTestDTO> searchTests(String keyword, Pageable pageable) {
        String cacheKey = "page=" + pageable.getPageNumber() +
                ",size=" + pageable.getPageSize() +
                ",search=" + keyword;

        return redisCacheService.get("tests", cacheKey, () -> {
            Page<Test> testPage = testRepository.searchTestByName(keyword, pageable);
            List<Test> testsList = testPage.getContent();
            List<FullTestDTO> fullTestDTOList = new ArrayList<>();
            for (Test test : testsList) {
                FullTestDTO fullTestDTO = new FullTestDTO();
                fullTestDTO.setId(Long.valueOf(test.getId()));
                fullTestDTO.setTitle(test.getTitle());
                fullTestDTO.setDescription(test.getDescription());
                fullTestDTO.setInstructions(test.getInstructions());
                fullTestDTO.setHasResult(!test.getResults().isEmpty());
                fullTestDTOList.add(fullTestDTO);
            }
            return new PageImpl<>(fullTestDTOList, testPage.getPageable(), testPage.getTotalElements());
        });
    }

    @Override
//    @Cacheable(value = "tests", key = "#id")
    public Test getTest(Integer id) {
        String cacheKey = String.valueOf(id);
        return redisCacheService.get("tests", cacheKey, () -> {
            System.out.println(">>> Query DB getTestById with id=" + id);
            return testRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Test not found"));
        });
    }

    @Override
    public Test createTest(Test test) {
        testRepository.save(test);
        return test;
    }

    @Override
//    @CachePut(value = "tests", key = "id")
    public Test updateTest(Integer id, Test test) {
        // Lưu vào DB trước
        Test updatedTest = testRepository.save(test);

        // Cập nhật cache
        String cacheKey = String.valueOf(id);
        redisCacheService.put("tests::" + cacheKey, updatedTest);

        return updatedTest;
    }


    @Override
//    @CacheEvict(value = "tests", key = "#id")
    public void deleteTest(Integer id) {
        // Lấy test từ DB trước
        Test testDel = testRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        // Xóa khỏi DB
        testRepository.delete(testDel);

        // Xóa khỏi cache
        String cacheKey = String.valueOf(id);
        redisCacheService.evict("tests::" + cacheKey);
    }

    @Override
    public List<TestQuestion> getTestQuestionsByTestId(Integer id) {
        return testQuestionRepository.getTestQuestionsByTestId(id);
    }

    @Override
    public TestQuestion getTestQuestion(Integer id) {
        return testQuestionRepository.findById(id).orElseThrow(()->new RuntimeException("Test Question not found"));
    }

    @Override
    public TestQuestion createTestQuestion(TestQuestion testQuestion) {
        testQuestionRepository.save(testQuestion);
        return testQuestion;
    }

    @Override
    public TestQuestion updateTestQuestion(Integer id, TestQuestion testQuestion) {
        testQuestionRepository.save(testQuestion);
        return testQuestion;
    }

    @Override
    public void deleteTestQuestion(Integer id) {
        TestQuestion testQuestionDel = testQuestionRepository.findById(id).orElseThrow(()->new RuntimeException("TestQuestion not found"));
        testQuestionRepository.delete(testQuestionDel);
    }

    @Override
    public List<TestOption> getTestOptionsByTestQuestionId(Integer id) {
        return testOptionRepository.getTestOptionsByTestQuestionId(id);
    }

    @Override
    public TestOption getTestOption(Integer id) {
        return testOptionRepository.findById(id).orElseThrow(()->new RuntimeException("TestOption not found"));
    }

    @Override
    public TestOption createTestOption(TestOption testOption) {
        testOptionRepository.save(testOption);
        return testOption;
    }

    @Override
    public TestOption updateTestOption(Integer id, TestOption testOption) {
        testOptionRepository.save(testOption);
        return testOption;
    }

    @Override
    public void deleteTestOption(Integer id) {
        testOptionRepository.deleteById(id);
    }

    @Override
    public List<TestResult> getTestResultsByTestId(Integer id) {
        return testResultRepository.getTestResultsByTestId(id);
    }

    @Override
    public TestResult getTestResult(Integer id) {
        return testResultRepository.findById(id).orElseThrow(()->new RuntimeException("TestResult not found"));
    }

    @Override
    public TestResult createTestResult(TestResult testResult) {
        testResultRepository.save(testResult);
        return testResult;
    }

    @Override
    public TestResult updateTestResult(Integer id, TestResult testResult) {
        testResultRepository.save(testResult);
        return testResult;
    }

    @Override
    public void deleteTestResult(Integer id) {
        testResultRepository.deleteById(id);
    }

    @Transactional
    public void createFullTest(FullTestDTO dto) {
        Test test = new Test();
        if(dto.getTitle() != null){
            test.setTitle(dto.getTitle());
        }
        test.setDescription(dto.getDescription());
        test.setInstructions(dto.getInstructions());
        test = testRepository.save(test);

        for (QuestionDTO qDto : dto.getQuestions()) {
            TestQuestion question = new TestQuestion();
            question.setId(qDto.getId());
            question.setTest(test);
            question.setQuestionText(qDto.getQuestionText());
            question.setQuestionType(qDto.getQuestionType());
            question.setQuestionOrder(qDto.getQuestionOrder());
            question = testQuestionRepository.save(question);

            for (OptionDTO oDto : qDto.getOptions()) {
                TestOption option = new TestOption();
                option.setId(oDto.getId());
                option.setQuestion(question);
                option.setOptionText(oDto.getOptionText());
                option.setScoreValue(oDto.getScoreValue());
                option.setOptionOrder(oDto.getOptionOrder());
                testOptionRepository.save(option);
            }
        }
//        if (dto.getResults() != null) {
//            for (TestResultDto rDto : dto.getResults()) {
//                TestResult result = new TestResult();
//                result.setTest(test);
//                result.setUserId(rDto.getUserId());
//                result.setTotalScore(rDto.getTotalScore());
//                result.setResultText(rDto.getResultText());
//                testResultRepo.save(result);
//            }
//        }
    }
    @Transactional
    @Override
    public void updateFullTest(FullTestDTO dto) {
        // 1. Tìm test cần cập nhật
        Test test = testRepository.findById(dto.getId().intValue())
                .orElseThrow(() -> new EntityNotFoundException("Test not found"));

        // 2. Cập nhật thông tin test
        test.setTitle(dto.getTitle());
        test.setDescription(dto.getDescription());
        test.setInstructions(dto.getInstructions());
        test = testRepository.save(test);

        // 3. Lấy danh sách câu hỏi hiện tại
        List<TestQuestion> existingQuestions = getTestQuestionsByTestId(test.getId());
        Map<Long, TestQuestion> existingQuestionMap = existingQuestions.stream()
                .collect(Collectors.toMap(q -> q.getId().longValue(), q -> q));

        Set<Long> incomingQuestionIds = new HashSet<>();

        for (QuestionDTO qDto : dto.getQuestions()) {
            TestQuestion question;
            if (qDto.getId() != null && existingQuestionMap.containsKey(qDto.getId())) {
                question = existingQuestionMap.get(qDto.getId());
            } else {
                question = new TestQuestion();
                question.setTest(test);
            }

            question.setQuestionText(qDto.getQuestionText());
            question.setQuestionType(qDto.getQuestionType());
            question.setQuestionOrder(qDto.getQuestionOrder());
            question = testQuestionRepository.save(question);

            incomingQuestionIds.add(question.getId().longValue());

            // Đáp án hiện tại
            List<TestOption> existingOptions = getTestOptionsByTestQuestionId(question.getId());
            Map<Long, TestOption> existingOptionMap = existingOptions.stream()
                    .filter(o -> o.getId() != null)
                    .collect(Collectors.toMap(o -> o.getId().longValue(), o -> o));

            Set<Long> incomingOptionIds = new HashSet<>();
            for (OptionDTO oDto : qDto.getOptions()) {
                TestOption option;
                if (oDto.getId() != null && existingOptionMap.containsKey(oDto.getId())) {
                    option = existingOptionMap.get(oDto.getId());
                } else {
                    option = new TestOption();
                    option.setQuestion(question);
                }

                option.setOptionText(oDto.getOptionText());
                option.setScoreValue(oDto.getScoreValue());
                option.setOptionOrder(oDto.getOptionOrder());
                option = testOptionRepository.save(option);

                incomingOptionIds.add(option.getId().longValue());
            }

            // Xóa đáp án bị loại bỏ
            for (TestOption option : existingOptions) {
                if (!incomingOptionIds.contains(option.getId().longValue())) {
                    testOptionRepository.delete(option);
                }
            }
        }

        // 6. Xóa câu hỏi bị loại bỏ
        for (TestQuestion question : existingQuestions) {
            if (!incomingQuestionIds.contains(question.getId().longValue())) {
                testOptionRepository.deleteAll(getTestOptionsByTestQuestionId(question.getId()));
                testQuestionRepository.delete(question);
            }
        }
    }



    @Override
    public Test checkDuplicateTest(String title) {
        return testRepository.findByTitleIgnoreCase(title);
    }



    @Override
    public Integer getMaxMarkOfTest(Integer id) {
        Test test = getTest(id);
        if (test.getQuestions() == null) return 0;

        return test.getQuestions().stream()
                .mapToInt(q -> q.getOptions().stream()
                        .mapToInt(opt -> opt.getScoreValue() != null ? opt.getScoreValue() : 0)
                        .max()
                        .orElse(0)
                )
                .sum();
    }

    @Override
    public Integer getMaxMarkOfTestByCategory(Integer id, String category) {
        Test test = getTest(id);
        if (test.getQuestions() == null) return 0;
        return test.getQuestions().stream().filter(q->q.getQuestionType().equalsIgnoreCase(category))
                .mapToInt(q -> q.getOptions().stream()
                        .mapToInt(opt -> opt.getScoreValue() != null ? opt.getScoreValue() : 0)
                        .max()
                        .orElse(0)
                )
                .sum();
    }

    @Override
    public List<TestResult> createMultipleTestResults(List<TestResult> testResults) {
        testResultRepository.saveAll(testResults);
        return testResults;
    }

    @Override
    public UserTestAttempt getUserTestAttempt(Integer userId, Integer testId) {
        return userTestAttempRepository.findUserTestAttemptByTestIdAndUserId(testId, userId);
    }

    @Override
    public UserTestAttempt saveUserTestAttempt(UserTestAttempt userTestAttempt) {
        userTestAttempRepository.save(userTestAttempt);
        return userTestAttempt;
    }

    @Override
    public UserTestAnswer getUserTestAnswer(Integer userId, Integer testId) {
        return userTestAnswerRepository.findUserTestAnswerByQuestionIdAndUserId(testId, userId);
    }

    @Override
    public List<UserTestAnswer> saveUserTestAnswer(List<UserTestAnswer> userTestAnswers) {
         userTestAnswerRepository.saveAll(userTestAnswers);
         return userTestAnswers;
    }

    @Override
    public List<UserTestAttempt> getHistoryAttempts(Integer userId) {
        return userTestAttempRepository.findByUserId(userId);
    }

    @Override
    public Set<String> getCategoriesOfTest(Integer testId) {
        Test test = getTest(testId);
        if (test.getQuestions() == null) return null;
        Set<String> categories = new HashSet<>();
        List<TestQuestion> testQuestions = test.getQuestions();
        for (TestQuestion question : testQuestions) {
            categories.add(question.getQuestionType());
        }
        return categories;
    }
    public TestResult getResultByDomainAndScore(String domainName, Integer score, Test test) {
        return testResultRepository.findByTestAndCategoryAndScore(test.getId(), domainName, score)
                .orElseThrow(() -> new RuntimeException(
                        "No result found for domain: " + domainName + " with score: " + score));
    }
    @Transactional
    public String submitTestResult(@RequestBody TestAnswerRequest request) {
        // 1. Tìm người dùng và bài test
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Test test = testRepository.findById(request.getTestId())
                .orElseThrow(() -> new RuntimeException("Test not found"));

        StringBuilder labelResult = new StringBuilder("Result of test: " + test.getTitle() + ": ");

        // 2. Tạo UserTestAttempt
        UserTestAttempt attempt = new UserTestAttempt();
        attempt.setUsers(user);
        attempt.setTest(test);
        attempt.setStartedAt(Instant.now());
        attempt.setCompletedAt(Instant.now());
//        attempt.setResultSummary(request.getResult());
        attempt.setTotalScore(0);

        // Lưu để có attemptId
        attempt = saveUserTestAttempt(attempt);

        // 3. Tính điểm theo domain
        Map<String, Integer> domainScores = new HashMap<>();
        int totalScore = 0;

        for (TestAnswerRequest.AnswerDetail ans : request.getAnswers()) {
            TestQuestion question = testQuestionRepository.findById(ans.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Invalid question ID"));

            TestOption selected = testOptionRepository.findById(ans.getSelectedOptionId())
                    .orElseThrow(() -> new RuntimeException("Invalid option ID"));

            int score = selected.getScoreValue();
            totalScore += score;

            domainScores.merge(question.getQuestionType(), score, Integer::sum);
        }
        attempt.setTotalScore(totalScore);

        // 4. Lưu domain results
        List<UserTestDomainResult> domainResults = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : domainScores.entrySet()) {
            String domain = entry.getKey();
            Integer score = entry.getValue();

            UserTestDomainResult domainResult = new UserTestDomainResult();
            domainResult.setAttempt(attempt);
            domainResult.setDomainName(domain);
            domainResult.setScore(score);

            // phân loại kết quả
            TestResult matchedResult = getResultByDomainAndScore(domain, score, test);
            domainResult.setResultText(matchedResult.getResultText());

            labelResult.append(" ").append(matchedResult.getResultText()).append(",");
            domainResults.add(domainResult);
        }
        attempt.getDomainResults().clear();
        attempt.getDomainResults().addAll(domainResults);

        // 5. Lưu từng UserTestAnswer (mapping mới)
        for (TestAnswerRequest.AnswerDetail ans : request.getAnswers()) {
            UserTestAnswer answer = new UserTestAnswer();

            UserTestAnswerId id = new UserTestAnswerId();
            id.setAttemptId(attempt.getId());
            id.setQuestionId(ans.getQuestionId());
            id.setSelectedOptionId(ans.getSelectedOptionId());
            answer.setId(id);

            // set quan hệ theo @MapsId
            answer.setAttempt(attempt);
            answer.setQuestion(testQuestionRepository.findById(ans.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Invalid question ID")));
            answer.setSelectedOption(testOptionRepository.findById(ans.getSelectedOptionId())
                    .orElseThrow(() -> new RuntimeException("Invalid option ID")));

            userTestAnswerRepository.save(answer);
        }
        attempt.setResultSummary(labelResult.toString());
        userTestAttempRepository.save(attempt);
        return labelResult.toString();
    }

}
