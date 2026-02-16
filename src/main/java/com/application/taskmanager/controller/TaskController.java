package com.application.taskmanager.controller;

import com.application.taskmanager.dto.request.CreateTaskRequest;
import com.application.taskmanager.dto.request.MoveTaskRequest;
import com.application.taskmanager.dto.request.UpdateTaskStatusRequest;
import com.application.taskmanager.dto.response.TaskResponse;
import com.application.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;

    /**
     * POST /api/task-lists/{taskListId}/tasks
     * Creates a new Task under a specific TaskList.
     */
    @PostMapping("/task-lists/{taskListId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(
            @PathVariable Long taskListId,
            @RequestBody CreateTaskRequest request) {

        return taskService.createTask(taskListId, request);
    }

    /**
     * GET /api/task-lists/{taskListId}/tasks
     * Returns tasks for specific TaskList with optional filtering and sorting.
     */
    @GetMapping("/task-lists/{taskListId}/tasks")
    public List<TaskResponse> getTasksByTaskList(
            @PathVariable Long taskListId,
            @RequestParam(required = false) String priorities,
            @RequestParam(required = false) String efforts,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        return taskService.getTasksByTaskListId(
                taskListId,
                priorities,
                efforts,
                sortBy,
                sortDirection
        );
    }

    /**
     * GET /api/tasks
     * Returns all tasks across lists with optional filtering and sorting.
     */
    @GetMapping("/tasks")
    public List<TaskResponse> getAllTasks(
            @RequestParam(required = false) String priorities,
            @RequestParam(required = false) String efforts,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        return taskService.getAllTasks(
                priorities,
                efforts,
                sortBy,
                sortDirection
        );
    }

    /**
     * GET /api/tasks/{taskId}
     * Returns a specific task by ID.
     */
    @GetMapping("/tasks/{taskId}")
    public TaskResponse getTaskById(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId);
    }

    /**
     * PATCH /api/tasks/{taskId}/status
     * Updates task state (PENDING or DONE).
     */
    @PatchMapping("/tasks/{taskId}/status")
    public TaskResponse updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody UpdateTaskStatusRequest request) {

        return taskService.updateTaskStatus(taskId, request);
    }

    /**
     * PATCH /api/tasks/{taskId}/move
     * Moves task to another TaskList.
     */
    @PatchMapping("/tasks/{taskId}/move")
    public TaskResponse moveTask(
            @PathVariable Long taskId,
            @RequestBody MoveTaskRequest request) {

        return taskService.moveTask(taskId, request);
    }

    /**
     * DELETE /api/tasks/{taskId}
     * Deletes a task.
     */
    @DeleteMapping("/tasks/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }
}