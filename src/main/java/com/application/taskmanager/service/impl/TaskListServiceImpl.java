package com.application.taskmanager.service.impl;

import com.application.taskmanager.dto.request.CreateTaskListRequest;
import com.application.taskmanager.dto.response.TaskListResponse;
import com.application.taskmanager.entity.TaskList;
import com.application.taskmanager.exception.ResourceNotFoundException;
import com.application.taskmanager.repository.TaskListRepository;
import com.application.taskmanager.service.TaskListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskListServiceImpl implements TaskListService {

    private final TaskListRepository taskListRepository;

    @Override
    public TaskListResponse createTaskList(CreateTaskListRequest request) {

        TaskList taskList = TaskList.builder()
                .name(request.getName())
                .build();

        return mapToResponse(taskListRepository.save(taskList));
    }

    @Override
    public List<TaskListResponse> getAllTaskLists() {

        return taskListRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TaskListResponse getTaskListById(Long id) {

        TaskList taskList = taskListRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("TaskList not found with id: " + id));

        return mapToResponse(taskList);
    }

    @Override
    public void deleteTaskList(Long id) {

        if (!taskListRepository.existsById(id)) {
            throw new ResourceNotFoundException("TaskList not found with id: " + id);
        }

        taskListRepository.deleteById(id);
    }

    private TaskListResponse mapToResponse(TaskList taskList) {

        return TaskListResponse.builder()
                .id(taskList.getId())
                .name(taskList.getName())
                .build();
    }
}