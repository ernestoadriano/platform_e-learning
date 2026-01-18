package com.ernesto.monolith.assessment.model;

import com.ernesto.monolith.assessment.model.enums.ExamType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exams")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long courseId;

    private Long moduleId;

    private Long lessonId;

    @Enumerated(EnumType.STRING)
    private ExamType type;

    private boolean active = false;
}
