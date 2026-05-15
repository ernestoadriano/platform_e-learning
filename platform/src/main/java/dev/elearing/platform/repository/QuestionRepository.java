package dev.elearing.platform.repository;

import dev.elearing.platform.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Purchase, Long> {
}
