package dev.elearning.lesson.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LessonResponse {

    private Long id;
    private String title;
    private String description;
    private Long moduleId;
    private String videoUrl;
    private Integer duration;
    private Integer lessonOrder;
    private Boolean isFree;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
