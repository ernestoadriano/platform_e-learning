package dev.elearing.platform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exam_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamOption extends BaseEntity{

    @Column(nullable = false, length = 500)
    private String text;

    @Column(nullable = false)
    private Integer optionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private ExamQuestion question;
}
