import model.enums.Status;
import model.model.Epic;
import model.model.Subtask;
import model.model.Task;
import model.service.taskManagers.FileBackedTasksManager;
import model.service.taskManagers.TaskManager;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        TaskManager m = new FileBackedTasksManager(new File("src/resources/task.csv"));

        Task task01 = new Task("A", Status.NEW, "First");
        Task task02 = new Task("B", Status.NEW, "Second");
        Epic epic01 = new Epic("Epic01", Status.NEW, "For test");
        Subtask subtask01 = new Subtask("Subtask01", Status.IN_PROGRESS, "subId01", 3);
        Subtask subtask02 = new Subtask("Subtask02", Status.DONE, "subId02", 3);
        Subtask subtask03 = new Subtask("Subtask03", Status.DONE, "subId03", 3);
        Epic epic02 = new Epic("Epic02", Status.NEW, "For test02");
        m.createTask(task01);
        m.createTask(task02);
        m.createEpic(epic01);
        m.createEpic(epic02);
        m.createSubtask(subtask01);
        m.createSubtask(subtask02);
        m.createSubtask(subtask03);
        m.getTask(1);
        m.getTask(2);
        m.getEpic(3);
       m.getSubtask(5);
        m.getSubtask(6);
        m.createTask(new Task("Задача","Описание"));
        //m.createTask(new Epic("Epic Task","Описание"));

m.removeEpic(3);
        //m.deleteEpics();
        //m.deleteTasks();
        //m.deleteSubtasks();
        m.createTask(new Task("Задача","Описание"));
       // m.updateTask(new Task());
        System.out.println("history:"+m.getHistory());

       TaskManager n = FileBackedTasksManager.load(new File("src/resources/task.csv"));
       n.createTask(new Task("Задачаnnneww","Описание"));
       // n.createTask(new Task("Задача","Описание"));
//        System.out.println(n.getTasks());
//
//        n.removeTask(1);
//        System.out.println(n.getTasks());
//
//        //n.removeTask(2);
//        System.out.println(n.getTasks());
//
       //System.out.println(n.getTasks());

    }
}
