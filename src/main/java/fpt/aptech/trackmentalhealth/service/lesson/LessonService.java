package fpt.aptech.trackmentalhealth.service.lesson;

import fpt.aptech.trackmentalhealth.dto.LessonDto;
import fpt.aptech.trackmentalhealth.dto.LessonStepDto;
import fpt.aptech.trackmentalhealth.entities.ContentCreator;
import fpt.aptech.trackmentalhealth.entities.Lesson;
import fpt.aptech.trackmentalhealth.entities.LessonStep;
import fpt.aptech.trackmentalhealth.repository.contentcreator.ContentCreatorRepository;
import fpt.aptech.trackmentalhealth.repository.lesson.LessonRepository;
import fpt.aptech.trackmentalhealth.repository.lesson.LessonStepRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonStepRepository lessonStepRepository;

    @Autowired
    private ContentCreatorRepository contentCreatorRepository;

    @Autowired
    private ContentModerationService contentModerationService;

    public List<LessonDto> getAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();

        return lessons.stream().map(lesson -> {
            LessonDto dto = new LessonDto();
            dto.setId(lesson.getId());
            dto.setTitle(lesson.getTitle());
            dto.setDescription(lesson.getDescription());
            dto.setStatus(lesson.getStatus());
            dto.setPhoto(lesson.getPhoto());
            dto.setCreatedAt(lesson.getCreatedAt());
            dto.setUpdatedAt(lesson.getUpdatedAt());
            dto.setCategory(lesson.getCategory());

            dto.setCreatedBy(lesson.getCreatedBy() != null ? lesson.getCreatedBy().getId() : null);

            List<LessonStepDto> steps = lesson.getLessonSteps().stream().map(step -> {
                LessonStepDto stepDto = new LessonStepDto();
                stepDto.setId(step.getId());
                stepDto.setStepNumber(step.getStepNumber());
                stepDto.setTitle(step.getTitle());
                stepDto.setContent(step.getContent());
                stepDto.setMediaType(step.getMediaType());
                stepDto.setMediaUrl(step.getMediaUrl());
                return stepDto;
            }).collect(Collectors.toList());

            dto.setLessonSteps(steps);
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public Lesson createOrUpdateLesson(LessonDto dto) {
        // Kiểm tra kết nối API trước khi xử lý
        contentModerationService.checkApiConnection();

        // Kiểm tra nội dung nhạy cảm trong title, description và các bước
        if (dto.getTitle() != null && contentModerationService.isSensitiveContent(dto.getTitle())) {
            throw new RuntimeException("Lesson title contains sensitive content.");
        }
        if (dto.getDescription() != null && contentModerationService.isSensitiveContent(dto.getDescription())) {
            throw new RuntimeException("Lesson description contains sensitive content.");
        }
        if (dto.getLessonSteps() != null) {
            for (LessonStepDto stepDto : dto.getLessonSteps()) {
                if (stepDto.getTitle() != null && contentModerationService.isSensitiveContent(stepDto.getTitle())) {
                    throw new RuntimeException("Step title contains sensitive content.");
                }
                if (stepDto.getContent() != null && contentModerationService.isSensitiveContent(stepDto.getContent())) {
                    throw new RuntimeException("Step content contains sensitive content.");
                }
            }
        }

        Lesson lesson = (dto.getId() != null)
                ? lessonRepository.findById(dto.getId()).orElse(new Lesson())
                : new Lesson();

        lesson.setTitle(dto.getTitle());
        lesson.setDescription(dto.getDescription());
        lesson.setStatus(dto.getStatus());
        lesson.setPhoto(dto.getPhoto());
        lesson.setCreatedAt(dto.getCreatedAt());
        lesson.setUpdatedAt(dto.getUpdatedAt());
        lesson.setCategory(dto.getCategory());

        if (dto.getCreatedBy() != null) {
            ContentCreator creator = contentCreatorRepository.findById(dto.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("ContentCreator not found with ID: " + dto.getCreatedBy()));
            lesson.setCreatedBy(creator);
        }

        lesson = lessonRepository.save(lesson);

        // If updating, remove old steps
        if (dto.getId() != null) {
            lessonStepRepository.deleteByLessonId(lesson.getId());
        }

        if (dto.getLessonSteps() != null) {
            for (LessonStepDto stepDto : dto.getLessonSteps()) {
                LessonStep step = new LessonStep();
                step.setLesson(lesson);
                step.setStepNumber(stepDto.getStepNumber());
                step.setTitle(stepDto.getTitle());
                step.setContent(stepDto.getContent());
                step.setMediaType(stepDto.getMediaType());
                step.setMediaUrl(stepDto.getMediaUrl());
                lessonStepRepository.save(step);
            }
        }

        return lesson;
    }

    public LessonDto getLessonById(Integer id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found with ID: " + id));

        LessonDto dto = new LessonDto();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setDescription(lesson.getDescription());
        dto.setStatus(lesson.getStatus());
        dto.setPhoto(lesson.getPhoto());
        dto.setCreatedAt(lesson.getCreatedAt());
        dto.setUpdatedAt(lesson.getUpdatedAt());
        dto.setCategory(lesson.getCategory());

        dto.setCreatedBy(lesson.getCreatedBy() != null ? lesson.getCreatedBy().getId() : null);

        List<LessonStepDto> steps = lesson.getLessonSteps().stream().map(step -> {
            LessonStepDto stepDto = new LessonStepDto();
            stepDto.setId(step.getId());
            stepDto.setStepNumber(step.getStepNumber());
            stepDto.setTitle(step.getTitle());
            stepDto.setContent(step.getContent());
            stepDto.setMediaType(step.getMediaType());
            stepDto.setMediaUrl(step.getMediaUrl());
            return stepDto;
        }).collect(Collectors.toList());

        dto.setLessonSteps(steps);
        return dto;
    }



    public List<LessonStepDto> getLessonStepsByLessonId(Integer lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found with ID: " + lessonId));

        return lesson.getLessonSteps().stream().map(step -> {
            LessonStepDto stepDto = new LessonStepDto();
            stepDto.setId(step.getId());
            stepDto.setStepNumber(step.getStepNumber());
            stepDto.setTitle(step.getTitle());
            stepDto.setContent(step.getContent());
            stepDto.setMediaType(step.getMediaType());
            stepDto.setMediaUrl(step.getMediaUrl());
            return stepDto;
        }).collect(Collectors.toList());
    }

    public LessonStepDto getLessonStepById(Integer lessonId, Integer stepId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found with ID: " + lessonId));

        LessonStep step = lesson.getLessonSteps().stream()
                .filter(s -> s.getId().equals(stepId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("LessonStep not found with ID: " + stepId + " in Lesson: " + lessonId));

        LessonStepDto stepDto = new LessonStepDto();
        stepDto.setId(step.getId());
        stepDto.setStepNumber(step.getStepNumber());
        stepDto.setTitle(step.getTitle());
        stepDto.setContent(step.getContent());
        stepDto.setMediaType(step.getMediaType());
        stepDto.setMediaUrl(step.getMediaUrl());

        return stepDto;
    }

    public List<LessonDto> getLessonsByCreatorId(Integer creatorId) {
        List<Lesson> lessons = lessonRepository.findByCreatedBy_Id(creatorId);

        return lessons.stream().map(lesson -> {
            LessonDto dto = new LessonDto();
            dto.setId(lesson.getId());
            dto.setTitle(lesson.getTitle());
            dto.setDescription(lesson.getDescription());
            dto.setStatus(lesson.getStatus());
            dto.setPhoto(lesson.getPhoto());
            dto.setCreatedAt(lesson.getCreatedAt());
            dto.setUpdatedAt(lesson.getUpdatedAt());
            dto.setCategory(lesson.getCategory());
            dto.setCreatedBy(lesson.getCreatedBy() != null ? lesson.getCreatedBy().getId() : null);

            List<LessonStepDto> steps = lesson.getLessonSteps().stream().map(step -> {
                LessonStepDto stepDto = new LessonStepDto();
                stepDto.setId(step.getId());
                stepDto.setStepNumber(step.getStepNumber());
                stepDto.setTitle(step.getTitle());
                stepDto.setContent(step.getContent());
                stepDto.setMediaType(step.getMediaType());
                stepDto.setMediaUrl(step.getMediaUrl());
                return stepDto;
            }).collect(Collectors.toList());

            dto.setLessonSteps(steps);
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public LessonDto approveLesson(Integer id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found with ID: " + id));

        lesson.setStatus("true"); // duyệt bài -> status = true
        lessonRepository.save(lesson);
        return getLessonById(id); // trả lại LessonDto sau khi duyệt
    }

}