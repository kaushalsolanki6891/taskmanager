package com.application.taskmanager.repository.specification;

import com.application.taskmanager.entity.Task;
import com.application.taskmanager.enums.Effort;
import com.application.taskmanager.enums.Priority;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * TaskSpecification
 *
 * <p>
 * Provides dynamic filtering capabilities for {@link Task} entity
 * using Spring Data JPA {@link Specification}.
 *
 * <p><b>Functional Purpose:</b>
 * Supports optional filtering for:
 * <ul>
 *     <li>TaskList identifier</li>
 *     <li>Multiple priorities (CSV supported)</li>
 *     <li>Multiple efforts (CSV supported)</li>
 * </ul>
 *
 * <p>
 * Designed to comply with filtering requirements defined in the
 * Task Management API specification.
 *
 * <p><b>Technical Design Notes:</b>
 * <ul>
 *     <li>Implements dynamic query composition using Specification chaining.</li>
 *     <li>Ignores null/empty filters gracefully.</li>
 *     <li>Uses direct column filtering (no entity join) as Task stores taskListId explicitly.</li>
 * </ul>
 *
 * <p>
 * This class is a utility holder and is not meant to be instantiated.
 */
public final class TaskSpecification {

    private TaskSpecification() {
        // Prevent instantiation
    }

    /**
     * Builds dynamic specification based on optional filters.
     */
    public static Specification<Task> filterTasks(
            Long taskListId,
            List<Priority> priorities,
            List<Effort> efforts
    ) {

        Specification<Task> specification = null;

        if (taskListId != null) {
            specification = hasTaskListId(taskListId);
        }

        if (priorities != null && !priorities.isEmpty()) {
            specification = (specification == null)
                    ? hasPriorities(priorities)
                    : specification.and(hasPriorities(priorities));
        }

        if (efforts != null && !efforts.isEmpty()) {
            specification = (specification == null)
                    ? hasEfforts(efforts)
                    : specification.and(hasEfforts(efforts));
        }

        return specification;
    }

    /**
     * Filters by TaskList ID (direct FK column).
     */
    public static Specification<Task> hasTaskListId(Long taskListId) {

        return (root, query, cb) ->
                cb.equal(
                        root.get(Task.Fields.ID),
                        taskListId
                );
    }

    /**
     * Multi-value filter for priority.
     * Translates to SQL IN clause.
     */
    public static Specification<Task> hasPriorities(List<Priority> priorities) {

        return (root, query, cb) ->
                root.get(Task.Fields.PRIORITY).in(priorities);
    }

    /**
     * Multi-value filter for effort.
     * Translates to SQL IN clause.
     */
    public static Specification<Task> hasEfforts(List<Effort> efforts) {

        return (root, query, cb) ->
                root.get(Task.Fields.EFFORT).in(efforts);
    }
}