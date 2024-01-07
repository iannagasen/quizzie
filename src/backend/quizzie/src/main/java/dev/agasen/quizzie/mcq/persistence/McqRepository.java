package dev.agasen.quizzie.mcq.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.agasen.quizzie.mcq.api.McqChoice;
import jakarta.transaction.Transactional;

public interface McqRepository extends JpaRepository<McqEntity, Long> {


  @Modifying
  @Transactional
  @Query(nativeQuery=true, value="""
      UPDATE mcq_choice mc
         SET mc.value = :#{#choice.value},
             mc.explanation = :#{#choice.explanation},
             mc.correct = :#{#choice.correct},
             mc.version = mc.version + 1
       WHERE mc.id = :id
         AND mc.mcq_id = :#{#choice.id}
  """)
  void updateMcqChoice(@Param("id") Long id, @Param("choice") McqChoice choice);

  @Query(nativeQuery=true, value="""
      SELECT * 
        FROM mcq 
       WHERE topic = :topic
       ORDER BY RAND()
       LIMIT :length
  """)
  List<McqEntity> findAllByTopic(@Param("topic") String topic, @Param("length") Integer length);

  List<McqEntity> findAllByTopic(@Param("topic") String topic);

}
