package model.model;

import model.enums.StateOfTasks;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtasks = new ArrayList<>();

    public Epic(String title, String description, StateOfTasks status) {
        super(title, description, status);
    }

    public Epic(String title, String description) {
        super(title, description);
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
        return "Epic{title='" + this.title
                + "', description='" + this.description
                + "', status='" + this.status
                + "', id='" + this.id
                + "', subtasksId='" + this.subtasks;
    }

    public void setEpicStatus(HashMap<Integer, Subtask> subs) {
        int isNew = 0;
        int isDone = 0;
        for (int id : getSubtasks()) {
            if (subs.get(id) == null || subs.get(id).status.equals(StateOfTasks.NEW)) {
                isNew++;
            } else if (subs.get(id).status.equals(StateOfTasks.DONE)) {
                isDone++;
            }
        }
        if (subtasks.size() == isNew) {
            this.status = StateOfTasks.NEW;
        } else if (subtasks.size() == isDone) {
            this.status = StateOfTasks.DONE;
        } else {
            this.status = StateOfTasks.IN_PROGRESS;
        }
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
                && Objects.equals(this.status, epic.status));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.subtasks, this.id, this.title, this.description, this.status);
    }

}
