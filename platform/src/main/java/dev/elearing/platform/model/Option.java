package dev.elearing.platform.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "options")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Option extends BaseEntity{

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private Integer optionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    @JsonBackReference
    private Question question;
}
