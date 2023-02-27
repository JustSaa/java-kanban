package model.service.taskManagers;

import model.tasks.Epic;
import model.tasks.Subtask;
import model.tasks.Task;

import java.util.List;

public interface TaskManager {

    //Создание задачи
    void createTask(Task task);

    //Вывод всех задач
    void getAllTasks();

    //Создание эпика
    void createEpic(Epic epic);

    //Вывод всех эпиков и его подзадач
    void getAllEpics();

    //Вывод всех подзадач
    void getAllSubtasks();

    //Создание подзадачи
    void createSubtask(Subtask subtask);

    //Удаление всех задач
    void deleteAllTasks();

    //Удаление всех эпиков
    void deleteAllEpics();

    //Удаление все подзадач
    void deleteAllSubtasks();

    //Генерация идентификатора
    int generateId();

    //Получение задачи по идентификатору
    void getTask(int id);

    //Получение эпика по идентификатору
    void getEpic(int id);

    //Получение подзадачи по идентификатору
    void getSubtask(int id);

    //Получение всех подзадач по идентификатору эпика
    void getEpicSubtasks(int id);

    //Обновление задачи по сущности
    void updateTask(Task updateTask);

    //Обновление задачи по сущности + id
    void updateTask(Task updateTask, int id);

    //Обновление эпика по сущности
    void updateEpic(Epic updateEpic);

    //Обновление эпика по сущности + id
    void updateEpic(Epic updateEpic, int id);

    //Обновление подзадачи по сущности
    void updateSubtask(Subtask updateSubtask);

    //Обновление подзадачи по сущности + id
    void updateSubtask(Subtask updateSubtask, int id);

    //Удаление задачи по идентификатору
    void removeTask(int id);

    //Удаление эпика по идентификатору
    void removeEpic(int id);

    //Удаление подзадачи по идентификатору
    void removeSubtasks(int id);

    // Получение истории
    List<Task> getHistory();
}
