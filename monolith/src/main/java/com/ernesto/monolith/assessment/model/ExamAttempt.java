package com.ernesto.monolith.assessment.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exam_attempt")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExamAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long examId;

    private Long studentId;

    private Long courseId;

    private Long moduleId;

    private Long lessonId;

    private double score;

    private Boolean passed;
}
