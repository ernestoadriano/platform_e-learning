package dev.elearning.course.service;

import dev.elearning.course.dto.request.ModuleRequest;
import dev.elearning.course.dto.response.ModuleResponse;
import dev.elearning.course.model.Course;
import dev.elearning.course.model.Module;
import dev.elearning.course.repository.CourseRepository;
import dev.elearning.course.repository.ModuleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<ModuleResponse> getModulesByCourse(Long courseId) {
        return moduleRepository.findAllByCourseIdOrderByModuleOrderAsc(courseId).stream()
                .map(this::toResponse)
                .toList();
    }

    public ModuleResponse getModuleById(Long id) {
        Module module = getById(id);
        return toResponse(module);
    }

    @Transactional
    public ModuleResponse create(Long courseId, ModuleRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseId));

        Module module = new Module();
        module.setTitle(request.getTitle());
        module.setDescription(request.getDescription());
        module.setCourse(course);
        module.setModuleOrder(request.getModuleOrder());

        module = moduleRepository.save(module);
        return toResponse(module);
    }

    @Transactional
    public ModuleResponse update(Long id, ModuleRequest request) {
        Module module = getById(id);

        boolean isUpdated = false;

        if (request.getTitle() != null) {
            module.setTitle(request.getTitle());
            isUpdated = true;
        }

        if (request.getDescription() != null) {
            module.setDescription(request.getDescription());
            isUpdated = true;
        }

        if (request.getModuleOrder() != null ) {
            module.setModuleOrder(request.getModuleOrder());
            isUpdated = true;
        }

        if (isUpdated)
            module.setUpdatedAt(LocalDateTime.now());

        module = moduleRepository.save(module);

        return toResponse(module);
    }

    @Transactional
    public void delete(Long id) {
        Module module = getById(id);
        moduleRepository.delete(module);
    }

    @Transactional
    public void reorderModules(Long courseId, List<Long> moduleIds) {
        for (int i = 0; i < moduleIds.size(); i++) {
            Module module = getById(moduleIds.get(i));
            module.setModuleOrder(i);
            module.setUpdatedAt(LocalDateTime.now());
            moduleRepository.save(module);
        }
    }

    private Module getById(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found: " + id));
    }

    private ModuleResponse toResponse(Module module) {
        ModuleResponse response = new ModuleResponse();
        response.setId(module.getId());
        response.setTitle(module.getTitle());
        response.setDescription(module.getDescription());
        response.setCourseId(module.getCourse().getId());
        response.setModuleOrder(module.getModuleOrder());
        response.setCreatedAt(module.getCreatedAt());
        response.setUpdatedAt(module.getUpdatedAt());
        return response;
    }
}
