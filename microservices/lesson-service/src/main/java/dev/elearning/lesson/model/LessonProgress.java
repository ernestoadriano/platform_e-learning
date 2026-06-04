package dev.elearning.lesson.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "lesson_progress", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "lesson_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LessonProgress extends BaseEntity{

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long lessonId;

    private Boolean isCompleted = false;

    private Integer score;

    private LocalDateTime completedAt;

    private LocalDateTime lastAccessedAt;
}
