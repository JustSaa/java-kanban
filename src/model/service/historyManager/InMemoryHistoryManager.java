package model.service.historyManager;

import model.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> historyOfQuery;
    private final Map<Integer, Node<Task>> historyOfTasks;

    public InMemoryHistoryManager() {
        this.historyOfQuery = new CustomLinkedList<>();
        this.historyOfTasks = new HashMap<>();
    }

    //Получение истории просмотров
    @Override
    public List<Task> getHistory() {
        return this.historyOfQuery.getTasks();
    }

    //Добавление задачи в историю
    @Override
    public void addTaskToHistory(Task anyTask) {
        Node<Task> node = historyOfQuery.linkLast(anyTask);
        int idTask = anyTask.getId();
        if (historyOfTasks.containsKey(idTask)) {
            historyOfQuery.removeNode(historyOfTasks.get(idTask));
        }
        historyOfTasks.put(idTask, node);
    }

    //Очистка истории
    @Override
    public void clearHistory() {
        historyOfTasks.clear();
    }

    @Override
    public void remove(int id) {
        historyOfQuery.removeNode(historyOfTasks.get(id));
        historyOfTasks.remove(id);
    }
}
