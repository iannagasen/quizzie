package dev.agasen.quizzie.mcq.persistence;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="mcq")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class McqEntity {
  
  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Long id;

  @Version
  private int version;

  private String topic;

  private String question;

  @OneToMany(
    mappedBy="mcq", // name of the field in McqChoice to be joined
    orphanRemoval=true,
    cascade=CascadeType.ALL
  )
  private Set<McqChoiceEntity> choices = new HashSet<>();

  private McqEntity(String topic, String question, Set<McqChoiceEntity> choices) {
    this(null, 1, topic, question, choices);
  }

  public McqEntity(String topic, String question) {
    this(topic, question, new HashSet<>());
  }

  public McqChoiceEntity addChoice(McqChoiceEntity choice) {
    choice.setMcq(this);
    this.choices.add(choice);
    return choice;
  }

  public Optional<McqChoiceEntity> findChoiceById(Long choiceId) {
    return this.choices.stream()
        .filter(c -> c.getId().equals(choiceId))
        .findFirst();
  }

  public McqChoiceEntityUpdater choiceUpdater() {
    return new McqChoiceEntityUpdater(this.choices);
  }


}
