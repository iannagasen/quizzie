package dev.agasen.quizzie.mcq.api;

public record McqChoice (
  Long id,
  String value,
  String explanation,
  boolean correct
) {

}
