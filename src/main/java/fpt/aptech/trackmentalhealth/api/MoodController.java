package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.Mood;
import fpt.aptech.trackmentalhealth.entities.Users;
import fpt.aptech.trackmentalhealth.repository.user.UserRepository;
import fpt.aptech.trackmentalhealth.service.mood.MoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/moods")
@CrossOrigin(origins = "*")
public class MoodController {

    private final MoodService moodService;
    private final UserRepository userRepository;

    public MoodController(MoodService moodService, UserRepository userRepository) {
        this.moodService = moodService;
        this.userRepository = userRepository;
    }

    // ✅ Lấy tất cả mood theo user đã đăng nhập
    @GetMapping("/my")
    public ResponseEntity<List<Mood>> getMyMoods() {
        Users user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).build();

        return ResponseEntity.ok(moodService.findByUserId(user.getId()));
    }

    // ✅ Tạo cảm xúc mới và tự động gán user đăng nhập
    @PostMapping
    public ResponseEntity<Mood> createMood(@RequestBody Mood mood) {
        Users user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).build();

        mood.setUsers(user); // Gán user đăng nhập vào mood
        if (mood.getDate() == null) {
            mood.setDate(LocalDate.now());
        }

        Mood saved = moodService.save(mood);
        return ResponseEntity.ok(saved);
    }

    // ✅ Các API khác vẫn giữ nguyên
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
    public ResponseEntity<Mood> updateMood(@PathVariable Integer id, @RequestBody Mood mood) {
        Users user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).build();

        mood.setId(id);
        mood.setUsers(user); // 🛠 Gán lại user cho mood
        return ResponseEntity.ok(moodService.save(mood));
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
    public ResponseEntity<List<Mood>> getMyMoodToday() {
        Users user = getCurrentUser();
        if (user == null) return ResponseEntity.status(401).build();

        LocalDate today = LocalDate.now();
        List<Mood> moods = moodService.findByUserIdAndDate(user.getId(), today);
        return ResponseEntity.ok(moods);
    }


    // ✅ Lấy thông tin user đang đăng nhập từ Spring Security
    private Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Email là username
        return userRepository.findAll().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }
}
