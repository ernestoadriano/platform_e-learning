package dev.elearing.platform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exam_answers", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"attempt_id", "question_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamAnswer extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private ExamAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private ExamQuestion question;

    @Column(nullable = false)
    private Integer selectedOption;

    @Column(nullable = false)
    private Boolean isCorrect;
}
