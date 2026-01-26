package com.org.course_service.controller;

import com.org.course_service.dto.CreateModuleDTO;
import com.org.course_service.dto.ModuleDTO;
import com.org.course_service.dto.UpdateModuleDTO;
import com.org.course_service.model.Module;
import com.org.course_service.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    @Autowired
    private ModuleService service;

    @GetMapping("/{courseId}")
    public List<ModuleDTO> getByCourseId(@PathVariable("courseId") Long courseId) {
        List<Module> modules = service.getAllByCourseId(courseId);
        List<ModuleDTO> moduleDTOS = new ArrayList<>();
        for (Module module : modules) {
            moduleDTOS.add(toDTO(module));
        }
        return moduleDTOS;
    }

    @PostMapping
    public ModuleDTO create(@RequestBody CreateModuleDTO dto) {
        return toDTO(service.create(dto));
    }

    @PutMapping("/{id}")
    public ModuleDTO update(@PathVariable("id") Long id, @RequestBody UpdateModuleDTO dto) {
        return toDTO(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

    private ModuleDTO toDTO(Module module) {
        return new ModuleDTO(module.getId(), module.getTitle(), module.getOrderIndex());
    }
}
