package com.org.course_service.service;

import com.org.course_service.dto.CreateModuleDTO;
import com.org.course_service.dto.UpdateModuleDTO;
import com.org.course_service.model.Course;
import com.org.course_service.model.Module;
import com.org.course_service.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseService courseService;

    public List<Module> getAllByCourseId(Long courseId) {
        return moduleRepository.findAllByCourseId(courseId);
    }

    public Module getById(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));
    }

    public Module create(CreateModuleDTO dto) {
        Course course = courseService.getById(dto.getCourseId());
        List<Module> modules = moduleRepository.findAllByCourseId(course.getId());

        int orderIndex = modules.isEmpty()
                ? 1
                : modules.stream()
                .mapToInt(Module::getOrderIndex)
                .max()
                .getAsInt() + 1;

        Module module = new Module();
        module.setCourse(course);
        module.setTitle(dto.getTitle());
        module.setOrderIndex(orderIndex);

        return moduleRepository.save(module);
    }

    public Module update(Long id, UpdateModuleDTO dto) {
        Module module = getById(id);

        module.setTitle(dto.title());

        return moduleRepository.save(module);
    }

    public void delete(Long id) {
        Module module = getById(id);
        moduleRepository.delete(module);
    }
}
