package dev.elearing.platform.dto;

import dev.elearing.platform.model.Module;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LessonDTO {

    private Long id;

    private String title;

    private String description;

    private String videoUrl;

    private Integer duration;

    private Integer order;

    private Module module;

    private List<QuestionDTO> questions;
}
