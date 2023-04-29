package model.model;

import model.enums.Status;
import model.enums.TaskType;

import java.util.Objects;

public class Task {
    protected String title;
    protected String description;
    protected Status status;
    protected int id;

    protected TaskType type;

    public Task(String title, Status status, String description) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = TaskType.TASK;
    }

    public Task(int id, String title, Status status, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = TaskType.TASK;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.type = TaskType.TASK;
    }

    public String getTitle() {
        return this.title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getType() {
        return this.type;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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
        Task task = (Task) obj;
        return (Objects.equals(this.id, task.id)
                && Objects.equals(this.title, task.title)
                && Objects.equals(this.description, task.description)
                && Objects.equals(this.status, task.status)
                && Objects.equals(this.type, task.type));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.description, this.status, this.type);
    }
}
