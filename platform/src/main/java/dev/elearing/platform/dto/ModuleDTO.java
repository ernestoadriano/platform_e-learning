package dev.elearing.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModuleDTO {

    private Long id;
    private String title;
    private String description;
    private Integer order;
    private List<LessonDTO> lessons;
}
