package dev.elearning.lesson.service;

import dev.elearning.lesson.dto.request.LessonRequest;
import dev.elearning.lesson.dto.response.LessonResponse;
import dev.elearning.lesson.model.Lesson;
import dev.elearning.lesson.repository.LessonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    public List<LessonResponse> getLessonsByModule(Long moduleId) {
        return lessonRepository.findAllByModuleIdOrderByLessonOrderAsc(moduleId).stream()
                .map(this::toResponse)
                .toList();
    }

    public LessonResponse getLessonById(Long id) {
        Lesson lesson = getById(id);
        return toResponse(lesson);
    }

    public LessonResponse getLessonByModuleOrder(Long moduleId, Integer lessonOrder) {
        Lesson lesson = lessonRepository.findByModuleIdAndLessonOrder(moduleId, lessonOrder)
                .orElseThrow(() -> new RuntimeException("Lesson not found for module: " + "and order: " + lessonOrder));
        return toResponse(lesson);
    }

    public LessonResponse getFirstLesson(Long moduleId) {
        Lesson lesson = lessonRepository.findTopByModuleIdOrderByLessonAsc(moduleId)
                .orElseThrow(() -> new RuntimeException("No lessons found for module: " + moduleId));
        return toResponse(lesson);
    }

    public LessonResponse getLastLesson(Long moduleId) {
        Lesson lesson = lessonRepository.findTopByModuleIdOrderByLessonDesc(moduleId)
                .orElseThrow(() -> new RuntimeException("No lessons found for module: " + moduleId));

        return toResponse(lesson);
    }

    public Long countLessonsByModule(Long moduleId) {
        return lessonRepository.countByModuleId(moduleId);
    }

    public List<LessonResponse> getFreeLessons() {
        return lessonRepository.findByIsFreeTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public LessonResponse create(LessonRequest request) {

        if (lessonRepository.findByModuleIdAndLessonOrder(request.getModuleId(), request.getLessonOrder()).isPresent()) {
            throw new RuntimeException("Lesson with order " + request.getLessonOrder() + " already exists in this module");
        }

        Lesson lesson = new Lesson();
        lesson.setTitle(request.getTitle());
        lesson.setDescription(request.getDescription());
        lesson.setModuleId(request.getModuleId());
        lesson.setVideoUrl(request.getVideoUrl());
        lesson.setDescription(request.getDescription());
        lesson.setLessonOrder(request.getLessonOrder());
        lesson.setIsFree(request.getIsFree() != null ? request.getIsFree() : false);

        lesson = lessonRepository.save(lesson);

        return toResponse(lesson);
    }

    @Transactional
    public List<LessonResponse> createLessons(List<LessonRequest> requests) {
        return requests.stream()
                .map(this::create)
                .toList();
    }

    @Transactional
    public LessonResponse updateLesson(Long id, LessonRequest request) {
        Lesson lesson = getById(id);

        boolean isUpdated = false;

        if (request.getTitle() != null) {
            lesson.setTitle(request.getTitle());
            isUpdated = true;
        }

        if (request.getDescription() != null) {
            lesson.setDescription(request.getDescription());
            isUpdated = true;
        }

        if (request.getVideoUrl() != null) {
            lesson.setVideoUrl(request.getVideoUrl());
            isUpdated = true;
        }

        if (request.getDuration() != null) {
            lesson.setDuration(request.getDuration());
            isUpdated = true;
        }

        if (request.getIsFree() != null) {
            lesson.setIsFree(request.getIsFree());
            isUpdated = true;
        }

        if (request.getLessonOrder() != null && !request.getLessonOrder().equals(lesson.getLessonOrder())) {
            if (lessonRepository.findByModuleIdAndLessonOrder(lesson.getModuleId(), request.getLessonOrder()).isPresent()) {
                throw new RuntimeException("Lesson with order " + request.getLessonOrder() + " already exists in this module");
            }
            lesson.setLessonOrder(request.getLessonOrder());
            isUpdated = true;
        }

        if (isUpdated) {
            lesson.setUpdatedAt(LocalDateTime.now());
        }
        lesson = lessonRepository.save(lesson);
        return toResponse(lesson);
    }

    @Transactional
    public void reorderLesson(Long moduleId, List<Long> lessonIds) {
        for (int i = 0; i < lessonIds.size(); i++) {
            Lesson lesson = getById(lessonIds.get(i));

            if (!lesson.getModuleId().equals(moduleId)) {
                throw new RuntimeException("Lesson " + lessonIds.get(i) + " does not belong to module " + moduleId);
            }

            lesson.setLessonOrder(i);
            lesson.setUpdatedAt(LocalDateTime.now());
            lessonRepository.save(lesson);
        }
    }

    @Transactional
    public void delete(Long id) {
        Lesson lesson = getById(id);
        lessonRepository.delete(lesson);
    }

    @Transactional
    public void deleteByModule(Long moduleId) {
        List<Lesson> lessons = lessonRepository.findAllByModuleId(moduleId);
        lessonRepository.deleteAll(lessons);
    }

    public boolean existsById(Long id) {
        return lessonRepository.existsById(id);
    }

    public boolean lessonBelongsToModule(Long lessonId, Long moduleId) {
        return lessonRepository.findById(lessonId)
                .map(lesson -> lesson.getModuleId().equals(moduleId))
                .orElse(false);
    }

    private Lesson getById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found: " + id));
    }

    private LessonResponse toResponse(Lesson lesson) {
        LessonResponse response = new LessonResponse();
        response.setId(lesson.getId());
        response.setTitle(lesson.getTitle());
        response.setDescription(lesson.getDescription());
        response.setModuleId(lesson.getModuleId());
        response.setVideoUrl(lesson.getVideoUrl());
        response.setDescription(lesson.getDescription());
        response.setLessonOrder(lesson.getLessonOrder());
        response.setIsFree(lesson.getIsFree());
        response.setCreatedAt(lesson.getCreatedAt());
        response.setUpdatedAt(lesson.getUpdatedAt());
        return response;
    }
}
