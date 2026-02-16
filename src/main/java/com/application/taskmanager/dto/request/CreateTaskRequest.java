package com.application.taskmanager.dto.request;

import com.application.taskmanager.enums.Effort;
import com.application.taskmanager.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO for creating a new Task inside a Task List.
 *
 * Functional Context:
 * - A Task represents a unit of work within a Task List.
 * - Each Task must have a name, priority level, and effort estimation.
 *
 * Business Rules:
 * - Task name must not be blank.
 * - Priority is mandatory.
 * - Effort estimation is mandatory.
 * - Default state (e.g., PENDING) is assigned in Service layer.
 *
 * Technical Notes:
 * - Mutable class required for JSON deserialization (Jackson).
 * - Validation annotations enforce request-level constraints.
 * - Cross-field and business validations are handled in Service layer.
 */
@Getter
@Setter
@NoArgsConstructor
public class CreateTaskRequest {

    /**
     * Descriptive name of the Task.
     *
     * Validation:
     * - Cannot be null or blank.
     */
    @NotBlank(message = "Task name must not be blank")
    private String name;

    /**
     * Priority level assigned to the Task.
     *
     * Functional Meaning:
     * - Indicates urgency or importance (e.g., LOW, MEDIUM, HIGH).
     *
     * Validation:
     * - Must be provided by client.
     */
    @NotNull(message = "Priority is required")
    private Priority priority;

    /**
     * Estimated effort required to complete the Task.
     *
     * Functional Meaning:
     * - Represents size/complexity of work (e.g., SMALL, MEDIUM, LARGE).
     *
     * Validation:
     * - Must be provided by client.
     */
    @NotNull(message = "Effort is required")
    private Effort effort;
}