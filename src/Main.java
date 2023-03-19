import model.enums.StateOfTasks;
import model.service.Managers;
import model.service.taskManagers.TaskManager;
import model.model.Epic;
import model.model.Subtask;
import model.model.Task;


public class Main {

    public static void main(String[] args) {
        TaskManager m = Managers.getDefault();
        Task task01 = new Task("A", "First", StateOfTasks.NEW);
        Task task02 = new Task("B", "Second", StateOfTasks.NEW);
        Epic epic01 = new Epic("Epic", "For test", StateOfTasks.NEW);
        Subtask subtask01 = new Subtask("Subtask01", "subId01", StateOfTasks.NEW, 3);
        Subtask subtask02 = new Subtask("Subtask02", "subId02", StateOfTasks.NEW, 3);
        Subtask subtask03 = new Subtask("Subtask03", "subId03", StateOfTasks.NEW, 3);
        Epic epic02 = new Epic("Epic02", "For test02", StateOfTasks.NEW);
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
        m.getEpic(4);
        m.getEpic(3);
        m.getEpicSubtasks(3);
        for (Task task : m.getHistory()) {
            System.out.println(task);
        }
        System.out.println("//////////");
        m.removeTask(1);
        m.removeEpic(3);
        for (Task task : m.getHistory()) {
            System.out.println(task);
        }
    }
}
