package com.application.taskmanager;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration Test covering:
 * - Create Task List
 * - Create Task
 * - Mark Done
 * - Move Task
 * - Filter & Sort
 * - Delete Task
 */
@SpringBootTest
@AutoConfigureMockMvc
class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void fullFlowTest() throws Exception {

        // ==============================
        // 1 Create Task List
        // POST /api/task-lists
        // ==============================

        String listResponse = mockMvc.perform(post("/api/task-lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Work\"}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Number listIdNumber = JsonPath.read(listResponse, "$.id");
        Long listId = listIdNumber.longValue();

        // ==============================
        // 2 Create Task
        // POST /api/task-lists/{id}/tasks
        // ==============================

        String taskResponse = mockMvc.perform(
                        post("/api/task-lists/" + listId + "/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Task1",
                                          "priority": "HIGH",
                                          "effort": "LOW"
                                        }
                                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.state").value("PENDING"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Number taskIdNumber = JsonPath.read(taskResponse, "$.id");
        Long taskId = taskIdNumber.longValue();

        // ==============================
        // 3 Update Status
        // PATCH /api/tasks/{id}/status
        // ==============================

        mockMvc.perform(
                        patch("/api/tasks/" + taskId + "/status")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "state": "DONE"
                                        }
                                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.state").value("DONE"));

        // ==============================
        // 4 Create Second List
        // ==============================

        String secondListResponse = mockMvc.perform(post("/api/task-lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Personal\"}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Number secondListNumber = JsonPath.read(secondListResponse, "$.id");
        Long secondListId = secondListNumber.longValue();

        // ==============================
        // 5 Move Task
        // PATCH /api/tasks/{id}/move
        // ==============================

        mockMvc.perform(
                        patch("/api/tasks/" + taskId + "/move")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "targetTaskListId": %d
                                        }
                                        """.formatted(secondListId)))
                .andExpect(status().isOk());

        // ==============================
        // 6 Get Tasks by TaskList (Filter + Sort)
        // GET /api/task-lists/{id}/tasks
        // ==============================

        mockMvc.perform(
                        get("/api/task-lists/" + secondListId + "/tasks")
                                .param("priorities", "HIGH")
                                .param("efforts", "LOW")
                                .param("sortBy", "priority,effort")
                                .param("sortDirection", "desc"))
                .andExpect(status().isOk());

        // ==============================
        // 7 Delete Task
        // DELETE /api/tasks/{id}
        // ==============================

        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isNoContent());
    }
}