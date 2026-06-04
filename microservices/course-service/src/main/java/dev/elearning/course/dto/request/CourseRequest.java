package dev.elearning.course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseRequest {

    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Long instructorId;
    private Long categoryId;
}
