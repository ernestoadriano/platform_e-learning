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

    /*
     * Verify if the user can access lesson
     * Rule:
     */

    public boolean canAccessLesson(Long userId, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        List<Lesson> moduleLessons = lessonRepository.findAllByModuleIdOrderByOrderAsc(lesson.getModule().getId());

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

        List<Lesson> previousModuleLessons = lessonRepository.findAllByModuleIdOrderByOrderAsc(
            getPreviousModuleId(module.getId(), module.getOrder()));


        for (Lesson lesson : previousModuleLessons) {
            if (!progressRepository.existsByUserIdAndLessonIdAndCompleted(userId, lesson.getId(), true)) {
                return false;
            }
        }
        return true;
    }

    private Long getPreviousModuleId(Long courseId, Integer currentOrder) {
        Module previousModule = moduleRepository.findByCourseIdAndOrder(courseId, currentOrder - 1)
                .orElseThrow();
        return previousModule.getId();
    }

    public ProgressResponse getCourseProgress(Long userId, Long courseId) {
        List<Lesson> lessons = lessonRepository.findAllByCourseId(courseId);
        List<UserProgress> userProgresses = progressRepository.findAllByUserIdAndCourseId(userId, courseId);

        Map<Long, Boolean> lessonStatus = new HashMap<>();
        Map<Long, Integer> lessonScores = new HashMap<>();

        for (UserProgress progress : userProgresses) {
            lessonStatus.put(progress.getLesson().getId(), progress.getCompleted());
            lessonScores.put(progress.getLesson().getId(), progress.getScore());
        }

        long completedCount = lessonStatus.values().stream().filter(v -> v).count();
        int percentage = lessons.isEmpty() ? 0 : (int) ((completedCount * 100) / lessons.size());

        ProgressResponse response = new ProgressResponse();
        response.setCourseId(courseId);
        response.setCompletedLessons((int) completedCount);
        response.setTotalLessons(lessons.size());
        response.setPercentage(percentage);
        response.setLessonStatus(lessonStatus);
        response.setLessonsScores(lessonScores);

        return response;
    }

    public Map<String, Object> getUserStatus(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalLessonsCompleted", progressRepository.countCompletedLessonsByUserId(userId));
        return stats;
    }
}
