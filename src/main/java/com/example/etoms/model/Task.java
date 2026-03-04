package com.example.etoms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Project project;

    public Task() {
    }

    public Task(Long id, String title, String status, User user, Project project) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.user = user;
        this.project = project;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
