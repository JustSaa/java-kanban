package service;

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
    private FileBackedTasksManager tasksManager;
    private File testFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = File.createTempFile("test", ".csv");
        tasksManager = new FileBackedTasksManager(testFile);
    }

    @AfterEach
    void tearDown() {
        try {
            tasksManager.deleteTasks();
            if (testFile != null) {
                Files.deleteIfExists(testFile.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    void saveAndLoadEmptyTaskListNoExceptions() {
        tasksManager.save(); // Save empty task list to file
        TaskManager loadedManager = FileBackedTasksManager.load(testFile);
        assertNotNull(loadedManager);
        assertTrue(loadedManager.getTasks().isEmpty());
    }

    @Test
    void saveAndLoadEpicWithoutSubtasksNoExceptions() {
        Epic epic = new Epic("First", Status.NEW, "Description");
        tasksManager.createEpic(epic);
        tasksManager.save(); // Save task list with an epic to file
        TaskManager loadedManager = FileBackedTasksManager.load(testFile);
        assertNotNull(loadedManager);
        List<Epic> tasks = loadedManager.getEpics();
        assertEquals(1, tasks.size());
    }

    @Test
    void saveAndLoadEmptyHistoryNoExceptions() {
        tasksManager.save(); // Save empty history to file
        TaskManager loadedManager = FileBackedTasksManager.load(testFile);
        assertNotNull(loadedManager);
        assertTrue(loadedManager.getHistory().isEmpty());
    }
}
