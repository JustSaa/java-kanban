package models;

import model.enums.Status;
import model.model.Epic;
import model.model.Subtask;
import model.service.taskManagers.InMemoryTaskManager;
import model.service.taskManagers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {

    private Epic epic;
    private TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        epic = new Epic("epicName", Status.NEW, "epicDescription");
        taskManager = new InMemoryTaskManager();
    }
    @Test
    public void testingForEpicEmptyListOfSubtasks() {
        taskManager.createEpic(epic);
        Status epicsStatus = epic.getStatus();
        assertEquals(epicsStatus, Status.NEW);
    }

    @Test
    public void testingForEpicAllSubtasksWithNewStatus() {
        taskManager.createEpic(epic);
        int epicId=epic.getId();
        Subtask subTask = new Subtask("Собрать ящики", Status.NEW, "Все самое любимое в первую очередь", epicId);
        Subtask subTask1 = new Subtask("a", Status.NEW, "b", epicId);
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask1);
        Status epicsStatus = epic.getStatus();
        assertEquals(epicsStatus, Status.NEW);
    }

    @Test
    public void testingForEpicAllSubtasksWithDoneStatus() {
        taskManager.createEpic(epic);
        int epicId=epic.getId();
        Subtask subTask = new Subtask("Собрать ящики", Status.DONE, "Все самое любимое в первую очередь", epicId);
        Subtask subTask1 = new Subtask("a", Status.DONE, "b", epicId);
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask1);
        Status epicsStatus = epic.getStatus();
        assertEquals(epicsStatus, Status.DONE);
    }

    @Test
    public void testingForEpicSubtasksWithNewAndDoneStatuses() {
        taskManager.createEpic(epic);
        int epicId=epic.getId();
        Subtask subTask = new Subtask("Собрать ящики", Status.DONE, "Все самое любимое в первую очередь", epicId);
        Subtask subTask1 = new Subtask("a", Status.NEW, "b", epicId);
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask1);
        Status epicsStatus = epic.getStatus();
        assertEquals(epicsStatus, Status.IN_PROGRESS);
    }

    @Test
    public void testingForEpicSubtasksWithStatusInProgress() {
        taskManager.createEpic(epic);
        Subtask subTask = new Subtask("Собрать ящики", Status.IN_PROGRESS, "Все самое любимое в первую очередь", 1);
        Subtask subTask1 = new Subtask("a", Status.IN_PROGRESS, "b", 1);
        taskManager.createSubtask(subTask);
        taskManager.createSubtask(subTask1);
        Status epicsStatus = taskManager.getEpic(1).getStatus();
        assertEquals(epicsStatus, Status.IN_PROGRESS);
    }
}
