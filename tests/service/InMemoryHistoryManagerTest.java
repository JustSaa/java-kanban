package service;

import model.enums.Status;
import model.model.Task;
import model.service.historyManager.HistoryManager;
import model.service.historyManager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void addToHistoryWithNullTaskNoException() {
        Task task = null;
        assertDoesNotThrow(() -> historyManager.addToHistory(task));
    }

    @Test
    void addToHistoryWithValidTaskTaskAddedToHistory() {
        Task task = new Task("A", Status.NEW, "First", LocalDateTime.now(), 22);
        historyManager.addToHistory(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertTrue(history.contains(task));
    }

    @Test
    void addToHistoryWithDuplicateTaskDuplicateTaskReplaced() {
        Task task = new Task("A", Status.NEW, "First", LocalDateTime.now(), 22);
        historyManager.addToHistory(task);

        Task duplicateTask = new Task("A", Status.NEW, "Duplicate", LocalDateTime.now(), 22);
        historyManager.addToHistory(duplicateTask);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertTrue(history.contains(duplicateTask));
        assertFalse(history.contains(task));
    }

    @Test
    void clearHistoryHistoryIsEmpty() {
        Task task1 = new Task("A", Status.NEW, "First");
        Task task2 = new Task("A", Status.NEW, "Second");
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);

        historyManager.clearHistory();

        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void removeFromBeginningOfHistoryTaskRemoved() {
        Task task1 = new Task(1,"A", Status.NEW, "First");
        Task task2 = new Task(2,"B", Status.NEW, "Second");
        Task task3 = new Task(3,"C", Status.NEW, "Third");
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.addToHistory(task3);

        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertFalse(history.contains(task1));
    }

    @Test
    void removeFromMiddleOfHistoryTaskRemoved() {
        Task task1 = new Task(1,"A", Status.NEW, "First");
        Task task2 = new Task(2,"B", Status.NEW, "Second");
        Task task3 = new Task(3,"C", Status.NEW, "Third");
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.addToHistory(task3);

        historyManager.remove(2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertFalse(history.contains(task2));
    }

    @Test
    void removeFromEndOfHistoryTaskRemoved() {
        Task task1 = new Task(1,"A", Status.NEW, "First");
        Task task2 = new Task(2,"B", Status.NEW, "Second");
        Task task3 = new Task(3,"C", Status.NEW, "Third");
        historyManager.addToHistory(task1);
        historyManager.addToHistory(task2);
        historyManager.addToHistory(task3);

        historyManager.remove(3);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertFalse(history.contains(task3));
    }
}
