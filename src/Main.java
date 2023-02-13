import model.enums.StateOfTasks;
import model.tasks.Epic;
import model.tasks.Subtask;
import model.tasks.Task;
import model.service.Manager;

public class Main {

    public static void main(String[] args) {
// Создано для самопроверки удалю при след итерации
        Manager manager = new Manager();
        //Создание. Сам объект должен передаваться в качестве параметра.
        Task task1 = new Task("Помыть посуду", "На кухня вымыть все", StateOfTasks.NEW);
        manager.createTask(task1);
        Task task2 = new Task("Go to the GYM", "Do some exercises at GYM", StateOfTasks.NEW);
        manager.createTask(task2);
        Epic epic0 = new Epic("Переезд", "В новую кв", StateOfTasks.IN_PROGRESS);
        manager.createEpic(epic0);
        Subtask subtask01 = new Subtask("Package all things", "Put in the box stuff", StateOfTasks.DONE, epic0.getEpicId());
        manager.createSubtask(subtask01);
        Subtask subtask02 = new Subtask("Find vehicle", "Call in service", StateOfTasks.NEW, epic0.getEpicId());
        manager.createSubtask(subtask02);
        Epic epic1 = new Epic("Buy a new phone", "Iphone 15");
        manager.createEpic(epic1);
        Subtask subtask11 = new Subtask("Go to the market", "Market placed at st.Tchaikovsky", StateOfTasks.NEW, epic1.getEpicId());
        manager.createSubtask(subtask11);
        //Получение списка всех задач
        manager.getAllTasks();
        manager.getAllEpics();
        manager.getAllSubtasks();

        //Получение по идентификатору.
        System.out.println(manager.getTask(1));
        //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
        manager.updateSubtask(subtask02);
        //Удаление по идентификатору.
        manager.removeTask(1);
        //Получение списка всех подзадач определённого эпика.
        manager.getEpicSubtasks(3);

        manager.removeSubtasks(5);
        System.out.println(manager.getEpic(3));
        manager.getEpicSubtasks(3);
        //Удаление всех задач
//        manager.deleteAllTasks();
//        manager.deleteAllSubtasks();
//        manager.deleteAllEpics();
    }
}
