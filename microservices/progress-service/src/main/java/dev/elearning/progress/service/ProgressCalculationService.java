package dev.elearning.progress.service;

import dev.elearning.progress.client.CourseClient;
import dev.elearning.progress.client.LessonClient;
import dev.elearning.progress.client.QuizClient;
import dev.elearning.progress.dto.response.CourseProgressResponse;
import dev.elearning.progress.dto.response.LessonResponse;
import dev.elearning.progress.dto.response.ModuleProgressResponse;
import dev.elearning.progress.model.ModuleProgress;
import dev.elearning.progress.model.UserCourseProgress;
import dev.elearning.progress.repository.ModuleProgressRepository;
import dev.elearning.progress.repository.UserCourseProgressRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProgressCalculationService {

    @Autowired
    private UserCourseProgressRepository courseProgressRepository;

    @Autowired
    private ModuleProgressRepository moduleProgressRepository;

    private CourseClient courseClient;
    private LessonClient lessonClient;
    private QuizClient quizClient;

    @Transactional
    public CourseProgressResponse calculateCourseProgress(Long userId, Long courseId) {
        UserCourseProgress courseProgress = courseProgressRepository.findByUserIdAndCourseId(userId, courseId)
                .orElse(new UserCourseProgress());
        courseProgress.setUserId(userId);
        courseProgress.setCourseId(courseId);

        var modules = courseClient.getModulesByCourseId(courseId);

        int totalLessonsInCourse = 0;
        int completedLessonInCourse = 0;
        List<ModuleProgressResponse> moduleProgressList = new ArrayList<>();

        for (var module : modules) {
            ModuleProgressResponse moduleProgress = calculateModuleProgress(userId, module.getModuleId());

            moduleProgressList.add(moduleProgress);

            totalLessonsInCourse += moduleProgress.getTotalLessons();
            completedLessonInCourse += moduleProgress.getCompletedLessons();
        }

        double percentage = totalLessonsInCourse > 0 ? (double) (completedLessonInCourse * 100) / totalLessonsInCourse : 0;

        courseProgress.setTotalLessons(totalLessonsInCourse);
        courseProgress.setCompletedLessons(completedLessonInCourse);
        courseProgress.setPercentage(percentage);

        courseProgress.setIsCompleted(percentage == 100);

        if (percentage == 100 && courseProgress.getCompletedAt() == null) {
            courseProgress.setCompletedAt(LocalDateTime.now());
        }

        courseProgressRepository.save(courseProgress);

        return toCourseResponse(courseProgress, moduleProgressList);
    }

    @Transactional
    public ModuleProgressResponse calculateModuleProgress(Long userId, Long moduleId) {
        ModuleProgress moduleProgress = moduleProgressRepository.findByUserIdAndModuleId(userId, moduleId)
                .orElse(new ModuleProgress());

        moduleProgress.setUserId(userId);
        moduleProgress.setModuleId(moduleId);

        Long totalLessons = lessonClient.countLessonsByModule(moduleId);

        moduleProgress.setTotalLessons(totalLessons.intValue());

        Long completeLessons = getCompletedLessonCount(userId, moduleId);

        moduleProgress.setCompletedLessons(completeLessons.intValue());

        double percentage = totalLessons > 0 ? (double) ((completeLessons * 100) / totalLessons) : 0;
        moduleProgress.setPercentage(percentage);
        moduleProgress.setIsCompleted(percentage == 100);

        if (percentage == 100 && moduleProgress.getCompletedAt() == null) {
            moduleProgress.setCompletedAt(LocalDateTime.now());
        }

        moduleProgressRepository.save(moduleProgress);

        return toModuleResponse(moduleProgress);
    }

    @Transactional
    public void updateAfterLessonCompletion(Long userId, Long moduleId, Long courseId, Double quizScore) {

        if (quizScore != null && quizScore >= 69.5) {
            ModuleProgress moduleProgress = moduleProgressRepository.findByUserIdAndModuleId(userId, moduleId)
                    .orElse(new ModuleProgress());
            moduleProgress.setUserId(userId);
            moduleProgress.setModuleId(moduleId);

            int completedLessons = (moduleProgress.getCompletedLessons() != null ? moduleProgress.getCompletedLessons() : 0) + 1;
            moduleProgress.setCompletedLessons(completedLessons);

            int totalLessons = moduleProgress.getTotalLessons() != null ? moduleProgress.getTotalLessons() : 0;


            double percentage = totalLessons > 0 ? (completedLessons * 100.0) / totalLessons : 0.0;
            moduleProgress.setPercentage(percentage);
            moduleProgress.setIsCompleted(Math.abs(percentage - 100.0) < 0.01);

            moduleProgressRepository.save(moduleProgress);

            calculateCourseProgress(userId, courseId);
        }
    }

    @Transactional
    public void updateModuleExamResult(Long userId, Long moduleId, Boolean passed, Double score) {

        ModuleProgress moduleProgress = moduleProgressRepository.findByUserIdAndModuleId(userId, moduleId)
                .orElseGet(() -> {
                    ModuleProgress newProgress = new ModuleProgress();
                    newProgress.setUserId(userId);
                    newProgress.setModuleId(moduleId);
                    return newProgress;
                });

        moduleProgress.setExamPassed(passed);

        if (score != null) {
            moduleProgress.setExamScore(score);
        }

        if (passed) {
            moduleProgress.setIsCompleted(true);
            moduleProgress.setCompletedAt(LocalDateTime.now());

            Long totalLessons = getTotalLessonCount(moduleId);
            Long completedLessons = getCompletedLessonCount(userId, moduleId);

            moduleProgress.setTotalLessons(totalLessons != null ? totalLessons.intValue() : 0);

            moduleProgress.setCompletedLessons(completedLessons != null ? completedLessons.intValue() : 0);

            moduleProgress.setPercentage(100.0);
            log.info("Module {} completed by user {} after passing exam", moduleId, userId);
        }

        moduleProgressRepository.save(moduleProgress);

        Long courseId = getCourseIdByModuleId(moduleId);
        if (courseId != null) {
            calculateCourseProgress(userId, courseId);
        }
    }

    private Long getCourseIdByModuleId(Long moduleId) {
        try {
            return courseClient.getCourseIdByModuleId(moduleId);
        } catch (Exception e) {
            log.error("Error getting course ID {}", moduleId, e);
            return null;
        }
    }


    private Long getCompletedLessonCount(Long userId, Long moduleId) {
        try {
            List<LessonResponse> lessons = lessonClient.getLessonsByModule(moduleId);

            if (lessons == null || lessons.isEmpty()) {
                return 0L;
            }

            long completedCount = 0;

            for (LessonResponse lesson : lessons) {
                Boolean hasPassed = quizClient.hasUserPassedQuiz(userId, lesson.getId());

                if (hasPassed != null && hasPassed) {
                    completedCount++;
                }
            }
            log.debug("User {} completed {} out of {} lessons in module {}", userId, completedCount, lessons.size(), moduleId);
            return completedCount;
        } catch (Exception e) {
            log.error("Error getting completed lesson count for user: {}, module: {}", userId, moduleId, e);
            return 0L;
        }
    }

    private Long getTotalLessonCount(Long moduleId) {
        try {
            return lessonClient.countLessonsByModule(moduleId);
        } catch (Exception e) {
            log.error("Error getting total lesson count for module: {}", moduleId, e);
            return 0L;
        }
    }
    private CourseProgressResponse toCourseResponse(UserCourseProgress progress, List<ModuleProgressResponse> modules) {
        CourseProgressResponse response = new CourseProgressResponse();

        response.setCourseId(progress.getCourseId());
        response.setCompletedLessons(progress.getCompletedLessons());
        response.setTotalLessons(progress.getTotalLessons());
        response.setPercentage(progress.getPercentage());
        response.setIsCompleted(progress.getIsCompleted());
        response.setLastAccessedAt(progress.getLastAccessedAt());
        response.setModules(modules);

        return response;
    }

    private ModuleProgressResponse toModuleResponse(ModuleProgress progress) {
        ModuleProgressResponse response = new ModuleProgressResponse();

        response.setModuleId(progress.getModuleId());
        response.setCompletedLessons(progress.getCompletedLessons());
        response.setTotalLessons(progress.getTotalLessons());
        response.setPercentage(progress.getPercentage());
        response.setIsCompleted(progress.getIsCompleted());
        response.setExamPassed(progress.getExamPassed());
        response.setExamScore(progress.getExamScore());

        return response;
    }
}
