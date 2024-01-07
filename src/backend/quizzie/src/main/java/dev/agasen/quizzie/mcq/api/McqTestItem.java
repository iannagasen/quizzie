package dev.agasen.quizzie.mcq.api;

public record McqTestItem(
  Long id,
  Mcq mcq, 
  McqChoice selectedChoice
) {
  
}
