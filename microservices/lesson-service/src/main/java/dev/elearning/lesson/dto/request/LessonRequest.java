package dev.elearning.lesson.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LessonRequest {

    private String title;
    private String description;
    private Long moduleId;
    private String videoUrl;
    private Integer duration;
    private Integer lessonOrder;
    private Boolean isFree;
}
