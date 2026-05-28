package dev.elearing.platform.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "module_exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModuleExam extends BaseEntity{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false, unique = true)
    private Module module;

    @Column(nullable = false)
    private Integer passingScore = 70;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ExamQuestion> questions = new ArrayList<>();
}
