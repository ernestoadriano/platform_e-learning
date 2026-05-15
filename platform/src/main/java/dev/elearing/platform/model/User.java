package dev.elearing.platform.model;

import dev.elearing.platform.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity{

    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String avatar;
    @Column(nullable = false)
    private Role role;
    @Column(name = "is_active")
    private Boolean isActive = true;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
}
