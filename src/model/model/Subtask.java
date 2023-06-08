package model.model;

import model.enums.Status;
import model.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static model.utils.Formatter.formatter;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String title, Status status, String description, int epicId) {
        super(title, status, description);
        this.epicId = epicId;
    }

    public Subtask(int id, String title, Status status, String description, int epicId) {
        super(id, title, status, description);
        this.epicId = epicId;
    }

    public Subtask(String title, Status status, String description, int epicId, LocalDateTime startTime, long duration) {
        super(title, status, description, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(int id, String title, Status status, String description, int epicId, LocalDateTime startTime, long duration) {
        super(id, title, status, description, startTime, duration);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.id).append(",")
                .append(getClass().getSimpleName()).append(",")
                .append(this.title).append(",")
                .append(this.status).append(",")
                .append(this.description);

        if (startTime != null) {
            builder.append(",").append(this.startTime.format(formatter))
                    .append(",").append(this.duration);
        }

        getEndTime().ifPresent(endTime ->
                builder.append(",").append(endTime.format(formatter)));
        builder.append(",").append(this.epicId);
        return builder.toString();
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
                && Objects.equals(this.startTime, subtask.startTime)
                && Objects.equals(this.duration, subtask.duration));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.epicId, this.id, this.title,
                this.description, this.status, getClass(), this.startTime, this.duration);
    }
}
