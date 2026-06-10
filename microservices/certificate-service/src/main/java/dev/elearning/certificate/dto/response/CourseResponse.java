package dev.elearning.certificate.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseResponse {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Long instructorId;
}
