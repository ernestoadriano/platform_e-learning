package com.ernesto.monolith.certificate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "certificates")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long courseId;

    private LocalDateTime issuedAt = LocalDateTime.now();

    private String certificateUrl;
}
