package fpt.aptech.trackmentalhealth.service.test;
import fpt.aptech.trackmentalhealth.dto.test.FullTestDTO;
import fpt.aptech.trackmentalhealth.dto.test.OptionDTO;
import fpt.aptech.trackmentalhealth.dto.test.QuestionDTO;
import fpt.aptech.trackmentalhealth.entities.Test;
import fpt.aptech.trackmentalhealth.entities.TestOption;
import fpt.aptech.trackmentalhealth.entities.TestQuestion;
import fpt.aptech.trackmentalhealth.entities.TestResult;
import fpt.aptech.trackmentalhealth.repository.test.TestOptionRepository;
import fpt.aptech.trackmentalhealth.repository.test.TestQuestionRepository;
import fpt.aptech.trackmentalhealth.repository.test.TestRepository;
import fpt.aptech.trackmentalhealth.repository.test.TestResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TestServiceImp implements TestService {
    TestOptionRepository testOptionRepository;
    TestResultRepository testResultRepository;
    TestQuestionRepository testQuestionRepository;
    TestRepository testRepository;

    public TestServiceImp(TestOptionRepository testOptionRepository, TestResultRepository testResultRepository,
                          TestQuestionRepository testQuestionRepository, TestRepository testRepository) {
        this.testOptionRepository = testOptionRepository;
        this.testResultRepository = testResultRepository;
        this.testQuestionRepository = testQuestionRepository;
        this.testRepository = testRepository;

    }

    @Override
    public List<Test> getTests() {
        return testRepository.findAll();
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
        test.setTitle(dto.getTitle());
        test.setDescription(dto.getDescription());
        test.setInstructions(dto.getInstructions());
        test = testRepository.save(test);

        for (QuestionDTO qDto : dto.getQuestions()) {
            TestQuestion question = new TestQuestion();
            question.setTest(test);
            question.setQuestionText(qDto.getQuestionText());
            question.setQuestionType(qDto.getQuestionType());
            question.setQuestionOrder(qDto.getQuestionOrder());
            question = testQuestionRepository.save(question);

            for (OptionDTO oDto : qDto.getOptions()) {
                TestOption option = new TestOption();
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

    @Override
    public Test checkDuplicateTest(String title) {
        return testRepository.findByTitleIgnoreCase(title);
    }


}
