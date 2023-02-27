import model.enums.StateOfTasks;
import model.service.Managers;
import model.service.taskManagers.InMemoryTaskManager;
import model.service.taskManagers.TaskManager;
import model.tasks.Epic;
import model.tasks.Subtask;
import model.tasks.Task;

public class Main {

    public static void main(String[] args) {
        //Удалю при след итерации, для проверки
        TaskManager tasks = Managers.getDefault();

        //InMemoryTaskManager tasks=new InMemoryTaskManager();
        Task task01=new Task("A", "First", StateOfTasks.NEW);
        Task task02=new Task("B", "Second", StateOfTasks.NEW);
        Epic epic01=new Epic("Epic", "For test", StateOfTasks.NEW);
        tasks.createTask(task01);
        tasks.createTask(task02);
        tasks.createEpic(epic01);

        Subtask subtask01=new Subtask("Subtask", "subId", StateOfTasks.NEW, 3);
        tasks.createSubtask(subtask01);
        tasks.getAllTasks();
        tasks.getAllEpics();

        tasks.getTask(1);
        tasks.getTask(2);
        tasks.getTask(1);
        tasks.getEpic(3);
        tasks.getTask(2);
        tasks.getTask(2);
        tasks.getTask(2);
        tasks.getTask(2);
        tasks.getTask(2);
        tasks.getEpic(3);
        tasks.getEpic(3);
        tasks.getTask(2);
        System.out.println();
        for (Task task:tasks.getHistory()) {
            System.out.println(task);
        }
    }
}
