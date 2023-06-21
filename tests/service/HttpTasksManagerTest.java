package service;

import model.enums.Status;
import model.model.Epic;
import model.model.Subtask;
import model.model.Task;
import model.servers.KVServer;
import model.service.taskManagers.HttpTasksManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTasksManagerTest {

    protected KVServer server;
    HttpTasksManager manager;

    @BeforeEach
    public void loadInitialConditions() throws IOException {
        server = new KVServer();
        server.start();
        manager = new HttpTasksManager();
    }

    @AfterEach
    void serverStop() {
        server.stop();
    }

    @Test
    void loadFromServerTest() {

        Task task1 = new Task("Task1", "Task1D", LocalDateTime.of(2023, 5, 1, 1, 1), 22);
        manager.createTask(task1);
        Task task2 = new Task("Task1", "Task1D", LocalDateTime.of(2023, 6, 1, 1, 1), 22);
        manager.createTask(task2);

        Epic epic1 = new Epic("Epic1", "Epic1D");
        manager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask01", Status.IN_PROGRESS, "subId01", epic1.getId(), LocalDateTime.of(2023, 7, 1, 1, 1), 22);
        manager.createSubtask(subtask1);

        Subtask subtask2 = new Subtask("Subtask02", Status.IN_PROGRESS, "subId02", epic1.getId(), LocalDateTime.of(2022, 12, 1, 1, 1), 22);
        manager.createSubtask(subtask2);

        manager.getTask(task1.getId());
        manager.getEpic(epic1.getId());
        manager.getSubtask(subtask1.getId());
        manager.getSubtask(subtask2.getId());

        manager.load();
        List<Task> tasks = manager.getTasks();

        assertNotNull(tasks);
        assertEquals(2, tasks.size());

        List<Task> history = manager.getHistory();

        assertNotNull(history);
        assertEquals(4, history.size());

    }

}
