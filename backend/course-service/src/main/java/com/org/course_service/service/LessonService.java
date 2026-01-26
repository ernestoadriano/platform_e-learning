package com.org.course_service.service;

import com.org.course_service.dto.CreateLessonDTO;
import com.org.course_service.dto.UpdateLessonDTO;
import com.org.course_service.model.Lesson;
import com.org.course_service.model.Module;
import com.org.course_service.repository.LessonRepsoitory;
import com.org.course_service.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonService {

    @Autowired
    private LessonRepsoitory lessonRepsoitory;

    @Autowired
    private ModuleRepository moduleRepository;

    public List<Lesson> getAllByModuleId(Long moduleId) {
        return lessonRepsoitory.findAllByModuleId(moduleId);
    }

    public Lesson create(CreateLessonDTO dto) {
        Module module = moduleRepository.findById(dto.moduleId())
                .orElseThrow(() -> new RuntimeException("Module not found"));

        if (lessonRepsoitory.existsByTitle(dto.title())) {
            throw new RuntimeException("Lesson's title already exists");
        }

        if (lessonRepsoitory.existsByVideoUrl(dto.videoUrl())) {
            throw new RuntimeException("Lesson's video url already exists");
        }

        List<Lesson> lessons = lessonRepsoitory.findAllByModuleId(module.getId());
        int orderIndex = lessons.isEmpty()
                ? 1
                : lessons.stream()
                .mapToInt(Lesson::getOrderIndex)
                .max()
                .getAsInt() + 1;

        Lesson lesson = new Lesson();

        lesson.setTitle(dto.title());
        lesson.setModule(module);
        lesson.setVideoUrl(dto.videoUrl());
        lesson.setOrderIndex(orderIndex);

        return lessonRepsoitory.save(lesson);
    }

    public Lesson update(Long id, UpdateLessonDTO dto) {
        Lesson lesson = getById(id);

        if (dto.videoUrl() != null) {
            if (!lessonRepsoitory.existsByVideoUrlAndIdNot(dto.videoUrl(), id)) {
                throw new RuntimeException("Lesson's video url already exists");
            }

            lesson.setVideoUrl(dto.videoUrl());
        }

        if (dto.title() != null) {
            if (!lessonRepsoitory.existsByTitleAndIdNot(dto.title(), id)) {
                throw new RuntimeException("Lesson's title already exists");
            }
        }
        return lessonRepsoitory.save(lesson);
    }

    public Lesson getById(Long id) {
        return lessonRepsoitory.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
    }

    public void delete(Long id) {
        Lesson lesson = getById(id);
        lessonRepsoitory.delete(lesson);
    }
}
