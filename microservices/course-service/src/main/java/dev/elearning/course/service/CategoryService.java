package dev.elearning.course.service;

import dev.elearning.course.dto.request.CategoryRequest;
import dev.elearning.course.dto.response.CategoryResponse;
import dev.elearning.course.model.Category;
import dev.elearning.course.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CategoryResponse> getMainCategories() {
        return categoryRepository.findAllByParentIdIsNull().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CategoryResponse> getSubCategories(Long parentId) {
        return categoryRepository.findAllByParentId(parentId).stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = getById(id);

        return toResponse(category);
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category already exists: " + request.getName());
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setParentId(request.getParentId());

        category = categoryRepository.save(category);

        return toResponse(category);
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getById(id);

        var isUpdated = false;
        if (request.getName() != null && !request.getName().equals(category.getName())) {
            if (categoryRepository.existsByName(request.getName())) {
                throw new RuntimeException("Category already exists: " + request.getName());
            }
            category.setName(request.getName());
            isUpdated = true;
        }

        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
            isUpdated = true;
        }

        if (request.getParentId() != null) {
            category.setParentId(request.getParentId());
            isUpdated = true;
        }

        if (isUpdated)
            category.setUpdatedAt(LocalDateTime.now());

        category = categoryRepository.save(category);

        return toResponse(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = getById(id);
        categoryRepository.delete(category);
    }

    private Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found: " + id));
    }

    private CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setParentId(category.getParentId());

        List<CategoryResponse> subcategories = categoryRepository.findAllByParentId(category.getParentId()).stream()
                .map(this::toResponse)
                .toList();
        response.setSubCategories(subcategories);
        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());

        return response;
    }
}
