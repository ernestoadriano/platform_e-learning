package dev.elearning.progress.dto.response;

import lombok.Data;

@Data
public class ModuleResponse {

    private Long id;
    private String title;
    private String description;
    private Long courseId;
    private Integer moduleOrder;
}
