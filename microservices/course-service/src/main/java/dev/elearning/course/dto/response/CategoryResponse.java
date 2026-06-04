package dev.elearning.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryResponse {

    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private List<CategoryResponse> subCategories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
