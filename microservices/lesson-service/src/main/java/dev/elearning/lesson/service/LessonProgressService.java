package dev.elearning.lesson.service;

import dev.elearning.lesson.dto.request.LessonProgressRequest;
import dev.elearning.lesson.dto.response.LessonProgressResponse;
import dev.elearning.lesson.dto.response.LessonResponse;
import dev.elearning.lesson.model.LessonProgress;
import dev.elearning.lesson.repository.LessonProgressRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LessonProgressService {

    @Autowired
    private LessonProgressRepository progressRepository;

    @Autowired
    private LessonService lessonService;

    public LessonProgressResponse getLessonProgress(Long userId, Long lessonId) {
        return progressRepository.findByUserIdAndLessonId(userId, lessonId)
                .map(this::toResponse)
                .orElse(null);
    }

    public List<LessonProgressResponse> getUserCompletedLessons(Long userId) {
        return progressRepository.findAllByUserIdAndIsCompletedTrue(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<LessonProgressResponse> getModuleProgress(Long userId, Long moduleId) {
        return progressRepository.findAllByUserIdAndModuleId(userId, moduleId).stream()
                .map(this::toResponse)
                .toList();
    }

    public Long countCompletedLessonsInModule(Long userId, Long moduleId) {
        return progressRepository.countCompletedLessonsInModule(userId, moduleId);
    }

    public boolean isLessonCompleted(Long userId, Long lessonId) {
        return progressRepository.existsByUserIdAndLessonIdAndIsCompletedTrue(userId, lessonId);
    }

    @Transactional
    public LessonProgressResponse completeLesson(LessonProgressRequest request) {
        if (!lessonService.existsById(request.getLessonId())) {
            throw new RuntimeException("Lesson not found: " + request.getLessonId());
        }

        LessonProgress progress = progressRepository.findByUserIdAndLessonId(request.getUserId(), request.getLessonId())
                .orElse(new LessonProgress());

        progress.setUserId(request.getUserId());
        progress.setLessonId(request.getLessonId());
        progress.setIsCompleted(true);
        progress.setCompletedAt(LocalDateTime.now());

        progress = progressRepository.save(progress);

        return toResponse(progress);
    }

    @Transactional
    public void resetLessonProgress(Long userId, Long lessonId) {
        progressRepository.findByUserIdAndLessonId(userId, lessonId)
                .ifPresent(progressRepository::delete);
    }

    private LessonProgressResponse toResponse(LessonProgress progress) {
        LessonProgressResponse response = new LessonProgressResponse();
        response.setId(progress.getId());
        response.setUserId(progress.getUserId());
        response.setLessonId(progress.getLessonId());
        response.setIsCompleted(progress.getIsCompleted());
        response.setScore(progress.getScore());
        response.setCompletedAt(progress.getCompletedAt());
        response.setLastAccessedAt(progress.getLastAccessedAt());

        return response;
    }
}
