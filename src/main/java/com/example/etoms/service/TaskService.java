package com.example.etoms.service;

import com.example.etoms.model.*;
import com.example.etoms.repository.ProjectRepository;
import com.example.etoms.repository.TaskRepository;
import com.example.etoms.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public Task createTask(TaskRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado con id: " + request.getUserId()));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Proyecto no encontrado con id: " + request.getProjectId()));

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setStatus(request.getStatus());
        task.setUser(user);
        task.setProject(project);

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getTasksByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Usuario no encontrado con id: " + userId);
        }
        return taskRepository.findByUserId(userId);
    }

    public Task updateTaskStatus(Long taskId, StatusRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tarea no encontrada con id: " + taskId));

        task.setStatus(request.getStatus());
        return taskRepository.save(task);
    }
}
