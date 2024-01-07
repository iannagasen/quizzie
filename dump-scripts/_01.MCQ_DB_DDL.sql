CREATE DATABASE IF NOT EXISTS `mcq-db`;

-- CREATE USER 'root'@'localhost' IDENTIFIED BY 'local'
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON *.* TO 'user'@'%';

USE `mcq-db`;

CREATE TABLE IF NOT EXISTS mcq (
  id          BIGINT          PRIMARY KEY AUTO_INCREMENT,
  version     INTEGER         NOT NULL,
  topic       VARCHAR(255),
  question    TEXT            NOT NULL
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS mcq_choice (
  id          BIGINT PRIMARY KEY AUTO_INCREMENT,
  version     INTEGER NOT NULL,
  value       VARCHAR(255) NOT NULL,
  explanation TEXT,
  correct     BOOLEAN NOT NULL,
  mcq_id      BIGINT,
  FOREIGN KEY (mcq_id) REFERENCES mcq(id)
) ENGINE=InnoDB;


INSERT INTO mcq (version, topic, question)
VALUES 
    (1, 'AWS', 'What does S3 stand for in AWS?'),
    (1, 'AWS', 'What service is used for computing power in AWS?'),
    (1, 'AWS', 'What service can be used to automatically scale resources based on traffic in AWS?'),
    (1, 'AWS', 'What is the AWS service for managing databases?');

INSERT INTO mcq_choice (version, value, explanation, correct, mcq_id)
VALUES 
    (1, 'Simple Storage Service', 'S3 stands for Simple Storage Service', true, 1),
    (1, 'Secure Storage Service', 'S3 stands for Secure Storage Service', false, 1),
    (1, 'System Storage Service', 'S3 stands for System Storage Service', false, 1),

    (1, 'EC2', 'EC2 provides computing power in AWS', true, 2),
    (1, 'S3', 'S3 provides computing power in AWS', false, 2),
    (1, 'RDS', 'RDS provides computing power in AWS', false, 2),

    (1, 'Elastic Load Balancing', 'ELB scales resources based on traffic', true, 3),
    (1, 'Auto Scaling', 'Auto Scaling scales resources based on traffic', true, 3),
    (1, 'AWS Lambda', 'AWS Lambda scales resources based on traffic', false, 3),

    (1, 'RDS', 'RDS is the AWS service for managing databases', true, 4),
    (1, 'DynamoDB', 'DynamoDB is the AWS service for managing databases', false, 4),
    (1, 'S3', 'S3 is the AWS service for managing databases', false, 4);


CREATE TABLE IF NOT EXISTS mcq_test (
  id            BIGINT PRIMARY KEY AUTO_INCREMENT,
  date_taken    DATE,
  time_taken    BIGINT,
  score         INTEGER,
  total         INTEGER
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS mcq_test_item (
  id            BIGINT PRIMARY KEY AUTO_INCREMENT,
  mcq_test_id   BIGINT,
  mcq_id        BIGINT,
  mcq_choice_id BIGINT,
  FOREIGN KEY (mcq_test_id)   REFERENCES mcq_test(id),
  FOREIGN KEY (mcq_id)        REFERENCES mcq(id),
  FOREIGN KEY (mcq_choice_id) REFERENCES mcq_choice(id)
) ENGINE=InnoDB;

INSERT INTO mcq_test(date_taken, time_taken, score, total)
VALUE (SYSDATE(), 500000, 3, 4);

INSERT INTO mcq_test_item (mcq_test_id, mcq_id, mcq_choice_id)
VALUES 
    (1, 1, 1),
    (1, 2, 4),
    (1, 3, 7),
    (1, 4, 9);