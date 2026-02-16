package com.application.taskmanager.repository;

import com.application.taskmanager.entity.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for TaskList persistence operations.
 *
 * Functional Responsibility:
 * - Manages CRUD operations for TaskList entities.
 * - Supports existence and lookup operations.
 *
 * Technical Notes:
 * - Extends JpaRepository to leverage Spring Data JPA capabilities.
 * - Custom query methods follow Spring Data naming conventions.
 * - No business logic should be implemented here.
 */
public interface TaskListRepository extends JpaRepository<TaskList, Long> {

    /**
     * Finds a TaskList by its name.
     *
     * Functional Use Case:
     * - Used to validate uniqueness of TaskList name.
     *
     * @param name task list name
     * @return Optional TaskList
     */
    Optional<TaskList> findByName(String name);

    /**
     * Checks if a TaskList exists with the given name.
     *
     * Functional Use Case:
     * - Used for uniqueness validation before creation.
     *
     * @param name task list name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
}