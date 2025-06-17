package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.entities.Diary;
import fpt.aptech.trackmentalhealth.service.diary.DiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diaries")
@CrossOrigin(origins = "*")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @GetMapping
    public List<Diary> getAllDiaries() {
        return diaryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Diary> getDiaryById(@PathVariable Integer id) {
        return diaryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<Diary> getDiariesByUserId(@PathVariable Integer userId) {
        return diaryService.findByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Diary> createDiary(@RequestBody Diary diary) {
        // Gán ngày hiện tại nếu chưa có ngày
        if (diary.getDate() == null) {
            diary.setDate(LocalDate.now());
        }
        Diary saved = diaryService.save(diary);
        return ResponseEntity.ok(saved);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Diary> updateDiary(@PathVariable Integer id, @RequestBody Diary diary) {
        return diaryService.findById(id)
                .map(existing -> {
                    diary.setId(id);
                    return ResponseEntity.ok(diaryService.save(diary));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Integer id) {
        if (diaryService.findById(id).isPresent()) {
            diaryService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}