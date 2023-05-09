package test.service;

import model.service.taskManagers.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;

public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    // Тесты для sortedTasks находится в TaskManager timeIntervalCheck() и shouldSortTasks()
    @BeforeEach
    public void setUp() {
        this.taskManager = new InMemoryTaskManager();
    }
}