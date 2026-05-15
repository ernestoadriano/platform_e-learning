package dev.elearing.platform.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class RefreshToken extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String tokenHash;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

    private Boolean revoked = false;

    @Column(name = "remember_me")
    private Boolean rememberMe = false;
}
