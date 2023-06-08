import model.enums.Status;
import model.model.Epic;
import model.model.Subtask;
import model.model.Task;
import model.service.taskManagers.FileBackedTasksManager;
import model.service.taskManagers.InMemoryTaskManager;
import model.service.taskManagers.TaskManager;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        //TaskManager m= new InMemoryTaskManager();
        TaskManager m = new FileBackedTasksManager(new File("src/resources/task.csv"));

        Task task01 = new Task("A", Status.NEW, "First", LocalDateTime.of(2023, 6, 1, 1, 1), 22);
        Task task02 = new Task("B", Status.NEW, "Second",LocalDateTime.of(2023, 8, 1, 1, 1), 22);
        Epic epic01 = new Epic("Epic01", Status.NEW, "For test");
        Subtask subtask01 = new Subtask("Subtask01", Status.IN_PROGRESS, "subId01", 3, LocalDateTime.of(2023, 7, 1, 1, 1), 22);
        Subtask subtask02 = new Subtask("Subtask02", Status.DONE, "subId02", 3, LocalDateTime.of(2022, 12, 1, 1, 1), 20);
        Subtask subtask03 = new Subtask("Subtask03", Status.DONE, "subId02", 3, LocalDateTime.of(2000, 12, 1, 1, 1), 20);
        Epic epic02 = new Epic("Epic02", Status.NEW, "For test02");
        m.createTask(task01);
        m.createTask(task02);
        m.createEpic(epic01);
        m.createEpic(epic02);
        m.createSubtask(subtask01);
        m.createSubtask(subtask02);
        m.createSubtask(subtask03);
        System.out.println(m.getTask(1).getEndTime());
        m.getTask(2);
        m.getEpic(3);
       m.getSubtask(5);
        m.getSubtask(6);

        m.createTask(new Epic("Epic Task","Описание"));

//m.removeEpic(3);
        //m.deleteEpics();
        //m.deleteTasks();
        //m.deleteSubtasks();
       // m.updateTask(new Task());
        System.out.println("history:"+m.getHistory());

       TaskManager n = FileBackedTasksManager.load(new File("src/resources/task.csv"));
       n.createTask(new Task("Задачаnnneww","Описание"));
       // n.createTask(new Task("Задача","Описание"));
//        System.out.println(n.getTasks());
//
//        n.removeTask(1);
       System.out.println(n.getTasks());
//
        n.removeTask(2);
System.out.println(n.getTasks());
//
       System.out.println(m.getTasks());
        //System.out.println(m.getEpics());
        System.out.println();
        System.out.println(m.getEpic(3));
        //System.out.println(m.getSubtasks());
        
    }
}
