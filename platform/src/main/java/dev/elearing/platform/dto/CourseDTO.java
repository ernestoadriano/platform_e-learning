package dev.elearing.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CourseDTO {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private UserDTO instructor;
    private Long instructorId;
    private List<Long> teacherIds;
    private List<UserDTO> teachers;
    private List<ModuleDTO> modules;
    private Integer totalDuration;
    private Integer enrolledStudents;
    private LocalDateTime createdAt;
}