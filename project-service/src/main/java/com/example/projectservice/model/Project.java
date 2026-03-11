package com.example.projectservice.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("name")
    @JsonAlias({ "title", "projectName", "nombre" })
    private String name;

    private String description;

    @JsonProperty("user_id")
    @JsonAlias("userId")
    @Column(name = "user_id")
    private Long userId;

    @Transient
    private com.example.projectservice.dto.UserDTO user;

    public Project() {
    }

    public Project(Long id, String name, String description, Long userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public com.example.projectservice.dto.UserDTO getUser() {
        return user;
    }

    public void setUser(com.example.projectservice.dto.UserDTO user) {
        this.user = user;
    }
}
