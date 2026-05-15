package dev.elearing.platform.service;

import dev.elearing.platform.repository.CourseRepository;
import dev.elearing.platform.repository.PurchaseRepository;
import dev.elearing.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ModuleService moduleService;
}
