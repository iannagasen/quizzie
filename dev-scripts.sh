
# initializing the project
spring init \
--boot-version=3.2.0 \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=quizzie \
--package-name=dev.agasen.quizzie \
--groupId=dev.agasen.quizzie \
--dependencies=actuator,webflux,mysql,lombok,data-jpa \
--version=0.0.1-SNAPSHOT \
quizzie