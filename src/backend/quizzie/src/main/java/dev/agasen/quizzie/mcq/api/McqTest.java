package dev.agasen.quizzie.mcq.api;

import java.time.LocalDate;
import java.util.List;

public record McqTest (
  Long id,
  LocalDate dateTaken,
  Long timeTaken,
  List<McqTestItem> mcqs
) {
  
}
