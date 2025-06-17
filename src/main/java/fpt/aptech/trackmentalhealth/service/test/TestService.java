package fpt.aptech.trackmentalhealth.service.test;

import fpt.aptech.trackmentalhealth.entities.Test;
import fpt.aptech.trackmentalhealth.entities.TestOption;
import fpt.aptech.trackmentalhealth.entities.TestQuestion;
import fpt.aptech.trackmentalhealth.entities.TestResult;

import java.util.List;

public interface TestService {
    // Business logic cua Test
    List<Test> getTests();
    Test getTest(Integer id);
    Test createTest(Test test);
    Test updateTest(Integer id, Test test);
    void deleteTest(Integer id);
    //Business logic cua TestAnswer
    List<TestQuestion> getTestQuestionsByTestId(Integer id);
    TestQuestion getTestQuestion(Integer id);
    TestQuestion createTestQuestion(TestQuestion testQuestion);
    TestQuestion updateTestQuestion(Integer id, TestQuestion testQuestion);
    void deleteTestQuestion(Integer id);
    // Business logic cua TestOption
    List<TestOption> getTestOptionsByTestQuestionId(Integer id);
    TestOption getTestOption(Integer id);
    TestOption createTestOption(TestOption testOption);
    TestOption updateTestOption(Integer id, TestOption testOption);
    void deleteTestOption(Integer id);
    // Business login cua TestResult
    List<TestResult> getTestResultsByTestId(Integer id);
    TestResult getTestResult(Integer id);
    TestResult createTestResult(TestResult testResult);
    TestResult updateTestResult(Integer id, TestResult testResult);
    void deleteTestResult(Integer id);
}
