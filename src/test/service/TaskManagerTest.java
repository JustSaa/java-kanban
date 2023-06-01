package test.service;

import model.enums.Status;
import model.model.Epic;
import model.model.Subtask;
import model.model.Task;
import model.service.taskManagers.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;
    private List<Subtask> subtasks;
    private final String error = "Epic имеет статус ";

    protected List<Subtask> createSubtasks(Status status1, Status status2, Status status3, int epicId) {
        Subtask subtask1 = new Subtask("subtask1Name", status1, "subtask1Description", epicId,
                LocalDateTime.of(2021, 1, 1, 10, 0), Duration.ofMinutes(100));
        Subtask subtask2 = new Subtask("subtask2Name", status2, "subtask2Description", epicId,
                LocalDateTime.of(2021, 1, 1, 10, 0).plusHours(10), Duration.ofMinutes(10));
        Subtask subtask3 = new Subtask("subtask3Name", status3, "subtask3Description", epicId,
                LocalDateTime.of(2021, 1, 1, 10, 0).minusHours(10), Duration.ofMinutes(50));

        List<Subtask> subtasks = new ArrayList<>();

        subtasks.add(subtask1);
        subtasks.add(subtask2);
        subtasks.add(subtask3);
        return subtasks;
    }

    @BeforeEach
    public void beforeEach() {
        epic = new Epic("EpicBeforeEach", Status.NEW, "epicDescription");
        task = new Task("TaskBeforeEach", Status.NEW, "taskDescription");
    }

    @Test
    public void epicShouldHasTaskStatusNew() {
        taskManager.createEpic(epic);
        final int epicId = epic.getEpicId();


        assertEquals(Status.NEW, epic.getStatus(), error + " вместо NEW, когда список подзадач Epic'а пустой");
        subtasks = createSubtasks(Status.NEW, Status.NEW, Status.NEW, epicId);

        taskManager.createSubtask(subtasks.get(0));
        taskManager.createSubtask(subtasks.get(1));
        taskManager.createSubtask(subtasks.get(2));

        assertEquals(Status.NEW, epic.getStatus(), error + "вместо NEW, когда все подзадачи имеет статус NEW");
    }

    @Test
    public void epicShouldHasTaskStatusInProgress() {
        taskManager.createEpic(epic);
        final int epicId = epic.getEpicId();
        subtasks = createSubtasks(Status.NEW, Status.IN_PROGRESS, Status.NEW, epicId);

        Subtask subtask1 = subtasks.get(0);
        Subtask subtask2 = subtasks.get(1);
        Subtask subtask3 = subtasks.get(2);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        assertEquals(Status.IN_PROGRESS, epic.getStatus()
                , error
                        + epic.getStatus().toString()
                        + "вместо IN_PROGRESS, когда одна из подзадач IN_PROGRESS, остальные NEW"
        );

        subtask1.setStatus(Status.DONE);
        subtask3.setStatus(Status.DONE);


        assertEquals(Status.IN_PROGRESS, epic.getStatus()
                , error
                        + epic.getStatus().toString()
                        + "вместо IN_PROGRESS, когда одна из подзадач IN_PROGRESS, остальные DONE"
        );

        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.NEW);


        assertEquals(Status.IN_PROGRESS, epic.getStatus()
                , error
                        + epic.getStatus().toString()
                        + "вместо IN_PROGRESS, когда одна из подзадач DONE, остальные NEW"
        );

        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask3.setStatus(Status.IN_PROGRESS);


        assertEquals(Status.IN_PROGRESS, epic.getStatus()
                , error
                        + epic.getStatus().toString()
                        + "вместо IN_PROGRESS, когда все подзадачи IN_PROGRESS"
        );
    }

    @Test
    public void epicShouldHasTaskStatusDone() {
        taskManager.createEpic(epic);
        final int epicId = epic.getEpicId();
        subtasks = createSubtasks(Status.DONE, Status.DONE, Status.DONE, epicId);
        Subtask subtask1 = subtasks.get(0);
        Subtask subtask2 = subtasks.get(1);
        Subtask subtask3 = subtasks.get(2);

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);

        assertEquals(Status.DONE, epic.getStatus(), error + epic.getStatus().toString() + " вместо DONE");
    }

    @Test
    public void subtaskMustHaveEpic() {
        subtask = new Subtask("subtaskName", Status.NEW, "subtaskDescription", 1);

        assertThrows(NullPointerException.class, () -> taskManager.createSubtask(subtask));
    }

    @Test
    public void shouldReturnTasks() {
        taskManager.createEpic(epic);
        taskManager.createTask(task);
        Subtask subtask = new Subtask("subtaskName", Status.DONE, "subtaskDescription", epic.getEpicId());
        taskManager.createSubtask(subtask);

        final List<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size());

        final List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size());

        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(1, subtasks.size());
    }

    @Test
    public void shouldReturnNullTasks() {
        final List<Task> tasks = taskManager.getTasks();
        assertEquals(0, tasks.size());

        final List<Epic> epics = taskManager.getEpics();
        assertEquals(0, epics.size());

        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(0, subtasks.size());
    }

    @Test
    public void shouldRemoveAllTasks() {
        taskManager.createEpic(epic);
        taskManager.createTask(task);
        Subtask subtask = new Subtask("subtaskName", Status.DONE, "subtaskDescription", epic.getEpicId());
        taskManager.createSubtask(subtask);

        taskManager.deleteTasks();
        final List<Task> tasks = taskManager.getTasks();
        assertEquals(0, tasks.size());

        taskManager.deleteEpics();
        final List<Epic> epics = taskManager.getEpics();
        assertEquals(0, epics.size());

        taskManager.deleteSubtasks();
        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(0, subtasks.size());

    }

    @Test
    public void shouldDeleteTaskById() {
        taskManager.createEpic(epic);
        Epic epic1 = new Epic("2", Status.NEW, "2");
        taskManager.createEpic(epic1);
        taskManager.createTask(task);
        Task task1 = new Task("1", Status.NEW, "2");
        taskManager.createTask(task1);
        Subtask subtask = new Subtask("subtaskName", Status.DONE, "subtaskDescription", epic.getEpicId());
        Subtask subtask1 = new Subtask("subtaskName", Status.DONE, "subtaskDescription", epic.getEpicId());
        taskManager.createSubtask(subtask);
        taskManager.createSubtask(subtask1);

        taskManager.removeTask(task1.getId());
        taskManager.removeEpic(epic1.getId());
        taskManager.removeSubtasks(subtask1.getId());

        final List<Task> tasks = taskManager.getTasks();
        final List<Epic> epics = taskManager.getEpics();
        final List<Subtask> subtasks = taskManager.getSubtasks();

        assertEquals(1, tasks.size());
        assertEquals(1, epics.size());
        assertEquals(1, subtasks.size());
    }

    @Test
    public void shouldNotDeleteNoExistingTaskById() {
        taskManager.createEpic(epic);
        taskManager.createTask(task);
        Subtask subtask = new Subtask("subtaskName", Status.DONE, "subtaskDescription", epic.getEpicId());
        taskManager.createSubtask(subtask);

        taskManager.removeTask(15);
        final List<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size(), "Произошло удаление задачи по неверному id.");

        taskManager.removeEpic(12);
        final List<Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Произошло удаление задачи по неверному id.");

        taskManager.removeSubtasks(13);
        final List<Subtask> subtasks = taskManager.getSubtasks();
        assertEquals(1, subtasks.size(), "Произошло удаление задачи по неверному id.");
    }

    @Test
    public void addNewTask() {
        taskManager.createTask(task);
        int taskId = task.getId();

        assertEquals(task, taskManager.getTask(taskId), "Manager does not contain added task");
        assertTrue(taskManager.getTasks().contains(task));
    }

    @Test
    public void addNewSubtask() {
        taskManager.createEpic(epic);
        final int epicId = epic.getEpicId();
        subtask = new Subtask("subtaskName", Status.NEW, "subtaskDescription", epicId);
        taskManager.createSubtask(subtask);
        int subtaskId = subtask.getId();

        assertEquals(subtask, taskManager.getSubtask(subtaskId), "Manager does not contain added subtask");
        assertTrue(taskManager.getSubtasks().contains(subtask));
    }

    @Test
    public void addNewEpic() {
        taskManager.createEpic(epic);
        final int epicId = epic.getEpicId();

        assertEquals(epic, taskManager.getEpic(epicId), "Manager does not contain added epic");
        assertTrue(taskManager.getEpics().contains(epic));
    }

    @Test
    public void updateTasks() {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        taskManager.deleteSubtasks();

        Epic updateEpic = new Epic("EpicName", Status.NEW, "desc");
        taskManager.createEpic(updateEpic);
        int epicId = updateEpic.getEpicId();
        Subtask updateSubtask = new Subtask("SubtaskName", Status.NEW, "desc", epicId);
        taskManager.createSubtask(updateSubtask);
        Task updateTask = new Task("TaskName", Status.NEW, "taskDescription");
        taskManager.createTask(updateTask);

        updateTask.setStatus(Status.DONE);
        taskManager.updateTask(updateTask);

        updateSubtask.setStatus(Status.DONE);
        taskManager.updateSubtask(updateSubtask);
        updateEpic.setStatus(Status.DONE);
        taskManager.updateEpic(updateEpic);

        assertTrue(taskManager.getTasks().contains(updateTask), "Does not update existing task");
        assertTrue(taskManager.getSubtasks().contains(updateSubtask), "Does not update existing subtask");
        assertTrue(taskManager.getEpics().contains(updateEpic), "Does not update existing epic");
    }

    // Test for setHistory
    @Test
    public void setHistory() {
        taskManager.createTask(task);
        int taskId = task.getId();
        taskManager.createEpic(epic);
        int epicId = epic.getEpicId();
        Subtask subtask1 = new Subtask("SubtaskName", Status.NEW, "desc", epicId);
        taskManager.createSubtask(subtask1);
        int subId = subtask1.getId();
        taskManager.getTask(taskId);
        taskManager.getEpic(epicId);
        taskManager.getSubtask(subId);

        List<Task> history = List.of(task, epic, subtask1);

        assertEquals(history, taskManager.getHistory());
    }

    @Test
    public void timeIntervalCheck() {
        Task taskHasTime = new Task("TaskTime", Status.NEW, "Task has time",
                LocalDateTime.of(2022, 1, 10, 9, 0), Duration.ofMinutes(60));
        taskManager.createTask(taskHasTime);

        Task taskHasTime2 = new Task("TaskTime", Status.NEW, "Task has time",
                LocalDateTime.of(2022, 1, 10, 8, 0), Duration.ofMinutes(90));

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> taskManager.createTask(taskHasTime2)
        );
        assertEquals("Это время уже занято. Выберите другое время", ex.getMessage());
    }

    @Test
    public void shouldSortTasks() {
        Task task1 = new Task("TaskTime", Status.NEW, "Task has time",
                LocalDateTime.of(2022, 1, 10, 10, 0), Duration.ofMinutes(60));
        taskManager.createTask(task1);
        Epic epic = new Epic("Task1", "Task1_desc");
        taskManager.createEpic(epic);
        int epicId = epic.getId();
        Subtask subtask1 = new Subtask("TaskTimeSub", Status.NEW, "Task has time1", epicId,
                LocalDateTime.of(2022, 3, 10, 10, 0), Duration.ofMinutes(60));
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("TaskTime", Status.NEW, "Task has time", epicId,
                LocalDateTime.of(2022, 2, 10, 10, 0), Duration.ofMinutes(60));
        taskManager.createSubtask(subtask2);

        Set<Task> expectedPrioritizedTasks = Set.of(task1, subtask2, subtask1, epic);


        Set<Task> actualPrioritizedTasks = taskManager.getListOfTasksSortedByTime();

        assertEquals(expectedPrioritizedTasks, actualPrioritizedTasks);
    }

    @Test
    public void shouldSetEpicsTimeAndDuration() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 12, 20, 20, 10, 10);

        taskManager.createEpic(epic);
        int id = epic.getId();
        subtask = new Subtask("subtask", Status.NEW, "subtaskDescription",
                id, dateTime, Duration.ofMinutes(10));

        taskManager.createSubtask(subtask);

        assertEquals(dateTime, epic.getStartTime());
        assertEquals(Duration.ofMinutes(10), epic.getDuration());
        assertEquals(dateTime.plusMinutes(10), epic.getEndTime());
    }
}