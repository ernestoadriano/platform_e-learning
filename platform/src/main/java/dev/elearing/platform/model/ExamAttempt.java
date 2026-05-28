package dev.elearing.platform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exam_attempts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamAttempt extends BaseEntity{

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false)
    private Boolean passed = false;

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExamAnswer> answers = new ArrayList<>();
}
