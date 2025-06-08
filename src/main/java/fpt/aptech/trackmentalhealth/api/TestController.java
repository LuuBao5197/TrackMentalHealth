package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.Test;
import fpt.aptech.trackmentalhealth.entities.TestOption;
import fpt.aptech.trackmentalhealth.entities.TestQuestion;
import fpt.aptech.trackmentalhealth.entities.TestResult;
import fpt.aptech.trackmentalhealth.service.doctor.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    TestService testService;

    // API CUA TEST //
    @GetMapping("/")
    public ResponseEntity<List<Test>> getTests() {
        List<Test> tests =  testService.getTests();
        return ResponseEntity.ok().body(tests);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Test> getTestById(@PathVariable Integer id) {
        Test test = testService.getTest(id);
        return ResponseEntity.ok().body(test);
    }
    @PostMapping("/")
    public ResponseEntity<Test> createTest(@RequestBody Test test) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testService.createTest(test));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Test> updateTest(@PathVariable Integer id, @RequestBody Test test) {
       return ResponseEntity.status(HttpStatus.OK).body(testService.updateTest(id, test));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Test> deleteTest(@PathVariable Integer id) {
        testService.deleteTest(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

                                     //API CUA TEST QUESTION


    @GetMapping("/{id}/question")
    public ResponseEntity<List<TestQuestion>> getTestQuestionsByTestId(@PathVariable Integer id) {
        List<TestQuestion> testQuestions = testService.getTestQuestionsByTestId(id);
        return ResponseEntity.ok().body(testQuestions);
    }
    @GetMapping("/question/{id}")
    public ResponseEntity<TestQuestion> getTestQuestionById(@PathVariable Integer id) {
        TestQuestion testQuestion = testService.getTestQuestion(id);
        return ResponseEntity.ok().body(testQuestion);
    }
    @PostMapping("/question")
    public ResponseEntity<TestQuestion> createTestQuestion(@RequestBody TestQuestion testQuestion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testService.createTestQuestion(testQuestion));
    }
    @PutMapping("/question/{id}")
    public ResponseEntity<TestQuestion> updateTestQuestion(@PathVariable Integer id, @RequestBody TestQuestion testQuestion) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.updateTestQuestion(id, testQuestion));
    }
    @DeleteMapping("/question/{id}")
    public ResponseEntity<TestQuestion> deleteTestQuestion(@PathVariable Integer id) {
       testService.deleteTestQuestion(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // API CUA TEST OPTION
    @GetMapping("/questions/{id}/option")
    public ResponseEntity<List<TestOption>> getTestOptionsByTestQuestionId(@PathVariable Integer id) {
        List<TestOption> testOptions = testService.getTestOptionsByTestQuestionId(id);
        return ResponseEntity.ok().body(testOptions);
    }
    @GetMapping("/question/option/{id}")
    public ResponseEntity<TestOption> getTestOptionById(@PathVariable Integer id) {
        TestOption testOption = testService.getTestOption(id);
        return ResponseEntity.ok().body(testOption);
    }
    @PostMapping("/question/option")
    public ResponseEntity<TestOption> createTestOption(@RequestBody TestOption testOption) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testService.createTestOption(testOption));
    }
    @PutMapping("/question/option/{id}")
    public ResponseEntity<TestOption> updateTestOption(@PathVariable Integer id, @RequestBody TestOption testOption) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.updateTestOption(id, testOption));
    }
    @DeleteMapping("/question/option/{id}")
    public ResponseEntity<TestOption> deleteTestOption(@PathVariable Integer id) {
        testService.deleteTestOption(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // API CUA TEST RESULT
    @GetMapping("/{id}/testResult")
    public ResponseEntity<List<TestResult>> getTestResultsByTestId(@PathVariable Integer id) {
            List<TestResult> testResults = testService.getTestResultsByTestId(id);
            return ResponseEntity.ok().body(testResults);
    }
    @GetMapping("/testResult/{id}")
    public ResponseEntity<TestResult> getTestResultById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.getTestResult(id));
    }
    @PostMapping("/testResult")
    public ResponseEntity<TestResult> createTestResult(@RequestBody TestResult testResult) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testService.createTestResult(testResult));
    }
    @PutMapping("/testResult/{id}")
    public ResponseEntity<TestResult> updateTestResult(@PathVariable Integer id, @RequestBody TestResult testResult) {
        return ResponseEntity.status(HttpStatus.OK).body(testService.updateTestResult(id, testResult));
    }
    @DeleteMapping("/testResult/{id}")
    public ResponseEntity<TestResult> deleteTestResult(@PathVariable Integer id) {
        testService.deleteTestResult(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}
