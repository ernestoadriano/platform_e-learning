package dev.elearing.platform.service;

import dev.elearing.platform.repository.CourseRepository;
import dev.elearing.platform.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonService lessonService;
}
