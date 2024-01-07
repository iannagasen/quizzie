package dev.agasen.quizzie.mcq.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
      mcqs.add(McqTestItemEntity.fromUnansweredQuestion(q));
    }
  }

}
