package model.service.taskManagers;

import model.model.Epic;
import model.model.Subtask;
import model.model.Task;
import model.service.historyManager.HistoryManager;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    //Создание задачи
    void createTask(Task task);

    //Вывод всех задач
    List<Task> getTasks();

    //Создание эпика
    void createEpic(Epic epic);

    //Вывод всех эпиков и его подзадач
    List<Epic> getEpics();

    //Вывод всех подзадач
    List<Subtask> getSubtasks();

    //Создание подзадачи
    void createSubtask(Subtask subtask);

    //Удаление всех задач
    void deleteTasks();

    //Удаление всех эпиков
    void deleteEpics();

    //Удаление все подзадач
    void deleteSubtasks();

    //Получение задачи по идентификатору
    Task getTask(int id);

    //Получение эпика по идентификатору
    Epic getEpic(int id);

    //Получение подзадачи по идентификатору
    Subtask getSubtask(int id);

    //Получение всех подзадач по идентификатору эпика
    List<Subtask> getEpicSubtasks(int id);

    //Обновление задачи по сущности
    void updateTask(Task updateTask);

    //Обновление эпика по сущности
    void updateEpic(Epic updateEpic);

    //Обновление подзадачи по сущности
    void updateSubtask(Subtask updateSubtask);

    //Удаление задачи по идентификатору
    void removeTask(int id);

    //Удаление эпика по идентификатору
    void removeEpic(int id);

    //Удаление подзадачи по идентификатору
    void removeSubtasks(int id);

    // Получение истории
    List<Task> getHistory();
}
