package dev.erique.myforum.repository;

import dev.erique.myforum.enums.TopicStatus;
import dev.erique.myforum.model.answers.Answer;
import dev.erique.myforum.model.topic.Topic;
import dev.erique.myforum.model.user.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    interface BasicAnswer{
        Integer getId();
        LocalDateTime getCreationDate();
        String getMessage();
        String getUserName();
    }

    @Query("SELECT a.id AS id, a.creationDate AS creationDate, a.message AS message, a.userName AS userName FROM Answer a WHERE a.client = :client")
    Page<BasicAnswer> findAllAnswer(Client client, Pageable pageable);

    @Query("SELECT a.id AS id, a.creationDate AS creationDate, a.message AS message, a.userName AS userName FROM Answer a WHERE a.topic = :topic")
    Page<BasicAnswer> findAllAnswerForTopic(Topic topic, Pageable pageable);

    @Query("SELECT a FROM Answer a WHERE a.client = :client AND a.id = :answerId")
    Answer findAnswer(Client client, Integer answerId);
}
