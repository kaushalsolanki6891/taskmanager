package com.application.taskmanager.repository;

import com.application.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository interface for Task persistence operations.
 *
 * Functional Responsibility:
 * - Handles CRUD operations for Task entity.
 * - Supports dynamic filtering via JPA Specifications.
 *
 * Technical Notes:
 * - JpaRepository provides standard CRUD.
 * - JpaSpecificationExecutor enables dynamic query construction.
 * - Business logic must not be implemented here.
 */
public interface TaskRepository
        extends JpaRepository<Task, Long>,
        JpaSpecificationExecutor<Task> {

}