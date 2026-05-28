package dev.elearing.platform.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Question extends BaseEntity{

    @Column(nullable = false, length = 500)
    private String text;

    @Column(nullable = false)
    private Integer correctAnswer;

    @Column(length = 500)
    private String explanation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    @JsonBackReference
    private Lesson lesson;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("optionOrder ASC")
    @JsonManagedReference
    private List<Option> options = new ArrayList<>();
}
