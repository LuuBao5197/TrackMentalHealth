package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.dto.test.*;
import fpt.aptech.trackmentalhealth.entities.Test;
import fpt.aptech.trackmentalhealth.entities.TestOption;
import fpt.aptech.trackmentalhealth.entities.TestQuestion;
import fpt.aptech.trackmentalhealth.entities.TestResult;
import fpt.aptech.trackmentalhealth.service.test.TestService;
import fpt.aptech.trackmentalhealth.ultis.TestImportService;
import fpt.aptech.trackmentalhealth.ultis.TestImportValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    TestService testService;
    @Autowired
    TestImportService testImportService;

    // API CUA TEST //
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getTests(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Test> tests;

        if (search != null && !search.isEmpty()) {
            tests = testService.searchTests(search, pageable);
        } else {
            tests = testService.getTests(pageable);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("data", tests.getContent());
        response.put("total", tests.getTotalElements());
        response.put("currentPage", tests.getNumber() + 1); // Page index starts at 0
        response.put("totalPages", tests.getTotalPages());

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<TestDetailDTO> getTestById(@PathVariable Integer id) {
        Test test = testService.getTest(id);
        if (test == null) {
            return ResponseEntity.notFound().build();
        }
        List<TestQuestion> questionList = testService.getTestQuestionsByTestId(test.getId());
        List<TestResult> questionResultList = testService.getTestResultsByTestId(test.getId());

        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (TestQuestion testQuestion : questionList) {
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setId(testQuestion.getId());
            questionDTO.setQuestionText(testQuestion.getQuestionText());
            questionDTO.setQuestionType(testQuestion.getQuestionType());
            questionDTO.setQuestionOrder(testQuestion.getQuestionOrder());
            List<TestOption> testOptionList =  testService.getTestOptionsByTestQuestionId(testQuestion.getId());
            List<OptionDTO> optionDTOList = new ArrayList<>();
            for (TestOption testOption : testOptionList) {
                OptionDTO optionDTO = new OptionDTO();
                optionDTO.setId(testOption.getId());
                optionDTO.setOptionText(testOption.getOptionText());
                optionDTO.setScoreValue(testOption.getScoreValue());
                optionDTO.setOptionOrder(testOption.getOptionOrder());
                optionDTOList.add(optionDTO);
            }
            questionDTO.setOptions(optionDTOList);
            questionDTOList.add(questionDTO);
        }

        List<TestResultDTO> resultDTOS  = new ArrayList<>();
        for (TestResult testResult : questionResultList) {
            TestResultDTO testResultDTO = new TestResultDTO();
            testResultDTO.setId(testResult.getId());
            testResultDTO.setResultText(testResult.getResultText());
            testResultDTO.setMinScore(testResult.getMinScore());
            testResultDTO.setMaxScore(testResult.getMaxScore());
            resultDTOS.add(testResultDTO);
        }
        TestDetailDTO testDetailDTO = new TestDetailDTO();
        testDetailDTO.setId(test.getId());
        testDetailDTO.setDescription(test.getDescription());
        testDetailDTO.setTitle(test.getTitle());
        testDetailDTO.setInstructions(test.getInstructions());
        testDetailDTO.setQuestions(questionDTOList);
        testDetailDTO.setResults(resultDTOS);
        return ResponseEntity.ok().body(testDetailDTO);

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
    @PostMapping("/multiTestResult")
    public ResponseEntity<List<TestResult>> createMultiTestResult(@RequestBody List<TestResult> testResults) {
        return ResponseEntity.status(HttpStatus.CREATED).body(testService.createMultipleTestResults(testResults));
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

    // api import file excel (xlsx) de tao bo cau hoi
    @PostMapping("/import-test")
    public ResponseEntity<?> importTest(@RequestParam("file") MultipartFile file) {
        try {
            List<String> result = testImportService.importFromFile(file, 1);
            return ResponseEntity.ok(result);
        } catch (TestImportValidationException ex) {
            return ResponseEntity.badRequest().body(ex.getErrors());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of("❌ Lỗi hệ thống: " + e.getMessage()));
        }
    }


    // api tao dong bo 1 bai test tu bai test cau hoi cac dap an den ket qua hien thi

    @PostMapping("/full")
    public ResponseEntity<?> createFullTest(@RequestBody FullTestDTO dto) {
        try {
            // Gọi service để xử lý
            testService.createFullTest(dto);
            return ResponseEntity.ok(Map.of("message", "Tạo bài test thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}/getMaxScore")
    public ResponseEntity<Integer> getMaxScore(@PathVariable Integer id) {

         Integer maxScore = testService.getMaxMarkOfTest(id);
         return ResponseEntity.ok(maxScore);
    }

}
