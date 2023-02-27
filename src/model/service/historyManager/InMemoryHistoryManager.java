package model.service.historyManager;

import model.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> historyOfTasks;

    public InMemoryHistoryManager() {
        this.historyOfTasks = new ArrayList<>();
    }

    //Получение истории просмотров
    @Override
    public List<Task> getHistory() {
        return this.historyOfTasks;
    }

    //Добавление задачи в историю
    @Override
    public void addTaskToHistory(Task anyTask) {
        if (historyOfTasks.size() > 9) {
            historyOfTasks.remove(0);
            historyOfTasks.add(anyTask);
        } else {
            historyOfTasks.add(anyTask);
        }
    }

    //Очистка истории
    @Override
    public void clearHistory() {
        historyOfTasks.clear();
    }
}
