package com.application.taskmanager.dto.request;

import com.application.taskmanager.enums.TaskState;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO for updating the status (state) of an existing Task.
 *
 * Functional Context:
 * - A Task transitions between different states during its lifecycle
 *   (e.g., PENDING → IN_PROGRESS → COMPLETED).
 * - This request is used specifically to change the state of a Task.
 *
 * Business Rules:
 * - Target state must be provided.
 * - State transition validation (e.g., cannot move from COMPLETED back to PENDING)
 *   should be enforced in the Service layer.
 *
 * Technical Notes:
 * - Mutable class required for JSON deserialization (Jackson).
 * - Bean validation ensures state value is provided.
 * - Enum deserialization is handled automatically by Spring.
 */
@Getter
@Setter
@NoArgsConstructor
public class UpdateTaskStatusRequest {

    /**
     * Target state of the Task.
     *
     * Functional Meaning:
     * - Represents the lifecycle stage of the Task.
     *
     * Validation:
     * - Must not be null.
     */
    @NotNull(message = "State is required")
    private TaskState state;
}