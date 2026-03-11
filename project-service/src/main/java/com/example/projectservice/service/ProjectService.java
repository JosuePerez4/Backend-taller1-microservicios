package com.example.projectservice.service;

import com.example.projectservice.dto.UserDTO;
import com.example.projectservice.model.Project;
import com.example.projectservice.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final RestTemplate restTemplate;

    private static final String USER_SERVICE_URL = "http://localhost:8081/api/users/";

    public ProjectService(ProjectRepository projectRepository, RestTemplate restTemplate) {
        this.projectRepository = projectRepository;
        this.restTemplate = restTemplate;
    }

    private UserDTO fetchUser(Long userId) {
        try {
            return restTemplate.getForObject(USER_SERVICE_URL + "{" + "id" + "}", UserDTO.class, userId);
        } catch (Exception e) {
            return null; // O manejar error de servicio caído
        }
    }

    private Project populateUserDetails(Project project) {
        if (project.getUserId() != null) {
            project.setUser(fetchUser(project.getUserId()));
        }
        return project;
    }

    public Project createProject(Project project) {
        if (project.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId es obligatorio para crear el proyecto");
        }
        UserDTO user = fetchUser(project.getUserId());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Usuario no encontrado con ID: " + project.getUserId());
        }
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::populateUserDetails)
                .collect(Collectors.toList());
    }

    public java.util.Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id).map(this::populateUserDetails);
    }
}
