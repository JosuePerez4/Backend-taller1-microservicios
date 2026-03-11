package com.example.etoms.controller;

import com.example.etoms.model.StatusRequest;
import com.example.etoms.model.Task;
import com.example.etoms.model.TaskRequest;
import com.example.etoms.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Task management endpoints")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    @Operation(summary = "Create a new task", description = "Creates a new task with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid task data")
    })
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest request) {
        Task created = taskService.createTask(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all tasks", description = "Retrieves a list of all tasks in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get tasks by user", description = "Retrieves all tasks assigned to a specific user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user tasks")
    public ResponseEntity<List<Task>> getTasksByUser(
            @Parameter(description = "User ID") @PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getTasksByUserId(userId));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update task status", description = "Updates the status of a specific task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found"),
        @ApiResponse(responseCode = "400", description = "Invalid status request")
    })
    public ResponseEntity<Task> updateTaskStatus(
            @Parameter(description = "Task ID") @PathVariable Long id,
            @RequestBody StatusRequest request) {
        Task updated = taskService.updateTaskStatus(id, request);
        return ResponseEntity.ok(updated);
    }
}
