package model.service.historyManager;

import model.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node<Task>> historyOfTasks = new HashMap<>();
    private Node<Task> first;
    private Node<Task> last;

    //Добавление задачи в историю
    @Override
    public void addToHistory(Task task) {
        if (task == null) {
            return;
        }
        final int id = task.getId();

        if (historyOfTasks.containsKey(id)) {
            removeNode(historyOfTasks.get(id));
        }
        linkLast(task);
        historyOfTasks.put(id, last);
    }

    //Очистка истории
    @Override
    public void clearHistory() {
        historyOfTasks.clear();
    }

    @Override
    public void remove(int id) {
        removeNode(historyOfTasks.get(id));
        historyOfTasks.remove(id);
    }

    //Реализация для своего LinkedList
    public void linkLast(Task someTask) {
        Node<Task> newNode = new Node<>(someTask, last, null);

        if (last == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;
    }

    //Получение истории просмотров
    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node<Task> nodeTask = first;

        while (nodeTask != null) {
            history.add(nodeTask.item);
            nodeTask = nodeTask.next;
        }
        return history;
    }

    public void removeNode(Node<Task> nodeTask) {
        if (nodeTask == null) {
            return;
        }
        if (nodeTask.equals(first)) {
            first = nodeTask.next;
            if (nodeTask.next != null) {
                nodeTask.next.prev = null;
            }
        } else {
            nodeTask.prev.next = nodeTask.next;
            if (nodeTask.next != null) {
                nodeTask.next.prev = nodeTask.prev;
            }
        }
    }
}
