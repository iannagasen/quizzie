package dev.agasen.quizzie.mcq.service;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import dev.agasen.quizzie.MysqlTestBase;
import dev.agasen.quizzie.mcq.api.Mcq;
import dev.agasen.quizzie.mcq.api.McqChoice;
import dev.agasen.quizzie.mcq.persistence.McqChoiceEntity;
import dev.agasen.quizzie.mcq.persistence.McqChoiceRepository;
import dev.agasen.quizzie.mcq.persistence.McqEntity;
import dev.agasen.quizzie.mcq.persistence.McqRepository;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT, properties={
  "spring.jpa.hibernate.ddl-auto=update"
})
class McqServiceImplTest extends MysqlTestBase {

  @Autowired private WebTestClient webClient;
  @Autowired private McqRepository mcqRepository;
  @Autowired private McqChoiceRepository mcqChoiceRepository;

  @BeforeEach
  void setupDb() {
    mcqRepository.deleteAll();
  }

  @Test
  void testGetQuestionsByTopic() {
    String topic = "AWS";
    String GET_QUESTIONS_ENDPOINT = "/mcq/questions/" + "topic";

    createMcqQuestion(topic, 0);
    createMcqQuestion(topic, 1);
    createMcqQuestion(topic, 2);

    int expectedSize = 3;
    int repositorySizeAfterInsertion = mcqRepository.findAll().size();
    assertEquals(expectedSize, repositorySizeAfterInsertion);

    getAndVerifyMcqs(GET_QUESTIONS_ENDPOINT, HttpStatus.OK)
        .jsonPath("$.length()").isEqualTo(expectedSize);
  }


  @Test
  void testAddMcqSuccessful() {
    String ADD_MCQ_ENDPOINT = "/mcq/question";

    var choice1 = new McqChoice(null, "choice 1", "choice 1", true);
    var choice2 = new McqChoice(null, "choice 2", "choice 2", false);
    var choice3 = new McqChoice(null, "choice 3", "choice 3", false);
    var mcq = new Mcq(null, "AWS", "question no 0", Set.of(choice1, choice2, choice3));
    
    webClient.post()
        .uri(ADD_MCQ_ENDPOINT) 
        .bodyValue(mcq)
        .exchange()
        .expectStatus().is2xxSuccessful();

    assertEquals(1, mcqRepository.findAll().size());
  }


  @Test
  void testAddMcqUnsuccessful() {
    /**
     * this should be 404 since, none of the choices are correct
     * i.e. all choices with correct tag = false
     */
    String ADD_MCQ_ENDPOINT = "/mcq/question";

    var choice1 = new McqChoice(null, "choice 1", "choice 1", false);
    var choice2 = new McqChoice(null, "choice 2", "choice 2", false);
    var choice3 = new McqChoice(null, "choice 3", "choice 3", false);
    var mcq = new Mcq(null, "AWS", "question no 0", Set.of(choice1, choice2, choice3));
    
    // server error here
    // TODO: when global api error handling is introduced. come back here and refine
    webClient.post()
        .uri(ADD_MCQ_ENDPOINT)
        .bodyValue(mcq)
        .exchange()
        .expectStatus().is5xxServerError();

    assertEquals(0, mcqRepository.findAll().size());
  }


  @Test
  void testAddChoice() {
    String ADD_CHOICE_ENDPOINT_FORMAT = "/mcq/question/%s/choice";

    McqEntity savedMcq = createMcqQuestion("AWS", 0);
    Long mcqId = savedMcq.getId();

    int currentChoiceCount = mcqChoiceRepository.findAllByMcq(mcqId).size();

    var choicePayload = new McqChoice(null, "choice 3", "choice 3", false);

    webClient.post()
        .uri(ADD_CHOICE_ENDPOINT_FORMAT.formatted(mcqId))
        .bodyValue(choicePayload)
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody()
        .jsonPath("$.id").isNotEmpty();

    // choice count should be incremented by 1
    int choiceCountAfterAddition = mcqChoiceRepository.findAllByMcq(mcqId).size();
    assertEquals(currentChoiceCount + 1, choiceCountAfterAddition);
  }


  @Test
  void testUpdateChoiceSuccessful() {
    String UPDATE_CHOICE_ENDPOINT_FORMAT = "/mcq/question/%s/choice";

    McqEntity savedMcq = createMcqQuestion("AWS", 0);
    Long mcqId = savedMcq.getId();    

    List<McqChoiceEntity> choiceEntities = mcqChoiceRepository.findAllByMcq(mcqId);
    Long choiceIdToUpdate = choiceEntities.get(0).getId();

    var choicePayload = new McqChoice(choiceIdToUpdate, "updated value", "updated explanation", true);

    webClient.patch()
        .uri(UPDATE_CHOICE_ENDPOINT_FORMAT.formatted(mcqId))
        .bodyValue(choicePayload)
        .exchange()
        .expectStatus().is2xxSuccessful();
    

    // version should be incremented
    int newVersionAfterUpdated = 2; // 1 incremented
    int version = mcqChoiceRepository.findById(choiceIdToUpdate)
        .map(McqChoiceEntity::getVersion)
        .orElseThrow();

    assertEquals(newVersionAfterUpdated, version);
  }


  
  private McqEntity createMcqQuestion(String topic, int randomId) {
    McqEntity mcq = new McqEntity(topic, "question no. " + randomId);
    mcq.addChoice(new McqChoiceEntity("value 1", "explanation 1", true));
    mcq.addChoice(new McqChoiceEntity("value 2", "explanation 2", false));
    mcq.addChoice(new McqChoiceEntity("value 3", "explanation 3", false));
    mcq.addChoice(new McqChoiceEntity("value 4", "explanation 4", false));
    return mcqRepository.save(mcq);
  }


  private WebTestClient.BodyContentSpec getAndVerifyMcqs(String urlQuery, HttpStatus expectedStatus) {
    return webClient.get()
        .uri(urlQuery)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isEqualTo(expectedStatus)
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();
  }
}
