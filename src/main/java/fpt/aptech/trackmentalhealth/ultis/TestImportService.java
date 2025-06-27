package fpt.aptech.trackmentalhealth.ultis;

import fpt.aptech.trackmentalhealth.entities.Test;
import fpt.aptech.trackmentalhealth.entities.TestOption;
import fpt.aptech.trackmentalhealth.entities.TestQuestion;
import fpt.aptech.trackmentalhealth.entities.TestResult;
import fpt.aptech.trackmentalhealth.service.community.CommunityService;
import fpt.aptech.trackmentalhealth.service.test.TestService;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xssf.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestImportService {

    @Autowired
    private TestService testService;

    @Transactional
    public void importFromFile(MultipartFile file, Integer createdBy) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {

            List<String> requiredSheets = List.of("Test", "Questions", "Options", "Results");
            for (String sheetName : requiredSheets) {
                if (workbook.getSheet(sheetName) == null) {
                    throw new IllegalArgumentException("Thiếu sheet bắt buộc: " + sheetName);
                }
            }
            Sheet testSheetVal = workbook.getSheet("Test");
            Row testHeader = testSheetVal.getRow(0);
            if (!checkHeader(testHeader, List.of("Title", "Description", "Instructions"))) {
                throw new IllegalArgumentException("Sheet 'Test' không đúng cấu trúc cột");
            }
            Sheet questionSheetVal = workbook.getSheet("Questions");
            Row questionHeader = questionSheetVal.getRow(0);
            if (!checkHeader(questionHeader, List.of("QuestionText", "QuestionType", "QuestionOrder", "TestTitle"))) {
                throw new IllegalArgumentException("Sheet 'Question' không đúng cấu trúc cột");
            }

            Sheet optionSheetVal = workbook.getSheet("Options");
            Row optionHeader = optionSheetVal.getRow(0);
            if (!checkHeader(optionHeader, List.of("QuestionText", "OptionText", "ScoreValue", "OptionOrder"))) {
                throw new IllegalArgumentException("Sheet 'Option' không đúng cấu trúc cột");
            }

            Sheet resultSheetVal = workbook.getSheet("Results");
            Row resultHeader = resultSheetVal.getRow(0);
            if (!checkHeader(resultHeader, List.of("TestTitle", "MinScore", "MaxScore", "ResultText"))) {
                throw new IllegalArgumentException("sheet 'Results' không đúng cấu trúc cột");
            }

            // 1. Read Test
            Sheet testSheet = workbook.getSheet("Test");
            Map<String, Test> testMap = saveTests(testSheet, createdBy);

            // 2. Read Questions
            Sheet questionSheet = workbook.getSheet("Questions");
            Map<String, TestQuestion> questionMap = saveQuestions(questionSheet, testMap);
            Map<String, Long> questionCountMap = buildQuestionCountMap(questionMap);

            // 3. Read Options
            Sheet optionSheet = workbook.getSheet("Options");
            saveOptions(optionSheet, questionMap);

            // 4. Read Results
            Sheet resultSheet = workbook.getSheet("Results");
            saveResults(resultSheet, testMap, questionCountMap);

            workbook.close();
        }


    }

    private boolean checkHeader(Row headerRow, List<String> expectedHeaders) {
        if (headerRow == null || headerRow.getPhysicalNumberOfCells() < expectedHeaders.size()) {
            return false;
        }
        for (int i = 0; i < expectedHeaders.size(); i++) {
            String actual = headerRow.getCell(i).getStringCellValue().trim();
            if (!actual.equalsIgnoreCase(expectedHeaders.get(i))) {
                return false;
            }
        }
        return true;
    }

    private void checkSheetHeader(Sheet sheet, List<String> expectedHeaders) {
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new IllegalArgumentException("Sheet '" + sheet.getSheetName() + "' không có dòng tiêu đề.");
        }

        for (int i = 0; i < expectedHeaders.size(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell == null || !expectedHeaders.get(i).equalsIgnoreCase(cell.getStringCellValue().trim())) {
                throw new IllegalArgumentException("Sheet '" + sheet.getSheetName()
                        + "' sai cột thứ " + (i + 1) + ": phải là '" + expectedHeaders.get(i) + "'");
            }
        }
    }

    private void validateSheetRows(Sheet sheet, int expectedColumnCount) {
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                throw new IllegalArgumentException("Sheet '" + sheet.getSheetName() + "' có dòng trống tại dòng " + (rowIndex + 1));
            }
            for (int col = 0; col < expectedColumnCount; col++) {
                Cell cell = row.getCell(col);
                if (cell == null || cell.getCellType() == CellType.BLANK ||
                        (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty())) {
                    throw new IllegalArgumentException("Sheet '" + sheet.getSheetName()
                            + "' có ô trống tại dòng " + (rowIndex + 1) + ", cột " + (col + 1));
                }
            }
        }
    }

    private Map<String, Test> saveTests(Sheet sheet, Integer createdBy) {
        Map<String, Test> map = new HashMap<>();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String title = row.getCell(0).getStringCellValue().trim();
            Test checkTest = testService.checkDuplicateTest(title);
            if (checkTest != null) {
                throw new IllegalArgumentException("Sheet" + sheet.getSheetName() + "Titlte of Test is duplicate in system");
            }
            Test test = new Test();
            test.setTitle(title);
            test.setDescription(row.getCell(1).getStringCellValue().trim());
            test.setInstructions(row.getCell(2).getStringCellValue().trim());
            test.setCreatedBy(createdBy);
            test.setCreatedAt(LocalDateTime.now());

            testService.createTest(test);
            map.put(title, test); // dùng title làm key
        }
        return map;
    }

    private Map<String, TestQuestion> saveQuestions(Sheet sheet, Map<String, Test> testMap) {
        Map<String, TestQuestion> map = new HashMap<>();
        Map<String, Integer> questionOrderMap = new HashMap<>();
        Map<String, Integer> questionCountMap = new HashMap<>();
        List<String> errors = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String testTitle = row.getCell(3).getStringCellValue().trim();
            String questionText = row.getCell(0).getStringCellValue().trim();
            String questionType = row.getCell(1).getStringCellValue().trim();
            int questionOrder = (int) row.getCell(2).getNumericCellValue();

            String questionKey = testTitle + "|" + questionText;
            String orderKey = testTitle + "|" + questionOrder;

            if (map.containsKey(questionText)) {
                errors.add("❌ Dòng " + (i + 1) + ": Trùng câu hỏi '" + questionText + "'");
            }
            if (questionOrderMap.containsKey(orderKey)) {
                errors.add("❌ Dòng " + (i + 1) + ": Trùng thứ tự câu hỏi " + questionOrder + " trong test '" + testTitle + "'");
            }

            map.put(questionText, new TestQuestion());
            questionOrderMap.put(orderKey, questionOrder);
            questionCountMap.put(testTitle, questionCountMap.getOrDefault(testTitle, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : questionCountMap.entrySet()) {
            if (entry.getValue() < 5) {
                errors.add("❌ Test '" + entry.getKey() + "' phải có ít nhất 5 câu hỏi.");
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }

        // Nếu không có lỗi, tiến hành lưu thật
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String testTitle = row.getCell(3).getStringCellValue().trim();
            String questionText = row.getCell(0).getStringCellValue().trim();

            TestQuestion q = new TestQuestion();
            q.setTest(testMap.get(testTitle));
            q.setQuestionText(questionText);
            q.setQuestionType(row.getCell(1).getStringCellValue().trim());
            q.setQuestionOrder((int) row.getCell(2).getNumericCellValue());

            testService.createTestQuestion(q);
            map.put(questionText, q);
        }

        return map;
    }

    private void saveOptions(Sheet sheet, Map<String, TestQuestion> questionMap) {
        Map<String, Map<Integer, Boolean>> orderCheck = new HashMap<>();
        Map<String, Map<Integer, Boolean>> scoreCheck = new HashMap<>();
        Map<String, Integer> countOptionsPerQuestion = new HashMap<>();
        List<String> errors = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String questionText = row.getCell(0).getStringCellValue().trim();
            int optionOrder = (int) row.getCell(3).getNumericCellValue();
            int score = (int) row.getCell(2).getNumericCellValue();

            if (!questionMap.containsKey(questionText)) {
                errors.add("❌ Dòng " + (i + 1) + ": Câu hỏi '" + questionText + "' không tồn tại.");
                continue;
            }

            if (optionOrder < 1 || optionOrder > 4) {
                errors.add("❌ Dòng " + (i + 1) + ": Thứ tự đáp án phải từ 1 đến 4.");
            }

            if (score < 1 || score > 4) {
                errors.add("❌ Dòng " + (i + 1) + ": Điểm phải từ 1 đến 4.");
            }

            orderCheck.putIfAbsent(questionText, new HashMap<>());
            if (orderCheck.get(questionText).containsKey(optionOrder)) {
                errors.add("❌ Dòng " + (i + 1) + ": Trùng thứ tự đáp án " + optionOrder + " trong câu hỏi '" + questionText + "'");
            } else {
                orderCheck.get(questionText).put(optionOrder, true);
            }

            scoreCheck.putIfAbsent(questionText, new HashMap<>());
            if (scoreCheck.get(questionText).containsKey(score)) {
                errors.add("❌ Dòng " + (i + 1) + ": Trùng điểm số " + score + " trong câu hỏi '" + questionText + "'");
            } else {
                scoreCheck.get(questionText).put(score, true);
            }

            countOptionsPerQuestion.put(questionText, countOptionsPerQuestion.getOrDefault(questionText, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : countOptionsPerQuestion.entrySet()) {
            if (entry.getValue() != 4) {
                errors.add("❌ Câu hỏi '" + entry.getKey() + "' phải có đúng 4 đáp án.");
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }

        // Nếu không có lỗi, lưu thật
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String questionText = row.getCell(0).getStringCellValue().trim();

            TestOption opt = new TestOption();
            opt.setQuestion(questionMap.get(questionText));
            opt.setOptionText(row.getCell(1).getStringCellValue().trim());
            opt.setScoreValue((int) row.getCell(2).getNumericCellValue());
            opt.setOptionOrder((int) row.getCell(3).getNumericCellValue());

            testService.createTestOption(opt);
        }
    }


    private void saveResults(Sheet sheet, Map<String, Test> testMap, Map<String, Long> questionCountMap) {
        List<String> errors = new ArrayList<>();
        Map<String, List<int[]>> testRanges = new HashMap<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String testTitle = row.getCell(0).getStringCellValue().trim();

            if (!testMap.containsKey(testTitle)) {
                errors.add("❌ Dòng " + (i + 1) + ": Test '" + testTitle + "' không tồn tại.");
                continue;
            }

            int min = (int) row.getCell(1).getNumericCellValue();
            int max = (int) row.getCell(2).getNumericCellValue();
            int maxPossible = (int) (questionCountMap.getOrDefault(testTitle, 0L) * 4);

            if (min <= 0) {
                errors.add("❌ Dòng " + (i + 1) + ": minScore phải > 0.");
            }

            if (max > maxPossible) {
                errors.add("❌ Dòng " + (i + 1) + ": maxScore phải < " + maxPossible + " (4 điểm * số câu hỏi).");
            }

            if (min > max) {
                errors.add("❌ Dòng " + (i + 1) + ": minScore không được lớn hơn maxScore.");
            }

            // Kiểm tra trùng khoảng
            testRanges.putIfAbsent(testTitle, new ArrayList<>());
            for (int[] existingRange : testRanges.get(testTitle)) {
                int existingMin = existingRange[0];
                int existingMax = existingRange[1];
                if (!(max < existingMin || min > existingMax)) {
                    errors.add("❌ Dòng " + (i + 1) + ": khoảng điểm (" + min + "-" + max + ") bị trùng với khoảng (" + existingMin + "-" + existingMax + ") trong test '" + testTitle + "'");
                    break;
                }
            }

            testRanges.get(testTitle).add(new int[]{min, max});
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }

        // Nếu không có lỗi thì lưu kết quả
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String testTitle = row.getCell(0).getStringCellValue().trim();

            TestResult result = new TestResult();
            result.setTest(testMap.get(testTitle));
            result.setMinScore((int) row.getCell(1).getNumericCellValue());
            result.setMaxScore((int) row.getCell(2).getNumericCellValue());
            result.setResultText(row.getCell(3).getStringCellValue().trim());

            testService.createTestResult(result);
        }
    }

    private Map<String, Long> buildQuestionCountMap(Map<String, TestQuestion> questionMap) {
        return questionMap.values().stream()
                .collect(Collectors.groupingBy(q -> q.getTest().getTitle(), Collectors.counting()));
    }

}

