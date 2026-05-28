package dev.elearing.platform.repository;

import dev.elearing.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndIdNot(String email, Long id);

    @Query("SELECT COUNT(p) FROM Purchase p WHERE p.user.id = :userId")
    Long countUserCourses(@Param("userId") Long userId);

    @Query("SELECT COUNT(c) FROM Certificate c WHERE c.user.id = :userId")
    Long countCertificates(@Param("userId") Long userId);
}
