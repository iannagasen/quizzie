package dev.agasen.quizzie.mcq.persistence;

import java.util.Set;

public class McqChoiceEntityUpdater {

  private Set<McqChoiceEntity> choices;

  private Long choiceId;
  private String value;
  private String explanation;
  private boolean correct;

  protected McqChoiceEntityUpdater(Set<McqChoiceEntity> choices) {
    this.choices = choices;
  }

  public McqChoiceEntityUpdater withId(Long choiceId) { 
    this.choiceId = choiceId;
    return this;
  }

  public McqChoiceEntityUpdater setValue(String value) {
    this.value = value;
    return this;
  }

  public McqChoiceEntityUpdater setExplanation(String explanation) {
    this.explanation = explanation;
    return this;
  }

  public McqChoiceEntityUpdater setCorrect(boolean correct) {
    this.correct = correct;
    return this;
  }

  public void update() {
    McqChoiceEntity targetChoice = this.choices.stream()
        .filter(c -> c.getId().equals(choiceId))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No choice with id " + choiceId + " was found."));

    if (targetChoice.isCorrect() != correct) {
      boolean hasNoOtherCorrectChoice = this.choices.stream()
          .filter(c -> !c.getId().equals(choiceId))
          .noneMatch(McqChoiceEntity::isCorrect);

      if (hasNoOtherCorrectChoice && targetChoice.isCorrect()) {
        throw new IllegalArgumentException("Can't update this choice to false, 1 choice must be set to correct");
      } 

      targetChoice.setCorrect(correct);
    }

    if (!targetChoice.getValue().equals(value) && value != null) {
      targetChoice.setValue(value);
    }

    if (!targetChoice.getExplanation().equals(explanation) && explanation != null) {
      targetChoice.setExplanation(explanation);
    }
  }

}
