package dev.elearing.platform.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Course extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    // REMOVA esta linha - estava errado
    // @Column(nullable = false)
    // private User instructor;

    @Column(name = "total_duration")
    private Integer totalDuration;

    @Column(name = "enrolled_students")
    private Integer enrolledStudents = 0;

    // ADICIONE - Relação muitos-para-muitos com Teachers
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "course_teachers",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<User> teachers = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Module> modules = new ArrayList<>();
}