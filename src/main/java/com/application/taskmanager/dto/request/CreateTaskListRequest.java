package com.application.taskmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO for creating a new Task List.
 *
 * Functional Context:
 * - A Task List represents a logical container for grouping tasks
 *   (e.g., Backlog, Sprint 1, Personal Tasks).
 * - The name is mandatory and provided by the client.
 *
 * Validation Rules:
 * - Name must not be null or blank.
 *
 * Technical Notes:
 * - Mutable class required for JSON deserialization by Jackson.
 * - Bean validation annotations are used for automatic request validation.
 * - Additional business validations (e.g., uniqueness check)
 *   should be handled in the Service layer.
 */
@Getter
@Setter
@NoArgsConstructor
public class CreateTaskListRequest {

    /**
     * Name of the Task List.
     *
     * Business Rule:
     * - Must not be blank.
     * - Should be unique within a given workspace (validated in service).
     */
    @NotBlank(message = "Task list name must not be blank")
    private String name;
}