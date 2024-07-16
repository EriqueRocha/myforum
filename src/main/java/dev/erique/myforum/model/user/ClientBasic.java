package dev.erique.myforum.model.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.erique.myforum.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class ClientBasic {

    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime creationDate;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

//-----------------------------------------------------------------------------


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
