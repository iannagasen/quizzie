package dev.agasen.quizzie.mcq.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface McqChoiceRepository extends JpaRepository<McqChoiceEntity, Long> {
  
  @Query(nativeQuery=true, value="""
    SELECT * FROM mcq_choice WHERE mcq_id = :mcqId
  """)
  List<McqChoiceEntity> findAllByMcq(@Param("mcqId") Long mcqId);
}
