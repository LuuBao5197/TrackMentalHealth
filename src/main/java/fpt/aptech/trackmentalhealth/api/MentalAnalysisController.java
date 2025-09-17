package fpt.aptech.trackmentalhealth.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.aptech.trackmentalhealth.entities.Test;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.test.TestRepository;
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
                .map(test -> "Title: " + test.getTitle()
                        + "\nDescription: " + test.getDescription()
                        + "\nInstructions: " + test.getInstructions())
                .collect(Collectors.joining("\n\n"));

        String prompt = "Below are the moods and diary entries of the user in the last 14 days:\n"
                + "---Moods---\n" + moodSummary + "\n"
                + "---Diary---\n" + diarySummary + "\n\n"
                + "---List of available psychological tests---\n" + testDescriptions + "\n\n"
                + "Please:\n"
                + "1. Analyze the user's mental state according to 4 levels:\n"
                + "   - Level 1: Stable (if Level = 1, return a motivational message encouraging the user to maintain their good state).\n"
                + "   - Level 2: Mild warning\n"
                + "   - Level 3: Moderate warning (prolonged anxiety, sadness)\n"
                + "   - Level 4: High risk (severe depression, hopelessness...)\n"
                + "2. If Level = 1, the suggestion must follow this format:\n"
                + "   {\n"
                + "     \"type\": \"motivation\",\n"
                + "     \"message\": \"You are doing very well, keep maintaining a positive lifestyle!\"\n"
                + "   }\n"
                + "3. If Level = 3, choose a suitable test from the list above and suggest it.\n"
                + "4. If Level = 4, provide an emergency warning and suggest contacting a psychologist or doctor. Do not suggest a test.\n"
                + "5. Only return raw JSON in the following format, without ``` or any other text:\n"
                + "{\n"
                + "  \"level\": 3,\n"
                + "  \"description\": \"...\",\n"
                + "  \"suggestion\": {\n"
                + "    \"type\": \"test\", // or 'emergency'\n"
                + "    \"testTitle\": \"...\",  // if type=test\n"
                + "    \"testDescription\": \"...\",\n"
                + "    \"instructions\": \"...\"\n"
                + "    // if type=emergency then only need: \"message\": \"...\"\n"
                + "  }\n"
                +  "Important: All output (description, suggestion, messages) must be in English only."
                + "}";

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-4o-mini");

            // If the model supports, request pure JSON response
            body.put("response_format", Map.of("type", "json_object"));

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

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map data = response.getBody();
                List choices = (List) data.get("choices");

                if (!choices.isEmpty()) {
                    Map choice = (Map) choices.get(0);
                    Map messageMap = (Map) choice.get("message");
                    String result = (String) messageMap.get("content");

                    // Clean the result if it still contains backticks
                    String cleanResult = result.trim();
                    // Remove all ```json or ``` wrappers
                    cleanResult = cleanResult.replaceAll("```json", "")
                            .replaceAll("```", "")
                            .trim();
                    if (cleanResult.startsWith("```")) {
                        int firstBrace = cleanResult.indexOf("{");
                        int lastBrace = cleanResult.lastIndexOf("}");
                        if (firstBrace >= 0 && lastBrace >= 0) {
                            cleanResult = cleanResult.substring(firstBrace, lastBrace + 1);
                        }
                    }

                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, Object> parsedResult = objectMapper.readValue(cleanResult, Map.class);

                        // Add testId if needed
                        Map<String, Object> suggestion = (Map<String, Object>) parsedResult.get("suggestion");
                        if (suggestion != null && "test".equals(suggestion.get("type"))) {
                            String testTitle = (String) suggestion.get("testTitle");
                            allTests.stream()
                                    .filter(t -> t.getTitle().equalsIgnoreCase(testTitle))
                                    .findFirst()
                                    .ifPresent(test -> suggestion.put("testId", test.getId()));
                        }

                        String modifiedJson = objectMapper.writeValueAsString(parsedResult);
                        return ResponseEntity.ok(modifiedJson);

                    } catch (Exception e) {
                        e.printStackTrace();
                        // Return raw if JSON parsing fails
                        return ResponseEntity.ok(cleanResult);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("Unable to analyze at the moment.");
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
