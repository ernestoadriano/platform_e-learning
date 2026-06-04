package dev.elearning.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModuleResponse {

    private Long id;
    private String title;
    private String description;
    private Long courseId;
    private Integer moduleOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
