package model.service.historyManager;

import model.tasks.Task;

import java.util.List;

public interface HistoryManager {

    //Получение истории просмотров
    List<Task> getHistory();

    //Добавление задачи в историю
    void addTaskToHistory(Task anyTask);

    //Очистка истории
    void clearHistory();
}
