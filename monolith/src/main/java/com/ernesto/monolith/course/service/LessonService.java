package com.ernesto.monolith.course.service;

import com.ernesto.monolith.common.dto.CreateLessonDTO;
import com.ernesto.monolith.common.dto.LessonDTO;
import com.ernesto.monolith.common.dto.UpdateLessonDTO;
import com.ernesto.monolith.common.exception.BusinessException;
import com.ernesto.monolith.course.model.Lesson;
import com.ernesto.monolith.course.repository.LessonRepository;
import com.ernesto.monolith.course.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    public List<LessonDTO> getByModuleId(Long moduleId) {
        List<Lesson> lessons = lessonRepository.findAllByModuleId(moduleId);
        List<LessonDTO> lessonList = new ArrayList<>();
        for (Lesson lesson : lessons) {
            lessonList.add(toDTO(lesson));
        }

        return lessonList;
    }

    public LessonDTO create(CreateLessonDTO dto) {
        if (!moduleRepository.existsById(dto.moduleId())) {
            throw new BusinessException("Module not found");
        }

        if (lessonRepository.existsByTitle(dto.title())) {
            throw new BusinessException("Lesson's title already exists");
        }

        if (lessonRepository.existsByVideoUrl(dto.videoUrl())) {
            throw new BusinessException("Lesson's video url already exists");
        }

        List<Lesson> lessons = lessonRepository.findAllByModuleId(dto.moduleId());
        int orderIndex = lessons.isEmpty()
                ? 1
                : lessons.stream()
                .mapToInt(Lesson::getOrderIndex)
                .max()
                .getAsInt() + 1;

        Lesson lesson = new Lesson();

        lesson.setTitle(dto.title());
        lesson.setModuleId(dto.moduleId());
        lesson.setVideoUrl(dto.videoUrl());
        lesson.setOrderIndex(orderIndex);
        lesson = lessonRepository.save(lesson);
        return toDTO(lesson);
    }

    public LessonDTO update(Long id, UpdateLessonDTO dto) {
        Lesson lesson = getLessonById(id);
        if (dto.videoUrl() != null) {
            if (!lesson.getVideoUrl().equals(dto.videoUrl())) {
                if (lessonRepository.existsByVideoUrl(dto.videoUrl())) {
                    throw new BusinessException("Lesson's title already exists");
                }
            }
            lesson.setVideoUrl(dto.videoUrl());
        }

        if (dto.title() != null) {
            if (!lesson.getTitle().equals(dto.title())) {
                if (lessonRepository.existsByTitle(dto.title())) {
                    throw new BusinessException("Lesson's title already exists");
                }
            }
            lesson.setTitle(dto.title());
        }

        lesson = lessonRepository.save(lesson);

        return toDTO(lesson);

    }

    public void delete(Long id) {
        Lesson lesson = getLessonById(id);
        lessonRepository.delete(lesson);
    }

    private Lesson getLessonById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Lesson not found"));
    }

    private LessonDTO toDTO(Lesson lesson) {
        return new LessonDTO(lesson.getId(), lesson.getTitle(), lesson.getVideoUrl(), lesson.getOrderIndex());
    }
}
