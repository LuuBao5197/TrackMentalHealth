package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.dto.LessonDto;
import fpt.aptech.trackmentalhealth.entities.Lesson;
import fpt.aptech.trackmentalhealth.service.lesson.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @GetMapping
    public ResponseEntity<List<LessonDto>> getAllLessons() {
        List<LessonDto> lessons = lessonService.getAllLessons();
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonDto> getLessonById(@PathVariable Integer id) {
        try {
            LessonDto dto = lessonService.getLessonById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> createOrUpdate(@RequestBody LessonDto dto) {
        try {
            Lesson savedLesson = lessonService.createOrUpdateLesson(dto);
            return ResponseEntity.ok(savedLesson);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
