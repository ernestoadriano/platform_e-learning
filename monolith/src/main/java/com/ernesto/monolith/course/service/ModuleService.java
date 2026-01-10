package com.ernesto.monolith.course.service;

import com.ernesto.monolith.common.dto.CreateModuleDTO;
import com.ernesto.monolith.common.dto.ModuleDTO;
import com.ernesto.monolith.common.dto.UpdateModuleDTO;
import com.ernesto.monolith.common.exception.BusinessException;
import com.ernesto.monolith.course.model.Module;
import com.ernesto.monolith.course.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;


    public List<ModuleDTO> getByCourseId(Long courseId) {
        List<Module> modules = moduleRepository.findAllByCourseId(courseId);
        List<ModuleDTO> modulesList = new ArrayList<>();
        for (Module module : modules) {
            modulesList.add(toDTO(module));
        }

        return modulesList;
    }

    public ModuleDTO create(CreateModuleDTO dto) {
        List<Module> modules = moduleRepository.findAllByCourseId(dto.getCourseId());
        int orderIndex = modules.isEmpty()
                ? 1
                : modules.stream()
                .mapToInt(Module::getOrderIndex)
                .max()
                .getAsInt() + 1;
        Module module = new Module();
        module.setCourseId(dto.getCourseId());
        module.setTitle(dto.getTitle());
        module.setOrderIndex(orderIndex);
        module = moduleRepository.save(module);
        return toDTO(module);
    }

    public ModuleDTO update(Long id, UpdateModuleDTO dto) {
        Module module = getModuleById(id);
        module.setTitle(dto.title());
        module = moduleRepository.save(module);
        return toDTO(module);
    }

    public void delete(Long id) {
        Module module = getModuleById(id);
        moduleRepository.delete(module);
    }

    private Module getModuleById(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Module not found"));
    }

    private ModuleDTO toDTO(Module module) {
        return new ModuleDTO(module.getId(), module.getTitle(), module.getOrderIndex());
    }
}
