package dev.elearing.platform.model;

import dev.elearing.platform.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements UserDetails {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String avatar;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @ManyToMany(mappedBy = "teachers")
    private List<Course> teachingCourses = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Purchase> purchases = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Certificate> certificates = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserProgress> progresses = new ArrayList<>();

    @Override
    public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
    }

    @Override
    public @NonNull String getUsername() {
        return email;
    }
}