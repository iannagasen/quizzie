package dev.agasen.quizzie.mcq.api;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface McqService {

  /**
   * GET /questions/{topic}
   */
  @GetMapping("/questions/{topic}")
  List<Mcq> getQuestions(
      @PathVariable(name="topic") String topic
  );

  /**
   * POST /question
   * Payload: Mcq
   */
  @PostMapping("/question")
  Mcq addMcq(
      @RequestBody Mcq mcq
  );

  /**
   * POST /question/{id}/choice
   * Payload: McqChoice
   */
  @PostMapping("/question/{id}/choice")
  McqChoice addChoice(
      @PathVariable(name="id") Long id, 
      @RequestBody McqChoice choice
  );

  /**
   * PATCH /question/{id}/choice
   * Payload: McqChoice
   */
  @PatchMapping("/question/{id}/choice")
  void updateChoice(
      @PathVariable(name="id") Long id, 
      @RequestBody McqChoice choice
  );

  /**
   * POST /test
   * add Query Params here, topic? no of items? timer?
   */
  @PostMapping("/test")
  McqTest generateTest(
      @RequestParam(name="topic") Optional<String> topic,
      @RequestParam(name="items") Optional<Integer> items
  );

  /**
   *  GET /test/{id}
   */
  @GetMapping("/test/{id}")
  McqTest getTestResult(
      @PathVariable(name="id") Long id
  );

  /**
   * POST /test/{id}
   * Payload: List<McqTestItem>
   */
  @PostMapping("/test/{id}")
  McqTest submitTest(
      @PathVariable(name="id") Long id,
      @RequestBody List<McqTestItem> testItems
  );

}
