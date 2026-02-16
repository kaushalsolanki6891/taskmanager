package com.application.taskmanager.entity;

import com.application.taskmanager.enums.Effort;
import com.application.taskmanager.enums.Priority;
import com.application.taskmanager.enums.TaskState;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long taskListId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskState state;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Effort effort;

    // ---- Field Name Constants ----
    public static final class Fields {
        public static final String ID = "id";
        public static final String TASK_LIST_ID = "taskListId";
        public static final String STATE = "state";
        public static final String PRIORITY = "priority";
        public static final String EFFORT = "effort";
    }
}