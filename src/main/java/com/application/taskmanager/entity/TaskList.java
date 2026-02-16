package com.application.taskmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "task_lists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // ---- Field Name Constants ----
    public static final class Fields {
        public static final String ID = "id";
    }
}