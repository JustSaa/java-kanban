package test.service;

import model.enums.Status;
import model.model.Epic;
import model.model.Task;
import model.service.taskManagers.FileBackedTasksManager;
import model.service.taskManagers.TaskManager;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest {
    private static final String TEST_FILE_PATH = "test_tasks.csv";
    private FileBackedTasksManager tasksManager;

    @BeforeEach
    void setUp() {
        File testFile = new File(TEST_FILE_PATH);
        tasksManager = new FileBackedTasksManager(testFile);
    }

    @AfterEach
    void tearDown() {
        try {
            tasksManager.deleteTasks();
            Files.deleteIfExists(Path.of(TEST_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void saveAndLoadEmptyTaskListNoExceptions() {
        tasksManager.save(); // Save empty task list to file
        TaskManager loadedManager = FileBackedTasksManager.load(new File(TEST_FILE_PATH));
        assertNotNull(loadedManager);
        assertTrue(loadedManager.getTasks().isEmpty());
    }

    @Test
    void saveAndLoadEpicWithoutSubtasksNoExceptions() {
        Epic epic = new Epic("First", Status.NEW, "Description");
        tasksManager.createEpic(epic);
        tasksManager.save(); // Save task list with an epic to file
        TaskManager loadedManager = FileBackedTasksManager.load(new File(TEST_FILE_PATH));
        assertNotNull(loadedManager);
        List<Epic> tasks = loadedManager.getEpics();
        assertEquals(1, tasks.size());
    }


    @Test
    void saveAndLoadEmptyHistoryNoExceptions() {
        tasksManager.save(); // Save empty history to file
        TaskManager loadedManager = FileBackedTasksManager.load(new File(TEST_FILE_PATH));
        assertNotNull(loadedManager);
        assertTrue(loadedManager.getHistory().isEmpty());
    }
}
