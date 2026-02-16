package com.application.taskmanager.service.impl;

import com.application.taskmanager.dto.request.CreateTaskRequest;
import com.application.taskmanager.dto.request.MoveTaskRequest;
import com.application.taskmanager.dto.request.UpdateTaskStatusRequest;
import com.application.taskmanager.dto.response.TaskResponse;
import com.application.taskmanager.entity.Task;
import com.application.taskmanager.enums.Effort;
import com.application.taskmanager.enums.Priority;
import com.application.taskmanager.enums.TaskState;
import com.application.taskmanager.exception.BadRequestException;
import com.application.taskmanager.exception.ResourceNotFoundException;
import com.application.taskmanager.repository.TaskListRepository;
import com.application.taskmanager.repository.TaskRepository;
import com.application.taskmanager.repository.specification.TaskSpecification;
import com.application.taskmanager.service.TaskService;
import com.application.taskmanager.util.EnumParser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    /**
     * Create Task inside a TaskList.
     */
    @Override
    public TaskResponse createTask(Long taskListId, CreateTaskRequest request) {

        if (!taskListRepository.existsById(taskListId)) {
            throw new ResourceNotFoundException("TaskList not found with id: " + taskListId);
        }

        long totalTasks = taskRepository.count();
        if (totalTasks >= 50) {
            throw new BadRequestException("Maximum task limit (50) reached");
        }

        Task task = Task.builder()
                .taskListId(taskListId)
                .name(request.getName())
                .priority(request.getPriority())
                .effort(request.getEffort())
                .state(TaskState.PENDING)
                .build();

        return mapToResponse(taskRepository.save(task));
    }

    @Override
    public TaskResponse getTaskById(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found with id: " + taskId));

        return mapToResponse(task);
    }

    @Override
    public List<TaskResponse> getAllTasks() {

        return taskRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse updateTaskStatus(Long taskId, UpdateTaskStatusRequest request) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found with id: " + taskId));

        task.setState(request.getState());

        return mapToResponse(taskRepository.save(task));
    }

    @Override
    public TaskResponse moveTask(Long taskId, MoveTaskRequest request) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found with id: " + taskId));

        // Validate target list exists
        if (!taskListRepository.existsById(request.getTargetTaskListId())) {
            throw new ResourceNotFoundException(
                    "Target TaskList not found with id: " + request.getTargetTaskListId());
        }

        //Just update the foreign key ID
        task.setTaskListId(request.getTargetTaskListId());

        return mapToResponse(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long taskId) {

        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with id: " + taskId);
        }

        taskRepository.deleteById(taskId);
    }

    @Override
    public List<TaskResponse> getAllTasks(
            String prioritiesCsv,
            String effortsCsv,
            String sortBy,
            String sortDirection
    ) {

        List<Priority> priorities =
                EnumParser.parseCsv(prioritiesCsv, Priority.class);

        List<Effort> efforts =
                EnumParser.parseCsv(effortsCsv, Effort.class);

        Specification<Task> spec =
                TaskSpecification.filterTasks(null, priorities, efforts);

        Sort sort = buildSort(sortBy, sortDirection);

        return taskRepository.findAll(spec, sort)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<TaskResponse> getTasksByTaskListId(
            Long taskListId,
            String prioritiesCsv,
            String effortsCsv,
            String sortBy,
            String sortDirection
    ) {

        if (!taskListRepository.existsById(taskListId)) {
            throw new ResourceNotFoundException(
                    "TaskList not found with id: " + taskListId);
        }

        List<Priority> priorities =
                EnumParser.parseCsv(prioritiesCsv, Priority.class);

        List<Effort> efforts =
                EnumParser.parseCsv(effortsCsv, Effort.class);

        Specification<Task> spec =
                TaskSpecification.filterTasks(taskListId, priorities, efforts);

        Sort sort = buildSort(sortBy, sortDirection);

        return taskRepository.findAll(spec, sort)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Entity → DTO mapping
     */
    private TaskResponse mapToResponse(Task task) {

        return TaskResponse.builder()
                .id(task.getId())
                .taskListId(task.getTaskListId())
                .name(task.getName())
                .state(task.getState())
                .priority(task.getPriority())
                .effort(task.getEffort())
                .build();
    }

    private Sort buildSort(String sortBy, String sortDirection) {

        Sort.Direction direction =
                "desc".equalsIgnoreCase(sortDirection)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        if (sortBy == null || sortBy.isBlank()) {
            return Sort.by(direction, "id"); // default sort
        }

        String[] fields = sortBy.split(",");

        List<Sort.Order> orders = new ArrayList<>();

        for (String field : fields) {
            orders.add(new Sort.Order(direction, field.trim()));
        }

        // Deterministic ordering
        orders.add(new Sort.Order(direction, "id"));

        return Sort.by(orders);
    }
}
