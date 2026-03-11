package com.example.taskservice.service;

import com.example.taskservice.model.*;
import com.example.taskservice.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final RestTemplate restTemplate;

    private static final String USER_SERVICE_URL = "http://localhost:8081/api/users/";
    private static final String PROJECT_SERVICE_URL = "http://localhost:8082/api/projects/";

    public TaskService(TaskRepository taskRepository, RestTemplate restTemplate) {
        this.taskRepository = taskRepository;
        this.restTemplate = restTemplate;
    }

    private User fetchUser(Long userId) {
        try {
            return restTemplate.getForObject(USER_SERVICE_URL + "{id}", User.class, userId);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "No se pudo contactar al servicio de usuarios: " + e.getMessage());
        }
    }

    private Project fetchProject(Long projectId) {
        try {
            return restTemplate.getForObject(PROJECT_SERVICE_URL + "{id}", Project.class, projectId);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "No se pudo contactar al servicio de proyectos: " + e.getMessage());
        }
    }

    // Método auxiliar para poblar los DTOs en la entidad Tarea cuando se recupera
    // de base de datos
    private Task populateTaskDetails(Task task) {
        if (task.getUserId() != null) {
            try {
                task.setUser(fetchUser(task.getUserId()));
            } catch (Exception ignored) {
            }
        }
        if (task.getProjectId() != null) {
            try {
                task.setProject(fetchProject(task.getProjectId()));
            } catch (Exception ignored) {
            }
        }
        return task;
    }

    public Task createTask(TaskRequest request) {
        if (request.getUserId() == null || request.getProjectId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId y projectId son obligatorios");
        }

        // Validar que el usuario y proyecto existan realizando la llamada HTTP
        // correspondiente
        User user = fetchUser(request.getUserId());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Usuario no encontrado con id: " + request.getUserId());
        }

        Project project = fetchProject(request.getProjectId());
        if (project == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Proyecto no encontrado con id: " + request.getProjectId());
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setStatus(request.getStatus());
        task.setUserId(request.getUserId());
        task.setProjectId(request.getProjectId());

        // Se guarda en base de datos.
        Task savedTask = taskRepository.save(task);

        // Se populen los objetos transitorios para devolver toda la información en el
        // request
        savedTask.setUser(user);
        savedTask.setProject(project);

        return savedTask;
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(this::populateTaskDetails).collect(Collectors.toList());
    }

    public List<Task> getTasksByUserId(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId es obligatorio");
        }
        // Validar que el usuario existe antes de buscar sus tareas
        User user = fetchUser(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con id: " + userId);
        }

        List<Task> tasks = taskRepository.findByUserId(userId);
        return tasks.stream().map(this::populateTaskDetails).collect(Collectors.toList());
    }

    public Task updateTaskStatus(Long taskId, StatusRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tarea no encontrada con id: " + taskId));

        task.setStatus(request.getStatus());
        Task savedTask = taskRepository.save(task);
        return populateTaskDetails(savedTask);
    }
}
