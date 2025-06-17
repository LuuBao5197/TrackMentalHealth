package fpt.aptech.trackmentalhealth.api;



import fpt.aptech.trackmentalhealth.entities.Mood;
import fpt.aptech.trackmentalhealth.service.mood.MoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/moods")
@CrossOrigin(origins = "*")
public class MoodController {

    private final MoodService moodService;

    public MoodController(MoodService moodService) {
        this.moodService = moodService;
    }

    // 🔹 GET /moods - Lấy tất cả bản ghi mood
    @GetMapping
    public ResponseEntity<List<Mood>> getAllMoods() {
        return ResponseEntity.ok(moodService.findAll());
    }

    // 🔹 GET /moods/{id} - Lấy 1 bản ghi mood theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Mood> getMoodById(@PathVariable Integer id) {
        Optional<Mood> mood = moodService.findById(id);
        return mood.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 GET /moods/user/{userId} - Lấy tất cả moods theo user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Mood>> getMoodsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(moodService.findByUserId(userId));
    }

    // 🔹 POST /moods - Tạo mới mood
    @PostMapping
    public ResponseEntity<Mood> createMood(@RequestBody Mood mood) {
        return ResponseEntity.ok(moodService.save(mood));
    }

    // 🔹 PUT /moods/{id} - Cập nhật mood
    @PutMapping("/{id}")
    public ResponseEntity<Mood> updateMood(@PathVariable Integer id, @RequestBody Mood mood) {
        mood.setId(id);
        return ResponseEntity.ok(moodService.save(mood));
    }

    // 🔹 DELETE /moods/{id} - Xoá mood
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMood(@PathVariable Integer id) {
        moodService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    // 🔹 GET /moods/user/{userId}/date/{date} - Lấy mood theo userId và ngày
    @GetMapping("/user/{userId}/date/{date}")
    public ResponseEntity<List<Mood>> getMoodsByUserAndDate(@PathVariable Integer userId, @PathVariable String date) {
        LocalDate localDate;
        try {
            localDate = LocalDate.parse(date); // format: yyyy-MM-dd
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        List<Mood> moods = moodService.findByUserIdAndDate(userId, localDate);
        return ResponseEntity.ok(moods);
    }

}

