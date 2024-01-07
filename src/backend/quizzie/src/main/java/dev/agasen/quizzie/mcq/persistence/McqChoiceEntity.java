package dev.agasen.quizzie.mcq.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="mcq_choice")
@Getter
@AllArgsConstructor 
@NoArgsConstructor
public class McqChoiceEntity {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @Version
  private int version;

  private String value;
  
  private String explanation;

  private boolean correct;

  @ManyToOne(fetch=FetchType.LAZY)
  private McqEntity mcq;

  public McqChoiceEntity(String value, String explanation, boolean correct, McqEntity mcq) {
    this(null, 1, value, explanation, correct, mcq);
  }

  public McqChoiceEntity(String value, String explanation, boolean correct) {
    this(value, explanation, correct, null);
  }

  protected void setMcq(McqEntity mcq) {
    this.mcq = mcq;
  }

  protected void setValue(String value) {
    this.value = value;
  }

  protected void setExplanation(String explanation) {
    this.explanation = explanation;
  }

  protected void setCorrect(boolean correct) {
    this.correct = correct;
  }

}
