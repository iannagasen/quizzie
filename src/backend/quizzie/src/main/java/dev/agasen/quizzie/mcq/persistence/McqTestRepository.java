package dev.agasen.quizzie.mcq.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface McqTestRepository extends JpaRepository<McqTestEntity, Long> {
  
}
