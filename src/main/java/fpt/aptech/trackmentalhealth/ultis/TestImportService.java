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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            // 3. Read Options
            Sheet optionSheet = workbook.getSheet("Options");
            saveOptions(optionSheet, questionMap);

            // 4. Read Results
            Sheet resultSheet = workbook.getSheet("Results");
            saveResults(resultSheet, testMap);

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

    private void saveResults(Sheet sheet, Map<String, Test> testMap) {
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


}

