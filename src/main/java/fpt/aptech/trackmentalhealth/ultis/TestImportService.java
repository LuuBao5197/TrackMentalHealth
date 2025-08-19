package fpt.aptech.trackmentalhealth.ultis;

import fpt.aptech.trackmentalhealth.entities.Test;
import fpt.aptech.trackmentalhealth.entities.TestOption;
import fpt.aptech.trackmentalhealth.entities.TestQuestion;
import fpt.aptech.trackmentalhealth.entities.TestResult;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestImportService {

    @Autowired
    private TestService testService;

    @Transactional
    public List<String> importFromFile(MultipartFile file, Integer createdBy) throws Exception {
        List<String> errors = new ArrayList<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            List<String> requiredSheets = List.of("Test", "Questions", "Options", "Results");

            for (String sheetName : requiredSheets) {
                if (workbook.getSheet(sheetName) == null) {
                    errors.add("❌ Sheet '" + sheetName + "' không tồn tại trong file Excel.");
                }
            }

            if (!errors.isEmpty()) {
                throw new TestImportValidationException("Thiếu sheet bắt buộc.", errors);
            }

            // Kiểm tra tiêu đề sheet
            if (!checkHeader(workbook.getSheet("Test").getRow(0), List.of("Title", "Description", "Instructions"))) {
                errors.add("❌ Sheet 'Test' không đúng cấu trúc cột.");
            }

            if (!checkHeader(workbook.getSheet("Questions").getRow(0), List.of("QuestionText", "QuestionType", "QuestionOrder", "TestTitle"))) {
                errors.add("❌ Sheet 'Questions' không đúng cấu trúc cột.");
            }

            if (!checkHeader(workbook.getSheet("Options").getRow(0), List.of("QuestionText", "OptionText", "ScoreValue", "OptionOrder"))) {
                errors.add("❌ Sheet 'Options' không đúng cấu trúc cột.");
            }

            if (!checkHeader(workbook.getSheet("Results").getRow(0), List.of("TestTitle", "MinScore", "MaxScore", "ResultText", "Category"))) {
                errors.add("❌ Sheet 'Results' không đúng cấu trúc cột.");
            }

            if (!errors.isEmpty()) {
                throw new TestImportValidationException("Lỗi cấu trúc tiêu đề trong các sheet.", errors);
            }

            // Đọc và lưu dữ liệu từng sheet
            Map<String, Test> testMap;
            Map<String, TestQuestion> questionMap;
            Map<String, Long> questionCountMap;

            try {
                Sheet testSheet = workbook.getSheet("Test");
                testMap = saveTests(testSheet, createdBy);
            } catch (IllegalArgumentException e) {
                errors.add(e.getMessage());
                throw new TestImportValidationException("Lỗi khi xử lý Sheet Test", errors);
            }

            try {
                Sheet questionSheet = workbook.getSheet("Questions");
                questionMap = saveQuestions(questionSheet, testMap);
                questionCountMap = buildQuestionCountMap(questionMap);
            } catch (IllegalArgumentException e) {
                errors.addAll(List.of(e.getMessage().split("\n")));
                throw new TestImportValidationException("Lỗi khi xử lý Sheet Questions", errors);
            }

            try {
                Sheet optionSheet = workbook.getSheet("Options");
                saveOptions(optionSheet, questionMap);
            } catch (IllegalArgumentException e) {
                errors.addAll(List.of(e.getMessage().split("\n")));
                throw new TestImportValidationException("Lỗi khi xử lý Sheet Options", errors);
            }

            try {
                Sheet resultSheet = workbook.getSheet("Results");
                saveResults(resultSheet, testMap, questionCountMap, questionMap);
            } catch (IllegalArgumentException e) {
                errors.addAll(List.of(e.getMessage().split("\n")));
                throw new TestImportValidationException("Lỗi khi xử lý Sheet Results", errors);
            }

            workbook.close();
        }

        return List.of("✅ Import thành công");
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
            if (entry.getValue() < 3) {
                errors.add("❌ Test '" + entry.getKey() + "' phải có ít nhất 3 câu hỏi.");
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
        Map<String, Set<Integer>> orderCheck = new HashMap<>();
        Map<String, Set<Integer>> scoreCheck = new HashMap<>();
        List<String> errors = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String questionText = row.getCell(0) != null ? row.getCell(0).getStringCellValue().trim() : "";

            if (!questionMap.containsKey(questionText)) {
                errors.add("❌ Dòng " + (i + 1) + ": Câu hỏi '" + questionText + "' không tồn tại.");
                continue;
            }

            int optionOrder = (int) row.getCell(3).getNumericCellValue();
            int score = (int) row.getCell(2).getNumericCellValue();

            // Kiểm tra điểm số >= 0
            if (score < 0) {
                errors.add("❌ Dòng " + (i + 1) + ": Điểm số phải >= 0 trong câu hỏi '" + questionText + "'");
            }

            // Kiểm tra trùng thứ tự đáp án trong cùng câu hỏi
            orderCheck.putIfAbsent(questionText, new HashSet<>());
            if (!orderCheck.get(questionText).add(optionOrder)) {
                errors.add("❌ Dòng " + (i + 1) + ": Trùng thứ tự đáp án " + optionOrder + " trong câu hỏi '" + questionText + "'");
            }

            // Kiểm tra trùng điểm số trong cùng câu hỏi
            scoreCheck.putIfAbsent(questionText, new HashSet<>());
            if (!scoreCheck.get(questionText).add(score)) {
                errors.add("❌ Dòng " + (i + 1) + ": Trùng điểm số " + score + " trong câu hỏi '" + questionText + "'");
            }
        }

        // Kiểm tra điểm số liên tiếp nhau cho từng câu hỏi
        for (Map.Entry<String, Set<Integer>> entry : scoreCheck.entrySet()) {
            String questionText = entry.getKey();
            List<Integer> scores = new ArrayList<>(entry.getValue());
            Collections.sort(scores);

            for (int j = 1; j < scores.size(); j++) {
                if (scores.get(j) != scores.get(j - 1) + 1) {
                    errors.add("❌ Câu hỏi '" + questionText + "' có điểm số không liên tiếp: "
                            + scores);
                    break;
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }

        // Nếu không có lỗi, lưu thật
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String questionText = row.getCell(0).getStringCellValue().trim();

            TestOption opt = new TestOption();
            opt.setQuestion(questionMap.get(questionText));
            questionMap.get(questionText).getOptions().add(opt);
            opt.setOptionText(row.getCell(1).getStringCellValue().trim());
            opt.setScoreValue((int) row.getCell(2).getNumericCellValue());
            opt.setOptionOrder((int) row.getCell(3).getNumericCellValue());

            testService.createTestOption(opt);
        }
    }

    private void saveResults(Sheet sheet,
                             Map<String, Test> testMap,
                             Map<String, Long> questionCountMap,
                             Map<String, TestQuestion> questionMap) {
        List<String> errors = new ArrayList<>();
        // Map<TestTitle, Map<Category, List<int[]>>>
        Map<String, Map<String, List<int[]>>> testCategoryRanges = new HashMap<>();

        // build map: testTitle -> (category -> maxScore có thể đạt)
        Map<String, Map<String, Integer>> testCategoryMaxScores = new HashMap<>();
        for (TestQuestion q : questionMap.values()) {
            String testTitle = q.getTest().getTitle();
            String category = q.getQuestionType();
            int maxOptionScore = q.getOptions()
                    .stream()
                    .mapToInt(TestOption::getScoreValue)
                    .max()
                    .orElse(0);

            testCategoryMaxScores
                    .computeIfAbsent(testTitle, k -> new HashMap<>())
                    .merge(category, maxOptionScore, Integer::sum);
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String testTitle = row.getCell(0).getStringCellValue().trim();
            if (!testMap.containsKey(testTitle)) {
                errors.add("❌ Dòng " + (i + 1) + ": Test '" + testTitle + "' không tồn tại.");
                continue;
            }

            int min = (int) row.getCell(1).getNumericCellValue();
            int max = (int) row.getCell(2).getNumericCellValue();
            String category = row.getCell(4).getStringCellValue().trim();

            // lấy maxScore thực sự của category này
            int maxPossible = testCategoryMaxScores
                    .getOrDefault(testTitle, new HashMap<>())
                    .getOrDefault(category, 0);

            // validate score
            if (min < 0) {
                errors.add("❌ Dòng " + (i + 1) + ": minScore phải >= 0.");
            }
            if (max > maxPossible) {
                errors.add("❌ Dòng " + (i + 1) + ": maxScore phải <= " + maxPossible +
                        " (tổng điểm tối đa của các câu hỏi trong category).");
            }
            if (min > max) {
                errors.add("❌ Dòng " + (i + 1) + ": minScore không được lớn hơn maxScore.");
            }

            // validate category tồn tại trong test
            List<String> allowedCategories = questionMap.values().stream()
                    .filter(q -> q.getTest().getTitle().equals(testTitle))
                    .map(TestQuestion::getQuestionType)
                    .distinct()
                    .toList();
            if (!allowedCategories.contains(category)) {
                errors.add("❌ Dòng " + (i + 1) + ": Category '" + category
                        + "' không hợp lệ. Phải thuộc questionType của test '" + testTitle + "'");
            }

            // kiểm tra trùng khoảng theo từng test + category
            testCategoryRanges
                    .computeIfAbsent(testTitle, k -> new HashMap<>())
                    .computeIfAbsent(category, k -> new ArrayList<>());

            List<int[]> ranges = testCategoryRanges.get(testTitle).get(category);
            for (int[] existingRange : ranges) {
                int existingMin = existingRange[0];
                int existingMax = existingRange[1];
                if (!(max < existingMin || min > existingMax)) {
                    errors.add("❌ Dòng " + (i + 1) + ": khoảng điểm (" + min + "-" + max
                            + ") bị trùng với khoảng (" + existingMin + "-" + existingMax
                            + ") trong test '" + testTitle + "', category '" + category + "'");
                    break;
                }
            }
            ranges.add(new int[]{min, max});
            ranges.sort((a, b) -> Integer.compare(a[0], b[0]));

            // kiểm tra liên tục
            for (int j = 1; j < ranges.size(); j++) {
                int[] prev = ranges.get(j - 1);
                int[] curr = ranges.get(j);
                if (curr[0] != prev[1] + 1) {
                    errors.add("❌ Test '" + testTitle + "', category '" + category +
                            "' có khoảng điểm không liên tiếp giữa (" +
                            prev[0] + "-" + prev[1] + ") và (" + curr[0] + "-" + curr[1] + ")");
                }
            }
            // Kiểm tra bắt đầu từ 0 và kết thúc bằng maxScore'
        }
        // Kiểm tra bắt đầu từ 0 và kết thúc bằng maxScore cho từng test + category
        for (Map.Entry<String, Map<String, List<int[]>>> testEntry : testCategoryRanges.entrySet()) {
            String testTitle = testEntry.getKey();
            for (Map.Entry<String, List<int[]>> catEntry : testEntry.getValue().entrySet()) {
                String category = catEntry.getKey();
                List<int[]> ranges = catEntry.getValue();
                ranges.sort((a, b) -> Integer.compare(a[0], b[0]));

                if (ranges.isEmpty()) continue;

                // Lấy min đầu tiên và max cuối cùng
                int firstMin = ranges.get(0)[0];
                int lastMax = ranges.get(ranges.size() - 1)[1];

                // Lấy maxScore thực sự của category này
                int maxPossible = testCategoryMaxScores
                        .getOrDefault(testTitle, new HashMap<>())
                        .getOrDefault(category, 0);

                if (firstMin != 0) {
                    errors.add("❌ Test '" + testTitle + "', category '" + category +
                            "' phải bắt đầu từ 0, hiện là " + firstMin);
                }
                if (lastMax != maxPossible) {
                    errors.add("❌ Test '" + testTitle + "', category '" + category +
                            "' phải kết thúc bằng maxScore " + maxPossible + ", hiện là " + lastMax);
                }
            }
        }


        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errors));
        }


        // Lưu dữ liệu
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            String testTitle = row.getCell(0).getStringCellValue().trim();

            TestResult result = new TestResult();
            result.setTest(testMap.get(testTitle));
            result.setMinScore((int) row.getCell(1).getNumericCellValue());
            result.setMaxScore((int) row.getCell(2).getNumericCellValue());
            result.setResultText(row.getCell(3).getStringCellValue().trim());
            result.setCategory(row.getCell(4).getStringCellValue().trim()); // thêm category

            testService.createTestResult(result);
        }
        Integer minScoreCurrent = 0;
        Integer maxScoreCurrent = 0;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if ((int) row.getCell(1).getNumericCellValue() < minScoreCurrent) {
                minScoreCurrent = (int) row.getCell(1).getNumericCellValue();
            }
            if (row == null) continue;

        }
    }

    private Map<String, Long> buildQuestionCountMap(Map<String, TestQuestion> questionMap) {
        return questionMap.values().stream()
                .collect(Collectors.groupingBy(q -> q.getTest().getTitle(), Collectors.counting()));
    }

}

