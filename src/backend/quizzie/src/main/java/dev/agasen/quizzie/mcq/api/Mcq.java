package dev.agasen.quizzie.mcq.api;

import java.util.Set;

public record Mcq(
  Long id, 
  String topic, 
  String question, 
  Set<McqChoice> choices
) {

}
