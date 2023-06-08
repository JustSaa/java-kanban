package model.model;

import model.enums.Status;
import model.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static model.utils.Formatter.formatter;

public class Task {
    protected String title;
    protected String description;
    protected Status status;
    protected int id;
    protected LocalDateTime startTime;
    protected long duration;

    public Task(String title, Status status, String description) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime=LocalDateTime.now();
        this.duration=0;
    }

    public Task(String title, Status status, String description, LocalDateTime startTime, long duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, String title, Status status, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime=LocalDateTime.now();
        this.duration=0;
    }

    public Task(int id, String title, Status status, String description, LocalDateTime startTime, long duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.startTime=LocalDateTime.now();
        this.duration=0;
    }

    public Task(String title, String description, LocalDateTime startTime, long duration) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Optional<LocalDateTime> getEndTime() {
        if (getDuration() != 0 && getStartTime() != null) {
            return Optional.of(getStartTime().plusMinutes(getDuration()));
        }
        return Optional.empty();
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

        return builder.toString();
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
                && Objects.equals(this.startTime, task.startTime)
                && Objects.equals(this.duration, task.duration));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.description,
                this.status, getClass(), this.duration, this.startTime);
    }
}
