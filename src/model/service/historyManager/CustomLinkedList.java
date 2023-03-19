package model.service.historyManager;

import java.util.ArrayList;
import java.util.List;

public class CustomLinkedList<Task> {
    private Node<Task> first;
    private Node<Task> last;

    public Node<Task> linkLast(Task someTask) {
        Node<Task> newNode = new Node<>(someTask, last, null);
        if (last == null) {
            first = newNode;
        } else {
            last.next = newNode;
        }
        last = newNode;
        return newNode;
    }

    public List<Task> getTasks() {
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
