package model.model;

import model.enums.StateOfTasks;

import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description, StateOfTasks status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{title='" + this.title
                + "', description='" + this.description
                + "', status='" + this.status
                + "', id='" + this.id
                + "', epicId='" + this.epicId + "'";
    }

    public int getEpicId() {
        return this.epicId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Subtask subtask = (Subtask) obj;
        return (Objects.equals(this.epicId, subtask.epicId)
                && Objects.equals(this.id, subtask.id)
                && Objects.equals(this.title, subtask.title)
                && Objects.equals(this.description, subtask.description)
                && Objects.equals(this.status, subtask.status));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.epicId, this.id, this.title, this.description, this.status);
    }
}
