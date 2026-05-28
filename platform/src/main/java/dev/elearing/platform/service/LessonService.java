package dev.elearing.platform.service;

import dev.elearing.platform.dto.LessonDTO;
import dev.elearing.platform.dto.OptionDTO;
import dev.elearing.platform.dto.QuestionDTO;
import dev.elearing.platform.model.*;
import dev.elearing.platform.model.Module;
import dev.elearing.platform.repository.LessonRepository;
import dev.elearing.platform.repository.ModuleRepository;
import dev.elearing.platform.repository.UserProgressRepository;
import dev.elearing.platform.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        return lessonRepository.findAllByModuleIdOrderByLessonOrderAsc(moduleId).stream()
                .map(this::toDTO)
                .toList();
    }

    public LessonDTO getById(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
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
        lesson.setModule(module);

        lesson = lessonRepository.save(lesson);
        return toDTO(lesson);
    }

    @Transactional
    public LessonDTO update(Long id, LessonDTO dto) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        boolean isUpdated = false;

        if (dto.getTitle() != null) {
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
            lesson.setVideoUrl(dto.getVideoUrl());
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

    @Transactional
    public void completeLesson(Long lessonId, Long userId, int score) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        saveProgress(userId, lesson, score);
    }

    private void saveProgress(Long userId, Lesson lesson, int score) {
        UserProgress progress = progressRepository.findByUserIdAndLessonId(userId, lesson.getId())
                .orElse(new UserProgress());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        progress.setUser(user);
        progress.setLesson(lesson);
        progress.setCompleted(true);
        progress.setScore(score);
        progress.setCompletedAt(LocalDateTime.now());
        progressRepository.save(progress);
    }

    public boolean isLessonCompleted(Long userId, Long lessonId) {
        return progressRepository.existsByUserIdAndLessonIdAndCompleted(userId, lessonId, true);
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
        dto.setCorrectAnswer(question.getCorrectAnswer());
        dto.setExplanation(question.getExplanation());

        if (question.getOptions() != null && !question.getOptions().isEmpty()) {
            List<OptionDTO> optionDTOS = question.getOptions().stream()
                    .map(this::convertOptionTodDTO)
                    .toList();
            dto.setOptions(optionDTOS);
        }
        return dto;
    }

    private OptionDTO convertOptionTodDTO(Option option) {
        OptionDTO dto = new OptionDTO();
        dto.setId(option.getId());
        dto.setText(option.getText());
        dto.setOptionOrder(option.getOptionOrder());
        return dto;
    }
}