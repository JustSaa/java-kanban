package model.model;

import model.enums.Status;

import java.util.Objects;

public class Task {
    protected String title;
    protected String description;
    protected Status status;
    protected int id;

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
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

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Task{title='" + this.title
                + "', description='" + this.description
                + "', status='" + this.status
                + "', id='" + this.id + "'";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return (Objects.equals(this.id, task.id)
                && Objects.equals(this.title, task.title)
                && Objects.equals(this.description, task.description)
                && Objects.equals(this.status, task.status));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.description, this.status);
    }
}
