package fpt.aptech.trackmentalhealth.api;

import fpt.aptech.trackmentalhealth.dto.LessonDTO;
import fpt.aptech.trackmentalhealth.entities.Lesson;
import fpt.aptech.trackmentalhealth.entities.LessonStep;
import fpt.aptech.trackmentalhealth.service.lesson.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lesson")
public class LessonController {
    @Autowired
    LessonService lessonService;

    // API CUA LESSON //
    @GetMapping("/")
    public ResponseEntity<List<LessonDTO>> getAllLessons() {
        List<LessonDTO> lessons = lessonService.getLessonDTOs();
        return ResponseEntity.ok(lessons);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LessonDTO> getLessonById(@PathVariable Integer id) {
        LessonDTO lessonDTO = lessonService.getLessonDTOById(id);
        return ResponseEntity.ok().body(lessonDTO);
    }

    @PostMapping("/")
    public ResponseEntity<LessonDTO> createLesson(@RequestBody Lesson lesson) {
        LessonDTO lessonDTO = lessonService.createLessonDTO(lesson);
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LessonDTO> updateLesson(@PathVariable Integer id, @RequestBody Lesson lesson) {
        LessonDTO lessonDTO = lessonService.updateLessonDTO(id, lesson);
        return ResponseEntity.status(HttpStatus.OK).body(lessonDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Lesson> deleteLesson(@PathVariable Integer id) {
        lessonService.deleteLesson(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //API CUA LESSON STEP


    @GetMapping("/{id}/step")
    public ResponseEntity<List<LessonStep>> getLessonStepsByLessonId(@PathVariable Integer id) {
        List<LessonStep> lessonSteps = lessonService.getLessonStepsByLessonId(id);
        return ResponseEntity.ok().body(lessonSteps);
    }
    @GetMapping("/step/{id}")
    public ResponseEntity<LessonStep> getTestQuestionById(@PathVariable Integer id) {
        LessonStep lessonStep = lessonService.getLessonStep(id);
        return ResponseEntity.ok().body(lessonStep);
    }
    @PostMapping("/step")
    public ResponseEntity<LessonStep> createTestQuestion(@RequestBody LessonStep lessonStep) {
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.createLessonStep(lessonStep));
    }
    @PutMapping("/step/{id}")
    public ResponseEntity<LessonStep> updateTestQuestion(@PathVariable Integer id, @RequestBody LessonStep lessonStep) {
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.updateLessonStep(id, lessonStep));
    }
    @DeleteMapping("/step/{id}")
    public ResponseEntity<LessonStep> deleteTestQuestion(@PathVariable Integer id) {
        lessonService.deleteLessonStep(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
