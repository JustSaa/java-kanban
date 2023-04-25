package model.model;

import model.enums.Status;
import model.enums.TaskType;

import java.util.Objects;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String title, Status status, String description, int epicId) {
        super(title, status, description);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    public Subtask(int id, String title, Status status, String description, int epicId) {
        super(id, title, status, description);
        this.epicId = epicId;
        this.type = TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return this.id + ","
                + this.type + ","
                + this.title + ","
                + this.status + ","
                + this.description + ","
                + this.epicId + ","
                + this.type;
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
                && Objects.equals(this.status, subtask.status)
                && Objects.equals(this.type, subtask.type));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.epicId, this.id, this.title, this.description, this.status, this.type);
    }
}
