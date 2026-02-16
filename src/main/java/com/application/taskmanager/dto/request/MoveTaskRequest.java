package com.application.taskmanager.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO for moving a Task from one Task List to another.
 *
 * Functional Context:
 * - A Task may need to be reorganized across different Task Lists
 *   (e.g., moving from Backlog to Sprint).
 * - This request captures the target Task List identifier.
 *
 * Business Rules:
 * - Target Task List ID must be provided.
 * - Target Task List must exist (validated in Service layer).
 * - Moving to the same Task List should be either prevented
 *   or handled idempotently (decided in Service layer).
 *
 * Technical Notes:
 * - Mutable class required for JSON deserialization (Jackson).
 * - Only structural validation is handled here.
 * - Referential integrity validation belongs to Service layer.
 */
@Getter
@Setter
@NoArgsConstructor
public class MoveTaskRequest {

    /**
     * Identifier of the target Task List.
     *
     * Functional Meaning:
     * - Specifies where the Task should be moved.
     *
     * Validation:
     * - Must not be null.
     */
    @NotNull(message = "Target taskListId is required")
    private Long targetTaskListId;
}