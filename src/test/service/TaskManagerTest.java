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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                LocalDateTime.now(), Duration.ofMinutes(100));
        Subtask subtask2 = new Subtask("subtask2Name", status2, "subtask2Description", epicId,
                LocalDateTime.now().plusHours(10), Duration.ofMinutes(10));
        Subtask subtask3 = new Subtask("subtask3Name", status3, "subtask3Description", epicId,
                LocalDateTime.now().minusHours(10), Duration.ofMinutes(50));

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

        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask3);

        assertEquals(Status.IN_PROGRESS, epic.getStatus()
                , error
                        + epic.getStatus().toString()
                        + "вместо IN_PROGRESS, когда одна из подзадач IN_PROGRESS, остальные DONE"
        );

        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.NEW);

        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus()
                , error
                        + epic.getStatus().toString()
                        + "вместо IN_PROGRESS, когда одна из подзадач DONE, остальные NEW"
        );

        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        subtask3.setStatus(Status.IN_PROGRESS);

        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);

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
    public void addSubtaskToAnEpic() {
        taskManager.createEpic(epic);
        final int epicId = epic.getEpicId();

        Subtask subtask = new Subtask("subtaskName", Status.DONE,"subtaskDescription",  epicId);

        taskManager.createSubtask(subtask);
        final int subtaskId = subtask.getEpicId();

        assertTrue(epic.getSubtasks().contains(subtaskId)
                , "ID Subtask'а не был добавлен в список подзадач Epic'а");
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
        subtask = new Subtask("subtaskName", Status.NEW,"subtaskDescription",  epicId);
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
    public void shouldNotReturnNoExistingTasks() {
        // No-existing task.Task
        assertNull(taskManager.getTask(1), "Returns no-existing task");

        // No-existing task.Subtask
        assertNull(taskManager.getSubtask(1), "Returns no-existing subtask");

        // No-existing task.Epic
        assertNull(taskManager.getEpic(1), "Returns no-existing epic");
    }

    @Test
    public void subtaskMustHaveEpic() {
        subtask = new Subtask("subtaskName", Status.NEW,"subtaskDescription",  1);

        taskManager.createSubtask(subtask);
        int subtaskId = subtask.getId();
        // Must return -1 if there is no such Epic with specified epicId
        assertNull(taskManager.getSubtask(subtaskId));
    }


    // Tests for update functions
    @Test
    public void shouldNotUpdateNoExistingTask() {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        taskManager.deleteSubtasks();
        taskManager.createTask(new Task("1", Status.NEW,"2"));
        taskManager.createEpic(new Epic("2", Status.NEW,"2"));
        int epicId = taskManager.getEpic(2).getEpicId();
        taskManager.createSubtask(new Subtask("1", Status.NEW,"2",  epicId));
        // No-existing task.Task
        taskManager.updateTask(new Task("very funny taskName",  Status.NEW, "taskDescription"));
        List<String> names = taskManager
                .getTasks()
                .stream()
                .map(Task::getTitle)
                .collect(Collectors.toList());
        assertFalse(names.contains("taskNames"), "Updates no-existing task");
        // No-existing task.Subtask
        taskManager.updateSubtask(new Subtask("very funny subtaskName", Status.NEW,"desc",  1));
        names = taskManager.getSubtasks()
                .stream()
                .map(Subtask::getTitle)
                .collect(Collectors.toList());
        assertFalse(names.contains("subtaskNames"), "Updates no-existing subtask");
        // No-existing task.Epic
        taskManager.updateEpic(new Epic("very funny epicName",  Status.NEW,"desc"));
        names = taskManager.getEpics()
                .stream()
                .map(Epic::getTitle)
                .collect(Collectors.toList());
        assertFalse(names.contains("epicNames"), "Updates no-existing epic");
    }

    @Test
    public void updateTasks() {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        taskManager.deleteSubtasks();

        Epic updateEpic = new Epic("EpicName",Status.NEW,"desc");
        taskManager.createEpic(updateEpic);
        int epicId = updateEpic.getEpicId();
        Subtask updateSubtask = new Subtask("SubtaskName", Status.NEW,"desc",  epicId);
        taskManager.createSubtask(updateSubtask);
        Task updateTask = new Task("TaskName", Status.NEW,"taskDescription");
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

    // Tests for delete functions
    @Test
    public void shouldNotDeleteNoExistingTask() {
        // No-existing task.Task
        taskManager.removeTask(1);
        assertNull(taskManager.getTask(1), "Deletes no-existing task");
        // No-existing task.Subtask
        taskManager.removeSubtasks(1);
        assertNull(taskManager.getSubtask(1), "Deletes no-existing subtask");
        // No-existing task.Epic
        taskManager.removeEpic(1);
        assertNull(taskManager.getEpic(1), "Deletes no-existing epic");
    }

    // Test for setHistory
    @Test
    public void setHistory() {
        taskManager.createTask(task);
        int taskId = task.getId();
        taskManager.createEpic(epic);
        int epicId = epic.getEpicId();
        Subtask subtask1 = new Subtask("SubtaskName", Status.NEW,"desc",  epicId);
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
        LocalDateTime dateTime = LocalDateTime.of(2022, 12, 20, 20, 10, 10);
        Task task1 = new Task("task 1", Status.NEW,"taskDescription", dateTime, Duration.ofMinutes(40));
        Task task2 = new Task("task 2", Status.NEW,"taskDescription",
                dateTime.plusMinutes(30), Duration.ofMinutes(10));
        Task task3 = new Task("task 3", Status.NEW,"taskDescription",
                dateTime.minusMinutes(10), Duration.ofMinutes(10));
        Task task4 = new Task("task 4", Status.NEW,"taskDescription",
                dateTime.minusMinutes(60), Duration.ofMinutes(60));
        Task task5 = new Task("task 5", Status.NEW, "taskDescription");
        Task task6 = new Task("task 6", Status.NEW, "taskDescription");

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.createTask(task6);

        List<Task> check = List.of(task1, task3, task5, task6);

        assertEquals(check, taskManager.getTasks());
    }

    @Test
    public void shouldSortTasks() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 12, 20, 20, 10, 10);
        Task task1 = new Task("task 1", Status.NEW,"taskDescription",
                 dateTime, Duration.ofMinutes(40));
        Task task2 = new Task("task 2", Status.NEW,"taskDescription",
                dateTime.plusMinutes(40), Duration.ofMinutes(10));
        Task task3 = new Task("task 3", Status.NEW,"taskDescription",
                dateTime.minusMinutes(10), Duration.ofMinutes(10));
        Task task4 = new Task("task 4", Status.NEW,"taskDescription",
                dateTime.minusMinutes(60), Duration.ofMinutes(10));
        Task task5 = new Task("task 5", Status.NEW, "taskDescription");
        Task task6 = new Task("task 6", Status.NEW, "taskDescription");

        taskManager.createTask(task4);
        taskManager.createTask(task3);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task5);
        taskManager.createTask(task6);

        List<Integer> check = List.of(task4.getId(),
                task3.getId(),
                task1.getId(),
                task2.getId(),
                task5.getId(),
                task6.getId());

        List<Integer> actual = taskManager
                .getListOfTasksSortedByTime()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList());

        assertEquals(check, actual);
    }

    @Test
    public void shouldSetEpicsTimeAndDuration() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 12, 20, 20, 10, 10);

        taskManager.createEpic(epic);
        int id = epic.getId();
        subtask = new Subtask("subtask", Status.NEW,"subtaskDescription",
                 id, dateTime, Duration.ofMinutes(10));

        taskManager.createSubtask(subtask);

        assertEquals(dateTime, epic.getStartTime());
        assertEquals(Duration.ofMinutes(10), epic.getDuration());
        assertEquals(Optional.of(dateTime.plusMinutes(10)), epic.getEndTime());
    }
}