PRODUCTS:
  - quizzie
    - mcq
      - test
      - learn
    - flashcard
      - test 
      - learn
    - notes - summary
  - miscellaneous
    - timer
    - pomodoro



ERD
```mermaid
erDiagram


  Mcq {
    long      id          PK
    int       version
    string    topic
    string    question
  }

  McqChoice {
    long      id          PK
    int       version
    string    value
    string    explanation
    boolean   correct
    long      mcq_id FK
  }

  McqTest {
    long      id          PK
    datetime  date_taken
    long      time_taken
    int       score
    int       total
  }

  McqTestItem {
    long      id          PK
    long      mcq_test_id FK
    long      mcq_id      FK
    long      choice_id   FK
  }

  Mcq ||--|{ McqChoice : "Mcq can have 1 or mor choices"
  McqTest ||--|{ McqTestItem: "Mcq Test can have 1 or more Test Items"
  McqTestItem ||--|| Mcq : "Mcq Test Item must have 1 Mcq reference"
  McqTestItem ||--|o McqChoice: "McqTestItem have 0 or 1 Selected Choice"
```

API
`API.java`
```java
@RestController
@RequestMapping("/mcq")
public class McqController {

  // add Query Params here, topic? no of items? timer?
  @GetMapping("/test")
  McqTest generateTest();

  @PostMapping("/test")
  McqTestResult submitTest(@RequestBody McqTestAnswer mcqTest);

  @GetMapping("/test/{id}")
  McqTestResult getTestResult(@PathVariable(name="id") Long id);

  @GetMapping("/questions/{topic}")
  List<Mcq> getQuestions(@PathVariable(name="topic") String topic);

  @PostMapping("/question")
  Mcq addMcq(@RequestBody Mcq mcq);

  @PostMapping("/question/{id}/choice")
  McqChoice addChoice(@RequestBody McqChoice choice);

  @PatchMapping("/question/{id}/choice")
  McqChoice updateChoice(@RequestBody McqChoice);

}

```