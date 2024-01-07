package dev.agasen.quizzie.mcq.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.agasen.quizzie.mcq.api.Mcq;
import dev.agasen.quizzie.mcq.api.McqChoice;
import dev.agasen.quizzie.mcq.api.McqService;
import dev.agasen.quizzie.mcq.api.McqTest;
import dev.agasen.quizzie.mcq.persistence.McqChoiceEntity;
import dev.agasen.quizzie.mcq.persistence.McqEntity;
import dev.agasen.quizzie.mcq.persistence.McqRepository;
import dev.agasen.quizzie.mcq.persistence.McqTestEntity;
import dev.agasen.quizzie.mcq.persistence.McqTestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mcq")
@RequiredArgsConstructor
public class McqServiceImpl implements McqService {

  private final McqRepository mcqRepository;
  private final McqMapper mcqMapper;
  private final McqTestRepository mcqTestRepository;
  private final McqTestMapper mcqTestMapper;

  @Override
  @Transactional
  public List<Mcq> getQuestions(String topic) {
    List<McqEntity> entities = mcqRepository.findAllByTopic(topic);
    return mcqMapper.toMcqs(entities);
  }


  @Override
  @Transactional
  public Mcq addMcq(Mcq mcq) {
    McqEntity entity = new McqEntity(mcq.topic(), mcq.question());

    int noOfCorrectAnswers = 0;
    for (McqChoice choice : mcq.choices()) {
      var c = new McqChoiceEntity(choice.value(), choice.explanation(), choice.correct());
      entity.addChoice(c);
      if (choice.correct()) noOfCorrectAnswers++;
    }

    if (noOfCorrectAnswers == 0) {
      throw new IllegalArgumentException("Question should have atleast 1 answer tagged as correct.");
    }

    McqEntity savedEntity = mcqRepository.save(entity);
    
    return mcqMapper.toMcq(savedEntity);
  }


  @Override
  @Transactional
  public McqChoice addChoice(Long id, McqChoice choice) {
    McqEntity mcq = mcqRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("No question with id " + id + " was found."));

    mcq.addChoice(new McqChoiceEntity(choice.value(), choice.explanation(), choice.correct()));

    McqEntity savedMcq = mcqRepository.save(mcq);

    McqChoiceEntity addedChoice = savedMcq.getChoices()
        .stream()
        .sorted(Comparator.comparing(McqChoiceEntity::getId, Comparator.reverseOrder()))
        .findFirst()
        .orElseThrow();
    
    return new McqChoice(addedChoice.getId(), addedChoice.getValue(), addedChoice.getExplanation(), addedChoice.isCorrect());
  }


  @Override
  @Transactional
  public void updateChoice(Long id, McqChoice choice) {
    McqEntity mcq = mcqRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("No question with id " + id + " was found."));
        
    mcq.choiceUpdater()
        .withId(choice.id())
        .setValue(choice.value())
        .setExplanation(choice.explanation())
        .setCorrect(choice.correct())
        .update();
  }


  @Override
  @Transactional
  public McqTest generateTest(Optional<String> topic, Optional<Integer> items) {
    /**
     * UPDATE this constants soon
     */
    String _topic = topic.orElse("AWS");
    Integer _items = items.orElse(25);

    List<McqEntity> entities = mcqRepository.findAllByTopic(_topic, _items);

    McqTestEntity newTest = McqTestEntity.newTest();
    newTest.addMcqItems(entities);

    McqTestEntity generatedTest = mcqTestRepository.save(newTest);
    
    return mcqTestMapper.toMcqTest(generatedTest);
  }


  @Override
  @Transactional
  public McqTest getTestResult(Long id) {
    McqTestEntity testResult = mcqTestRepository.findById(id).orElseThrow();
    return mcqTestMapper.toMcqTest(testResult);
  }

}