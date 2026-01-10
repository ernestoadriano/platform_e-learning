package com.ernesto.monolith.course.controller;

import com.ernesto.monolith.common.dto.CreateModuleDTO;
import com.ernesto.monolith.common.dto.ModuleDTO;
import com.ernesto.monolith.common.dto.UpdateModuleDTO;
import com.ernesto.monolith.course.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    @Autowired
    private ModuleService service;

    @GetMapping("/{courseId}")
    public List<ModuleDTO> getByCourseId(@PathVariable("courseId") Long courseId) {
        return service.getByCourseId(courseId);
    }

    @PostMapping
    public ModuleDTO create(@RequestBody CreateModuleDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public ModuleDTO update(@PathVariable("id") Long id, @RequestBody UpdateModuleDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
