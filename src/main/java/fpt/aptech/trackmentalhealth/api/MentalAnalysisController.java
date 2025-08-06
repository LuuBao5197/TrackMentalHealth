package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import fpt.aptech.trackmentalhealth.service.diary.DiaryService;
import fpt.aptech.trackmentalhealth.service.mood.MoodService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mental")
@CrossOrigin(origins = "*")
public class MentalAnalysisController {

    private final MoodService moodService;
    private final DiaryService diaryService;
    private final UserRepository userRepository;
    private final TestRepository testRepository;

    @Value("${openai.api.key}")
    private String apiKey;

    public MentalAnalysisController(MoodService moodService, DiaryService diaryService, UserRepository userRepository, TestRepository testRepository) {
        this.moodService = moodService;
        this.diaryService = diaryService;
        this.userRepository = userRepository;
        this.testRepository = testRepository;
    }

    @GetMapping("/analyze")
    public ResponseEntity<String> analyzeMentalState() {
        Users user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).build();

        LocalDate now = LocalDate.now();
        LocalDate fourteenDaysAgo = now.minusDays(14);

        var moods = moodService.findByUserId(user.getId()).stream()
                .filter(m -> m.getDate() != null && !m.getDate().isBefore(fourteenDaysAgo))
                .sorted(Comparator.comparing(m -> m.getDate(), Comparator.reverseOrder()))
                .collect(Collectors.toList());

        var diaries = diaryService.findByUserId(user.getId()).stream()
                .filter(d -> d.getDate() != null && !d.getDate().isBefore(fourteenDaysAgo))
                .sorted(Comparator.comparing(d -> d.getDate(), Comparator.reverseOrder()))
                .collect(Collectors.toList());

        String moodSummary = moods.stream()
                .map(m -> m.getDate() + ": " + m.getMoodLevel().getName() + " - " + (m.getNote() != null ? m.getNote() : ""))
                .collect(Collectors.joining("\n"));

        String diarySummary = diaries.stream()
                .map(d -> d.getDate() + ": " + d.getContent())
                .collect(Collectors.joining("\n"));

        List<Test> allTests = testRepository.findAll();

        String testDescriptions = allTests.stream()
                .map(test -> "Tên: " + test.getTitle()
                        + "\nMô tả: " + test.getDescription()
                        + "\nHướng dẫn: " + test.getInstructions())
                .collect(Collectors.joining("\n\n"));

        String prompt = "Dưới đây là cảm xúc và nhật ký của người dùng trong 14 ngày gần nhất:\n"
                + "---Cảm xúc---\n" + moodSummary + "\n"
                + "---Nhật ký---\n" + diarySummary + "\n\n"
                + "---Danh sách các bài test tâm lý có sẵn---\n" + testDescriptions + "\n\n"
                + "Hãy:\n"
                + "1. Phân tích tình trạng tinh thần người dùng theo 4 mức độ:\n"
                + "   - Level 1: Ổn định\n"
                + "   - Level 2: Cảnh báo nhẹ\n"
                + "   - Level 3: Cảnh báo vừa (lo âu, buồn bã kéo dài)\n"
                + "   - Level 4: Nguy cơ cao (trầm cảm nghiêm trọng, tuyệt vọng...)\n"
                + "2. Nếu Level = 3, chọn một bài test phù hợp từ danh sách trên và gợi ý.\n"
                + "3. Nếu Level = 4, cảnh báo khẩn cấp và gợi ý liên hệ chuyên gia tâm lý hoặc bác sĩ. Không đề xuất bài test.\n"
                + "4. Trả kết quả theo định dạng JSON như sau:\n"
                + "{\n"
                + "  \"level\": 3,\n"
                + "  \"description\": \"...\",\n"
                + "  \"suggestion\": {\n"
                + "    \"type\": \"test\", // hoặc 'emergency'\n"
                + "    \"testTitle\": \"...\",  // nếu là type=test\n"
                + "    \"testDescription\": \"...\",\n"
                + "    \"instructions\": \"...\"\n"
                + "    // nếu type=emergency thì chỉ cần: \"message\": \"...\"\n"
                + "  }\n"
                + "}";

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-4o-mini");
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", prompt));
            body.put("messages", messages);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://api.openai.com/v1/chat/completions",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                Map data = response.getBody();
                List choices = (List) data.get("choices");
                if (!choices.isEmpty()) {
                    Map choice = (Map) choices.get(0);
                    Map messageMap = (Map) choice.get("message");
                    String result = (String) messageMap.get("content");

                    try {
                        // Parse JSON từ chuỗi result
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> parsedResult = objectMapper.readValue(result, Map.class);

                        // Nếu có gợi ý bài test thì tìm testId và thêm vào
                        Map<String, Object> suggestion = (Map<String, Object>) parsedResult.get("suggestion");
                        if (suggestion != null && "test".equals(suggestion.get("type"))) {
                            String testTitle = (String) suggestion.get("testTitle");

                            Optional<Test> matchedTest = allTests.stream()
                                    .filter(t -> t.getTitle().equalsIgnoreCase(testTitle))
                                    .findFirst();

                            matchedTest.ifPresent(test -> suggestion.put("testId", test.getId()));
                        }

                        // Trả lại chuỗi JSON sau khi thêm testId
                        String modifiedJson = objectMapper.writeValueAsString(parsedResult);
                        return ResponseEntity.ok(modifiedJson);

                    } catch (Exception e) {
                        e.printStackTrace();
                        // Nếu lỗi parse, vẫn trả về raw result
                        return ResponseEntity.ok(result);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("Không thể phân tích lúc này.");
    }

    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findAll().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }
}
