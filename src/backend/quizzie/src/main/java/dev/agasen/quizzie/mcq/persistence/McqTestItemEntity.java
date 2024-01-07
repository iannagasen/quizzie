package dev.agasen.quizzie.mcq.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="mcq_test_item")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class McqTestItemEntity {
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(
    name="mcq_id",
    referencedColumnName="id"
  )
  private McqEntity question;

  @OneToOne
  @JoinColumn(
    name="mcq_choice_id",
    referencedColumnName="id"
  )
  private McqChoiceEntity selectedChoice;

  @ManyToOne(fetch=FetchType.LAZY)
  private McqTestEntity mcqTest;

  private McqTestItemEntity(McqEntity question) {
    this.question = question;
  }

  public static McqTestItemEntity fromUnansweredQuestion(McqEntity question) {
    return new McqTestItemEntity(question);
  }

}
