package dev.agasen.quizzie.mcq.service;

import java.util.List;

import org.springframework.stereotype.Component;

import dev.agasen.quizzie.mcq.api.McqTest;
import dev.agasen.quizzie.mcq.api.McqTestItem;
import dev.agasen.quizzie.mcq.persistence.McqTestEntity;
import dev.agasen.quizzie.mcq.persistence.McqTestItemEntity;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class McqTestMapper {

  private final McqMapper mcqMapper;
  
  McqTest toMcqTest(McqTestEntity e) {
    return new McqTest(e.getId(), e.getDateTaken(), e.getTimeTaken(), toMcqs(e.getMcqs()));
  }
  
  McqTestItem toTestItem(McqTestItemEntity e) {
    return new McqTestItem(
      e.getId(), 
      e.getQuestion() != null ? mcqMapper.toMcq(e.getQuestion()) : null, 
      e.getSelectedChoice() != null ? e.getSelectedChoice().getId() : null
    );
  }

  List<McqTestItem> toMcqs(List<McqTestItemEntity> e) {
    return e.stream().map(this::toTestItem).toList();
  }
}
