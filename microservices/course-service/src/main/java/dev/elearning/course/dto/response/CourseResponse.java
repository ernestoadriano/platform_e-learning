package dev.elearning.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseResponse {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Long instructorId;
    private Boolean published;
    private LocalDateTime publishedAt;
    private Integer enrolledStudents;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
