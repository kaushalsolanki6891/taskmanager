package com.application.taskmanager.service;

import com.application.taskmanager.dto.request.CreateTaskListRequest;
import com.application.taskmanager.dto.response.TaskListResponse;

import java.util.List;

public interface TaskListService {

    TaskListResponse createTaskList(CreateTaskListRequest request);

    List<TaskListResponse> getAllTaskLists();

    TaskListResponse getTaskListById(Long id);

    void deleteTaskList(Long id);
}