package com.ernesto.monolith.notification.model;

import com.ernesto.monolith.common.dto.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String title;

    @Column(length = 1000)
    private String message;

    private boolean read = false;

    private LocalDateTime sentAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private NotificationType type;
}
