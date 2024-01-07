package dev.agasen.quizzie.mcq.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="mcq_test")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class McqTestEntity {
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  private LocalDate dateTaken;

  private Long timeTaken;

  private Integer total;

  private Integer score;

  @OneToMany(
    mappedBy="mcqTest",
    cascade=CascadeType.ALL,
    orphanRemoval=true
  )
  private List<McqTestItemEntity> mcqs = new ArrayList<>();

  public static McqTestEntity newTest() {
    return new McqTestEntity();
  }

  public void addMcqItems(Collection<McqEntity> mcqEntities) {
    for (McqEntity q : mcqEntities) {
      McqTestItemEntity i = McqTestItemEntity.fromUnansweredQuestion(q);
      i.setMcqTest(this);
      mcqs.add(i);
    }
  }

  public McqTestEntity submitAnswers(List<ItemAnswer> answers) {
    int score = 0;
    for (int i = 0; i < mcqs.size(); i++) {
      ItemAnswer ans = answers.get(i);
      McqTestItemEntity testItem = mcqs.get(i);

      Long ansId = ans.itemId();
      Long itemId = testItem.getId();
      boolean notSameQuestion = !itemId.equals(ansId);

      if (notSameQuestion) {
        Optional<ItemAnswer> answerFound = answers.stream()
            .filter(a -> a.itemId().equals(itemId))
            .findFirst();

        if (answerFound.isPresent()) {
          ans = answerFound.get();
        } else {
          continue;
        }
      }

      testItem.selectAnswer(ans.choiceId());

      if (testItem.isSelectedAnswerCorrect()) {
        score++;
      }
    }
    this.score = score;

    return this;
  }

  public record ItemAnswer (
    Long itemId,
    Long choiceId
  ) {}
}
