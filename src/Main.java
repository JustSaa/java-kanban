
import java.io.IOException;
import java.time.LocalDateTime;

import model.enums.Status;
import model.model.Epic;
import model.model.Subtask;
import model.model.Task;
import model.servers.KVServer;
import model.service.Managers;
import model.service.taskManagers.HttpTasksManager;
import model.service.taskManagers.TaskManager;

public class Main {

    public static void main(String[] args) throws IOException {

        new KVServer().start();

        TaskManager httpManager = Managers.getDefault();

        Task task1 = new Task("Task1", "Task1D", LocalDateTime.of(2023, 5, 1, 1, 1), 22);
        httpManager.createTask(task1);
        Task task2 = new Task("Task1", "Task1D", LocalDateTime.of(2023, 6, 1, 1, 1), 22);
        httpManager.createTask(task2);

        Epic epic1 = new Epic("Epic1", "Epic1D");
        httpManager.createEpic(epic1);

        Subtask subtask1=new Subtask("Subtask01", Status.IN_PROGRESS, "subId01", epic1.getId(),  LocalDateTime.of(2023, 7, 1, 1, 1), 22);
        httpManager.createSubtask(subtask1);

        Subtask subtask2=new Subtask("Subtask02", Status.IN_PROGRESS, "subId02", epic1.getId(),  LocalDateTime.of(2022, 12, 1, 1, 1), 22);
        httpManager.createSubtask(subtask2);

        httpManager.getTask(task1.getId());
        httpManager.getEpic(epic1.getId());
        httpManager.getSubtask(subtask1.getId());
        httpManager.getSubtask(subtask2.getId());
        httpManager.getHistory();

        HttpTasksManager refreshedHttpManager = new HttpTasksManager();
        refreshedHttpManager.load();
        refreshedHttpManager.getPrioritizedTasks();
    }
}