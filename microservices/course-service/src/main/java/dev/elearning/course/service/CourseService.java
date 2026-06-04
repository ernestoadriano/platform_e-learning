package dev.elearning.course.service;

import dev.elearning.course.dto.request.CourseRequest;
import dev.elearning.course.dto.response.CourseResponse;
import dev.elearning.course.model.Category;
import dev.elearning.course.model.Course;
import dev.elearning.course.repository.CategoryRepository;
import dev.elearning.course.repository.CourseRepository;
import dev.elearning.course.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public List<CourseResponse> getAllPublishedCourses() {
        return courseRepository.findAllByPublishedTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    public CourseResponse getCourseById(Long id) {
        Course course = getById(id);

        return toResponse(course);
    }

    public List<CourseResponse> getCoursesByCategory(Long categoryId) {
        return courseRepository.findAllByCategoryId(categoryId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CourseResponse> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findAllByInstructorId(instructorId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CourseResponse> getRecentCourse(int limit) {
        return courseRepository.findRecentCourses(PageRequest.of(0, limit)).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CourseResponse> getMostPopularCourses(int limit) {
        return courseRepository.findMostPopularCourses(PageRequest.of(0, limit)).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CourseResponse> searchCoursesByTitle(String title) {
        return courseRepository.findAllTitleContainingIgnoreCase(title).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CourseResponse create(CourseRequest request) {
        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setImageUrl(request.getImageUrl());
        course.setInstructorId(request.getInstructorId());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found: " + request.getCategoryId()));
            course.setCategory(category);
        }

        course = courseRepository.save(course);
        return toResponse(course);
    }

    @Transactional
    public CourseResponse update(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found: " + id));

        boolean isUpdated = false;

        if (request.getTitle() != null) {
            course.setTitle(request.getTitle());
            isUpdated = true;
        }

        if (request.getDescription() != null) {
            course.setDescription(request.getDescription());
            isUpdated = true;
        }

        if (request.getPrice() != null) {
            course.setPrice(request.getPrice());
            isUpdated = true;
        }

        if (request.getImageUrl() != null) {
            course.setImageUrl(request.getImageUrl());
            isUpdated = true;
        }

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found: "  + request.getCategoryId()));
            course.setCategory(category);
            isUpdated = true;
        }

        if (isUpdated)
            course.setUpdatedAt(LocalDateTime.now());

        return toResponse(courseRepository.save(course));
    }


    @Transactional
    public CourseResponse publishedCourse(Long id) {
        Course course = getById(id);
        course.setPublished(true);
        course.setPublishedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());

        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse unpublishCourse(Long id) {
        Course course = getById(id);

        course.setPublished(false);
        course.setPublishedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());

        return toResponse(courseRepository.save(course));
    }

    @Transactional
    public void delete(Long id) {
        Course course = getById(id);
        courseRepository.delete(course);
    }

    @Transactional
    public void incrementEnrolledStudents(Long id) {
        Course course = getById(id);
        course.setEnrolledStudents((course.getEnrolledStudents() == null ? 0 : course.getEnrolledStudents() + 1));
        courseRepository.save(course);
    }

    @Transactional
    public void decrementEnrolledStudents(Long id) {
        Course course = getById(id);
        int current = course.getEnrolledStudents() == null ? 0 : course.getEnrolledStudents();

        course.setEnrolledStudents(Math.max(0, current - 1));
        courseRepository.save(course);
    }

    private Course getById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found: " + id));
    }

    private CourseResponse toResponse(Course course) {
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setDescription(course.getDescription());
        response.setPrice(course.getPrice());
        response.setImageUrl(course.getImageUrl());
        response.setInstructorId(course.getInstructorId());
        response.setPublished(course.getPublished());
        response.setPublishedAt(course.getPublishedAt());
        response.setEnrolledStudents(course.getEnrolledStudents());

        if (course.getCategory() != null) {
            response.setCategoryId(course.getCategory().getId());
            response.setCategoryName(course.getCategory().getName());
        }

        response.setCreatedAt(course.getCreatedAt());
        response.setUpdatedAt(course.getUpdatedAt());

        return response;
    }
}
