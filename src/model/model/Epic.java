package model.model;

import model.enums.Status;
import model.enums.TaskType;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtasks = new ArrayList<>();

    public Epic(String title, Status status, String description) {
        super(title, status, description);
        this.type = TaskType.EPIC;
    }

    public Epic(int id, String title, Status status, String description) {
        super(id, title, status, description);
        this.type = TaskType.EPIC;
    }

    public Epic(String title, String description) {
        super(title, description);
        this.type=TaskType.EPIC;
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasks = subtasks;
    }

    public void setSubtask(Subtask subtask) {
        this.subtasks.add(subtask.id);
    }

    public void removeSubtask(int id) {
        for (int i = 0; i < this.subtasks.size(); i++) {
            if (id == this.subtasks.get(i)) {
                this.subtasks.remove(i);
            }
        }
    }

    public int getEpicId() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.id + ","
                + this.type + ","
                + this.title + ","
                + this.status + ","
                + this.description;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Epic epic = (Epic) obj;
        return (Objects.equals(this.subtasks, epic.subtasks)
                && Objects.equals(this.id, epic.id)
                && Objects.equals(this.title, epic.title)
                && Objects.equals(this.description, epic.description)
                && Objects.equals(this.status, epic.status)
                && Objects.equals(this.type, epic.type));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.subtasks, this.id, this.title, this.description, this.status, this.type);
    }

}
