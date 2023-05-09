package model.model;

import model.enums.Status;
import model.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class Epic extends Task {
    private ArrayList<Integer> subtasks = new ArrayList<>();
    private LocalDateTime endTime;

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
        this.type = TaskType.EPIC;
    }

    public Epic(String title, Status status, String description, LocalDateTime startTime, Duration duration) {
        super(title, status, description, startTime, duration);
        this.type = TaskType.EPIC;
    }

    public Epic(int id, String title, Status status, String description, LocalDateTime startTime, Duration duration) {
        super(id, title, status, description, startTime, duration);
        this.type = TaskType.EPIC;
    }

    public Epic(String title, String description, LocalDateTime startTime, Duration duration) {
        super(title, description, startTime, duration);
        this.type = TaskType.EPIC;
    }

    @Override
    public Optional<LocalDateTime> getEndTime() {
        if (endTime == null) {
            return Optional.empty();
        } else {
            return Optional.of(endTime);
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
                + this.description
                + this.startTime + ","
                + this.duration + ","
                + this.endTime;
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
                && Objects.equals(this.type, epic.type)
                && Objects.equals(this.duration, epic.duration)
                && Objects.equals(this.startTime, epic.startTime)
                && Objects.equals(this.endTime, epic.endTime));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.subtasks, this.id, this.title, this.description,
                this.status, this.type, this.startTime, this.duration, this.endTime);
    }

}
