package com.example.etoms.model;

public class TaskRequest {

    private String title;
    private String status;
    private Long userId;
    private Long projectId;

    public TaskRequest() {
    }

    public TaskRequest(String title, String status, Long userId, Long projectId) {
        this.title = title;
        this.status = status;
        this.userId = userId;
        this.projectId = projectId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
