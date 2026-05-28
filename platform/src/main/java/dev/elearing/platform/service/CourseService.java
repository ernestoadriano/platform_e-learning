package dev.elearing.platform.service;

import dev.elearing.platform.dto.CourseDTO;
import dev.elearing.platform.dto.ModuleDTO;
import dev.elearing.platform.dto.UserDTO;
import dev.elearing.platform.model.Course;
import dev.elearing.platform.model.Purchase;
import dev.elearing.platform.model.User;
import dev.elearing.platform.model.enums.PurchaseStatus;
import dev.elearing.platform.model.enums.Role;
import dev.elearing.platform.repository.CourseRepository;
import dev.elearing.platform.repository.PurchaseRepository;
import dev.elearing.platform.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private UserService userService;

    public List<CourseDTO> getAll() {
        return courseRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public CourseDTO getById(Long id) {
        Course course = getCourseById(id);
        return toDTO(course);
    }

    public List<CourseDTO> getUserCourses(Long userId) {
        return courseRepository.findCoursesByUserId(userId).stream()
                .map(this::toDTO)
                .toList();
    }

    public List<CourseDTO> getTeacherCourses(Long teacherId) {
        return courseRepository.findCoursesByTeacherId(teacherId).stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public CourseDTO createCourse(CourseDTO dto) {
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setPrice(dto.getPrice() != null ? dto.getPrice() : BigDecimal.ZERO);
        course.setImageUrl(dto.getImageUrl());

        // Adiciona teachers
        if (dto.getTeacherIds() != null && !dto.getTeacherIds().isEmpty()) {
            List<User> teachers = new ArrayList<>();
            for (Long teacherId : dto.getTeacherIds()) {
                User teacher = userRepository.findById(teacherId)
                        .orElseThrow(() -> new RuntimeException("Teacher not found: " + teacherId));
                if (teacher.getRole() != Role.TEACHER && teacher.getRole() != Role.ADMIN) {
                    throw new RuntimeException("User " + teacherId + " is not a teacher");
                }
                teachers.add(teacher);
            }
            course.setTeachers(teachers);
        }

        course.setTotalDuration(0);
        course.setEnrolledStudents(0);
        course.setCreatedAt(LocalDateTime.now());

        course = courseRepository.save(course);
        return toDTO(course);
    }

    @Transactional
    public CourseDTO updateCourse(Long id, CourseDTO dto) {
        Course course = getCourseById(id);

        boolean isUpdate = false;
        if (dto.getTitle() != null) {
            course.setTitle(dto.getTitle());
            isUpdate = true;
        }
        if (dto.getDescription() != null) {
            course.setDescription(dto.getDescription());
            isUpdate = true;
        }
        if (dto.getPrice() != null) {
            course.setPrice(dto.getPrice());
            isUpdate = true;
        }
        if (dto.getImageUrl() != null) {
            course.setImageUrl(dto.getImageUrl());
            isUpdate = true;
        }
        if (dto.getTotalDuration() != null) {
            course.setTotalDuration(dto.getTotalDuration());
            isUpdate = true;
        }

        if (isUpdate) {
            course.setUpdatedAt(LocalDateTime.now());
        }

        course = courseRepository.save(course);
        return toDTO(course);
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course course = getCourseById(id);
        courseRepository.delete(course);
    }

    @Transactional
    public void purchaseCourse(Long courseId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = getCourseById(courseId);

        if (purchaseRepository.existsByUserIdAndCourseId(userId, courseId)) {
            throw new RuntimeException("Course already purchased");
        }

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setCourse(course);
        purchase.setAmount(course.getPrice());
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setStatus(PurchaseStatus.COMPLETED);

        course.setEnrolledStudents(course.getEnrolledStudents() + 1);
        courseRepository.save(course);
        purchaseRepository.save(purchase);
    }

    private Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public boolean hasPurchased(Long userId, Long courseId) {
        return purchaseRepository.existsByUserIdAndCourseId(userId, courseId);
    }

    private CourseDTO toDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setPrice(course.getPrice());
        dto.setImageUrl(course.getImageUrl());
        dto.setTotalDuration(course.getTotalDuration());
        dto.setEnrolledStudents(course.getEnrolledStudents());
        dto.setCreatedAt(course.getCreatedAt());

        if (course.getModules() != null && !course.getModules().isEmpty()) {
            List<ModuleDTO> moduleDTOS = moduleService.getAllByCourse(course.getId());
            dto.setModules(moduleDTOS);
        } else {
            dto.setModules(new ArrayList<>());
        }

        // Adiciona lista de teachers
        if (course.getTeachers() != null && !course.getTeachers().isEmpty()) {
            List<UserDTO> teachers = course.getTeachers().stream()
                    .map(userService::toDTO)
                    .toList();
            dto.setTeachers(teachers);
        }

        return dto;
    }
}