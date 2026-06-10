package dev.elearning.progress.model;

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
@Table(name = "module_progress", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "module_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModuleProgress extends BaseEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long moduleId;

    private Integer completedLessons = 0;

    private Integer totalLessons = 0;

    private Double percentage = 0.0;

    private Boolean isCompleted = false;

    private Boolean examPassed = false;

    private Double examScore;

    private LocalDateTime lastAccessedAt = LocalDateTime.now();

    private LocalDateTime completedAt;
}
