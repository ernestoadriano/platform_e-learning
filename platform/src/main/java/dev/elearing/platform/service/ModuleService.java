package dev.elearing.platform.service;

import dev.elearing.platform.dto.LessonDTO;
import dev.elearing.platform.dto.ModuleDTO;
import dev.elearing.platform.model.Course;
import dev.elearing.platform.model.Module;
import dev.elearing.platform.repository.CourseRepository;
import dev.elearing.platform.repository.ModuleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonService lessonService;


    public List<ModuleDTO> getAllByCourse(Long courseId) {
        // CORRIGIDO: método correto é findAllByCourseIdOrderByModuleOrderAsc
        return moduleRepository.findAllByCourseIdOrderByModuleOrderAsc(courseId).stream()
                .map(this::toDTO)
                .toList();
    }

    public ModuleDTO getById(Long id) {
        return toDTO(getModuleById(id));
    }

    @Transactional
    public ModuleDTO createModule(Long courseId, ModuleDTO dto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Module module = new Module();
        module.setTitle(dto.getTitle());
        module.setDescription(dto.getDescription());
        module.setOrder(dto.getOrder());  // order agora é moduleOrder no banco
        module.setCourse(course);
        module = moduleRepository.save(module);
        return toDTO(module);
    }

    @Transactional
    public ModuleDTO updateModule(Long id, ModuleDTO dto) {
        Module module = getModuleById(id);

        if (dto.getTitle() != null) {
            module.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            module.setDescription(dto.getDescription());
        }
        if (dto.getOrder() != null) {
            module.setOrder(dto.getOrder());
        }

        module = moduleRepository.save(module);
        return toDTO(module);
    }

    public void deleteModule(Long id) {
        Module module = getModuleById(id);
        moduleRepository.delete(module);
    }

    private Module getModuleById(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));
    }

    private ModuleDTO toDTO(Module module) {
        ModuleDTO dto = new ModuleDTO();
        dto.setId(module.getId());
        dto.setTitle(module.getTitle());
        dto.setDescription(module.getDescription());
        dto.setOrder(module.getOrder());

        if (module.getLessons() != null && !module.getLessons().isEmpty()) {
            List<LessonDTO> lessons = lessonService.getAllByModule(module.getId());
            dto.setLessons(lessons);
        }

        return dto;
    }


}