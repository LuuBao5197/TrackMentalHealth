package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.MoodLevel;
import fpt.aptech.trackmentalhealth.repository.mood.MoodLevelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mood-levels")
@CrossOrigin(origins = "*")
public class MoodLevelController {

    private final MoodLevelRepository moodLevelRepository;

    public MoodLevelController(MoodLevelRepository moodLevelRepository) {
        this.moodLevelRepository = moodLevelRepository;
    }

    @GetMapping
    public ResponseEntity<List<MoodLevel>> getAllMoodLevels() {
        return ResponseEntity.ok(moodLevelRepository.findAll());
    }
}
