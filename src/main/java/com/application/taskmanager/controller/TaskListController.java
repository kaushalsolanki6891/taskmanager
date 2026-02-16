package com.application.taskmanager.controller;

import com.application.taskmanager.dto.request.CreateTaskListRequest;
import com.application.taskmanager.dto.response.TaskListResponse;
import com.application.taskmanager.service.TaskListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-lists")
@RequiredArgsConstructor
public class TaskListController {

    private final TaskListService taskListService;

    /**
     * POST /api/task-lists
     * Creates a new TaskList.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskListResponse createTaskList(
            @RequestBody CreateTaskListRequest request) {

        return taskListService.createTaskList(request);
    }

    /**
     * GET /api/task-lists
     * Returns all task lists.
     */
    @GetMapping
    public List<TaskListResponse> getAllTaskLists() {
        return taskListService.getAllTaskLists();
    }
}