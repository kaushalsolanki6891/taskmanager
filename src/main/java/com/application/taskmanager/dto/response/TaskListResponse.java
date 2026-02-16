package com.application.taskmanager.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Response DTO representing TaskList data.
 *
 * Functional Context:
 * - Returned when a TaskList is created or retrieved.
 *
 * Design Principles:
 * - Encapsulated fields.
 * - Does not expose entity directly.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskListResponse {

    /**
     * Unique identifier of the TaskList.
     */
    private Long id;

    /**
     * Display name of the TaskList.
     */
    private String name;
}