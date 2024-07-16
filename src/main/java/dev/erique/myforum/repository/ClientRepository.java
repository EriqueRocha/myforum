package dev.erique.myforum.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.erique.myforum.enums.Role;
import dev.erique.myforum.model.topic.Topic;
import dev.erique.myforum.model.user.Client;
import jakarta.persistence.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    UserDetails findByEmail(String email);

    interface BasicClient{
        Integer getId();
        LocalDateTime getCreationDate();
        String getName();
        String getEmail();
        Role getRole();
    }

    @Query("SELECT a.id AS id, a.creationDate AS creationDate, a.name AS name, a.email AS email, a.role AS role FROM Client a")
    Page<BasicClient> findAllClient(Pageable pageable);

    @Query("SELECT s FROM Client s WHERE s.email = ?1")
    Client findClientByEmail(String login);

    boolean existsByEmail(String email);
}
