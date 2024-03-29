package model.service.historyManager;

import model.model.Task;

import java.util.List;

public interface HistoryManager {

    //Получение истории просмотров
    List<Task> getHistory();

    //Добавление задачи в историю
    void addToHistory(Task task);

    //Очистка истории
    void clearHistory();

    //Удаление задачи из просмотра
    void remove(int id);
}
