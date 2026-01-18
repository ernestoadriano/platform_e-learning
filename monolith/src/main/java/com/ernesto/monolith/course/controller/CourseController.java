package com.ernesto.monolith.course.controller;

import com.ernesto.monolith.common.dto.CourseDTO;
import com.ernesto.monolith.common.dto.PurchaseDTO;
import com.ernesto.monolith.course.service.CourseService;
import com.ernesto.monolith.order.model.Purchase;
import com.ernesto.monolith.order.model.enums.PaymentMethod;
import com.ernesto.monolith.order.service.PurchaseService;
import com.ernesto.monolith.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService service;

    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/{course}/purchase")
    public PurchaseDTO purchaseCourse(@PathVariable("courseId") Long courseId, @RequestParam PaymentMethod method,
                                      User student) {
        Purchase purchase = purchaseService.createPurchase(courseId, method, student);

        return new PurchaseDTO(purchase.getStudentId(), purchase.getCourseId(), purchase.getAmount(), purchase.getStatus(), method);
    }

    @GetMapping
    public List<CourseDTO> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CourseDTO getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @PostMapping
    public CourseDTO create(@RequestBody CourseDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public CourseDTO update(@PathVariable("id") Long id, @RequestBody CourseDTO dto) {
        return service.update(id, dto);
    }

    @PutMapping("/publish/{id}")
    public CourseDTO publishCourse(@PathVariable("id") Long id) {
        return service.publishCourse(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
