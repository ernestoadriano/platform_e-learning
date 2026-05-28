package dev.elearing.platform.service;

import dev.elearing.platform.dto.ProgressResponse;
import dev.elearing.platform.model.Lesson;
import dev.elearing.platform.model.Module;
import dev.elearing.platform.model.UserProgress;
import dev.elearing.platform.repository.LessonRepository;
import dev.elearing.platform.repository.ModuleRepository;
import dev.elearing.platform.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProgressService {

    @Autowired
    private UserProgressRepository progressRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public boolean canAccessLesson(Long userId, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        List<Lesson> moduleLessons = lessonRepository.findAllByModuleIdOrderByLessonOrderAsc(lesson.getModule().getId());

        if (lesson.getOrder() == 0) {
            return canAccessModule(userId, lesson.getModule().getId());
        }

        Lesson previousLesson = moduleLessons.get(lesson.getOrder() - 1);
        return progressRepository.existsByUserIdAndLessonIdAndCompleted(userId, previousLesson.getId(), true);
    }

    public boolean canAccessModule(Long userId, Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        if (module.getOrder() == 0) {
            return true;
        }

        Module previousModule = moduleRepository.findByCourseIdAndModuleOrder(module.getCourse().getId(), module.getOrder() - 1)
                .orElse(null);

        if (previousModule == null) {
            return true;
        }

        List<Lesson> previousModuleLessons = lessonRepository.findAllByModuleIdOrderByLessonOrderAsc(previousModule.getId());

        for (Lesson lesson : previousModuleLessons) {
            if (!progressRepository.existsByUserIdAndLessonIdAndCompleted(userId, lesson.getId(), true)) {
                return false;
            }
        }
        return true;
    }

    public ProgressResponse getCourseProgress(Long userId, Long courseId) {
        List<Lesson> lessons = lessonRepository.findAllByCourseIdOrdered(courseId);
        List<UserProgress> userProgresses = progressRepository.findAllByUserIdAndCourseId(userId, courseId);

        Map<Long, Boolean> lessonStatus = new HashMap<>();
        Map<Long, Integer> lessonScores = new HashMap<>();

        for (UserProgress progress : userProgresses) {
            lessonStatus.put(progress.getLesson().getId(), progress.getCompleted());
            if (progress.getScore() != null) {
                lessonScores.put(progress.getLesson().getId(), progress.getScore());
            }
        }

        long completedCount = lessonStatus.values().stream().filter(v -> v != null && v).count();
        int totalLessons = lessons.size();
        int percentage = totalLessons == 0 ? 0 : (int) ((completedCount * 100) / totalLessons);

        ProgressResponse response = new ProgressResponse();
        response.setCourseId(courseId);
        response.setCompletedLessons((int) completedCount);
        response.setTotalLessons(totalLessons);
        response.setPercentage(percentage);
        response.setLessonStatus(lessonStatus);
        response.setLessonsScores(lessonScores);

        return response;
    }

    public Map<String, Object> getUserStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLessonsCompleted", progressRepository.countCompletedLessonsByUserId(userId));
        return stats;
    }
}