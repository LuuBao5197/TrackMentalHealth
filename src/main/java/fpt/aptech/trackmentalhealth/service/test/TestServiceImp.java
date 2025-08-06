package fpt.aptech.trackmentalhealth.service.test;
import fpt.aptech.trackmentalhealth.dto.test.FullTestDTO;
import fpt.aptech.trackmentalhealth.dto.test.OptionDTO;
import fpt.aptech.trackmentalhealth.dto.test.QuestionDTO;
import fpt.aptech.trackmentalhealth.entities.*;
import fpt.aptech.trackmentalhealth.repository.test.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public TestServiceImp(TestOptionRepository testOptionRepository, TestResultRepository testResultRepository,
                          TestQuestionRepository testQuestionRepository, TestRepository testRepository,
                          UserTestAttempRepository userTestAttempRepository, UserTestAnswerRepository userTestAnswerRepository) {
        this.testOptionRepository = testOptionRepository;
        this.testResultRepository = testResultRepository;
        this.testQuestionRepository = testQuestionRepository;
        this.testRepository = testRepository;
        this.userTestAttempRepository = userTestAttempRepository;
        this.userTestAnswerRepository = userTestAnswerRepository;
    }

    @Override
    public Page<FullTestDTO> getTests(Pageable pageable) {
        Page<Test> testPage = testRepository.findAll(pageable);
        List<Test> testsList = testPage.getContent();
        List<FullTestDTO> fullTestDTOList = new ArrayList<>();
        for (Test test : testsList) {
            FullTestDTO fullTestDTO = new FullTestDTO();
            fullTestDTO.setId(Long.valueOf(test.getId()));
            fullTestDTO.setTitle(test.getTitle());
            fullTestDTO.setDescription(test.getDescription());
            fullTestDTO.setInstructions(test.getInstructions());
            fullTestDTOList.add(fullTestDTO);
        }
        Page<FullTestDTO> fullTestDTOPage = new PageImpl<>(fullTestDTOList);
        return fullTestDTOPage;
    }

    @Override
    public Page<FullTestDTO> searchTests(String keyword, Pageable pageable) {
        Page<Test> testPage = testRepository.searchTestByName(keyword, pageable);
        List<Test> testsList = testPage.getContent();
        List<FullTestDTO> fullTestDTOList = new ArrayList<>();
        for (Test test : testsList) {
            FullTestDTO fullTestDTO = new FullTestDTO();
            fullTestDTO.setId(Long.valueOf(test.getId()));
            fullTestDTO.setTitle(test.getTitle());
            fullTestDTO.setDescription(test.getDescription());
            fullTestDTO.setInstructions(test.getInstructions());
            fullTestDTOList.add(fullTestDTO);
        }
        Page<FullTestDTO> fullTestDTOPage = new PageImpl<>(fullTestDTOList);
        return fullTestDTOPage;
    }

    @Override
    public Test getTest(Integer id) {
        return testRepository.findById(id).orElseThrow(()->new RuntimeException("Test not found"));
    }

    @Override
    public Test createTest(Test test) {
        testRepository.save(test);
        return test;
    }

    @Override
    public Test updateTest(Integer id, Test test) {
        testRepository.save(test);
        return test;
    }

    @Override
    public void deleteTest(Integer id) {
        Test testDel = testRepository.findById(id).orElseThrow(()->new RuntimeException("Test not found"));
        if(testDel == null){
            throw new RuntimeException("Test not found");
        } else {
            testRepository.delete(testDel);
        }
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
            test.setId(dto.getId().intValue());
        }
        test.setTitle(dto.getTitle());
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



}
