package dev.agasen.quizzie.mcq.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import dev.agasen.quizzie.mcq.api.Mcq;
import dev.agasen.quizzie.mcq.api.McqChoice;
import dev.agasen.quizzie.mcq.persistence.McqChoiceEntity;
import dev.agasen.quizzie.mcq.persistence.McqEntity;

@Component
public class McqMapper {
  
  public List<Mcq> toMcqs(List<McqEntity> e) {
    return e.stream().map(this::toMcq).toList();
  }

  public Mcq toMcq(McqEntity e) {
    return new Mcq(e.getId(), e.getTopic(), e.getQuestion(), e.getChoices().stream().map(this::toMcqChoice).collect(Collectors.toSet()));
  }

  public McqChoice toMcqChoice(McqChoiceEntity e) {
    return new McqChoice(e.getId(), e.getValue(), e.getExplanation(), e.isCorrect());
  }

}
