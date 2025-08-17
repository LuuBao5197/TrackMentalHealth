package fpt.aptech.trackmentalhealth.service.test;

import fpt.aptech.trackmentalhealth.dto.test.FullTestDTO;
import fpt.aptech.trackmentalhealth.dto.test.history.UserTestHistoryDTO;
import fpt.aptech.trackmentalhealth.entities.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface TestService {
    // Business logic cua Test
    Page<FullTestDTO> getTests(Pageable pageable);
    Page<FullTestDTO> searchTests(String keyword, Pageable pageable);
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
    void createFullTest(FullTestDTO dto);
    void updateFullTest(FullTestDTO dto);
    Test checkDuplicateTest(String title);

    // Ham lay diem toi da cua 1 bai test
    Integer getMaxMarkOfTest(Integer id);
    public Integer getMaxMarkOfTestByCategory(Integer id, String category);

    List<TestResult> createMultipleTestResults(List<TestResult> testResults);

    // Cac phuong thuc lien quan den viec luu tru ket qua nguoi dung lam bai Test
    UserTestAttempt getUserTestAttempt(Integer userId, Integer testId);
    UserTestAttempt saveUserTestAttempt(UserTestAttempt userTestAttempt);
    UserTestAnswer getUserTestAnswer(Integer userId, Integer testId);
    List<UserTestAnswer> saveUserTestAnswer(List<UserTestAnswer> userTestAnswers);
    List<UserTestAttempt> getHistoryAttempts(Integer userId);

    Set<String> getCategoriesOfTest(Integer testId);
}
