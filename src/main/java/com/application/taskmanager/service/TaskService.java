package com.application.taskmanager.service;

import com.application.taskmanager.dto.request.CreateTaskRequest;
import com.application.taskmanager.dto.request.MoveTaskRequest;
import com.application.taskmanager.dto.request.UpdateTaskStatusRequest;
import com.application.taskmanager.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(Long taskListId, CreateTaskRequest request);

    TaskResponse getTaskById(Long taskId);

    List<TaskResponse> getAllTasks();

    TaskResponse updateTaskStatus(Long taskId, UpdateTaskStatusRequest request);

    TaskResponse moveTask(Long taskId, MoveTaskRequest request);

    void deleteTask(Long taskId);

    List<TaskResponse> getAllTasks(
            String priorities,
            String efforts,
            String sortBy,
            String sortDirection
    );

    List<TaskResponse> getTasksByTaskListId(
            Long taskListId,
            String priorities,
            String efforts,
            String sortBy,
            String sortDirection
    );
}