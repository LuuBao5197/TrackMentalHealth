package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.Mood;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import fpt.aptech.trackmentalhealth.service.mood.MoodService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/moods")
@CrossOrigin(origins = "*")
public class MoodController {

    private final MoodService moodService;
    private final UserRepository userRepository;

    @Value("${openai.api.key}")
    private String apiKey;


    public MoodController(MoodService moodService, UserRepository userRepository) {
        this.moodService = moodService;
        this.userRepository = userRepository;
    }

    // ✅ Get all moods by logged-in user
    @GetMapping("/my")
    public ResponseEntity<List<Mood>> getMyMoods() {
        Users user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(moodService.findByUserId(user.getId()));
    }

    @PostMapping
    public ResponseEntity<?> createMood(@RequestBody Mood mood) {
        Users user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).build();

        mood.setUsers(user);
        if (mood.getDate() == null) {
            mood.setDate(LocalDate.now());
        }
        // ✅ Check contradiction between mood and note
        if (isContradictory(mood)) {
            return ResponseEntity.status(400).body("⚠️ The note does not match the selected mood. Please check again.");
        }

        String aiSuggestion = generateAISuggestion(mood);
        mood.setAiSuggestion(aiSuggestion);

        Mood saved = moodService.save(mood);
        return ResponseEntity.ok(saved);
    }

    private boolean isContradictory(Mood mood) {
        String moodName = mood.getMoodLevel() != null ? mood.getMoodLevel().getName().toLowerCase() : "";
        String note = mood.getNote() != null ? mood.getNote().toLowerCase() : "";

        List<String> sadKeywords = Arrays.asList("sad", "bad", "bored", "tired", "lonely", "exhausted", "cry", "disappointed");
        List<String> happyKeywords = Arrays.asList("happy", "joyful", "excited", "cheerful", "smile", "enthusiastic", "lucky");

        boolean moodIsPositive = moodName.contains("happy") || moodName.contains("normal");
        boolean moodIsNegative = moodName.contains("bad") || moodName.contains("sad");

        boolean noteIsPositive = happyKeywords.stream().anyMatch(note::contains);
        boolean noteIsNegative = sadKeywords.stream().anyMatch(note::contains);

        return (moodIsPositive && noteIsNegative) || (moodIsNegative && noteIsPositive);
    }

    private String generateAISuggestion(Mood mood) {
        try {
            String moodName = mood.getMoodLevel() != null ? mood.getMoodLevel().getName() : "undefined";
            String note = mood.getNote() != null ? mood.getNote() : "";

            String prompt = "The user has recorded their mood today:\n"
                    + "- Mood level: '" + moodName + "'\n"
                    + "- Note: '" + note + "'\n"
                    + "Please analyze whether the user is mentally stable or not. "
                    + "If not stable, gently give a warning. "
                    + "Regardless, provide a short motivational or positive suggestion (1-2 sentences). "
                    + "Keep the answer short for direct display to the user.";

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey); // <-- added space!


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

            if (response.getStatusCode() == HttpStatus.OK) {
                Map data = response.getBody();
                List choices = (List) data.get("choices");
                if (!choices.isEmpty()) {
                    Map choice = (Map) choices.get(0);
                    Map messageMap = (Map) choice.get("message");
                    return (String) messageMap.get("content");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unable to generate AI suggestion at the moment.";
    }

    // ✅ Keep other APIs as they are
    @GetMapping
    public ResponseEntity<List<Mood>> getAllMoods() {
        return ResponseEntity.ok(moodService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mood> getMoodById(@PathVariable Integer id) {
        Optional<Mood> mood = moodService.findById(id);
        return mood.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Mood>> getMoodsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(moodService.findByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMood(@PathVariable Integer id, @RequestBody Mood mood) {
        Users user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).build();

        mood.setId(id);
        mood.setUsers(user);

        // ✅ Check contradiction
        if (isContradictory(mood)) {
            return ResponseEntity.status(400).body("⚠️ The note does not match the selected mood. Please check again.");
        }

        String aiSuggestion = generateAISuggestion(mood);
        mood.setAiSuggestion(aiSuggestion);

        return ResponseEntity.ok(moodService.save(mood));
    }

    @GetMapping("/my/statistics")
    public ResponseEntity<Map<String, Integer>> getMoodLevelStats() {
        Users user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).build();

        List<Mood> moods = moodService.findByUserId(user.getId());
        Map<String, Integer> stats = moods.stream()
                .collect(Collectors.toMap(
                        m -> m.getDate().toString(),
                        m -> {
                            String name = m.getMoodLevel().getName();
                            return switch (name) {
                                case "Very bad" -> 1;
                                case "Bad" -> 2;
                                case "Normal" -> 3;
                                case "Happy" -> 4;
                                case "Very happy" -> 5;
                                default -> 0;
                            };
                        }
                ));
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/my/page")
    public ResponseEntity<Map<String, Object>> getMyMoodsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Users user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).build();

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<Mood> moodPage = moodService.findByUserIdPaged(user.getId(), pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("moods", moodPage.getContent());
        response.put("currentPage", moodPage.getNumber());
        response.put("totalItems", moodPage.getTotalElements());
        response.put("totalPages", moodPage.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMood(@PathVariable Integer id) {
        moodService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/date/{date}")
    public ResponseEntity<List<Mood>> getMoodsByUserAndDate(@PathVariable Integer userId, @PathVariable String date) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        List<Mood> moods = moodService.findByUserIdAndDate(userId, localDate);
        return ResponseEntity.ok(moods);
    }

    @GetMapping("/my/today")
    public ResponseEntity<Mood> getMyMoodToday() {
        Users user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).build();

        LocalDate today = LocalDate.now();
        List<Mood> moods = moodService.findByUserIdAndDate(user.getId(), today);

        if (moods.isEmpty()) {
            return ResponseEntity.ok(null); // no mood today
        } else {
            return ResponseEntity.ok(moods.get(0)); // take only 1 mood
        }
    }

    // ✅ Get logged-in user information from Spring Security
    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Email is username
        return userRepository.findAll().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }
}
