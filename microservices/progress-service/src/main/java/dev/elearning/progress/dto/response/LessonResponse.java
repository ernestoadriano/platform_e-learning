package dev.elearning.progress.dto.response;

import lombok.Data;

@Data
public class LessonResponse {
    private Long id;
    private String title;
    private String description;
    private Long moduleId;
    private String videoUrl;
    private Integer duration;
    private Integer lessonOrder;
    private Boolean isFree;
}
