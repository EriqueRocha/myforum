package dev.erique.myforum.repository;

import dev.erique.myforum.enums.TopicStatus;
import dev.erique.myforum.model.topic.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface TopicRepository extends JpaRepository<Topic, Integer> {

    interface BasicTopic{
        Integer getId();
        LocalDateTime getCreationDate();
        String getTitle();
        String getUserName();
        TopicStatus getStatus();
    }

    @Query("SELECT p.id AS id, p.creationDate AS creationDate, p.title AS title, p.userName AS userName, p.status AS status FROM Topic p")
    Page<BasicTopic> findAllTopic(Pageable pageable);

}
