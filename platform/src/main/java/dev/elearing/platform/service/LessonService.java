package dev.elearing.platform.service;

import dev.elearing.platform.dto.LessonDTO;
import dev.elearing.platform.dto.QuestionDTO;
import dev.elearing.platform.model.Lesson;
import dev.elearing.platform.model.Module;
import dev.elearing.platform.model.Question;
import dev.elearing.platform.model.User;
import dev.elearing.platform.model.UserProgress;
import dev.elearing.platform.repository.LessonRepository;
import dev.elearing.platform.repository.ModuleRepository;
import dev.elearing.platform.repository.UserProgressRepository;
import dev.elearing.platform.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private UserProgressRepository progressRepository;

    @Autowired
    private UserRepository userRepository;

    public List<LessonDTO> getAllByModule(Long moduleId) {
        return lessonRepository.findAllByModuleIdOrderByOrderAsc(moduleId).stream()
                .map(this::toDTO)
                .toList();
    }

    public LessonDTO getById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() ->  new RuntimeException("Lesson not found"));
        return toDTO(lesson);
    }

    @Transactional
    public LessonDTO createLesson(Long moduleId, LessonDTO dto) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        Lesson lesson = new Lesson();
        lesson.setTitle(dto.getTitle());
        lesson.setDescription(dto.getDescription());
        lesson.setVideoUrl(dto.getVideoUrl());
        lesson.setDuration(dto.getDuration());
        lesson.setOrder(dto.getOrder());
        lesson.setModule(dto.getModule());

        lesson = lessonRepository.save(lesson);

        return toDTO(lesson);
    }

    @Transactional
    public LessonDTO update(Long id, LessonDTO dto) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        boolean isUpdated = false;

        if (dto.getTitle() != null ) {
            lesson.setTitle(dto.getTitle());
            isUpdated = true;
        }
        if (dto.getDescription() != null) {
            lesson.setDescription(dto.getDescription());
            isUpdated = true;
        }
        if (dto.getDuration() != null) {
            lesson.setDuration(dto.getDuration());
            isUpdated = true;
        }
        if (dto.getVideoUrl() != null) {
            lesson.setDuration(dto.getDuration());
            isUpdated = true;
        }
        if (dto.getOrder() != null) {
            lesson.setOrder(dto.getOrder());
            isUpdated = true;
        }

        if (isUpdated) {
            lesson.setUpdatedAt(LocalDateTime.now());
        }

        lesson = lessonRepository.save(lesson);

        return toDTO(lesson);
    }

    @Transactional
    public void deleteLesson(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new RuntimeException("Lesson not found");
        }

        lessonRepository.deleteById(id);
    }

    /*@Transactional
    public void completedLesson(Long lessonId, Long userId, QuizAnswerRequest request) {

    }*/

    private void saveProgress(Long userId, Lesson lesson, int score) {
        UserProgress progress = progressRepository.findByUserIdAndLessonId(userId, lesson.getId())
                .orElse(new UserProgress());
        User user = userRepository.findById(userId)
                        .orElseThrow();

        progress.setUser(user);
        progress.setLesson(lesson);
        progress.setCompleted(true);
        progress.setScore(score);
        progress.setCompletedAt(LocalDateTime.now());
        progressRepository.save(progress);
    }


    private LessonDTO toDTO(Lesson lesson) {
        LessonDTO dto = new LessonDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setDescription(lesson.getDescription());
        dto.setVideoUrl(lesson.getVideoUrl());
        dto.setDuration(lesson.getDuration());
        dto.setOrder(lesson.getOrder());

        if (lesson.getQuestions() != null && !lesson.getQuestions().isEmpty()) {
            List<QuestionDTO> questions = lesson.getQuestions().stream()
                    .map(this::convertQuestionToDTO)
                    .toList();
            dto.setQuestions(questions);
        }
        return dto;
    }

    private QuestionDTO convertQuestionToDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(question.getId());
        dto.setText(question.getText());
        dto.setOptions(question.getOptions());
        dto.setCorrectAnswer(question.getCorrectAnswer());
        dto.setExplanation(question.getExplanation());
        return dto;
    }
}
