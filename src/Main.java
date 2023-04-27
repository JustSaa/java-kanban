import model.enums.Status;
import model.model.Subtask;
import model.model.Task;
import model.service.Managers;
import model.service.taskManagers.FileBackedTasksManager;
import model.service.taskManagers.TaskManager;
import model.model.Epic;

import java.io.File;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {

//        Task task01 = new Task("A", Status.NEW, "First");
//        Task task02 = new Task("B", Status.NEW, "Second");
//        Epic epic01 = new Epic("Epic01", Status.NEW, "For test");
//        Subtask subtask01 = new Subtask("Subtask01", Status.IN_PROGRESS, "subId01", 3);
//        Subtask subtask02 = new Subtask("Subtask02", Status.DONE, "subId02", 3);
//        Subtask subtask03 = new Subtask("Subtask03", Status.DONE, "subId03", 3);
//        Epic epic02 = new Epic("Epic02", Status.NEW, "For test02");
//        m.createTask(task01);
//        m.createTask(task02);
//        m.createEpic(epic01);
//        m.createEpic(epic02);
//        m.createSubtask(subtask01);
//        m.createSubtask(subtask02);
//        m.createSubtask(subtask03);
//        m.getTask(1);
//        m.getEpic(3);


      TaskManager m = FileBackedTasksManager.load(new File("src/resources/task.csv"));
      System.out.println(m.getHistory());

    }
}
