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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mental")
@CrossOrigin(origins = "*")
public class MentalAnalysisController {

    private final MoodService moodService;
    private final DiaryService diaryService;
    private final UserRepository userRepository;

    @Value("${openai.api.key}")
    private String apiKey;

    public MentalAnalysisController(MoodService moodService, DiaryService diaryService, UserRepository userRepository) {
        this.moodService = moodService;
        this.diaryService = diaryService;
        this.userRepository = userRepository;
    }

    @GetMapping("/analyze")
    public ResponseEntity<String> analyzeMentalState() {
        Users user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).build();

        LocalDate now = LocalDate.now();
        LocalDate fourteenDaysAgo = now.minusDays(14);

        var moods = moodService.findByUserId(user.getId()).stream()
                .filter(m -> m.getDate() != null && !m.getDate().isBefore(fourteenDaysAgo))
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .collect(Collectors.toList());

        var diaries = diaryService.findByUserId(user.getId()).stream()
                .filter(d -> d.getDate() != null && !d.getDate().isBefore(fourteenDaysAgo))
                .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                .collect(Collectors.toList());

        String moodSummary = moods.stream()
                .map(m -> m.getDate() + ": " + m.getMoodLevel().getName() + " - " + (m.getNote() != null ? m.getNote() : ""))
                .collect(Collectors.joining("\n"));

        String diarySummary = diaries.stream()
                .map(d -> d.getDate() + ": " + d.getContent())
                .collect(Collectors.joining("\n"));

        String prompt = "Dưới đây là cảm xúc và nhật ký của người dùng trong 14 ngày gần nhất:\n"
                + "---Cảm xúc---\n" + moodSummary + "\n"
                + "---Nhật ký---\n" + diarySummary + "\n"
                + "Hãy phân tích và **chỉ trả lời nếu phát hiện dấu hiệu bất ổn về tinh thần**.\n"
                + "Nếu có, **chỉ đưa ra cảnh báo ngắn gọn** (1 câu) và **gợi ý 1 bài tập cụ thể phù hợp**.\n"
                + "Nếu không có dấu hiệu bất ổn, **không cần trả lời gì cả**.";


        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", apiKey);

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
                    return ResponseEntity.ok(result);
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
