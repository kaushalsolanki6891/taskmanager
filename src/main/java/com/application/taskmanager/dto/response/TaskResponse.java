package com.application.taskmanager.dto.response;

import com.application.taskmanager.enums.Effort;
import com.application.taskmanager.enums.Priority;
import com.application.taskmanager.enums.TaskState;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response DTO representing Task data exposed via API.
 *
 * Functional Context:
 * - Used when returning Task details.
 *
 * Design Principles:
 * - Immutable.
 * - Builder-based construction.
 * - Clear encapsulation.
 * - Decoupled from persistence entity.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskResponse {

    /**
     * Unique identifier of the Task.
     */
    private Long id;

    /**
     * Identifier of the parent TaskList.
     */
    private Long taskListId;

    /**
     * Name of the Task.
     */
    private String name;

    /**
     * Current lifecycle state of the Task.
     */
    private TaskState state;

    /**
     * Priority level of the Task.
     */
    private Priority priority;

    /**
     * Effort estimation for the Task.
     */
    private Effort effort;
}
